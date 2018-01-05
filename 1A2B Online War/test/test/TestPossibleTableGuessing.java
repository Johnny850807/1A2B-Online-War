package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import gamecore.model.games.a1b2.core.A1B2NumberValidator;
import gamecore.model.games.a1b2.core.GuessRecord;
import gamecore.model.games.a1b2.core.NumberNotValidException;
import gamecore.model.games.a1b2.imp.PossibleTableGuessing;
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
	
	//@Test
	public void testIfAllGuessingValid() throws NumberNotValidException {
		for (int i = 0 ; i < 1000 ; i ++)
			A1B2NumberValidator.validateNumber(algorithm.nextGuess());
	}
	
	@Test
	public void testIfTheAlgorithmCanGuessCorrectlyInSevenTimes() throws Exception{
		for (int i = 0 ; i < 8 ; i ++)
		{
			String guess = algorithm.nextGuess();
			GuessRecord guessRecord = new GuessRecord(guess, A1B2NumberValidator.getGuessResult(answer, guess));
			if (guessRecord.getA() == 4)
				return;  //success
			algorithm.feedRecord(guessRecord);
		}
		System.out.println("Failed with the answer: " + answer);
		Assert.fail();  
	}
	
	@Parameterized.Parameters
	public static Collection primeNumbers() {
		Object[][] answers = new Object[1000][1];
		for (int i = 0 ; i < 1000 ; i ++)
			answers[i] = new Object[]{RandomString.nextNonDuplicatedNumber(4)};
		
		return Arrays.asList(answers);
	}
}
