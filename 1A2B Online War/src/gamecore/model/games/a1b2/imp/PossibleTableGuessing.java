package gamecore.model.games.a1b2.imp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import gamecore.model.games.a1b2.core.A1B2NumberValidator;
import gamecore.model.games.a1b2.core.GuessRecord;
import gamecore.model.games.a1b2.core.GuessResult;
import gamecore.model.games.a1b2.core.GuessStrategy;

/**
 * @author Waterball
 * The 1A2B guessing algorithm which uses a table for storing every possible numbers
 * and filtering each number from each guess record.
 * 
 * This algorithm can produce the correct the answer in at most eight guesses.
 */
public class PossibleTableGuessing implements GuessStrategy{
	private List<String> possibleNumbers = new LinkedList<>();
	
	public PossibleTableGuessing() {
		for (int i = 9876 ; i >= 0123 ; i --)
		{
			String number = padTheNumberWithLeftZeroFilled(i);
			if (A1B2NumberValidator.isNumberValid(number)) 
				possibleNumbers.add(number);
		}
	}
	
	@Override
	public void feedRecord(GuessRecord guessRecord) {
		String guess = guessRecord.getGuess();
		int expectedA = guessRecord.getA();
		int expectedB = guessRecord.getB();
		
		List<String> removeList = new LinkedList<>();
		int size = possibleNumbers.size();
		for (int i = 0 ; i < size ; i ++)
		{
			String num = possibleNumbers.get(i);
			if (!isPossibleNumber(guess, num, expectedA, expectedB))
				removeList.add(num);
		}
		possibleNumbers.removeAll(removeList);
	}

	private boolean isPossibleNumber(String guess, String caseNum, int expectedA, int expectedB){
		int actualA = 0, actualB = 0;
		for (int i = 0 ; i < guess.length() ; i ++ )
			for (int j = 0 ; j < caseNum.length() ; j ++)
				if (guess.charAt(i) == caseNum.charAt(j))
				{
					if (i == j)
						actualA ++;
					else
						actualB ++;
				}
		return actualA == expectedA && actualB == expectedB;
	}
	
	
	@Override
	public String nextGuess() {
		int randIndex = new Random(System.currentTimeMillis()).nextInt(possibleNumbers.size());
		return possibleNumbers.get(randIndex);
	}
	
	/**
	 * make the number with a length less than 4 characters filled with 0 from left.
	 */
	private String padTheNumberWithLeftZeroFilled(int num){
		return String.format("%04d", num);  
	}
	
	public static void main(String[] argv){
		PossibleTableGuessing algorithm = new PossibleTableGuessing();
		Scanner scanner = new Scanner(System.in);
		boolean gameOver = false;
		while(!gameOver)
		{
			String guess = algorithm.nextGuess();
			System.out.println(guess);
			System.out.print("A: ");
			int a = scanner.nextInt();
			System.out.print("B: ");
			int b = scanner.nextInt();
			
			if (a == 4)
				gameOver = true;
			else
			{
				GuessResult result = new GuessResult(a, b);
				algorithm.feedRecord(new GuessRecord(guess, result));
			}
		}
	}
}
