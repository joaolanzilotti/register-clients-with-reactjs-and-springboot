package com.corporation.apiclient.controller;

import com.corporation.apiclient.dto.AdressDTO;
import com.corporation.apiclient.dto.ClientDTO;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.entities.Client;
import com.corporation.apiclient.repositories.ClientRepository;
import com.corporation.apiclient.services.AdressService;
import com.corporation.apiclient.services.ClientService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<Adress>> findAllAdress(){
        List<Adress> listAllAdress = adressService.findAllAdress();
        return ResponseEntity.ok().body(listAllAdress);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AdressDTO> updateAdress(@PathVariable Long id,@RequestBody AdressDTO adressDTO){
        adressDTO.setId(id);
        Adress adress = adressService.updateAdress(adressDTO);
        return ResponseEntity.ok().body(adressDTO);

    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<AdressDTO> addAdress(@PathVariable Long id, @Valid @RequestBody AdressDTO adressDTO){
        Client client = clientService.findClientById(id);
        Adress adress = adressService.addAdress(adressDTO);
        client.setAdress(adress);
        clientRepository.save(client);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(adressDTO.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

}
