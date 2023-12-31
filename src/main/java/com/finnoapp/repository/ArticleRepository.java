package com.finnoapp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finnoapp.model.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

	@Query("SELECT a FROM Article a ORDER BY a.publicationDate DESC")
	Page<Article> getLatestArticles(Pageable pageable);

	@Query("SELECT a FROM Article a WHERE a.user.userId = :userId ORDER BY a.publicationDate DESC")
	List<Article> getLatestUserArticles(@Param("userId") Long userId);

	@Query("SELECT a FROM Article a ORDER BY a.publicationDate DESC")
	List<Article> getLatestArticles();

}
