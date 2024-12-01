package com.example.marvel_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "comics")
public class Comic {

    @Id
    private Long id;  // El id ya no es generado automáticamente

    private String title;
    private String description;
    private String imageUrl;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
