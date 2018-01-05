package container.waterbot.brain;

import container.core.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import container.waterbot.Brain;
import container.waterbot.WaterBot;
import gamecore.entity.Player;
import gamecore.model.ClientStatus;
import gamecore.model.ContentModel;
import gamecore.model.RoomStatus;
import gamecore.model.games.a1b2.core.A1B2NumberValidator;
import gamecore.model.games.a1b2.duel.core.Duel1A2BPlayerBarModel;
import utils.MyGson;
import utils.RandomString;

import static container.core.Constants.Events.Chat.*;
import static container.core.Constants.Events.Games.*;
import static container.core.Constants.Events.Games.Duel1A2B.*;
import static container.core.Constants.Events.InRoom.*;
import static container.core.Constants.Events.RoomList.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.reflect.TypeToken;

public class Duel1A2BGameBrain extends BaseChatChainBrain{
	private int guessedTime = 0;
	private String opponentAnswer;
	public Duel1A2BGameBrain(Brain next, ProtocolFactory protocolFactory) {
		super(next, protocolFactory);
	}
	
	@Override
	protected void onReceiveSuccessProtocol(WaterBot waterBot, Protocol protocol, Client client) {
		switch (protocol.getEvent()) {
		case GAMESTARTED:
			broadcastSetAnswer(waterBot, client);
			break;
		case SET_ANSWER:
			sendMessageRequest(waterBot, waterBot.getMemory().getRoom(), client, "我設定好答案囉！");
			return;
		case GUESSING_STARTED:
			guessedTime = 0;
			opponentAnswer = "";
			broadcastRandomGuess(waterBot, client);
			return;
		case ONE_ROUND_OVER:
			if (++guessedTime == 1)
				parseOpponentAnswer(waterBot, protocol);
			broadcastRandomGuessAfter2Seconds(waterBot, client);
			return;
		case GAMEOVER:
			waterBot.getMe().setUserStatus(ClientStatus.signedIn);
			waterBot.getGameRoom().setRoomStatus(RoomStatus.waiting);
			break;
		default:
			break;
		}
		
		nextIfNotNull(waterBot, protocol, client);
	}

	private void parseOpponentAnswer(WaterBot waterBot, Protocol protocol) {
		Player me = waterBot.getMe();
		List<Duel1A2BPlayerBarModel> bars = MyGson.parseDuel1A2BPlayerBarModels(protocol.getData());
		Duel1A2BPlayerBarModel opponentModel = bars.get(0).getPlayerId().equals(me.getId()) ? 
				bars.get(1) : bars.get(0);
		this.opponentAnswer = opponentModel.getAnswer();
	}

	private void broadcastSetAnswer(WaterBot waterBot, Client client){
		Protocol protocol = protocolFactory.createProtocol(SET_ANSWER, REQUEST,
				gson.toJson(new ContentModel(waterBot.getMe().getId(), 
						waterBot.getGameRoom().getId(), RandomString.nextNonDuplicatedNumber(4))));
		client.broadcast(protocol);
	}
	
	private void broadcastRandomGuess(WaterBot waterBot, Client client){
		String random = RandomString.nextNonDuplicatedNumber(4);
		ContentModel model = new ContentModel(waterBot.getMemory().getMe().getId(), 
				waterBot.getMemory().getRoom().getId(), random);
		Protocol protocol = protocolFactory.createProtocol(GUESS, REQUEST, gson.toJson(model));
		client.broadcast(protocol);
	}
	
	private void broadcastAbsolutelyCorrectGuess(WaterBot waterBot, Client client){
		ContentModel model = new ContentModel(waterBot.getMemory().getMe().getId(), 
				waterBot.getMemory().getRoom().getId(), opponentAnswer);
		Protocol protocol = protocolFactory.createProtocol(GUESS, REQUEST, gson.toJson(model));
		client.broadcast(protocol);
	}
	
	private void broadcastRandomGuessAfter2Seconds(WaterBot waterBot, Client client){
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				if (waterBot.getGameRoom() != null && waterBot.getGameRoom().getRoomStatus() == RoomStatus.gamestarted)
				{
					if (guessedTime < 2)
						broadcastRandomGuess(waterBot, client);
					else
						broadcastAbsolutelyCorrectGuess(waterBot, client);
				}
			}
		}, 2000);
	}
}
