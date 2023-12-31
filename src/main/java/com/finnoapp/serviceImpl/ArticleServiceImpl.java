package com.finnoapp.serviceImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.finnoapp.exception.CustomException;
import com.finnoapp.model.Article;
import com.finnoapp.repository.ArticleRepository;
import com.finnoapp.service.ArticleService;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	ArticleRepository articleRepository;

	@Override
	@Transactional
	public Article addArticle(Article article) {

		return this.articleRepository.save(article);
	}

	@Override
	public Article getArticleById(Long articleId) {
		return this.articleRepository.findById(articleId)
				.orElseThrow(() -> new CustomException("Article not found with ID: " + articleId));

	}

	@Override
	public List<Article> getAllArticles() {

		return this.articleRepository.getLatestArticles();
	}

	@Override
	public void deleteArticle(Long articleId) {

		this.articleRepository.deleteById(articleId);
	}

	@Override
	public Article updateArticle(Article article) {

		return this.articleRepository.save(article);
	}

	@Override
	public Page<Article> getLatestArticles(Pageable pageable) {
		return articleRepository.getLatestArticles(pageable);
		// Adjust the repository method based on your data access logic
	}

	@Override
	public List<Article> getUserArticles(Long userId) {

		return this.articleRepository.getLatestUserArticles(userId);
	}

}
