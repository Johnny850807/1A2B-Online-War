package container.base;

public interface MyLogger {
	public void trace(String msg);
	public void fatal(String msg);
	public void error(String msg, Exception err);
	public void error(String msg);
	public void info(String msg);
	public void warn(String msg);
}
