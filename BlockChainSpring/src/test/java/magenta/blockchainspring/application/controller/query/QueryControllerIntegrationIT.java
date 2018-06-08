package magenta.blockchainspring.application.controller.query;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import magenta.blockchainspring.application.model.Items;
import magenta.blockchainspring.application.model.Visit;
import magenta.blockchainspring.application.repository.BCRepository;
import magenta.blockchainspring.application.service.blockchainrepo.DbManager;
import magenta.blockchainspring.application.service.blockchainrepo.GetBlockChainRepository;
import magenta.blockchainspring.application.service.parser.ParseAnsware;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryControllerIntegrationIT {
	private MockMvc mockMvc;
	private MockHttpSession session;

	@Autowired
	GetBlockChainRepository getBlockChainRepository;
	@Autowired
	DbManager db;
	@Autowired
	QueryController controller;

	@Before
	public void setup() {
		session = new MockHttpSession();
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testQueryPageWithLogin() throws Exception {
		BCRepository bcRepo = getBlockChainRepository.getBCRepository("admin", "Magenta", "Org1MSP");
		bcRepo.login("adminpw");
		bcRepo.setEventList(db.getEventList());
		bcRepo.setOrdererList(db.getOrderList());
		bcRepo.setPeerList(db.getPeerList());
		bcRepo.initChannel("mychannel");
		session.setAttribute("u1", bcRepo);
		mockMvc.perform(get("/query").session(session).param("queryName", "queryAllVisits").param("args", ""))
				.andExpect(view().name("index")).andExpect(model().attributeExists("records"))
				.andExpect(model().attributeExists("queryAnsware"));
	}

	@Test
	public void testQueryPageWithoutLogin() throws Exception {
		BCRepository bcRepo = getBlockChainRepository.getBCRepository("admin", "Magenta", "Org1MSP");
		session.setAttribute("u1", bcRepo);
		mockMvc.perform(get("/query").session(session).param("queryName", "queryAllVisits").param("args", ""))
				.andExpect(view().name("index")).andExpect(model().attribute("resources", "loginForm"))
				.andExpect(model().attributeExists("login"));
	}

	@Test
	public void testWrongQueryPageReturnDefault() throws Exception {
		BCRepository bcRepo = getBlockChainRepository.getBCRepository("admin", "Magenta", "Org1MSP");
		bcRepo.login("adminpw");
		bcRepo.setEventList(db.getEventList());
		bcRepo.setOrdererList(db.getOrderList());
		bcRepo.setPeerList(db.getPeerList());
		bcRepo.initChannel("mychannel");
		session.setAttribute("u1", bcRepo);
		mockMvc.perform(get("/query").session(session).param("queryName", "inventedQuery").param("args", ""))
				.andExpect(view().name("index")).andExpect(model().attributeExists("records"))
				.andExpect(model().attributeExists("queryAnsware"));
	}

}
