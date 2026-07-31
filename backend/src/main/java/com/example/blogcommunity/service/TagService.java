package com.example.blogcommunity.service;

import com.example.blogcommunity.dto.response.TagResponse;

import java.util.List;

public interface TagService {

    List<TagResponse> getAllTags();

    TagResponse getTagById(Long tagId);

    TagResponse getTagByName(String name);

    TagResponse createTag(String name);

    TagResponse updateTag(Long tagId, String name);

    void deleteTag(Long tagId);
}