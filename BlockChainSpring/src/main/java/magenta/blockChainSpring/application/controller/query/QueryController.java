package magenta.blockChainSpring.application.controller.query;

import java.util.LinkedList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import magenta.blockChainSpring.application.controller.login.Login;
import magenta.blockChainSpring.application.model.Items;
import magenta.blockChainSpring.application.model.SpringConstant;
import magenta.blockChainSpring.application.repository.BCRepository;
import magenta.blockChainSpring.application.service.parser.ParserStrategy;

@Controller
public class QueryController {
	@Autowired
	private ParserStrategy parserStrategy;

	@GetMapping("/query")
	public String queryRunner(@ModelAttribute("query") Query queryR, Model model, HttpSession httpSession) {

		BCRepository u1 = (BCRepository) httpSession.getAttribute("u1");
		if (u1 != null && u1.getLoginStatus()) {
			String jSonQueryAnsware = "";
			LinkedList<Items> record = null;
			String query = queryR.getQuery();
			try {
				jSonQueryAnsware = u1.queryDB(parserStrategy.getFormattedQuery(query, queryR.getArgs()));
				record = parserStrategy.getCommandParser(query).execute(jSonQueryAnsware);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (record != null && record.size() > 0) {
				model.addAttribute("records", record);
				model.addAttribute("queryAnsware", jSonQueryAnsware);
				model.addAttribute("valName", record.get(0).getValName());
			} else {
				model.addAttribute("queryAnsware", "empty");
			}

			model.addAttribute(SpringConstant.fragmentsPath, "fragments/response");
			model.addAttribute(SpringConstant.resourcesPath, "queryVisit");
		} else {
			model.addAttribute(SpringConstant.fragmentsPath, "fragments/indexForm");
			model.addAttribute(SpringConstant.resourcesPath, "loginForm");
			model.addAttribute("login", new Login());
		}
		return "index";
	}
}
