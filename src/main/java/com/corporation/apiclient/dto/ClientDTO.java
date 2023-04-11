package com.corporation.apiclient.dto;

import com.corporation.apiclient.entities.Adress;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "email", "password", "cellphone", "rg", "cpf", "birthDay", "adress"})
public class ClientDTO {

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
