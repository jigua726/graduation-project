package com.example.blogcommunity.controller;

import com.example.blogcommunity.dto.request.UserUpdateRequest;
import com.example.blogcommunity.dto.response.ApiResponse;
import com.example.blogcommunity.dto.response.ArticleResponse;
import com.example.blogcommunity.dto.response.PageResponse;
import com.example.blogcommunity.dto.response.UserCategoryStatsDTO;
import com.example.blogcommunity.dto.response.UserResponse;
import com.example.blogcommunity.service.UserService;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        UserResponse response = userService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long userId) {
        UserResponse response = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByUsername(@PathVariable String username) {
        UserResponse response = userService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Long userId,
                                                               @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateUser(userId, request);
        return ResponseEntity.ok(ApiResponse.success("更新成功", response));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }

    @PostMapping("/{targetUserId}/follow")
    public ResponseEntity<ApiResponse<Boolean>> followUser(@PathVariable Long targetUserId) {
        boolean success = userService.followUser(targetUserId);
        return ResponseEntity.ok(ApiResponse.success("关注成功", success));
    }

    @DeleteMapping("/{targetUserId}/follow")
    public ResponseEntity<ApiResponse<Boolean>> unfollowUser(@PathVariable Long targetUserId) {
        boolean success = userService.unfollowUser(targetUserId);
        return ResponseEntity.ok(ApiResponse.success("取消关注成功", success));
    }

    @GetMapping("/{targetUserId}/is-following")
    public ResponseEntity<ApiResponse<Boolean>> isFollowing(@PathVariable Long targetUserId) {
        Boolean isFollowing = userService.isFollowing(targetUserId);
        return ResponseEntity.ok(ApiResponse.success(isFollowing));
    }

    @GetMapping("/{userId}/category-stats")
    public ResponseEntity<ApiResponse<List<UserCategoryStatsDTO>>> getUserCategoryStats(@PathVariable Long userId) {
        List<UserCategoryStatsDTO> stats = userService.getUserCategoryStats(userId);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/{userId}/favorites")
    public ResponseEntity<ApiResponse<PageResponse<ArticleResponse>>> getUserFavorites(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<ArticleResponse> favorites = userService.getUserFavorites(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(favorites));
    }

    @GetMapping("/{userId}/followings")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getUserFollowings(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<UserResponse> followings = userService.getUserFollowings(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(followings));
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getUserFollowers(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<UserResponse> followers = userService.getUserFollowers(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(followers));
    }
}