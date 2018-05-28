package magenta.blockchainspring.application.controller.visit;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import magenta.blockchainspring.application.controller.ControllerTest;
import magenta.blockchainspring.application.controller.visit.VisitCreateController;

@RunWith(SpringRunner.class)
@WebMvcTest(VisitCreateController.class)
public class VisitCreateControllerTest extends ControllerTest {

	@Before
	public void setup() {
		this.page = "create";
	}

	@Test	
	public void testCreatePageWithLogin() throws Exception {
		when(mockRepository.getLoginStatus()).thenReturn(true);
		session.setAttribute("u1", mockRepository);
		mockMvc.perform(get("/"+page).session(session)).andExpect(view().name("index"))
				.andExpect(model().attribute("resources", "createVisit"))
				.andExpect(model().attribute("page", "fragments/visitForm")).andExpect(model().attributeExists("visitCollector"));
	}

}
