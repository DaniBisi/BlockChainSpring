package magenta.blockchainspring.application.controller.home;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import magenta.blockchainspring.application.controller.ControllerTest;
import magenta.blockchainspring.application.repository.BCRepository;
import magenta.blockchainspring.application.service.blockchainrepo.GetBlockChainRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
public class HomeControllerTest extends ControllerTest{

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	protected GetBlockChainRepository gBCRepository;

	protected MockHttpSession session;
	private BCRepository mockRepository;

	@Before
	public void setup() {
		session = new MockHttpSession();
		gBCRepository = mock(GetBlockChainRepository.class);
	}

	@Test
	public void testHomePage() throws Exception {
		mockMvc.perform(get("/").session(session)).andExpect(view().name("index"))
				.andExpect(model().attribute("resources", "loginForm"));
	}

	@Test
	public void testHomePageU1NotNullNotLoggedin() throws Exception {
		mockRepository = mock(BCRepository.class);
		when(mockRepository.getLoginStatus()).thenReturn(false);
		session.setAttribute("u1", mockRepository);
		mockMvc.perform(get("/").session(session)).andExpect(view().name("index"))
				.andExpect(model().attribute("resources", "loginForm"));
	}

	@Test
	public void testHomePageWithLogin() throws Exception {

		mockRepository = mock(BCRepository.class);
		when(mockRepository.getLoginStatus()).thenReturn(true);
		session.setAttribute("u1", mockRepository);
		mockMvc.perform(get("/").session(session)).andExpect(view().name("index"))
				.andExpect(model().attribute("resources", "queryForm"));
	}


}