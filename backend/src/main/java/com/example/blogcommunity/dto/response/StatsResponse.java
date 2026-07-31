package com.example.blogcommunity.dto.response;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class StatsResponse {

    private Long totalUsers;

    private Long totalArticles;

    private Long totalComments;

    private Long totalLikes;

    private Long totalFavorites;

    private Long totalFollows;
}