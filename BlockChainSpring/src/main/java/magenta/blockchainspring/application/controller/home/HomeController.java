package magenta.blockchainspring.application.controller.home;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import magenta.blockchainspring.application.SessionHandler;
import magenta.blockchainspring.application.controller.query.Query;
import magenta.blockchainspring.application.repository.BCRepository;

@Controller
public class HomeController extends SessionHandler{
	


	@GetMapping({ "/", "/home" })
	public String welcome(Model model, HttpSession httpSession) {
		bcRepo = (BCRepository) httpSession.getAttribute("u1");
		fragmentsPath = "fragments/indexForm";
		if (checkSession()) {resourcesPath = "queryForm";
			model.addAttribute("query", new Query());
		}
		
		setModel(model);
		
		return "index";
	}

	//

	

}
