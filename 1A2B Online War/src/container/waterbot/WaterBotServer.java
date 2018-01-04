package container.waterbot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import container.base.Client;
import container.waterbot.brain.Duel1A2BGameBrain;
import container.waterbot.brain.InRoomBrain;
import container.waterbot.brain.RoomListBrain;
import container.waterbot.brain.SignBrain;
import gamefactory.GameFactory;
import gamefactory.GameOnlineReleaseFactory;

public class WaterBotServer {
	private static final GameFactory gameFactory =  new GameOnlineReleaseFactory();
	public static void main(String[] argv){
		if (argv.length > 1)
			System.out.println("The argument should be only 1 for the bot amount.");
		System.setProperty("log4j.configurationFile","configuration.xml");
		int amount = Integer.parseInt("2");
		List<Thread> botWorkers = new ArrayList<>();

		IntStream.range(0, amount).parallel()
			.forEach(i -> {
				WaterBot bot = new WaterBot(createChainBran());
				Client client = new WbotClient(bot, gameFactory.getProtocolFactory());
				botWorkers.add(new Thread(client));
			});
		
		botWorkers.forEach(t -> {
			t.start();
			try {
				Thread.sleep(350);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		System.out.println("The waterbot server runs all the bot threads.");
		botWorkers.forEach(t -> { try {t.join();} catch (InterruptedException e) {} });
		System.out.println("The waterbot server is shutted down.");
	}
	
	private static Brain createChainBran(){
		Brain duel1A2BBrain = new Duel1A2BGameBrain(null, gameFactory.getProtocolFactory());
		Brain inroomBrain = new InRoomBrain(duel1A2BBrain, gameFactory.getProtocolFactory());
		Brain roomlistBrain = new RoomListBrain(inroomBrain, gameFactory.getProtocolFactory());
		Brain signBrain = new SignBrain(roomlistBrain, gameFactory.getProtocolFactory());
		return signBrain;
	}
}
