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

    public AdminServiceImpl(UserRepository userRepository, ArticleRepository articleRepository,
                           CommentRepository commentRepository, LikeRepository likeRepository,
                           FavoriteRepository favoriteRepository, FollowRepository followRepository,
                           CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.favoriteRepository = favoriteRepository;
        this.followRepository = followRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
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
        Map<String, List<String>> tagCategories = new HashMap<>();
        
        tagCategories.put("技术", new ArrayList<>(java.util.Arrays.asList("技术", "编程", "开发", "代码", "项目", "教程")));
        tagCategories.put("前端", new ArrayList<>(java.util.Arrays.asList("前端", "Vue", "React", "JavaScript", "HTML", "CSS", "TypeScript", "Node.js")));
        tagCategories.put("后端", new ArrayList<>(java.util.Arrays.asList("后端", "Java", "Python", "Spring", "数据库", "MySQL", "Redis", "架构")));
        tagCategories.put("AI", new ArrayList<>(java.util.Arrays.asList("AI", "人工智能", "机器学习", "深度学习", "大数据", "云计算")));
        tagCategories.put("生活", new ArrayList<>(java.util.Arrays.asList("生活", "日常", "生活感悟", "生活方式", "生活随笔", "生活分享")));
        tagCategories.put("读书", new ArrayList<>(java.util.Arrays.asList("读书", "阅读", "书单", "书评", "读书笔记", "文学", "小说", "散文")));
        tagCategories.put("旅行", new ArrayList<>(java.util.Arrays.asList("旅行", "旅游", "游记", "风景", "自然", "人文", "古镇", "海岛")));
        tagCategories.put("美食", new ArrayList<>(java.util.Arrays.asList("美食", "烹饪", "菜谱", "家常菜", "烘焙", "甜点", "食材")));
        tagCategories.put("摄影", new ArrayList<>(java.util.Arrays.asList("摄影", "拍照", "相机", "镜头", "构图", "光影", "色彩")));
        tagCategories.put("健康", new ArrayList<>(java.util.Arrays.asList("健康", "健身", "运动", "跑步", "瑜伽", "冥想", "养生")));
        tagCategories.put("音乐", new ArrayList<>(java.util.Arrays.asList("音乐", "听歌", "唱歌", "乐器", "吉他", "钢琴", "歌单")));
        tagCategories.put("科技", new ArrayList<>(java.util.Arrays.asList("科技", "数码", "手机", "电脑", "互联网", "创新")));
        tagCategories.put("职场", new ArrayList<>(java.util.Arrays.asList("职场", "工作", "职业发展", "面试", "简历", "团队管理")));

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