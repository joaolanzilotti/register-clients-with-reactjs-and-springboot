package com.corporation.apiclient.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiSwaggerConfig {

    @Bean
    public OpenAPI costumOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RestFul API-Clients with Java 17 and Spring Boot 3.0.5")
                        .version("v1.0")
                        .description("API Developed to System Clients - By Joao Lanzilotti")
                        .termsOfService("https://github.com/joaolanzilotti")
                        .license(new License().name("Apache 2.0").url("https://github.com/joaolanzilotti/api-client")));
    }

}
