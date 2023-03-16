package com.corporation.apiclient.api.controller;

import com.corporation.apiclient.domain.model.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/clients")
@RestController
public class ClientController {

    @GetMapping
    public ResponseEntity<List<Client>> listClients(){
        List<Client> listClient = new ArrayList<>();
        Client cliente1 = new Client(1L, "Joao Pedro", "teste@gmail.com", "123456");
        Client cliente2 = new Client(2L, "Pedro", "lol@gmail.com", "987468");
        listClient.add(cliente1);
        listClient.add(cliente2);
        return ResponseEntity.ok().body(listClient);
    }

}
