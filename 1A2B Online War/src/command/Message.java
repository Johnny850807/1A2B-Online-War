package command;

import gamecore.entity.Entity;
import utils.JsonConverter;

public class Message<TData extends Entity> {
	private String event;
	private TData data;
	
	public String getEvent(){
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public TData getData() {
		return data;
	}
	public void setData(TData data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return JsonConverter.messageToJson(this);
	}
	
}
