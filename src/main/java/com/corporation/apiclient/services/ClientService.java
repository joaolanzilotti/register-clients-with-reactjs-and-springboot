package com.corporation.apiclient.services;

import com.corporation.apiclient.controller.AdressController;
import com.corporation.apiclient.controller.ClientController;
import com.corporation.apiclient.entities.Adress;
import java.util.logging.Logger;
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
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
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

    @Autowired
    private PagedResourcesAssembler<ClientDTO> assembler;

    private Logger logger = Logger.getLogger(ClientService.class.getName());

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PagedModel<EntityModel<ClientDTO>> findClientByName(String name,Pageable pageable) {
        Page<Client> clientsPage = clientRepository.findClientsByName(name,pageable);

        Page<ClientDTO> clientDTOPage = clientsPage.map(p -> modelMapper.map(p, ClientDTO.class));
        clientDTOPage.map(
                p -> p.add(
                        linkTo(methodOn(ClientController.class)
                                .findClientById(p.getId())).withSelfRel()));

        Link link = linkTo(
                methodOn(ClientController.class)
                        .findAll(pageable.getPageNumber(),
                                pageable.getPageSize(),
                                "asc")).withSelfRel();

        return assembler.toModel(clientDTOPage, link);   // Usando Assembler para Colocar Links das Pages, Sizes e Direction

    }

    public PagedModel<EntityModel<ClientDTO>> findAll(Pageable pageable) {
        Page<Client> clientsPage = clientRepository.findAll(pageable);

        Page<ClientDTO> clientDTOPage = clientsPage.map(p -> modelMapper.map(p, ClientDTO.class));
        clientDTOPage.map(
                p -> p.add(
                        linkTo(methodOn(ClientController.class)
                                .findClientById(p.getId())).withSelfRel()));

        Link link = linkTo(
                methodOn(ClientController.class)
                        .findAll(pageable.getPageNumber(),
                                pageable.getPageSize(),
                                "asc")).withSelfRel();

        return assembler.toModel(clientDTOPage, link);   // Usando Assembler para Colocar Links das Pages, Sizes e Direction

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
