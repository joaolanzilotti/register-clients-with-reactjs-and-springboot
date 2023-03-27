package com.corporation.apiclient.dto;

import com.corporation.apiclient.entities.Client;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdressDTO implements Serializable {

    private Long id;
    private String street;
    private String district;
    private String number;
    private String city;
    private String state;

}
