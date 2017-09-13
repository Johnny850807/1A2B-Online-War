package main;

import Util.Input;

public class SignInView2 extends View{
	
	@Override
	public void onCreate() {
		//TODO 資源生成
				
	}

	@Override
	public void onRecycleActions() {
		//TODO 登入測試
		String name = Input.next("Input your name2: ");
		if (name.contains("end"))
			finish();
	}

	@Override
	public void onDestroy() {
		//TODO 資源釋放
	}

	@Override
	public String getViewName() {
		return "Sign-In View";
	}
	
}
