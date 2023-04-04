package com.corporation.apiclient.services;

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
import java.util.Optional;

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

    public Adress findAdressById(Long id){
        Optional<Adress> adress = adressRepository.findById(id);
        return adress.orElseThrow(() -> new ObjectNotFoundException("Adress Not Found"));
    }

    public Adress updateAdress(AdressDTO adressDTO){
        return adressRepository.save(modelMapper.map(adressDTO, Adress.class));
    }

}
