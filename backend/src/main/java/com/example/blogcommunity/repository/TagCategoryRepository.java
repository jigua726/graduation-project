package com.example.blogcommunity.repository;

import com.example.blogcommunity.entity.TagCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagCategoryRepository extends JpaRepository<TagCategory, Long> {

    Optional<TagCategory> findByCategoryName(String categoryName);

    boolean existsByCategoryName(String categoryName);

    /**
     * 查询所有已启用的分类，按 sortOrder 升序排列
     */
    List<TagCategory> findByEnabledTrueOrderBySortOrderAsc();
}
