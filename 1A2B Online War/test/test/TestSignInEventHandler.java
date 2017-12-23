package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import container.eventhandler.ConfigBasedGameEventHandlerFactory;
import container.eventhandler.EventHandler;
import container.eventhandler.GameEventHandlerFactory;
import container.eventhandler.EventHandler.OnRespondingListener;
import container.protocol.Protocol;
import gamecore.model.RequestStatus;
import gamefactory.GameFactory;
import gamefactory.GameOnlineReleaseFactory;
import mock.MockClient;

public class TestSignInEventHandler implements OnRespondingListener{
	private GameFactory gameFactory;
	private GameEventHandlerFactory handlerFactory;
	private boolean error = true;
	@Before
	public void setUp() throws Exception {
		gameFactory = new GameOnlineReleaseFactory();
		handlerFactory = new ConfigBasedGameEventHandlerFactory(gameFactory);
	}

	@Test
	public void test() {
		MockClient client = new MockClient();
		Protocol protocol = gameFactory.getProtocolFactory().createProtocol("SignIn", RequestStatus.request.toString() , "{\"name\":\"Johnny\"}");
		EventHandler handler = handlerFactory.createGameEventHandler(client, protocol);
		handler.setOnRespondingListener(this);
		assertEquals(handler.getClass().getSimpleName(), "SignInHandler");

		handler.handle();
		assertEquals(false, error);
		assertEquals(1, client.getResponses().size());
	}

	@Override
	public void onErrorResponding(Protocol responseProtocol) {
		System.out.println(responseProtocol);
	}

	@Override
	public void onSuccessResponding(Protocol responseProtocol) {
		System.out.println(responseProtocol);
		error = false;
	}

}
