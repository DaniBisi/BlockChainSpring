package magenta.BlockChainSpring.Controller;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceView;

import magenta.BlockChainSpring.Application.BCRepository;
import magenta.BlockChainSpring.Dao.QueryCollect;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
public class SessionWebControllerTest2 {

	private BCRepository mockRepository;

	// @MockBean
	// SessionWrapper s1;
	//
	// HFCAClient caClient;
	// HFClient client;
	// AppUser userLogged;
	// ChaincodeID chainCodeId;
	// private Channel c1;
	// Add WebApplicationContext field here
	@Test
	public void testHomePage() throws Exception {
		SessionWebController controller = new SessionWebController(null, "indexForm");
		MockMvc mockMvc = standaloneSetup(controller).build();
		mockMvc.perform(get("/")).andExpect(view().name("index"))
				.andExpect(model().attribute("resources", "loginForm"));
	}

	@Test
	public void testHomePageWithLogin() throws Exception {
		mockRepository = mock(BCRepository.class);
		when(mockRepository.getLoginStatus()).thenReturn(true);
		SessionWebController controller = new SessionWebController(mockRepository, "indexForm");
		MockMvc mockMvc = standaloneSetup(controller).build();
		mockMvc.perform(get("/")).andExpect(view().name("index"))
				.andExpect(model().attribute("u1", mockRepository));
	}

	@Test
	public void testQueryPageWithLogin() throws Exception {
		mockRepository = mock(BCRepository.class);
		String passwd = "adminpw";
		when(mockRepository.login(passwd)).thenReturn(true);
		SessionWebController controller = new SessionWebController(mockRepository, "indexForm");
		MockMvc mockMvc = standaloneSetup(controller).build();
		mockMvc.perform(post("/login").param("userName","admin").param("password","adminpw")).andExpect(view().name("index")).andExpect(model().attributeExists("user"))
				.andExpect(model().attribute("user", ("admin")));
	}

	// @Before
	// public void setup() {
	// chainCodeId = ChaincodeID.newBuilder().setName("fabcar").build();
	// caClient = mock(HFCAClient.class);
	// client = mock(HFClient.class);
	// userLogged = mock(AppUser.class);
	// c1 = mock(Channel.class);
	// }
	//
	// @Test
	// public void contexLoads() throws Exception {
	// assertThat(controller).isNotNull();
	// }
	//
	// @Test
	// public void testWellcomeLoggedIn() throws Exception {
	//
	// // s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
	// // s1 = Mockito.spy(new SessionWrapper(caClient, client, userLogged,
	// // chainCodeId));
	// when(s1.getLoginStatus()).thenReturn(true);
	// when(s1.getUserName()).thenReturn("admin");
	//
	// mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(model().attribute("user",
	// "admin"));
	//
	// // verify(s1, times(1)).getLoginStatus();
	// }
	// @Test
	// public void testWellcome() throws Exception {
	//
	// // s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
	// // s1 = Mockito.spy(new SessionWrapper(caClient, client, userLogged,
	// // chainCodeId));
	// when(s1.getLoginStatus()).thenReturn(false);
	//
	// mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(model().attribute("resources",
	// "indexForm"));
	//
	// // verify(s1, times(1)).getLoginStatus();
	// }
	//
	////
	//// @Test
	//// public void testWellcomeU1null() throws Exception {
	////
	//// // s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
	//// // s1 = Mockito.spy(new SessionWrapper(caClient, client, userLogged,
	//// // chainCodeId));
	//// s1 = null;
	////
	//// mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(model().attribute("resources",
	// "indexForm"));
	////
	//// // verify(s1, times(1)).getLoginStatus();
	//// }
	//
	//
	// @Test
	// public void testLoginUSer() throws Exception {
	//
	// // s1 = new SessionWrapper(caClient, client, userLogged, chainCodeId);
	// // s1 = Mockito.spy(new SessionWrapper(caClient, client, userLogged,
	// // chainCodeId));
	// when(s1.login(anyString())).thenReturn(true);
	// when(s1.getLoginStatus()).thenReturn(false);
	//
	// mockMvc.perform(post("/login")).andExpect(status().isOk()).andExpect(model().attribute("resources",
	// "indexForm"));
	//
	// // verify(s1, times(1)).getLoginStatus();
	// }

}