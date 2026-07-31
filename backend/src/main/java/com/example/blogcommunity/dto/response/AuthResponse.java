package com.example.blogcommunity.dto.response;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class AuthResponse {

    private String accessToken;

    private String tokenType = "Bearer";

    private UserResponse user;
}