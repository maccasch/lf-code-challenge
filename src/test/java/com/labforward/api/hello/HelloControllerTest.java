package com.labforward.api.hello;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import com.labforward.api.common.MVCIntegrationTest;
import com.labforward.api.hello.domain.Greeting;
import com.labforward.api.hello.service.HelloWorldService;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloControllerTest extends MVCIntegrationTest {

	private static final String	HELLO_LUKE		= "Hello Luke";

	private static final String	EMPTY_MESSAGE	= "";

	@Test
	@Order(1)
	public void getHelloWorldIsOKAndReturnsValidJSON() throws Exception {
		mockMvc.perform(get("/hello"))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.id", is(HelloWorldService.DEFAULT_ID)))
		       .andExpect(jsonPath("$.message", is(HelloWorldService.DEFAULT_MESSAGE)));
	}

	@Test
	public void createGreetingReturnsBadRequestWhenMessageMissing() throws Exception {
		String body = "{}";
		mockMvc.perform(post("/hello").content(body)
		                              .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isUnprocessableEntity())
		       .andExpect(jsonPath("$.validationErrors", hasSize(1)))
		       .andExpect(jsonPath("$.validationErrors[*].field", contains("message")));
	}

	@Test
	public void createGreetingReturnsBadRequestWhenUnexpectedAttributeProvided() throws Exception {
		String body = "{ \"tacos\":\"value\" }}";
		mockMvc.perform(post("/hello").content(body).contentType(MediaType.APPLICATION_JSON))

				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.message", containsString("Bad Request")));
	}

	@Test
	public void createGreetingReturnsBadRequestWhenMessageEmptyString() throws Exception {
		Greeting emptyMessage = new Greeting(EMPTY_MESSAGE);
		final String body = getGreetingBody(emptyMessage);

		mockMvc.perform(post("/hello").content(body)
		                              .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isUnprocessableEntity())
		       .andExpect(jsonPath("$.validationErrors", hasSize(1)))
		       .andExpect(jsonPath("$.validationErrors[*].field", contains("message")));
	}

	@Test
	public void createGreetingIsOKWhenRequiredGreetingProvided() throws Exception {
		Greeting hello = new Greeting(HELLO_LUKE);
		final String body = getGreetingBody(hello);

		mockMvc.perform(post("/hello").contentType(MediaType.APPLICATION_JSON)
		                              .content(body))
				.andExpect(status().isCreated())
		       .andExpect(jsonPath("$.message", is(hello.getMessage())));
	}

	@Test
	public void updateGreetingIsOKWhenRequiredGreetingProvided() throws Exception {
		String id = "default";
		Greeting greeting = new Greeting(id, HelloWorldService.DEFAULT_MESSAGE);

		String body = getGreetingBody(greeting);

		mockMvc.perform(put("/hello/" + id)
				.contentType(MediaType.APPLICATION_JSON).content(body))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is(greeting.getMessage())))
				.andExpect(jsonPath("$.id", is(greeting.getId())));

	}

	@Test
	public void updateGreetingReturnsBadRequestWhenMessageMissing() throws Exception {
		String id = "default";
		String body = "{}";

		mockMvc.perform(put("/hello/" + id)
				.contentType(MediaType.APPLICATION_JSON).content(body))

				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.validationErrors", hasSize(1)))
				.andExpect(jsonPath("$.validationErrors[*].field", contains("id")));
	}

	@Test
	public void updateGreetingReturnsBadRequestWhenMessageEmptyString() throws Exception {
		String id = "default";
		Greeting emptyMessage = new Greeting(id, EMPTY_MESSAGE);
		final String body = getGreetingBody(emptyMessage);

		mockMvc.perform(put("/hello/" + id)
				.content(body).contentType(MediaType.APPLICATION_JSON))

				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.validationErrors", hasSize(1)))
				.andExpect(jsonPath("$.validationErrors[*].field", contains("message")));
	}

	@Test
	public void updateGreetingReturnsBadRequestWhenUnexpectedAttributeProvided() throws Exception {
		String id = "default";
		String body = "{ \"tacos\":\"value\" }}";

		mockMvc.perform(put("/hello/" + id)
				.content(body).contentType(MediaType.APPLICATION_JSON))

				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.message", containsString("Bad Request")));
	}

	private String getGreetingBody(Greeting greeting) throws JSONException {
		JSONObject json = new JSONObject().put("message", greeting.getMessage());

		if (greeting.getId() != null) {
			json.put("id", greeting.getId());
		}

		return json.toString();
	}

}
