package magenta.blockchainspring.application.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.hyperledger.fabric.protos.common.Common.Block;
import org.hyperledger.fabric.protos.common.Common.BlockData;
import org.hyperledger.fabric.protos.peer.FabricProposalResponse;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.BlockchainInfo;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.EventHub;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import magenta.blockchainspring.application.model.AppUser;
import magenta.blockchainspring.application.repository.BCRepository;

public class BCRepositoryTest {
	HFCAClient caClient;
	HFClient client;
	AppUser userLogged;
	ChaincodeID chainCodeId;
	private Channel channel;
	BCRepository bcRepo;

	@Before
	public void setup() {
		chainCodeId = ChaincodeID.newBuilder().setName("fabcar").build();
		caClient = mock(HFCAClient.class);
		client = mock(HFClient.class);
		userLogged = mock(AppUser.class);
		channel = mock(Channel.class);
	}

	@Test
	public void testConstructor() {
		bcRepo = new BCRepository(caClient, client, userLogged, chainCodeId);
	}

	@Test
	public void testLoginCorrect() throws Exception {
		when(client.newChannel(anyString())).thenReturn(channel);
		bcRepo = new BCRepository(caClient, client, userLogged, chainCodeId);
		String psswd = "Ciao";
		assertEquals(true, bcRepo.login(psswd));
	}

	@Test
	public void testGetLoginStatus() throws Exception {
		when(client.newChannel(anyString())).thenReturn(channel);
		bcRepo = new BCRepository(caClient, client, userLogged, chainCodeId);
		String psswd = "Ciao";
		bcRepo.login(psswd);
		assertEquals(true, bcRepo.getLoginStatus());
	}

	@Test
	public void testLoginUnCorrect() throws Exception {
		when(userLogged.getName()).thenReturn("user");
		when(caClient.enroll("user","Ciao")).thenThrow(IllegalArgumentException.class);
		bcRepo = new BCRepository(caClient, client, userLogged, chainCodeId);
		String psswd = "Ciao";
		assertEquals(false, bcRepo.login(psswd));
	}

	@Test
	public void testRegisterCorrect() throws Exception {
		when(caClient.register(any(RegistrationRequest.class), eq(userLogged))).thenReturn("ciao");
		bcRepo = new BCRepository(caClient, client, userLogged, chainCodeId);
		assertEquals("ciao", bcRepo.userRegister("dani", "org"));
	}

	@Test
	public void testRegisterUnCorrect() throws Exception {
		when(caClient.register(any(RegistrationRequest.class), eq(userLogged)))
				.thenThrow(IllegalArgumentException.class);
		bcRepo = new BCRepository(caClient, client, userLogged, chainCodeId);
		assertEquals("[Register Fail]", bcRepo.userRegister("dani", "org"));
	}

	@Test
	public void testAddUserRole() {
		bcRepo = new BCRepository(caClient, client, userLogged, chainCodeId);
		String psswd = "Ciao";
		bcRepo.login(psswd);
		bcRepo.addUserRole("client");
		verify(userLogged, times(1)).addRoles(anyString());
	}

