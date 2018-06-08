package magenta.blockchainspring.application.controller.login;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginControllerIT {

	private MockMvc mockMvc;

	@Autowired
	private LoginController controller;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testLoginPageWithLogin() throws Exception {
		mockMvc.perform(post("/login").param("userName", "admin").param("password", "adminpw"))
				.andExpect(view().name("index")).andExpect(model().attribute("resources", "queryForm"));
	}

	@Test
	public void testLoginPageWithWrongPassword() throws Exception {
		mockMvc.perform(post("/login").param("userName", "admin").param("password", "ciao"))
				.andExpect(view().name("index")).andExpect(model().attribute("resources", "loginForm"));
	}
	
	

}