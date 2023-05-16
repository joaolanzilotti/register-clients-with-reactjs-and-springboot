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

    private String username;
    private String password;

}
