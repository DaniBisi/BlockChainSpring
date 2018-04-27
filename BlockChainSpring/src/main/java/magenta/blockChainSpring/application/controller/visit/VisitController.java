package magenta.blockChainSpring.application.controller.visit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
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
import magenta.blockChainSpring.application.repository.BCRepository;
import magenta.blockChainSpring.application.service.parser.ParserStrategy;

@Controller
public class VisitController {
	@Autowired
	private ParserStrategy parserStrategy;

	private static final org.apache.log4j.Logger logger = LogManager.getLogger(VisitController.class);
	public VisitController() {
	}

	@GetMapping({ "/create" })
	public String welcome(Model model, HttpSession httpSession,
			@RequestParam(value = "idVisit", required = false) String idVisit) {
		BCRepository u1 = (BCRepository) httpSession.getAttribute("u1");
		if (u1 == null || !u1.getLoginStatus()) {
			model.addAttribute("resources", "loginForm");
			model.addAttribute("login", new Login());
		} else {
			model.addAttribute("resources", "visit");
			VisitCollector v1 = new VisitCollector();
			v1.setAgency(u1.getUserAgency());
			v1.setUserName( u1.getUserName());
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			System.out.println(dateFormat.format(date));
			logger.info(dateFormat.format(date));
			v1.setDate(dateFormat.format(date));
			model.addAttribute("visitCollector", v1);
		}
		return "index";
	}
	
	
	@PostMapping("/update")
	public String queryRunner(@ModelAttribute("visitCollector") VisitCollector Visit, Model model, HttpSession httpSession) {
		
		String retValue;
		BCRepository u1 = (BCRepository) httpSession.getAttribute("u1");
		if (u1.getLoginStatus()) {
			String jSonQueryAnsware = "";
			LinkedList<Items> record = null;
			String[] query = new String[5];
			query[0] = "createVisit";
			query[1] = Integer.toString(Visit.getFileName().hashCode());
			query[2] = Visit.getUserName();
			query[3] = Visit.getAgency();
			query[4] = Visit.getDate();
			try {
				jSonQueryAnsware = u1.queryDB(query);
				record = parserStrategy.getCommandParser("createVisit").execute(jSonQueryAnsware);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (record != null && record.size() > 0) {
				model.addAttribute("records", record);
				model.addAttribute("queryAnsware", jSonQueryAnsware);
				model.addAttribute("valName", record.get(0).getValName());
			} else {
//				model.addAttribute("valName", null);
//				model.addAttribute("records", "empty");
				model.addAttribute("queryAnsware", "empty");
			}
			retValue = "response";
		} else {
			retValue = "index";
			model.addAttribute("resources", "loginForm");
			model.addAttribute("login", new Login());
		}
		return retValue;
	}

	//

}
