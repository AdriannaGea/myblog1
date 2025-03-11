package org.wcs.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wcs.myblog.dto.AuthorDTO;
import org.wcs.myblog.model.Author;
import org.wcs.myblog.repository.AuthorRepository;
import org.wcs.myblog.service.AuthorService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) { this.authorService = authorService; }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<AuthorDTO> authors = authorService.getAllAuthors();
        if (authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        AuthorDTO author = authorService.getAuthorById(id);
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(author);
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody Author author) {
        AuthorDTO savedAuthor = authorService.createAuthor(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuthorDTO> deleteAuthor(@PathVariable Long id) {

        if (authorService.deleteAuthor(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
