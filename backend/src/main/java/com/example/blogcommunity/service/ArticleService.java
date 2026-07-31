package com.example.blogcommunity.service;

import com.example.blogcommunity.dto.request.ArticleCreateRequest;
import com.example.blogcommunity.dto.request.ArticleUpdateRequest;
import com.example.blogcommunity.dto.response.ArticleResponse;
import com.example.blogcommunity.dto.response.PageResponse;

public interface ArticleService {

    ArticleResponse createArticle(ArticleCreateRequest request);

    ArticleResponse getArticleById(Long articleId);

    PageResponse<ArticleResponse> getAllArticles(int page, int size);

    PageResponse<ArticleResponse> getArticlesByCategory(Long categoryId, int page, int size);

    PageResponse<ArticleResponse> getArticlesByTag(String tagName, int page, int size);

    PageResponse<ArticleResponse> getArticlesByUser(Long userId, int page, int size);

    PageResponse<ArticleResponse> searchArticles(String keyword, int page, int size);

    ArticleResponse updateArticle(Long articleId, ArticleUpdateRequest request);

    void deleteArticle(Long articleId);

    ArticleResponse likeArticle(Long articleId);

    ArticleResponse unlikeArticle(Long articleId);

    ArticleResponse favoriteArticle(Long articleId);

    ArticleResponse unfavoriteArticle(Long articleId);
}