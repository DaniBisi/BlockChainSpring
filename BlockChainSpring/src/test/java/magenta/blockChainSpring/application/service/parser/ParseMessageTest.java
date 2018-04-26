package magenta.blockChainSpring.application.service.parser;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import magenta.blockChainSpring.model.Items;
import magenta.blockChainSpring.model.Message;

public class ParseMessageTest {

	private ParseMessage p1;

	@Before
	public void Setup() {
		p1 = new ParseMessage();
	}

	@Test
	public void testOneMessage() {// l'assunsione che si fa in questo test è che il record json sia formattato
								// correttamente.
		LinkedList<Items> l1 = new LinkedList<Items>();
		LinkedList<Items> r1 = new LinkedList<Items>();
		Message m1 = new Message();

		m1.addMessage("Create Visit", "SUCCESSFULL");
		m1.addMessage("LastInsertedId", "0");
		l1.add(m1);
		r1 = p1.execute("0");
		boolean result = checListEquals(l1, r1);
		assertEquals(true, result);
	}


	private boolean checListEquals(LinkedList<Items> l1, LinkedList<Items> r1) {
		boolean result = true;
		for (int i = 0; i < l1.size(); i++) {
			if (!l1.get(i).equals(r1.get(i))) {
				result = false;
			}
		}
		return result;
	}

}
