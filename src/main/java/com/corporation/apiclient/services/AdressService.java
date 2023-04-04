package com.corporation.apiclient.services;

import com.corporation.apiclient.dto.AdressDTO;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.repositories.AdressRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

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

}
