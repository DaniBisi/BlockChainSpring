package magenta.blockChainSpring.application.service.parser;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
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

	@Test
	public void testQueryLastInsertedId() {
		String query = "lastInsertedId";
		assertThat(p1.getCommandParser(query), instanceOf(ParseAllVisits.class));
	}
	@Test
	public void testQueryInvalid() {
		String query = "InvalidQuery";
		assertThat(p1.getCommandParser(query), instanceOf(ParseAllVisits.class));
	}

	@Test
	public void testGetFormattedQuery() {
		String query = "testQuery";
		String args = "args1 args2 args3";
		String []queryF = {"testQuery","args1", "args2", "args3"};
		assertEquals(p1.getFormattedQuery(query, args), queryF);	
	}
	@Test
	public void testGetFormattedQueryArgsNull() {
		String query = "testQuery";
		String args = null;
		String []queryF = {"testQuery"};
		assertEquals(p1.getFormattedQuery(query, args), queryF);	
	}

	@Test
	public void testGetFormattedQueryArgsEmpty() {
		String query = "testQuery";
		String args = "";
		String []queryF = {"testQuery"};
		assertEquals(p1.getFormattedQuery(query, args), queryF);	
	}

}
