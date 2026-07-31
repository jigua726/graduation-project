package com.example.blogcommunity.controller;

import com.example.blogcommunity.dto.request.ArticleCreateRequest;
import com.example.blogcommunity.dto.request.ArticleUpdateRequest;
import com.example.blogcommunity.dto.response.ApiResponse;
import com.example.blogcommunity.dto.response.ArticleResponse;
import com.example.blogcommunity.dto.response.PageResponse;
import com.example.blogcommunity.service.ArticleService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<PageResponse<ArticleResponse>>> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<ArticleResponse> response = articleService.getAllArticles(page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/public/{articleId}")
    public ResponseEntity<ApiResponse<ArticleResponse>> getArticleById(@PathVariable Long articleId) {
        ArticleResponse response = articleService.getArticleById(articleId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ApiResponse<ArticleResponse>> getArticleByIdWithUserStatus(@PathVariable Long articleId) {
        ArticleResponse response = articleService.getArticleById(articleId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/public/category/{categoryId}")
    public ResponseEntity<ApiResponse<PageResponse<ArticleResponse>>> getArticlesByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<ArticleResponse> response = articleService.getArticlesByCategory(categoryId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/public/tag/{tagName}")
    public ResponseEntity<ApiResponse<PageResponse<ArticleResponse>>> getArticlesByTag(
            @PathVariable String tagName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<ArticleResponse> response = articleService.getArticlesByTag(tagName, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/public/user/{userId}")
    public ResponseEntity<ApiResponse<PageResponse<ArticleResponse>>> getArticlesByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<ArticleResponse> response = articleService.getArticlesByUser(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/public/search")
    public ResponseEntity<ApiResponse<PageResponse<ArticleResponse>>> searchArticles(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<ArticleResponse> response = articleService.searchArticles(keyword, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ArticleResponse>> createArticle(@Valid @RequestBody ArticleCreateRequest request) {
        ArticleResponse response = articleService.createArticle(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("创建成功", response));
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<ApiResponse<ArticleResponse>> updateArticle(
            @PathVariable Long articleId,
            @RequestBody ArticleUpdateRequest request) {
        ArticleResponse response = articleService.updateArticle(articleId, request);
        return ResponseEntity.ok(ApiResponse.success("更新成功", response));
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<ApiResponse<Void>> deleteArticle(@PathVariable Long articleId) {
        articleService.deleteArticle(articleId);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }

    @PostMapping("/{articleId}/like")
    public ResponseEntity<ApiResponse<ArticleResponse>> likeArticle(@PathVariable Long articleId) {
        ArticleResponse response = articleService.likeArticle(articleId);
        return ResponseEntity.ok(ApiResponse.success("点赞成功", response));
    }

    @DeleteMapping("/{articleId}/like")
    public ResponseEntity<ApiResponse<ArticleResponse>> unlikeArticle(@PathVariable Long articleId) {
        ArticleResponse response = articleService.unlikeArticle(articleId);
        return ResponseEntity.ok(ApiResponse.success("取消点赞", response));
    }

    @PostMapping("/{articleId}/favorite")
    public ResponseEntity<ApiResponse<ArticleResponse>> favoriteArticle(@PathVariable Long articleId) {
        ArticleResponse response = articleService.favoriteArticle(articleId);
        return ResponseEntity.ok(ApiResponse.success("收藏成功", response));
    }

    @DeleteMapping("/{articleId}/favorite")
    public ResponseEntity<ApiResponse<ArticleResponse>> unfavoriteArticle(@PathVariable Long articleId) {
        ArticleResponse response = articleService.unfavoriteArticle(articleId);
        return ResponseEntity.ok(ApiResponse.success("取消收藏", response));
    }
}