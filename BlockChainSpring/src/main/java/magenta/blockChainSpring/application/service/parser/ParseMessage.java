package magenta.blockChainSpring.application.service.parser;

import java.util.LinkedList;

import magenta.blockChainSpring.model.Items;
import magenta.blockChainSpring.model.Message;

public class ParseMessage implements ParseAnsware {

	@Override
	public LinkedList<Items> execute(String arguments) {
		LinkedList<Items> record = new LinkedList<Items>();
		Message m1 = new Message();
		m1.addMessage("Create Visit", "SUCCESSFULL");
		record.add(m1);
		return record;
	}

}
