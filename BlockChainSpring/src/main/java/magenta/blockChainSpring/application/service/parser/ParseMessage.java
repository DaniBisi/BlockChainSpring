package magenta.blockChainSpring.application.service.parser;

import java.util.LinkedList;

import magenta.blockChainSpring.application.model.Items;
import magenta.blockChainSpring.application.model.Message;

public class ParseMessage implements ParseAnsware {

	@Override
	public LinkedList<Items> execute(String arguments) {
		LinkedList<Items> record = new LinkedList<Items>();
		Message m1 = new Message();
		/*ANALIZE ARGUMENTS TO LOAD CORRECT MESSAGE*/
		m1.addMessage("Create Visit", "SUCCESSFULL");
		m1.addMessage("LastInsertedId", arguments);
		record.add(m1);
		return record;
	}

}
