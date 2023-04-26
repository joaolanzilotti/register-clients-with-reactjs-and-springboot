package com.corporation.apiclient.services;

import com.corporation.apiclient.controller.AdressController;
import com.corporation.apiclient.controller.ClientController;
import com.corporation.apiclient.dto.AdressDTO;
import com.corporation.apiclient.dto.ClientDTO;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.entities.Client;
import com.corporation.apiclient.exceptions.ObjectNotFoundException;
import com.corporation.apiclient.repositories.AdressRepository;
import com.corporation.apiclient.repositories.ClientRepository;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class AdressService implements Serializable {

    @Autowired
    private AdressRepository adressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    public Adress addAdress(AdressDTO adressDTO, Long id){
        Client client = clientService.findClientById(id);
        Adress adress = modelMapper.map(adressDTO, Adress.class);
        adressRepository.save(adress);
        client.setAdress(adress);
        clientRepository.save(client);
        return adress;
    }

    public List<Adress> findAllAdress(){
        List<Adress> listAdress = adressRepository.findAll();
        return listAdress;
    }

    public Adress findAdressById(Long id){
        return adressRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Adress Not Found"));
    }

    public Adress updateAdress(AdressDTO adressDTO){
        Adress adress = modelMapper.map(adressDTO, Adress.class);
        return adressRepository.save(adress);
    }

    public void deleteAdress(Long id){
        findAdressById(id);
        adressRepository.deleteById(id);
    }

}
