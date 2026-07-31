package com.example.blogcommunity.controller;

import com.example.blogcommunity.dto.response.ApiResponse;
import com.example.blogcommunity.dto.response.TagResponse;
import com.example.blogcommunity.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TagResponse>>> getAllTags() {
        List<TagResponse> response = tagService.getAllTags();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<ApiResponse<TagResponse>> getTagById(@PathVariable Long tagId) {
        TagResponse response = tagService.getTagById(tagId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<TagResponse>> getTagByName(@PathVariable String name) {
        TagResponse response = tagService.getTagByName(name);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TagResponse>> createTag(@RequestBody java.util.Map<String, String> body) {
        String name = body.get("name");
        TagResponse response = tagService.createTag(name);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("创建成功", response));
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<ApiResponse<TagResponse>> updateTag(
            @PathVariable Long tagId,
            @RequestBody java.util.Map<String, String> body) {
        String name = body.get("name");
        TagResponse response = tagService.updateTag(tagId, name);
        return ResponseEntity.ok(ApiResponse.success("更新成功", response));
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<ApiResponse<Void>> deleteTag(@PathVariable Long tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }
}