package magenta.blockChainSpring.application.controller.query;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import magenta.blockChainSpring.application.controller.ControllerTest;
import magenta.blockChainSpring.application.model.Items;
import magenta.blockChainSpring.application.model.Visit;
import magenta.blockChainSpring.application.repository.BCRepository;
import magenta.blockChainSpring.application.service.parser.ParseAnsware;
import magenta.blockChainSpring.application.service.parser.ParserStrategy;

@RunWith(SpringRunner.class)
@WebMvcTest(QueryController.class)
public class QueryControllerTest extends ControllerTest{

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	protected ParserStrategy parserStrategy;

	

	@Before
	public void setup() {
		this.page = "query";
		// gBCRepository = mock(GetBlockChainRepository.class);
	}

	@Test
	public void testQueryPageWithLogin() throws Exception {
		LinkedList<Items> llItems = new LinkedList<Items>();
		Items mockedEmploy = mock(Visit.class);
		llItems.add(mockedEmploy);
		mockRepository = mock(BCRepository.class);
		String[] args = { "QueryAllVisits", "" };
		ParseAnsware pAnwsare = mock(ParseAnsware.class);
		String jsonAnswareMocked = "[{\"Key\":\"0\", \"Record\":{\"IDhash\":\"ec363bd2b1c7715e48ace871bb7fe64820ace657\",\"agency\":\"Magenta3.58\",\"date\":\"10-04-18:9.49\",\"name\":\"Admin\"}},{\"Key\":\"1\", \"Record\":{\"IDhash\":\"158ec78ab7b9190385379230587a7ca8e0d33383\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.48\",\"name\":\"User2\"}},{\"Key\":\"2\", \"Record\":{\"IDhash\":\"1b740424702841d0529c9af5c8478d25ead55379\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.47\",\"name\":\"User1\"}},{\"Key\":\"3\", \"Record\":{\"IDhash\":\"f480397b54c1b0f9bc403985eb4700amockItemsb1c672f\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.46\",\"name\":\"Admin\"}},{\"Key\":\"4\", \"Record\":{\"IDhash\":\"18281e6f75fmockItems412d0f2afa2894050677e4c456a\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.45\",\"name\":\"Admin\"}},{\"Key\":\"5\", \"Record\":{\"IDhash\":\"ee24822909bb95354f3f43c964a4b988c7de66a6\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.44\",\"name\":\"User2\"}},{\"Key\":\"6\", \"Record\":{\"IDhash\":\"343af2b08f306b8b407dd6d57d7a95b699479d1e\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.43\",\"name\":\"User1\"}},{\"Key\":\"7\", \"Record\":{\"IDhash\":\"mockItems3b501f310f19651757d46555223c82f6fdc4c1\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.42\",\"name\":\"Admin\"}},{\"Key\":\"8\", \"Record\":{\"IDhash\":\"a8ed3cac9e9280b664e409b6c5mockItems6ef969ad7f5a\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.42\",\"name\":\"User2\"}},{\"Key\":\"9\", \"Record\":{\"IDhash\":\"8c88abef1efda183eb66475f001ff4ea200266f9\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.41\",\"name\":\"Admin\"}}]";
		when(pAnwsare.execute(Mockito.anyString())).thenReturn(llItems);
		when(parserStrategy.getFormattedQuery(Mockito.anyString(), Mockito.anyString())).thenReturn(args);
		when(parserStrategy.getCommandParser(Mockito.anyString())).thenReturn(pAnwsare);
		when(mockRepository.queryDB(args)).thenReturn(jsonAnswareMocked);
		when(mockRepository.getLoginStatus()).thenReturn(true);
		session.setAttribute("u1", mockRepository);
		mockMvc.perform(get("/query").session(session).param("query", "queryAllVisits").param("args", ""))
				.andExpect(view().name("index")).andExpect(model().attribute("records", llItems))
				.andExpect(model().attribute("queryAnsware", jsonAnswareMocked));
	}

	@Test
	public void testQueryPageWithoutLogin() throws Exception {
		mockRepository = mock(BCRepository.class);
		when(mockRepository.getLoginStatus()).thenReturn(false);
		session.setAttribute("u1", mockRepository);
		mockMvc.perform(get("/query").session(session).param("query", "queryAllVisits").param("args", ""))
				.andExpect(view().name("index")).andExpect(model().attribute("resources", "loginForm"))
				.andExpect(model().attributeExists("login"));
	}

