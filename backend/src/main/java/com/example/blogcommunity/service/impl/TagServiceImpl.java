package com.example.blogcommunity.service.impl;

import com.example.blogcommunity.dto.response.TagResponse;
import com.example.blogcommunity.entity.Tag;
import com.example.blogcommunity.exception.BusinessException;
import com.example.blogcommunity.exception.ResourceNotFoundException;
import com.example.blogcommunity.repository.TagRepository;
import com.example.blogcommunity.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll().stream()
                .map(this::toTagResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TagResponse getTagById(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", tagId));
        return toTagResponse(tag);
    }

    @Override
    public TagResponse getTagByName(String name) {
        Tag tag = tagRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "name", name));
        return toTagResponse(tag);
    }

    @Override
    @Transactional
    public TagResponse createTag(String name) {
        if (tagRepository.existsByName(name)) {
            throw new BusinessException("标签名称已存在");
        }

        Tag tag = Tag.builder()
                .name(name)
                .articleCount(0)
                .build();

        Tag savedTag = tagRepository.save(tag);
        return toTagResponse(savedTag);
    }

    @Override
    @Transactional
    public TagResponse updateTag(Long tagId, String name) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", tagId));

        if (tagRepository.existsByName(name) && !tag.getName().equals(name)) {
            throw new BusinessException("标签名称已存在");
        }

        tag.setName(name);
        Tag savedTag = tagRepository.save(tag);
        return toTagResponse(savedTag);
    }

    @Override
    @Transactional
    public void deleteTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", tagId));

        if (tag.getArticleCount() > 0) {
            throw new BusinessException("该标签下存在文章，无法删除");
        }

        tagRepository.delete(tag);
    }

    private TagResponse toTagResponse(Tag tag) {
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .articleCount(tag.getArticleCount())
                .build();
    }
}