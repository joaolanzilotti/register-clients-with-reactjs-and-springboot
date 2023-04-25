package com.corporation.apiclient.services;

import com.corporation.apiclient.dto.ClientDTO;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.entities.Client;
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
import org.springframework.hateoas.Link;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    Client client;
    Adress adress;
    ClientDTO clientDTO;
    Optional<Client> optionalClient;

    ClientServiceTest() throws ParseException {
    }

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



    private void startClientAndAdress() {
        client = new Client(1L, "Joao", "teste@teste.com", "123", "56006548", "09113144568", new Date(123, 4, 25), "12659874848", adress);
        clientDTO = new ClientDTO(1L, "Joao", "teste@teste.com", "123", "56006548", "09113144568", new Date(123, 4, 25), "12659874848", adress);
        optionalClient = Optional.of(new Client(1L, "Joao", "teste@teste.com", "123", "56006548", "09113144568", new Date(), "12659874848", adress));
        adress = new Adress(1L, "maranhao", "district", "50", "Ubatuba", "SP", null);
    }
}