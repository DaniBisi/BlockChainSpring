package magenta.blockchainspring.application.controller.visit;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import magenta.blockchainspring.application.controller.ControllerTest;
import magenta.blockchainspring.application.controller.visit.VisitCollector;
import magenta.blockchainspring.application.controller.visit.VisitUpdateController;
import magenta.blockchainspring.application.model.Items;
import magenta.blockchainspring.application.model.Visit;
import magenta.blockchainspring.application.service.parser.ParseAnsware;
import magenta.blockchainspring.application.service.parser.ParserStrategy;

@RunWith(SpringRunner.class)
@WebMvcTest(VisitUpdateController.class)
public class VisitUpdateControllerTest extends ControllerTest {



	@MockBean
	protected ParserStrategy parserStrategy;

	@Before
	public void setup() {
		this.page = "update";
	}

	@Test
	public void testUpdatePage() throws Exception {
		LinkedList<Items> llItems = new LinkedList<Items>();
		Items employ = new Visit("0", "hashedId", "agency", "name", "date", "time");
		llItems.add(employ);
		when(mockRepository.getLoginStatus()).thenReturn(true);
		ParseAnsware pAnwsare = mock(ParseAnsware.class);
		String jsonAnswareMocked = "[{\"Key\":\"0\", \"Record\":{\"IDhash\":\"ec363bd2b1c7715e48ace871bb7fe64820ace657\",\"agency\":\"Magenta3.58\",\"date\":\"10-04-18:9.49\",\"name\":\"Admin\"}},{\"Key\":\"1\", \"Record\":{\"IDhash\":\"158ec78ab7b9190385379230587a7ca8e0d33383\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.48\",\"name\":\"User2\"}},{\"Key\":\"2\", \"Record\":{\"IDhash\":\"1b740424702841d0529c9af5c8478d25ead55379\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.47\",\"name\":\"User1\"}},{\"Key\":\"3\", \"Record\":{\"IDhash\":\"f480397b54c1b0f9bc403985eb4700amockItemsb1c672f\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.46\",\"name\":\"Admin\"}},{\"Key\":\"4\", \"Record\":{\"IDhash\":\"18281e6f75fmockItems412d0f2afa2894050677e4c456a\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.45\",\"name\":\"Admin\"}},{\"Key\":\"5\", \"Record\":{\"IDhash\":\"ee24822909bb95354f3f43c964a4b988c7de66a6\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.44\",\"name\":\"User2\"}},{\"Key\":\"6\", \"Record\":{\"IDhash\":\"343af2b08f306b8b407dd6d57d7a95b699479d1e\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.43\",\"name\":\"User1\"}},{\"Key\":\"7\", \"Record\":{\"IDhash\":\"mockItems3b501f310f19651757d46555223c82f6fdc4c1\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.42\",\"name\":\"Admin\"}},{\"Key\":\"8\", \"Record\":{\"IDhash\":\"a8ed3cac9e9280b664e409b6c5mockItems6ef969ad7f5a\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.42\",\"name\":\"User2\"}},{\"Key\":\"9\", \"Record\":{\"IDhash\":\"8c88abef1efda183eb66475f001ff4ea200266f9\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.41\",\"name\":\"Admin\"}}]";
		when(pAnwsare.execute(Mockito.anyString())).thenReturn(llItems);
		String[] args = { "QueryAllVisits", "" };
		when(parserStrategy.getFormattedQuery(Mockito.anyString(), Mockito.anyString())).thenReturn(args);
		when(parserStrategy.getCommandParser(Mockito.anyString())).thenReturn(pAnwsare);
		when(mockRepository.queryDB(args)).thenReturn(jsonAnswareMocked);
		session.setAttribute("u1", mockRepository);
		mockMvc.perform(get("/" + page).session(session).param("idVisit", "1")).andExpect(view().name("index"))
				.andExpect(model().attribute("resources", "updateVisit"))
				.andExpect(model().attribute("page", "fragments/visitForm"))
				.andExpect(model().attributeExists("visitCollector"));
	}

	/*
	 * private String userName; private String agency; private String fileName;
	 * private String date; private String time; private String idVisit;
	 */

