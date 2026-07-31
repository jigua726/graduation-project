package com.example.blogcommunity.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ArticleCreateRequest {

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    private String summary;

    private Long categoryId;

    private List<String> tags;

    private String coverImage;
}