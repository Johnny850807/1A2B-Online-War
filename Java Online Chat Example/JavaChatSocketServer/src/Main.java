

public class Main {

	public static void main(String[] args) {
		try (ChatServer server = new ChatServer()){
			server.runServer();
		}catch (Exception err){
			err.printStackTrace();
		}
	}
	
}