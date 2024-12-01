package com.example.marvel_api.controller;

import com.example.marvel_api.model.Character;
import com.example.marvel_api.model.Comic;
import com.example.marvel_api.model.Search;
import com.example.marvel_api.service.MarvelApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marvel")
public class MarvelApiController {

    @Autowired
    private MarvelApiService marvelApiService;

    // Método para obtener personajes por nombre
    @GetMapping("/character")
    public List<Character> getCharactersByName(@RequestParam String name, @RequestParam Long userId) {
        return marvelApiService.getCharactersByNameAndSave(name, userId);
    }

    // Método para obtener todos los personajes
    @GetMapping("/characters/all")
    public List<Character> getAllCharacters() {
        return marvelApiService.getAllCharacters();
    }

    // Método para obtener detalles de un personaje específico
    @GetMapping("/characters/{id}")
    public Character getCharacterDetails(@PathVariable Long id, @RequestParam Long userId) {
        return marvelApiService.getCharacterDetailsAndSave(id, userId);
    }

    // Método para obtener cómics por ID de personaje
    @GetMapping("/characters/{id}/comics")
    public List<Comic> getComicsByCharacterId(@PathVariable Long id, @RequestParam Long userId) {
        return marvelApiService.getComicsByCharacterIdAndSave(id, userId);
    }

    // Método para obtener todos los cómics de Marvel
    @GetMapping("/comics/all")
    public List<Comic> getAllComics() {
        return marvelApiService.getAllComicsAndSave();
    }

    // Método para obtener detalles de un cómic por ID
    @GetMapping("/comics/{id}")
    public Comic getComicById(@PathVariable Long id, @RequestParam Long userId) {
        return marvelApiService.getComicById(id, userId);
    }

    // Método para realizar una búsqueda y guardar el término de búsqueda
    @PostMapping("/search")
    public void searchAndSave(@RequestParam Long userId, @RequestParam String searchTerm) {
        marvelApiService.searchAndSave(userId, searchTerm);
    }

    // Método para obtener el historial de búsquedas de un usuario
    @GetMapping("/search/history/{userId}")
    public List<Search> getSearchHistory(@PathVariable Long userId) {
        return marvelApiService.getSearchHistory(userId);
    }
}
