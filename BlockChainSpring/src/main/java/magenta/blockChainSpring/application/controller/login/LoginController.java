package magenta.blockChainSpring.application.controller.login;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import magenta.blockChainSpring.application.controller.query.Query;
import magenta.blockChainSpring.application.repository.BCRepository;
import magenta.blockChainSpring.application.service.blockChainRepo.GetBlockChainRepository;

@Controller
public class LoginController {
	@Autowired
	private GetBlockChainRepository gBCRepository;

	@PostMapping("/login")
	public String loginUser(@ModelAttribute("login") Login login, Model model, HttpSession httpSession) {
		String name = login.getUserName();
		String password = login.getPassword();
		if (StringUtils.isEmpty(name)) {
			name = "admin";
			password = "adminpw";
		}
		BCRepository u1 = gBCRepository.getBCRepository(name);
		if (u1 != null && u1.login(password)) {
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
