package org.wcs.myblog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wcs.myblog.dto.AuthorDTO;
import org.wcs.myblog.model.Author;
import org.wcs.myblog.repository.AuthorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorRepository authorRepository;

    public AuthorController(AuthorRepository authorRepository) { this.authorRepository = authorRepository; }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<AuthorDTO> authorDTOS = authors.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(authorDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        Author author = authorRepository.findById(id).orElse(null);
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(author));
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody Author author) {
        Author savedAuthor = authorRepository.save(author);
        return ResponseEntity.status(201).body(convertToDTO(savedAuthor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuthorDTO> deleteAuthor(@PathVariable Long id) {
        Author author = authorRepository.findById(id).orElse(null);

        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        authorRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    private AuthorDTO convertToDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setFirstname(author.getFirstname());
        authorDTO.setLastname(author.getLastname());
        return authorDTO;
    }
}
