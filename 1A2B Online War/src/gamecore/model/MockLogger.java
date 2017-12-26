package gamecore.model;

import java.io.Serializable;

import container.base.MyLogger;

public class MockLogger implements MyLogger, Serializable{

	@Override
	public void trace(String msg) {
		System.out.println(msg);
	}

	@Override
	public void fatal(String msg) {
		System.out.println(msg);
	}

	@Override
	public void error(String msg, Exception err) {
		System.err.println(msg);
		System.err.println(err.toString());
	}

	@Override
	public void error(String msg) {
		System.out.println(msg);
	}

	@Override
	public void info(String msg) {
		System.out.println(msg);
	}

	@Override
	public void warn(String msg) {
		System.out.println(msg);
	}
	
}
