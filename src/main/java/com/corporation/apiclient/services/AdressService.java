package com.corporation.apiclient.services;

import com.corporation.apiclient.controller.AdressController;
import com.corporation.apiclient.dto.AdressDTO;
import com.corporation.apiclient.dto.ClientDTO;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.entities.Client;
import com.corporation.apiclient.exceptions.ObjectNotFoundException;
import com.corporation.apiclient.repositories.AdressRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
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

    public Adress addAdress(AdressDTO adressDTO){
        Adress adress = modelMapper.map(adressDTO, Adress.class);
        return adressRepository.save(adress);
    }

    public List<Adress> findAllAdress(){
        List<Adress> listAdress = adressRepository.findAll();
        return listAdress;
    }

    public AdressDTO findAdressById(Long id){

        Adress adress = adressRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Adress Not Found"));
        AdressDTO adressDTO = modelMapper.map(adress, AdressDTO.class);
        adressDTO.add(linkTo(methodOn(AdressController.class).adressById(adress.getId())).withSelfRel());
        return adressDTO;
    }

    public Adress updateAdress(AdressDTO adressDTO){
        return adressRepository.save(modelMapper.map(adressDTO, Adress.class));
    }

    public void deleteAdress(Long id){
        findAdressById(id);
        adressRepository.deleteById(id);
    }

}
