package com.corporation.apiclient.services;

import com.corporation.apiclient.domain.model.Client;
import com.corporation.apiclient.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService implements Serializable {

    @Autowired
    private ClientRepository repository;

    public List<Client> listClient(){
        return repository.findAll();
    }

    public Optional<Client> findClientById(Long id){
        Optional<Client> cliente = repository.findById(id);
        return cliente;
    }

    public Client addCliente(Client client){
    return repository.save(client);
    }

    public Client updateclient(Client client){
        return repository.save(client);
    }

    public void deleteClient(Long id){
        repository.deleteById(id);
    }

    public Boolean existByClientId(Long id){
        return repository.existsById(id);
    }

}
