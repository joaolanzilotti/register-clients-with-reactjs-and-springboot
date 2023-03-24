package com.corporation.apiclient.services;

import com.corporation.apiclient.domain.model.Client;
import com.corporation.apiclient.exceptions.DataIntegratyViolationException;
import com.corporation.apiclient.exceptions.ObjectNotFoundException;
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
    private ClientRepository clientRepository;

    public List<Client> listClient(){
        return clientRepository.findAll();
    }

    public Client findClientById(Long id){
        Optional<Client> client = clientRepository.findById(id);
        return client.orElseThrow(() -> new ObjectNotFoundException("Client Not Found"));
    }

    public Client addCliente(Client client){
        return clientRepository.save(client);
    }

    public Client updateClient(Client client){
            return clientRepository.save(client);
    }

    public void deleteClient(Long id){
        if(!existByClientId(id)){
            throw new ObjectNotFoundException("Cliente Não Encontrado!");
        }
        clientRepository.deleteById(id);
    }

    private void findByEmail(Client client){
        Optional<Client> clientOptional = clientRepository.findByEmail(client.getEmail()) ;
        if(clientOptional.isPresent()){
            throw new DataIntegratyViolationException("E-mail Já Cadastrado no Sistema");
        }
    }

    public Boolean existByClientId(Long id){
        return clientRepository.existsById(id);
    }

}
