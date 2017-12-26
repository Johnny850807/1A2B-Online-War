package utils;

import java.util.Random;

public class RandomString {
	public static String next(int length){
		Random random = new Random();
		StringBuilder strb = new StringBuilder();
		for (int i = 0 ; i < length ; i ++)
			strb.append((char)(random.nextInt(26) + 97));  //97~122
		return strb.toString();
	}
}
