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
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdressServiceTest {

    @Mock
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AdressRepository adressRepository;

    @InjectMocks
    private AdressService adressService;

    @Mock
    private ModelMapper modelMapper;

    private Adress adress;
    private AdressDTO adressDTO;
    private Optional<Adress> optionalAdress;

    private Client client;
    private ClientDTO clientDTO;
    private Optional<Client> optionalClient;

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
    void addAdressTest() {

        Mockito.when(clientService.findClientById(1L)).thenReturn(client);
        Mockito.when(adressRepository.save(Mockito.any())).thenReturn(adress);
        Adress response = adressService.addAdress(adressDTO, 1L);

    }

    @Test
    void whenSucessUpdateAdressThenReturnSucess() {
        Mockito.when(adressRepository.save(Mockito.any())).thenReturn(adress);
        Adress response = adressService.updateAdress(adressDTO);

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
    void deleteAdressWithSucess() {
        Mockito.when(adressRepository.findById(Mockito.anyLong())).thenReturn(optionalAdress);
        Mockito.doNothing().when(adressRepository).deleteById(Mockito.anyLong());
        adressService.deleteAdress(1L);
        Mockito.verify(adressRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    private void startAdress() {
        adress = new Adress(1L, "Rua Tenorio", "Centro", "50", "Ubatuba", "SP", null);
        adressDTO = new AdressDTO(1L, "Rua Tenorio", "Centro", "50", "Ubatuba", "SP", null);
        optionalAdress = Optional.of(new Adress(1L, "Rua Tenorio", "Centro", "50", "Ubatuba", "SP", null));
        client = new Client(1L, "Joao", "teste@teste.com", "123", "56006548", "09113144568", new Date(123, 4, 25), "12659874848", null, true);
        clientDTO = new ClientDTO(1L, "Joao", "teste@teste.com", "123", "56006548", "09113144568", new Date(123, 4, 25), "12659874848", null, true);
        optionalClient = Optional.of(new Client(1L, "Joao", "teste@teste.com", "123", "56006548", "09113144568", new Date(), "12659874848", null, true));
    }
}
