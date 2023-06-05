package com.corporation.apiclient.services;

import com.corporation.apiclient.dto.UserDTO;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.entities.User;
import com.corporation.apiclient.exceptions.DataIntegratyViolationException;
import com.corporation.apiclient.exceptions.ObjectNotFoundException;
import com.corporation.apiclient.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServices userServices;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    private User user;
    private Adress adress;
    private UserDTO userDTO;
    private Optional<User> optionalUser;

    @BeforeEach
    void setUpMocks() throws Exception {
        MockitoAnnotations.openMocks(this);
        startClientAndAdress();

    }

    @Test
    void findClientById() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(optionalUser);
        User response = userServices.findUserById(1L);
        response.setBirthDay(new Date(123, 4, 25));

        //---Verificando se os valores não estão nulos---//
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getEmail());
        Assertions.assertNotNull(response.getCpf());
        Assertions.assertNotNull(response.getName());
        Assertions.assertNotNull(response.getBirthDay());
        Assertions.assertNotNull(response.getPassword());
        Assertions.assertNotNull(response.getRg());
        //---Verificando se os Valores são iguais---//
        Assertions.assertEquals(User.class, response.getClass());
        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals("Joao", response.getName());
        Assertions.assertEquals("teste@teste.com", response.getEmail());
        Assertions.assertEquals("123", response.getPassword());
        Assertions.assertEquals("56006548", response.getRg());
        Assertions.assertEquals("09113144568", response.getCpf());
        Assertions.assertEquals(new Date(123, 4, 25), response.getBirthDay());
        Assertions.assertEquals("12659874848", response.getCellphone());

    }

    @Test
    void findClientByIdReturnObjectNotFoundException() {

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenThrow(new ObjectNotFoundException("Client Not Found"));

        try {
            userServices.findUserById(1L);
        } catch (Exception ex) {
            //---Estou Comparando os Valores da classe ObjectNotFoundException---//
            Assertions.assertEquals(ObjectNotFoundException.class, ex.getClass());
            Assertions.assertEquals("Client Not Found", ex.getMessage());
        }

    }

    @Test
    void whenAddThenReturnSucess() {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        User response = userServices.addUser(userDTO);
        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getEmail());
        Assertions.assertNotNull(response.getCpf());
        Assertions.assertNotNull(response.getName());
        Assertions.assertNotNull(response.getBirthDay());
        Assertions.assertNotNull(response.getPassword());
        Assertions.assertNotNull(response.getRg());

        Assertions.assertNotNull(response);
        Assertions.assertEquals(User.class, response.getClass());
        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals("Joao", response.getName());
        Assertions.assertEquals("teste@teste.com", response.getEmail());
        Assertions.assertEquals("123", response.getPassword());
        Assertions.assertEquals("56006548", response.getRg());
        Assertions.assertEquals("09113144568", response.getCpf());
        Assertions.assertEquals(new Date(123, 4, 25), response.getBirthDay());
        Assertions.assertEquals("12659874848", response.getCellphone());

    }

    @Test
    void whenAddThenReturnDataIntegratyViolationException() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(optionalUser);

        try {
            optionalUser.get().setId(1L);
            userServices.addUser(userDTO);
        } catch (Exception ex) {
            Assertions.assertEquals(DataIntegratyViolationException.class, ex.getClass());
            Assertions.assertEquals("this E-mail already exists", ex.getMessage());
        }

    }

    @Test
    void whenUpdateThenReturnSucess() {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        User response = userServices.updateUser(userDTO);
        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getEmail());
        Assertions.assertNotNull(response.getCpf());
        Assertions.assertNotNull(response.getName());
        Assertions.assertNotNull(response.getBirthDay());
        Assertions.assertNotNull(response.getPassword());
        Assertions.assertNotNull(response.getRg());

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals("Joao", response.getName());
        Assertions.assertEquals("teste@teste.com", response.getEmail());
        Assertions.assertEquals("123", response.getPassword());
        Assertions.assertEquals("56006548", response.getRg());
        Assertions.assertEquals("09113144568", response.getCpf());
        Assertions.assertEquals(new Date(123, 4, 25), response.getBirthDay());
        Assertions.assertEquals("12659874848", response.getCellphone());

    }

    @Test
    void deleteClientWithSucess(){
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(optionalUser);

        Mockito.doNothing().when(userRepository).deleteById(Mockito.anyLong());
        userServices.deleteUser(1L);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(Mockito.anyLong());

    }

    @Test
    void deleteClientWithObjectNotFoundException(){
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenThrow(new ObjectNotFoundException("Client Not Found"));

        try{
            userServices.deleteUser(1L);
        }catch (Exception ex){
            Assertions.assertEquals(ObjectNotFoundException.class, ex.getClass());
            Assertions.assertEquals("Client Not Found", ex.getMessage());
        }
    }

    private void startClientAndAdress() {
        user = new User(1L, "teste@teste.com", "56006548", "09113144568", new Date(123, 4, 25), "12659874848", adress, true, "Joao", "123");
        userDTO =  new UserDTO(1L, "teste@teste.com", "56006548", "09113144568", new Date(123, 4, 25), "Masculino", "12659874848", adress, true, "Joao", "123", true, true, true, null);
        optionalUser = Optional.of(new User(1L, "teste@teste.com", "56006548", "09113144568", new Date(123, 4, 25), "12659874848", adress, true, "Joao", "123"));
        adress = new Adress(1L,"11695108", "maranhao", "district", "50", "Ubatuba", "SP", null);
    }
}