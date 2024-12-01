package com.example.marvel_api.repository;

import com.example.marvel_api.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    // Métodos adicionales si es necesario
}
