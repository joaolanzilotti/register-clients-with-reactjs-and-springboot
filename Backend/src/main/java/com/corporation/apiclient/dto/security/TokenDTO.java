package com.corporation.apiclient.dto.security;

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
public class TokenDTO implements Serializable {

    private String email;
    private Boolean authenticated;
    private Date created;
    private Date expiration;
    private String token;
    private String refreshToken;

}
