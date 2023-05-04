package com.corporation.apiclient.integrationtests.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class AdressDTO implements Serializable {

    private Long id;
    private String street;
    private String district;
    private String number;
    private String city;
    private String state;

}
