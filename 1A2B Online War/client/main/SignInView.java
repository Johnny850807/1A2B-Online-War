package main;

import Util.Input;

public class SignInView extends View{
	
	@Override
	public void onCreate() {
		//TODO �귽�ͦ�
				
	}

	@Override
	public void onRecycleActions() {
		//TODO �n�J����
		String name = Input.next("Input your name: ");
		
	}

	@Override
	public void onDestroy() {
		//TODO �귽����
	}

	@Override
	public String getViewName() {
		return "Sign-In View";
	}
	
}
