package magenta.blockchainspring.application.controller.home;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import magenta.blockchainspring.application.controller.login.Login;
import magenta.blockchainspring.application.controller.query.Query;
import magenta.blockchainspring.application.model.SpringConstant;
import magenta.blockchainspring.application.repository.BCRepository;

@Controller
public class HomeController {
	


	@GetMapping({ "/", "/home" })
	public String welcome(Model model, HttpSession httpSession) {
		BCRepository u1 = (BCRepository) httpSession.getAttribute("u1");
		String fragmentsPath = "fragments/indexForm";
		String resourcesPath ;
		fragmentsPath = "fragments/indexForm";
		if (u1 == null || !u1.getLoginStatus()) {
			resourcesPath = "loginForm";
			model.addAttribute("login", new Login());
		} else {
			resourcesPath = "queryForm";
			model.addAttribute("query", new Query());
		}
		
		model.addAttribute(SpringConstant.FRAGMENTSPATH, fragmentsPath);
		model.addAttribute(SpringConstant.RESOURCESPATH, resourcesPath);
		
		return "index";
	}

	//

	

}
