package magenta.blockchainspring.application.repository;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.protos.common.Common.BlockData;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.BlockchainInfo;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.EventHub;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.Attribute;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

import com.google.protobuf.ByteString;

import magenta.blockchainspring.application.model.AppUser;

public class BCRepository implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(BCRepository.class);
	private transient HFCAClient caClient;
	private transient HFClient client;
	private transient Channel channel;
	private transient AppUser userLogged;
	private String transactionID;
	final transient ChaincodeID chainCodeId;
	private boolean loginStatus;
	protected LinkedList<Peer> peerList;
	protected LinkedList<Orderer> orderList;
	protected LinkedList<EventHub> eventHubList;

	public BCRepository(HFCAClient caClient, HFClient client, AppUser userLogged, ChaincodeID chainCodeId) {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		this.caClient = caClient;
		this.client = client;
		this.userLogged = userLogged;
		this.chainCodeId = chainCodeId;
		loginStatus = false;
		this.transactionID = "noTransactionDone";
		peerList = new LinkedList<>();
		orderList = new LinkedList<>();
		eventHubList = new LinkedList<>();
	}

	public boolean getLoginStatus() {
		return loginStatus;
	}

	public String queryDB(String[] query) throws org.hyperledger.fabric.sdk.exception.InvalidArgumentException,
			ProposalException, InterruptedException, ExecutionException, TimeoutException {
		QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
		queryByChaincodeRequest.setChaincodeID(chainCodeId);
		queryByChaincodeRequest.setFcn(query[0]);
		if (setArguments(query) != null) {
			queryByChaincodeRequest.setArgs(setArguments(query));
		}
		logger.info("############### chainCodeId name: " + chainCodeId.getName() + "##################");
		Collection<ProposalResponse> queryProposals = channel.queryByChaincode(queryByChaincodeRequest);
		String payload = evaluateResponse(queryProposals);
		CompletableFuture<BlockEvent.TransactionEvent> txFuture = channel.sendTransaction(queryProposals,
				client.getUserContext());
		BlockEvent.TransactionEvent event = txFuture.get(600, TimeUnit.SECONDS);
		this.transactionID = event.getTransactionID();
		return payload;
	}

	public String[] queryBlock() throws InvalidArgumentException, ProposalException {
		BlockchainInfo chainInfo = channel.queryBlockchainInfo();
		int blockNumber = (int) chainInfo.getHeight();
		String[] payload;
		if (blockNumber > 0) {
			payload = new String[blockNumber];
		} else {
			payload = null;
		}
		logger.info("############### Query block : " + blockNumber + "##################");

		for (int i = 0; i < blockNumber && payload != null; i++) {
			BlockInfo bInfo = channel.queryBlockByNumber(i);
			BlockData payloadAsBlockData = bInfo.getBlock().getData();
			List<ByteString> dataList = payloadAsBlockData.getDataList();
			payload[i] = dataList.get(0).toStringUtf8();
			payload[i] = payload[i].replaceAll("[�ϑ]", "");
		}
		return payload;

	}

	public String evaluateResponse(Collection<ProposalResponse> queryProposals) {
		String payload = "";
		for (ProposalResponse proposalResponse : queryProposals) {
			if (!proposalResponse.isVerified() || proposalResponse.getStatus() != ProposalResponse.Status.SUCCESS) {
				logger.error("[Query Failed]:" + proposalResponse.getPeer().getName() + " status: "
						+ proposalResponse.getStatus() + ". Messages: " + proposalResponse.getMessage()
						+ ". Was verified : " + proposalResponse.isVerified());
				payload = "Error...  see log for more information";
			} else {
				payload = proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
				logger.info(proposalResponse.getPeer().getName() + " Risposta " + payload);
			}
		}
		return payload;
	}

	protected String[] setArguments(String[] query) {
		String[] arg;
		if (query.length > 1) {
			arg = Arrays.copyOfRange(query, 1, query.length);
		} else {
			arg = null;
		}
		return arg;
	}

	public boolean login(String passw) {
		try {
			Enrollment userEnrollment;
			userEnrollment = caClient.enroll(userLogged.getName(), passw);
			userLogged.setEnrollment(userEnrollment);
			client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
			client.setUserContext(userLogged);
			loginStatus = true;
		} catch (Exception e) {
			logger.error("[Login Failed]:" + e.toString());
			return false;
		}
		return true;
	}

	public void setPeerList(Map<String, String> peerListP) throws InvalidArgumentException {
		for (Entry<String, String> entry : peerListP.entrySet()) {
			peerList.add(client.newPeer(entry.getKey(), entry.getValue()));
			logger.info(entry.getKey() + " : " + entry.getValue());
		}

	}

	public void setOrdererList(Map<String, String> orderListP) throws InvalidArgumentException {
		for (Entry<String, String> entry : orderListP.entrySet()) {
			orderList.add(client.newOrderer(entry.getKey(), entry.getValue()));
			logger.info(entry.getKey() + " : " + entry.getValue());
		}
	}

	public void setEventList(Map<String, String> eventListP) throws InvalidArgumentException {
		for (Entry<String, String> entry : eventListP.entrySet()) {
			String hubName = entry.getKey();
			String hubAddress = entry.getValue();
			EventHub eventHub = client.newEventHub(hubName, hubAddress);
			eventHubList.add(eventHub);
			logger.info(entry.getKey() + " : " + entry.getValue());
		}

	}

	public void initChannel(String channelName) throws InvalidArgumentException, TransactionException {
		channel = client.newChannel(channelName);
		addOrdererToChannel();
		addEventHubToChannel();
		addPeerToChannel();
		channel.initialize();
	}

	protected void addPeerToChannel() throws InvalidArgumentException {
		for (Peer peer : peerList) {
			channel.addPeer(peer);
		}
	}

	protected void addEventHubToChannel() throws InvalidArgumentException {
		for (EventHub event : eventHubList) {
			channel.addEventHub(event);
		}
	}

	protected void addOrdererToChannel() throws InvalidArgumentException {
		for (Orderer orderer : orderList) {
			channel.addOrderer(orderer);
		}
	}

	public String userRegister(String username, String org) {
		String userSecret = "";
		try {
			RegistrationRequest rr = new RegistrationRequest(username, org);
			Attribute a1 = new Attribute("mspid", "Org1MSP");
			rr.addAttribute(a1);
			userSecret = caClient.register(rr, userLogged);
		} catch (Exception e) {
			logger.error("[Register Fail]:" + e.toString());
			userSecret = "[Register Fail]";
		}
		return userSecret;

	}

	public void addUserRole(String role) {
		userLogged.addRoles(role);

	}

	public String getTransactionID() {
		return this.transactionID;
	}

	public String getUserName() {
		return userLogged.getName();
	}

	public String getUserAgency() {
		return userLogged.getOrganization();
	}

	public Collection<Peer> getPeers() {

		return channel.getPeers();
	}
}
