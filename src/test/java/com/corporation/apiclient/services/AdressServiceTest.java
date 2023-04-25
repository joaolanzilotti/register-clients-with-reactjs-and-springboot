package com.corporation.apiclient.services;

import com.corporation.apiclient.dto.AdressDTO;
import com.corporation.apiclient.dto.ClientDTO;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.entities.Client;
import com.corporation.apiclient.exceptions.ObjectNotFoundException;
import com.corporation.apiclient.repositories.AdressRepository;
import com.corporation.apiclient.repositories.ClientRepository;
import jakarta.persistence.ManyToOne;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdressServiceTest {

    @InjectMocks
    private AdressService adressService;

    @Mock
    private ClientService clientService;
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AdressRepository adressRepository;

    @Mock
    private ModelMapper modelMapper;

    private Adress adress;
    private AdressDTO adressDTO;
    private Optional<Adress> optionalAdress;

    private Client client;
    private ClientDTO clientDTO;
    private Optional<Client> optionalClient;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startAdress();
    }

    @Test
    void findAdressById() {
        Mockito.when(adressRepository.findById(Mockito.anyLong())).thenReturn(optionalAdress);

        Adress response = adressService.findAdressById(1L);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getStreet());
        Assertions.assertNotNull(response.getDistrict());
        Assertions.assertNotNull(response.getNumber());
        Assertions.assertNotNull(response.getCity());
        Assertions.assertNotNull(response.getState());

        Assertions.assertEquals(Adress.class, response.getClass());
        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals("Rua Tenorio", response.getStreet());
        Assertions.assertEquals("Centro", response.getDistrict());
        Assertions.assertEquals("50", response.getNumber());
        Assertions.assertEquals("Ubatuba", response.getCity());
        Assertions.assertEquals("SP", response.getState());

    }

    @Test
    void whenFindAllThenReturnListOfAdress() {
        Mockito.when(adressRepository.findAll()).thenReturn(List.of(adress));
        List<Adress> response = adressService.findAllAdress();

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.get(0).getId());
        Assertions.assertNotNull(response.get(0).getStreet());
        Assertions.assertNotNull(response.get(0).getDistrict());
        Assertions.assertNotNull(response.get(0).getNumber());
        Assertions.assertNotNull(response.get(0).getCity());
        Assertions.assertNotNull(response.get(0).getState());

        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals(Adress.class, response.get(0).getClass());
        Assertions.assertEquals(1L, response.get(0).getId());
        Assertions.assertEquals("Rua Tenorio", response.get(0).getStreet());
        Assertions.assertEquals("Centro", response.get(0).getDistrict());
        Assertions.assertEquals("50", response.get(0).getNumber());
        Assertions.assertEquals("Ubatuba", response.get(0).getCity());
        Assertions.assertEquals("SP", response.get(0).getState());

    }

    @Test
    void findAdressByIdReturnObjectNotFoundException() {

        Mockito.when(adressRepository.findById(Mockito.anyLong())).thenThrow(new ObjectNotFoundException("Adress Not Found"));

        try {
            adressService.findAdressById(1L);
        } catch (Exception ex) {
            //---Estou Comparando os Valores da classe ObjectNotFoundException---//
            Assertions.assertEquals(ObjectNotFoundException.class, ex.getClass());
            Assertions.assertEquals("Adress Not Found", ex.getMessage());
        }

    }

//    @Test
//    void whenAddThenReturnSucess(){
//        Mockito.when(adressRepository.save(Mockito.any())).thenReturn(adress);
//        Mockito.when(clientRepository.save(Mockito.any())).thenReturn(client);
//        Client responseCliente = clientService.findClientById(1L);
//        responseCliente.setId(1L);
//        Adress response = adressService.addAdress(adressDTO, 1L);
//        Assertions.assertNotNull(response);
//    }


    private void startAdress() {
        adress = new Adress(1L, "Rua Tenorio", "Centro", "50", "Ubatuba", "SP", null);
        adressDTO = new AdressDTO(1L, "Rua Tenorio", "Centro", "50", "Ubatuba", "SP");
        optionalAdress = Optional.of(new Adress(1L, "Rua Tenorio", "Centro", "50", "Ubatuba", "SP", null));
        client = new Client(1L, "Joao", "teste@teste.com", "123", "56006548", "09113144568", new Date(123, 4, 25), "12659874848", null);
        clientDTO = new ClientDTO(1L, "Joao", "teste@teste.com", "123", "56006548", "09113144568", new Date(123, 4, 25), "12659874848", null);
        optionalClient = Optional.of(new Client(1L, "Joao", "teste@teste.com", "123", "56006548", "09113144568", new Date(), "12659874848", null));
    }

}