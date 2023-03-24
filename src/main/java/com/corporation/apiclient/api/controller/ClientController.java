package com.corporation.apiclient.api.controller;

import com.corporation.apiclient.domain.model.Client;
import com.corporation.apiclient.dto.ClientDTO;
import com.corporation.apiclient.exceptions.ObjectNotFoundException;
import com.corporation.apiclient.services.ClientService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/clients")
@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<ClientDTO>> listClients() {
        List<ClientDTO> listClientDTO = clientService.listClient().stream().map(x -> modelMapper.map(x, ClientDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listClientDTO);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ClientDTO> findClientById(@PathVariable Long id) {
            Client clientById = clientService.findClientById(id);
            return ResponseEntity.ok().body(modelMapper.map(clientService.findClientById(id),ClientDTO.class));

        }

    @PostMapping
    public ResponseEntity<ClientDTO> addClient(@Valid @RequestBody ClientDTO clientDTO) {
        Client cliente = clientService.addCliente(clientDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cliente.getId()).toUri();
        return ResponseEntity.created(uri).build();

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id,@Valid @RequestBody ClientDTO clientDTO){

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
