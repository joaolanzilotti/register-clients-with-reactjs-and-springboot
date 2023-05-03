package com.corporation.apiclient.integrationtests.dto;

import com.corporation.apiclient.entities.Adress;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class ClientDTO implements Serializable {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String rg;
    private String cpf;
    @Temporal(TemporalType.DATE)
    private Date birthDay;
    private String cellphone;
    private Adress adress;
    

}
