package com.example.marvel_api.service;

import com.example.marvel_api.model.Character;
import com.example.marvel_api.model.Comic;
import com.example.marvel_api.model.CharactersComics;
import com.example.marvel_api.model.Search;
import com.example.marvel_api.model.User;
import com.example.marvel_api.repository.CharacterRepository;
import com.example.marvel_api.repository.ComicRepository;
import com.example.marvel_api.repository.CharactersComicsRepository;
import com.example.marvel_api.repository.SearchRepository;
import com.example.marvel_api.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MarvelApiService {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private CharactersComicsRepository charactersComicsRepository;

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${marvel.public.key}")
    private String publicKey;

    @Value("${marvel.private.key}")
    private String privateKey;

    private final String baseUrl = "https://gateway.marvel.com:443/v1/public/";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public MarvelApiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // Generar el hash MD5 para la autenticación de la API
    private String generateHash(String ts) {
        String input = ts + privateKey + publicKey;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }

    // Método para obtener personajes por nombre y guardarlos en la base de datos
    public List<Character> getCharactersByNameAndSave(String name, Long userId) {
        List<Character> characters = getCharactersByName(name, userId);
        for (Character character : characters) {
            // Verificar si el personaje ya está en la base de datos
            if (characterRepository.findById(character.getId()).isEmpty()) {
                saveCharacter(character);  // Guardar solo si no existe
                saveSearch(userId, character.getName());  // Registrar búsqueda
            }
        }
        return characters;
    }
    

    // Método para obtener personajes de la API
    public List<Character> getCharactersByName(String name, Long userId) {
        try {
            String ts = String.valueOf(System.currentTimeMillis() / 1000);  // Timestamp
            String hash = generateHash(ts);  // Hash MD5

            String url = String.format("%scharacters?name=%s&ts=%s&apikey=%s&hash=%s", baseUrl, name, ts, publicKey, hash);
            String response = restTemplate.getForObject(url, String.class);

            saveSearch(userId, name);

            return mapCharacterResponse(response);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving characters by name", e);
        }
    }

    // Método para obtener todos los personajes de la API
    public List<Character> getAllCharacters() {
        try {
            String ts = String.valueOf(System.currentTimeMillis() / 1000);  // Timestamp
            String hash = generateHash(ts);  // Hash MD5

            String url = String.format("%scharacters?ts=%s&apikey=%s&hash=%s", baseUrl, ts, publicKey, hash);
            String response = restTemplate.getForObject(url, String.class);

            return mapCharacterResponse(response);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all characters", e);
        }
    }

    // Método para obtener detalles de un personaje específico y guardarlo
    public Character getCharacterDetailsAndSave(Long characterId, Long userId) {
        Character character = getCharacterDetails(characterId, userId);
        if (character != null && characterRepository.findById(character.getId()).isEmpty()) {
            saveCharacter(character);  // Guardar solo si no existe
            saveSearch(userId, character.getName());  // Registrar búsqueda
        }
        return character;
    }
    

    // Método para obtener detalles de un personaje específico
    public Character getCharacterDetails(Long characterId, Long userId) {
        try {
            String ts = String.valueOf(System.currentTimeMillis() / 1000); // Timestamp
            String hash = generateHash(ts); // Hash MD5

            String url = String.format("%scharacters/%d?ts=%s&apikey=%s&hash=%s", baseUrl, characterId, ts, publicKey, hash);
            String response = restTemplate.getForObject(url, String.class);

            List<Character> characters = mapCharacterResponse(response);

            // Validar que la lista no sea nula ni vacía
            if (characters != null && !characters.isEmpty()) {
                saveSearch(userId, characters.get(0).getName());
                return characters.get(0); // Retornar el primer personaje
            }

            // Retornar null si no hay personajes
            return null;

        } catch (Exception e) {
            throw new RuntimeException("Error retrieving character details for ID: " + characterId, e);
        }
    }

    // Método para obtener cómics de un personaje por ID y guardarlos
    public List<Comic> getComicsByCharacterIdAndSave(Long characterId, Long userId) {
        List<Comic> comics = getComicsByCharacterId(characterId, userId);
        for (Comic comic : comics) {
            if (comicRepository.findById(comic.getId()).isEmpty()) {
                saveComic(comic);
                saveSearch(userId, comic.getTitle());
            }
            saveCharacterComicRelationship(characterId, comic.getId());
        }
        return comics;
    }

    // Método para obtener cómics por personaje
    public List<Comic> getComicsByCharacterId(Long characterId, Long userId) {
        try {
            String ts = String.valueOf(System.currentTimeMillis() / 1000);  // Timestamp
            String hash = generateHash(ts);  // Hash MD5

            String url = String.format("%scharacters/%d/comics?ts=%s&apikey=%s&hash=%s", baseUrl, characterId, ts, publicKey, hash);
            String response = restTemplate.getForObject(url, String.class);

            saveSearch(userId, String.valueOf(characterId));

            return mapComicResponse(response);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving comics by character ID", e);
        }
    }

    // Método para obtener todos los cómics de Marvel
    public List<Comic> getAllComicsAndSave() {
        List<Comic> comics = getAllComics();
        for (Comic comic : comics) {
            if (comicRepository.findById(comic.getId()).isEmpty()) {
                saveComic(comic);
            }
        }
        return comics;
    }

    // Método para obtener todos los cómics
    public List<Comic> getAllComics() {
        try {
            String ts = String.valueOf(System.currentTimeMillis() / 1000);  // Timestamp
            String hash = generateHash(ts);  // Hash MD5

            String url = String.format("%scomics?ts=%s&apikey=%s&hash=%s", baseUrl, ts, publicKey, hash);
            String response = restTemplate.getForObject(url, String.class);

            return mapComicResponse(response);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all comics", e);
        }
    }

    // Método para obtener cómics por ID
    public Comic getComicById(Long comicId, Long userId) {
        try {
            String ts = String.valueOf(System.currentTimeMillis() / 1000);  // Timestamp
            String hash = generateHash(ts);  // Hash MD5

            String url = String.format("%scomics/%d?ts=%s&apikey=%s&hash=%s", baseUrl, comicId, ts, publicKey, hash);
            String response = restTemplate.getForObject(url, String.class);

            List<Comic> comics = mapComicResponse(response);

            // Validar que la lista no sea nula ni vacía
            if (comics != null && !comics.isEmpty()) {
                saveSearch(userId, comics.get(0).getTitle());
                return comics.get(0); // Retornar el primer personaje
            }

            // Retornar null si no hay personajes
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving comic by ID", e);
        }
    }

    // Método para guardar personajes en la base de datos
    private Character saveCharacter(Character character) {
        // Verificar si el ID ya existe en la base de datos
        if (characterRepository.existsById(character.getId())) {
            throw new RuntimeException("Character with ID " + character.getId() + " already exists.");
        }
        return characterRepository.save(character);  // Guardar el personaje
    }

    // Método para guardar cómics en la base de datos
    private Comic saveComic(Comic comic) {
        // Verificar si el ID ya existe en la base de datos
        if (comicRepository.existsById(comic.getId())) {
            throw new RuntimeException("Comic with ID " + comic.getId() + " already exists.");
        }
        return comicRepository.save(comic);  // Guardar el cómic
    }

    // Método para guardar la relación entre personajes y cómics
    private void saveCharacterComicRelationship(Long characterId, Long comicId) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Character not found with id: " + characterId));
        Comic comic = comicRepository.findById(comicId)
                .orElseThrow(() -> new RuntimeException("Comic not found with id: " + comicId));
    
        // Crear la relación entre el personaje y el cómic
        CharactersComics charactersComics = new CharactersComics();
        charactersComics.setCharacter(character);
        charactersComics.setComic(comic);
    
        // Guardar la relación
        charactersComicsRepository.save(charactersComics);
    }
    
    public void saveSearch(Long userId, String searchTerm) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    
        Search search = new Search();
        search.setUser(user); // Asocia el usuario con la búsqueda
        search.setSearchTerm(searchTerm);
    
        searchRepository.save(search); // Hibernate asigna automáticamente el valor de searchDate
    }
    
    // Método para registrar una búsqueda y obtener personajes
    public void searchAndSave(Long userId, String searchTerm) {
        // Guardar la búsqueda en la base de datos
        saveSearch(userId, searchTerm);

        // Lógica de búsqueda, por ejemplo, por nombre de personaje
        getCharactersByNameAndSave(searchTerm, userId);
    }

    private List<Character> mapCharacterResponse(String response) {
        List<Character> characters = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode dataNode = rootNode.path("data").path("results");
            for (JsonNode node : dataNode) {
                Character character = objectMapper.treeToValue(node, Character.class);
                characters.add(character);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error parsing character response", e);
        }
        return characters;
    }
    
    private List<Comic> mapComicResponse(String response) {
        List<Comic> comics = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode dataNode = rootNode.path("data").path("results");
            for (JsonNode node : dataNode) {
                Comic comic = objectMapper.treeToValue(node, Comic.class);
                comics.add(comic);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error parsing comic response", e);
        }
        return comics;
    }
    

    // Método para obtener el historial de búsquedas de un usuario
    public List<Search> getSearchHistory(Long userId) {
        try {
            return searchRepository.findByUserId(userId);  // Obtener las búsquedas por userId
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving search history", e);
        }
    }
}
