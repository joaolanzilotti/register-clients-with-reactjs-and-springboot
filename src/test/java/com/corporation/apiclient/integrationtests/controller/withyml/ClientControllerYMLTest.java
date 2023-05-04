package com.corporation.apiclient.integrationtests.controller.withyml;

import com.corporation.apiclient.config.TestConfig;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.integrationtests.controller.withyml.mapper.YMLMapper;
import com.corporation.apiclient.integrationtests.dto.ClientDTO;
import com.corporation.apiclient.integrationtests.dto.pagedmodels.PagedModelClient;
import com.corporation.apiclient.integrationtests.dto.security.AccountCredentialsDTO;
import com.corporation.apiclient.integrationtests.dto.security.TokenDTO;
import com.corporation.apiclient.integrationtests.testcontainers.AbstractIntegrationTest;
import com.corporation.apiclient.integrationtests.dto.wrappers.WrapperClientDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class) // Aplicando Minha Ordem para executar os testes
public class ClientControllerYMLTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;

    private static ClientDTO clientDTO;

    private static YMLMapper mapper;

    @BeforeAll
    public static void setup() {
        clientDTO = new ClientDTO();
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

        var persistedPerson = given().spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                TestConfig.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .accept(TestConfig.CONTENT_TYPE_YML)
                .body(clientDTO, mapper)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(ClientDTO.class, mapper);

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

        Assertions.assertEquals("Joao", persistedPerson.getName());
        Assertions.assertEquals("joao@gmail.com", persistedPerson.getEmail());
        Assertions.assertEquals("123", persistedPerson.getPassword());
        Assertions.assertEquals("45645", persistedPerson.getRg());
        Assertions.assertEquals("92519732024", persistedPerson.getCpf());
        Assertions.assertEquals(new Date(2023, 04, 27), persistedPerson.getBirthDay());
        Assertions.assertEquals("123654848", persistedPerson.getCellphone());
    }

    @Test
    @Order(2)
    public void testUpdate() throws JsonMappingException, JsonProcessingException {
        clientDTO.setName("name changed");
        clientDTO.setRg("65498798");
        clientDTO.setCpf("32451021004");
        clientDTO.setAdress(new Adress(1L, "maranhao", "district", "50", "Ubatuba", "SP", null));
        var persistedPerson = given().spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                TestConfig.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .accept(TestConfig.CONTENT_TYPE_YML)
                .body(clientDTO, mapper)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(ClientDTO.class, mapper);

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
        Assertions.assertNotNull(persistedPerson.getAdress());
        Assertions.assertTrue(persistedPerson.isEnabled());

        Assertions.assertEquals("name changed", persistedPerson.getName());
        Assertions.assertEquals("joao@gmail.com", persistedPerson.getEmail());
        Assertions.assertEquals("123", persistedPerson.getPassword());
        Assertions.assertEquals("65498798", persistedPerson.getRg());
        Assertions.assertEquals("32451021004", persistedPerson.getCpf());
        Assertions.assertEquals("123654848", persistedPerson.getCellphone());
    }

    @Test
    @Order(3)
    public void testDisableClientById() throws JsonMappingException, JsonProcessingException {

        var createdClientDTO = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_JP)
                .pathParam("id", clientDTO.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(ClientDTO.class, mapper);

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

        Assertions.assertEquals("name changed", createdClientDTO.getName());
        Assertions.assertEquals("joao@gmail.com", createdClientDTO.getEmail());
        Assertions.assertEquals("123", createdClientDTO.getPassword());
        Assertions.assertEquals("65498798", createdClientDTO.getRg());
        Assertions.assertEquals("32451021004", createdClientDTO.getCpf());
        //Assertions.assertEquals(new Date(2023, 04, 27), createdClientDTO.getBirthDay());
        Assertions.assertEquals("123654848", createdClientDTO.getCellphone());
    }

    @Test
    @Order(4)
    public void testFindById() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        var persistedPerson = given().spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                TestConfig.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .accept(TestConfig.CONTENT_TYPE_YML)
                .pathParam("id", clientDTO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(ClientDTO.class, mapper);

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
        Assertions.assertNotNull(persistedPerson.getAdress());
        Assertions.assertFalse(persistedPerson.isEnabled());

        Assertions.assertEquals(27, persistedPerson.getId());
        Assertions.assertEquals("name changed", persistedPerson.getName());
        Assertions.assertEquals("joao@gmail.com", persistedPerson.getEmail());
        Assertions.assertEquals("123", persistedPerson.getPassword());
        Assertions.assertEquals("65498798", persistedPerson.getRg());
        Assertions.assertEquals("32451021004", persistedPerson.getCpf());
        Assertions.assertEquals("123654848", persistedPerson.getCellphone());
    }

    @Test
    @Order(5)
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
                .pathParam("id", clientDTO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    public void testFindAll() throws JsonMappingException, JsonProcessingException {

        var wrapperClientDTO = given().spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                TestConfig.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .accept(TestConfig.CONTENT_TYPE_YML)
                .queryParams("page", 0,"size", 15, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelClient.class, mapper);

        List<ClientDTO> client = wrapperClientDTO.getContent();

        ClientDTO foundPersonOne = client.get(0);

        Assertions.assertNotNull(foundPersonOne.getId());
        Assertions.assertNotNull(foundPersonOne.getName());
        Assertions.assertNotNull(foundPersonOne.getCellphone());
        Assertions.assertNotNull(foundPersonOne.getRg());
        Assertions.assertNotNull(foundPersonOne.getCpf());
        Assertions.assertNotNull(foundPersonOne.getPassword());
        Assertions.assertNotNull(foundPersonOne.getBirthDay());

        Assertions.assertEquals(16 , foundPersonOne.getId());
        Assertions.assertEquals("apesterfield9@geocities.jp", foundPersonOne.getEmail());
        Assertions.assertEquals("Anne-marie", foundPersonOne.getName());
        Assertions.assertEquals("V1CenGtxxU0m", foundPersonOne.getPassword());
        Assertions.assertEquals("3087759756", foundPersonOne.getCpf());
        Assertions.assertEquals("2407557956", foundPersonOne.getRg());
        Assertions.assertEquals("9467202548", foundPersonOne.getCellphone());

        ClientDTO foundClientFive = client.get(5);

        Assertions.assertNotNull(foundClientFive.getId());
        Assertions.assertNotNull(foundClientFive.getName());
        Assertions.assertNotNull(foundClientFive.getCellphone());
        Assertions.assertNotNull(foundClientFive.getRg());
        Assertions.assertNotNull(foundClientFive.getCpf());
        Assertions.assertNotNull(foundClientFive.getPassword());
        Assertions.assertNotNull(foundClientFive.getBirthDay());

        Assertions.assertEquals(11 , foundClientFive.getId());
        Assertions.assertEquals("cpriestner4@army.mil", foundClientFive.getEmail());
        Assertions.assertEquals("Caren", foundClientFive.getName());
        Assertions.assertEquals("a89tcpL03", foundClientFive.getPassword());
        Assertions.assertEquals("8389871428", foundClientFive.getCpf());
        Assertions.assertEquals("2424153963", foundClientFive.getRg());
        Assertions.assertEquals("3799713605", foundClientFive.getCellphone());
    }

    @Test
    @Order(7)
    public void testFindClientByName() throws JsonMappingException, JsonProcessingException {

        var wrapperClientDTO = given().spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                TestConfig.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .accept(TestConfig.CONTENT_TYPE_YML)
                .pathParam("name", "pe")
                .queryParams("page", 0,"size", 15, "direction", "asc")
                .when()
                .get("findClientByName/{name}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelClient.class, mapper);

        List<ClientDTO> client = wrapperClientDTO.getContent();

        ClientDTO foundPersonOne = client.get(0);

        Assertions.assertNotNull(foundPersonOne.getId());
        Assertions.assertNotNull(foundPersonOne.getName());
        Assertions.assertNotNull(foundPersonOne.getCellphone());
        Assertions.assertNotNull(foundPersonOne.getRg());
        Assertions.assertNotNull(foundPersonOne.getCpf());
        Assertions.assertNotNull(foundPersonOne.getPassword());
        Assertions.assertNotNull(foundPersonOne.getBirthDay());

        Assertions.assertEquals(2 , foundPersonOne.getId());
        Assertions.assertEquals("pedro545664564@gmail.com", foundPersonOne.getEmail());
        Assertions.assertEquals("Pedro", foundPersonOne.getName());
        Assertions.assertEquals("9180", foundPersonOne.getPassword());
        Assertions.assertEquals("09113155865", foundPersonOne.getCpf());
        Assertions.assertEquals("5624987155", foundPersonOne.getRg());
        Assertions.assertEquals("1238334010", foundPersonOne.getCellphone());

    }

    @Test
    @Order(8)
    public void testHATEOAS() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .queryParams("page", 0,"size", 15, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/clients/12\"}}}"));
        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/clients/21\"}}}"));
        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/clients/26\"}}}"));

        assertTrue(content.contains("{\"first\":{\"href\":\"http://localhost:8888/api/clients?direction=asc&page=0&size=15&sort=name,asc\"}"));
        assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/clients?page=0&size=15&direction=asc\"}"));
        assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8888/api/clients?direction=asc&page=1&size=15&sort=name,asc\"}"));
        assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8888/api/clients?direction=asc&page=1&size=15&sort=name,asc\"}}"));

    }

    private static void mockPerson() {
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
