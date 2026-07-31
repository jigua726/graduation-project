package com.example.blogcommunity.service;

import com.example.blogcommunity.dto.request.CommentRequest;
import com.example.blogcommunity.dto.response.CommentResponse;
import com.example.blogcommunity.dto.response.PageResponse;

public interface CommentService {

    CommentResponse createComment(CommentRequest request);

    PageResponse<CommentResponse> getCommentsByArticle(Long articleId, int page, int size);

    CommentResponse updateComment(Long commentId, String content);

    void deleteComment(Long commentId);

    CommentResponse likeComment(Long commentId);

    CommentResponse unlikeComment(Long commentId);

    CommentResponse togglePinComment(Long commentId);
}