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
	private DbManager db;
	private GetBlockChainRepository gBCRepository;

	@Autowired
	public LoginController(final DbManager db, final GetBlockChainRepository gBCRepository) {
		this.db = db;
		this.gBCRepository = gBCRepository;
	}
	@PostMapping("/login")
	public String loginUser(@ModelAttribute("login") Login login, Model model, HttpSession httpSession) throws InvalidArgumentException, TransactionException {
		String name = login.getUserName();
		String password = login.getPassword();
		String organization = db.getOrganization();
		String agency = db.getAgency();
		
		BCRepository bcRepo = gBCRepository.getBCRepository(name, agency, organization);
		if (bcRepo != null && bcRepo.login(password)) {
			bcRepo.setEventList(db.getEventList());
			bcRepo.setOrdererList(db.getOrderList());
			bcRepo.setPeerList(db.getPeerList());
			bcRepo.initChannel("mychannel");
			httpSession.setAttribute("u1", bcRepo);
			model.addAttribute("user", bcRepo.getUserName());
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
