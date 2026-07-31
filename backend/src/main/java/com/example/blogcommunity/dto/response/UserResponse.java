package com.example.blogcommunity.dto.response;

import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private String email;

    private String nickname;

    private String avatar;

    private String bio;

    private String role;

    private Integer articleCount;

    private Integer followerCount;

    private Integer followingCount;

    private Boolean isFollowing;

    private LocalDateTime createdAt;
}