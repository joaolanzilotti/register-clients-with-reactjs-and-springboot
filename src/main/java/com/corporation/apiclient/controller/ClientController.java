package com.corporation.apiclient.controller;

import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.entities.Client;
import com.corporation.apiclient.dto.ClientDTO;
import com.corporation.apiclient.services.ClientService;
import com.corporation.apiclient.utils.MediaType;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/clients")
@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public ResponseEntity<List<ClientDTO>> listClients() {
        List<ClientDTO> listClientDTO = clientService.listClient().stream().map(x -> modelMapper.map(x, ClientDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listClientDTO);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public ResponseEntity<ClientDTO> findClientById(@PathVariable Long id) {
            Client clientById = clientService.findClientById(id);
            ClientDTO clientDTO = modelMapper.map(clientById, ClientDTO.class);
        clientDTO.add(linkTo(methodOn(ClientController.class).findClientById(id)).withSelfRel());
        clientDTO.add(linkTo(methodOn(AdressController.class).adressById(clientById.getAdress().getId())).withSelfRel());
            return ResponseEntity.ok().body(clientDTO);

        }

    @PostMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
                 consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public ResponseEntity<ClientDTO> addClient(@Valid @RequestBody ClientDTO clientDTO) {


        clientService.addClient(clientDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(clientDTO.getId()).toUri();
        return ResponseEntity.created(uri).build();

    }

    @PutMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id,@Valid @RequestBody ClientDTO clientDTO){

        Client clientById = clientService.findClientById(id);
        clientDTO.setAdress(clientById.getAdress());
        clientDTO.setId(id);


        Client client = clientService.updateClient(clientDTO);
        return ResponseEntity.ok().body(modelMapper.map(client, ClientDTO.class));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id){
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

}
