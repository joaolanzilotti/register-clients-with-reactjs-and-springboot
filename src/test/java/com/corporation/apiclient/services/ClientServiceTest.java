package com.corporation.apiclient.services;

import com.corporation.apiclient.dto.ClientDTO;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.entities.Client;
import com.corporation.apiclient.exceptions.DataIntegratyViolationException;
import com.corporation.apiclient.exceptions.ObjectNotFoundException;
import com.corporation.apiclient.repositories.ClientRepository;
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

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ModelMapper modelMapper;

    private Client client;
    private Adress adress;
    private ClientDTO clientDTO;
    private Optional<Client> optionalClient;

    @BeforeEach
    void setUpMocks() throws Exception {
        MockitoAnnotations.openMocks(this);
        startClientAndAdress();

    }


    @Test
    void findClientById() {
        Mockito.when(clientRepository.findById(Mockito.anyLong())).thenReturn(optionalClient);
        Client response = clientService.findClientById(1L);
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
        Assertions.assertEquals(Client.class, response.getClass());
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

        Mockito.when(clientRepository.findById(Mockito.anyLong())).thenThrow(new ObjectNotFoundException("Client Not Found"));

        try {
            clientService.findClientById(1L);
        } catch (Exception ex) {
            //---Estou Comparando os Valores da classe ObjectNotFoundException---//
            Assertions.assertEquals(ObjectNotFoundException.class, ex.getClass());
            Assertions.assertEquals("Client Not Found", ex.getMessage());
        }

    }

    @Test
    void whenAddThenReturnSucess() {
        Mockito.when(clientRepository.save(Mockito.any())).thenReturn(client);

        Client response = clientService.addClient(clientDTO);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(Client.class, response.getClass());
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
        Mockito.when(clientRepository.findByEmail(Mockito.anyString())).thenReturn(optionalClient);

        try {
            optionalClient.get().setId(1L);
            clientService.addClient(clientDTO);
        } catch (Exception ex) {
            Assertions.assertEquals(DataIntegratyViolationException.class, ex.getClass());
            Assertions.assertEquals("this E-mail already exists", ex.getMessage());
        }

    }

    @Test
    void whenUpdateThenReturnSucess() {
        Mockito.when(clientRepository.save(Mockito.any())).thenReturn(client);
        Client response = clientService.updateClient(clientDTO);
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
        Mockito.when(clientRepository.findById(Mockito.anyLong())).thenReturn(optionalClient);

        Mockito.doNothing().when(clientRepository).deleteById(Mockito.anyLong());
        clientService.deleteClient(1L);
        Mockito.verify(clientRepository, Mockito.times(1)).deleteById(Mockito.anyLong());

    }

    @Test
    void deleteClientWithObjectNotFoundException(){
        Mockito.when(clientRepository.findById(Mockito.anyLong())).thenThrow(new ObjectNotFoundException("Client Not Found"));

        try{
            clientService.deleteClient(1L);
        }catch (Exception ex){
            Assertions.assertEquals(ObjectNotFoundException.class, ex.getClass());
            Assertions.assertEquals("Client Not Found", ex.getMessage());
        }
    }

    private void startClientAndAdress() {
        client = new Client(1L, "Joao", "teste@teste.com", "123", "56006548", "09113144568", new Date(123, 4, 25), "12659874848", adress);
        clientDTO = new ClientDTO(1L, "Joao", "teste@teste.com", "123", "56006548", "09113144568", new Date(123, 4, 25), "12659874848", adress);
        optionalClient = Optional.of(new Client(1L, "Joao", "teste@teste.com", "123", "56006548", "09113144568", new Date(), "12659874848", adress));
        adress = new Adress(1L, "maranhao", "district", "50", "Ubatuba", "SP", null);
    }
}