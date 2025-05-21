package com.lucasasmuniz.devcatalog.config.doc;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@OpenAPIDefinition
@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI devCatalogApi() {
		return new OpenAPI()
				.info(new Info()
				           .title("DevCatalog API")
				           .description("DevCatalog Reference Project")
				           .version("v0.0.1")
				           .license(new License()
				           .name("Apache 2.0")
				           .url("https://github.com/lucasasmuniz/devcatalog"))
				           .contact(createContact()))
				.externalDocs(new ExternalDocumentation()
						.description("Github Repository")
						.url("https://github.com/lucasasmuniz/devcatalog"))
				.servers(List.of(
		                new Server()
		                    .url("https://github.com/lucasasmuniz/devcatalog")
		                    .description("Repositório GitHub"),
		                new Server()
		                    .url("http://localhost:8080")
		                    .description("Ambiente local")
		            ));

	}
	
	public Contact createContact() {
		return new Contact()
				.email("lucasmunizfk@gmail.com")
				.name("Lucas Muniz");
	}
}
