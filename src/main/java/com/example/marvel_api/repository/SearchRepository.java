package com.example.marvel_api.repository;

import com.example.marvel_api.model.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<Search, Long> {
    List<Search> findByUserId(Long userId);
}
