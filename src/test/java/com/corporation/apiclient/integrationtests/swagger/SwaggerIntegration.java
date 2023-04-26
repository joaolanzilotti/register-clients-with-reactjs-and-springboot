package com.corporation.apiclient.integrationtests.swagger;

import static io.restassured.RestAssured.given;

import com.corporation.apiclient.config.TestConfig;
import com.corporation.apiclient.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegration extends AbstractIntegrationTest {

    //Test da Conexao com o Site da Documentação
    @Test
    public void shouldDisplaySwaggerUiPage(){
        String content = given()
                .basePath("/swagger-ui/index.html")
                .port(TestConfig.SERVER_PORT)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        Assertions.assertTrue(content.contains("Swagger UI"));
    }

}
