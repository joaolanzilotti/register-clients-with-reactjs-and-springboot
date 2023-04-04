package com.corporation.apiclient.services;

import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.entities.Client;
import com.corporation.apiclient.dto.ClientDTO;
import com.corporation.apiclient.exceptions.DataIntegratyViolationException;
import com.corporation.apiclient.exceptions.ObjectNotFoundException;
import com.corporation.apiclient.repositories.AdressRepository;
import com.corporation.apiclient.repositories.ClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService implements Serializable {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AdressRepository adressRepository;

    public List<Client> listClient(){

        return clientRepository.findAll();

    }

    public Client findClientById(Long id){
        Optional<Client> client = clientRepository.findById(id);
        return client.orElseThrow(() -> new ObjectNotFoundException("Client Not Found"));
    }

    public void addClient(ClientDTO clientDTO) {
        Client client = modelMapper.map(clientDTO, Client.class);
        alreadyExistsByRg(modelMapper.map(client, ClientDTO.class));
        alreadyExistsByEmail(modelMapper.map(client, ClientDTO.class));
        alreadyExistsByCpf(modelMapper.map(client, ClientDTO.class));
        clientRepository.save(client);

    }

    public Client updateClient(ClientDTO clientDTO){
        alreadyExistsByRg(clientDTO);
        alreadyExistsByCpf(clientDTO);
        alreadyExistsByEmail(clientDTO);
        return clientRepository.save(modelMapper.map(clientDTO, Client.class));
    }

    public void deleteClient(Long id){
        findClientById(id);
        clientRepository.deleteById(id);
    }

    private void alreadyExistsByEmail(ClientDTO clientDTO){
        Optional<Client> client = clientRepository.findByEmail(clientDTO.getEmail());
        if(client.isPresent() && !client.get().getId().equals(clientDTO.getId())){
            throw new DataIntegratyViolationException("this E-mail already exists");
        }
    }

    private void alreadyExistsByCpf(ClientDTO clientDTO){
        Optional<Client> client = clientRepository.findByCpf(clientDTO.getCpf());
        if(client.isPresent() && !client.get().getId().equals(clientDTO.getId())){
            throw new DataIntegratyViolationException("this CPF already exists");
        }
    }

    private void alreadyExistsByRg(ClientDTO clientDTO){
        Optional<Client> client = clientRepository.findByRg(clientDTO.getRg());
        if(client.isPresent() && !client.get().getId().equals(clientDTO.getId())){
            throw new DataIntegratyViolationException("this RG already exists");
        }
    }

    public Boolean existByClientId(Long id){
        return clientRepository.existsById(id);
    }

}
