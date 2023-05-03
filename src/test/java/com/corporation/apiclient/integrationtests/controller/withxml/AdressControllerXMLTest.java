package com.corporation.apiclient.integrationtests.controller.withxml;

import com.corporation.apiclient.config.TestConfig;
import com.corporation.apiclient.integrationtests.dto.AdressDTO;
import com.corporation.apiclient.integrationtests.dto.security.AccountCredentialsDTO;
import com.corporation.apiclient.integrationtests.dto.security.TokenDTO;
import com.corporation.apiclient.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.restassured.RestAssured.given;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class) // Aplicando Minha Ordem para executar os testes
public class AdressControllerXMLTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static AdressDTO adressDTO;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        adressDTO = new AdressDTO();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonMappingException, JsonProcessingException {

        AccountCredentialsDTO client = new AccountCredentialsDTO("joaolanzilotti","admin123");

        var acessToken = given()
                .basePath("/auth/signin")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_XML)
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
                .setBasePath("/api/adress")
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
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_JP)
                .pathParam("id", 1L)
                .body(adressDTO)
                .when()
                .post("{id}")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        AdressDTO createdAdressDTO = objectMapper.readValue(content, AdressDTO.class);
        adressDTO = createdAdressDTO;

        Assertions.assertNotNull(createdAdressDTO);
        Assertions.assertNotNull(createdAdressDTO.getId());
        Assertions.assertNotNull(createdAdressDTO.getCity());
        Assertions.assertNotNull(createdAdressDTO.getStreet());
        Assertions.assertNotNull(createdAdressDTO.getState());
        Assertions.assertNotNull(createdAdressDTO.getNumber());
        Assertions.assertNotNull(createdAdressDTO.getDistrict());


        Assertions.assertEquals("Ubatuba", createdAdressDTO.getCity());
        Assertions.assertEquals("Rua das Flores", createdAdressDTO.getStreet());
        Assertions.assertEquals("SP", createdAdressDTO.getState());
        Assertions.assertEquals("50", createdAdressDTO.getNumber());
        Assertions.assertEquals("Mirim", createdAdressDTO.getDistrict());
    }

    @Test
    @Order(2)
    public void testUpdate() throws JsonMappingException, JsonProcessingException {
        adressDTO.setStreet("Adress Changed");
        adressDTO.setCity("Ubatuba");

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .pathParam("id", 1L)
                .body(adressDTO)
                .when()
                .post("{id}")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        AdressDTO persistedAdress = objectMapper.readValue(content, AdressDTO.class);
        adressDTO = persistedAdress;

        Assertions.assertNotNull(persistedAdress);
        Assertions.assertNotNull(persistedAdress.getId());
        Assertions.assertNotNull(persistedAdress.getCity());
        Assertions.assertNotNull(persistedAdress.getStreet());
        Assertions.assertNotNull(persistedAdress.getState());
        Assertions.assertNotNull(persistedAdress.getNumber());
        Assertions.assertNotNull(persistedAdress.getDistrict());

        Assertions.assertEquals("Ubatuba", persistedAdress.getCity());
        Assertions.assertEquals("Adress Changed", persistedAdress.getStreet());
        Assertions.assertEquals("SP", persistedAdress.getState());
        Assertions.assertEquals("50", persistedAdress.getNumber());
        Assertions.assertEquals("Mirim", persistedAdress.getDistrict());

    }

    @Test
    @Order(3)
    public void testFindById() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_JP)
                .pathParam("id", adressDTO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        AdressDTO createdAdressDTO = objectMapper.readValue(content, AdressDTO.class);
        adressDTO = createdAdressDTO;

        Assertions.assertNotNull(createdAdressDTO);
        Assertions.assertNotNull(createdAdressDTO.getId());
        Assertions.assertNotNull(createdAdressDTO.getCity());
        Assertions.assertNotNull(createdAdressDTO.getStreet());
        Assertions.assertNotNull(createdAdressDTO.getState());
        Assertions.assertNotNull(createdAdressDTO.getNumber());
        Assertions.assertNotNull(createdAdressDTO.getDistrict());


        Assertions.assertEquals("Ubatuba", createdAdressDTO.getCity());
        Assertions.assertEquals("Adress Changed", createdAdressDTO.getStreet());
        Assertions.assertEquals("SP", createdAdressDTO.getState());
        Assertions.assertEquals("50", createdAdressDTO.getNumber());
        Assertions.assertEquals("Mirim", createdAdressDTO.getDistrict());
    }

    @Test
    @Order(4)
    public void testDelete() throws JsonMappingException, JsonProcessingException {

        given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .pathParam("id", adressDTO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    public void testFindAll() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<AdressDTO> adress = objectMapper.readValue(content, new TypeReference<List<AdressDTO>>() {
        });

        AdressDTO foundAdressOne = adress.get(0);

        Assertions.assertNotNull(foundAdressOne);
        Assertions.assertNotNull(foundAdressOne.getId());
        Assertions.assertNotNull(foundAdressOne.getCity());
        Assertions.assertNotNull(foundAdressOne.getStreet());
        Assertions.assertNotNull(foundAdressOne.getState());
        Assertions.assertNotNull(foundAdressOne.getNumber());
        Assertions.assertNotNull(foundAdressOne.getDistrict());


        Assertions.assertEquals("Ubatuba", foundAdressOne.getCity());
        Assertions.assertEquals("Rua das Flores", foundAdressOne.getStreet());
        Assertions.assertEquals("SP", foundAdressOne.getState());
        Assertions.assertEquals("50", foundAdressOne.getNumber());
        Assertions.assertEquals("Mirim", foundAdressOne.getDistrict());
    }

    private void mockPerson() {
        adressDTO.setCity("Ubatuba");
        adressDTO.setDistrict("Mirim");
        adressDTO.setState("SP");
        adressDTO.setStreet("Rua das Flores");
        adressDTO.setNumber("50");

    }

}