	@Test
	public void testQueryDbProposalVerifiedOneArguments() throws InvalidArgumentException, ProposalException,
			InterruptedException, ExecutionException, TimeoutException, TransactionException {
		bcRepo = Mockito.spy(new BCRepository(caClient, client, userLogged, chainCodeId));
		String psswd = "Ciao";
		when(client.newChannel(any(String.class))).thenReturn(channel);
		org.hyperledger.fabric.sdk.User u1 = mock(org.hyperledger.fabric.sdk.User.class);
		when(client.getUserContext()).thenReturn(u1);
		bcRepo.login(psswd);
		QueryByChaincodeRequest q1 = mock(QueryByChaincodeRequest.class);
		when(client.newQueryProposalRequest()).thenReturn(q1);
		Collection<ProposalResponse> p1 = new LinkedList<ProposalResponse>();
		ProposalResponse pRMocked = mock(ProposalResponse.class);
		when(pRMocked.isVerified()).thenReturn(true);
		Peer pM1 = mock(Peer.class);
		when(pRMocked.getPeer()).thenReturn(pM1);
		p1.add(pRMocked);
		Mockito.doReturn("All Goods").when(bcRepo).evaluateResponse(any(Collection.class)); // <-- protected method will be
																						// // tested by itself
		CompletableFuture<TransactionEvent> txFuture = mock(CompletableFuture.class);
		BlockEvent.TransactionEvent e1 = mock(BlockEvent.TransactionEvent.class);
		when(channel.queryByChaincode(any(QueryByChaincodeRequest.class))).thenReturn(p1);
		when(channel.sendTransaction(p1, u1)).thenReturn(txFuture);
		when(txFuture.get(600, TimeUnit.SECONDS)).thenReturn(e1);
		when(e1.getTransactionID()).thenReturn("successIDReturned");
		String[] args = { "ciao" };
		bcRepo.initChannel("mychannel");
		assertEquals("All Goods", bcRepo.queryDB(args));
	}

	
	@Test
	public void testQueryDbProposalVerifiedMoreArguments() throws InvalidArgumentException, ProposalException,
			InterruptedException, ExecutionException, TimeoutException, TransactionException {
		bcRepo = Mockito.spy(new BCRepository(caClient, client, userLogged, chainCodeId));
		String psswd = "Ciao";
		when(client.newChannel(any(String.class))).thenReturn(channel);
		org.hyperledger.fabric.sdk.User u1 = mock(org.hyperledger.fabric.sdk.User.class);
		when(client.getUserContext()).thenReturn(u1);
		bcRepo.login(psswd);
		QueryByChaincodeRequest q1 = mock(QueryByChaincodeRequest.class);
		when(client.newQueryProposalRequest()).thenReturn(q1);
		Collection<ProposalResponse> p1 = new LinkedList<ProposalResponse>();
		ProposalResponse pRMocked = mock(ProposalResponse.class);
		when(pRMocked.isVerified()).thenReturn(true);
		Peer pM1 = mock(Peer.class);
		when(pRMocked.getPeer()).thenReturn(pM1);
		p1.add(pRMocked);
		Mockito.doReturn("All Goods").when(bcRepo).evaluateResponse(any(Collection.class)); // <-- protected method will be
																						// // tested by itself
		CompletableFuture<TransactionEvent> txFuture = mock(CompletableFuture.class);
		BlockEvent.TransactionEvent e1 = mock(BlockEvent.TransactionEvent.class);
		when(channel.queryByChaincode(any(QueryByChaincodeRequest.class))).thenReturn(p1);
		when(channel.sendTransaction(p1, u1)).thenReturn(txFuture);
		when(txFuture.get(600, TimeUnit.SECONDS)).thenReturn(e1);
		when(e1.getTransactionID()).thenReturn("successIDReturned");
		String[] args = { "ciao","miao"};
		bcRepo.initChannel("mychannel");
		assertEquals("All Goods", bcRepo.queryDB(args));
	}

	
	
	@Test
	public void testGetTransactionId() throws InvalidArgumentException, ProposalException, InterruptedException,
			ExecutionException, TimeoutException, TransactionException {
		initSession(false);
		assertEquals("noTransactionDone", bcRepo.getTransactionID());
	}
	
	
	@Test
	public void testGetUserName() throws InvalidArgumentException, ProposalException, InterruptedException,
			ExecutionException, TimeoutException, TransactionException {
		when(userLogged.getName()).thenReturn("dani");
		initSession(false);
		assertEquals("dani", bcRepo.getUserName());
	}

	@Test
	public void testGetPeers() throws InvalidArgumentException, ProposalException, InterruptedException,
			ExecutionException, TimeoutException, TransactionException {

		initSession(true);
		bcRepo.getPeers();
		
		verify(channel, times(1)).getPeers();
	}
	
