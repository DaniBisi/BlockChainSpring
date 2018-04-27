package magenta.blockChainSpring.application.repository;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
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
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.Attribute;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.InvalidProtocolBufferException;

import magenta.blockChainSpring.application.model.AppUser;

public class BCRepository {

	private static final Logger logger = LogManager.getLogger(BCRepository.class);
	private HFCAClient caClient;
	private HFClient client;
	private Channel channel;
	private Enrollment userEnrollment;
	private AppUser userLogged;
	private String transactionID;
	final ChaincodeID chainCodeId;
	private boolean loginStatus;

	public boolean getLoginStatus() {
		return loginStatus;
	}

	public BCRepository(HFCAClient caClient, HFClient client, AppUser userLogged, ChaincodeID chainCodeId) {
		BasicConfigurator.configure();
		this.caClient = caClient;
		this.client = client;
		this.userLogged = userLogged;
		this.chainCodeId = chainCodeId;
		loginStatus = false;
		this.transactionID = "noTransactionDone";
	}

	public String queryDB(String[] query) throws org.hyperledger.fabric.sdk.exception.InvalidArgumentException,
			ProposalException, InterruptedException, ExecutionException, TimeoutException {
		QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
		queryByChaincodeRequest.setChaincodeID(chainCodeId);
		queryByChaincodeRequest.setFcn(query[0]);
		if (setArguments(query) != null) {
			queryByChaincodeRequest.setArgs(setArguments(query));
		}
		logger.info("############### function name: " + query[0] + "##################");
		logger.info("############### chainCodeId name: " + chainCodeId.getName() + "##################");
		Collection<ProposalResponse> queryProposals = channel.queryByChaincode(queryByChaincodeRequest);
		String payload = evaluateResponse(queryProposals);
		CompletableFuture<BlockEvent.TransactionEvent> txFuture = channel.sendTransaction(queryProposals,
				client.getUserContext());
		BlockEvent.TransactionEvent event = txFuture.get(600, TimeUnit.SECONDS);
		this.transactionID = event.getTransactionID();
		return payload;
	}

	public String []queryBlock(int blockNumber)
			throws InvalidArgumentException, ProposalException, InvalidProtocolBufferException {
		String payload[] = new String[10];
		logger.info("############### Query block : " + blockNumber + "##################");
		LinkedList<byte[]> b1 = new LinkedList<byte[]>();
		for (int i = 0; i < blockNumber; i++) {

			BlockInfo bInfo = channel.queryBlockByNumber(i);
			Map<FieldDescriptor, Object> payloadAsByte = bInfo.getBlock().getAllFields();
			BlockData payloadAsByte2 = bInfo.getBlock().getData();
			payload[i] = payloadAsByte2.toString();
//			for (Map.Entry<FieldDescriptor, Object> entry : payloadAsByte.entrySet()) {
//				String entryString = entry.getKey().toString(); //+ "/" + entry.getValue());
//				System.out.println(entryString);
//				if(entryString.equals("common.Block.metadata")) {
//					System.out.println("YEAHHHHHHHHHHHHHHHHH");
//					String answareRaw = entry.getValue().toString();
//					System.out.println(answareRaw);
//					String[] parts = answareRaw.split("-----END CERTIFICATE-----");
//					String risposta = "";
//					if(parts.length>1) {
//						risposta = parts[1];
//					}
//					System.out.println(risposta);
//					
//				}
//			}

			// payload = payload + new String(bInfo.getChannelId());
			// payload = payload + new String(bInfo.getPreviousHash());
			// payload = payload + new String(payloadAsByte);
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
			arg = new String[query.length - 1];
			for (int i = 1; i < query.length; i++) {
				arg[i - 1] = query[i];
			}

		} else {
			arg = null;
		}
		return arg;
	}

	public boolean login(String passw) {
		try {
			userEnrollment = caClient.enroll(userLogged.getName(), passw);
			userLogged.setEnrollment(userEnrollment);
			client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
			client.setUserContext(userLogged);
			Peer peer = client.newPeer("testPeer", "grpc://127.0.0.1:7051");
			Orderer orderer = client.newOrderer("orderer", "grpc://127.0.0.1:7050");
			EventHub eventHub = client.newEventHub("eventhub", "grpc://localhost:7053");
			channel = client.newChannel("mychannel");
			channel.addPeer(peer);
			channel.addEventHub(eventHub);
			channel.addOrderer(orderer);
			channel.initialize();
			// channel.joinPeer(peer);
			loginStatus = true;
		} catch (Exception e) {
			logger.error("[Login Failed]:" + e.toString());
			return false;
		}
		return true;
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
