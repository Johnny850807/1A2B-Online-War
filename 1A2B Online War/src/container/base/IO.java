package container.base;

import java.io.InputStream;
import java.io.OutputStream;

public interface IO {
	public InputStream getInputStream() throws Exception;
	public OutputStream getOutputStream() throws Exception;
}
