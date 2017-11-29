package container.eventhandler.handlers;

import com.google.gson.Gson;

import container.base.Client;
import container.eventhandler.GameEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;

/**
 * Abstract handler helping subclasses converting the input JSON into the object.
 * The subclass should implement getDataClass(), the returned class type will determine the gson converted object type.
 * @param <In> the input source type.
 * @param <Out> the output source type.
 */
public abstract class GsonEventHandler<In, Out> extends GameEventHandler<In, Out>{
	protected static Gson gson = new Gson();
	public GsonEventHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}
	
	protected abstract Class<In> getDataClass();

	@Override
	protected In parseData(String data) {
		return GsonEventHandler.gson.fromJson(data, getDataClass());
	}
	
	@Override
	protected String dataToString(Object data) {
		return gson.toJson(data);
	}
	

}
