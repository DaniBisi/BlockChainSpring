package magenta.blockChainSpring.application.controller.home;

import javax.servlet.http.HttpSession;

//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import magenta.blockChainSpring.application.controller.login.Login;
import magenta.blockChainSpring.application.controller.query.Query;
import magenta.blockChainSpring.application.model.SpringConstant;
import magenta.blockChainSpring.application.repository.BCRepository;

@Controller
public class HomeController {
	


	@GetMapping({ "/", "/home" })
	public String welcome(Model model, HttpSession httpSession) {
		BCRepository u1 = (BCRepository) httpSession.getAttribute("u1");
		if (u1 == null || !u1.getLoginStatus()) {
			model.addAttribute(SpringConstant.resourcesPath, "loginForm");
			model.addAttribute("page", "fragments/indexForm");
			model.addAttribute("login", new Login());
		} else {
			model.addAttribute("page", "fragments/indexForm");
			model.addAttribute(SpringConstant.resourcesPath, "queryForm");
			model.addAttribute("query", new Query());
		}
		return "index";
	}

	//

	

}
