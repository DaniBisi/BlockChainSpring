package magenta.blockchainspring.application.service.parser;

import java.util.LinkedList;
import java.util.List;

import magenta.blockchainspring.application.model.Items;
import magenta.blockchainspring.application.model.Message;

public class ParseMessage implements ParseAnsware {

	@Override
	public List<Items> execute(String arguments) {
		LinkedList<Items> record = new LinkedList<>();
		Message m1 = new Message();
		/*ANALIZE ARGUMENTS TO LOAD CORRECT MESSAGE*/
		m1.addMessage("Create Visit", "SUCCESSFULL");
		m1.addMessage("LastInsertedId", arguments);
		record.add(m1);
		return record;
	}

}
