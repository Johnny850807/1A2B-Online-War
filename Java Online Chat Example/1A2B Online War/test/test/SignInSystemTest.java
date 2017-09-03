package test;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import command.CommandParser;
import command.Message;
import factory.GameFactory;
import factory.GameOnlineReleaseFactory;
import gamecore.GameCore;
import gamecore.entity.User;
import mock.MockService;

public class SignInSystemTest {
	private GameFactory factory;
	private GameCore gamecore;
	private CommandParser parser;
	private String signInRequestJson;
	
	@Before
	public void setup(){
		factory = new GameOnlineReleaseFactory();
		gamecore = factory.createGameCore();
		signInRequestJson = createSignInRequestJson();
		parser = factory.createCommandParser();
	}
	
	private String createSignInRequestJson(){
		//TODO create json which imposed to sign in
		return "";
	}
	
	@Test
	public void test() {
		String name = "test";
		MockService service = new MockService();
		User user = gamecore.signIn(service, name);
		assertEquals(name, user.getName());
		assertTrue(service.hasBeenResponded());
	}

}
