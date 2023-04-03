package com.corporation.apiclient.repositories;

import com.corporation.apiclient.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);
    Optional<Client> findByRg(String rg);
    Optional<Client> findByCpf(String cpf);
    
}
