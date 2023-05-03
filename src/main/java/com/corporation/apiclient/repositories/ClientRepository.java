package com.corporation.apiclient.repositories;

import com.corporation.apiclient.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Modifying // Usar Quando for modificar Valores nas Querys feita pelo Spring
    @Query("UPDATE Client c SET c.enabled = false WHERE c.id =:id")
    void updateDisableClient(@Param("id") Long id);

    Optional<Client> findByEmail(String email);
    Optional<Client> findByRg(String rg);
    Optional<Client> findByCpf(String cpf);
    
}
