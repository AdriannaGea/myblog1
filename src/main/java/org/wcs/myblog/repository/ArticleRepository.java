package org.wcs.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wcs.myblog.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
