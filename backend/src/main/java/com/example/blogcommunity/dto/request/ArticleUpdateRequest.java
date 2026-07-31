package com.example.blogcommunity.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ArticleUpdateRequest {

    private String title;

    private String content;

    private String summary;

    private Long categoryId;

    private List<String> tags;

    private Boolean published;

    private String coverImage;
}