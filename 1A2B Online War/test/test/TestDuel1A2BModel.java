package test; 
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gamecore.entity.Player;
import gamecore.model.gamemodels.a1b2.Duel1A2BModel;
import gamecore.model.gamemodels.a1b2.GuessRecord;
import gamecore.model.gamemodels.a1b2.GuessResult;
import gamecore.model.gamemodels.a1b2.NumberNotValidException;

public class TestDuel1A2BModel {
	Player host = new Player("Host");  // the host assumed to be the winner
	Player player =  new Player("Player");
	Duel1A2BModel game = new Duel1A2BModel(host, player);
	
	@Before
	public void setup(){
		host.initId();
		player.initId();
		game = new Duel1A2BModel(host, player);
	}
	
	@Test(expected = NumberNotValidException.class)
	public void numberInvalidTest() throws NumberNotValidException {
		game.commitPlayerAnswer(host, "5512");
	}
	
	@Test 
	public void test() throws Exception{
		game.commitPlayerAnswer(host, "5678");
		game.commitPlayerAnswer(player, "1234");
		assertEquals(game.getPlayerBarModel(host).getAnswer(), "5678");
		assertEquals(game.getPlayerBarModel(player).getAnswer(), "1234");
		
		GuessResult[] resultsHost = {new GuessResult(0, 3), new GuessResult(0, 0), new GuessResult(0, 4), 
				new GuessResult(2, 2), new GuessResult(4, 0)};
		
		game.guess(host, "0123");  //0A3B
		game.guess(host, "5678");  //0A0B
		game.guess(host, "2341");  //0A4B
		game.guess(host, "1243");  //2A2B
		game.guess(host, "1234");  //4A0B

		assertTrue(game.isGameOver());
		
		GuessResult[] resultsPlayer = {new GuessResult(0, 0), new GuessResult(4, 0)};
		game.guess(player, "1234");  //0A0B
		game.guess(player, "5678");  //4A0B, but the host guess the answer first, so the host should win.
		
		List<GuessRecord> recordsHost = game.getGuessRecords(host);
		List<GuessRecord> recordsPlayer = game.getGuessRecords(player);
		
		for(int i = 0 ; i < recordsHost.size() ; i ++)
			assertEquals(recordsHost.get(i).getResult(), resultsHost[i]);
		for(int i = 0 ; i < recordsPlayer.size() ; i ++)
			assertEquals(recordsPlayer.get(i).getResult(), resultsPlayer[i]);
		
		assertEquals(host, game.getWinner());
	}
}
