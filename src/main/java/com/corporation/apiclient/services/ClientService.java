package com.corporation.apiclient.services;

import com.corporation.apiclient.controller.AdressController;
import com.corporation.apiclient.controller.ClientController;
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
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ClientService implements Serializable {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ClientDTO> findAll(){
        Type listType = new TypeToken<List<ClientDTO>>() {}.getType();
        List<ClientDTO> listClientDTO = modelMapper.map(clientRepository.findAll(), listType);
        listClientDTO.stream().forEach(c -> c.add(linkTo(methodOn(ClientController.class).findClientById(c.getId())).withSelfRel()));

        return listClientDTO;

    }

    public ClientDTO findClientById(Long id){
        Client client = clientRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Client Not Found"));
        ClientDTO clientDTO = modelMapper.map(client, ClientDTO.class);
        clientDTO.add(linkTo(methodOn(ClientController.class).findClientById(id)).withSelfRel());
        clientDTO.add(linkTo(methodOn(AdressController.class).adressById(client.getAdress().getId())).withSelfRel());
        return clientDTO;
    }

    public ClientDTO addClient(ClientDTO clientDTO) {
        Client client = modelMapper.map(clientDTO, Client.class);
        alreadyExistsByRg(clientDTO);
        alreadyExistsByEmail(clientDTO);
        alreadyExistsByCpf(clientDTO);
        ClientDTO DTO = modelMapper.map(clientRepository.save(client), ClientDTO.class);
        DTO.add(linkTo(methodOn(ClientController.class).findClientById(DTO.getId())).withSelfRel());
        return DTO;

    }

    public ClientDTO updateClient(ClientDTO clientDTO){
        Client client = modelMapper.map(clientDTO, Client.class);
        ClientDTO DTO = modelMapper.map(clientRepository.save(client), ClientDTO.class);
        DTO.add(linkTo(methodOn(ClientController.class).findClientById(DTO.getId())).withSelfRel());
        return DTO;
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
