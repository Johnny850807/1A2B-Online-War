package container.eventhandler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import container.core.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamefactory.GameFactory;

/**
 * @author Johnny85007
 * The factory which binds the request from the client to the correct handler by its name
 * written in the /config/eventhandler.config file.
 */
public class ConfigBasedGameEventHandlerFactory implements GameEventHandlerFactory{
	private static Logger log = LogManager.getLogger(ConfigBasedGameEventHandlerFactory.class);
	private static final String CONFIG_PATH = "./config/eventhandler.config";
	private static final String EVENT_HANDLER_TAG = "EventHandler";
	private static final String HANDLER_MAPPING_TAG = "EventHandlerMapping";
	private GameFactory gameFactory;
	private Map<String, String> mappings = new HashMap<>(); // <event name, handler's name>
	private Map<String, Class<?>> handlerMap = new HashMap<>(); // <handler's name, handler's class>
	
	public ConfigBasedGameEventHandlerFactory(GameFactory gameFactory){
		try {
			this.gameFactory = gameFactory;
			loadAllMappingNameWithHandlers();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	private void loadAllMappingNameWithHandlers() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException{
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.parse(new File(CONFIG_PATH));
		setupMappings(document);
		setupHandlers(document);
	}
	
	private void setupMappings(Document document){
		NodeList mappingNodes = document.getElementsByTagName(HANDLER_MAPPING_TAG);
		for (int i = 0 ; i < mappingNodes.getLength() ; i ++)
		{
			Element element = (Element) mappingNodes.item(i);
			String event = element.getAttribute("event");
			String handlerName = element.getAttribute("handlerName");
			log.info("Event handler registering.. event: " + event + "  ->  handler name: " + handlerName);
			mappings.put(event, handlerName);
		}
	}
	
	private void setupHandlers(Document document) throws ClassNotFoundException, MalformedURLException{
		NodeList handlerNodes = document.getElementsByTagName(EVENT_HANDLER_TAG);
		for (int i = 0 ; i < handlerNodes.getLength() ; i ++)
		{
			Element element = (Element) handlerNodes.item(i);
			String handlerClassName = "container.eventhandler.handlers." + element.getAttribute("handlerClass");
			Class<?> handlerClass = Class.forName(handlerClassName);
			String handlerName = element.getAttribute("handlerName");
			log.info("Event handler class mapping.. handler name: " + handlerName + "  ->  class: " + handlerClass);
			handlerMap.put(handlerName, handlerClass);
		}
	}
	

	@Override
	public EventHandler createGameEventHandler(Client client, Protocol protocol) {
		String event = protocol.getEvent();
		String handlerName = mappings.get(event);
		try {
			log.trace("Handler creating: event " + event + ", handlerName: " + handlerName);
			return createEventHandler(client, protocol, handlerMap.get(handlerName));
		} catch (Exception e) {
			log.error("Handler creating error occurs.", e);
		}
		return null;
	}

	private EventHandler createEventHandler(Client client, Protocol request, Class<?> handlerClass) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		//Use reflection to find out the corresponding handler class and init it with the constructor.
		Constructor<?> constructor = handlerClass.getDeclaredConstructor(
				Client.class, Protocol.class, GameCore.class, ProtocolFactory.class);
		
		return (EventHandler) constructor.newInstance(client, request, gameFactory.getGameCore(), gameFactory.getProtocolFactory());
	}
}
