package com.projeto.abilities.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import com.projeto.abilities.utils.ReadJsonFileToJsonObject;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;

@OpenAPIDefinition
@Configuration
@SecurityScheme(name = "token", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class OpenApiConfig {

	@Bean
	public OpenAPI baseOpenAPI() throws IOException {

		ReadJsonFileToJsonObject readJsonFileToJsonObject = new ReadJsonFileToJsonObject();

		ApiResponse badRequestAPI = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example()
										.value(readJsonFileToJsonObject.read()
												.get("badRequestResponse")
												.toString()))))
				.description("Bad Request!");

		ApiResponse badCredentialsAPI = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example().value(
										readJsonFileToJsonObject.read().get(
												"badCredentialsResponse")
												.toString()))))
				.description("Bad Credentials!");

		ApiResponse forbiddenAPI = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example()
										.value(readJsonFileToJsonObject.read()
												.get("forbiddenResponse")
												.toString()))))
				.description("Forbidden!");

		ApiResponse unprocessableEntityAPI = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example()
										.value(readJsonFileToJsonObject.read()
												.get("unprocessableEntityResponse")
												.toString()))))
				.description("unprocessableEntity!");

		ApiResponse internalServerErrorAPI = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example()
										.value(readJsonFileToJsonObject.read()
												.get("internalServerError")
												.toString()))))
				.description("Internal Server Error!");

		Components components = new Components();
		components.addResponses("BadRequest", badRequestAPI);
		components.addResponses("badcredentials", badCredentialsAPI);
		components.addResponses("forbidden", forbiddenAPI);
		components.addResponses("unprocessableEntity", unprocessableEntityAPI);
		components.addResponses("internalServerError", internalServerErrorAPI);

		return new OpenAPI()
				.components(components)
				.info(new Info().title("projeto Service")
						.version("V0.0.1")
						.description("API projetoAbility")
						.contact(new Contact().name("Suporte projeto")
								.email("projeto@gmail.com"))
						.license(new License().name("Apache 2.0")
								.url("https://www.apache.org/licenses/LICENSE-2.0.html")));
	}

}

// Esse código é uma classe de configuração para gerar a especificação OpenAPI
// (anteriormente conhecida como Swagger) de uma API Spring Boot. Ele usa a
// anotação "@OpenAPIDefinition" para especificar que é uma definição OpenAPI.
// Também usa a anotação "@SecurityScheme" para definir um esquema de segurança
// chamado "token" do tipo HTTP, com formato Bearer JWT.

// A classe contém um método chamado "baseOpenAPI" que cria um objeto OpenAPI.
// Dentro desse método, são adicionadas várias respostas de exemplo à
// configuração de respostas padrão usando o objeto ApiResponse. Cada resposta é
// associada a um código de status HTTP específico e uma descrição
// correspondente. Além disso, é adicionado um objeto Components que contém as
// respostas adicionadas, que podem ser referenciadas posteriormente em outros
// lugares da especificação.

// Por fim, é definido um objeto OpenAPI contendo informações básicas sobre a
// API, como o título, a versão e uma descrição. Ele também inclui informações
// de contato e uma licença associada à API.
