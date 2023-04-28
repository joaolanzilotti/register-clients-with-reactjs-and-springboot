package com.corporation.apiclient.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

@Entity
@Table(name = "permission")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Permission implements Serializable, GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Override
    public String getAuthority() {
        return this.description;
    }
}
