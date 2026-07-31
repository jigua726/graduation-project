package com.example.blogcommunity.repository;

import com.example.blogcommunity.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findByPublishedTrue(Pageable pageable);

    Page<Article> findByUserIdAndPublishedTrue(Long userId, Pageable pageable);

    Page<Article> findByCategoryIdAndPublishedTrue(Long categoryId, Pageable pageable);

    Page<Article> findByTagsNameAndPublishedTrue(String tagName, Pageable pageable);

    @Query("SELECT a FROM Article a WHERE a.published = true AND (a.title LIKE %:keyword% OR a.content LIKE %:keyword%)")
    Page<Article> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Modifying
    @Query("UPDATE Article a SET a.viewCount = a.viewCount + 1 WHERE a.id = :id")
    void incrementViewCount(Long id);

    @Modifying
    @Query("UPDATE Article a SET a.likeCount = a.likeCount + 1 WHERE a.id = :id")
    void incrementLikeCount(Long id);

    @Modifying
    @Query("UPDATE Article a SET a.likeCount = a.likeCount - 1 WHERE a.id = :id")
    void decrementLikeCount(Long id);

    @Modifying
    @Query("UPDATE Article a SET a.commentCount = a.commentCount + 1 WHERE a.id = :id")
    void incrementCommentCount(Long id);

    @Modifying
    @Query("UPDATE Article a SET a.commentCount = a.commentCount - 1 WHERE a.id = :id")
    void decrementCommentCount(Long id);

    @Query("SELECT a FROM Article a WHERE a.published = true ORDER BY a.viewCount DESC")
    List<Article> findTopByViewCount(Pageable pageable);

    @Query("SELECT a FROM Article a WHERE a.published = true ORDER BY a.likeCount DESC")
    List<Article> findTopByLikeCount(Pageable pageable);

    long countByUserId(Long userId);

    long countByCategoryId(Long categoryId);

    long countByUserIdAndCategoryId(Long userId, Long categoryId);
}