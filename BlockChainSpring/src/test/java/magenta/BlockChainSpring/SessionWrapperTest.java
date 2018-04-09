package magenta.BlockChainSpring;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.junit.Before;
import org.junit.Test;

import magenta.BlockChainSpring.AppUser;
import magenta.BlockChainSpring.SessionWrapper;

public class SessionWrapperTest {
	HFCAClient caClient;
	HFClient client;
	AppUser userLogged;
	ChaincodeID chainCodeId;
	private Channel c1;
	
	@Before
	public void setup() {
		chainCodeId = ChaincodeID.newBuilder().setName("fabcar").build();
		caClient = mock(HFCAClient.class);
		client = mock(HFClient.class);
		userLogged = mock(AppUser.class);
		c1 = mock(Channel.class);
	}
	
	
	@Test
	public void testConstructor() {
		SessionWrapper s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
	}
	
	@Test
	public void testLoginCorrect() throws Exception{
		SessionWrapper s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
		when(client.newChannel(anyString())).thenReturn(c1);
		String psswd= "Ciao";
		assertEquals(true, s1.login(psswd));
	}
	@Test 
	public void testLoginUnCorrect() throws Exception{
	//with this test something inside the login function will fail and the exeption will be handled and login will fail
		SessionWrapper s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
		String psswd= "Ciao";
		assertEquals(false, s1.login(psswd));
	}
	
	@Test 
	public void testRegisterCorrect() throws Exception{
		SessionWrapper s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
		when(caClient.register(any(RegistrationRequest.class), eq(userLogged))).thenReturn("ciao");
		assertEquals("ciao", s1.userRegister("dani", "org"));
	}
	@Test
	public void testRegisterUnCorrect() throws Exception{
		SessionWrapper s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
		when(caClient.register(any(RegistrationRequest.class), eq(userLogged))).thenThrow(IllegalAccessError.class);
		assertEquals(null, s1.userRegister("dani", "org"));
	}
}
