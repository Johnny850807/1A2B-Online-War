package main;

import Util.Input;

public class SignInView extends View{
	
	@Override
	public void onCreate() {
		//TODO 資源生成
				
	}

	@Override
	public void onRecycleActions() {
		//TODO 登入測試
		String name = Input.next("請輸入暱稱: ");
		
	}

	@Override
	public void onDestroy() {
		//TODO 資源釋放
	}
	
}
