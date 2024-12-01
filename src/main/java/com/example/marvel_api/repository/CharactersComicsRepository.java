package com.example.marvel_api.repository;

import com.example.marvel_api.model.CharactersComics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharactersComicsRepository extends JpaRepository<CharactersComics, Long> {
    // Métodos adicionales si es necesario
}