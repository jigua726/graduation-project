<template>
  <div class="followings-list">
    <div class="page-header">
      <h2>{{ title }}</h2>
      <p>共 {{ totalCount }} 人</p>
    </div>

    <div class="user-list">
      <div 
        v-for="user in users" 
        :key="user.id" 
        class="user-item"
        @click="goToUserProfile(user.id)"
      >
        <img :src="user.avatar || `https://picsum.photos/seed/${user.username}/64/64`" class="user-avatar" alt="avatar" />
        <div class="user-info">
          <span class="user-nickname">{{ user.nickname }}</span>
          <span class="user-username">@{{ user.username }}</span>
          <span class="user-bio">{{ user.bio || '暂无简介' }}</span>
        </div>
        <div class="user-stats">
          <span>{{ user.articleCount }} 文章</span>
          <span>{{ user.followerCount }} 粉丝</span>
        </div>
        <el-button 
          v-if="isLoggedIn && user.id !== currentUserId"
          :type="user.isFollowing ? 'default' : 'primary'"
          size="small"
          @click.stop="handleFollow(user.id, user.isFollowing)"
        >
          {{ user.isFollowing ? '已关注' : '+ 关注' }}
        </el-button>
      </div>
      <div v-if="users.length === 0" class="empty-state">
        <p>暂无{{ emptyText }}</p>
      </div>
    </div>

    <el-pagination
      v-if="totalCount > pageSize"
      :current-page="currentPage"
      :page-size="pageSize"
      :total="totalCount"
      @current-change="handlePageChange"
      layout="prev, pager, next, jumper"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { userAPI } from '../api'

const route = useRoute()
const router = useRouter()

const userId = ref(route.params.userId)
const listType = ref(route.params.type) // 'followings' or 'followers'

const users = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const totalCount = ref(0)

const isLoggedIn = computed(() => localStorage.getItem('token') !== null)
const currentUserId = computed(() => {
  const user = localStorage.getItem('user')
  return user ? JSON.parse(user).id : null
})

const title = computed(() => {
  return listType.value === 'followings' ? '我关注的人' : '我的粉丝'
})

const emptyText = computed(() => {
  return listType.value === 'followings' ? '关注' : '粉丝'
})

const loadUsers = async () => {
  try {
    const params = { page: currentPage.value - 1, size: pageSize.value }
    let response
    
    if (listType.value === 'followings') {
      response = await userAPI.getUserFollowings(userId.value, params)
    } else {
      response = await userAPI.getUserFollowers(userId.value, params)
    }
    
    if (response.success) {
      users.value = response.data.content
      totalCount.value = response.data.totalElements
    }
  } catch (error) {
    console.error('Failed to load users:', error)
  }
}

const handlePageChange = (page) => {
  currentPage.value = page
  loadUsers()
}

const goToUserProfile = (id) => {
  router.push(`/user/${id}`)
}

const handleFollow = async (targetId, isFollowing) => {
  try {
    if (isFollowing) {
      await userAPI.unfollow(targetId)
    } else {
      await userAPI.follow(targetId)
    }
    loadUsers()
  } catch (error) {
    console.error('Failed to follow/unfollow:', error)
  }
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.followings-list {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 1.5rem;
  margin-bottom: 5px;
}

.page-header p {
  color: #909399;
}

.user-list {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.user-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
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

.user-avatar {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  object-fit: cover;
}

.user-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-nickname {
  font-weight: 600;
  color: #303133;
}

.user-username {
  font-size: 0.85rem;
  color: #909399;
}

.user-bio {
  font-size: 0.85rem;
  color: #606266;
  margin-top: 4px;
}

.user-stats {
  display: flex;
  gap: 12px;
  font-size: 0.8rem;
  color: #909399;
}

.empty-state {
  padding: 60px 20px;
  text-align: center;
  color: #909399;
}

.el-pagination {
  margin-top: 20px;
  text-align: center;
}
</style>