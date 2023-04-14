package com.corporation.apiclient.services;

import com.corporation.apiclient.dto.ClientDTO;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.entities.Client;
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

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

    @BeforeEach
    void setUpMocks() throws Exception {
        MockitoAnnotations.openMocks(this);
        startClientAndAdress();
    }

    @Test
    void findAll() {

    }

    @Test
    void findClientById() {

        Mockito.when(clientRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(client));
        var response  = clientService.findClientById(1L);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getLinks());
        Assertions.assertTrue(response.toString().contains(""));


    }

    @Test
    void addClient() {
    }

    @Test
    void updateClient() {
    }

    @Test
    void deleteClient() {
    }

    private void startClientAndAdress(){
        client = new Client(1L, "Joao", "teste@teste.com", "123", "56006548", "09113144568", new Date(), "12659874848", adress);
        clientDTO = new ClientDTO(1L, "Joao", "teste@teste.com", "123", "56006548", "09113144568", new Date(), "12659874848", adress);
        adress = new Adress(1L, "maranhao", "district", "50", "Ubatuba", "SP", null);
    }

}