package test;
import static org.junit.Assert.*;import javax.net.ssl.ExtendedSSLSession;

import org.junit.Before;
import org.junit.Test;

import command.Command;
import command.SignInCommand;
import communication.CommandParser;
import communication.Message;
import communication.RequestParser;
import factory.GameFactory;
import factory.GameOnlineReleaseFactory;
import gamecore.GameCore;
import gamecore.entity.Entity;
import gamecore.entity.User;
import gamecore.entity.UserImp;

import static communication.Status.*;
import static communication.Event.*;
import mock.MockService;
import socket.UserService;

public class SignInSystemTest {
	private GameFactory factory;
	private GameCore gamecore;
	private CommandParser commandParser;
	private RequestParser requestParser;
	private String signInRequestJson;
	
	//Mocked
	private UserService mockService;
	
	@Before
	public void setup(){
		factory = new GameOnlineReleaseFactory();
		gamecore = factory.createGameCore();
		signInRequestJson = createSignInRequestJson();
		commandParser = factory.createCommandParser();
		requestParser = factory.createRequestParser();
		mockService = new MockService();
	}
	
	private String createSignInRequestJson(){
		return "{\"event\": \"signIn\","+
				"\"data\": {	"+
					"\"name\" : \"Test\""+
				"}"+
				"}";
	}
	
	@Test
	public void testRequestParser() {
		Message<? extends Entity> message = requestParser.parseRequest(signInRequestJson);
		assertEquals("Test", ((User)message.getData()).getName());
		assertEquals(signIn, message.getEvent());
	}
	
	@Test
	public void testCommandParser() {
		String name = "Test";
		User user = new UserImp(name);
		Message<User> message = new Message<User>(signIn, none, user);
		Command command = commandParser.parse(message);
		assertTrue(command instanceof SignInCommand);
	}
	
	@Test
	public void wholeTest() {
		MockService service = new MockService();
		Message<? extends Entity> message = requestParser.parseRequest(signInRequestJson);
		Command signInCommand = commandParser.parse(message);
		gamecore.executeCommand(signInCommand);
		
		User expect = (User) message.getData();
		User result = (User) service.getReceivedData();
		assertEquals(expect.getName(), result.getName());
	}

}
