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
        user = userRepository.findUserByNamePage("Joao", pageable).getContent().get(0);

        Assertions.assertNotNull(user.getId());
        Assertions.assertNotNull(user.getName());
        Assertions.assertNotNull(user.getCellphone());
        Assertions.assertNotNull(user.getRg());
        Assertions.assertNotNull(user.getCpf());
        Assertions.assertNotNull(user.getBirthDay());

        Assertions.assertEquals(14, user.getId());
        Assertions.assertEquals("algamerjoao1@hotmail.com", user.getEmail());
        Assertions.assertEquals("Joao Pedro", user.getName());
        Assertions.assertEquals("48684998820", user.getCpf());
        Assertions.assertEquals("5656566", user.getRg());
        Assertions.assertEquals("12996598968", user.getCellphone());

    }



}
