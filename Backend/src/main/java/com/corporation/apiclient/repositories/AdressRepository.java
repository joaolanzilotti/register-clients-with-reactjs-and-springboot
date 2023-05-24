package com.corporation.apiclient.repositories;

import com.corporation.apiclient.entities.Adress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdressRepository extends JpaRepository<Adress, Long> {

}
