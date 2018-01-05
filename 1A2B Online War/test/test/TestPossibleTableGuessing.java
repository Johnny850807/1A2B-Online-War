package test;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import gamecore.model.MockLogger;
import gamecore.model.games.a1b2.A1B2NumberValidator;
import gamecore.model.games.a1b2.GuessRecord;
import gamecore.model.games.a1b2.NumberNotValidException;
import gamecore.model.games.a1b2.PossibleTableGuessing;
import gamecore.model.games.a1b2.boss.OnePunchBoss;
import gamecore.model.games.a1b2.boss.TestingBoss;
import gamefactory.GameOnlineReleaseFactory;
import utils.RandomString;

@RunWith(Parameterized.class)
public class TestPossibleTableGuessing {
	private PossibleTableGuessing algorithm;
	private String answer;
	
	@Before
	public void setup(){
		algorithm = new PossibleTableGuessing();
	}
	
	public TestPossibleTableGuessing(String answer) throws NumberNotValidException {
		A1B2NumberValidator.validateNumber(answer);
		this.answer = answer;
	}
	
	@Test
	public void testIfAllGuessingValid() throws NumberNotValidException {
		for (int i = 0 ; i < 1000 ; i ++)
			A1B2NumberValidator.validateNumber(algorithm.nextGuess());
	}
	
	@Test
	public void testIfTheAlgorithmCanGuessCorrectlyInSevenTimes() throws Exception{
		for (int i = 0 ; i < 7 ; i ++)
		{
			String guess = algorithm.nextGuess();
			GuessRecord guessRecord = new GuessRecord(guess, A1B2NumberValidator.getGuessResult(answer, guess));
			if (guessRecord.getA() == 4)
				return;  //success
			algorithm.feedRecord(guessRecord);
		}
		Assert.fail();  
	}
	
	@Parameterized.Parameters
	public static Collection primeNumbers() {
		return Arrays.asList(new Object[][] {
			{"1234"},
			{"5678"},
			{"5640"},
			{"1642"},
			{"0159"},
			{"5764"},
			{"5301"},
			{"8745"},
			{"1570"},
			{"6485"},
			{"2304"},
			{"5264"},
			{"1205"},
			{"9854"},
			{"2543"},
			{"0765"},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)},
			{RandomString.nextNonDuplicatedNumber(4)}
	      });
	}
}
