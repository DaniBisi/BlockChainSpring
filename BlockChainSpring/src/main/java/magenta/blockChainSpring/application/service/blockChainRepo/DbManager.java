package magenta.blockChainSpring.application.service.blockChainRepo;

import java.util.HashMap;

import org.springframework.stereotype.Service;

@Service
public class DbManager {

	public HashMap<String, String> getPeerList() {
		HashMap<String, String> peerList = new HashMap<String,String>();
		peerList.put("testPeer", "grpc://127.0.0.1:7051");
		return peerList;

	}

	public HashMap<String, String> getOrderList() {
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
		return "http://127.0.0.1:7054";
	}

	public String getAgency() {
		return "Magenta";
	}

	public String getOrganization() {
		return "Org1MSP";
	}

}
