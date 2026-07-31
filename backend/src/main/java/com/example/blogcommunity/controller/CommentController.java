package com.example.blogcommunity.controller;

import com.example.blogcommunity.dto.request.CommentRequest;
import com.example.blogcommunity.dto.response.ApiResponse;
import com.example.blogcommunity.dto.response.CommentResponse;
import com.example.blogcommunity.dto.response.PageResponse;
import com.example.blogcommunity.service.CommentService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(@Valid @RequestBody CommentRequest request) {
        CommentResponse response = commentService.createComment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("评论成功", response));
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<ApiResponse<PageResponse<CommentResponse>>> getCommentsByArticle(
            @PathVariable Long articleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<CommentResponse> response = commentService.getCommentsByArticle(articleId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long commentId,
            @RequestBody String content) {
        CommentResponse response = commentService.updateComment(commentId, content);
        return ResponseEntity.ok(ApiResponse.success("更新成功", response));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<ApiResponse<CommentResponse>> likeComment(@PathVariable Long commentId) {
        CommentResponse response = commentService.likeComment(commentId);
        return ResponseEntity.ok(ApiResponse.success("点赞成功", response));
    }

    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<ApiResponse<CommentResponse>> unlikeComment(@PathVariable Long commentId) {
        CommentResponse response = commentService.unlikeComment(commentId);
        return ResponseEntity.ok(ApiResponse.success("取消点赞", response));
    }

    @PostMapping("/{commentId}/pin")
    public ResponseEntity<ApiResponse<CommentResponse>> togglePinComment(@PathVariable Long commentId) {
        CommentResponse response = commentService.togglePinComment(commentId);
        String message = response.getIsPinned() ? "置顶成功" : "取消置顶成功";
        return ResponseEntity.ok(ApiResponse.success(message, response));
    }
}