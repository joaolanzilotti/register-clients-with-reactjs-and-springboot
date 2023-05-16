package com.corporation.apiclient.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;

@Configuration
public class OpenApiSwaggerConfig {

    @Bean
    public OpenAPI costumOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))

                .info(new Info()
                        .title("RestFul API-Clients with Java 17 and Spring Boot 3.0.5")
                        .version("v1.0")
                        .description("API Developed to System Clients - By Joao Lanzilotti")
                        .termsOfService("https://github.com/joaolanzilotti")
                        .license(new License().name("Apache 2.0").url("https://github.com/joaolanzilotti/api-client"))


                );
    }

}
