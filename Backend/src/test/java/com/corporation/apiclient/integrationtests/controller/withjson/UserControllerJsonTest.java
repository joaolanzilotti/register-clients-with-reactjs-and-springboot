package com.corporation.apiclient.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.corporation.apiclient.config.TestConfig;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.integrationtests.dto.UserDTO;
import com.corporation.apiclient.integrationtests.dto.security.AccountCredentialsDTO;
import com.corporation.apiclient.integrationtests.dto.security.TokenDTO;
import com.corporation.apiclient.integrationtests.testcontainers.AbstractIntegrationTest;
import com.corporation.apiclient.integrationtests.dto.wrappers.WrapperUserDTO;
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
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class) // Aplicando Minha Ordem para executar os testes
public class UserControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static UserDTO userDTO;


    @Autowired
    private ModelMapper modelMapper;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        userDTO = new UserDTO();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonMappingException, JsonProcessingException {

        AccountCredentialsDTO user = new AccountCredentialsDTO("admin@admin.com","admin123");

        var acessToken = given()
                .basePath("/auth/signin")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class)
                .getToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + acessToken)
                .setBasePath("/api/users")
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
                .body(userDTO)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        UserDTO createdUserDTO = objectMapper.readValue(content, UserDTO.class);
        userDTO = createdUserDTO;

        Assertions.assertNotNull(createdUserDTO);
        Assertions.assertNotNull(createdUserDTO.getId());
        Assertions.assertNotNull(createdUserDTO.getName());
        Assertions.assertNotNull(createdUserDTO.getEmail());
        Assertions.assertNotNull(createdUserDTO.getRg());
        Assertions.assertNotNull(createdUserDTO.getCpf());
        Assertions.assertNotNull(createdUserDTO.getBirthDay());
        Assertions.assertNotNull(createdUserDTO.getCellphone());
        Assertions.assertTrue(createdUserDTO.isEnabled());


        Assertions.assertEquals("Joao", createdUserDTO.getName());
        Assertions.assertEquals("joao@gmail.com", createdUserDTO.getEmail());
        Assertions.assertEquals("45645", createdUserDTO.getRg());
        Assertions.assertEquals("92519732024", createdUserDTO.getCpf());
        Assertions.assertEquals(new Date(2023, 04, 27), createdUserDTO.getBirthDay());
        Assertions.assertEquals("123654848", createdUserDTO.getCellphone());
    }

    @Test
    @Order(2)
    public void testUpdate() throws JsonMappingException, JsonProcessingException {
        userDTO.setName("Name Changed");
        userDTO.setAdress(new Adress(1L, "maranhao", "district", "50", "Ubatuba", "SP", null));

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(userDTO)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        UserDTO persistedPerson = objectMapper.readValue(content, UserDTO.class);
        userDTO = persistedPerson;

        Assertions.assertNotNull(persistedPerson);
        Assertions.assertNotNull(persistedPerson.getId());
        Assertions.assertNotNull(persistedPerson.getName());
        Assertions.assertNotNull(persistedPerson.getEmail());
        Assertions.assertNotNull(persistedPerson.getRg());
        Assertions.assertNotNull(persistedPerson.getCpf());
        Assertions.assertNotNull(persistedPerson.getBirthDay());
        Assertions.assertNotNull(persistedPerson.getCellphone());
        Assertions.assertTrue(persistedPerson.isEnabled());

        Assertions.assertEquals("Name Changed", persistedPerson.getName());
        Assertions.assertEquals("joao@gmail.com", persistedPerson.getEmail());
        Assertions.assertEquals("45645", persistedPerson.getRg());
        Assertions.assertEquals("92519732024", persistedPerson.getCpf());
        Assertions.assertEquals(new Date(2023, 04, 27), persistedPerson.getBirthDay());
        Assertions.assertEquals("123654848", persistedPerson.getCellphone());

    }

    @Test
    @Order(3)
    public void testDisableUserById() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_JP)
                .pathParam("id", userDTO.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        UserDTO createdClientDTO = objectMapper.readValue(content, UserDTO.class);
        userDTO = createdClientDTO;

        Assertions.assertNotNull(createdClientDTO);
        Assertions.assertNotNull(createdClientDTO.getId());
        Assertions.assertNotNull(createdClientDTO.getName());
        Assertions.assertNotNull(createdClientDTO.getEmail());
        Assertions.assertNotNull(createdClientDTO.getRg());
        Assertions.assertNotNull(createdClientDTO.getCpf());
        Assertions.assertNotNull(createdClientDTO.getBirthDay());
        Assertions.assertNotNull(createdClientDTO.getCellphone());
        Assertions.assertNotNull(createdClientDTO.getAdress());
        Assertions.assertFalse(createdClientDTO.isEnabled());

        Assertions.assertEquals("Name Changed", createdClientDTO.getName());
        Assertions.assertEquals("joao@gmail.com", createdClientDTO.getEmail());
        Assertions.assertEquals("45645", createdClientDTO.getRg());
        Assertions.assertEquals("92519732024", createdClientDTO.getCpf());
        //Assertions.assertEquals(new Date(2023, 04, 27), createdClientDTO.getBirthDay());
        Assertions.assertEquals("123654848", createdClientDTO.getCellphone());
    }

    @Test
    @Order(4)
    public void testFindById() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_JP)
                .pathParam("id", userDTO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        UserDTO createdClientDTO = objectMapper.readValue(content, UserDTO.class);
        userDTO = createdClientDTO;

        Assertions.assertNotNull(createdClientDTO);
        Assertions.assertNotNull(createdClientDTO.getId());
        Assertions.assertNotNull(createdClientDTO.getName());
        Assertions.assertNotNull(createdClientDTO.getEmail());
        Assertions.assertNotNull(createdClientDTO.getRg());
        Assertions.assertNotNull(createdClientDTO.getCpf());
        Assertions.assertNotNull(createdClientDTO.getBirthDay());
        Assertions.assertNotNull(createdClientDTO.getCellphone());
        Assertions.assertNotNull(createdClientDTO.getAdress());
        Assertions.assertFalse(createdClientDTO.isEnabled());

        Assertions.assertEquals("Name Changed", createdClientDTO.getName());
        Assertions.assertEquals("joao@gmail.com", createdClientDTO.getEmail());
        Assertions.assertEquals("45645", createdClientDTO.getRg());
        Assertions.assertEquals("92519732024", createdClientDTO.getCpf());
        //Assertions.assertEquals(new Date(2023, 04, 27), createdClientDTO.getBirthDay());
        Assertions.assertEquals("123654848", createdClientDTO.getCellphone());
    }

    @Test
    @Order(5)
    public void testDelete() throws JsonMappingException, JsonProcessingException {

        given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .pathParam("id", userDTO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    public void testFindAll() throws JsonMappingException, JsonProcessingException {

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

        WrapperUserDTO wrapperUserDTO = objectMapper.readValue(content, WrapperUserDTO.class);
        List<UserDTO> user = wrapperUserDTO.getEmbedded().getUsers();

        UserDTO foundPersonOne = user.get(0);

        Assertions.assertNotNull(foundPersonOne.getId());
        Assertions.assertNotNull(foundPersonOne.getName());
        Assertions.assertNotNull(foundPersonOne.getCellphone());
        Assertions.assertNotNull(foundPersonOne.getRg());
        Assertions.assertNotNull(foundPersonOne.getCpf());
        Assertions.assertNotNull(foundPersonOne.getBirthDay());

        Assertions.assertEquals(2 , foundPersonOne.getId());
        Assertions.assertEquals("pedro545664564@gmail.com", foundPersonOne.getEmail());
        Assertions.assertEquals("Pedro", foundPersonOne.getName());
        Assertions.assertEquals("09113155865", foundPersonOne.getCpf());
        Assertions.assertEquals("5624987155", foundPersonOne.getRg());
        Assertions.assertEquals("1238334010", foundPersonOne.getCellphone());

    }

    @Test
    @Order(7)
    public void testFindClientByName() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .accept(TestConfig.CONTENT_TYPE_JSON)
                .pathParam("name", "pe")
                .queryParams("page", 0,"size", 15, "direction", "asc")
                .when()
                .get("findUserByName/{name}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperUserDTO wrapperUserDTO = objectMapper.readValue(content, WrapperUserDTO.class);
        List<UserDTO> user = wrapperUserDTO.getEmbedded().getUsers();

        UserDTO foundPersonOne = user.get(0);

        Assertions.assertNotNull(foundPersonOne.getId());
        Assertions.assertNotNull(foundPersonOne.getName());
        Assertions.assertNotNull(foundPersonOne.getCellphone());
        Assertions.assertNotNull(foundPersonOne.getRg());
        Assertions.assertNotNull(foundPersonOne.getCpf());
        Assertions.assertNotNull(foundPersonOne.getBirthDay());

        Assertions.assertEquals(14 , foundPersonOne.getId());
        Assertions.assertEquals("algamerjoao1@hotmail.com", foundPersonOne.getEmail());
        Assertions.assertEquals("Joao Pedro", foundPersonOne.getName());
        Assertions.assertEquals("48684998820", foundPersonOne.getCpf());
        Assertions.assertEquals("5656566", foundPersonOne.getRg());
        Assertions.assertEquals("12996598968", foundPersonOne.getCellphone());

    }

    @Test
    @Order(8)
    public void testHATEOAS() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .queryParams("page", 0,"size", 15)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/users/12\"}}}"));
        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/users/15\"}}}"));
        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/users/13\"}}}"));

        assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/users?page=0&size=15\"}"));

    }

    private void mockPerson() {
        userDTO.setName("Joao");
        userDTO.setEmail("joao@gmail.com");
        userDTO.setPassword("123");
        userDTO.setCpf("92519732024");
        userDTO.setRg("45645");
        userDTO.setBirthDay(new Date(2023, 4, 27));
        userDTO.setCellphone("123654848");
        userDTO.setEnabled(true);
        userDTO.setAccountNonLocked(true);
        userDTO.setAccountNonExpired(true);
        userDTO.setCredentialsNonExpired(true);
    }

}