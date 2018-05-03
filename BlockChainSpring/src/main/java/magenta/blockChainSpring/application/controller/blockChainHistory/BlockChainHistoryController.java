package magenta.blockChainSpring.application.controller.blockChainHistory;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import javax.servlet.http.HttpSession;

import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.protobuf.InvalidProtocolBufferException;

import magenta.blockChainSpring.application.controller.login.Login;
import magenta.blockChainSpring.application.model.Items;
import magenta.blockChainSpring.application.model.Message;
import magenta.blockChainSpring.application.repository.BCRepository;
import magenta.blockChainSpring.application.service.parser.ParserStrategy;

@Controller
public class BlockChainHistoryController {
//	@Autowired
//	private ParserStrategy parserStrategy;


	@GetMapping("/chainhistory")
	public String chainHistoryRunner(Model model, HttpSession httpSession)
			throws NumberFormatException, InvalidArgumentException, ProposalException, InvalidProtocolBufferException, UnsupportedEncodingException {

		BCRepository u1 = (BCRepository) httpSession.getAttribute("u1");
		if (u1 != null && u1.getLoginStatus()) {
			String[] payload = u1.queryBlock();

			LinkedList<Items> record = new LinkedList<Items>();
			int i = 0;
			for (String data : payload) {
				Message m = new Message();
				m.addMessage("Rensponse from " + i, data);
				record.add(m);
				i = i++;
			}
			if (record.size() > 0) {
				model.addAttribute("records", record);
				model.addAttribute("queryAnsware", "boh");
				model.addAttribute("valName", record.get(0).getValName());
			} else {
				model.addAttribute("queryAnsware", "empty");
			}
			model.addAttribute("page", "fragments/response");
			model.addAttribute("resources", "queryResponse");

		} else {
			model.addAttribute("page", "fragments/indexForm");
			model.addAttribute("resources", "loginForm");
			model.addAttribute("login", new Login());
		}
		return "index";
	}
}
