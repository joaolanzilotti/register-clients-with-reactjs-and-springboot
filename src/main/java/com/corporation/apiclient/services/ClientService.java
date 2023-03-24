package com.corporation.apiclient.services;

import com.corporation.apiclient.domain.model.Client;
import com.corporation.apiclient.dto.ClientDTO;
import com.corporation.apiclient.exceptions.DataIntegratyViolationException;
import com.corporation.apiclient.exceptions.ObjectNotFoundException;
import com.corporation.apiclient.repositories.ClientRepository;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;

    public List<Client> listClient(){

        return clientRepository.findAll();

    }

    public Client findClientById(Long id){
        Optional<Client> client = clientRepository.findById(id);
        return client.orElseThrow(() -> new ObjectNotFoundException("Client Not Found"));
    }

    public Client addCliente(ClientDTO clientDTO){
        findByEmail(clientDTO);
        return clientRepository.save(modelMapper.map(clientDTO, Client.class));
    }

    public Client updateClient(ClientDTO clientDTO){
            findByEmail(clientDTO);
            return clientRepository.save(modelMapper.map(clientDTO, Client.class));
    }

    public void deleteClient(Long id){
        findClientById(id);
        clientRepository.deleteById(id);
    }

    private void findByEmail(ClientDTO clientDTO){
        Optional<Client> client = clientRepository.findByEmail(clientDTO.getEmail());
        if(client.isPresent() && !client.get().getId().equals(clientDTO.getId())){
            throw new DataIntegratyViolationException("this E-mail already exists");
        }
    }

    public Boolean existByClientId(Long id){
        return clientRepository.existsById(id);
    }

}
