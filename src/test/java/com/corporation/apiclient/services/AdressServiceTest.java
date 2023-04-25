package com.corporation.apiclient.services;

import com.corporation.apiclient.dto.AdressDTO;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.repositories.AdressRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdressServiceTest {

    @InjectMocks
    private AdressService adressService;

    @Mock
    private AdressRepository adressRepository;

    @Mock
    private ModelMapper modelMapper;

    private Adress adress;
    private AdressDTO adressDTO;
    private Optional<Adress> optionalAdress;

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
        Assertions.assertEquals(Adress.class, response.getClass());
        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals("Rua Tenorio", response.getStreet());
        Assertions.assertEquals("Centro", response.getDistrict());
        Assertions.assertEquals("50", response.getNumber());
        Assertions.assertEquals("Ubatuba", response.getCity());
        Assertions.assertEquals("SP", response.getState());

    }


    private void startAdress() {
        adress = new Adress(1L, "Rua Tenorio", "Centro", "50", "Ubatuba", "SP", null);
        adressDTO = new AdressDTO(1L, "Rua Tenorio", "Centro", "50", "Ubatuba", "SP", null);
        optionalAdress = Optional.of(new Adress(1L, "Rua Tenorio", "Centro", "50", "Ubatuba", "SP", null));
    }

}