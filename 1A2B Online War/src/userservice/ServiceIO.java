package userservice;

import java.io.InputStream;
import java.io.OutputStream;

public interface ServiceIO {
	public InputStream getInputStream() throws Exception;
	public OutputStream getOutputStream() throws Exception;
}
