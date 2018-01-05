package gamecore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import container.core.MyLogger;

public class ApacheLoggerAdapter implements MyLogger{
	private Logger logger;
	
	public ApacheLoggerAdapter(Class<?> clazz){
		this.logger = LogManager.getLogger(clazz);
	}
	
	@Override
	public void trace(String msg) {
		logger.trace(msg);
	}

	@Override
	public void fatal(String msg) {
		logger.fatal(msg);
	}

	@Override
	public void error(String msg, Exception err) {
		logger.error(msg, err);
	}
	
	@Override
	public void error(String msg) {
		logger.error(msg);
	}
	
	@Override
	public void info(String msg) {
		logger.info(msg);
	}

	@Override
	public void warn(String msg) {
		logger.warn(msg);
	}

}
