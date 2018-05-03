package magenta.blockChainSpring.application.controller.visit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;

import javax.servlet.http.HttpSession;

import org.apache.catalina.startup.HomesUserDatabase;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import magenta.blockChainSpring.application.controller.login.Login;
import magenta.blockChainSpring.application.controller.query.Query;
import magenta.blockChainSpring.application.model.Items;
import magenta.blockChainSpring.application.model.Visit;
import magenta.blockChainSpring.application.repository.BCRepository;
import magenta.blockChainSpring.application.service.parser.ParserStrategy;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
@Controller
public class VisitCreateController {
	

	private static final org.apache.log4j.Logger logger = LogManager.getLogger(VisitUpdateController.class);

	public VisitCreateController() {
	}

	@GetMapping({ "/create" })
	public String create(Model model, HttpSession httpSession) {
		BCRepository bcRepo = (BCRepository) httpSession.getAttribute("u1");
		if (bcRepo == null || !bcRepo.getLoginStatus()) {
			model.addAttribute("page", "fragments/indexForm");
			model.addAttribute("resources", "loginForm");
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
			model.addAttribute("resources", "createVisit");
			model.addAttribute("page", "fragments/visitForm");
			model.addAttribute("visitCollector", v1);
		}
		return "index";
	}


}
