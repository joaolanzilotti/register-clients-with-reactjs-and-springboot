package com.corporation.apiclient.services;

import java.util.Optional;
import java.util.logging.Logger;

import com.corporation.apiclient.controller.UserController;
import com.corporation.apiclient.dto.UserDTO;
import com.corporation.apiclient.entities.User;
import com.corporation.apiclient.exceptions.DataIntegratyViolationException;
import com.corporation.apiclient.exceptions.ObjectNotFoundException;
import com.corporation.apiclient.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserServices implements UserDetailsService {

    @Autowired
    private PagedResourcesAssembler<UserDTO> assembler;

    @Autowired
    private ModelMapper modelMapper;

    private Logger logger = Logger.getLogger(UserServices.class.getName());


    @Autowired
    UserRepository repository;

    public UserServices(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Finding one user by name " + email + "!");
        Optional<User> userOP = repository.findByEmail(email);
        User user = userOP.get();
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("Username " + email + " not found!");
        }
    }

    public PagedModel<EntityModel<UserDTO>> findUserByName(String name, Pageable pageable) {
        Page<User> usersPage = repository.findUserByNamePage(name,pageable);

        Page<UserDTO> userDTOPage = usersPage.map(p -> modelMapper.map(p, UserDTO.class));
        userDTOPage.map(
                p -> p.add(
                        linkTo(methodOn(UserController.class)
                                .findUserById(p.getId())).withSelfRel()));

        Link link = linkTo(
                methodOn(UserController.class)
                        .findClientByName(name,pageable.getPageNumber(),
                                pageable.getPageSize(),
                                "asc")).withSelfRel();

        return assembler.toModel(userDTOPage, link);   // Usando Assembler para Colocar Links das Pages, Sizes e Direction

    }

    public PagedModel<EntityModel<UserDTO>> findAll(Pageable pageable) {
        Page<User> usersPage = repository.findAll(pageable);

        Page<UserDTO> userDTOPage = usersPage.map(p -> modelMapper.map(p, UserDTO.class));
        userDTOPage.map(
                p -> p.add(
                        linkTo(methodOn(UserController.class)
                                .findUserById(p.getId())).withSelfRel()));

        Link link = linkTo(
                methodOn(UserController.class)
                        .findAll(pageable.getPageNumber(),
                                pageable.getPageSize())).withSelfRel();

        return assembler.toModel(userDTOPage, link);   // Usando Assembler para Colocar Links das Pages, Sizes e Direction

    }

    public User findUserById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User Not Found"));
    }

    @Transactional // Quando a Query é criada e precisa modificar os dados precisa adicionar @Transactional
    public User updateDisableClient(Long id) {

        logger.info("Disabling User");

        repository.updateDisableClient(id);
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User Not Found"));
    }

    public User addUser(UserDTO userDTO) {

        logger.info("Adding User");

        alreadyExistsByRg(userDTO);
        alreadyExistsByEmail(userDTO);
        alreadyExistsByCpf(userDTO);
        //DTO.add(linkTo(methodOn(ClientController.class).findClientById(DTO.getId())).withSelfRel());
        return repository.save(modelMapper.map(userDTO, User.class));

    }

    public User updateUser(UserDTO userDTO) {

        logger.info("Updating Client");

        alreadyExistsByEmail(userDTO);
        return repository.save(modelMapper.map(userDTO, User.class));
    }

    public void deleteUser(Long id) {

        logger.info("Deleting User");

        findUserById(id);
        repository.deleteById(id);
    }

    private void alreadyExistsByEmail(UserDTO userDTO) {
        Optional<User> user = repository.findByEmail(userDTO.getEmail());
        if (user.isPresent() && !user.get().getId().equals(userDTO.getId())) {
            throw new DataIntegratyViolationException("this E-mail already exists");
        }
    }

    private void alreadyExistsByCpf(UserDTO userDTO) {
        Optional<User> user = repository.findByCpf(userDTO.getCpf());
        if (user.isPresent() && !user.get().getId().equals(userDTO.getId())) {
            throw new DataIntegratyViolationException("this CPF already exists");
        }
    }

    private void alreadyExistsByRg(UserDTO userDTO) {
        Optional<User> user = repository.findByRg(userDTO.getRg());
        if (user.isPresent() && !user.get().getId().equals(userDTO.getId())) {
            throw new DataIntegratyViolationException("this RG already exists");
        }
    }

    public Boolean existByClientId(Long id) {
        return repository.existsById(id);
    }

}