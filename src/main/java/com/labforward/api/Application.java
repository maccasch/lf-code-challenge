package com.labforward.api;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.labforward")).paths(PathSelectors.any()).build().useDefaultResponseMessages(false)
				.apiInfo(new ApiInfo("Labforward Code Challenge", "API's for Labforward Code Challenge", "1.0", null,
						new Contact("Mohamed Abdelrahman", "https://www.linkedin.com/in/mohamed-sherif-eg7/", "mo.abdelrahman07@gmail.com"), null, null, new ArrayList()));
	}
}
