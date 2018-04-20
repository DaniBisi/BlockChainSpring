package magenta.BlockChainSpring.Controller;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import magenta.BlockChainSpring.Application.AppUser;
import magenta.BlockChainSpring.Application.BCRepository;
import magenta.BlockChainSpring.Controller.SessionWebController;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SessionWebControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private SessionWebController controller;

	//@MockBean
	BCRepository s1;

	HFCAClient caClient;
	HFClient client;
	AppUser userLogged;
	ChaincodeID chainCodeId;
	private Channel c1;
	// Add WebApplicationContext field here

	@Before
	public void setup() {
		chainCodeId = ChaincodeID.newBuilder().setName("fabcar").build();
		caClient = mock(HFCAClient.class);
		client = mock(HFClient.class);
		userLogged = mock(AppUser.class);
		c1 = mock(Channel.class);
	}

	@Test
	public void contexLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

	@Test
	public void testWellcomeLoggedIn() throws Exception {

		// s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
		// s1 = Mockito.spy(new SessionWrapper(caClient, client, userLogged,
		// chainCodeId));
		when(s1.getLoginStatus()).thenReturn(true);
		when(s1.getUserName()).thenReturn("admin");

		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(model().attribute("user", "admin"));

		// verify(s1, times(1)).getLoginStatus();
	}
	@Test
	public void testWellcome() throws Exception {

		// s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
		// s1 = Mockito.spy(new SessionWrapper(caClient, client, userLogged,
		// chainCodeId));
		when(s1.getLoginStatus()).thenReturn(false);

		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(model().attribute("resources", "indexForm"));

		// verify(s1, times(1)).getLoginStatus();
	}
	
//	
//	@Test
//	public void testWellcomeU1null() throws Exception {
//
//		// s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
//		// s1 = Mockito.spy(new SessionWrapper(caClient, client, userLogged,
//		// chainCodeId));
//		s1 = null;
//
//		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(model().attribute("resources", "indexForm"));
//
//		// verify(s1, times(1)).getLoginStatus();
//	}
	
	
	@Test
	public void testLoginUSer() throws Exception {

		// s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
		// s1 = Mockito.spy(new SessionWrapper(caClient, client, userLogged,
		// chainCodeId));
		when(s1.login(anyString())).thenReturn(true);
		when(s1.getLoginStatus()).thenReturn(false);

		mockMvc.perform(post("/login")).andExpect(status().isOk()).andExpect(model().attribute("resources", "indexForm"));

		// verify(s1, times(1)).getLoginStatus();
	}
	
}