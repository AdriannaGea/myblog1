package org.wcs.myblog.repository;

import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.wcs.myblog.model.Article;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByTitle(String title);

    List<Article> findByContentContaining(String terms);

    List<Article> findByCreatedAtAfter(LocalDateTime date);

    List<Article> findTop5ByOrderByCreatedAtDesc();
}
