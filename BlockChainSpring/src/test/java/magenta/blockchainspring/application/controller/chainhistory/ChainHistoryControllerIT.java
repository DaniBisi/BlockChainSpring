package magenta.blockchainspring.application.controller.chainhistory;

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
public class ChainHistoryControllerIT {
	private MockMvc mockMvc;
	private MockHttpSession session;

	@Autowired
	GetBlockChainRepository getBlockChainRepository;
	@Autowired
	DbManager db;
	@Autowired
	ChainHistoryController controller;
	

	@Before
	public void setup() {
		session = new MockHttpSession();
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testChainHistoryWithMessage() throws Exception {
		BCRepository bcRepo = getBlockChainRepository.getBCRepository("admin", "Magenta", "Org1MSP");
		bcRepo.login("adminpw");
		bcRepo.setEventList(db.getEventList());
		bcRepo.setOrdererList(db.getOrderList());
		bcRepo.setPeerList(db.getPeerList());
		bcRepo.initChannel("mychannel");
		session.setAttribute("u1", bcRepo);
		mockMvc.perform(get("/chainhistory").session(session)).andExpect(view().name("index"))
				.andExpect(model().attribute("resources", "history"))
				.andExpect(model().attribute("page", "fragments/chainHistory"))
				.andExpect(model().attributeExists("blockNum")).andExpect(model().attributeExists("records"));
	}

}