package com.example.marvel_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "characters")
public class Character {

    @Id
    private Long id;  // El id ya no es generado automáticamente

    private String name;
    private String description;
    private String imageUrl;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
