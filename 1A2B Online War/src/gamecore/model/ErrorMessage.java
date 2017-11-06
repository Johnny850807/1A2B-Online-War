package gamecore.model;

/**
 * ErrorMessage stands for a certain Exception by its code number.
 */
public class ErrorMessage {
	private int code;
	private String message;
	
	public ErrorMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
