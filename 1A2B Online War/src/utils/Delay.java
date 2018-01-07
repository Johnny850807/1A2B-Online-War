package utils;

public class Delay {
	public static void sleep(long duration){
		try {Thread.sleep(duration);} catch (InterruptedException e) {}
	}
}
