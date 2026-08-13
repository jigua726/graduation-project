package com.example.blogcommunity.service.impl;

import com.example.blogcommunity.dto.response.ArticleResponse;
import com.example.blogcommunity.dto.response.AutoGenerateTagsResponse;
import com.example.blogcommunity.dto.response.CategoryResponse;
import com.example.blogcommunity.dto.response.PageResponse;
import com.example.blogcommunity.dto.response.StatsResponse;
import com.example.blogcommunity.dto.response.TagResponse;
import com.example.blogcommunity.dto.response.UserResponse;
import com.example.blogcommunity.entity.Article;
import com.example.blogcommunity.entity.Category;
import com.example.blogcommunity.entity.Tag;
import com.example.blogcommunity.entity.TagCategory;
import com.example.blogcommunity.entity.User;
import com.example.blogcommunity.exception.ResourceNotFoundException;
import com.example.blogcommunity.repository.*;
import com.example.blogcommunity.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final FavoriteRepository favoriteRepository;
    private final FollowRepository followRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final TagCategoryRepository tagCategoryRepository;

    public AdminServiceImpl(UserRepository userRepository, ArticleRepository articleRepository,
                           CommentRepository commentRepository, LikeRepository likeRepository,
                           FavoriteRepository favoriteRepository, FollowRepository followRepository,
                           CategoryRepository categoryRepository, TagRepository tagRepository,
                           TagCategoryRepository tagCategoryRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.favoriteRepository = favoriteRepository;
        this.followRepository = followRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.tagCategoryRepository = tagCategoryRepository;
    }

    @Override
    public StatsResponse getStats() {
        return StatsResponse.builder()
                .totalUsers(userRepository.count())
                .totalArticles(articleRepository.count())
                .totalComments(commentRepository.count())
                .totalLikes(likeRepository.count())
                .totalFavorites(favoriteRepository.count())
                .totalFollows(followRepository.count())
                .build();
    }

    @Override
    public PageResponse<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> userPage = userRepository.findAll(pageable);

        return PageResponse.<UserResponse>builder()
                .content(userPage.getContent().stream()
                        .map(this::toUserResponse)
                        .collect(Collectors.toList()))
                .page(userPage.getNumber())
                .size(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .first(userPage.isFirst())
                .last(userPage.isLast())
                .build();
    }

    @Override
    @Transactional
    public UserResponse updateUserRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setRole(role);
        User savedUser = userRepository.save(user);
        return toUserResponse(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public void disableUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void enableUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public PageResponse<ArticleResponse> getAllArticles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Article> articlePage = articleRepository.findAll(pageable);

        return PageResponse.<ArticleResponse>builder()
                .content(articlePage.getContent().stream()
                        .map(this::toArticleResponse)
                        .collect(Collectors.toList()))
                .page(articlePage.getNumber())
                .size(articlePage.getSize())
                .totalElements(articlePage.getTotalElements())
                .totalPages(articlePage.getTotalPages())
                .first(articlePage.isFirst())
                .last(articlePage.isLast())
                .build();
    }

    @Override
    @Transactional
    public void deleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", articleId));

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
    public void updateArticleStatus(Long articleId, Boolean published) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", articleId));
        article.setPublished(published);
        articleRepository.save(article);
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
                .isLiked(false)
                .isFavorited(false)
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .bio(user.getBio())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public AutoGenerateTagsResponse autoGenerateTags() {
        // 从数据库读取所有启用的标签分类
        List<TagCategory> tagCategoryList = tagCategoryRepository.findByEnabledTrueOrderBySortOrderAsc();

        // 转换为 Map<String, List<String>> 结构，兼容原有逻辑
        Map<String, List<String>> tagCategories = new HashMap<>();
        for (TagCategory tagCategory : tagCategoryList) {
            String[] tagArray = tagCategory.getTags().split(",");
            List<String> tagList = new ArrayList<>();
            for (String tag : tagArray) {
                String trimmed = tag.trim();
                if (!trimmed.isEmpty()) {
                    tagList.add(trimmed);
                }
            }
            if (!tagList.isEmpty()) {
                tagCategories.put(tagCategory.getCategoryName(), tagList);
            }
        }

        List<Article> articles = articleRepository.findAll();
        int updatedArticles = 0;
        int createdTags = 0;

        for (Article article : articles) {
            String content = article.getTitle() + " " + article.getContent();
            boolean articleUpdated = false;

            for (List<String> tags : tagCategories.values()) {
                boolean categoryMatched = false;
                for (String tag : tags) {
                    if (content.contains(tag)) {
                        categoryMatched = true;
                        break;
                    }
                }

                if (categoryMatched) {
                    for (int i = 0; i < Math.min(3, tags.size()); i++) {
                        String tagName = tags.get(i);
                        if (!article.getTags().stream().anyMatch(t -> t.getName().equals(tagName))) {
                            Tag tag = tagRepository.findByName(tagName).orElse(null);
                            if (tag == null) {
                                tag = new Tag();
                                tag.setName(tagName);
                                tag.setArticleCount(0);
                                tag = tagRepository.save(tag);
                                createdTags++;
                            }
                            article.getTags().add(tag);
                            tag.setArticleCount(tag.getArticleCount() + 1);
                            articleUpdated = true;
                        }
                    }
                }
            }

            if (articleUpdated) {
                articleRepository.save(article);
                updatedArticles++;
            }
        }

        return new AutoGenerateTagsResponse(updatedArticles, createdTags);
    }
}