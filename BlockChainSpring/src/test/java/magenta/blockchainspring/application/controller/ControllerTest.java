package magenta.blockchainspring.application.controller;

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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import magenta.blockchainspring.application.controller.home.HomeController;
import magenta.blockchainspring.application.repository.BCRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
public class ControllerTest {

	protected MockHttpSession session;
	protected BCRepository mockRepository;
	protected String page;
	
	@Autowired
	protected MockMvc mockMvc;
	
	
	@Before
	public void setupFirst(){
		session = new MockHttpSession();
		mockRepository = mock(BCRepository.class);
		page = "";
	}
	
	@Test
	public void testUserNotLoggedIn() throws Exception {
		when(mockRepository.getLoginStatus()).thenReturn(false);
		session.setAttribute("u1", mockRepository);
		mockMvc.perform(get("/"+page).session(session)).andExpect(view().name("index")).andExpect(model().attribute("resources", "loginForm"))
				.andExpect(model().attributeExists("login"));
	}
	@Test
	public void testNullSession() throws Exception {
		session.setAttribute("u1", null);
		mockMvc.perform(get("/"+page).session(session)).andExpect(view().name("index")).andExpect(model().attribute("resources", "loginForm"))
				.andExpect(model().attributeExists("login"));
	}
}
