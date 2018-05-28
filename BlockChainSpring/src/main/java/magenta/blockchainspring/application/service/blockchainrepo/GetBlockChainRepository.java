package magenta.blockchainspring.application.service.blockchainrepo;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import magenta.blockchainspring.application.model.AppUser;
import magenta.blockchainspring.application.repository.BCRepository;

@Service
public class GetBlockChainRepository {

	@Autowired
	DbManager db;
	
	public BCRepository getBCRepository(String name,String agency,String organization) {

		try {
			CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
			HFCAClient caClient = HFCAClient.createNewInstance(db.getCaAddress(), null);
			caClient.setCryptoSuite(cryptoSuite);
			HFClient client = HFClient.createNewInstance();
			client.setCryptoSuite(cryptoSuite);
			return new BCRepository(caClient, client, new AppUser(name, agency, organization),
					ChaincodeID.newBuilder().setName("employVisit").build());
			
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
	}
}
