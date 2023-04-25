package com.corporation.apiclient.controller;

import com.corporation.apiclient.dto.AdressDTO;
import com.corporation.apiclient.dto.ClientDTO;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.entities.Client;
import com.corporation.apiclient.repositories.ClientRepository;
import com.corporation.apiclient.services.AdressService;
import com.corporation.apiclient.services.ClientService;
import com.corporation.apiclient.utils.MediaType;
import com.google.gson.reflect.TypeToken;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping(value = "/adress")
@RestController
public class AdressController {

    @Autowired
    private AdressService adressService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public ResponseEntity<List<AdressDTO>> findAllAdress() {
        Type listType = new TypeToken<List<AdressDTO>>() {}.getType();
        List<AdressDTO> listAdressDTO = modelMapper.map(adressService.findAllAdress(), listType);
        listAdressDTO.stream().forEach(a -> a.add(linkTo(methodOn(AdressController.class).adressById(a.getId())).withSelfRel()));
        return ResponseEntity.ok().body(listAdressDTO);
    }

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public ResponseEntity<AdressDTO> adressById(@PathVariable Long id) {
        AdressDTO adressDTO = modelMapper.map(adressService.findAdressById(id), AdressDTO.class);
        adressDTO.add(linkTo(methodOn(AdressController.class).adressById(adressDTO.getId())).withSelfRel());
        return ResponseEntity.ok().body(adressDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AdressDTO> updateAdress(@PathVariable Long id, @RequestBody AdressDTO adressDTO) {
        adressDTO.setId(id);
        AdressDTO DTO = adressService.updateAdress(adressDTO);
        return ResponseEntity.ok().body(DTO);

    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<AdressDTO> addAdress(@PathVariable Long id, @Valid @RequestBody AdressDTO adressDTO) {
        AdressDTO DTO = modelMapper.map(adressService.addAdress(adressDTO, id), AdressDTO.class);
        DTO.add(linkTo(methodOn(AdressController.class).adressById(DTO.getId())).withSelfRel());
        DTO.add(linkTo(methodOn(ClientController.class).findClientById(id)).withSelfRel());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(DTO.getId()).toUri();
        return ResponseEntity.created(uri).body(DTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Adress> deleteAdress(@PathVariable Long id) {
        adressService.deleteAdress(id);
        return ResponseEntity.noContent().build();
    }

}
