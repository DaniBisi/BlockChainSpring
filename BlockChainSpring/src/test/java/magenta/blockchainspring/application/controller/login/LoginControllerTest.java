package magenta.blockchainspring.application.controller.login;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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

import magenta.blockchainspring.application.repository.BCRepository;
import magenta.blockchainspring.application.service.blockchainrepo.DbManager;
import magenta.blockchainspring.application.service.blockchainrepo.GetBlockChainRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(LoginController.class)
public class LoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	protected GetBlockChainRepository gBCRepository;
	@MockBean
	protected DbManager db;
	
	protected MockHttpSession session;
	private BCRepository mockRepository;

	@Before
	public void setup() {
		session = new MockHttpSession();
	}

	@Test
	public void testLoginPageWithLogin() throws Exception {

		mockRepository = mock(BCRepository.class);
		String passwd = "adminpw";
		when(db.getAgency()).thenReturn("Magenta");
		when(db.getOrganization()).thenReturn("Org1MSP");
		when(mockRepository.login(passwd)).thenReturn(true);
		when(gBCRepository.getBCRepository("admin","Magenta","Org1MSP")).thenReturn(mockRepository);
		mockMvc.perform(post("/login").param("userName", "admin").param("password", passwd))
				.andExpect(view().name("index")).andExpect(model().attribute("resources", "queryForm"));
	}


	@Test
	public void testLoginPageWithoutLogin() throws Exception {

		mockRepository = mock(BCRepository.class);
		String passwd = "adminpw";
		when(mockRepository.login(passwd)).thenReturn(false);
		when(gBCRepository.getBCRepository("admin","Magenta","Org1MSP")).thenReturn(mockRepository);
		mockMvc.perform(post("/login").param("userName", "admin").param("password", passwd))
				.andExpect(view().name("index")).andExpect(model().attribute("resources", "loginForm"));
	}
	@Test
	public void testLoginPageNullRepo() throws Exception {

		mockRepository = mock(BCRepository.class);
		String passwd = "adminpw";
		when(mockRepository.login(passwd)).thenReturn(false);
		when(gBCRepository.getBCRepository("admin","Magenta","Org1MSP")).thenReturn(null);
		mockMvc.perform(post("/login").param("userName", "admin").param("password", passwd))
				.andExpect(view().name("index")).andExpect(model().attribute("resources", "loginForm"));
	}


}