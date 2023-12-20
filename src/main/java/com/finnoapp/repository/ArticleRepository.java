package com.finnoapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finnoapp.model.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

}
