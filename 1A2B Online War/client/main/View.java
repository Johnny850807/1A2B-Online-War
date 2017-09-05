package main;

import java.util.Stack;

public abstract class View {
	private static Stack<View> viewStack = new Stack<>();
	
	public void start() {
		viewStack.push(this);
		onCreate();
		onStart();
	}
	
	public abstract void onCreate();
	
	public abstract void onRecycleActions();
	
	public void onRestart() {
		
	}
	
	public void onStart() {
		while(!viewStack.isEmpty() && isAlive())
			onRecycleActions();
	}
	
	
	public void finish() {
		viewStack.pop();
		onDestroy();
		
		if (viewStack.isEmpty())
			System.out.println("´ú¸Õµ²§ô");
		else
			viewStack.peek().onRestart();
	}
	
	public abstract void onDestroy();
	
	public boolean isAlive() {
		return viewStack.lastElement() == this;
	}
}
