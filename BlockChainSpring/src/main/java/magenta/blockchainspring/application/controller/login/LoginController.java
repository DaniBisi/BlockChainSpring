package magenta.blockchainspring.application.controller.login;

import javax.servlet.http.HttpSession;

import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import magenta.blockchainspring.application.controller.query.Query;
import magenta.blockchainspring.application.model.SpringConstant;
import magenta.blockchainspring.application.repository.BCRepository;
import magenta.blockchainspring.application.service.blockchainrepo.DbManager;
import magenta.blockchainspring.application.service.blockchainrepo.GetBlockChainRepository;

@Controller
public class LoginController {
	@Autowired
	private DbManager db;
	@Autowired
	private GetBlockChainRepository gBCRepository;

	@PostMapping("/login")
	public String loginUser(@ModelAttribute("login") Login login, Model model, HttpSession httpSession) throws InvalidArgumentException, TransactionException {
		String name = login.getUserName();
		String password = login.getPassword();
		String organization = db.getOrganization();
		String agency = db.getAgency();
		
		BCRepository u1 = gBCRepository.getBCRepository(name, agency, organization);
		if (u1 != null && u1.login(password)) {
			u1.setEventList(db.getEventList());
			u1.setOrdererList(db.getOrderList());
			u1.setPeerList(db.getPeerList());
			u1.initChannel("mychannel");
			httpSession.setAttribute("u1", u1);
			model.addAttribute("user", u1.getUserName());
			model.addAttribute(SpringConstant.FRAGMENTSPATH, "fragments/indexForm");
			model.addAttribute(SpringConstant.RESOURCESPATH, "queryForm");
			model.addAttribute("query", new Query());
		} else {
			model.addAttribute(SpringConstant.FRAGMENTSPATH, "fragments/indexForm");
			model.addAttribute(SpringConstant.RESOURCESPATH, "loginForm");
			model.addAttribute("login", new Login());
		}

		return "index";
	}
}
