package com.corporation.apiclient.integrationtests.dto.security;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@XmlRootElement
public class AccountCredentialsDTO implements Serializable {

    private String name;
    private String email;
    private String password;

    public AccountCredentialsDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
