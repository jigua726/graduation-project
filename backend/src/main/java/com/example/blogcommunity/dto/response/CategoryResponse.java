package com.example.blogcommunity.dto.response;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class CategoryResponse {

    private Long id;

    private String name;

    private String description;

    private Integer articleCount;
}