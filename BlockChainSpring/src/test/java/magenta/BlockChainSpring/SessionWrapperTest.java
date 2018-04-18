package magenta.BlockChainSpring;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;

import org.hyperledger.fabric.protos.peer.FabricProposalResponse;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric_ca.sdk.exception.RegistrationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import magenta.BlockChainSpring.AppUser;
import magenta.BlockChainSpring.SessionWrapper;

public class SessionWrapperTest {
	HFCAClient caClient;
	HFClient client;
	AppUser userLogged;
	ChaincodeID chainCodeId;
	private Channel c1;
	SessionWrapper s1;

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
		s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
	}

	@Test
	public void testLoginCorrect() throws Exception {
		when(client.newChannel(anyString())).thenReturn(c1);
		s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
		String psswd = "Ciao";
		assertEquals(true, s1.login(psswd));
	}

	@Test
	public void testGetLoginStatus() throws Exception {
		when(client.newChannel(anyString())).thenReturn(c1);
		s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
		String psswd = "Ciao";
		s1.login(psswd);
		assertEquals(true, s1.getLoginStatus());
	}

	@Test
	public void testLoginUnCorrect() throws Exception {
		// with this test something inside the login function will fail and the exeption
		// will be handled and login will fail
		s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
		String psswd = "Ciao";
		assertEquals(false, s1.login(psswd));
	}

	@Test
	public void testRegisterCorrect() throws Exception {
		when(caClient.register(any(RegistrationRequest.class), eq(userLogged))).thenReturn("ciao");
		s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
		assertEquals("ciao", s1.userRegister("dani", "org"));
	}

	@Test
	public void testRegisterUnCorrect() throws Exception {
		when(caClient.register(any(RegistrationRequest.class), eq(userLogged)))
				.thenThrow(IllegalArgumentException.class);
		s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
		assertEquals("[Register Fail]", s1.userRegister("dani", "org"));
	}

	@Test
	public void testAddUserRole() {
		s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
		String psswd = "Ciao";
		s1.login(psswd);
		s1.addUserRole("client");
		verify(userLogged, times(1)).addRoles(anyString());
	}

	@Test
	public void testQueryDbProposalVerifiedOneArguments() throws InvalidArgumentException, ProposalException,
			InterruptedException, ExecutionException, TimeoutException {
		s1 = Mockito.spy(new SessionWrapper(caClient, client, userLogged, chainCodeId));
		String psswd = "Ciao";
		when(client.newChannel(any(String.class))).thenReturn(c1);
		org.hyperledger.fabric.sdk.User u1 = mock(org.hyperledger.fabric.sdk.User.class);
		when(client.getUserContext()).thenReturn(u1);
		s1.login(psswd);
		QueryByChaincodeRequest q1 = mock(QueryByChaincodeRequest.class);
		when(client.newQueryProposalRequest()).thenReturn(q1);
		Collection<ProposalResponse> p1 = new LinkedList<ProposalResponse>();
		ProposalResponse pRMocked = mock(ProposalResponse.class);
		when(pRMocked.isVerified()).thenReturn(true);
		Peer pM1 = mock(Peer.class);
		when(pRMocked.getPeer()).thenReturn(pM1);
		p1.add(pRMocked);
		Mockito.doReturn("All Goods").when(s1).evaluateResponse(any(Collection.class)); // <-- protected method will be
																						// // tested by itself
		CompletableFuture<TransactionEvent> txFuture = mock(CompletableFuture.class);
		BlockEvent.TransactionEvent e1 = mock(BlockEvent.TransactionEvent.class);
		when(c1.queryByChaincode(any(QueryByChaincodeRequest.class))).thenReturn(p1);
		when(c1.sendTransaction(p1, u1)).thenReturn(txFuture);
		when(txFuture.get(600, TimeUnit.SECONDS)).thenReturn(e1);
		when(e1.getTransactionID()).thenReturn("successIDReturned");
		String[] args = { "ciao" };
		assertEquals("All Goods", s1.queryDB(args));
	}

	@Test
	public void testGetTransactionId() throws InvalidArgumentException, ProposalException, InterruptedException,
			ExecutionException, TimeoutException {
		initSession(false);
		assertEquals("noTransactionDone", s1.getTransactionID());
	}

	@Test
	public void testGetUserName() throws InvalidArgumentException, ProposalException, InterruptedException,
			ExecutionException, TimeoutException {
		when(userLogged.getName()).thenReturn("dani");
		initSession(false);
		assertEquals("dani", s1.getUserName());
	}

	@Test
	public void testGetPeers() throws InvalidArgumentException, ProposalException, InterruptedException,
			ExecutionException, TimeoutException {

		initSession(true);
		s1.getPeers();
		verify(c1, times(1)).getPeers();
	}

	@Test
	public void testEvaluateResponse() throws InvalidArgumentException {
		initSession(true);
		Collection<ProposalResponse> p1 = new LinkedList<ProposalResponse>();
		ProposalResponse pRMocked = mock(ProposalResponse.class);
		when(pRMocked.isVerified()).thenReturn(true);
		when(pRMocked.getStatus()).thenReturn(ProposalResponse.Status.SUCCESS);
		Peer pM1 = mock(Peer.class);
		FabricProposalResponse.ProposalResponse pRMocked2 = mock(FabricProposalResponse.ProposalResponse.class);
		com.google.protobuf.ByteString payLoad1 = mock(com.google.protobuf.ByteString.class);
		org.hyperledger.fabric.protos.peer.FabricProposalResponse.Response r1 = mock(
				org.hyperledger.fabric.protos.peer.FabricProposalResponse.Response.class);
		when(pRMocked.getPeer()).thenReturn(pM1);
		when(pRMocked.getProposalResponse()).thenReturn(pRMocked2);
		when(pRMocked2.getResponse()).thenReturn(r1);
		when(r1.getPayload()).thenReturn(payLoad1);
		when(payLoad1.toStringUtf8()).thenReturn("success");
		p1.add(pRMocked);
		assertEquals("success", s1.evaluateResponse(p1));
	}

	@Test
	public void testEvaluateResponseNotVerified() throws InvalidArgumentException {
		initSession(true);
		Collection<ProposalResponse> p1 = new LinkedList<ProposalResponse>();
		ProposalResponse pRMocked = mock(ProposalResponse.class);
		when(pRMocked.isVerified()).thenReturn(false);
		when(pRMocked.getStatus()).thenReturn(ProposalResponse.Status.SUCCESS);
		Peer pM1 = mock(Peer.class);
		when(pRMocked.getPeer()).thenReturn(pM1);
		p1.add(pRMocked);
		assertEquals("Error...  see log for more information", s1.evaluateResponse(p1));
	}

	@Test
	public void testEvaluateResponseVerifiedButFailed() throws InvalidArgumentException {
		initSession(true);
		Collection<ProposalResponse> p1 = new LinkedList<ProposalResponse>();
		ProposalResponse pRMocked = mock(ProposalResponse.class);
		when(pRMocked.isVerified()).thenReturn(true);
		when(pRMocked.getStatus()).thenReturn(ProposalResponse.Status.FAILURE);
		Peer pM1 = mock(Peer.class);
		when(pRMocked.getPeer()).thenReturn(pM1);
		p1.add(pRMocked);
		assertEquals("Error...  see log for more information", s1.evaluateResponse(p1));
	}

	private void initSession(boolean withLogin) throws InvalidArgumentException {
		when(client.newChannel(any(String.class))).thenReturn(c1);
		org.hyperledger.fabric.sdk.User u1 = mock(org.hyperledger.fabric.sdk.User.class);
		when(client.getUserContext()).thenReturn(u1);
		s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
		String psswd = "Ciao";
		if (withLogin) {
			s1.login(psswd);
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSetArguments() {
		s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
		String[] query = { "hello", "good morning", "goodbye" };
		String[] queryFormatted = { "good morning", "goodbye" };

		assertEquals(queryFormatted, s1.setArguments(query));
	}

	@Test
	public void testSetArgumentsEmpty() {
		s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
		String[] query = {};
		assertNull(s1.setArguments(query));
	}
}