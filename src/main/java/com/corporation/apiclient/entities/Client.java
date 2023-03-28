package com.corporation.apiclient.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client")
public class Client implements Serializable {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name is Null!")
    private String name;
    @NotBlank(message = "Email is Null!")
    @Email
    private String email;

    private String password;

    private String rg;

    @CPF(message = "Invalid CPF")
    private String cpf;
    private Date birthDay;

    @NotBlank(message = "Cellphone is Null!")
    private String cellphone;

    @ManyToOne
    @JoinColumn(name = "adress_id")
    private Adress adress;

}
