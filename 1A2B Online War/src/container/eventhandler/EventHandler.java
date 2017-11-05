package container.eventhandler;

import container.eventhandler.GameEventHandler.Response;
import container.protocol.Protocol;

public interface EventHandler {
	void handle();
	void setOnRespondingListener(OnRespondingListener onRespondingListener);
	Protocol request();
	
	public interface OnRespondingListener{
		void onErrorResponding(Protocol responseProtocol);
		void onSuccessResponding(Protocol responseProtocol);
	}
}
