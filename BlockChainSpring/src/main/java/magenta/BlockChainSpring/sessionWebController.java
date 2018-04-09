package magenta.BlockChainSpring;

import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.qos.logback.classic.Logger;
import magenta.BlockChainSpring.AppUser;
import magenta.BlockChainSpring.Car;
import magenta.BlockChainSpring.SessionWrapper;
import magenta.BlockChainSpring.commandFactory;

@Controller
public class sessionWebController {
	@Value("indexForm")
	private String resources;
	
	@GetMapping("/")
	public String welcome(Model model) {
	model.addAttribute("resources", resources);
	model.addAttribute("login", new LoginCollect());
	return "index";
	}
	
	@PostMapping("/login")
	public String loginUser(@ModelAttribute LoginCollect login,Model model) {
		String name = login.getUserName();
		String password = login.getPassword();
		String query = login.getQuery();
		String args = login.getArgs();
		
		if (StringUtils.isEmpty(name)) {
			name = "admin";
			password = "adminpw";
		}
		if(StringUtils.isEmpty(query)) {
			query = "queryAllCars";
		}
		CryptoSuite cryptoSuite;
		try {
			cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
			HFCAClient caClient = HFCAClient.createNewInstance("http://127.0.0.1:7054", null);
			caClient.setCryptoSuite(cryptoSuite);
			HFClient client = HFClient.createNewInstance();
			client.setCryptoSuite(cryptoSuite);
			SessionWrapper u1 = new SessionWrapper(caClient, client, new AppUser(name, "Magenta", "Magenta"),
					ChaincodeID.newBuilder().setName("mycc").build());
			u1.login(password);
			commandFactory f1 = new commandFactory(query,args);
			String queryAnsware = u1.queryDB(f1.getFormattedQuery());
			LinkedList<Car> record;
			record = f1.getCommand().execute(queryAnsware);
			
			model.addAttribute("txId", u1.getTransactionId());
			model.addAttribute("user", name);
			model.addAttribute("records", record);
			model.addAttribute("queryAnsware", queryAnsware);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "response";
	}
}
