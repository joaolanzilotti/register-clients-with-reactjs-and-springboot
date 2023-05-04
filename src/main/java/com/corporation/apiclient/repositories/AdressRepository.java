package com.corporation.apiclient.repositories;

import com.corporation.apiclient.entities.Adress;
import com.corporation.apiclient.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdressRepository extends JpaRepository<Adress, Long> {

}
