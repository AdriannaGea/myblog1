package org.wcs.myblog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wcs.myblog.model.Article;
import org.wcs.myblog.repository.ArticleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/articles")

public class ArticleController {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
    Article article = articleRepository.findById(id).orElse(null);
    if (article == null) {
        return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(article);
    }

    @GetMapping("/search-title")
    public ResponseEntity<List<Article>> getArticlesByTitle(@RequestParam String searchTerms) {
        List<Article> articles = articleRepository.findByTitle(searchTerms);
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/search-content-containing")
    public ResponseEntity<List<Article>> getArticlesByContentContaining(@RequestParam String searchContent) {
        List<Article> articles = articleRepository.findByContentContaining(searchContent);
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/search-created-after")
    public ResponseEntity<List<Article>> getArticlesCreatedAtAfter(@RequestParam LocalDateTime searchDate) {
        List<Article> articles = articleRepository.findByCreatedAtAfter(searchDate);
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/last-five")
    public ResponseEntity<List<Article>> getLastFiveArticlesOrdered() {
        List<Article> articles = articleRepository.findTop5ByOrderByCreatedAtDesc();
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        Article savedArticle = articleRepository.save(article);
        return ResponseEntity.ok(savedArticle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {

        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
        return ResponseEntity.notFound().build();
        }
        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article.setUpdatedAt(LocalDateTime.now());

        Article updateArticle = articleRepository.save(article);
        return ResponseEntity.ok(updateArticle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Article> deleteArticle(@PathVariable Long id) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return ResponseEntity.notFound().build();
        }
        articleRepository.delete(article);
        return ResponseEntity.noContent().build();
    }

}

