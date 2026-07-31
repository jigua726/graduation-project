package com.example.blogcommunity.dto.response;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class TagResponse {

    private Long id;

    private String name;

    private Integer articleCount;
}