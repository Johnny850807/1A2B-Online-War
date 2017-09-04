package communication;

import command.Command;
import gamecore.entity.BaseEntity;
import gamecore.entity.Entity;

public abstract class CommandParser {
	protected CommandParser next;
	
	public CommandParser(CommandParser next){
		this.next = next;
	}
	
	public abstract Command parse(Message<? extends Entity> event);
}
