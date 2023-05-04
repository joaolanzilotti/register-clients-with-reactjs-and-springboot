package com.corporation.apiclient.integrationtests.repositories;

import com.corporation.apiclient.config.TestConfig;
import com.corporation.apiclient.entities.Client;
import com.corporation.apiclient.integrationtests.dto.ClientDTO;
import com.corporation.apiclient.integrationtests.dto.wrappers.WrapperClientDTO;
import com.corporation.apiclient.integrationtests.testcontainers.AbstractIntegrationTest;
import com.corporation.apiclient.repositories.ClientRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static io.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Aplicando Minha Ordem para executar os testes
public class ClientRepositoryTest  extends AbstractIntegrationTest {

    @Autowired
    public ClientRepository clientRepository;

    private static Client client;

    @BeforeAll
    public static void setup(){
        client = new Client();
    }

    @Test
    @Order(1)
    public void testFindClientByName() throws JsonMappingException, JsonProcessingException {

        Pageable pageable = PageRequest.of(0, 15, Sort.by(Sort.Direction.ASC, "name"));
        client = clientRepository.findClientsByName("wilt", pageable).getContent().get(0);

        Assertions.assertNotNull(client.getId());
        Assertions.assertNotNull(client.getName());
        Assertions.assertNotNull(client.getCellphone());
        Assertions.assertNotNull(client.getRg());
        Assertions.assertNotNull(client.getCpf());
        Assertions.assertNotNull(client.getPassword());
        Assertions.assertNotNull(client.getBirthDay());

        Assertions.assertEquals(9, client.getId());
        Assertions.assertEquals("wmeachan2@shop-pro.jp", client.getEmail());
        Assertions.assertEquals("Wilt", client.getName());
        Assertions.assertEquals("9MV4nQYcezp", client.getPassword());
        Assertions.assertEquals("9262435509", client.getCpf());
        Assertions.assertEquals("7921787143", client.getRg());
        Assertions.assertEquals("9859933663", client.getCellphone());

    }

    @Test
    @Order(2)
    public void testDisableClient() throws JsonMappingException, JsonProcessingException {

        clientRepository.updateDisableClient(client.getId());

        Pageable pageable = PageRequest.of(0, 15, Sort.by(Sort.Direction.ASC, "name"));
        client = clientRepository.findClientsByName("wilt", pageable).getContent().get(0);

        Assertions.assertNotNull(client.getId());
        Assertions.assertNotNull(client.getName());
        Assertions.assertNotNull(client.getCellphone());
        Assertions.assertNotNull(client.getRg());
        Assertions.assertNotNull(client.getCpf());
        Assertions.assertNotNull(client.getPassword());
        Assertions.assertNotNull(client.getBirthDay());

        Assertions.assertFalse(client.isEnabled());

        Assertions.assertEquals(9, client.getId());
        Assertions.assertEquals("wmeachan2@shop-pro.jp", client.getEmail());
        Assertions.assertEquals("Wilt", client.getName());
        Assertions.assertEquals("9MV4nQYcezp", client.getPassword());
        Assertions.assertEquals("9262435509", client.getCpf());
        Assertions.assertEquals("7921787143", client.getRg());
        Assertions.assertEquals("9859933663", client.getCellphone());

    }


}
