package com.tournament_organizer.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class Swagger {
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Sports organizer")
                        .version("1.0")
                        .description("API documentation for Sports organizer Project")
                        .contact(new Contact()
                                .name("Stefan, Alex, Alex")
                                .email("stefan.ivanov1227@gmail.com")
                                .url("http://localhost:8080"))
                        .license(new License()
                                .name("License")
                                .url("http://some-url.com"))
                        .termsOfService("http://some-url.com/terms"))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local ENV")));
    }
}