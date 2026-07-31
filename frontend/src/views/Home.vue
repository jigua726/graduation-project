<template>
  <div class="home">
    <div class="page-header">
      <h1>最新文章</h1>
      <p>发现社区里的精彩内容</p>
    </div>

    <div class="article-list">
      <div 
        v-for="article in articles" 
        :key="article.id" 
        class="card article-card"
        @click="goToArticle(article.id)"
      >
        <img 
          v-if="article.coverImage"
          :src="article.coverImage" 
          alt="文章封面" 
          class="article-cover"
        />
        <h2 class="article-title">{{ article.title }}</h2>
        <p class="article-summary">{{ article.summary }}</p>
        
        <div class="article-meta">
          <div class="author-info" @click.stop="goToUser(article.author.id)">
            <div class="author-avatar-wrapper">
              <img :src="article.author.avatar || `https://picsum.photos/seed/${article.author.username}/40/40`" class="author-avatar" alt="avatar" />
              <span v-if="article.author.role === 'ADMIN'" class="admin-badge">✓</span>
            </div>
            <div class="author-meta">
              <span class="author-name">{{ article.author.nickname }}</span>
              <span class="author-bio">{{ article.author.bio ? article.author.bio.slice(0, 30) + (article.author.bio.length > 30 ? '...' : '') : '暂无简介' }}</span>
            </div>
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
    </div>

    <el-pagination
      :current-page="currentPage + 1"
      :page-size="pageSize"
      :total="total"
      @current-change="handlePageChange"
      layout="prev, pager, next, jumper"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { articleAPI } from '../api'

const articles = ref([])
const currentPage = ref(0)
const pageSize = ref(10)
const total = ref(0)

onMounted(() => {
  loadArticles()
})

const loadArticles = async () => {
  try {
    const response = await articleAPI.getAll({ page: currentPage.value, size: pageSize.value })
    if (response.success) {
      articles.value = response.data.content
      total.value = response.data.totalElements
    }
  } catch (error) {
    console.error('Failed to load articles:', error)
  }
}

const handlePageChange = (page) => {
  currentPage.value = page - 1
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
  color: #909399;
}

.article-list {
  max-width: 800px;
  margin: 0 auto;
}

.article-card {
  cursor: pointer;
  transition: all 0.3s;
  padding: 20px;
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

.article-title {
  font-size: 1.3rem;
  font-weight: 600;
  color: #303133;
  margin-bottom: 10px;
}

.article-summary {
  color: #606266;
  line-height: 1.6;
  margin-bottom: 15px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  padding: 10px;
  border-radius: 8px;
  transition: background 0.3s;
}

.author-info:hover {
  background: #f5f7fa;
}

.author-avatar-wrapper {
  position: relative;
}

.author-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.admin-badge {
  position: absolute;
  bottom: -2px;
  right: -2px;
  width: 16px;
  height: 16px;
  background: #409eff;
  color: white;
  border-radius: 50%;
  font-size: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
}

.author-meta {
  display: flex;
  flex-direction: column;
}

.author-name {
  font-weight: 600;
  color: #303133;
}

.author-bio {
  font-size: 0.8rem;
  color: #909399;
}

.category {
  padding: 4px 10px;
  background: #ecf5ff;
  color: #409eff;
  border-radius: 4px;
  font-size: 0.8rem;
}

.article-tags {
  margin: 15px 0;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag {
  padding: 4px 12px;
  background: #f5f7fa;
  color: #606266;
  border-radius: 12px;
  font-size: 0.8rem;
  cursor: pointer;
  transition: all 0.3s;
}

.tag:hover {
  background: #ecf5ff;
  color: #409eff;
}

.article-stats {
  display: flex;
  gap: 20px;
  color: #909399;
  font-size: 0.85rem;
  margin-top: 10px;
  padding-top: 15px;
  border-top: 1px solid #f2f6fc;
}
</style>