<template>
  <div class="user-profile">
    <div v-if="loading" class="loading-state">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>
    
    <div v-else-if="error" class="error-state">
      <div class="error-icon">❌</div>
      <p>无法加载用户信息</p>
      <p class="error-message">{{ error }}</p>
      <el-button @click="reloadPage">重新加载</el-button>
    </div>
    
    <div v-else-if="user">
      <header class="profile-header">
      <div class="header-content">
        <div class="avatar-section">
          <img :src="user.avatar || `https://picsum.photos/seed/${user.username}/128/128`" class="avatar" alt="avatar" />
          <div class="verified-badge" v-if="user.role === 'ADMIN'">✓</div>
        </div>
        <div class="profile-info">
          <h1 class="nickname">{{ user.nickname }}</h1>
          <p class="username">@{{ user.username }}</p>
          <p class="bio">{{ user.bio || '暂无简介' }}</p>
          
          <div class="stats-row">
            <div class="stat-item">
              <span class="stat-value">{{ user.articleCount }}</span>
              <span class="stat-label">文章</span>
            </div>
            <div class="stat-item clickable" @click="goToFollowList('followers')">
              <span class="stat-value">{{ user.followerCount }}</span>
              <span class="stat-label">粉丝</span>
            </div>
            <div class="stat-item clickable" @click="goToFollowList('followings')">
              <span class="stat-value">{{ user.followingCount }}</span>
              <span class="stat-label">关注</span>
            </div>
          </div>

          <div class="action-buttons">
            <el-button 
              v-if="isLoggedIn && !isCurrentUser" 
              :type="user.isFollowing ? 'default' : 'primary'"
              :class="{ 'followed': user.isFollowing }"
              @click="handleFollow"
            >
              {{ user.isFollowing ? '已关注' : '+ 关注' }}
            </el-button>
            <el-button 
              v-if="isCurrentUser" 
              type="default"
              @click="goToEditProfile"
            >
              编辑资料
            </el-button>
          </div>
        </div>
      </div>

      <nav class="profile-nav">
        <a 
          v-for="item in navItems" 
          :key="item.key"
          :href="item.href"
          :class="{ active: activeNav === item.key }"
          @click.prevent="activeNav = item.key"
        >
          {{ item.label }}
          <span v-if="item.count" class="nav-count">{{ item.count }}</span>
        </a>
      </nav>
    </header>

    <main class="profile-content">
      <aside class="sidebar">
        <div class="widget about-card">
          <h3>关于我</h3>
          <div class="widget-content">
            <p>{{ user.bio || '暂无详细介绍' }}</p>
            <div class="contact-info">
              <div class="contact-item">
                <span class="icon">📧</span>
                <span>{{ user.email }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="widget stats-card">
          <h3>数据统计</h3>
          <div class="widget-content">
            <div class="stat-grid">
              <div class="mini-stat">
                <span class="number">{{ user.articleCount }}</span>
                <span class="text">文章</span>
              </div>
              <div class="mini-stat">
                <span class="number">{{ totalViews }}</span>
                <span class="text">总阅读</span>
              </div>
              <div class="mini-stat">
                <span class="number">{{ totalLikes }}</span>
                <span class="text">总获赞</span>
              </div>
              <div class="mini-stat">
                <span class="number">{{ runningDays }}</span>
                <span class="text">运行天数</span>
              </div>
            </div>
          </div>
        </div>

        <div class="widget tag-cloud">
          <h3>标签云</h3>
          <div class="widget-content">
            <span 
              v-for="tag in tags" 
              :key="tag" 
              class="tag"
              @click="filterByTag(tag)"
            >
              {{ tag }}
            </span>
          </div>
        </div>

        <div class="widget search-widget">
          <h3>搜索文章</h3>
          <div class="widget-content">
            <el-input 
              v-model="searchQuery" 
              placeholder="搜索文章..."
              @keyup.enter="handleSearch"
            >
              <template #append>
                <el-button @click="handleSearch">搜索</el-button>
              </template>
            </el-input>
          </div>
        </div>
      </aside>

      <section class="main-content">
        <div v-if="activeNav === 'articles'" class="tab-content">
          <div class="content-header">
            <h2>最新文章</h2>
            <div class="filter-tabs">
              <button 
                v-for="tab in articleTabs" 
                :key="tab.key"
                :class="{ active: activeArticleTab === tab.key }"
                @click="activeArticleTab = tab.key"
              >
                {{ tab.label }}
              </button>
            </div>
          </div>

          <div class="article-list">
            <div 
              v-for="article in articles" 
              :key="article.id" 
              class="article-card"
              @click="goToArticle(article.id)"
            >
              <img 
                v-if="article.coverImage"
                :src="article.coverImage" 
                alt="文章封面" 
                class="article-cover"
              />
              <div class="article-header">
                <span class="category" :style="{ backgroundColor: article.categoryColor }">
                  {{ article.categoryName }}
                </span>
                <span class="publish-date">{{ formatDate(article.createdAt) }}</span>
              </div>
              <h3 class="article-title">{{ article.title }}</h3>
              <p class="article-summary">{{ article.summary }}</p>
              <div class="article-footer">
                <span class="meta-item">👁️ {{ article.viewCount }}</span>
                <span class="meta-item">❤️ {{ article.likeCount }}</span>
                <span class="meta-item">💬 {{ article.commentCount }}</span>
                <div class="article-actions" v-if="isCurrentUser">
                  <span class="action-btn edit" @click.stop="goToEdit(article.id)">✏️ 编辑</span>
                  <span class="action-btn delete" @click.stop="handleDelete(article.id)">🗑️ 删除</span>
                </div>
              </div>
            </div>

            <div v-if="articles.length === 0" class="empty-state">
              <div class="empty-icon">📝</div>
              <p>暂无文章</p>
            </div>
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

        <div v-if="activeNav === 'categories'" class="tab-content">
          <h2>文章分类</h2>
          <div class="category-grid">
            <div 
              v-for="stat in userCategoryStats" 
              :key="stat.categoryId" 
              class="category-card"
              @click="filterByCategory(stat.categoryId)"
            >
              <div class="category-icon">📁</div>
              <h3>{{ stat.categoryName }}</h3>
              <p>{{ stat.categoryDescription }}</p>
              <span class="article-count">{{ stat.articleCount }} 篇文章</span>
            </div>
          </div>
        </div>

        <div v-if="activeNav === 'archive'" class="tab-content">
          <h2>文章归档</h2>
          <div class="archive-list">
            <div 
              v-for="(articlesByMonth, monthKey) in archivedArticles" 
              :key="monthKey"
              class="archive-month"
            >
              <h3>{{ monthKey }}</h3>
              <ul>
                <li 
                  v-for="article in articlesByMonth" 
                  :key="article.id"
                  @click="goToArticle(article.id)"
                >
                  {{ article.title }}
                  <span class="date">{{ formatDate(article.createdAt) }}</span>
                </li>
              </ul>
            </div>
          </div>
        </div>

        <div v-if="activeNav === 'favorites'" class="tab-content">
          <div class="content-header">
            <h2>我的收藏</h2>
          </div>
          <div class="article-list">
            <article 
              v-for="article in favoriteArticles" 
              :key="article.id" 
              class="article-card"
              @click="goToArticle(article.id)"
            >
              <div class="article-cover-wrapper">
                <img v-if="article.coverImage" :src="article.coverImage" class="article-cover" alt="cover" />
              </div>
              <div class="article-content">
                <h3 class="article-title">{{ article.title }}</h3>
                <p class="article-summary">{{ article.summary }}</p>
                <div class="article-footer">
                  <span class="meta-item">👁️ {{ article.viewCount }}</span>
                  <span class="meta-item">❤️ {{ article.likeCount }}</span>
                  <span class="meta-item">💬 {{ article.commentCount }}</span>
                </div>
              </div>
            </article>

            <div v-if="favoriteArticles.length === 0" class="empty-state">
              <div class="empty-icon">⭐</div>
              <p>暂无收藏的文章</p>
            </div>
          </div>

          <el-pagination
            v-if="totalFavorites > pageSize"
            :current-page="favoritePage"
            :page-size="pageSize"
            :total="totalFavorites"
            @current-change="handleFavoritePageChange"
            layout="prev, pager, next, jumper"
          />
        </div>
      </section>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { userAPI, articleAPI, categoryAPI } from '../api'
import { getArticleCover } from '../utils/articleCover'

const route = useRoute()
const router = useRouter()
const userId = ref(route.params.id)

const loading = ref(true)
const error = ref(null)
const user = ref(null)
const articles = ref([])
const categories = ref([])
const userCategoryStats = ref([])
const tags = ref([])
const currentPage = ref(1)
const pageSize = ref(6)
const totalArticles = ref(0)
const activeNav = ref('articles')
const activeArticleTab = ref('all')
const searchQuery = ref('')
const favoriteArticles = ref([])
const favoritePage = ref(1)
const totalFavorites = ref(0)

const isLoggedIn = computed(() => localStorage.getItem('token') !== null)
const currentUser = computed(() => {
  const user = localStorage.getItem('user')
  return user ? JSON.parse(user) : null
})
const isCurrentUser = computed(() => {
  return user.value && currentUser.value && user.value.id === currentUser.value.id
})

const navItems = computed(() => {
  const items = [
    { key: 'articles', label: '文章', href: '#', count: user.value?.articleCount },
    { key: 'categories', label: '分类', href: '#', count: categories.value.length },
    { key: 'archive', label: '归档', href: '#' }
  ]
  if (isCurrentUser.value) {
    items.push({ key: 'favorites', label: '收藏', href: '#' })
  }
  return items
})

const articleTabs = [
  { key: 'all', label: '全部' },
  { key: 'latest', label: '最新' },
  { key: 'popular', label: '热门' }
]

const totalViews = computed(() => {
  return articles.value.reduce((sum, article) => sum + article.viewCount, 0)
})

const totalLikes = computed(() => {
  return articles.value.reduce((sum, article) => sum + article.likeCount, 0)
})

const runningDays = ref(365)

const currentYear = new Date().getFullYear()

const archivedArticles = computed(() => {
  const grouped = {}
  articles.value.forEach(article => {
    const date = new Date(article.createdAt)
    const monthKey = `${date.getFullYear()}年${date.getMonth() + 1}月`
    if (!grouped[monthKey]) {
      grouped[monthKey] = []
    }
    grouped[monthKey].push(article)
  })
  return grouped
})

onMounted(() => {
  loadData()
})

watch(activeNav, (newNav) => {
  if (newNav === 'favorites') {
    loadFavorites()
  }
})

const loadData = async () => {
  loading.value = true
  error.value = null
  
  try {
    await Promise.all([
      loadUser(),
      loadArticles(),
      loadCategories(),
      loadUserCategoryStats(),
      loadTags()
    ])
  } catch (err) {
    error.value = err.message || '加载失败，请检查网络连接或后端服务是否启动'
    console.error('Failed to load data:', err)
  } finally {
    loading.value = false
  }
}

const reloadPage = () => {
  window.location.reload()
}

const loadUser = async () => {
  const response = await userAPI.getUserById(userId.value)
  if (response.success) {
    user.value = response.data
  }
}

const loadArticles = async () => {
  try {
    const response = await articleAPI.getByUser(userId.value, { page: currentPage.value - 1, size: pageSize.value })
    if (response.success) {
      articles.value = response.data.content
      totalArticles.value = response.data.totalElements
    }
  } catch (error) {
    console.error('Failed to load articles:', error)
  }
}

const loadCategories = async () => {
  try {
    const response = await categoryAPI.getAll()
    if (response.success) {
      categories.value = response.data
    }
  } catch (error) {
    console.error('Failed to load categories:', error)
  }
}

const loadUserCategoryStats = async () => {
  try {
    const response = await userAPI.getUserCategoryStats(userId.value)
    if (response.success) {
      userCategoryStats.value = response.data
    }
  } catch (error) {
    console.error('Failed to load user category stats:', error)
  }
}

const loadTags = async () => {
  tags.value = ['Java', 'Spring Boot', 'Vue', '前端', '生活', '旅行', '美食', '读书']
}

const handleFollow = async () => {
  try {
    if (user.value.isFollowing) {
      const response = await userAPI.unfollow(userId.value)
      if (response.success) {
        user.value.isFollowing = false
        user.value.followerCount--
      }
    } else {
      const response = await userAPI.follow(userId.value)
      if (response.success) {
        user.value.isFollowing = true
        user.value.followerCount++
      }
    }
  } catch (error) {
    console.error('Failed to follow:', error)
  }
}

const goToArticle = (id) => {
  window.location.href = `/article/${id}`
}

const goToEdit = (id) => {
  window.location.href = `/article/edit/${id}`
}

const handleDelete = async (id) => {
  if (!confirm('确定要删除这篇文章吗？此操作无法撤销。')) {
    return
  }
  
  try {
    const response = await articleAPI.delete(id)
    if (response.success) {
      alert('文章删除成功！')
      currentPage.value = 1
      loadArticles()
      loadUser()
    } else {
      alert('删除失败: ' + (response.message || '未知错误'))
    }
  } catch (error) {
    console.error('Failed to delete article:', error)
    if (error.response && error.response.data) {
      alert('删除失败: ' + error.response.data.message)
    } else {
      alert('删除失败，请稍后重试')
    }
  }
}

const goToEditProfile = () => {
  window.location.href = '/profile/edit'
}

const goToFollowList = (type) => {
  window.location.href = `/user/${userId.value}/${type}`
}

const handlePageChange = (page) => {
  currentPage.value = page
  loadArticles()
}

const filterByTag = (tag) => {
  searchQuery.value = tag
  handleSearch()
}

const filterByCategory = (categoryId) => {
  activeNav.value = 'articles'
  currentPage.value = 1
  loadArticles()
}

const handleSearch = () => {
  activeNav.value = 'articles'
  currentPage.value = 1
  loadArticles()
}

const loadFavorites = async () => {
  try {
    const response = await userAPI.getUserFavorites(userId.value, { page: favoritePage.value - 1, size: pageSize.value })
    if (response.success) {
      favoriteArticles.value = response.data.content
      totalFavorites.value = response.data.totalElements
    }
  } catch (error) {
    console.error('Failed to load favorites:', error)
  }
}

const handleFollowUser = async (targetId) => {
  try {
    const response = await userAPI.follow(targetId)
    if (response.success) {
      loadUser()
    }
  } catch (error) {
    console.error('Failed to follow:', error)
  }
}

const handleUnfollowUser = async (targetId) => {
  try {
    const response = await userAPI.unfollow(targetId)
    if (response.success) {
      loadUser()
    }
  } catch (error) {
    console.error('Failed to unfollow:', error)
  }
}

const handleFavoritePageChange = (page) => {
  favoritePage.value = page
  loadFavorites()
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
.user-profile {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.profile-header {
  background: white;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
  display: flex;
  gap: 40px;
}

.avatar-section {
  position: relative;
}

.avatar {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid #f0f0f0;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.verified-badge {
  position: absolute;
  bottom: 5px;
  right: 5px;
  width: 24px;
  height: 24px;
  background: #409eff;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
}

.profile-info {
  flex: 1;
}

.nickname {
  font-size: 2rem;
  font-weight: 700;
  color: #303133;
  margin-bottom: 8px;
}

.username {
  font-size: 1rem;
  color: #909399;
  margin-bottom: 12px;
}

.bio {
  font-size: 1rem;
  color: #606266;
  line-height: 1.7;
  margin-bottom: 20px;
}

.stats-row {
  display: flex;
  gap: 40px;
  margin-bottom: 20px;
}

.stat-item {
  display: flex;
  flex-direction: column;
}

.stat-item.clickable {
  cursor: pointer;
}

.stat-item.clickable:hover .stat-value,
.stat-item.clickable:hover .stat-label {
  color: #409eff;
}

.stat-value {
  font-size: 1.5rem;
  font-weight: 600;
  color: #303133;
}

.stat-label {
  font-size: 0.85rem;
  color: #909399;
}

.action-buttons {
  display: flex;
  gap: 12px;
}

.action-buttons .el-button {
  padding: 8px 24px;
  border-radius: 20px;
}

.action-buttons .el-button.followed {
  border: 1px solid #d9d9d9;
  color: #606266;
}

.profile-nav {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  gap: 30px;
}

.profile-nav a {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 16px 0;
  color: #606266;
  text-decoration: none;
  font-size: 0.95rem;
  transition: all 0.3s;
}

.profile-nav a:hover,
.profile-nav a.active {
  color: #409eff;
  font-weight: 500;
}

.nav-count {
  background: #f5f5f5;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 0.75rem;
}

.profile-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 20px;
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 30px;
}

.sidebar {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.widget {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.widget h3 {
  font-size: 0.95rem;
  font-weight: 600;
  color: #303133;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.widget-content {
  color: #606266;
  font-size: 0.9rem;
}

.about-card .widget-content p {
  line-height: 1.7;
  margin-bottom: 15px;
}

.contact-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.stat-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.mini-stat {
  text-align: center;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 8px;
}

.mini-stat .number {
  display: block;
  font-size: 1.2rem;
  font-weight: 600;
  color: #409eff;
}

.mini-stat .text {
  font-size: 0.75rem;
  color: #909399;
}

.tag-cloud .widget-content {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag {
  padding: 4px 12px;
  background: #ecf5ff;
  color: #409eff;
  border-radius: 12px;
  font-size: 0.8rem;
  cursor: pointer;
  transition: all 0.3s;
}

.tag:hover {
  background: #409eff;
  color: white;
}

.search-widget .el-input {
  width: 100%;
}

.main-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.tab-content {
  background: white;
  border-radius: 12px;
  padding: 25px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.content-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.content-header h2 {
  font-size: 1.3rem;
  font-weight: 600;
  color: #303133;
}

.filter-tabs {
  display: flex;
  gap: 15px;
}

.filter-tabs button {
  padding: 6px 16px;
  background: #f5f7fa;
  border: none;
  border-radius: 16px;
  color: #606266;
  font-size: 0.85rem;
  cursor: pointer;
  transition: all 0.3s;
}

.filter-tabs button:hover,
.filter-tabs button.active {
  background: #409eff;
  color: white;
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.article-card {
  padding: 20px;
  background: #fafafa;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s;
}

.article-card:hover {
  background: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.article-cover {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 15px;
}

.article-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.category {
  padding: 3px 10px;
  border-radius: 6px;
  font-size: 0.75rem;
  color: white;
}

.publish-date {
  font-size: 0.8rem;
  color: #909399;
}

.article-title {
  font-size: 1.15rem;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.article-summary {
  font-size: 0.9rem;
  color: #606266;
  line-height: 1.6;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-footer {
  display: flex;
  gap: 20px;
  color: #909399;
  font-size: 0.85rem;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #909399;
}

.empty-icon {
  font-size: 3rem;
  margin-bottom: 15px;
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
}

.category-card {
  padding: 20px;
  background: #fafafa;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s;
  text-align: center;
}

.category-card:hover {
  background: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.category-icon {
  font-size: 2rem;
  margin-bottom: 10px;
}

.category-card h3 {
  font-size: 1rem;
  font-weight: 600;
  color: #303133;
  margin-bottom: 5px;
}

.category-card p {
  font-size: 0.8rem;
  color: #909399;
  margin-bottom: 10px;
}

.article-count {
  font-size: 0.75rem;
  color: #409eff;
  background: #ecf5ff;
  padding: 3px 10px;
  border-radius: 10px;
}

.archive-list {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.archive-month h3 {
  font-size: 1rem;
  font-weight: 600;
  color: #303133;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.archive-month ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.archive-month li {
  display: flex;
  justify-content: space-between;
  padding: 10px 15px;
  background: #fafafa;
  border-radius: 6px;
  margin-bottom: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.archive-month li:hover {
  background: #ecf5ff;
}

.archive-month li .date {
  font-size: 0.8rem;
  color: #909399;
}

.profile-footer {
  background: #303133;
  margin-top: 50px;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.social-links {
  display: flex;
  gap: 20px;
}

.social-link {
  color: #909399;
  text-decoration: none;
  font-size: 0.9rem;
  transition: color 0.3s;
}

.social-link:hover {
  color: #409eff;
}

.copyright {
  color: #606266;
  font-size: 0.85rem;
}

@media (max-width: 768px) {
  .profile-content {
    grid-template-columns: 1fr;
  }
  
  .header-content {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .stats-row {
    justify-content: center;
  }
  
  .action-buttons {
    justify-content: center;
  }
  
  .profile-nav {
    overflow-x: auto;
    padding: 10px 20px;
  }
  
  .footer-content {
    flex-direction: column;
    gap: 15px;
  }
}

.user-list {
  max-height: 400px;
  overflow-y: auto;
}

.user-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background 0.3s;
}

.user-item:last-child {
  border-bottom: none;
}

.user-item:hover {
  background: #f5f7fa;
}

.user-item .user-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
}

.user-item .user-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.user-item .user-nickname {
  font-weight: 600;
  color: #303133;
}

.user-item .user-username {
  font-size: 0.85rem;
  color: #909399;
}
</style>