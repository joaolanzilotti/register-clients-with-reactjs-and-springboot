package com.corporation.apiclient.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataIntegratyViolationException extends RuntimeException{


    public DataIntegratyViolationException(String message) {
        super(message);
    }
}
