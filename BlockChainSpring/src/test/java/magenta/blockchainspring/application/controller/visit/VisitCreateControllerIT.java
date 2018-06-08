package magenta.blockchainspring.application.controller.visit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

public class VisitCreateControllerIT {

	private MockMvc mockMvc;
	private MockHttpSession session;

	@Autowired
	GetBlockChainRepository getBlockChainRepository;
	@Autowired
	DbManager db;
	@Autowired
	private VisitCreateController controller;

	@Before
	public void setup() {
		session = new MockHttpSession();
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testCreatePageWithLogin() throws Exception {
		BCRepository bcRepo = getBlockChainRepository.getBCRepository("admin", "Magenta", "Org1MSP");
		bcRepo.login("adminpw");
		session.setAttribute("u1", bcRepo);
		mockMvc.perform(get("/create").session(session)).andExpect(view().name("index"))
				.andExpect(model().attribute("resources", "createVisit"))
				.andExpect(model().attribute("page", "fragments/visitForm"))
				.andExpect(model().attributeExists("visitCollector"));
	}

}
