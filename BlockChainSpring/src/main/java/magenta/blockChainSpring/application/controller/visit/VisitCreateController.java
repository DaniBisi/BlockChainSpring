package magenta.blockChainSpring.application.controller.visit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import magenta.blockChainSpring.application.controller.login.Login;
import magenta.blockChainSpring.application.model.SpringConstant;
import magenta.blockChainSpring.application.repository.BCRepository;
@Controller
public class VisitCreateController {
	

	private static final org.apache.log4j.Logger logger = LogManager.getLogger(VisitUpdateController.class);

	@GetMapping({ "/create" })
	public String create(Model model, HttpSession httpSession) {
		BCRepository bcRepo = (BCRepository) httpSession.getAttribute("u1");
		if (bcRepo == null || !bcRepo.getLoginStatus()) {
			model.addAttribute(SpringConstant.fragmentsPath, "fragments/indexForm");
			model.addAttribute(SpringConstant.resourcesPath, "loginForm");
			model.addAttribute("login", new Login());
		} else {
			VisitCollector v1 = new VisitCollector();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm");
			Date date = new Date();
			logger.info("sto loggando date" + date);
			logger.info(dateFormat.format(date));
			v1.setDate(dateFormat.format(date));
			v1.setTime(timeFormat.format(date));
			v1.setAgency(bcRepo.getUserAgency());
			v1.setUserName(bcRepo.getUserName());
			logger.info("sto loggando v1.time" + v1.getTime());
			model.addAttribute(SpringConstant.resourcesPath, "createVisit");
			model.addAttribute(SpringConstant.fragmentsPath, "fragments/visitForm");
			model.addAttribute("visitCollector", v1);
		}
		return "index";
	}


}
