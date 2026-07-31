package com.example.blogcommunity.dto.response;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class UserCategoryStatsDTO {
    private Long categoryId;
    private String categoryName;
    private String categoryDescription;
    private Integer articleCount;
}