package com.finnoapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.finnoapp.model.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

	@Query("SELECT a FROM Article a ORDER BY a.publicationDate DESC")
	Page<Article> getLatestArticles(Pageable pageable);

}
