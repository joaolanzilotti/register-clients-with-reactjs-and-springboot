package com.corporation.apiclient.repositories;

import com.corporation.apiclient.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
