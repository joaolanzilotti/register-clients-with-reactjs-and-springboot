package com.corporation.apiclient.api.controller;

import com.corporation.apiclient.domain.model.Client;
import com.corporation.apiclient.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/clients")
@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<List<Client>> listClients(){
        List<Client> listClient = clientService.listClient();
        return ResponseEntity.ok().body(listClient);
    }

}
