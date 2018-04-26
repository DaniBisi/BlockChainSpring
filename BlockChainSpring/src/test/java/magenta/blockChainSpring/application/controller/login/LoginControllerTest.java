package magenta.blockChainSpring.application.controller.login;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import magenta.blockChainSpring.application.controller.home.HomeController;
import magenta.blockChainSpring.application.repository.BCRepository;
import magenta.blockChainSpring.application.service.blockChainRepo.GetBlockChainRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(LoginController.class)
public class LoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	protected GetBlockChainRepository gBCRepository;

	protected MockHttpSession session;
	private BCRepository mockRepository;

	@Before
	public void setup() {
		session = new MockHttpSession();
		//gBCRepository = mock(GetBlockChainRepository.class);
	}

	@Test
	public void testLoginPageWithLogin() throws Exception {

		mockRepository = mock(BCRepository.class);
		String passwd = "adminpw";
		when(mockRepository.login(passwd)).thenReturn(true);
		when(gBCRepository.getBCRepository("admin")).thenReturn(mockRepository);
		mockMvc.perform(post("/login").param("userName", "admin").param("password", passwd))
				.andExpect(view().name("index")).andExpect(model().attribute("resources", "queryForm"));
	}


	@Test
	public void testLoginPageWithoutLogin() throws Exception {

		mockRepository = mock(BCRepository.class);
		String passwd = "adminpw";
		when(mockRepository.login(passwd)).thenReturn(false);
		when(gBCRepository.getBCRepository("admin")).thenReturn(mockRepository);
		mockMvc.perform(post("/login").param("userName", "admin").param("password", passwd))
				.andExpect(view().name("index")).andExpect(model().attribute("resources", "loginForm"));
	}
	@Test
	public void testLoginPageNullRepo() throws Exception {

		mockRepository = mock(BCRepository.class);
		String passwd = "adminpw";
		when(mockRepository.login(passwd)).thenReturn(false);
		when(gBCRepository.getBCRepository("admin")).thenReturn(null);
		mockMvc.perform(post("/login").param("userName", "admin").param("password", passwd))
				.andExpect(view().name("index")).andExpect(model().attribute("resources", "loginForm"));
	}


}