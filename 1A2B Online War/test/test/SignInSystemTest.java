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
import command.SignInCommand;
import communication.CommandParser;
import communication.message.Event;
import communication.message.Message;
import communication.message.Status;
import communication.protocol.Protocol;
import communication.protocol.ProtocolFactory;
import communication.protocol.XXXDelimiterFactory;
import gamecore.GameCore;
import gamecore.entity.Entity;
import gamecore.entity.user.User;
import gamecore.entity.user.UserImp;
import gamefactory.GameFactory;
import gamefactory.GameOnlineReleaseFactory;
import mock.MockService;
import socket.UserService;

public class SignInSystemTest {
	private final String EVENT = "signIn";
	private final String STATUS = "request";
	private final String XXX = "XXX";
	
	private ProtocolFactory protocolFactory;
	private GameFactory gameFactory;
	private GameCore gamecore;
	private CommandParser commandParser;
	
	private String signInRequest;
	
	//Mocked
	private UserService mockService;
	
	@Before
	public void setup() throws IOException{
		protocolFactory = new XXXDelimiterFactory();
		gameFactory = new GameOnlineReleaseFactory();
		gamecore = gameFactory.createGameCore();
		commandParser = gameFactory.createCommandParser(protocolFactory);
		
		mockService = new MockService();
		signInRequest = EVENT + XXX + STATUS + 
				XXX + FileUtils.readFileToString(new File("userSignIn.json"), "UTF-8");
	}


	@Test
	public void testCommandParser() {
		Protocol protocol = protocolFactory.createProtocol(signInRequest);
		Command command = commandParser.parse(protocol);
		assertTrue(command instanceof SignInCommand);
	}
	
	@Test
	public void testSignInCommand() {
		User expect = new UserImp("Test");
		MockService service = new MockService();
		Message<User> message = new Message<>(Event.signIn, Status.request, expect);
		Command command = new SignInCommand(gamecore, service, message);
		gamecore.executeCommand(command);
		
		User result = (User) service.getMessage().getData();
		assertEquals(expect.getName(), result.getName());
	}
	
	

}
