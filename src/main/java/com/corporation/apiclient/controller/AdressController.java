package com.corporation.apiclient.controller;

import com.corporation.apiclient.dto.AdressDTO;
import com.corporation.apiclient.dto.ClientDTO;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.entities.Client;
import com.corporation.apiclient.repositories.ClientRepository;
import com.corporation.apiclient.services.AdressService;
import com.corporation.apiclient.services.ClientService;
import com.corporation.apiclient.utils.MediaType;
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

    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public ResponseEntity<List<Adress>> findAllAdress(){
        List<Adress> listAllAdress = adressService.findAllAdress();
        return ResponseEntity.ok().body(listAllAdress);
    }

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public ResponseEntity<Adress> adressById(@PathVariable Long id){
        Adress adress = adressService.findAdressById(id);
        return ResponseEntity.ok().body(adress);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AdressDTO> updateAdress(@PathVariable Long id,@RequestBody AdressDTO adressDTO){
        adressDTO.setId(id);
        Adress adress = adressService.updateAdress(adressDTO);
        return ResponseEntity.ok().body(adressDTO);

    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<AdressDTO> addAdress(@PathVariable Long id, @Valid @RequestBody AdressDTO adressDTO){
        Client client = modelMapper.map(clientService.findClientById(id),Client.class);
        Adress adress = adressService.addAdress(adressDTO);
        client.setAdress(adress);
        clientRepository.save(client);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(adressDTO.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Adress> deleteAdress(@PathVariable Long id){
        adressService.deleteAdress(id);
        return ResponseEntity.noContent().build();
    }

}
