package com.example.blogcommunity.dto.response;

import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ArticleResponse {

    private Long id;

    private String title;

    private String content;

    private String summary;

    private String coverImage;

    private UserResponse author;

    private CategoryResponse category;

    private List<TagResponse> tags;

    private Integer viewCount;

    private Integer likeCount;

    private Integer commentCount;

    private Boolean isLiked;

    private Boolean isFavorited;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}