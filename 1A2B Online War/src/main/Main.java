package main;

import gamefactory.GameFactory;
import gamefactory.GameOnlineReleaseFactory;
import socket.SocketAccepter;

public class Main {

	public static void main(String[] args) {
		GameFactory factory = new GameOnlineReleaseFactory();
		new Thread(new SocketAccepter(factory)).start();

	}

}
