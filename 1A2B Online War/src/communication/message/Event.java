package communication.message;

import java.util.regex.Pattern;

public enum Event {
	signIn, signOut;
	
	public boolean match(String regex) {
		return Pattern.matches(regex, this.toString());
	}
}
