package container.waterbot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import container.base.Client;
import container.waterbot.brain.RoomListBrain;
import container.waterbot.brain.SignBrain;
import gamefactory.GameFactory;
import gamefactory.GameOnlineReleaseFactory;

public class WaterBotServer {
	public static void main(String[] argv){
		if (argv.length > 1)
			System.out.println("The argument should be only 1 for the bot amount.");
		System.setProperty("log4j.configurationFile","configuration.xml");
		GameFactory gameFactory = new GameOnlineReleaseFactory();
		int amount = /*Integer.parseInt(argv[0]);*/1;
		List<Thread> botWorkers = new ArrayList<>();
		
		Brain roomlistBrain = new RoomListBrain(null, gameFactory.getProtocolFactory());
		Brain brain = new SignBrain(roomlistBrain, gameFactory.getProtocolFactory());
		
		IntStream.range(0, amount).parallel()
			.forEach(i -> {
				WaterBot bot = new WaterBot(brain);
				Client client = new WbotClient(bot, gameFactory.getProtocolFactory());
				botWorkers.add(new Thread(client));
			});
		
		botWorkers.forEach(w -> w.start());
		System.out.println("The waterbot server runs all the bot threads.");
		botWorkers.forEach(w -> { try {w.join();} catch (InterruptedException e) {} });
		System.out.println("The waterbot server is shutted down.");
	}
}
