package com.example.blogcommunity.service.impl;

import com.example.blogcommunity.dto.request.CommentRequest;
import com.example.blogcommunity.dto.response.CommentResponse;
import com.example.blogcommunity.dto.response.PageResponse;
import com.example.blogcommunity.dto.response.UserResponse;
import com.example.blogcommunity.entity.Article;
import com.example.blogcommunity.entity.Comment;
import com.example.blogcommunity.entity.User;
import com.example.blogcommunity.exception.BusinessException;
import com.example.blogcommunity.exception.ResourceNotFoundException;
import com.example.blogcommunity.repository.ArticleRepository;
import com.example.blogcommunity.repository.CommentRepository;
import com.example.blogcommunity.repository.UserRepository;
import com.example.blogcommunity.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, ArticleRepository articleRepository,
                             UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CommentResponse createComment(CommentRequest request) {
        User user = getCurrentUser();
        Article article = articleRepository.findById(request.getArticleId())
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", request.getArticleId()));

        Comment parentComment = null;
        if (request.getParentId() != null) {
            parentComment = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", request.getParentId()));
        }

        Comment comment = Comment.builder()
                .content(request.getContent())
                .article(article)
                .user(user)
                .parent(parentComment)
                .deleted(false)
                .pinned(false)
                .build();

        Comment savedComment = commentRepository.save(comment);
        
        long actualCount = commentRepository.countByArticleIdAndDeletedFalse(request.getArticleId());
        article.setCommentCount((int) actualCount);
        articleRepository.save(article);

        return toCommentResponse(savedComment);
    }

    @Override
    public PageResponse<CommentResponse> getCommentsByArticle(Long articleId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findByArticleIdAndParentIsNullAndDeletedFalseOrderByPinnedDescCreatedAtDesc(articleId, pageable);
        
        List<CommentResponse> content = commentPage.getContent().stream()
                .map(this::toCommentResponseWithReplies)
                .collect(Collectors.toList());

        return PageResponse.<CommentResponse>builder()
                .content(content)
                .page(commentPage.getNumber())
                .size(commentPage.getSize())
                .totalElements(commentPage.getTotalElements())
                .totalPages(commentPage.getTotalPages())
                .first(commentPage.isFirst())
                .last(commentPage.isLast())
                .build();
    }

    @Override
    @Transactional
    public CommentResponse updateComment(Long commentId, String content) {
        User currentUser = getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if (!comment.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().equals("ADMIN")) {
            throw new BusinessException("无权修改此评论");
        }

        comment.setContent(content);
        Comment savedComment = commentRepository.save(comment);
        return toCommentResponse(savedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        User currentUser = getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        boolean isCommentAuthor = comment.getUser().getId().equals(currentUser.getId());
        boolean isArticleAuthor = comment.getArticle().getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole().equals("ADMIN");
        
        if (!isCommentAuthor && !isArticleAuthor && !isAdmin) {
            throw new BusinessException("无权删除此评论");
        }

        commentRepository.softDelete(commentId);
        
        long actualCount = commentRepository.countByArticleIdAndDeletedFalse(comment.getArticle().getId());
        comment.getArticle().setCommentCount((int) actualCount);
        articleRepository.save(comment.getArticle());
    }

    @Override
    @Transactional
    public CommentResponse likeComment(Long commentId) {
        User user = getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        commentRepository.incrementLikeCount(commentId);
        comment.setLikeCount(comment.getLikeCount() + 1);
        return toCommentResponse(comment);
    }

    @Override
    @Transactional
    public CommentResponse unlikeComment(Long commentId) {
        User user = getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        commentRepository.decrementLikeCount(commentId);
        comment.setLikeCount(Math.max(0, comment.getLikeCount() - 1));
        return toCommentResponse(comment);
    }

    @Override
    @Transactional
    public CommentResponse togglePinComment(Long commentId) {
        User currentUser = getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        boolean isArticleAuthor = comment.getArticle().getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole().equals("ADMIN");
        
        if (!isArticleAuthor && !isAdmin) {
            throw new BusinessException("无权置顶此评论");
        }

        boolean newPinnedStatus = !comment.getPinned();
        commentRepository.updatePinned(commentId, newPinnedStatus);
        comment.setPinned(newPinnedStatus);
        
        return toCommentResponse(comment);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new BusinessException("用户未登录");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", userDetails.getUsername()));
    }

    private CommentResponse toCommentResponse(Comment comment) {
        UserResponse author = UserResponse.builder()
                .id(comment.getUser().getId())
                .username(comment.getUser().getUsername())
                .nickname(comment.getUser().getNickname())
                .avatar(comment.getUser().getAvatar())
                .build();

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .author(author)
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .likeCount(comment.getLikeCount())
                .isLiked(false)
                .isPinned(comment.getPinned())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    private CommentResponse toCommentResponseWithReplies(Comment comment) {
        CommentResponse response = toCommentResponse(comment);
        
        List<Comment> replies = commentRepository.findByParentIdAndDeletedFalse(comment.getId());
        List<CommentResponse> replyResponses = replies.stream()
                .map(this::toCommentResponse)
                .collect(Collectors.toList());
        
        response.setReplies(replyResponses);
        return response;
    }
}