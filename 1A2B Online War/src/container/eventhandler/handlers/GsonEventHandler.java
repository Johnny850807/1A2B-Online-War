package container.eventhandler.handlers;

import com.google.gson.Gson;

import container.base.Client;
import container.eventhandler.GameEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;

public abstract class GsonEventHandler<T> extends GameEventHandler<T>{
	protected static Gson gson = new Gson();
	public GsonEventHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}
	
	protected abstract Class<T> getDataClass();

	@Override
	protected T parseData(String data) {
		return GsonEventHandler.gson.fromJson(data, getDataClass());
	}
	
	@Override
	protected String dataToString(Object data) {
		return gson.toJson(data);
	}
	

}
