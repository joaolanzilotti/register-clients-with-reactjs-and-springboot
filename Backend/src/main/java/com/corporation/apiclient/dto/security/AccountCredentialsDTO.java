package com.corporation.apiclient.dto.security;

import com.corporation.apiclient.entities.Adress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountCredentialsDTO implements Serializable {

    private String name;
    private String email;
    private String password;


}
