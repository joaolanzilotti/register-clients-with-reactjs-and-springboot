package com.corporation.apiclient.entities;

import java.io.Serializable;
import java.util.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    private String email;

    private String rg;

    @CPF(message = "Invalid CPF")
    private String cpf;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_day")
    private Date birthDay;

    @NotBlank(message = "Cellphone is Null!")
    private String cellphone;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adress_id")
    private Adress adress;

    private boolean enabled;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "account_non_expired")
    private Boolean accountNonExpired;

    @Column(name = "account_non_locked")
    private Boolean accountNonLocked;

    @Column(name = "credentials_non_expired")
    private Boolean credentialsNonExpired;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_permission", joinColumns = {@JoinColumn (name = "id_user")},
            inverseJoinColumns = {@JoinColumn (name = "id_permission")}
    )
    private List<Permission> permissions;

    public List<String> getRoles() {
        List<String> roles = new ArrayList<>();
        for (Permission permission : permissions) {
            roles.add(permission.getDescription());
        }
        return roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.permissions;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public User(Long id, String email, String rg, String cpf, Date birthDay, String cellphone, Adress adress, boolean enabled, String name, String password) {
        this.id = id;
        this.email = email;
        this.rg = rg;
        this.cpf = cpf;
        this.birthDay = birthDay;
        this.cellphone = cellphone;
        this.adress = adress;
        this.enabled = enabled;
        this.name = name;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (enabled != user.enabled) return false;
        if (!Objects.equals(id, user.id)) return false;
        if (!Objects.equals(email, user.email)) return false;
        if (!Objects.equals(rg, user.rg)) return false;
        if (!Objects.equals(cpf, user.cpf)) return false;
        if (!Objects.equals(birthDay, user.birthDay)) return false;
        if (!Objects.equals(cellphone, user.cellphone)) return false;
        if (!Objects.equals(adress, user.adress)) return false;
        if (!Objects.equals(name, user.name)) return false;
        if (!Objects.equals(password, user.password)) return false;
        if (!Objects.equals(accountNonExpired, user.accountNonExpired))
            return false;
        if (!Objects.equals(accountNonLocked, user.accountNonLocked))
            return false;
        if (!Objects.equals(credentialsNonExpired, user.credentialsNonExpired))
            return false;
        return Objects.equals(permissions, user.permissions);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (rg != null ? rg.hashCode() : 0);
        result = 31 * result + (cpf != null ? cpf.hashCode() : 0);
        result = 31 * result + (birthDay != null ? birthDay.hashCode() : 0);
        result = 31 * result + (cellphone != null ? cellphone.hashCode() : 0);
        result = 31 * result + (adress != null ? adress.hashCode() : 0);
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (accountNonExpired != null ? accountNonExpired.hashCode() : 0);
        result = 31 * result + (accountNonLocked != null ? accountNonLocked.hashCode() : 0);
        result = 31 * result + (credentialsNonExpired != null ? credentialsNonExpired.hashCode() : 0);
        result = 31 * result + (permissions != null ? permissions.hashCode() : 0);
        return result;
    }
}