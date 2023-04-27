package com.corporation.apiclient.controller;

import com.corporation.apiclient.dto.ClientDTO;
import com.corporation.apiclient.entities.Client;
import com.corporation.apiclient.services.ClientService;
import com.corporation.apiclient.utils.MediaType;
import com.google.gson.reflect.TypeToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/clients")
@RestController
//Configuration Swagger - Change name and Description on Methods Client
@Tag(name = "Client", description = "Endpoints for Managing Client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ModelMapper modelMapper;

    //@CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Finds All Client", description = "Finds All Client", tags = {"Client"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ClientDTO.class)))}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<List<ClientDTO>> findAll() {
        Type listType = new TypeToken<List<ClientDTO>>() {
        }.getType();
        List<ClientDTO> listClientDTO = modelMapper.map(clientService.findAll(), listType);
        listClientDTO.stream().forEach(c -> c.add(linkTo(methodOn(ClientController.class).findClientById(c.getId())).withSelfRel().withSelfRel()));
        //listClientDTO.stream().forEach(c -> c.add(linkTo(methodOn(AdressController.class).adressById(c.getAdress().getId())).withSelfRel().withSelfRel()));
        return ResponseEntity.ok().body(listClientDTO);
    }

    //@CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Finds a Client", description = "Finds a Client", tags = {"Client"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {@Content(schema = @Schema(implementation = ClientDTO.class))}),
                    @ApiResponse(description = "No Content", responseCode = "204", content = {@Content}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<ClientDTO> findClientById(@PathVariable Long id) {
        ClientDTO clientDTO = modelMapper.map(clientService.findClientById(id), ClientDTO.class);
        clientDTO.add(linkTo(methodOn(ClientController.class).findClientById(id)).withSelfRel());
        clientDTO.add(linkTo(methodOn(AdressController.class).findAdressById(clientDTO.getAdress().getId())).withSelfRel());
        return ResponseEntity.ok().body(clientDTO);

    }

    //@CrossOrigin(origins = {"http://localhost:8080", "https://jp.com.br"})
    @PostMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Adds a New Client", description = "Adds a New Client by passing in a JSON, XML or YML representation of the Client.", tags = {"Client"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {@Content(schema = @Schema(implementation = ClientDTO.class))}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<ClientDTO> addClient(@Valid @RequestBody ClientDTO clientDTO) {
        ClientDTO DTO = modelMapper.map(clientService.addClient(clientDTO), ClientDTO.class);
        DTO.add(linkTo(methodOn(ClientController.class).findClientById(DTO.getId())).withSelfRel());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(clientDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(DTO);

    }

    //@CrossOrigin(origins = "http://localhost:8080")
    @PutMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Updates a Client", description = "Updates a Client by passing in a JSON, XML or YML representation of the Client.", tags = {"Client"},
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200", content = {@Content(schema = @Schema(implementation = ClientDTO.class))}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDTO clientDTO) {
        Client clientById = modelMapper.map(clientService.findClientById(id), Client.class);
        clientDTO.setAdress(clientById.getAdress());
        clientDTO.setId(id);
        ClientDTO DTO = modelMapper.map(clientService.updateClient(clientDTO), ClientDTO.class);
        DTO.add(linkTo(methodOn(ClientController.class).findClientById(id)).withSelfRel());
        return ResponseEntity.ok().body(DTO);
    }

    //@CrossOrigin(origins = "http://localhost:8080")
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletes a Client", description = "Deletes a Client by passing in a JSON, XML or YML representation of the Client.", tags = {"Client"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = {@Content}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

}
