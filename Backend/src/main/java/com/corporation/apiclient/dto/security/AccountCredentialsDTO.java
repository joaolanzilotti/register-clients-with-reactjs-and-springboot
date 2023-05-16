package com.corporation.apiclient.dto.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountCredentialsDTO implements Serializable {

    private String username;
    private String password;

}
