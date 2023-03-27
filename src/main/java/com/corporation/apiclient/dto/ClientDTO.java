package com.corporation.apiclient.dto;

import com.corporation.apiclient.entities.Adress;
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
public class ClientDTO {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String rg;
    private String cpf;
    private Date birthDay;
    private String cellphone;
    private Adress adressDTO;
    

}
