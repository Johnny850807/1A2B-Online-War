package gamecore.model.gamemodels.a1b2;

public final class A1B2NumberValidator {
	private static final char UNASSIGNED = '-';
	
	/**
	 * validate the number fit to the 1A2B guess or answer. 
	 * @param number the number to be validated.
	 * @throws NumberNotValidException if the number is not in 4 length, any number is not a digit or duplicated digit.
	 */
	public static void validateNumber(String number) throws NumberNotValidException{
		number = number.trim();
		char[] duplicatedCheck = getCheckChars(); //for ASCII 48 ~ 57 letter duplicated checking.
		
		if (number.length() != 4)
			throw new NumberNotValidException("Number length should be 4.");
		for (int i = 0 ; i < number.length() ; i ++)
		{
			char num = number.charAt(i);
			if (!Character.isDigit(num))
				throw new NumberNotValidException("Number cannot be an alphabet.");
			int index = num - 48;
			if (duplicatedCheck[index] == UNASSIGNED)
				duplicatedCheck[index] = num;
			else
				throw new NumberNotValidException("Digit duplicated.");
		}
	}
	
	private static final char[] getCheckChars(){
		char[] checkchars = new char[10];
		for (int i = 0 ; i < 10 ; i ++)
			checkchars[i] = UNASSIGNED;
		return checkchars;
	}
	
	public static GuessResult getGuessResult(String answer, String guess){
		int a = 0, b = 0;
		for (int i = 0 ; i < 4 ; i ++)
		{
			char answerNum = answer.charAt(i);
			for (int j = 0 ; j < 4 ; j ++)
			{
				char guessNum = guess.charAt(j);
				if (answerNum == guessNum && i == j)
					a ++;
				else if (answerNum == guessNum)
					b ++;
			}
		}
		return new GuessResult(a, b);
	}
}
