package magenta.blockchainspring.application.controller.query;

import java.util.LinkedList;

import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import magenta.blockchainspring.application.SessionHandler;
import magenta.blockchainspring.application.model.Items;
import magenta.blockchainspring.application.repository.BCRepository;
import magenta.blockchainspring.application.service.parser.ParserStrategy;

@Controller
public class QueryController extends SessionHandler {
	@Autowired
	private ParserStrategy parserStrategy;
	private static final Logger logger = LogManager.getLogger(QueryController.class);

	@GetMapping("/query")
	public String queryRunner(@ModelAttribute("query") Query queryR, Model model, HttpSession httpSession) {
		bcRepo = (BCRepository) httpSession.getAttribute("u1");
		if (checkSession()) {
			String jSonQueryAnsware = "";
			LinkedList<Items> record = null;
			String query = queryR.getQueryName();
			try {
				jSonQueryAnsware = bcRepo.queryDB(parserStrategy.getFormattedQuery(query, queryR.getArgs()));
				record = (LinkedList<Items>) parserStrategy.getCommandParser(query).execute(jSonQueryAnsware);
			} catch (Exception e) {
				logger.error("QUERY FAIL:An error occurred in query procedure..." + e.toString());

			}
			if (record != null && !record.isEmpty()) {
				model.addAttribute("records", record);
				model.addAttribute("queryAnsware", jSonQueryAnsware);
				model.addAttribute("valName", record.get(0).getValName());
			} else {
				model.addAttribute("queryAnsware", "empty");
			}

			fragmentsPath = "fragments/response";
			resourcesPath = "queryVisit";
		}
		setModel(model);

		return "index";
	}
}
