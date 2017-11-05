package container;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import container.base.IO;

public class SocketIO implements IO{
	private Socket socket;
	
	public SocketIO(Socket socket) {
		this.socket = socket;
	}

	@Override
	public InputStream getInputStream()  throws Exception{
		return socket.getInputStream();
	}

	@Override
	public OutputStream getOutputStream()  throws Exception{
		return socket.getOutputStream();
	}

}
