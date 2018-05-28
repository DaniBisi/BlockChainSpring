package magenta.blockchainspring.application.service.blockchainrepo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class DbManager {
	private final String AGENZIA = "Magenta";
	private final String CAADDRESS = "http://127.0.0.1:7054";
	private final String ORAGNIZATION = "Org1MSP";
	public Map<String, String> getPeerList() {
		HashMap<String, String> peerList = new HashMap<String,String>();
		peerList.put("testPeer", "grpc://127.0.0.1:7051");
		return peerList;

	}

	public Map<String, String> getOrderList() {
		HashMap<String, String> orderList = new HashMap<String,String>();
		orderList.put("orderer", "grpc://127.0.0.1:7050");
		return orderList;

	}

	public HashMap<String, String> getEventList() {
		HashMap<String, String> eventList = new HashMap<String,String>();
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
