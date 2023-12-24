package com.finnoapp.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.finnoapp")).build().apiInfo(apiInfo())
				.securityContexts(securityContexts()).securitySchemes(Arrays.asList(apiKeys()));
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("FinnoApp Api's Documentation").description("Articles , Images Api's")
				.version("1.0.0").build();
	}

	private ApiKey apiKeys() {
		return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
	}

	private List<SecurityContext> securityContexts() {

		return Arrays.asList(SecurityContext.builder().securityReferences(securityReferences()).build());

	}

	private List<SecurityReference> securityReferences() {
		AuthorizationScope scopes = new AuthorizationScope("global", "accessEverything");
		return Arrays.asList(new SecurityReference("JWT", new AuthorizationScope[] { scopes }));
	}

}
