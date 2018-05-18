package magenta.blockChainSpring.application.controller.login;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import magenta.blockChainSpring.application.controller.query.Query;
import magenta.blockChainSpring.application.repository.BCRepository;
import magenta.blockChainSpring.application.service.blockChainRepo.GetBlockChainRepository;
import magenta.blockChainSpring.application.service.blockChainRepo.dbManager;

@Controller
public class LoginController {
	@Autowired
	private dbManager db;
	@Autowired
	private GetBlockChainRepository gBCRepository;

	@PostMapping("/login")
	public String loginUser(@ModelAttribute("login") Login login, Model model, HttpSession httpSession) throws InvalidArgumentException, TransactionException {
		String name = login.getUserName();
		String password = login.getPassword();
		String organization = db.getOrganization();
		String agency = db.getAgency();
		if (StringUtils.isEmpty(name)) {
			name = "admin";
			password = "adminpw";
			agency = "Magenta";
			organization = "Org1MSP";
		}
		BCRepository u1 = gBCRepository.getBCRepository(name, agency, organization);
		if (u1 != null && u1.login(password)) {
			u1.setEventList(db.getEventList());
			u1.setOrderList(db.getOrderList());
			u1.setPeerList(db.getPeerList());
			u1.initChannel("mychannel");
			httpSession.setAttribute("u1", u1);
			model.addAttribute("user", u1.getUserName());
			model.addAttribute("page", "fragments/indexForm");
			model.addAttribute("resources", "queryForm");
			model.addAttribute("query", new Query());
		} else {
			model.addAttribute("page", "fragments/indexForm");
			model.addAttribute("resources", "loginForm");
			model.addAttribute("login", new Login());
		}

		return "index";
	}
}
