package com.example.blogcommunity.service.impl;

import com.example.blogcommunity.dto.request.ArticleCreateRequest;
import com.example.blogcommunity.dto.request.ArticleUpdateRequest;
import com.example.blogcommunity.dto.response.*;
import com.example.blogcommunity.entity.*;
import com.example.blogcommunity.exception.BusinessException;
import com.example.blogcommunity.exception.ResourceNotFoundException;
import com.example.blogcommunity.repository.*;
import com.example.blogcommunity.service.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final LikeRepository likeRepository;
    private final FavoriteRepository favoriteRepository;
    private final CommentRepository commentRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository, UserRepository userRepository,
                             CategoryRepository categoryRepository, TagRepository tagRepository,
                             LikeRepository likeRepository, FavoriteRepository favoriteRepository,
                             CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.likeRepository = likeRepository;
        this.favoriteRepository = favoriteRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public ArticleResponse createArticle(ArticleCreateRequest request) {
        User user = getCurrentUser();

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
        }

        List<Tag> tags = new ArrayList<>();
        if (request.getTags() != null) {
            for (String tagName : request.getTags()) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> Tag.builder().name(tagName).articleCount(0).build());
                tags.add(tag);
            }
        }

        String summary = request.getSummary();
        if (summary == null || summary.isEmpty()) {
            summary = request.getContent().length() > 200 ? request.getContent().substring(0, 200) + "..." : request.getContent();
        }

        Article article = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .summary(summary)
                .coverImage(request.getCoverImage())
                .user(user)
                .category(category)
                .tags(tags)
                .published(true)
                .build();

        Article savedArticle = articleRepository.save(article);

        if (category != null) {
            categoryRepository.incrementArticleCount(category.getId());
        }

        for (Tag tag : tags) {
            tagRepository.incrementArticleCount(tag.getId());
        }

        return toArticleResponse(savedArticle);
    }

    @Override
    @Transactional
    public ArticleResponse getArticleById(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", articleId));

        articleRepository.incrementViewCount(articleId);

        return toArticleResponse(article);
    }

    @Override
    public PageResponse<ArticleResponse> getAllArticles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Article> articlePage = articleRepository.findByPublishedTrue(pageable);
        return toPageResponse(articlePage);
    }

    @Override
    public PageResponse<ArticleResponse> getArticlesByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Article> articlePage = articleRepository.findByCategoryIdAndPublishedTrue(categoryId, pageable);
        return toPageResponse(articlePage);
    }

    @Override
    public PageResponse<ArticleResponse> getArticlesByTag(String tagName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Article> articlePage = articleRepository.findByTagsNameAndPublishedTrue(tagName, pageable);
        return toPageResponse(articlePage);
    }

    @Override
    public PageResponse<ArticleResponse> getArticlesByUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Article> articlePage = articleRepository.findByUserIdAndPublishedTrue(userId, pageable);
        return toPageResponse(articlePage);
    }

    @Override
    public PageResponse<ArticleResponse> searchArticles(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Article> articlePage = articleRepository.searchByKeyword(keyword, pageable);
        return toPageResponse(articlePage);
    }

    @Override
    @Transactional
    public ArticleResponse updateArticle(Long articleId, ArticleUpdateRequest request) {
        User currentUser = getCurrentUser();
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", articleId));

        if (!article.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().equals("ADMIN")) {
            throw new BusinessException("无权修改此文章");
        }

        if (request.getTitle() != null) {
            article.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            article.setContent(request.getContent());
            if (request.getSummary() == null) {
                article.setSummary(request.getContent().length() > 200 ? request.getContent().substring(0, 200) + "..." : request.getContent());
            }
        }
        if (request.getSummary() != null) {
            article.setSummary(request.getSummary());
        }
        if (request.getCategoryId() != null) {
            Category oldCategory = article.getCategory();
            Category newCategory = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            
            if (oldCategory != null && !oldCategory.getId().equals(newCategory.getId())) {
                categoryRepository.decrementArticleCount(oldCategory.getId());
            }
            article.setCategory(newCategory);
            categoryRepository.incrementArticleCount(newCategory.getId());
        }
        if (request.getPublished() != null) {
            article.setPublished(request.getPublished());
        }
        if (request.getCoverImage() != null) {
            article.setCoverImage(request.getCoverImage());
        }

        Article savedArticle = articleRepository.save(article);
        return toArticleResponse(savedArticle);
    }

    @Override
    @Transactional
    public void deleteArticle(Long articleId) {
        User currentUser = getCurrentUser();
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", articleId));

        if (!article.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().equals("ADMIN")) {
            throw new BusinessException("无权删除此文章");
        }

        if (article.getCategory() != null) {
            categoryRepository.decrementArticleCount(article.getCategory().getId());
        }

        for (Tag tag : article.getTags()) {
            tagRepository.decrementArticleCount(tag.getId());
        }

        likeRepository.deleteByArticleId(articleId);
        favoriteRepository.deleteByArticleId(articleId);
        commentRepository.deleteByArticleId(articleId);

        articleRepository.delete(article);
    }

    @Override
    @Transactional
    public ArticleResponse likeArticle(Long articleId) {
        User user = getCurrentUser();
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", articleId));

        if (likeRepository.existsByUserIdAndArticleId(user.getId(), articleId)) {
            throw new BusinessException("已经点赞此文章");
        }

        Like like = Like.builder()
                .user(user)
                .article(article)
                .build();

        likeRepository.save(like);
        articleRepository.incrementLikeCount(articleId);

        return toArticleResponse(articleRepository.findById(articleId).get());
    }

    @Override
    @Transactional
    public ArticleResponse unlikeArticle(Long articleId) {
        User user = getCurrentUser();

        Like like = likeRepository.findByUserIdAndArticleId(user.getId(), articleId)
                .orElseThrow(() -> new BusinessException("未点赞此文章"));

        likeRepository.delete(like);
        articleRepository.decrementLikeCount(articleId);

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", articleId));
        return toArticleResponse(article);
    }

    @Override
    @Transactional
    public ArticleResponse favoriteArticle(Long articleId) {
        User user = getCurrentUser();
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", articleId));

        if (favoriteRepository.existsByUserIdAndArticleId(user.getId(), articleId)) {
            throw new BusinessException("已经收藏此文章");
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .article(article)
                .build();

        favoriteRepository.save(favorite);
        return toArticleResponse(article);
    }

    @Override
    @Transactional
    public ArticleResponse unfavoriteArticle(Long articleId) {
        User user = getCurrentUser();

        Favorite favorite = favoriteRepository.findByUserIdAndArticleId(user.getId(), articleId)
                .orElseThrow(() -> new BusinessException("未收藏此文章"));

        favoriteRepository.delete(favorite);

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", articleId));
        return toArticleResponse(article);
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

    private ArticleResponse toArticleResponse(Article article) {
        UserResponse author = UserResponse.builder()
                .id(article.getUser().getId())
                .username(article.getUser().getUsername())
                .nickname(article.getUser().getNickname())
                .avatar(article.getUser().getAvatar())
                .build();

        CategoryResponse category = null;
        if (article.getCategory() != null) {
            category = CategoryResponse.builder()
                    .id(article.getCategory().getId())
                    .name(article.getCategory().getName())
                    .description(article.getCategory().getDescription())
                    .articleCount(article.getCategory().getArticleCount())
                    .build();
        }

        List<TagResponse> tags = article.getTags().stream()
                .map(tag -> TagResponse.builder()
                        .id(tag.getId())
                        .name(tag.getName())
                        .articleCount(tag.getArticleCount())
                        .build())
                .collect(Collectors.toList());

        User currentUser = null;
        try {
            currentUser = getCurrentUser();
        } catch (Exception e) {
            // Not logged in
        }

        boolean isLiked = currentUser != null && likeRepository.existsByUserIdAndArticleId(currentUser.getId(), article.getId());
        boolean isFavorited = currentUser != null && favoriteRepository.existsByUserIdAndArticleId(currentUser.getId(), article.getId());

        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .summary(article.getSummary())
                .coverImage(article.getCoverImage())
                .author(author)
                .category(category)
                .tags(tags)
                .viewCount(article.getViewCount())
                .likeCount(article.getLikeCount())
                .commentCount(article.getCommentCount())
                .isLiked(isLiked)
                .isFavorited(isFavorited)
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

    private PageResponse<ArticleResponse> toPageResponse(Page<Article> articlePage) {
        List<ArticleResponse> content = articlePage.getContent().stream()
                .map(this::toArticleResponse)
                .collect(Collectors.toList());

        return PageResponse.<ArticleResponse>builder()
                .content(content)
                .page(articlePage.getNumber())
                .size(articlePage.getSize())
                .totalElements(articlePage.getTotalElements())
                .totalPages(articlePage.getTotalPages())
                .first(articlePage.isFirst())
                .last(articlePage.isLast())
                .build();
    }
}