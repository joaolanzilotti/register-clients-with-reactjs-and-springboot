package com.corporation.apiclient.controller;

import com.corporation.apiclient.dto.security.AccountCredentialsDTO;
import com.corporation.apiclient.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthServices authServices;

    @Operation(summary = "Authenticates a Client and Return a Token")
    @PostMapping(value = "/signin")
    public ResponseEntity signin(@RequestBody AccountCredentialsDTO data){
        if (checkIfparamsIsNotNull(data)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client");
        }
            var token = authServices.signin(data);
            if(token == null){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client");
            }
            return token;
    }

    private boolean checkIfparamsIsNotNull(AccountCredentialsDTO data){
        return data == null || data.getUsername() == null || data.getUsername().isBlank() || data.getPassword() == null || data.getPassword().isBlank();
    }

}