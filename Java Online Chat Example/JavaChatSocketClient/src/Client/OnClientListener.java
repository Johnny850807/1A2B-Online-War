package Client;

public interface OnClientListener {
	String onNaming();
	void onReceive(String message);
}
