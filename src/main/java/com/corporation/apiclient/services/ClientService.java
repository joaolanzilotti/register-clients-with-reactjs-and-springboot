package com.corporation.apiclient.services;

import com.corporation.apiclient.domain.model.Client;
import com.corporation.apiclient.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class ClientService implements Serializable {

    @Autowired
    private ClientRepository repository;

    public List<Client> listClient(){
        return repository.findAll();
    }

}
