package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import command.base.Command;
import command.user.SignInCommand;
import communication.commandparser.base.CommandParser;
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
		protocolFactory = factory.getProtocolFactory();
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
