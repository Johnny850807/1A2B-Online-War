package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import command.Command;
import command.SignInCommand;
import communication.commandparser.CommandParser;
import communication.protocol.Protocol;
import communication.protocol.ProtocolFactory;
import gamefactory.GameFactory;
import gamefactory.GameOnlineReleaseFactory;
import mock.MockProtocol;
import mock.MockService;

public class CommandParserTest {
	private ProtocolFactory protocolFactory;
	private CommandParser parser;
	
	@Before
	public void setup() {
		GameFactory factory = new GameOnlineReleaseFactory();
		protocolFactory = factory.createProtocolFactory();
		parser = factory.createCommandParser(new MockService());
	}
	
	@Test
	public void user() {
		Protocol protocol = new MockProtocol
				("signIn", "request", "{\"name\":\"Test\"}");
		
		Command command = parser.parse(protocol);
		assertTrue(command instanceof SignInCommand);
	}

}
