package magenta.blockChainSpring.application.service.parser;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class ParseStrategyTest {

	private ParserStrategy p1;

	@Before
	public void Setup() {
		p1 = new ParserStrategy();
	}

	@Test
	public void testQueryMessage() {
		String query = "createVisit";
		assertThat(p1.getCommandParser(query), instanceOf(ParseMessage.class));
	}

	@Test
	public void testQueryParseVisit() {
		String query = "queryVisit";
		assertThat(p1.getCommandParser(query), instanceOf(ParseVisit.class));
	}

	@Test
	public void testQueryAllVisits() {
		String query = "queryAllVisits";
		assertThat(p1.getCommandParser(query), instanceOf(ParseAllVisits.class));
	}

}
