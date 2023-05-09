package com.corporation.apiclient.integrationtests.controller.withyml;

import com.corporation.apiclient.config.TestConfig;
import com.corporation.apiclient.integrationtests.controller.withyml.mapper.YMLMapper;
import com.corporation.apiclient.integrationtests.dto.AdressDTO;
import com.corporation.apiclient.integrationtests.dto.ClientDTO;
import com.corporation.apiclient.integrationtests.dto.pagedmodels.PagedModelAdress;
import com.corporation.apiclient.integrationtests.dto.security.AccountCredentialsDTO;
import com.corporation.apiclient.integrationtests.dto.security.TokenDTO;
import com.corporation.apiclient.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class) // Aplicando Minha Ordem para executar os testes
public class AdressControllerYMLTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;

    private static AdressDTO adressDTO;

    private static YMLMapper mapper;

    @BeforeAll
    public static void setup() {
        adressDTO = new AdressDTO();
        mapper = new YMLMapper();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonMappingException, JsonProcessingException {

        AccountCredentialsDTO client = new AccountCredentialsDTO("joaolanzilotti","admin123");

        var accessToken = given()
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                TestConfig.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .basePath("/auth/signin")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .accept(TestConfig.CONTENT_TYPE_YML)
                .body(client, mapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class, mapper)
                .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
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

        var createdAdressDTO = given().spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                TestConfig.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .accept(TestConfig.CONTENT_TYPE_YML)
                .pathParam("id", 4L)
                .body(adressDTO, mapper)
                .when()
                .post("{id}")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(AdressDTO.class, mapper);

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

        var createdAdressDTO = given().spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                TestConfig.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .accept(TestConfig.CONTENT_TYPE_YML)
                .pathParam("id", 4L)
                .body(adressDTO, mapper)
                .when()
                .post("{id}")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(AdressDTO.class, mapper);

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
    @Order(3)
    public void testFindById() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        var createdAdressDTO = given().spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                TestConfig.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .accept(TestConfig.CONTENT_TYPE_YML)
                .pathParam("id", adressDTO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(AdressDTO.class, mapper);

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
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                TestConfig.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .accept(TestConfig.CONTENT_TYPE_YML)
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
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                TestConfig.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .accept(TestConfig.CONTENT_TYPE_YML)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelAdress.class, mapper);

        List<AdressDTO> adress = content.getContent();

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

    @Test
    @Order(6)
    public void testHATEOAS() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .queryParams("page", 0,"size", 15, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/adress/2\"}}}"));
        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/adress/3\"}}}"));
        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/adress/4\"}}}"));

        assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/adress?page=0&size=15&direction=asc\"}"));

    }

    private void mockPerson() {
        adressDTO.setCity("Ubatuba");
        adressDTO.setDistrict("Mirim");
        adressDTO.setState("SP");
        adressDTO.setStreet("Rua das Flores");
        adressDTO.setNumber("50");

    }

}
