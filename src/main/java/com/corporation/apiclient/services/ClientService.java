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
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ClientService implements Serializable {

    private java.util.logging.Logger logger = Logger.getLogger(ClientService.class.getName());

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<Client> findAll(Pageable pageable) {
        logger.info("Finding All Clients");
        Page<Client> listClient = clientRepository.findAll(pageable);

        return listClient;

    }

    public Client findClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Client Not Found"));
    }

    @Transactional // Quando a Query é criada e precisa modificar os dados precisa adicionar @Transactional
    public Client updateDisableClient(Long id) {

        logger.info("Disabling Client");

        clientRepository.updateDisableClient(id);
        return clientRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Client Not Found"));
    }

    public Client addClient(ClientDTO clientDTO) {

        logger.info("Adding Client");

        alreadyExistsByRg(clientDTO);
        alreadyExistsByEmail(clientDTO);
        alreadyExistsByCpf(clientDTO);
        //DTO.add(linkTo(methodOn(ClientController.class).findClientById(DTO.getId())).withSelfRel());
        return clientRepository.save(modelMapper.map(clientDTO, Client.class));

    }

    public Client updateClient(ClientDTO clientDTO) {

        logger.info("Updating Client");

        alreadyExistsByEmail(clientDTO);
        return clientRepository.save(modelMapper.map(clientDTO, Client.class));
    }

    public void deleteClient(Long id) {

        logger.info("Deleting Client");

        findClientById(id);
        clientRepository.deleteById(id);
    }

    private void alreadyExistsByEmail(ClientDTO clientDTO) {
        Optional<Client> client = clientRepository.findByEmail(clientDTO.getEmail());
        if (client.isPresent() && !client.get().getId().equals(clientDTO.getId())) {
            throw new DataIntegratyViolationException("this E-mail already exists");
        }
    }

    private void alreadyExistsByCpf(ClientDTO clientDTO) {
        Optional<Client> client = clientRepository.findByCpf(clientDTO.getCpf());
        if (client.isPresent() && !client.get().getId().equals(clientDTO.getId())) {
            throw new DataIntegratyViolationException("this CPF already exists");
        }
    }

    private void alreadyExistsByRg(ClientDTO clientDTO) {
        Optional<Client> client = clientRepository.findByRg(clientDTO.getRg());
        if (client.isPresent() && !client.get().getId().equals(clientDTO.getId())) {
            throw new DataIntegratyViolationException("this RG already exists");
        }
    }

    public Boolean existByClientId(Long id) {
        return clientRepository.existsById(id);
    }

}
