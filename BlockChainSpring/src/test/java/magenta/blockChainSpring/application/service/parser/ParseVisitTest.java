package magenta.blockChainSpring.application.service.parser;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import magenta.blockChainSpring.application.model.Items;
import magenta.blockChainSpring.application.model.Visit;

public class ParseVisitTest {

	private ParseVisit p1;

	@Before
	public void Setup() {
		p1 = new ParseVisit();
	}

	@Test
	public void testOneVisit() {// l'assunsione che si fa in questo test è che il record json sia formattato
								// correttamente.
		LinkedList<Items> l1 = new LinkedList<Items>();
		LinkedList<Items> r1 = new LinkedList<Items>();
		l1.add(new Visit(null, "ec363bd2b1c7715e48ace871bb7fe64820ace657", "Magenta", "Admin", "10-04-18:9.49"));
		r1 = p1.execute(
				"{\"IDhash\":\"ec363bd2b1c7715e48ace871bb7fe64820ace657\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.49\",\"name\":\"Admin\"}");
		
		assertEquals(true, l1.get(0).equals(r1.get(0)));
	}

	@Test
	public void testParserOneVisitReturnEmptyList() {
		LinkedList<Items> r1 = new LinkedList<Items>();
		r1 = p1.execute("");
		
		assertEquals(0, r1.size());
	}


}