	@Test
	public void testGetUserAgency() throws InvalidArgumentException, TransactionException {
		initSession(true);
		assertEquals("agency", bcRepo.getUserAgency());
	}
// ###################### START TEST INIT CHANNEL ############
	@Test
	public void testInitChannel() throws InvalidArgumentException, ProposalException, InterruptedException,
			ExecutionException, TimeoutException, TransactionException {
		when(client.newChannel(any(String.class))).thenReturn(channel);
		when(userLogged.getOrganization()).thenReturn("agency");
		org.hyperledger.fabric.sdk.User u1 = mock(org.hyperledger.fabric.sdk.User.class);
		when(client.getUserContext()).thenReturn(u1);
		bcRepo = new BCRepository(caClient, client, userLogged, chainCodeId);
		bcRepo.initChannel("myChannel");
		verify(client, times(1)).newChannel("myChannel");
		verify(channel, times(1)).initialize();	
	}
	@Test
	public void testAddOrdererToChannel() throws InvalidArgumentException, ProposalException, InterruptedException,
			ExecutionException, TimeoutException, TransactionException {
		Orderer mOrderer = mock(Orderer.class);
		when(client.newOrderer(anyString(), anyString())).thenReturn(mOrderer);	
		initSession(false);
		HashMap<String, String> mapList = new HashMap<String,String>();
		mapList.put("mockedObj", "mockedAddress");
		bcRepo.setOrdererList(mapList);
		bcRepo.addOrdererToChannel();
		verify(channel, times(1)).addOrderer(mOrderer);	
	}
	@Test
	public void testAddEventToChannel() throws InvalidArgumentException, ProposalException, InterruptedException,
			ExecutionException, TimeoutException, TransactionException {
		EventHub mEvent = mock(EventHub.class);
		when(client.newEventHub(anyString(), anyString())).thenReturn(mEvent);	
		initSession(false);
		HashMap<String, String> mapList = new HashMap<String,String>();
		mapList.put("mockedObj", "mockedAddress");
		bcRepo.setEventList(mapList);
		bcRepo.addEventHubToChannel();
		verify(channel, times(1)).addEventHub(mEvent);	
	}

	@Test
	public void testAddPeerToChannel() throws InvalidArgumentException, ProposalException, InterruptedException,
			ExecutionException, TimeoutException, TransactionException {
		Peer mPeer = mock(Peer.class);
		when(client.newPeer(anyString(), anyString())).thenReturn(mPeer);	
		initSession(false);
		HashMap<String, String> mapList = new HashMap<String,String>();
		mapList.put("mockedObj", "mockedAddress");
		bcRepo.setPeerList(mapList);
		bcRepo.addPeerToChannel();
		verify(channel, times(1)).addPeer(mPeer);	
	}
	//#################### START TEST SETTERS ####################
	@Test
	public void testSetPeerList() throws InvalidArgumentException, TransactionException {
		initSession(false);
		HashMap<String, String> eventList = new HashMap<String,String>();
		eventList.put("mockedPeer", "mockedAddress");
		bcRepo.setPeerList(eventList);
		verify(client, times(1)).newPeer("mockedPeer","mockedAddress");
	}
	@Test
	public void testSetHubList() throws InvalidArgumentException, TransactionException {
		initSession(false);
		HashMap<String, String> eventList = new HashMap<String,String>();
		eventList.put("mockedHub", "mockedAddress");
		bcRepo.setEventList(eventList);
		verify(client, times(1)).newEventHub("mockedHub","mockedAddress");
	}
	@Test
	public void testSetOrderList() throws InvalidArgumentException, TransactionException {
		initSession(false);
		HashMap<String, String> eventList = new HashMap<String,String>();
		eventList.put("mockedOrder", "mockedAddress");
		bcRepo.setOrdererList(eventList);
		verify(client, times(1)).newOrderer("mockedOrder","mockedAddress");
	}
	@Test
	public void testSetPeerListEmpty() throws InvalidArgumentException, TransactionException {
		initSession(false);
		HashMap<String, String> eventList = new HashMap<String,String>();
		bcRepo.setPeerList(eventList);
		verify(client, times(0)).newPeer("mockedPeer","mockedAddress");
	}
	@Test
	public void testSetHubListEmpty() throws InvalidArgumentException, TransactionException {
		initSession(false);
		HashMap<String, String> eventList = new HashMap<String,String>();
		bcRepo.setEventList(eventList);
		verify(client, times(0)).newEventHub("mockedHub","mockedAddress");
	}
	@Test
	public void testSetOrderListEmpty() throws InvalidArgumentException, TransactionException {
		initSession(false);
		HashMap<String, String> eventList = new HashMap<String,String>();
		bcRepo.setOrdererList(eventList);
		verify(client, times(0)).newOrderer("mockedOrder","mockedAddress");
	}
	//#################### END TEST SETTERS ####################
	
	
	@Test
	public void testEvaluateResponse() throws InvalidArgumentException, TransactionException {
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
		assertEquals("success", bcRepo.evaluateResponse(p1));
	}

