package com.corporation.apiclient.services;

import com.corporation.apiclient.controller.AdressController;
import com.corporation.apiclient.dto.AdressDTO;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.entities.User;
import com.corporation.apiclient.exceptions.ObjectNotFoundException;
import com.corporation.apiclient.repositories.AdressRepository;
import com.corporation.apiclient.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.io.Serializable;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class AdressService implements Serializable {

    @Autowired
    private AdressRepository adressRepository;

    @Autowired
    private PagedResourcesAssembler<AdressDTO> assembler;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserServices userServices;

    @Autowired
    private UserRepository userRepository;

    public Adress addAdress(AdressDTO adressDTO, Long id){
        User user = userServices.findUserById(id);
        Adress adress = modelMapper.map(adressDTO, Adress.class);
        adressRepository.save(adress);
        user.setAdress(adress);
        userRepository.save(user);
        return adress;
    }

    public PagedModel<EntityModel<AdressDTO>> findAllAdress(Pageable pageable){
        Page<Adress> adressPage = adressRepository.findAll(pageable);

        Page<AdressDTO> adressDTOPage = adressPage.map(p -> modelMapper.map(p, AdressDTO.class));
        adressDTOPage.map(
                p -> p.add(
                        linkTo(methodOn(AdressController.class)
                                .findAdressById(p.getId())).withSelfRel()));

        Link link = linkTo(
                methodOn(AdressController.class)
                        .findAllAdress(pageable.getPageNumber(),
                                pageable.getPageSize(),
                                "asc")).withSelfRel();

        return assembler.toModel(adressDTOPage, link);
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
