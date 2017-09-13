package test;
import static communication.message.Event.signIn;
import static communication.message.Status.none;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import command.Command;
import command.user.SignInCommand;
import communication.commandparser.CommandParser;
import communication.commandparser.CommandParserFactory;
import communication.message.Event;
import communication.message.Message;
import communication.message.Status;
import communication.protocol.Protocol;
import communication.protocol.ProtocolFactory;
import communication.protocol.XOXOXDelimiterFactory;
import gamecore.GameCore;
import gamecore.entity.Entity;
import gamecore.entity.user.User;
import gamecore.entity.user.UserImp;
import gamefactory.GameFactory;
import gamefactory.GameOnlineReleaseFactory;
import mock.MockService;
import userservice.UserService;

public class CommandExecutionTest {
	private final String XXX = "XXX";
	
	private ProtocolFactory protocolFactory;
	private GameFactory gameFactory;
	private GameCore gamecore;
	private CommandParserFactory commandParserFactory;
	
	
	@Before
	public void setup() throws IOException{
		gameFactory = new GameOnlineReleaseFactory();
		protocolFactory = gameFactory.getProtocolFactory();
		gamecore = gameFactory.getGameCore();
		commandParserFactory = gameFactory.getCommandParserFactory();
	}


	@Test
	public void testSignInCommand() throws IOException {
		String signInRequest = FileUtils.readFileToString(new File("userSignIn.txt"));
		Protocol protocol = protocolFactory.createProtocol(signInRequest);
		int currentSize = gamecore.getOnlineUsers().size();
		MockService mockService = new MockService();
		
		CommandParser parser = gameFactory.createCommandParser(mockService);
		Command command = parser.parse(protocol);
		gamecore.executeCommand(command);
		
		User result = (User) mockService.getMessage().getData();
		assertEquals("Test", result.getName()); // same user
		assertEquals(currentSize + 1, gamecore.getOnlineUsers().size()); // online user plus one
	}
	

}
