package com.example.blogcommunity.repository;

import com.example.blogcommunity.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByArticleIdAndParentIsNullAndDeletedFalse(Long articleId, Pageable pageable);

    List<Comment> findByParentIdAndDeletedFalse(Long parentId);

    Page<Comment> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    long countByArticleIdAndDeletedFalse(Long articleId);

    @Modifying
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount + 1 WHERE c.id = :id")
    void incrementLikeCount(Long id);

    @Modifying
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount - 1 WHERE c.id = :id")
    void decrementLikeCount(Long id);

    @Modifying
    @Query("UPDATE Comment c SET c.deleted = true WHERE c.id = :id")
    void softDelete(Long id);

    @Modifying
    @Query("UPDATE Comment c SET c.pinned = :pinned WHERE c.id = :id")
    void updatePinned(Long id, Boolean pinned);

    Page<Comment> findByArticleIdAndParentIsNullAndDeletedFalseOrderByPinnedDescCreatedAtDesc(Long articleId, Pageable pageable);

    void deleteByArticleId(Long articleId);
}