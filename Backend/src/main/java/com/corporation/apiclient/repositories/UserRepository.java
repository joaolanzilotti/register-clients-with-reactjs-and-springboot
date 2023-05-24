package com.corporation.apiclient.repositories;

import com.corporation.apiclient.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email =:email")
    Optional<User> findByEmail(@Param("email") String email);

    @Modifying // Usar Quando for modificar Valores nas Querys feita pelo Spring
    @Query("UPDATE User c SET c.enabled = false WHERE c.id =:id")
    void updateDisableClient(@Param("id") Long id);

    @Query("SELECT c FROM User c WHERE c.name LIKE LOWER(concat('%',:name,'%'))")  // lower converte tudo para minusculo
    Page<User> findUserByNamePage(@Param("name") String name, Pageable pageable);
    Optional<User> findByRg(String rg);
    Optional<User> findByCpf(String cpf);
    
}
