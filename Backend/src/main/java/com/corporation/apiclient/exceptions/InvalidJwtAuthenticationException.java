package com.corporation.apiclient.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidJwtAuthenticationException extends AuthenticationException {


    public InvalidJwtAuthenticationException(String message) {
        super(message);
    }
}
