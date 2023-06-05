package com.corporation.apiclient.integrationtests.dto;

import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.entities.Permission;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class UserDTO implements Serializable {

    private Long id;

    private String email;

    private String rg;

    private String cpf;

    @Temporal(TemporalType.DATE)
    private Date birthDay;

    private String sexo;

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
