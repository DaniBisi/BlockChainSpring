package magenta.blockchainspring.application.controller.chainhistory;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import javax.servlet.http.HttpSession;

import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.protobuf.InvalidProtocolBufferException;

import magenta.blockchainspring.application.controller.login.Login;
import magenta.blockchainspring.application.model.Items;
import magenta.blockchainspring.application.model.Message;
import magenta.blockchainspring.application.model.SpringConstant;
import magenta.blockchainspring.application.repository.BCRepository;

@Controller
public class ChainHistoryController {

	@GetMapping("/chainhistory")
	public String chainHistoryRunner(Model model, HttpSession httpSession)
			throws InvalidArgumentException, ProposalException, InvalidProtocolBufferException, UnsupportedEncodingException {

		BCRepository u1 = (BCRepository) httpSession.getAttribute("u1");
		if (u1 != null && u1.getLoginStatus()) {
			String[] payload = u1.queryBlock();

			LinkedList<Items> record = new LinkedList<>();
			int i = 0;
			for (String data : payload) {
				Message m = new Message();
				m.addMessage("Block " + i, data);
				record.add(m);
				i = i+1;
			}
			if (!record.isEmpty()) {
				model.addAttribute("records", record);
				model.addAttribute("blockNum", i);
				model.addAttribute("valName", record.get(0).getValName());
			} else {
				model.addAttribute("queryAnsware", "empty");
			}
			model.addAttribute(SpringConstant.FRAGMENTSPATH, "fragments/chainHistory");
			model.addAttribute(SpringConstant.RESOURCESPATH, "history");

		} else {
			model.addAttribute(SpringConstant.FRAGMENTSPATH, "fragments/indexForm");
			model.addAttribute(SpringConstant.RESOURCESPATH, "loginForm");
			model.addAttribute("login", new Login());
		}
		return "index";
	}
}
