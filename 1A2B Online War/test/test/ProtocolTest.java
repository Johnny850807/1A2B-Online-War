package test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import communication.protocol.Protocol;
import communication.protocol.ProtocolFactory;
import communication.protocol.XXXDelimiterFactory;

@RunWith(Parameterized.class)
public class ProtocolTest {
	private ProtocolFactory protocolFactory;
	
	private String request;
	private String expectEvent;
	private String expectStatus;
	private String expectData;
	
	public ProtocolTest(String request, String expectEvent, String expectStatus, String expectData) {
		this.request = request;
		this.expectEvent = expectEvent;
		this.expectStatus = expectStatus;
		this.expectData = expectData;
	}

	@Before
	public void setup() {
		protocolFactory = new XXXDelimiterFactory();
	}

	@Test
	public void testProtocol() {
		Protocol protocol = protocolFactory.createProtocol(request);
		assertEquals(expectEvent, protocol.getEvent());
		assertEquals(expectStatus, protocol.getStatus());
		assertEquals(expectData, protocol.getData());
		
		// test deserializing
		Protocol desProtocol = protocol.toProtocol(expectEvent,expectStatus, expectData);
		assertEquals(expectEvent, desProtocol.getEvent());
		assertEquals(expectStatus, desProtocol.getStatus());
		assertEquals(expectData, desProtocol.getData());
		
		assertEquals(request, desProtocol.toString());
	}
	
	
	@Parameterized.Parameters
	public static Collection primeNumbers() {
		// content, expected-event, expected-status, expected-data
		return Arrays.asList(new Object[][] {
			{"eventXOXOXstatusXOXOXdata","event","status","data"},
			{"XXXXXOXOXXXX X X X XOXOXXXXXOXXX","XXXX","XXX X X X ","XXXXOXXX"},
			{"xxxXOXOXxxxXOXOXxxx","xxx","xxx","xxx"},
			{"\"\"\"XOXOX\\\\\\XOXOX1238949834720834907230","\"\"\"","\\\\\\","1238949834720834907230"},
			{"1 2 3 4XOXOX5   6    7     8   XOXOX9	9	9	1	1	","1 2 3 4","5   6    7     8   ","9	9	9	1	1	"},
			{"asdsd asd8asd89axXoxoxoX AASODASOOXOX OXOXOXASD4A6544D56  4 54AS5D4A6SDA31234+XOXOXD SMAKDM XOX DK xoxo","asdsd asd8asd89axXoxoxoX AASODASOOXOX O","ASD4A6544D56  4 54AS5D4A6SDA31234+","D SMAKDM XOX DK xoxo"}
	      });
	}
}