	@Test
	public void testQueryPageWithLoginRecordNotEmpty() throws Exception {
		LinkedList<Items> llItems = new LinkedList<Items>();
		LinkedList<String> valNameMocked = new LinkedList<String>();
		valNameMocked.add("mockedmockItems");
		Items mockItems = mock(Items.class);
		when(mockItems.getValName()).thenReturn(valNameMocked);
		llItems.add(mockItems);
		mockRepository = mock(BCRepository.class);
		String[] args = { "QueryAllVisits", "" };
		ParseAnsware pAnwsare = mock(ParseAnsware.class);
		when(pAnwsare.execute(Mockito.anyString())).thenReturn(llItems);
		when(parserStrategy.getFormattedQuery(Mockito.anyString(), Mockito.anyString())).thenReturn(args);
		when(parserStrategy.getCommandParser(Mockito.anyString())).thenReturn(pAnwsare);
		when(mockRepository.queryDB(args)).thenReturn("ciao");
		when(mockRepository.getLoginStatus()).thenReturn(true);
		session.setAttribute("u1", mockRepository);
		mockMvc.perform(get("/query").session(session).param("query", "queryAllVisits").param("args", ""))
				.andExpect(view().name("index")).andExpect(model().attribute("records", llItems))
				.andExpect(model().attribute("queryAnsware", "ciao"))
				.andExpect(model().attribute("valName", valNameMocked));
	}

	@Test
	public void testQueryPageWithLoginHandledException() throws Exception {
		mockRepository = mock(BCRepository.class);
		String[] args = { "QueryAllVisits", "" };
		when(parserStrategy.getFormattedQuery(Mockito.anyString(), Mockito.anyString())).thenReturn(args);
		when(mockRepository.getLoginStatus()).thenReturn(true);
		when(mockRepository.queryDB(args))
				.thenThrow(new org.hyperledger.fabric.sdk.exception.InvalidArgumentException("expected"));
		session.setAttribute("u1", mockRepository);
		mockMvc.perform(get("/query").session(session).param("query", "queryAllVisits").param("args", ""))
				.andExpect(view().name("index"));
	}
	@Test
	public void testQueryPageRecordNull() throws Exception {
		mockRepository = mock(BCRepository.class);
		String[] args = { "QueryAllVisits", "" };
		ParseAnsware pAnwsare = mock(ParseAnsware.class);
		when(pAnwsare.execute(Mockito.anyString())).thenReturn(null);
		when(parserStrategy.getFormattedQuery(Mockito.anyString(), Mockito.anyString())).thenReturn(args);
		when(parserStrategy.getCommandParser(Mockito.anyString())).thenReturn(pAnwsare);
		
		when(mockRepository.getLoginStatus()).thenReturn(true);
		when(mockRepository.queryDB(args))
				.thenThrow(new org.hyperledger.fabric.sdk.exception.InvalidArgumentException("expected"));
		session.setAttribute("u1", mockRepository);
		mockMvc.perform(get("/query").session(session).param("query", "queryAllVisits").param("args", ""))
				.andExpect(view().name("index")).andExpect(model().attribute("queryAnsware","empty"));
	}
	@Test
	public void testQueryPageRecordEmpty() throws Exception {
		LinkedList<Items> llItems = new LinkedList<Items>();
		mockRepository = mock(BCRepository.class);
		String[] args = { "QueryAllVisits", "" };
		ParseAnsware pAnwsare = mock(ParseAnsware.class);
		when(pAnwsare.execute(Mockito.anyString())).thenReturn(llItems);
		when(parserStrategy.getFormattedQuery(Mockito.anyString(), Mockito.anyString())).thenReturn(args);
		when(parserStrategy.getCommandParser(Mockito.anyString())).thenReturn(pAnwsare);
		
		when(mockRepository.getLoginStatus()).thenReturn(true);
		when(mockRepository.queryDB(args))
				.thenThrow(new org.hyperledger.fabric.sdk.exception.InvalidArgumentException("expected"));
		session.setAttribute("u1", mockRepository);
		mockMvc.perform(get("/query").session(session).param("query", "queryAllVisits").param("args", ""))
				.andExpect(view().name("index")).andExpect(model().attribute("queryAnsware","empty"));
	}

}