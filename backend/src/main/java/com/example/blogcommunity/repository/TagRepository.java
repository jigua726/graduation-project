package com.example.blogcommunity.repository;

import com.example.blogcommunity.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    boolean existsByName(String name);

    @Modifying
    @Query("UPDATE Tag t SET t.articleCount = t.articleCount + 1 WHERE t.id = :id")
    void incrementArticleCount(Long id);

    @Modifying
    @Query("UPDATE Tag t SET t.articleCount = t.articleCount - 1 WHERE t.id = :id")
    void decrementArticleCount(Long id);
}