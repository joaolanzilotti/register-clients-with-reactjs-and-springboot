package com.corporation.apiclient.integrationtests.controller.withxml;

import com.corporation.apiclient.config.TestConfig;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.integrationtests.dto.ClientDTO;
import com.corporation.apiclient.integrationtests.dto.pagedmodels.PagedModelClient;
import com.corporation.apiclient.integrationtests.dto.security.AccountCredentialsDTO;
import com.corporation.apiclient.integrationtests.dto.security.TokenDTO;
import com.corporation.apiclient.integrationtests.testcontainers.AbstractIntegrationTest;
import com.corporation.apiclient.integrationtests.dto.wrappers.WrapperClientDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class) // Aplicando Minha Ordem para executar os testes
public class ClientControllerXMLTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;

    private static ClientDTO clientDTO;


    @Autowired
    private ModelMapper modelMapper;

    @BeforeAll
    public static void setup() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        clientDTO = new ClientDTO();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonMappingException, JsonProcessingException {

        AccountCredentialsDTO client = new AccountCredentialsDTO("joaolanzilotti", "admin123");

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
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .accept(TestConfig.CONTENT_TYPE_XML)
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
        Assertions.assertTrue(createdClientDTO.isEnabled());


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
    public void testUpdate() throws JsonMappingException, JsonProcessingException {
        clientDTO.setName("Name Changed");
        clientDTO.setAdress(new Adress(1L, "maranhao", "district", "50", "Ubatuba", "SP", null));

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .accept(TestConfig.CONTENT_TYPE_XML)
                .body(clientDTO)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        ClientDTO persistedPerson = objectMapper.readValue(content, ClientDTO.class);
        clientDTO = persistedPerson;

        Assertions.assertNotNull(persistedPerson);
        Assertions.assertNotNull(persistedPerson.getId());
        Assertions.assertNotNull(persistedPerson.getName());
        Assertions.assertNotNull(persistedPerson.getEmail());
        Assertions.assertNotNull(persistedPerson.getPassword());
        Assertions.assertNotNull(persistedPerson.getRg());
        Assertions.assertNotNull(persistedPerson.getCpf());
        Assertions.assertNotNull(persistedPerson.getBirthDay());
        Assertions.assertNotNull(persistedPerson.getCellphone());
        Assertions.assertTrue(persistedPerson.isEnabled());

        Assertions.assertEquals("Name Changed", persistedPerson.getName());
        Assertions.assertEquals("joao@gmail.com", persistedPerson.getEmail());
        Assertions.assertEquals("123", persistedPerson.getPassword());
        Assertions.assertEquals("45645", persistedPerson.getRg());
        Assertions.assertEquals("92519732024", persistedPerson.getCpf());
        Assertions.assertEquals(new Date(2023, 04, 27), persistedPerson.getBirthDay());
        Assertions.assertEquals("123654848", persistedPerson.getCellphone());

    }

    @Test
    @Order(3)
    public void testDisableClientById() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .accept(TestConfig.CONTENT_TYPE_XML)
                .pathParam("id", clientDTO.getId())
                .when()
                .patch("{id}")
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
        Assertions.assertFalse(createdClientDTO.isEnabled());

        Assertions.assertEquals("Name Changed", createdClientDTO.getName());
        Assertions.assertEquals("joao@gmail.com", createdClientDTO.getEmail());
        Assertions.assertEquals("123", createdClientDTO.getPassword());
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
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .accept(TestConfig.CONTENT_TYPE_XML)
                .pathParam("id", clientDTO.getId())
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
        Assertions.assertFalse(createdClientDTO.isEnabled());

        Assertions.assertEquals("Name Changed", createdClientDTO.getName());
        Assertions.assertEquals("joao@gmail.com", createdClientDTO.getEmail());
        Assertions.assertEquals("123", createdClientDTO.getPassword());
        Assertions.assertEquals("45645", createdClientDTO.getRg());
        Assertions.assertEquals("92519732024", createdClientDTO.getCpf());
        //Assertions.assertEquals(new Date(2023, 04, 27), createdClientDTO.getBirthDay());
        Assertions.assertEquals("123654848", createdClientDTO.getCellphone());
    }

    @Test
    @Order(5)
    public void testDelete() throws JsonMappingException, JsonProcessingException {

        given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .accept(TestConfig.CONTENT_TYPE_XML)
                .pathParam("id", clientDTO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    public void testFindAll() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .accept(TestConfig.CONTENT_TYPE_XML)
                .queryParams("page", 0,"size", 15, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PagedModelClient pagedModelClient = objectMapper.readValue(content, PagedModelClient.class);
        List<ClientDTO> client = pagedModelClient.getContent();

        ClientDTO foundPersonOne = client.get(0);

        Assertions.assertNotNull(foundPersonOne.getId());
        Assertions.assertNotNull(foundPersonOne.getName());
        Assertions.assertNotNull(foundPersonOne.getCellphone());
        Assertions.assertNotNull(foundPersonOne.getRg());
        Assertions.assertNotNull(foundPersonOne.getCpf());
        Assertions.assertNotNull(foundPersonOne.getPassword());
        Assertions.assertNotNull(foundPersonOne.getBirthDay());

        Assertions.assertEquals(446 , foundPersonOne.getId());
        Assertions.assertEquals("amonginc7@europa.eu", foundPersonOne.getEmail());
        Assertions.assertEquals("Abba", foundPersonOne.getName());
        Assertions.assertEquals("SUyOoxl8", foundPersonOne.getPassword());
        Assertions.assertEquals("2967812235", foundPersonOne.getCpf());
        Assertions.assertEquals("2626833328", foundPersonOne.getRg());
        Assertions.assertEquals("4809137261", foundPersonOne.getCellphone());

        ClientDTO foundClientFive = client.get(5);

        Assertions.assertNotNull(foundClientFive.getId());
        Assertions.assertNotNull(foundClientFive.getName());
        Assertions.assertNotNull(foundClientFive.getCellphone());
        Assertions.assertNotNull(foundClientFive.getRg());
        Assertions.assertNotNull(foundClientFive.getCpf());
        Assertions.assertNotNull(foundClientFive.getPassword());
        Assertions.assertNotNull(foundClientFive.getBirthDay());

        Assertions.assertEquals(362 , foundClientFive.getId());
        Assertions.assertEquals("atreamayne9v@blogspot.com", foundClientFive.getEmail());
        Assertions.assertEquals("Adolph", foundClientFive.getName());
        Assertions.assertEquals("XVKrD5", foundClientFive.getPassword());
        Assertions.assertEquals("3284196817", foundClientFive.getCpf());
        Assertions.assertEquals("6639511277", foundClientFive.getRg());
        Assertions.assertEquals("5594004016", foundClientFive.getCellphone());

    }
    private void mockPerson() {
        clientDTO.setName("Joao");
        clientDTO.setEmail("joao@gmail.com");
        clientDTO.setPassword("123");
        clientDTO.setCpf("92519732024");
        clientDTO.setRg("45645");
        clientDTO.setBirthDay(new Date(2023, 4, 27));
        clientDTO.setCellphone("123654848");
        clientDTO.setEnabled(true);
    }

}
