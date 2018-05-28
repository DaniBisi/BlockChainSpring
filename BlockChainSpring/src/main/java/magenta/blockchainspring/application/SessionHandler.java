package magenta.blockchainspring.application;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import magenta.blockchainspring.application.controller.login.Login;
import magenta.blockchainspring.application.model.SpringConstant;
import magenta.blockchainspring.application.repository.BCRepository;

@Controller
public abstract class SessionHandler {
	protected String fragmentsPath ;
	protected String resourcesPath ;
	protected BCRepository bcRepo;
	private boolean session;
	public SessionHandler() {
		fragmentsPath = "fragments/indexForm";
		resourcesPath = "loginForm";
	}
	
	public boolean checkSession() {
		session = (bcRepo != null && bcRepo.getLoginStatus());
		return session;
	}
	
	public void setModel(Model model) {
		if(!session) {
			model.addAttribute("login", new Login());	
			fragmentsPath = "fragments/indexForm";
			resourcesPath = "loginForm";
		}
		model.addAttribute(SpringConstant.FRAGMENTSPATH, fragmentsPath);
		model.addAttribute(SpringConstant.RESOURCESPATH, resourcesPath);
	}
}
