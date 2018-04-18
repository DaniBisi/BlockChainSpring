package magenta.BlockChainSpring;

import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import magenta.BlockChainSpring.AppUser;
import magenta.BlockChainSpring.SessionWrapper;
import magenta.BlockChainSpring.commandFactory;

@Controller
@Scope("session")
public class SessionWebController {
	@Value("indexForm")
	private String resources;
	private SessionWrapper u1;
	private String retValue;
	private String queryAnsware;

	@GetMapping("/")
	public String welcome(Model model) {
		if (u1 == null || !u1.getLoginStatus()) {
			model.addAttribute("resources", resources);
			model.addAttribute("login", new LoginCollect());
		} else {
			model.addAttribute("u1", u1);
			model.addAttribute("user", u1.getUserName());
			model.addAttribute("query", new QueryCollect());
		}
		return "index";
	}

	@PostMapping("/login")
	public String loginUser(@ModelAttribute("login") LoginCollect login, Model model) {
		retValue = "";
		String name = login.getUserName();
		String password = login.getPassword();

		if (StringUtils.isEmpty(name)) {
			name = "admin";
			password = "adminpw";
		}
		CryptoSuite cryptoSuite;
		try {
			cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
			HFCAClient caClient = HFCAClient.createNewInstance("http://127.0.0.1:7054", null);
			caClient.setCryptoSuite(cryptoSuite);
			HFClient client = HFClient.createNewInstance();
			client.setCryptoSuite(cryptoSuite);
			u1 = new SessionWrapper(caClient, client, new AppUser(name, "Org1MSP", "Org1MSP"),
					ChaincodeID.newBuilder().setName("employVisit").build());

			u1.login(password);

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (u1.getLoginStatus()) {

			model.addAttribute("u1", u1);
			model.addAttribute("user", name);
			model.addAttribute("query", new QueryCollect());
		} else {
			model.addAttribute("login", new LoginCollect());
		}
		return "index";
	}

	@PostMapping("/query")
	public String queryRunner(@ModelAttribute("query") QueryCollect query, Model model) {

		String queryReceived = query.getQuery();
		String args = query.getArgs();
		if (StringUtils.isEmpty(queryReceived)) {
			queryReceived = "queryAllVisits";
		}
		commandFactory f1 = new commandFactory(queryReceived, args);
		System.out.println("query: " + f1.getFormattedQuery().toString());
		try {
			queryAnsware = u1.queryDB(f1.getFormattedQuery());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		LinkedList<Items> record;
		System.out.println("questa è la risposta." + queryAnsware);
		record = f1.getCommandParser().execute(queryAnsware);

		model.addAttribute("u1", u1);
		model.addAttribute("records", record);
		model.addAttribute("valName", record.get(0).getValName());
		model.addAttribute("queryAnsware", queryAnsware);

		if (u1.getLoginStatus()) {
			model.addAttribute("u1", u1);
			retValue = "response";
		} else {
			retValue = "index";
			model.addAttribute("login", new LoginCollect());
		}
		return "response";
	}
}
