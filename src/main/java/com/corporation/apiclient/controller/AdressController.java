package com.corporation.apiclient.controller;

import com.corporation.apiclient.dto.AdressDTO;
import com.corporation.apiclient.dto.ClientDTO;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.entities.Client;
import com.corporation.apiclient.repositories.ClientRepository;
import com.corporation.apiclient.services.AdressService;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping(value = "/adress")
@RestController
@Tag(name = "Adress", description = "Endpoints for Managing Adress")
public class AdressController {

    @Autowired
    private AdressService adressService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Finds All Adress", description = "Finds All Adress", tags = {"Adress"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdressDTO.class)))}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<List<AdressDTO>> findAllAdress() {
        Type listType = new TypeToken<List<AdressDTO>>() {}.getType();
        List<AdressDTO> listAdressDTO = modelMapper.map(adressService.findAllAdress(), listType);
        listAdressDTO.stream().forEach(a -> a.add(linkTo(methodOn(AdressController.class).findAdressById(a.getId())).withSelfRel()));
        return ResponseEntity.ok().body(listAdressDTO);
    }

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Finds a Adress", description = "Finds a Adress", tags = {"Adress"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {@Content(schema = @Schema(implementation = AdressDTO.class))}),
                    @ApiResponse(description = "No Content", responseCode = "204", content = {@Content}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<AdressDTO> findAdressById(@PathVariable Long id) {
        AdressDTO adressDTO = modelMapper.map(adressService.findAdressById(id), AdressDTO.class);
        adressDTO.add(linkTo(methodOn(AdressController.class).findAdressById(adressDTO.getId())).withSelfRel());
        return ResponseEntity.ok().body(adressDTO);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Updates a Adress", description = "Updates a Adress by passing in a JSON, XML or YML representation of the Adress.", tags = {"Adress"},
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200", content = {@Content(schema = @Schema(implementation = AdressDTO.class))}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<AdressDTO> updateAdress(@PathVariable Long id, @RequestBody AdressDTO adressDTO) {
        adressDTO.setId(id);
        AdressDTO DTO = modelMapper.map(adressService.updateAdress(adressDTO), AdressDTO.class);
        DTO.add(linkTo(methodOn(AdressController.class).findAdressById(DTO.getId())).withSelfRel());
        return ResponseEntity.ok().body(DTO);

    }

    @PostMapping(value = "/{id}")
    @Operation(summary = "Adds a New Adress", description = "Adds a New Adress by passing in a JSON, XML or YML representation of the Adress.", tags = {"Adress"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {@Content(schema = @Schema(implementation = AdressDTO.class))}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<AdressDTO> addAdress(@PathVariable Long id, @Valid @RequestBody AdressDTO adressDTO) {
        AdressDTO DTO = modelMapper.map(adressService.addAdress(adressDTO, id), AdressDTO.class);
        DTO.add(linkTo(methodOn(AdressController.class).findAdressById(DTO.getId())).withSelfRel());
        DTO.add(linkTo(methodOn(ClientController.class).findClientById(id)).withSelfRel());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(DTO.getId()).toUri();
        return ResponseEntity.created(uri).body(DTO);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletes a Adress", description = "Deletes a Adress by passing in a JSON, XML or YML representation of the Adress.", tags = {"Adress"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = {@Content}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<Adress> deleteAdress(@PathVariable Long id) {
        adressService.deleteAdress(id);
        return ResponseEntity.noContent().build();
    }

}
