package magenta.blockchainspring.application.controller.chainhistory;

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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;

import magenta.blockchainspring.application.controller.ControllerTest;
import magenta.blockchainspring.application.controller.chainhistory.ChainHistoryController;
import magenta.blockchainspring.application.model.Items;
import magenta.blockchainspring.application.model.Message;
import magenta.blockchainspring.application.repository.BCRepository;
import magenta.blockchainspring.application.service.blockchainrepo.GetBlockChainRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(ChainHistoryController.class)
public class ChainHistoryControllerTest extends ControllerTest {

	@MockBean
	protected GetBlockChainRepository gBCRepository;

	@Before
	public void setup() {
		session = new MockHttpSession();
		mockRepository = mock(BCRepository.class);
		this.page = "chainhistory";
	}

	@Test
	public void testChainHistoryNoMessage() throws Exception {

		when(mockRepository.getLoginStatus()).thenReturn(true);
		String[] payload = {};
		when(mockRepository.queryBlock()).thenReturn(payload);
		session.setAttribute("u1", mockRepository);
		mockMvc.perform(get("/chainhistory").session(session)).andExpect(view().name("index"))
				.andExpect(model().attribute("resources", "history"))
				.andExpect(model().attribute("page", "fragments/chainHistory"))
				.andExpect(model().attribute("queryAnsware", "empty"));
	}
	
	
	@Test
	public void testChainHistoryWithMessage() throws Exception {

		when(mockRepository.getLoginStatus()).thenReturn(true);
		String[] payload = {"ciao","miao"};
		when(mockRepository.queryBlock()).thenReturn(payload);
		session.setAttribute("u1", mockRepository);
		LinkedList<Items> record = new LinkedList<Items>();
		Message m = new Message();
		m.addMessage("Block " + "0", "ciao");
		record.add(m);
		m = new Message();
		m.addMessage("Block " + "1", "miao");
		record.add(m);
		mockMvc.perform(get("/chainhistory").session(session)).andExpect(view().name("index"))
				.andExpect(model().attribute("resources", "history"))
				.andExpect(model().attribute("page", "fragments/chainHistory"))
				.andExpect(model().attribute("blockNum", 2))
				.andExpect(model().attributeExists("records"));
	}

	

}