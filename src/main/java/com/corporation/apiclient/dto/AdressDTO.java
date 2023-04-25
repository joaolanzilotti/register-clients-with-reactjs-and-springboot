package com.corporation.apiclient.dto;

import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.entities.Client;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdressDTO extends RepresentationModel<ClientDTO> implements Serializable {

    private Long id;
    private String street;
    private String district;
    private String number;
    private String city;
    private String state;
    private List<Client> clients;

}
