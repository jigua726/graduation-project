package com.example.blogcommunity.service.impl;

import com.example.blogcommunity.dto.request.LoginRequest;
import com.example.blogcommunity.dto.request.RegisterRequest;
import com.example.blogcommunity.dto.request.UserUpdateRequest;
import com.example.blogcommunity.dto.response.ArticleResponse;
import com.example.blogcommunity.dto.response.AuthResponse;
import com.example.blogcommunity.dto.response.PageResponse;
import com.example.blogcommunity.dto.response.UserCategoryStatsDTO;
import com.example.blogcommunity.dto.response.UserResponse;
import com.example.blogcommunity.entity.Article;
import com.example.blogcommunity.entity.Category;
import com.example.blogcommunity.entity.Favorite;
import com.example.blogcommunity.entity.Follow;
import com.example.blogcommunity.entity.PasswordResetToken;
import com.example.blogcommunity.entity.User;
import com.example.blogcommunity.exception.BusinessException;
import com.example.blogcommunity.exception.ResourceNotFoundException;
import com.example.blogcommunity.repository.ArticleRepository;
import com.example.blogcommunity.repository.CategoryRepository;
import com.example.blogcommunity.repository.FavoriteRepository;
import com.example.blogcommunity.repository.FollowRepository;
import com.example.blogcommunity.repository.PasswordResetTokenRepository;
import com.example.blogcommunity.repository.UserRepository;
import com.example.blogcommunity.security.JwtUtil;
import com.example.blogcommunity.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final FavoriteRepository favoriteRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository, FollowRepository followRepository,
                          ArticleRepository articleRepository, CategoryRepository categoryRepository,
                          FavoriteRepository favoriteRepository,
                          PasswordResetTokenRepository passwordResetTokenRepository,
                          PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                          AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.favoriteRepository = favoriteRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("邮箱已被注册");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .nickname(request.getNickname() != null ? request.getNickname() : request.getUsername())
                .avatar("https://api.dicebear.com/7.x/avataaars/svg?seed=" + request.getUsername())
                .bio("这个人很懒，还没有自我介绍")
                .role("USER")
                .enabled(true)
                .articleCount(0)
                .followerCount(0)
                .followingCount(0)
                .build();

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser.getUsername());

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .user(toUserResponse(savedUser))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.generateToken(request.getUsername());

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "username", request.getUsername()));

            return AuthResponse.builder()
                    .accessToken(token)
                    .tokenType("Bearer")
                    .user(toUserResponse(user))
                    .build();
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            throw new BusinessException("用户名或密码错误");
        }
    }

    @Override
    public UserResponse getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", userDetails.getUsername()));
        return toUserResponse(user);
    }

    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return toUserResponseWithFollowingStatus(user);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return toUserResponseWithFollowingStatus(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
        User currentUser = getCurrentUserEntity();
        
        if (!currentUser.getId().equals(userId) && !currentUser.getRole().equals("ADMIN")) {
            throw new BusinessException("无权修改此用户信息");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new BusinessException("邮箱已被使用");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        User savedUser = userRepository.save(user);
        return toUserResponse(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User currentUser = getCurrentUserEntity();
        
        if (!currentUser.getId().equals(userId) && !currentUser.getRole().equals("ADMIN")) {
            throw new BusinessException("无权删除此用户");
        }

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public boolean followUser(Long targetUserId) {
        User currentUser = getCurrentUserEntity();
        
        if (currentUser.getId().equals(targetUserId)) {
            throw new BusinessException("不能关注自己");
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", targetUserId));

        if (followRepository.existsByFollowerIdAndFollowingId(currentUser.getId(), targetUserId)) {
            throw new BusinessException("已经关注此用户");
        }

        Follow follow = Follow.builder()
                .follower(currentUser)
                .following(targetUser)
                .build();

        followRepository.save(follow);
        return true;
    }

    @Override
    @Transactional
    public boolean unfollowUser(Long targetUserId) {
        User currentUser = getCurrentUserEntity();

        Follow follow = followRepository.findByFollowerIdAndFollowingId(currentUser.getId(), targetUserId)
                .orElseThrow(() -> new BusinessException("未关注此用户"));

        followRepository.delete(follow);
        return true;
    }

    @Override
    public Boolean isFollowing(Long targetUserId) {
        User currentUser = getCurrentUserEntity();
        return followRepository.existsByFollowerIdAndFollowingId(currentUser.getId(), targetUserId);
    }

    private User getCurrentUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new BusinessException("用户未登录");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", userDetails.getUsername()));
    }

    private UserResponse toUserResponse(User user) {
        long articleCount = articleRepository.countByUserId(user.getId());
        long followerCount = followRepository.countByFollowingId(user.getId());
        long followingCount = followRepository.countByFollowerId(user.getId());

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .bio(user.getBio())
                .role(user.getRole())
                .articleCount((int) articleCount)
                .followerCount((int) followerCount)
                .followingCount((int) followingCount)
                .createdAt(user.getCreatedAt())
                .build();
    }

    private UserResponse toUserResponseWithFollowingStatus(User user) {
        UserResponse response = toUserResponse(user);
        try {
            User currentUser = getCurrentUserEntity();
            response.setIsFollowing(followRepository.existsByFollowerIdAndFollowingId(currentUser.getId(), user.getId()));
        } catch (Exception e) {
            response.setIsFollowing(false);
        }
        return response;
    }

    @Override
    public List<UserCategoryStatsDTO> getUserCategoryStats(Long userId) {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category -> {
            long articleCount = articleRepository.countByUserIdAndCategoryId(userId, category.getId());
            return UserCategoryStatsDTO.builder()
                    .categoryId(category.getId())
                    .categoryName(category.getName())
                    .categoryDescription(category.getDescription())
                    .articleCount((int) articleCount)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public PageResponse<ArticleResponse> getUserFavorites(Long userId, int page, int size) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Favorite> favoritePage = favoriteRepository.findByUserId(userId, pageable);

        List<ArticleResponse> content = favoritePage.getContent().stream()
                .map(favorite -> {
                    Article article = favorite.getArticle();
                    return ArticleResponse.builder()
                            .id(article.getId())
                            .title(article.getTitle())
                            .summary(article.getSummary())
                            .coverImage(article.getCoverImage())
                            .viewCount(article.getViewCount())
                            .likeCount(article.getLikeCount())
                            .commentCount(article.getCommentCount())
                            .isFavorited(true)
                            .createdAt(article.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        return PageResponse.<ArticleResponse>builder()
                .content(content)
                .page(favoritePage.getNumber())
                .size(favoritePage.getSize())
                .totalElements(favoritePage.getTotalElements())
                .totalPages(favoritePage.getTotalPages())
                .first(favoritePage.isFirst())
                .last(favoritePage.isLast())
                .build();
    }

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("该邮箱未注册"));

        passwordResetTokenRepository.deleteByUserId(user.getId());

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(java.time.LocalDateTime.now().plusHours(1))
                .used(false)
                .build();

        passwordResetTokenRepository.save(resetToken);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException("无效的重置链接"));

        if (resetToken.getUsed()) {
            throw new BusinessException("该链接已被使用");
        }

        if (resetToken.getExpiryDate().isBefore(java.time.LocalDateTime.now())) {
            throw new BusinessException("链接已过期，请重新申请");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }

    @Override
    public boolean validateResetToken(String token) {
        return passwordResetTokenRepository.findByToken(token)
                .map(t -> !t.getUsed() && t.getExpiryDate().isAfter(java.time.LocalDateTime.now()))
                .orElse(false);
    }

    @Override
    public PageResponse<UserResponse> getUserFollowings(Long userId, int page, int size) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Follow> followPage = followRepository.findByFollowerId(userId, pageable);

        List<UserResponse> content = followPage.getContent().stream()
                .map(follow -> toUserResponseWithFollowingStatus(follow.getFollowing()))
                .collect(Collectors.toList());

        return PageResponse.<UserResponse>builder()
                .content(content)
                .page(followPage.getNumber())
                .size(followPage.getSize())
                .totalElements(followPage.getTotalElements())
                .totalPages(followPage.getTotalPages())
                .first(followPage.isFirst())
                .last(followPage.isLast())
                .build();
    }

    @Override
    public PageResponse<UserResponse> getUserFollowers(Long userId, int page, int size) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Follow> followPage = followRepository.findByFollowingId(userId, pageable);

        List<UserResponse> content = followPage.getContent().stream()
                .map(follow -> toUserResponseWithFollowingStatus(follow.getFollower()))
                .collect(Collectors.toList());

        return PageResponse.<UserResponse>builder()
                .content(content)
                .page(followPage.getNumber())
                .size(followPage.getSize())
                .totalElements(followPage.getTotalElements())
                .totalPages(followPage.getTotalPages())
                .first(followPage.isFirst())
                .last(followPage.isLast())
                .build();
    }
}