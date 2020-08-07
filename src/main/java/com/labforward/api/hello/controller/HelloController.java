package com.labforward.api.hello.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.labforward.api.core.exception.ResourceNotFoundException;
import com.labforward.api.hello.domain.Greeting;
import com.labforward.api.hello.service.HelloWorldService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(description= "Operations on greetings (Get, Get by id, Create, Update)")
public class HelloController {

	public static final String GREETING_NOT_FOUND = "Greeting Not Found";

	private HelloWorldService helloWorldService;

	public HelloController(HelloWorldService helloWorldService) {
		this.helloWorldService = helloWorldService;
	}

	@ApiResponses(value = @ApiResponse(code = 200, message = "OK"))
	@ApiOperation(value = "Get default greeting", response = Greeting.class)
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Greeting helloWorld() {
		return getGreeting(HelloWorldService.DEFAULT_ID);
	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = GREETING_NOT_FOUND) })
	@ApiOperation(value = "Get greeting by id", response = Greeting.class)
	@RequestMapping(value = "/hello/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Greeting getGreeting(@PathVariable String id) {
		return helloWorldService.getGreeting(id)
		                        .orElseThrow(() -> new ResourceNotFoundException(GREETING_NOT_FOUND));
	}

	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 422, message = "Bad Request") })
	@ApiOperation(value = "Create greeting", response = Greeting.class)
	@RequestMapping(value = "/hello", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public Greeting createGreeting(@RequestBody Greeting request) {
		return helloWorldService.createGreeting(request);
	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 422, message = "Bad Request") })
	@ApiOperation(value = "Update existing greeting", response = Greeting.class)
	@RequestMapping(value = "/hello/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Greeting updateGreeting(@PathVariable String id, @RequestBody Greeting request) {
		return helloWorldService.updateGreeting(id, request);
	}
}
