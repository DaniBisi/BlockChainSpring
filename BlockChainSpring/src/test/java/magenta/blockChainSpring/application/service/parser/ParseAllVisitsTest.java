package magenta.blockChainSpring.application.service.parser;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import magenta.blockChainSpring.model.Items;
import magenta.blockChainSpring.model.Visit;

public class ParseAllVisitsTest {

	private ParseAllVisits p1;

	@Before
	public void Setup() {
		p1 = new ParseAllVisits();
	}

	@Test
	public void testOneVisit() {// l'assunsione che si fa in questo test è che il record json sia formattato
								// correttamente.
		LinkedList<Items> l1 = new LinkedList<Items>();
		LinkedList<Items> r1 = new LinkedList<Items>();
		l1.add(new Visit("0", "ec363bd2b1c7715e48ace871bb7fe64820ace657", "Magenta", "Admin", "10-04-18:9.49"));
		r1 = p1.execute(
				"[{\"Key\":\"0\", \"Record\":{\"IDhash\":\"ec363bd2b1c7715e48ace871bb7fe64820ace657\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.49\",\"name\":\"Admin\"}}]");
		boolean result = checListEquals(l1, r1);
		assertEquals(true, result);
	}

	@Test
	public void testParserOneVisitReturnEmptyList() {
		LinkedList<Items> l1 = new LinkedList<Items>();
		LinkedList<Items> r1 = new LinkedList<Items>();
		r1 = p1.execute("");
		boolean result = checListEquals(l1, r1);
		assertEquals(true, result);
	}

	@Test
	public void testMoreVisits() {// l'assunsione che si fa in questo test è che il record json sia formattato
									// correttamente.
		LinkedList<Items> l1 = new LinkedList<Items>();
		LinkedList<Items> r1 = new LinkedList<Items>();
		l1.add(new Visit("0", "ec363bd2b1c7715e48ace871bb7fe64820ace657", "Magenta", "Admin", "10-04-18:9.49"));
		l1.add(new Visit("1", "ec363bd2b1c7715e48ace871bb7fe64820ace657", "Unifi", "Admin", "11-04-18:9.49"));
		r1 = p1.execute(
				"[{\"Key\":\"0\", \"Record\":{\"IDhash\":\"ec363bd2b1c7715e48ace871bb7fe64820ace657\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.49\",\"name\":\"Admin\"}},{\"Key\":\"1\", \"Record\":{\"IDhash\":\"ec363bd2b1c7715e48ace871bb7fe64820ace657\",\"agency\":\"Unifi\",\"date\":\"11-04-18:9.49\",\"name\":\"Admin\"}}]");
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
