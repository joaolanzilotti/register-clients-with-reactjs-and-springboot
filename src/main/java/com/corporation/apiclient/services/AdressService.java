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

    public AdressDTO addAdress(AdressDTO adressDTO, Long id){
        Client client = modelMapper.map(clientService.findClientById(id), Client.class);
        Adress adress = modelMapper.map(adressDTO, Adress.class);
        AdressDTO DTO = modelMapper.map(adressRepository.save(adress), AdressDTO.class);
        client.setAdress(modelMapper.map(DTO, Adress.class));
        clientRepository.save(client);
        DTO.add(linkTo(methodOn(AdressController.class).adressById(DTO.getId())).withSelfRel());
        DTO.add(linkTo(methodOn(ClientController.class).findClientById(id)).withSelfRel());
        return DTO;
    }

    public List<AdressDTO> findAllAdress(){
        Type listType = new TypeToken<List<AdressDTO>>() {}.getType();
        List<AdressDTO> listAdressDTO = modelMapper.map(adressRepository.findAll(), listType);
        listAdressDTO.stream().forEach(a -> a.add(linkTo(methodOn(AdressController.class).adressById(a.getId())).withSelfRel()));
        return listAdressDTO;
    }

    public AdressDTO findAdressById(Long id){

        Adress adress = adressRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Adress Not Found"));
        AdressDTO adressDTO = modelMapper.map(adress, AdressDTO.class);
        adressDTO.add(linkTo(methodOn(AdressController.class).adressById(adress.getId())).withSelfRel());
        return adressDTO;
    }

    public AdressDTO updateAdress(AdressDTO adressDTO){
        Adress adress = modelMapper.map(adressDTO, Adress.class);
        AdressDTO DTO = modelMapper.map(adressRepository.save(adress),AdressDTO.class);
        DTO.add(linkTo(methodOn(AdressController.class).adressById(DTO.getId())).withSelfRel());
        return DTO;
    }

    public void deleteAdress(Long id){
        findAdressById(id);
        adressRepository.deleteById(id);
    }

}
