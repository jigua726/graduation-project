<template>
  <div class="tag-search-page">
    <div class="tag-header">
      <el-tag type="primary" size="large">{{ tagName }}</el-tag>
      <p class="tag-count">共 {{ totalArticles }} 篇相关文章</p>
    </div>

    <div class="article-list">
      <div 
        v-for="article in articles" 
        :key="article.id" 
        class="article-card"
        @click="goToArticle(article.id)"
      >
        <h3 class="article-title">{{ article.title }}</h3>
        <p class="article-preview">{{ article.content.slice(0, 150) }}...</p>
        <div class="article-meta">
          <span class="author">👤 {{ article.author.nickname }}</span>
          <span class="time">{{ formatDate(article.createdAt) }}</span>
          <span class="views">👁️ {{ article.viewCount }}</span>
        </div>
        <div class="article-tags">
          <span 
            v-for="tag in article.tags" 
            :key="tag.id" 
            class="tag"
            @click.stop="goToTag(tag.name)"
          >
            {{ tag.name }}
          </span>
        </div>
      </div>
    </div>

    <div v-if="articles.length === 0" class="empty-state">
      <p>暂无相关文章</p>
    </div>

    <el-pagination
      v-if="totalArticles > pageSize"
      :current-page="currentPage"
      :page-size="pageSize"
      :total="totalArticles"
      @current-change="handlePageChange"
      layout="prev, pager, next, jumper"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { articleAPI } from '../api'

const route = useRoute()
const tagName = ref(decodeURIComponent(route.params.name))

const articles = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const totalArticles = ref(0)

const loadArticles = async () => {
  try {
    const response = await articleAPI.getByTag(tagName.value, { page: currentPage.value - 1, size: pageSize.value })
    if (response.success) {
      articles.value = response.data.content
      totalArticles.value = response.data.totalElements
    }
  } catch (error) {
    console.error('Failed to load articles by tag:', error)
  }
}

const handlePageChange = (page) => {
  currentPage.value = page
  loadArticles()
}

const goToArticle = (id) => {
  window.location.href = `/article/${id}`
}

const goToTag = (name) => {
  window.location.href = `/tag/${encodeURIComponent(name)}`
}

const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

onMounted(() => {
  loadArticles()
})
</script>

<style scoped>
.tag-search-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.tag-header {
  text-align: center;
  margin-bottom: 30px;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  color: white;
}

.tag-header .el-tag {
  font-size: 1.5rem;
  padding: 8px 24px;
}

.tag-count {
  margin-top: 10px;
  opacity: 0.9;
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.article-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: all 0.3s ease;
}

.article-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.article-title {
  font-size: 1.25rem;
  margin-bottom: 10px;
  color: #303133;
}

.article-preview {
  color: #606266;
  line-height: 1.6;
  margin-bottom: 15px;
}

.article-meta {
  display: flex;
  gap: 20px;
  color: #909399;
  font-size: 0.9rem;
  margin-bottom: 10px;
}

.article-tags {
  display: flex;
  gap: 8px;
}

.tag {
  padding: 4px 10px;
  background: #f0f5ff;
  color: #409eff;
  border-radius: 4px;
  font-size: 0.85rem;
  cursor: pointer;
}

.tag:hover {
  background: #e8f0fe;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: #f5f7fa;
  border-radius: 12px;
  color: #909399;
}

.el-pagination {
  margin-top: 30px;
  text-align: center;
}
</style>