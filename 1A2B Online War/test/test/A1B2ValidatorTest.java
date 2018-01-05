package test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import gamecore.model.games.a1b2.core.A1B2NumberValidator;
import gamecore.model.games.a1b2.core.GuessResult;
import gamecore.model.games.a1b2.core.NumberNotValidException;
import junit.framework.Assert;

@RunWith(Parameterized.class)
public class A1B2ValidatorTest {
	private String answer;
	private String inputNumber;
	private boolean valid;
	private Class<? extends Exception> expClazz;
	private GuessResult result;

	public A1B2ValidatorTest(String answer, String inputNumber, boolean valid, Class<? extends Exception> expClazz, GuessResult guessResult) {
		this.answer = answer;
		this.inputNumber = inputNumber;
		this.valid = valid;
		this.expClazz = expClazz;
		this.result = guessResult;
	}


	@Test
	public void test() {
		try{
			A1B2NumberValidator.validateNumber(inputNumber);
			if(!valid)  // expect an exception thrown if not valid.
				fail();
			GuessResult result = A1B2NumberValidator.getGuessResult(answer, inputNumber);
			assertEquals(this.result, result);
		}catch (NumberNotValidException e) {
			if(valid)  // expect no exception thrown if valid.
				fail();
			assertEquals(expClazz, e.getClass());
		}catch (Exception e) {
			fail();
		}
	}

	
	@Parameterized.Parameters
	public static Collection primeNumbers() {
		return Arrays.asList(new Object[][] {
			//answer, input, valid, exception, expect result
			{"1234", "1234", true, null, new GuessResult(4, 0)},
			{"1234", "1230", true, null, new GuessResult(3, 0)},
			{"1234", "4321", true, null, new GuessResult(0, 4)},
			{"1234", "0123", true, null, new GuessResult(0, 3)},
			{"1234", "5678", true, null, new GuessResult(0, 0)},
			{"1234", "8735", true, null, new GuessResult(1, 0)},
			{"1234", "4567", true, null, new GuessResult(0, 1)},
			{"1234", "8931", true, null, new GuessResult(1, 1)},

			{"1234", "123", false, NumberNotValidException.class, null},
			{"1234", "12345", false, NumberNotValidException.class, null},
			{"1234", "1233", false, NumberNotValidException.class, null},
			{"1234", "a123", false, NumberNotValidException.class, null},
			{"1234", "123b", false, NumberNotValidException.class, null},
			{"1234", "ab01", false, NumberNotValidException.class, null},
			{"1234", "4565", false, NumberNotValidException.class, null},
			{"1234", "987 6", false, NumberNotValidException.class, null},
			{"1234", "4\n567", false, NumberNotValidException.class, null}
		});
	}
}
