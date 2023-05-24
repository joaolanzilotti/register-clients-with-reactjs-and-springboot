package com.corporation.apiclient.integrationtests.repositories;

import com.corporation.apiclient.entities.User;
import com.corporation.apiclient.integrationtests.testcontainers.AbstractIntegrationTest;
import com.corporation.apiclient.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Aplicando Minha Ordem para executar os testes
public class UserRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    public UserRepository userRepository;

    private static User user;

    @BeforeAll
    public static void setup(){
        user = new User();
    }

    @Test
    @Order(1)
    public void testFindClientByName() throws JsonMappingException, JsonProcessingException {

        Pageable pageable = PageRequest.of(0, 15, Sort.by(Sort.Direction.ASC, "name"));
        user = userRepository.findUserByNamePage("wilt", pageable).getContent().get(0);

        Assertions.assertNotNull(user.getId());
        Assertions.assertNotNull(user.getName());
        Assertions.assertNotNull(user.getCellphone());
        Assertions.assertNotNull(user.getRg());
        Assertions.assertNotNull(user.getCpf());
        Assertions.assertNotNull(user.getPassword());
        Assertions.assertNotNull(user.getBirthDay());

        Assertions.assertEquals(9, user.getId());
        Assertions.assertEquals("wmeachan2@shop-pro.jp", user.getEmail());
        Assertions.assertEquals("Wilt", user.getName());
        Assertions.assertEquals("9MV4nQYcezp", user.getPassword());
        Assertions.assertEquals("9262435509", user.getCpf());
        Assertions.assertEquals("7921787143", user.getRg());
        Assertions.assertEquals("9859933663", user.getCellphone());

    }

    @Test
    @Order(2)
    public void testDisableClient() throws JsonMappingException, JsonProcessingException {

        userRepository.updateDisableClient(user.getId());

        Pageable pageable = PageRequest.of(0, 15, Sort.by(Sort.Direction.ASC, "name"));
        user = userRepository.findUserByNamePage("wilt", pageable).getContent().get(0);

        Assertions.assertNotNull(user.getId());
        Assertions.assertNotNull(user.getName());
        Assertions.assertNotNull(user.getCellphone());
        Assertions.assertNotNull(user.getRg());
        Assertions.assertNotNull(user.getCpf());
        Assertions.assertNotNull(user.getPassword());
        Assertions.assertNotNull(user.getBirthDay());

        Assertions.assertFalse(user.isEnabled());

        Assertions.assertEquals(9, user.getId());
        Assertions.assertEquals("wmeachan2@shop-pro.jp", user.getEmail());
        Assertions.assertEquals("Wilt", user.getName());
        Assertions.assertEquals("9MV4nQYcezp", user.getPassword());
        Assertions.assertEquals("9262435509", user.getCpf());
        Assertions.assertEquals("7921787143", user.getRg());
        Assertions.assertEquals("9859933663", user.getCellphone());

    }


}
