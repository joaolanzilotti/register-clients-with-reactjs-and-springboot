package com.corporation.apiclient.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.corporation.apiclient.config.TestConfig;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.integrationtests.dto.AdressDTO;
import com.corporation.apiclient.integrationtests.dto.ClientDTO;
import com.corporation.apiclient.integrationtests.dto.security.AccountCredentialsDTO;
import com.corporation.apiclient.integrationtests.dto.security.TokenDTO;
import com.corporation.apiclient.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

import java.util.Date;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class) // Aplicando Minha Ordem para executar os testes
public class ClientControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static ClientDTO clientDTO;

    private static AdressDTO adressDTO;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        clientDTO = new ClientDTO();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonMappingException, JsonProcessingException {

        AccountCredentialsDTO client = new AccountCredentialsDTO("joaolanzilotti","admin123");

        var acessToken = given()
                .basePath("/auth/signin")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(client)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class)
                .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + acessToken)
                .setBasePath("/api/clients")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

    }

    //Test da Conexao com o Site da Documentação
    @Test
    @Order(1)
    public void testCreate() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_JP)
                .body(clientDTO)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        ClientDTO createdClientDTO = objectMapper.readValue(content, ClientDTO.class);
        clientDTO = createdClientDTO;

        Assertions.assertNotNull(createdClientDTO);
        Assertions.assertNotNull(createdClientDTO.getId());
        Assertions.assertNotNull(createdClientDTO.getName());
        Assertions.assertNotNull(createdClientDTO.getEmail());
        Assertions.assertNotNull(createdClientDTO.getPassword());
        Assertions.assertNotNull(createdClientDTO.getRg());
        Assertions.assertNotNull(createdClientDTO.getCpf());
        Assertions.assertNotNull(createdClientDTO.getBirthDay());
        Assertions.assertNotNull(createdClientDTO.getCellphone());

        Assertions.assertEquals("Joao", createdClientDTO.getName());
        Assertions.assertEquals("joao@gmail.com", createdClientDTO.getEmail());
        Assertions.assertEquals("123", createdClientDTO.getPassword());
        Assertions.assertEquals("45645", createdClientDTO.getRg());
        Assertions.assertEquals("92519732024", createdClientDTO.getCpf());
        Assertions.assertEquals(new Date(2023, 04, 27), createdClientDTO.getBirthDay());
        Assertions.assertEquals("123654848", createdClientDTO.getCellphone());
    }

    @Test
    @Order(2)
    public void testCreateWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .header(TestConfig.HEADER_PARAM_ORIGIN, "https://www.urlerrada.com.br")
                .body(clientDTO)
                .when()
                .post()
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();


        Assertions.assertNotNull(content);
        Assertions.assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(3)
    public void testFindById() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_JP)
                .pathParam("id", 1L)
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        ClientDTO createdClientDTO = objectMapper.readValue(content, ClientDTO.class);
        clientDTO = createdClientDTO;

        Assertions.assertNotNull(createdClientDTO);
        Assertions.assertNotNull(createdClientDTO.getId());
        Assertions.assertNotNull(createdClientDTO.getName());
        Assertions.assertNotNull(createdClientDTO.getEmail());
        Assertions.assertNotNull(createdClientDTO.getPassword());
        Assertions.assertNotNull(createdClientDTO.getRg());
        Assertions.assertNotNull(createdClientDTO.getCpf());
        Assertions.assertNotNull(createdClientDTO.getBirthDay());
        Assertions.assertNotNull(createdClientDTO.getCellphone());
        Assertions.assertNotNull(createdClientDTO.getAdress());

        Assertions.assertEquals("Pedro", createdClientDTO.getName());
        Assertions.assertEquals("pedro54566564@gmail.com", createdClientDTO.getEmail());
        Assertions.assertEquals("9180", createdClientDTO.getPassword());
        Assertions.assertEquals("562498715", createdClientDTO.getRg());
        Assertions.assertEquals("48684998820", createdClientDTO.getCpf());
        //Assertions.assertEquals(new Date(2023, 04, 27), createdClientDTO.getBirthDay());
        Assertions.assertEquals("1238334010", createdClientDTO.getCellphone());
    }

    @Test
    @Order(4)
    public void testFindByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .header(TestConfig.HEADER_PARAM_ORIGIN, "https://www.urlerrada.com.br")
                .pathParam("id", clientDTO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();

        Assertions.assertNotNull(content);
        Assertions.assertEquals("Invalid CORS request", content);
    }

    private void mockPerson() {
        clientDTO.setName("Joao");
        clientDTO.setEmail("joao@gmail.com");
        clientDTO.setPassword("123");
        clientDTO.setCpf("92519732024");
        clientDTO.setRg("45645");
        clientDTO.setBirthDay(new Date(2023, 4, 27));
        clientDTO.setCellphone("123654848");
    }

}
