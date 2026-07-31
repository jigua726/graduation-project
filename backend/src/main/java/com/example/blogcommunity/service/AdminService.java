package com.example.blogcommunity.service;

import com.example.blogcommunity.dto.response.ArticleResponse;
import com.example.blogcommunity.dto.response.AutoGenerateTagsResponse;
import com.example.blogcommunity.dto.response.PageResponse;
import com.example.blogcommunity.dto.response.StatsResponse;
import com.example.blogcommunity.dto.response.UserResponse;

public interface AdminService {

    StatsResponse getStats();

    PageResponse<UserResponse> getAllUsers(int page, int size);

    UserResponse updateUserRole(Long userId, String role);

    void deleteUser(Long userId);

    void disableUser(Long userId);

    void enableUser(Long userId);

    PageResponse<ArticleResponse> getAllArticles(int page, int size);

    void deleteArticle(Long articleId);

    void updateArticleStatus(Long articleId, Boolean published);

    AutoGenerateTagsResponse autoGenerateTags();
}