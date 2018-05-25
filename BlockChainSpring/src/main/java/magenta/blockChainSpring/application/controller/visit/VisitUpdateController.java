package magenta.blockChainSpring.application.controller.visit;

import java.security.NoSuchAlgorithmException;
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

import magenta.blockChainSpring.application.controller.login.Login;
import magenta.blockChainSpring.application.model.Items;
import magenta.blockChainSpring.application.model.SpringConstant;
import magenta.blockChainSpring.application.model.Visit;
import magenta.blockChainSpring.application.repository.BCRepository;
import magenta.blockChainSpring.application.service.parser.ParserStrategy;

@Controller
public class VisitUpdateController {
	@Autowired
	private ParserStrategy parserStrategy;

	private static final org.apache.log4j.Logger logger = LogManager.getLogger(VisitCreateController.class);

	@GetMapping("/update")
	public String update(Model model, HttpSession httpSession,
			@RequestParam(value = "idVisit", required = false) String idVisit) {
		BCRepository bcRepo = (BCRepository) httpSession.getAttribute("u1");
		if (bcRepo == null || !bcRepo.getLoginStatus()) {
			model.addAttribute(SpringConstant.fragmentsPath, "fragments/indexForm");
			model.addAttribute(SpringConstant.resourcesPath, "loginForm");
			model.addAttribute("login", new Login());
		} else {
			VisitCollector visitCollector = new VisitCollector();
			String jSonQueryAnsware = "";
			LinkedList<Items> record = null;
			try {
				String[] queryLedger = parserStrategy.getFormattedQuery(SpringConstant.visitFunction, idVisit);
				logger.info("queryLedgeer " + queryLedger);
				jSonQueryAnsware = bcRepo.queryDB(parserStrategy.getFormattedQuery(SpringConstant.visitFunction, idVisit));
				record = parserStrategy.getCommandParser(SpringConstant.visitFunction).execute(jSonQueryAnsware);
				Visit visit = (Visit) record.get(0);
				visitCollector.setDate(visit.getDate());
				visitCollector.setTime(visit.getTime());
				visitCollector.setIdVisit(idVisit);
				visitCollector.setAgency(bcRepo.getUserAgency());
				visitCollector.setUserName(bcRepo.getUserName());
				logger.info("sto loggando visit1.time" + visitCollector.getTime());

				model.addAttribute(SpringConstant.resourcesPath, "updateVisit");
				model.addAttribute(SpringConstant.fragmentsPath, "fragments/visitForm");
				model.addAttribute("visitCollector", visitCollector);
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute(SpringConstant.resourcesPath, "queryResponse");
				model.addAttribute(SpringConstant.fragmentsPath, "fragments/response");
				model.addAttribute("queryAnsware", "empty");
			}
		}
		return "index";
	}

	@PostMapping("/update")
	public String queryRunner(@ModelAttribute("visitCollector") VisitCollector visit, Model model,
			HttpSession httpSession) throws NoSuchAlgorithmException {
		BCRepository bcRepo = (BCRepository) httpSession.getAttribute("u1");
		if (bcRepo == null || !bcRepo.getLoginStatus()) {
			model.addAttribute(SpringConstant.fragmentsPath, "fragments/indexForm");
			model.addAttribute(SpringConstant.resourcesPath, "loginForm");
			model.addAttribute("login", new Login());
		} else {
			String jSonQueryAnsware = "";
			LinkedList<Items> record = null;
			String[] query = createQuery(visit);
			try {
				jSonQueryAnsware = bcRepo.queryDB(query);
				record = parserStrategy.getCommandParser("createVisit").execute(jSonQueryAnsware);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (record != null && record.size() > 0) {
				model.addAttribute(SpringConstant.resourcesPath, "updateVisit");
				model.addAttribute(SpringConstant.fragmentsPath, "fragments/visitForm");
				model.addAttribute("visitCollector", visit);
			} else {
				model.addAttribute(SpringConstant.resourcesPath, "queryResponse");
				model.addAttribute(SpringConstant.fragmentsPath, "fragments/response");
				model.addAttribute("queryAnsware", "empty");
			}
		}
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
