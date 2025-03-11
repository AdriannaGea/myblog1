package org.wcs.myblog.service;

import org.springframework.stereotype.Service;
import org.wcs.myblog.dto.AuthorDTO;
import org.wcs.myblog.exception.ResourceNotFoundException;
import org.wcs.myblog.mapper.AuthorMapper;
import org.wcs.myblog.model.Author;
import org.wcs.myblog.repository.ArticleAuthorRepository;
import org.wcs.myblog.repository.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final ArticleAuthorRepository articleAuthorRepository;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper, ArticleAuthorRepository articleAuthorRepository) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.articleAuthorRepository = articleAuthorRepository;
    }

    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            throw new ResourceNotFoundException("Aucun auteur trouvé.");
        }
        return authors.stream().map(authorMapper::convertToDTO).collect(Collectors.toList());
    }

    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'auteur avec l'id " + id + " n'a pas été trouvé"));
        return authorMapper.convertToDTO(author);
    }

    public AuthorDTO createAuthor(Author author) {
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.convertToDTO(savedAuthor);
    }

    public boolean deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'auteur avec l'id " + id + " n'a pas été trouvé"));
        authorRepository.delete(author);
        return true;
    }
}