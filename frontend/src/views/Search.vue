<template>
  <div class="search-page">
    <div class="page-header">
      <h1>搜索结果</h1>
      <p>关键词: "{{ keyword }}"</p>
      <p class="article-count">共 {{ totalArticles }} 篇文章</p>
    </div>

    <div class="article-list">
      <div 
        v-for="article in articles" 
        :key="article.id" 
        class="card article-card"
        @click="goToArticle(article.id)"
      >
        <img 
          :src="article.coverImage || getArticleCover(article)" 
          alt="文章封面" 
          class="article-cover"
        />
        <h2 class="article-title">{{ article.title }}</h2>
        <p class="article-summary">{{ article.summary }}</p>
        
        <div class="article-meta">
          <div class="author-info" @click.stop="goToUser(article.author.id)">
            <img :src="article.author.avatar || `https://picsum.photos/seed/${article.author.username}/40/40`" class="user-avatar-sm" alt="avatar" />
            <span>{{ article.author.nickname }}</span>
          </div>
          <span class="category" v-if="article.category">{{ article.category.name }}</span>
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

        <div class="article-stats">
          <span>👁️ {{ article.viewCount }}</span>
          <span>❤️ {{ article.likeCount }}</span>
          <span>💬 {{ article.commentCount }}</span>
          <span>📅 {{ formatDate(article.createdAt) }}</span>
        </div>
      </div>
      
      <div v-if="articles.length === 0" class="empty-state">
        <p>暂无搜索结果</p>
      </div>
    </div>

    <el-pagination
      v-if="totalArticles > 0"
      :current-page="currentPage"
      :page-size="pageSize"
      :total="totalArticles"
      @current-change="handlePageChange"
      layout="prev, pager, next, jumper"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getArticleCover } from '../utils/articleCover'
import { articleAPI } from '../api'

const route = useRoute()
const keyword = computed(() => {
  const params = new URLSearchParams(window.location.search)
  return params.get('keyword') || ''
})

const articles = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const totalArticles = ref(0)

onMounted(() => {
  loadArticles()
})

const loadArticles = async () => {
  if (!keyword.value.trim()) return
  
  try {
    const response = await articleAPI.search({ keyword: keyword.value, page: currentPage.value - 1, size: pageSize.value })
    if (response.success) {
      articles.value = response.data.content
      totalArticles.value = response.data.totalElements
    }
  } catch (error) {
    console.error('Failed to search articles:', error)
  }
}

const handlePageChange = (page) => {
  currentPage.value = page
  loadArticles()
}

const goToArticle = (id) => {
  window.location.href = `/article/${id}`
}

const goToUser = (id) => {
  window.location.href = `/user/${id}`
}

const goToTag = (name) => {
  window.location.href = `/tag/${encodeURIComponent(name)}`
}

const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}
</script>

<style scoped>
.search-page {
  max-width: 800px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
}

.page-header h1 {
  font-size: 2rem;
  color: #303133;
  margin-bottom: 10px;
}

.page-header p {
  color: #606266;
}

.article-count {
  color: #909399;
  font-size: 0.9rem;
}

.article-card {
  cursor: pointer;
  transition: all 0.3s;
  overflow: hidden;
}

.article-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.article-cover {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 15px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #606266;
}

.author-info:hover {
  color: #667eea;
}

.article-tags {
  margin: 10px 0;
}

.article-stats {
  display: flex;
  gap: 15px;
  color: #909399;
  font-size: 0.85rem;
  margin-top: 10px;
  padding-top: 10px;
  border-top: 1px solid #f2f6fc;
}

.empty-state {
  text-align: center;
  padding: 40px;
  color: #909399;
}
</style>