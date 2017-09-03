package View;

import java.util.Stack;

import com.sun.org.apache.bcel.internal.generic.NEW;

public abstract class View{
	private static Stack<View> views = new Stack<>();
	
	public final void start(){
		views.push(this);
		onCreate();
	}
	
	protected abstract void onCreate();
	protected abstract void onResume();
	
	protected void finish(){
		views.pop(); // pop out the currect view 
		onDestroy();
		views.lastElement().onResume(); // and pick out the next view to resume
	}
	
	protected void onDestroy(){
		// hook method
	}
	
	public boolean isAlive(){
		return views.contains(this);
	}
}
