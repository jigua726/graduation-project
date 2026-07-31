<template>
  <div v-if="!hideLayout">
    <el-container>
      <el-header>
        <div class="header-content">
          <div class="logo" @click="goHome">
            <img src="https://picsum.photos/seed/bloglogo/40/40" alt="博客社区" class="logo-image" />
            <span class="logo-text">博客社区</span>
          </div>
          
          <div class="search-box">
            <el-input 
              v-model="searchKeyword" 
              placeholder="搜索文章..." 
              class="search-input"
              @keyup.enter="handleSearch"
            >
              <template #append>
                <el-button @click="handleSearch" icon="Search"></el-button>
              </template>
            </el-input>
          </div>

          <div class="nav-items">
            <el-button v-if="!isLoggedIn" @click="goToLogin" type="text" text>登录</el-button>
            <el-button v-if="!isLoggedIn" @click="goToRegister" type="primary" text>注册</el-button>
            
            <el-dropdown v-if="isLoggedIn">
              <el-button type="text" class="user-btn">
                <img :src="currentUser.avatar || `https://picsum.photos/seed/${currentUser.username}/32/32`" class="user-avatar-sm" alt="avatar" />
                <span class="user-name">{{ currentUser.nickname }}</span>
              </el-button>
              <template #dropdown>
                <div class="user-dropdown-header">
                  <img :src="currentUser.avatar || `https://picsum.photos/seed/${currentUser.username}/48/48`" class="user-avatar-md" alt="avatar" />
                  <div class="user-info">
                    <span class="user-nickname">{{ currentUser.nickname }}</span>
                    <span class="user-role">{{ currentUser.role === 'ADMIN' ? '管理员' : '普通用户' }}</span>
                  </div>
                </div>
                <el-dropdown-menu>
                  <el-dropdown-item @click="goToProfile">个人主页</el-dropdown-item>
                  <el-dropdown-item @click="goToEditProfile">编辑资料</el-dropdown-item>
                  <el-dropdown-item v-if="isAdmin" @click="goToAdmin">管理后台</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>

      <el-container>
        <el-aside width="200px" class="sidebar">
          <div class="sidebar-section">
            <h3 class="sidebar-title">分类</h3>
            <el-menu default-active="" class="category-menu">
              <el-menu-item v-for="cat in categories" :key="cat.id" @click="goToCategory(cat.id)">
                <template #icon><span>📁</span></template>
                {{ cat.name }} ({{ cat.articleCount }})
              </el-menu-item>
            </el-menu>
          </div>

          <div class="sidebar-section">
            <h3 class="sidebar-title">热门标签</h3>
            <div class="tag-list">
              <span 
                v-for="tag in tags" 
                :key="tag.id" 
                class="tag"
                @click="goToTag(tag.name)"
              >
                {{ tag.name }}
              </span>
            </div>
          </div>

          <div class="sidebar-section" v-if="isLoggedIn">
            <el-button @click="goToCreateArticle" class="btn-primary" style="width: 100%;">
              <template #icon><span>✏️</span></template>
              写文章
            </el-button>
          </div>
        </el-aside>

        <el-main>
          <router-view @article-created="refreshCategories" @article-deleted="refreshCategories" />
        </el-main>
      </el-container>

      <el-footer class="footer">
        <p>© 2024 博客社区 - 个性化博客社区平台</p>
      </el-footer>
    </el-container>
  </div>
  <router-view v-else />
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { categoryAPI, tagAPI } from './api'

const route = useRoute()
const searchKeyword = ref('')
const categories = ref([])
const tags = ref([])
const hideLayout = ref(false)

const isLoggedIn = ref(false)
const currentUser = ref({
  id: null,
  username: '',
  nickname: '',
  avatar: '',
  role: ''
})

onMounted(() => {
  loadCategories()
  loadTags()
  loadCurrentUser()
  updateHideLayout()
})

watch(() => route.path, async () => {
  await loadCategories()
  updateHideLayout()
}, { immediate: false })

const updateHideLayout = () => {
  hideLayout.value = route.meta?.hideLayout || false
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

const refreshCategories = async () => {
  await loadCategories()
}

const loadTags = async () => {
  try {
    const response = await tagAPI.getAll()
    if (response.success) {
      tags.value = response.data.slice(0, 10)
    }
  } catch (error) {
    console.error('Failed to load tags:', error)
  }
}

const loadCurrentUser = () => {
  const user = localStorage.getItem('user')
  if (user) {
    currentUser.value = JSON.parse(user)
    isLoggedIn.value = true
  }
}

const goHome = () => {
  window.location.href = '/'
}

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    window.location.href = `/search?keyword=${encodeURIComponent(searchKeyword.value)}`
  }
}

const goToLogin = () => {
  window.location.href = '/login'
}

const goToRegister = () => {
  window.location.href = '/register'
}

const goToProfile = () => {
  if (currentUser.value.id) {
    window.location.href = `/user/${currentUser.value.id}`
  } else {
    const user = localStorage.getItem('user')
    if (user) {
      const parsedUser = JSON.parse(user)
      if (parsedUser.id) {
        window.location.href = `/user/${parsedUser.id}`
      } else {
        alert('用户信息异常，请重新登录')
        window.location.href = '/login'
      }
    } else {
      window.location.href = '/login'
    }
  }
}

const goToEditProfile = () => {
  window.location.href = '/profile/edit'
}

const goToAdmin = () => {
  window.location.href = '/admin'
}

const goToCategory = (id) => {
  window.location.href = `/category/${id}`
}

const goToTag = (name) => {
  window.location.href = `/tag/${encodeURIComponent(name)}`
}

const goToCreateArticle = () => {
  window.location.href = '/article/create'
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  isLoggedIn.value = false
  currentUser.value = { id: null, username: '', nickname: '', avatar: '', role: '' }
  window.location.href = '/'
}

const isAdmin = () => {
  return currentUser.value.role === 'ADMIN'
}
</script>

<style scoped>
.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 60px;
}

.logo {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: white;
}

.logo-image {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  margin-right: 8px;
  object-fit: cover;
}

.logo-text {
  font-size: 1.2rem;
  font-weight: bold;
}

.search-box {
  flex: 1;
  max-width: 350px;
  margin: 0 20px;
  min-width: 150px;
}

.search-input {
  border-radius: 20px;
}

.nav-items {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 120px;
  flex-shrink: 0;
}

.nav-items .el-button--text {
  color: white;
}

.nav-items .el-button--text:hover {
  color: rgba(255, 255, 255, 0.8);
}

.nav-items .el-button--primary {
  color: white;
}

.user-btn {
  color: white;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 0 10px;
  white-space: nowrap;
}

.user-name {
  white-space: nowrap;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-avatar-sm {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

.user-dropdown-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 15px 20px;
  background: #f5f7fa;
  border-radius: 4px 4px 0 0;
}

.user-avatar-md {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
}

.user-info {
  display: flex;
  flex-direction: column;
}

.user-nickname {
  font-weight: 600;
  color: #303133;
}

.user-role {
  font-size: 0.75rem;
  color: #909399;
}

.sidebar {
  background: white;
  padding: 20px;
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.05);
}

.sidebar-section {
  margin-bottom: 20px;
}

.sidebar-title {
  font-size: 1rem;
  font-weight: 600;
  color: #303133;
  margin-bottom: 10px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f2f6fc;
}

.category-menu {
  border: none;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.footer {
  text-align: center;
  padding: 20px;
  background: #f5f7fa;
  color: #909399;
  font-size: 0.9rem;
}
</style>