	@Test
	public void testEvaluateResponseNotVerified() throws InvalidArgumentException, TransactionException {
		initSession(true);
		Collection<ProposalResponse> p1 = new LinkedList<ProposalResponse>();
		ProposalResponse pRMocked = mock(ProposalResponse.class);
		when(pRMocked.isVerified()).thenReturn(false);
		when(pRMocked.getStatus()).thenReturn(ProposalResponse.Status.SUCCESS);
		Peer pM1 = mock(Peer.class);
		when(pRMocked.getPeer()).thenReturn(pM1);
		p1.add(pRMocked);
		assertEquals("Error...  see log for more information", bcRepo.evaluateResponse(p1));
	}

	@Test
	public void testEvaluateResponseVerifiedButFailed() throws InvalidArgumentException, TransactionException {
		initSession(true);
		Collection<ProposalResponse> p1 = new LinkedList<ProposalResponse>();
		ProposalResponse pRMocked = mock(ProposalResponse.class);
		when(pRMocked.isVerified()).thenReturn(true);
		when(pRMocked.getStatus()).thenReturn(ProposalResponse.Status.FAILURE);
		Peer pM1 = mock(Peer.class);
		when(pRMocked.getPeer()).thenReturn(pM1);
		p1.add(pRMocked);
		assertEquals("Error...  see log for more information", bcRepo.evaluateResponse(p1));
	}

	private void initSession(boolean withLogin) throws InvalidArgumentException, TransactionException {
		when(client.newChannel(any(String.class))).thenReturn(channel);
		when(userLogged.getOrganization()).thenReturn("agency");
		org.hyperledger.fabric.sdk.User u1 = mock(org.hyperledger.fabric.sdk.User.class);
		when(client.getUserContext()).thenReturn(u1);
		bcRepo = new BCRepository(caClient, client, userLogged, chainCodeId);
		String psswd = "Ciao";
		if (withLogin) {
			bcRepo.login(psswd);
		}
		bcRepo.initChannel("mychannel");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSetArguments() {
		bcRepo = new BCRepository(caClient, client, userLogged, chainCodeId);
		String[] query = { "hello", "good morning", "goodbye" };
		String[] queryFormatted = { "good morning", "goodbye" };

		assertEquals(queryFormatted, bcRepo.setArguments(query));
	}

	@Test
	public void testSetArgumentsEmpty() {
		bcRepo = new BCRepository(caClient, client, userLogged, chainCodeId);
		String[] query = {};
		assertNull(bcRepo.setArguments(query));
	}
	
	
	@Test
	public void testQueryBlock() throws ProposalException, InvalidArgumentException, InvalidProtocolBufferException, UnsupportedEncodingException, TransactionException {
		BlockchainInfo mChainInfo = mock(BlockchainInfo.class);
		BlockInfo mockbInfo = mock(BlockInfo.class);
		Block mBlock = mock(Block.class);
		BlockData mPayLoadBD = mock(BlockData.class);
		List<ByteString> dataList = new LinkedList<ByteString>();
		dataList.add(ByteString.copyFrom("Str1".getBytes()));
		when(mPayLoadBD.getDataList()).thenReturn(dataList);
		when(mockbInfo.getBlock()).thenReturn(mBlock);
		when(mBlock.getData()).thenReturn(mPayLoadBD);
		when(channel.queryBlockByNumber(0)).thenReturn(mockbInfo);
		when(channel.queryBlockByNumber(1)).thenReturn(mockbInfo);
		when(channel.queryBlockByNumber(2)).thenReturn(mockbInfo);
		when(mChainInfo.getHeight()).thenReturn((long) 3);
		when(channel.queryBlockchainInfo()).thenReturn(mChainInfo);
		initSession(false);
		String[] payload = {"Str1","Str1","Str1"};
		assertEquals(payload, bcRepo.queryBlock());
	}
	@Test
	public void testQueryBlockEmptyChain() throws ProposalException, InvalidArgumentException, InvalidProtocolBufferException, UnsupportedEncodingException, TransactionException {
		BlockchainInfo mChainInfo = mock(BlockchainInfo.class);
		when(mChainInfo.getHeight()).thenReturn((long) 0);
		when(channel.queryBlockchainInfo()).thenReturn(mChainInfo);
		initSession(false);
		assertEquals(null, bcRepo.queryBlock());
	}
}