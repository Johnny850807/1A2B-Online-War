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
	}
	
	
	@Parameterized.Parameters
	public static Collection primeNumbers() {
		return Arrays.asList(new Object[][] {
			{"eventXXXstatusXXXdata","event","status","data"},
			{"xxxXXXxxxXXXxxx","xxx","xxx","xxx"},
			{"\"\"\"XXX\\\\\\XXX1238949834720834907230","\"\"\"","\\\\\\","1238949834720834907230"},
			{"1 2 3 4XXX5   6    7     8   XXX9	9	9	1	1	","1 2 3 4","5   6    7     8   ","9	9	9	1	1	"}
	      });
	}
}
