package magenta.blockChainSpring.application.service.blockChainRepo;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.springframework.stereotype.Service;

import magenta.blockChainSpring.application.model.AppUser;
import magenta.blockChainSpring.application.repository.BCRepository;

@Service
public class GetBlockChainRepository {

	
	public BCRepository getBCRepository(String name) {

		try {
			CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
			HFCAClient caClient = HFCAClient.createNewInstance("http://127.0.0.1:7054", null);
			caClient.setCryptoSuite(cryptoSuite);
			HFClient client = HFClient.createNewInstance();
			client.setCryptoSuite(cryptoSuite);
			return new BCRepository(caClient, client, new AppUser(name, "Magenta", "Org1MSP"),
					ChaincodeID.newBuilder().setName("employVisit").build());
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
	}
}
