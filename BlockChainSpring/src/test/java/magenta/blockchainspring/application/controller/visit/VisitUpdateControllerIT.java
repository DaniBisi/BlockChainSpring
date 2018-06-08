package magenta.blockchainspring.application.controller.visit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import magenta.blockchainspring.application.repository.BCRepository;
import magenta.blockchainspring.application.service.blockchainrepo.DbManager;
import magenta.blockchainspring.application.service.blockchainrepo.GetBlockChainRepository;

@RunWith(SpringRunner.class)
@SpringBootTest

public class VisitUpdateControllerIT {

	private MockMvc mockMvc;
	private MockHttpSession session;

	@Autowired
	GetBlockChainRepository getBlockChainRepository;
	@Autowired
	DbManager db;
	@Autowired
	private VisitUpdateController controller;
	private String page;

	public VisitUpdateControllerIT() {
		page = "update";

	}
	
	@Before
	public void setup() {
		session = new MockHttpSession();
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		
	}

	@Test
	public void testUpdatePage() throws Exception {
		BCRepository bcRepo = getBlockChainRepository.getBCRepository("admin", "Magenta", "Org1MSP");
		bcRepo.login("adminpw");
		bcRepo.setEventList(db.getEventList());
		bcRepo.setOrdererList(db.getOrderList());
		bcRepo.setPeerList(db.getPeerList());
		bcRepo.initChannel("mychannel");
		session.setAttribute("u1", bcRepo);
		mockMvc.perform(get("/" +page).session(session).param("idVisit", "1")).andExpect(view().name("index"))
				.andExpect(model().attribute("resources", "updateVisit"))
				.andExpect(model().attribute("page", "fragments/visitForm"))
				.andExpect(model().attributeExists("visitCollector"));
	}


	@Test
	public void testUpdatePagePost() throws Exception {
		BCRepository bcRepo = getBlockChainRepository.getBCRepository("admin", "Magenta", "Org1MSP");
		bcRepo.login("adminpw");
		bcRepo.setEventList(db.getEventList());
		bcRepo.setOrdererList(db.getOrderList());
		bcRepo.setPeerList(db.getPeerList());
		bcRepo.initChannel("mychannel");
		session.setAttribute("u1", bcRepo);
		mockMvc.perform(post("/" + page).session(session).param("userName", "userName").param("agency", "agency")
				.param("fileName", "fileName").param("date", "date").param("time", "time").param("idVisit", "idVisit"))
				.andExpect(view().name("index")).andExpect(model().attribute("resources", "updateVisit"))
				.andExpect(model().attribute("page", "fragments/visitForm"))
				.andExpect(model().attributeExists("visitCollector"));
	}

	@Test
	public void testPostNullSession() throws Exception {
		session.setAttribute("u1", null);
		mockMvc.perform(post("/"+page).session(session)).andExpect(view().name("index")).andExpect(model().attribute("resources", "loginForm"))
				.andExpect(model().attributeExists("login"));
	}

}
