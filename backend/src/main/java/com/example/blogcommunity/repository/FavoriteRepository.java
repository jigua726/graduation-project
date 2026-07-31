package com.example.blogcommunity.repository;

import com.example.blogcommunity.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserIdAndArticleId(Long userId, Long articleId);

    boolean existsByUserIdAndArticleId(Long userId, Long articleId);

    Page<Favorite> findByUserId(Long userId, Pageable pageable);

    long countByArticleId(Long articleId);

    void deleteByArticleId(Long articleId);
}