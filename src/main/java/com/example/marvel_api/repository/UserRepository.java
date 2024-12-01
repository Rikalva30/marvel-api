package com.example.marvel_api.repository;

import com.example.marvel_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Puedes agregar métodos personalizados aquí si lo necesitas
}