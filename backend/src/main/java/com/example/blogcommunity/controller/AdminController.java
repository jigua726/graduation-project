package com.example.blogcommunity.controller;

import com.example.blogcommunity.dto.response.ApiResponse;
import com.example.blogcommunity.dto.response.ArticleResponse;
import com.example.blogcommunity.dto.response.AutoGenerateTagsResponse;
import com.example.blogcommunity.dto.response.PageResponse;
import com.example.blogcommunity.dto.response.StatsResponse;
import com.example.blogcommunity.dto.response.UserResponse;
import com.example.blogcommunity.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<StatsResponse>> getStats() {
        StatsResponse response = adminService.getStats();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<UserResponse> response = adminService.getAllUsers(page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserRole(
            @PathVariable Long userId,
            @RequestParam String role) {
        UserResponse response = adminService.updateUserRole(userId, role);
        return ResponseEntity.ok(ApiResponse.success("角色更新成功", response));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }

    @PostMapping("/users/{userId}/disable")
    public ResponseEntity<ApiResponse<Void>> disableUser(@PathVariable Long userId) {
        adminService.disableUser(userId);
        return ResponseEntity.ok(ApiResponse.success("禁用成功", null));
    }

    @PostMapping("/users/{userId}/enable")
    public ResponseEntity<ApiResponse<Void>> enableUser(@PathVariable Long userId) {
        adminService.enableUser(userId);
        return ResponseEntity.ok(ApiResponse.success("启用成功", null));
    }

    @GetMapping("/articles")
    public ResponseEntity<ApiResponse<PageResponse<ArticleResponse>>> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<ArticleResponse> response = adminService.getAllArticles(page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<ApiResponse<Void>> deleteArticle(@PathVariable Long articleId) {
        adminService.deleteArticle(articleId);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }

    @PutMapping("/articles/{articleId}/status")
    public ResponseEntity<ApiResponse<Void>> updateArticleStatus(
            @PathVariable Long articleId,
            @RequestParam Boolean published) {
        adminService.updateArticleStatus(articleId, published);
        return ResponseEntity.ok(ApiResponse.success("状态更新成功", null));
    }

    @PostMapping("/articles/generate-tags")
    public ResponseEntity<ApiResponse<AutoGenerateTagsResponse>> autoGenerateTags() {
        AutoGenerateTagsResponse response = adminService.autoGenerateTags();
        return ResponseEntity.ok(ApiResponse.success("标签生成成功", response));
    }
}