	@Test
	public void testUpdatePagePost() throws Exception {
		LinkedList<Items> llItems = new LinkedList<Items>();
		Items employ = new Visit("0", "hashedId", "agency", "name", "date", "time");
		llItems.add(employ);
		when(mockRepository.getLoginStatus()).thenReturn(true);
		ParseAnsware pAnwsare = mock(ParseAnsware.class);
		String jsonAnswareMocked = "[{\"Key\":\"0\", \"Record\":{\"IDhash\":\"ec363bd2b1c7715e48ace871bb7fe64820ace657\",\"agency\":\"Magenta3.58\",\"date\":\"10-04-18:9.49\",\"name\":\"Admin\"}},{\"Key\":\"1\", \"Record\":{\"IDhash\":\"158ec78ab7b9190385379230587a7ca8e0d33383\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.48\",\"name\":\"User2\"}},{\"Key\":\"2\", \"Record\":{\"IDhash\":\"1b740424702841d0529c9af5c8478d25ead55379\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.47\",\"name\":\"User1\"}},{\"Key\":\"3\", \"Record\":{\"IDhash\":\"f480397b54c1b0f9bc403985eb4700amockItemsb1c672f\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.46\",\"name\":\"Admin\"}},{\"Key\":\"4\", \"Record\":{\"IDhash\":\"18281e6f75fmockItems412d0f2afa2894050677e4c456a\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.45\",\"name\":\"Admin\"}},{\"Key\":\"5\", \"Record\":{\"IDhash\":\"ee24822909bb95354f3f43c964a4b988c7de66a6\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.44\",\"name\":\"User2\"}},{\"Key\":\"6\", \"Record\":{\"IDhash\":\"343af2b08f306b8b407dd6d57d7a95b699479d1e\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.43\",\"name\":\"User1\"}},{\"Key\":\"7\", \"Record\":{\"IDhash\":\"mockItems3b501f310f19651757d46555223c82f6fdc4c1\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.42\",\"name\":\"Admin\"}},{\"Key\":\"8\", \"Record\":{\"IDhash\":\"a8ed3cac9e9280b664e409b6c5mockItems6ef969ad7f5a\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.42\",\"name\":\"User2\"}},{\"Key\":\"9\", \"Record\":{\"IDhash\":\"8c88abef1efda183eb66475f001ff4ea200266f9\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.41\",\"name\":\"Admin\"}}]";
		when(pAnwsare.execute(Mockito.anyString())).thenReturn(llItems);
		String[] args = { "createVisit", "0d5187834e6b3eefce86981a9d551b89a0741310a1f33a7e9e3f88bf6366425e", "userName",
				"agency", "date", "time", "idVisit" };
		when(parserStrategy.getCommandParser(Mockito.anyString())).thenReturn(pAnwsare);
		when(mockRepository.queryDB(args)).thenReturn(jsonAnswareMocked);
		session.setAttribute("u1", mockRepository);
		mockMvc.perform(post("/" + page).session(session).param("userName", "userName").param("agency", "agency")
				.param("fileName", "fileName").param("date", "date").param("time", "time").param("idVisit", "idVisit"))
				.andExpect(view().name("index")).andExpect(model().attribute("resources", "updateVisit"))
				.andExpect(model().attribute("page", "fragments/visitForm"))
				.andExpect(model().attributeExists("visitCollector"));
	}

