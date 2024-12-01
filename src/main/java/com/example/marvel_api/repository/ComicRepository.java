package com.example.marvel_api.repository;

import com.example.marvel_api.model.Comic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {
    // Métodos adicionales si es necesario
}
