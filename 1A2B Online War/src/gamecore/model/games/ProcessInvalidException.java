package gamecore.model.games;

public class ProcessInvalidException extends IllegalStateException{
	public ProcessInvalidException(String mgs){
		super(mgs);
	}
}
