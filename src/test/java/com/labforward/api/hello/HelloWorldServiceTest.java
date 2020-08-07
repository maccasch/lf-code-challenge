package com.labforward.api.hello;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.labforward.api.core.exception.EntityValidationException;
import com.labforward.api.hello.domain.Greeting;
import com.labforward.api.hello.service.HelloWorldService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloWorldServiceTest {

	@Autowired
	private HelloWorldService		helloService;

	private final String		EMPTY_MESSAGE		= "";

	private final String		GREETING_MESSAGE	= "Hello Luke";

	private final String		UPDATE_MESSAGE		= "Hello there!";

	private final Greeting		MOCK_REQUEST		= new Greeting(GREETING_MESSAGE);

	public HelloWorldServiceTest() {
	}

	@Test
	public void getDefaultGreetingIsOK() {
		Optional<Greeting> greeting = helloService.getDefaultGreeting();
		Assert.assertTrue(greeting.isPresent());
		Assert.assertEquals(HelloWorldService.DEFAULT_ID, greeting.get().getId());
		Assert.assertEquals(HelloWorldService.DEFAULT_MESSAGE, greeting.get().getMessage());
	}

	@Test(expected = EntityValidationException.class)
	public void createGreetingWithEmptyMessageThrowsException() {
		helloService.createGreeting(new Greeting(EMPTY_MESSAGE));
	}

	@Test(expected = EntityValidationException.class)
	public void createGreetingWithNullMessageThrowsException() {
		helloService.createGreeting(new Greeting(null));
	}

	@Test
	public void createGreetingOKWhenValidRequest() {
		Greeting created = helloService.createGreeting(MOCK_REQUEST);
		Assert.assertEquals(GREETING_MESSAGE, created.getMessage());
	}

	@Test
	public void updateGreetingOKWhenValidRequest() {
		Greeting created = helloService.createGreeting(MOCK_REQUEST);

		created.setMessage(UPDATE_MESSAGE);
		helloService.updateGreeting(created.getId(), created);

		Optional<Greeting> existing = helloService.getGreeting(created.getId());

		Assert.assertEquals(created.getMessage(), existing.get().getMessage());
	}

	@Test(expected = EntityValidationException.class)
	public void updateGreetingWithNullMessageThrowsException() {
		Greeting created = helloService.createGreeting(MOCK_REQUEST);

		created.setMessage(null);
		helloService.updateGreeting(created.getId(), created);
	}

	@Test(expected = EntityValidationException.class)
	public void updateGreetingWithEmptyMessageThrowsException() {
		Greeting created = helloService.createGreeting(MOCK_REQUEST);

		created.setMessage(EMPTY_MESSAGE);
		helloService.updateGreeting(created.getId(), created);
	}
}
