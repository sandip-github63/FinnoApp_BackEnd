package com.finnoapp.service;

import java.util.List;

import com.finnoapp.model.Article;

public interface ArticleService {

	public Article addArticle(Article article);

	public Article getArticleById(Long articleId);

	public List<Article> getAllArticles();

	public void deleteArticle(Long articleId);

	public Article updateArticle(Article article);

}
