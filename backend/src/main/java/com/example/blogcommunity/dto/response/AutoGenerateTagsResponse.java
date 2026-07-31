package com.example.blogcommunity.dto.response;

public class AutoGenerateTagsResponse {

    private int updatedArticles;
    private int createdTags;

    public AutoGenerateTagsResponse(int updatedArticles, int createdTags) {
        this.updatedArticles = updatedArticles;
        this.createdTags = createdTags;
    }

    public int getUpdatedArticles() {
        return updatedArticles;
    }

    public void setUpdatedArticles(int updatedArticles) {
        this.updatedArticles = updatedArticles;
    }

    public int getCreatedTags() {
        return createdTags;
    }

    public void setCreatedTags(int createdTags) {
        this.createdTags = createdTags;
    }
}