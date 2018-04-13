package magenta.BlockChainSpring;

import java.util.LinkedList;

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
