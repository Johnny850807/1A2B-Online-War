package container.eventhandler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

public @interface ErrorHandling {
	public Class<? extends Exception> exception();
	
}
