package org.wcs.myblog.mapper;

import org.springframework.stereotype.Component;
import org.wcs.myblog.dto.AuthorDTO;
import org.wcs.myblog.model.Author;

@Component
public class AuthorMapper {

    public AuthorDTO convertToDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setFirstname(author.getFirstname());
        authorDTO.setLastname(author.getLastname());
        return authorDTO;
    }
}
