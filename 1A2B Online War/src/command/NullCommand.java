package command;

public class NullCommand implements Command{
	// Null object pattern which the command does nothing at all
	
	@Override
	public void execute() {
		throw new IllegalStateException("Null command executed.");
	}

}
