package com.corporation.apiclient.dto;

import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.entities.Permission;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "email", "cellphone", "rg", "cpf", "birthDay", "adress", "enabled", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"})
public class UserDTO extends RepresentationModel<UserDTO> implements Serializable {

    private Long id;

    private String email;

    private String rg;

    private String cpf;

    @Temporal(TemporalType.DATE)
    private Date birthDay;

    private String cellphone;

    private Adress adress;

    private boolean enabled;

    private String name;

    private String password;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private List<Permission> permissions;


}
