package communication;

import gamecore.entity.Entity;
import utils.JsonConverter;

public class Message<TData extends Entity> {
	private Event event;
	private Status status = Status.none;
	private TData data;
	
	public Message(Event event, Status status, TData data) {
		this.event = event;
		this.status = status;
		this.data = data;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public Event getEvent(){
		return event;
	}
	public void setEvent(Event event) {
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
