package command;

import gamecore.entity.Entity;

public abstract class CommandParser {
	protected CommandParser next;
	
	public CommandParser(CommandParser next){
		this.next = next;
	}
	
	public abstract Command parse(Message<? extends Entity> event);
}
