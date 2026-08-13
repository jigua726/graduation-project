package com.example.blogcommunity.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 标签分类实体
 * 存储分类名称与该分类下的标签列表（以逗号分隔）
 * 替代原本硬编码在 AdminServiceImpl 中的 Map<String, List<String>>
 */
@Entity
@Table(name = "tag_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 分类名称，例如：技术、前端、后端、AI、生活 等
     */
    @Column(unique = true, nullable = false, length = 50)
    private String categoryName;

    /**
     * 该分类下的标签列表，以英文逗号分隔
     * 例如："技术,编程,开发,代码,项目,教程"
     */
    @Column(nullable = false, length = 500)
    private String tags;

    /**
     * 排序权重，数字越小越靠前
     */
    @Builder.Default
    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    /**
     * 是否启用
     */
    @Builder.Default
    @Column(nullable = false)
    private Boolean enabled = true;

    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
