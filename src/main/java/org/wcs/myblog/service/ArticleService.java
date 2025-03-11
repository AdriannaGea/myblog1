package org.wcs.myblog.service;

import org.springframework.stereotype.Service;
import org.wcs.myblog.dto.ArticleDTO;
import org.wcs.myblog.exception.CategoryNotFoundException;
import org.wcs.myblog.exception.ImageNotFoundException;
import org.wcs.myblog.exception.ResourceNotFoundException;
import org.wcs.myblog.mapper.ArticleMapper;
import org.wcs.myblog.model.*;
import org.wcs.myblog.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final AuthorRepository authorRepository;
    private final ArticleAuthorRepository articleAuthorRepository;

    public ArticleService(
            ArticleRepository articleRepository,
            ArticleMapper articleMapper,
            CategoryRepository categoryRepository,
            ImageRepository imageRepository,
            AuthorRepository authorRepository,
            ArticleAuthorRepository articleAuthorRepository) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.authorRepository = authorRepository;
        this.articleAuthorRepository = articleAuthorRepository;
    }

    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        return articles.stream().map(articleMapper::convertToDTO).collect(Collectors.toList());
    }

    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'article avec l'id " + id + " n'a pas été trouvé"));
        return articleMapper.convertToDTO(article);
    }

    public ArticleDTO createArticle(Article article) {
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        if (article.getCategory() != null) {
            Category category = categoryRepository.findById(article.getCategory().getId())
                    .orElseThrow(() -> new CategoryNotFoundException("La catégorie avec l'id " + article.getCategory().getId() + " n'a pas été trouvée"));
            article.setCategory(category);
        }

        List<Image> validImages = new ArrayList<>();
        if (article.getImages() != null) {
            for (Image image : article.getImages()) {
                Image existingImage = imageRepository.findById(image.getId())
                        .orElseThrow(() -> new ImageNotFoundException("L'image avec l'id " + image.getId() + " n'a pas été trouvée"));
                validImages.add(existingImage);
            }
            article.setImages(validImages);
        }

        Article savedArticle = articleRepository.save(article);

        if (article.getArticleAuthors() != null) {
            for (ArticleAuthor articleAuthor : article.getArticleAuthors()) {
                Author author = authorRepository.findById(articleAuthor.getAuthor().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("L'auteur avec l'id " + articleAuthor.getAuthor().getId() + " n'a pas été trouvé"));
                articleAuthor.setAuthor(author);
                articleAuthor.setArticle(savedArticle);
                articleAuthorRepository.save(articleAuthor);
            }
        }

        return articleMapper.convertToDTO(savedArticle);
    }

    public ArticleDTO updateArticle(Long id, Article articleDetails) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'article avec l'id " + id + " n'a pas été trouvé"));

        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article.setUpdatedAt(LocalDateTime.now());

        if (articleDetails.getCategory() != null) {
            Category category = categoryRepository.findById(articleDetails.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("La catégorie avec l'id " + articleDetails.getCategory().getId() + " n'a pas été trouvée"));
            article.setCategory(category);
        }

        List<Image> validImages = new ArrayList<>();
        if (articleDetails.getImages() != null) {
            for (Image image : articleDetails.getImages()) {
                Image existingImage = imageRepository.findById(image.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("L'image avec l'id " + image.getId() + " n'a pas été trouvée"));
                validImages.add(existingImage);
            }
            article.setImages(validImages);
        } else {
            article.getImages().clear();
        }

        articleAuthorRepository.deleteAll(article.getArticleAuthors());
        List<ArticleAuthor> updatedArticleAuthors = new ArrayList<>();
        if (articleDetails.getArticleAuthors() != null) {
            for (ArticleAuthor articleAuthorDetails : articleDetails.getArticleAuthors()) {
                Author author = authorRepository.findById(articleAuthorDetails.getAuthor().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("L'auteur avec l'id " + articleAuthorDetails.getAuthor().getId() + " n'a pas été trouvé"));

                ArticleAuthor newArticleAuthor = new ArticleAuthor();
                newArticleAuthor.setAuthor(author);
                newArticleAuthor.setArticle(article);
                newArticleAuthor.setContribution(articleAuthorDetails.getContribution());
                updatedArticleAuthors.add(newArticleAuthor);
            }
            articleAuthorRepository.saveAll(updatedArticleAuthors);
            article.setArticleAuthors(updatedArticleAuthors);
        }

        Article updatedArticle = articleRepository.save(article);
        return articleMapper.convertToDTO(updatedArticle);
    }

    public boolean deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'article avec l'id " + id + " n'a pas été trouvé"));

        articleAuthorRepository.deleteAll(article.getArticleAuthors());
        articleRepository.delete(article);
        return true;
    }
}
