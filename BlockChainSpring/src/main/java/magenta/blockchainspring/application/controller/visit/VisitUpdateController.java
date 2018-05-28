package magenta.blockchainspring.application.controller.visit;

import java.util.LinkedList;

import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import magenta.blockchainspring.application.SessionHandler;
import magenta.blockchainspring.application.model.Items;
import magenta.blockchainspring.application.model.SpringConstant;
import magenta.blockchainspring.application.model.Visit;
import magenta.blockchainspring.application.repository.BCRepository;
import magenta.blockchainspring.application.service.parser.ParserStrategy;

@Controller
public class VisitUpdateController extends SessionHandler {
	@Autowired
	private ParserStrategy parserStrategy;

	private static final org.apache.log4j.Logger logger = LogManager.getLogger(VisitCreateController.class);

	@GetMapping("/update")
	public String update(Model model, HttpSession httpSession,
			@RequestParam(value = "idVisit", required = false) String idVisit) {
		bcRepo = (BCRepository) httpSession.getAttribute("u1");
		if (checkSession()) {
			VisitCollector visitCollector = new VisitCollector();
			String jSonQueryAnsware = "";
			LinkedList<Items> record = null;
			try {
				String[] queryLedger = parserStrategy.getFormattedQuery(SpringConstant.VISITFUNCTION, idVisit);
				logger.info("queryLedgeer " + queryLedger);
				jSonQueryAnsware = bcRepo
						.queryDB(parserStrategy.getFormattedQuery(SpringConstant.VISITFUNCTION, idVisit));
				record = (LinkedList<Items>) parserStrategy.getCommandParser(SpringConstant.VISITFUNCTION)
						.execute(jSonQueryAnsware);
				Visit visit = (Visit) record.get(0);
				visitCollector.setDate(visit.getDate());
				visitCollector.setTime(visit.getTime());
				visitCollector.setIdVisit(idVisit);
				visitCollector.setAgency(bcRepo.getUserAgency());
				visitCollector.setUserName(bcRepo.getUserName());

				fragmentsPath = "fragments/visitForm";
				resourcesPath = "updateVisit";
				model.addAttribute("visitCollector", visitCollector);
			} catch (Exception e) {
				logger.error("UPDATE FAIL:An error occurred in update procedure..." + e.toString());

				fragmentsPath = "fragments/response";
				resourcesPath = "queryResponse";
				model.addAttribute("queryAnsware", "empty");
			}
		}

		setModel(model);

		return "index";
	}

	@PostMapping("/update")
	public String queryRunner(@ModelAttribute("visitCollector") VisitCollector visit, Model model,
			HttpSession httpSession) {
		bcRepo = (BCRepository) httpSession.getAttribute("u1");
		if (checkSession()) {
			String jSonQueryAnsware = "";
			LinkedList<Items> record = null;
			String[] query = createQuery(visit);
			try {
				jSonQueryAnsware = bcRepo.queryDB(query);
				record = (LinkedList<Items>) parserStrategy.getCommandParser("createVisit").execute(jSonQueryAnsware);
			} catch (Exception e) {
				logger.error("UPDATE FAIL:An error occurred in update procedure..." + e.toString());
			}
			if (record != null && !record.isEmpty()) {
				resourcesPath = "updateVisit";
				fragmentsPath = "fragments/visitForm";
				model.addAttribute("visitCollector", visit);
			} else {
				resourcesPath = "queryResponse";
				fragmentsPath = "fragments/response";
				model.addAttribute("queryAnsware", "empty");
			}
		}
		setModel(model);
		return "index";
	}

	protected String[] createQuery(VisitCollector visit) {
		SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest256();
		byte[] digest = digestSHA3.digest(visit.getFileName().getBytes());
		String[] query = new String[7];
		query[0] = "createVisit";
		query[1] = Hex.toHexString(digest);
		query[2] = visit.getUserName();
		query[3] = visit.getAgency();
		query[4] = visit.getDate();
		query[5] = visit.getTime();
		if (visit.getIdVisit() != null) {
			query[6] = visit.getIdVisit();
		} else {
			query[6] = "";
		}
		return query;
	}

	//

}
