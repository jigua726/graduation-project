package com.example.blogcommunity.dto.response;

import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommentResponse {

    private Long id;

    private String content;

    private UserResponse author;

    private Long parentId;

    private List<CommentResponse> replies;

    private Integer likeCount;

    private Boolean isLiked;

    private Boolean isPinned;

    private LocalDateTime createdAt;
}