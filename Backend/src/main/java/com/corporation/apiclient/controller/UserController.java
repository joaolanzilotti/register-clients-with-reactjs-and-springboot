package com.corporation.apiclient.controller;

import com.corporation.apiclient.dto.UserDTO;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.entities.User;
import com.corporation.apiclient.services.UserServices;
import com.corporation.apiclient.utils.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/api/users")
@RestController
//Configuration Swagger - Change name and Description on Methods Client
@Tag(name = "Users", description = "Endpoints for Managing User")
public class UserController {

    @Autowired
    private PagedResourcesAssembler<UserDTO> assembler;

    @Autowired
    private UserServices userServices;

    @Autowired
    private ModelMapper modelMapper;

    //@CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Finds All Users", description = "Finds All Users", tags = {"Users"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<PagedModel<EntityModel<UserDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "15") Integer size){
        //@RequestParam(value = "direction", defaultValue = "asc") String direction) {
// esta ignorando as Letras Maiuscula ou Minuscula e um operador ternario se ele identificar DESC na Requisicao ele retorna Um Direction.DESC, se nao Direction.ASC
        //Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;  // Ordenando por name
        //Usando o Page para fazer pesquisa por paginacao
        //Pageable pageable = PageRequest.of(page,size, Sort.by(sortDirection, "name"));
        Pageable pageable = PageRequest.of(page,size);

        return ResponseEntity.ok(userServices.findAll(pageable));
    }

    @GetMapping(value = "/findUserByName/{name}",produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Find User By Name", description = "Find User By Name", tags = {"Users"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<PagedModel<EntityModel<UserDTO>>> findUserByName(
            @PathVariable String name,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "15") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {
// esta ignorando as Letras Maiuscula ou Minuscula e um operador ternario se ele identificar DESC na Requisicao ele retorna Um Direction.DESC, se nao Direction.ASC
        Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;  // Ordenando por name
        //Usando o Page para fazer pesquisa por paginacao
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortDirection, "name"));

        return ResponseEntity.ok(userServices.findUserByName(name,pageable));
    }

    //@CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Finds a User", description = "Finds a User", tags = {"Users"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDTO.class))}),
                    @ApiResponse(description = "No Content", responseCode = "204", content = {@Content}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<UserDTO> findUserById(@PathVariable Long id) {
        UserDTO userDTO = modelMapper.map(userServices.findUserById(id), UserDTO.class);
        userDTO.add(linkTo(methodOn(UserController.class).findUserById(id)).withSelfRel());
        if(userDTO.getAdress() == null) {
            System.out.println("No Adress In User: " + userDTO.getName());
            userDTO.setAdress(new Adress());
            return ResponseEntity.ok().body(userDTO);
        }
        userDTO.add(linkTo(methodOn(AdressController.class).findAdressById(userDTO.getAdress().getId())).withSelfRel());
        return ResponseEntity.ok().body(userDTO);

    }

    @GetMapping(value = "/findUserByEmail/{email}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Finds a User by Email", description = "Finds a User by Email", tags = {"Users"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDTO.class))}),
                    @ApiResponse(description = "No Content", responseCode = "204", content = {@Content}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<UserDTO> findUserByEmail(@PathVariable String email) {
        UserDTO userDTO = modelMapper.map(userServices.findByEmail(email), UserDTO.class);
        userDTO.add(linkTo(methodOn(UserController.class).findUserByEmail(email)).withSelfRel());
        if(userDTO.getAdress() == null) {
            System.out.println("No Adress In User: " + userDTO.getName());
            userDTO.setAdress(new Adress());
            return ResponseEntity.ok().body(userDTO);
        }
        userDTO.add(linkTo(methodOn(AdressController.class).findAdressById(userDTO.getAdress().getId())).withSelfRel());
        return ResponseEntity.ok().body(userDTO);

    }

    //Usar @PatchMapping quando precisar atualizar valor de apenas um campo
    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Disable a specific User by ID", description = "Disable a specific User by ID", tags = {"Users"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDTO.class))}),
                    @ApiResponse(description = "No Content", responseCode = "204", content = {@Content}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<UserDTO> DisableUser(@PathVariable Long id) {
        UserDTO userDTO = modelMapper.map(userServices.updateDisableClient(id),UserDTO.class);
        return ResponseEntity.ok().body(userDTO);

    }

    //@CrossOrigin(origins = {"http://localhost:8080", "https://jp.com.br"})
    @PostMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Adds a New User", description = "Adds a New User by passing in a JSON, XML or YML representation of the Client.", tags = {"Users"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDTO.class))}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<UserDTO> addClient(@Valid @RequestBody UserDTO userDTO) {
        UserDTO DTO = modelMapper.map(userServices.addUser(userDTO), UserDTO.class);
        DTO.add(linkTo(methodOn(UserController.class).findUserById(DTO.getId())).withSelfRel());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(DTO);

    }

    //@CrossOrigin(origins = "http://localhost:8080")
    @PutMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Updates a User", description = "Updates a User by passing in a JSON, XML or YML representation of the User.", tags = {"Users"},
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDTO.class))}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        User userById = modelMapper.map(userServices.findUserById(id), User.class);
        userDTO.setAccountNonLocked(userById.getAccountNonLocked());
        userDTO.setAccountNonExpired(userById.isAccountNonExpired());
        userDTO.setCredentialsNonExpired(userById.getAccountNonExpired());
        userDTO.setPermissions(userById.getPermissions());
        userDTO.setAdress(userById.getAdress());
        userDTO.setEnabled(true);
        userDTO.setId(id);
        UserDTO DTO = modelMapper.map(userServices.updateUser(userDTO), UserDTO.class);
        DTO.add(linkTo(methodOn(UserController.class).findUserById(id)).withSelfRel());
        DTO.add(linkTo(methodOn(AdressController.class).findAdressById(id)).withSelfRel());
        return ResponseEntity.ok().body(DTO);
    }

    //@CrossOrigin(origins = "http://localhost:8080")
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletes a User", description = "Deletes a User by passing in a JSON, XML or YML representation of the User.", tags = {"Users"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = {@Content}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        userServices.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/userWithAdress/{idUser}/{idAdress}")
    @Operation(summary = "Insert Adress in User", description = "Insert Adress in User by passing in a JSON, XML or YML representation of the User.", tags = {"Users"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = {@Content}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<User> insertAdressInUser(@PathVariable Long idUser, @PathVariable Long idAdress) {
        User user = userServices.insertAdressinUser(idUser, idAdress);
        return ResponseEntity.ok().body(user);
    }

}