	@Test
	public void testUpdatePagePostNullAnsware() throws Exception {

		when(mockRepository.getLoginStatus()).thenReturn(true);
		ParseAnsware pAnwsare = mock(ParseAnsware.class);
		String jsonAnswareMocked = "[{\"Key\":\"0\", \"Record\":{\"IDhash\":\"ec363bd2b1c7715e48ace871bb7fe64820ace657\",\"agency\":\"Magenta3.58\",\"date\":\"10-04-18:9.49\",\"name\":\"Admin\"}},{\"Key\":\"1\", \"Record\":{\"IDhash\":\"158ec78ab7b9190385379230587a7ca8e0d33383\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.48\",\"name\":\"User2\"}},{\"Key\":\"2\", \"Record\":{\"IDhash\":\"1b740424702841d0529c9af5c8478d25ead55379\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.47\",\"name\":\"User1\"}},{\"Key\":\"3\", \"Record\":{\"IDhash\":\"f480397b54c1b0f9bc403985eb4700amockItemsb1c672f\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.46\",\"name\":\"Admin\"}},{\"Key\":\"4\", \"Record\":{\"IDhash\":\"18281e6f75fmockItems412d0f2afa2894050677e4c456a\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.45\",\"name\":\"Admin\"}},{\"Key\":\"5\", \"Record\":{\"IDhash\":\"ee24822909bb95354f3f43c964a4b988c7de66a6\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.44\",\"name\":\"User2\"}},{\"Key\":\"6\", \"Record\":{\"IDhash\":\"343af2b08f306b8b407dd6d57d7a95b699479d1e\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.43\",\"name\":\"User1\"}},{\"Key\":\"7\", \"Record\":{\"IDhash\":\"mockItems3b501f310f19651757d46555223c82f6fdc4c1\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.42\",\"name\":\"Admin\"}},{\"Key\":\"8\", \"Record\":{\"IDhash\":\"a8ed3cac9e9280b664e409b6c5mockItems6ef969ad7f5a\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.42\",\"name\":\"User2\"}},{\"Key\":\"9\", \"Record\":{\"IDhash\":\"8c88abef1efda183eb66475f001ff4ea200266f9\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.41\",\"name\":\"Admin\"}}]";
		when(pAnwsare.execute(Mockito.anyString())).thenReturn(null);
		String[] args = { "createVisit", "0d5187834e6b3eefce86981a9d551b89a0741310a1f33a7e9e3f88bf6366425e", "userName",
				"agency", "date", "time", "idVisit" };
		when(parserStrategy.getCommandParser(Mockito.anyString())).thenReturn(pAnwsare);
		when(mockRepository.queryDB(args)).thenReturn(jsonAnswareMocked);
		session.setAttribute("u1", mockRepository);
		mockMvc.perform(post("/" + page).session(session).param("userName", "userName").param("agency", "agency")
				.param("fileName", "fileName").param("date", "date").param("time", "time").param("idVisit", "idVisit"))
				.andExpect(view().name("index")).andExpect(model().attribute("resources", "queryResponse"))
				.andExpect(model().attribute("page", "fragments/response"))
				.andExpect(model().attribute("queryAnsware", "empty"));
	}

	@Test
	public void testUpdatePagePostEmptyAnsware() throws Exception {
		LinkedList<Items> llItems = new LinkedList<Items>();
		when(mockRepository.getLoginStatus()).thenReturn(true);
		ParseAnsware pAnwsare = mock(ParseAnsware.class);
		String jsonAnswareMocked = "[{\"Key\":\"0\", \"Record\":{\"IDhash\":\"ec363bd2b1c7715e48ace871bb7fe64820ace657\",\"agency\":\"Magenta3.58\",\"date\":\"10-04-18:9.49\",\"name\":\"Admin\"}},{\"Key\":\"1\", \"Record\":{\"IDhash\":\"158ec78ab7b9190385379230587a7ca8e0d33383\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.48\",\"name\":\"User2\"}},{\"Key\":\"2\", \"Record\":{\"IDhash\":\"1b740424702841d0529c9af5c8478d25ead55379\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.47\",\"name\":\"User1\"}},{\"Key\":\"3\", \"Record\":{\"IDhash\":\"f480397b54c1b0f9bc403985eb4700amockItemsb1c672f\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.46\",\"name\":\"Admin\"}},{\"Key\":\"4\", \"Record\":{\"IDhash\":\"18281e6f75fmockItems412d0f2afa2894050677e4c456a\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.45\",\"name\":\"Admin\"}},{\"Key\":\"5\", \"Record\":{\"IDhash\":\"ee24822909bb95354f3f43c964a4b988c7de66a6\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.44\",\"name\":\"User2\"}},{\"Key\":\"6\", \"Record\":{\"IDhash\":\"343af2b08f306b8b407dd6d57d7a95b699479d1e\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.43\",\"name\":\"User1\"}},{\"Key\":\"7\", \"Record\":{\"IDhash\":\"mockItems3b501f310f19651757d46555223c82f6fdc4c1\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.42\",\"name\":\"Admin\"}},{\"Key\":\"8\", \"Record\":{\"IDhash\":\"a8ed3cac9e9280b664e409b6c5mockItems6ef969ad7f5a\",\"agency\":\"Unifi\",\"date\":\"10-04-18:9.42\",\"name\":\"User2\"}},{\"Key\":\"9\", \"Record\":{\"IDhash\":\"8c88abef1efda183eb66475f001ff4ea200266f9\",\"agency\":\"Magenta\",\"date\":\"10-04-18:9.41\",\"name\":\"Admin\"}}]";
		when(pAnwsare.execute(Mockito.anyString())).thenReturn(llItems);
		String[] args = { "createVisit", "0d5187834e6b3eefce86981a9d551b89a0741310a1f33a7e9e3f88bf6366425e", "userName",
				"agency", "date", "time", "idVisit" };
		when(parserStrategy.getCommandParser(Mockito.anyString())).thenReturn(pAnwsare);
		when(mockRepository.queryDB(args)).thenReturn(jsonAnswareMocked);
		session.setAttribute("u1", mockRepository);
		mockMvc.perform(post("/" + page).session(session).param("userName", "userName").param("agency", "agency")
				.param("fileName", "fileName").param("date", "date").param("time", "time").param("idVisit", "idVisit"))
				.andExpect(view().name("index")).andExpect(model().attribute("resources", "queryResponse"))
				.andExpect(model().attribute("page", "fragments/response"))
				.andExpect(model().attribute("queryAnsware", "empty"));
	}
	@Test
	public void testUpdateGetWithError() throws Exception {
		when(mockRepository.getLoginStatus()).thenReturn(true);
		String[] args = { "createVisit", "0d5187834e6b3eefce86981a9d551b89a0741310a1f33a7e9e3f88bf6366425e", "userName",
				"agency", "date", "time", "idVisit" };
		when(mockRepository.queryDB(args)).thenThrow(org.hyperledger.fabric.sdk.exception.InvalidArgumentException.class);
		session.setAttribute("u1", mockRepository);
		mockMvc.perform(get("/" + page).session(session).param("userName", "userName").param("agency", "agency")
				.param("fileName", "fileName").param("date", "date").param("time", "time").param("idVisit", "idVisit"))
				.andExpect(view().name("index")).andExpect(model().attribute("resources", "queryResponse"))
				.andExpect(model().attribute("page", "fragments/response"));
	}
	@Test
	public void testUpdatePagePostWithError() throws Exception {
		when(mockRepository.getLoginStatus()).thenReturn(true);
		String[] args = { "createVisit", "0d5187834e6b3eefce86981a9d551b89a0741310a1f33a7e9e3f88bf6366425e", "userName",
				"agency", "date", "time", "idVisit" };
		when(mockRepository.queryDB(args)).thenThrow(org.hyperledger.fabric.sdk.exception.InvalidArgumentException.class);
		session.setAttribute("u1", mockRepository);
		mockMvc.perform(post("/" + page).session(session).param("userName", "userName").param("agency", "agency")
				.param("fileName", "fileName").param("date", "date").param("time", "time").param("idVisit", "idVisit"))
				.andExpect(view().name("index")).andExpect(model().attribute("resources", "queryResponse"))
				.andExpect(model().attribute("page", "fragments/response"))
				.andExpect(model().attribute("queryAnsware", "empty"));
	}
	
