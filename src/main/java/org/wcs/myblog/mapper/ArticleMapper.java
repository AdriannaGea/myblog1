package org.wcs.myblog.mapper;

import org.springframework.stereotype.Component;
import org.wcs.myblog.dto.ArticleCreateDTO;
import org.wcs.myblog.dto.ArticleDTO;
import org.wcs.myblog.dto.AuthorDTO;
import org.wcs.myblog.model.Article;
import org.wcs.myblog.model.Image;

import java.util.stream.Collectors;

@Component

public class ArticleMapper {

    public Article convertToEntity(ArticleCreateDTO articleCreateDTO) {
        Article article = new Article();
        article.setTitle(articleCreateDTO.getTitle());
        article.setContent(articleCreateDTO.getContent());

        return article;
    }

    public ArticleDTO convertToDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setContent(article.getContent());
        articleDTO.setUpdatedAt(article.getUpdatedAt());
        if (article.getCategory() != null) {
            articleDTO.setCategoryName(article.getCategory().getName());
        }
        if (article.getImages() != null) {
            articleDTO.setImagesUrls(article.getImages().stream().map(Image::getUrl).collect(Collectors.toList()));
        }
        if (article.getArticleAuthors() != null) {
            articleDTO.setAuthorDTOs(article.getArticleAuthors().stream()
                    .filter(articleAuthor -> articleAuthor.getAuthor() != null)
                    .map(articleAuthor -> {
                        AuthorDTO authorDTO = new AuthorDTO();
                        authorDTO.setId(articleAuthor.getAuthor().getId());
                        authorDTO.setFirstname(articleAuthor.getAuthor().getFirstname());
                        authorDTO.setLastname(articleAuthor.getAuthor().getLastname());
                        return  authorDTO;
                    })
                    .collect(Collectors.toList()));
        }
        return articleDTO;
    }

}
