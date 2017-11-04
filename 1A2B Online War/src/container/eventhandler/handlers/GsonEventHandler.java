package container.eventhandler.handlers;

import com.google.gson.Gson;

import container.Client;
import container.eventhandler.GameEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;

public abstract class GsonEventHandler<T> extends GameEventHandler<T>{
	private static Gson gson = new Gson();
	private Class<T> clazz;
	public GsonEventHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
		this.clazz = getDataClass();
	}
	
	protected abstract Class<T> getDataClass();

	@Override
	protected T parseData(String data) {
		return GsonEventHandler.gson.fromJson(data, clazz);
	}
	
	@Override
	protected String dataToString(Object data) {
		return gson.toJson(data);
	}
	

}
