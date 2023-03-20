package com.corporation.apiclient.api.controller;

import com.corporation.apiclient.domain.model.Client;
import com.corporation.apiclient.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequestMapping("/clients")
@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<List<Client>> listClients() {
        List<Client> listClient = clientService.listClient();
        return ResponseEntity.ok().body(listClient);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<Client>> findClientById(@PathVariable Long id) {

        Optional<Client> cliente = clientService.findClientById(id);

        if (cliente.isPresent()) {

            return ResponseEntity.ok().body(cliente);

        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Client> addClient(@RequestBody Client client) {
        Client cliente = clientService.addCliente(client);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cliente.getId()).toUri();
        return ResponseEntity.created(uri).build();

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client client){

        if(!clientService.existByClientId(id)){
            return ResponseEntity.notFound().build();
        }
        client.setId(id);
        client = clientService.addCliente(client);
        return ResponseEntity.ok().body(client);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id){
        if(!clientService.existByClientId(id)){
            return ResponseEntity.notFound().build();
        }
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

}
