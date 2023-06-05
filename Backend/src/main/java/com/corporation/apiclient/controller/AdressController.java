package com.corporation.apiclient.controller;

import com.corporation.apiclient.dto.AdressDTO;
import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.services.AdressService;
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
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping(value = "/api/adress")
@RestController
@Tag(name = "Adress", description = "Endpoints for Managing Adress")
public class AdressController {

    @Autowired
    private AdressService adressService;

    @Autowired
    private ModelMapper modelMapper;


    //@CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Finds All Adress", description = "Finds All Adress", tags = {"Adress"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdressDTO.class)))}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<PagedModel<EntityModel<AdressDTO>>> findAllAdress(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "15") Integer size) {
        // esta ignorando as Letras Maiuscula ou Minuscula e um operador ternario se ele identificar DESC na Requisicao ele retorna Um Direction.DESC, se nao Direction.ASC

        //Usando o Page para fazer pesquisa por paginacao
        Pageable pageable = PageRequest.of(page,size);

        return ResponseEntity.ok(adressService.findAllAdress(pageable));
    }

    //@CrossOrigin(origins = "http://localhost:8080")
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

    //@CrossOrigin(origins = "http://localhost:8080")
    @PutMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
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

    //@CrossOrigin(origins = "http://localhost:8080")
    @PostMapping(
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Adds a New Adress", description = "Adds a New Adress by passing in a JSON, XML or YML representation of the Adress.", tags = {"Adress"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {@Content(schema = @Schema(implementation = AdressDTO.class))}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})})
    public ResponseEntity<AdressDTO> addAdress(@Valid @RequestBody AdressDTO adressDTO) {
        AdressDTO DTO = modelMapper.map(adressService.addAdress(adressDTO), AdressDTO.class);
        DTO.add(linkTo(methodOn(AdressController.class).findAdressById(DTO.getId())).withSelfRel());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(DTO.getId()).toUri();
        return ResponseEntity.created(uri).body(DTO);
    }

    //@CrossOrigin(origins = "http://localhost:8080")
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
