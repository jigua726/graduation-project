package com.example.blogcommunity.service;

import com.example.blogcommunity.dto.request.LoginRequest;
import com.example.blogcommunity.dto.request.RegisterRequest;
import com.example.blogcommunity.dto.request.UserUpdateRequest;
import com.example.blogcommunity.dto.response.ArticleResponse;
import com.example.blogcommunity.dto.response.AuthResponse;
import com.example.blogcommunity.dto.response.PageResponse;
import com.example.blogcommunity.dto.response.UserCategoryStatsDTO;
import com.example.blogcommunity.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserResponse getCurrentUser();

    UserResponse getUserById(Long id);

    UserResponse getUserByUsername(String username);

    UserResponse updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long userId);

    boolean followUser(Long targetUserId);

    boolean unfollowUser(Long targetUserId);

    Boolean isFollowing(Long targetUserId);

    List<UserCategoryStatsDTO> getUserCategoryStats(Long userId);

    PageResponse<ArticleResponse> getUserFavorites(Long userId, int page, int size);

    PageResponse<UserResponse> getUserFollowings(Long userId, int page, int size);

    PageResponse<UserResponse> getUserFollowers(Long userId, int page, int size);

    void requestPasswordReset(String email);

    void resetPassword(String token, String newPassword);

    boolean validateResetToken(String token);
}