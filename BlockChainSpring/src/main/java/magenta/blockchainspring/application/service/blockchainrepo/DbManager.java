package magenta.blockchainspring.application.service.blockchainrepo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class DbManager {
	private static final String AGENZIA = "Magenta";
	private static final String CAADDRESS = "http://127.0.0.1:7054";
	private static final String ORAGNIZATION = "Org1MSP";

	public Map<String, String> getPeerList() {
		HashMap<String, String> peerList = new HashMap<>();
		peerList.put("testPeer", "grpc://127.0.0.1:7051");
		return peerList;

	}

	public Map<String, String> getOrderList() {
		HashMap<String, String> orderList = new HashMap<>();
		orderList.put("orderer", "grpc://127.0.0.1:7050");
		return orderList;

	}

	public Map<String, String> getEventList() {
		HashMap<String, String> eventList = new HashMap<>();
		eventList.put("eventhub", "grpc://localhost:7053");
		return eventList;
	}

	public String getCaAddress() {
		return CAADDRESS;
	}

	public String getAgency() {
		return AGENZIA;
	}

	public String getOrganization() {
		return ORAGNIZATION;
	}

}
