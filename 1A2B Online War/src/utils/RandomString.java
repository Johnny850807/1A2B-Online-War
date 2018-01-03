package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomString {
	public static String next(int length){
		Random random = new Random();
		StringBuilder strb = new StringBuilder();
		for (int i = 0 ; i < length ; i ++)
			strb.append((char)(random.nextInt(26) + 97));  //97~122
		return strb.toString();
	}
	
	public static String nextNonDuplicatedNumber(int length){
		return nextNonDuplicatedNumberWith(length, new ArrayList<>());
	}
	
	public static String nextNonDuplicatedNumberWith(int length, Integer[] with){
		return nextNonDuplicatedNumberWith(length, Arrays.asList(with));
	}
	
	public static String nextNonDuplicatedNumberWith(int length, List<Integer> with){
		StringBuilder strb = new StringBuilder();
		List<Integer> nums = new ArrayList<>();
		for (int i = 0 ; i < 10 ; i ++)
			nums.add(i);
		for (int num : with)
			nums.remove(new Integer(num));
		Collections.shuffle(nums);
		List<Integer> fromRandom = nums.subList(0, length);
		fromRandom.addAll(with);
		Collections.shuffle(fromRandom);
		for(int num : fromRandom)
			strb.append(num);
		return strb.toString();
	}
	
}