	@Test
	public void testCreateQueryIdVisitNotNull() {
		VisitCollector visitCollector = new VisitCollector();
		visitCollector.setDate("date");
		visitCollector.setFileName("fileName");
		visitCollector.setTime("time");
		visitCollector.setIdVisit("idVisit");
		visitCollector.setAgency("agency");
		visitCollector.setUserName("userName");
		String[] args = { "createVisit", "0d5187834e6b3eefce86981a9d551b89a0741310a1f33a7e9e3f88bf6366425e", "userName",
				"agency", "date", "time", "idVisit" };
		VisitUpdateController vController = new VisitUpdateController();
		assertArrayEquals(args, vController.createQuery(visitCollector));
	}

	@Test
	public void testCreateQueryIdVisitNull() {
		VisitCollector visitCollector = new VisitCollector();
		visitCollector.setDate("date");
		visitCollector.setFileName("fileName");
		visitCollector.setTime("time");
		visitCollector.setIdVisit(null);
		visitCollector.setAgency("agency");
		visitCollector.setUserName("userName");
		String[] args = { "createVisit", "0d5187834e6b3eefce86981a9d551b89a0741310a1f33a7e9e3f88bf6366425e", "userName",
				"agency", "date", "time", "" };
		VisitUpdateController vController = new VisitUpdateController();
		assertArrayEquals(args, vController.createQuery(visitCollector));
	}
	
	@Test
	public void testPostNotUserLoggedIn() throws Exception {
		when(mockRepository.getLoginStatus()).thenReturn(false);
		session.setAttribute("u1", mockRepository);
		mockMvc.perform(post("/"+page).session(session)).andExpect(view().name("index")).andExpect(model().attribute("resources", "loginForm"))
				.andExpect(model().attributeExists("login"));
	}
	@Test
	public void testPostNullSession() throws Exception {
		session.setAttribute("u1", null);
		mockMvc.perform(post("/"+page).session(session)).andExpect(view().name("index")).andExpect(model().attribute("resources", "loginForm"))
				.andExpect(model().attributeExists("login"));
	}

}
