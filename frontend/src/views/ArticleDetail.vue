<template>
  <div class="article-detail" v-if="article">
    <div class="card">
      <h1 class="article-title">{{ article.title }}</h1>
      
      <div class="article-meta">
        <div class="author-info" @click="goToUser(article.author.id)">
          <img :src="article.author.avatar" class="user-avatar" alt="avatar" />
          <div>
            <span class="author-name">{{ article.author.nickname }}</span>
            <span class="publish-time">{{ formatDate(article.createdAt) }}</span>
          </div>
        </div>
        
        <div class="article-actions">
          <span class="category" v-if="article.category">{{ article.category.name }}</span>
          <span 
            v-for="tag in article.tags" 
            :key="tag.id" 
            class="tag"
            @click="goToTag(tag.name)"
          >
            {{ tag.name }}
          </span>
        </div>
      </div>

      <div class="article-tags-header">
        <span class="tags-label">标签：</span>
        <span 
          v-for="tag in article.tags" 
          :key="tag.id" 
          class="tag-item"
          @click="goToTag(tag.name)"
        >
          #{{ tag.name }}
        </span>
      </div>

      <div class="article-content">
        {{ article.content }}
      </div>

      <div class="article-stats">
        <span>👁️ {{ article.viewCount }}</span>
        <span 
          class="like-btn" 
          :class="{ liked: article.isLiked, disabled: !isLoggedIn }"
          @click="handleLike"
        >
          {{ article.isLiked ? '❤️' : '🤍' }} {{ article.likeCount }}
        </span>
        <span 
          class="favorite-btn" 
          :class="{ favorited: article.isFavorited, disabled: !isLoggedIn }"
          @click="handleFavorite"
        >
          {{ article.isFavorited ? '⭐' : '☆' }} {{ article.isFavorited ? '已收藏' : '收藏' }}
        </span>
        <span>💬 {{ article.commentCount }}</span>
      </div>

      <div v-if="isAuthor" class="article-edit">
        <el-button @click="goToEdit">编辑文章</el-button>
        <el-button type="danger" @click="handleDelete">删除文章</el-button>
      </div>
    </div>

    <div class="card comments-section">
      <h3>评论 ({{ comments.length }})</h3>
      
      <div v-if="isLoggedIn" class="comment-form">
        <textarea 
          v-model="newComment" 
          placeholder="写下你的评论..."
          rows="3"
          class="comment-input"
        ></textarea>
        <button type="button" class="btn btn-primary" @click="handleComment">发表评论</button>
      </div>
      
      <div v-else class="login-prompt">
        <p>💡 <a href="/login">登录</a> 后可以发表评论和点赞</p>
      </div>

      <div class="comment-list">
        <div v-for="comment in comments" :key="comment.id" class="comment-item" :class="{ pinned: comment.isPinned }">
          <div v-if="comment.isPinned" class="pinned-badge">📌 置顶</div>
          <div class="comment-header">
            <img :src="comment.author.avatar" class="user-avatar-sm" alt="avatar" />
            <span class="comment-author">{{ comment.author.nickname }}</span>
            <span class="comment-time">{{ formatDate(comment.createdAt) }}</span>
          </div>
          <p class="comment-content">{{ comment.content }}</p>
          <div class="comment-actions">
            <span 
              class="comment-like" 
              :class="{ disabled: !isLoggedIn }"
              @click="handleCommentLike(comment.id)"
            >
              👍 {{ comment.likeCount }}
            </span>
            <span v-if="isLoggedIn" class="reply-link" @click="showReply(comment.id)">回复</span>
            <span v-if="isAuthor" class="delete-link" @click="handleDeleteComment(comment.id)">删除</span>
            <span v-if="isAuthor" class="pin-link" @click="handlePinComment(comment.id)">
              {{ comment.isPinned ? '取消置顶' : '置顶' }}
            </span>
          </div>
          
          <div v-if="replyTo === comment.id" class="reply-form">
            <input 
              type="text"
              v-model="replyContent" 
              placeholder="回复 {{ comment.author.nickname }}..."
              class="reply-input"
              @keyup.enter="handleReply(comment.id)"
            />
            <button type="button" class="btn btn-sm btn-primary" @click="handleReply(comment.id)">回复</button>
            <button type="button" class="btn btn-sm btn-secondary" @click="replyTo = null">取消</button>
          </div>

          <div v-if="comment.replies && comment.replies.length > 0" class="reply-comments">
            <div v-for="reply in comment.replies" :key="reply.id" class="reply-comment">
              <span class="reply-author">{{ reply.author.nickname }}:</span>
              <span>{{ reply.content }}</span>
            </div>
          </div>
        </div>
      </div>

      <el-pagination
        v-if="totalComments > pageSize"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="totalComments"
        @current-change="handlePageChange"
        layout="prev, pager, next, jumper"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { articleAPI, commentAPI } from '../api'

const route = useRoute()
const articleId = ref(route.params.id)

const article = ref(null)
const comments = ref([])
const newComment = ref('')
const replyContent = ref('')
const replyTo = ref(null)
const currentPage = ref(1)
const pageSize = ref(10)
const totalComments = ref(0)

const isLoggedIn = computed(() => localStorage.getItem('token') !== null)
const currentUser = computed(() => {
  const user = localStorage.getItem('user')
  return user ? JSON.parse(user) : null
})
const isAuthor = computed(() => {
  return article.value && currentUser.value && article.value.author.id === currentUser.value.id
})

onMounted(() => {
  loadArticle()
  loadComments()
})

const loadArticle = async () => {
  try {
    const response = await articleAPI.getById(articleId.value)
    if (response.success) {
      article.value = response.data
    }
  } catch (error) {
    console.error('Failed to load article:', error)
  }
}

const loadComments = async () => {
  try {
    const response = await commentAPI.getByArticle(articleId.value, { page: currentPage.value - 1, size: pageSize.value })
    if (response.success) {
      comments.value = response.data.content
      totalComments.value = response.data.totalElements
    }
  } catch (error) {
    console.error('Failed to load comments:', error)
  }
}

const handleLike = async () => {
  console.log('点击点赞按钮，当前登录状态:', isLoggedIn.value)
  console.log('Token:', localStorage.getItem('token'))
  
  if (!isLoggedIn.value) {
    if (confirm('请先登录后再点赞，是否跳转到登录页面？')) {
      window.location.href = '/login'
    }
    return
  }
  
  try {
    console.log('发送点赞请求...')
    if (article.value.isLiked) {
      console.log('取消点赞，articleId:', articleId.value)
      const response = await articleAPI.unlike(articleId.value)
      console.log('取消点赞响应:', response)
      if (response.success) {
        article.value = response.data
        showToast('已取消点赞')
      }
    } else {
      console.log('点赞，articleId:', articleId.value)
      const response = await articleAPI.like(articleId.value)
      console.log('点赞响应:', response)
      if (response.success) {
        article.value = response.data
        showToast('点赞成功')
      }
    }
  } catch (error) {
    console.error('点赞失败:', error)
    console.error('错误状态:', error.response?.status)
    console.error('错误信息:', error.response?.data)
    if (error.response && error.response.status === 401) {
      showToast('登录已过期，请重新登录')
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      setTimeout(() => window.location.href = '/login', 1500)
    }
  }
}

const handleFavorite = async () => {
  if (!isLoggedIn.value) {
    if (confirm('请先登录后再收藏，是否跳转到登录页面？')) {
      window.location.href = '/login'
    }
    return
  }
  
  try {
    if (article.value.isFavorited) {
      const response = await articleAPI.unfavorite(articleId.value)
      if (response.success) {
        article.value = response.data
        showToast('已取消收藏')
      }
    } else {
      const response = await articleAPI.favorite(articleId.value)
      if (response.success) {
        article.value = response.data
        showToast('收藏成功')
      }
    }
  } catch (error) {
    console.error('Failed to favorite article:', error)
    if (error.response && error.response.status === 401) {
      showToast('登录已过期，请重新登录')
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      setTimeout(() => window.location.href = '/login', 1500)
    }
  }
}

const showToast = (message) => {
  const toast = document.createElement('div')
  toast.className = 'toast-message'
  toast.textContent = message
  document.body.appendChild(toast)
  setTimeout(() => toast.classList.add('show'), 10)
  setTimeout(() => {
    toast.classList.remove('show')
    setTimeout(() => document.body.removeChild(toast), 300)
  }, 2000)
}

const handleComment = async () => {
  if (!newComment.value.trim()) {
    alert('请输入评论内容')
    return
  }
  
  try {
    const response = await commentAPI.create({
      articleId: articleId.value,
      content: newComment.value
    })
    if (response.success) {
      newComment.value = ''
      loadComments()
      loadArticle()
      showToast('评论成功！')
    } else {
      showToast('评论失败: ' + (response.message || '未知错误'))
    }
  } catch (error) {
    console.error('Failed to comment:', error)
    if (error.response) {
      if (error.response.status === 401) {
        alert('登录已过期，请重新登录')
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        window.location.href = '/login'
      } else if (error.response.data && error.response.data.message) {
        alert('评论失败: ' + error.response.data.message)
      } else {
        alert('评论失败，请稍后重试')
      }
    } else {
      alert('网络错误，请检查网络连接')
    }
  }
}

const handleReply = async (commentId) => {
  if (!replyContent.value.trim()) {
    alert('请输入回复内容')
    return
  }
  
  try {
    const response = await commentAPI.create({
      articleId: articleId.value,
      content: replyContent.value,
      parentId: commentId
    })
    if (response.success) {
      replyContent.value = ''
      replyTo.value = null
      loadComments()
      loadArticle()
      showToast('回复成功')
    } else {
      showToast('回复失败: ' + response.message)
    }
  } catch (error) {
    console.error('Failed to reply:', error)
    if (error.response && error.response.status === 401) {
      alert('登录已过期，请重新登录')
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    } else {
      alert('回复失败，请稍后重试')
    }
  }
}

const handleCommentLike = async (commentId) => {
  if (!isLoggedIn.value) {
    if (confirm('请先登录后再点赞，是否跳转到登录页面？')) {
      window.location.href = '/login'
    }
    return
  }
  
  try {
    await commentAPI.like(commentId)
    loadComments()
  } catch (error) {
    console.error('Failed to like comment:', error)
    if (error.response && error.response.status === 401) {
      alert('登录已过期，请重新登录')
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
  }
}

const handleDeleteComment = async (commentId) => {
  if (!confirm('确定要删除这条评论吗？')) {
    return
  }
  
  try {
    const response = await commentAPI.delete(commentId)
    if (response.success) {
      loadComments()
      loadArticle()
      showToast('删除成功')
    }
  } catch (error) {
    console.error('Failed to delete comment:', error)
    showToast('删除失败')
  }
}

const handlePinComment = async (commentId) => {
  try {
    const response = await commentAPI.pin(commentId)
    if (response.success) {
      loadComments()
    }
  } catch (error) {
    console.error('Failed to pin comment:', error)
    alert('操作失败')
  }
}

const showReply = (commentId) => {
  replyTo.value = replyTo.value === commentId ? null : commentId
}

const goToUser = (id) => {
  window.location.href = `/user/${id}`
}

const goToTag = (name) => {
  window.location.href = `/tag/${encodeURIComponent(name)}`
}

const goToEdit = () => {
  window.location.href = `/article/edit/${articleId.value}`
}

const handleDelete = async () => {
  if (!confirm('确定要删除这篇文章吗？')) return
  
  try {
    const response = await articleAPI.delete(articleId.value)
    if (response.success) {
      window.location.href = '/'
    }
  } catch (error) {
    console.error('Failed to delete article:', error)
  }
}

const handlePageChange = (page) => {
  currentPage.value = page
  loadComments()
}

const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}
</script>

<style scoped>
.article-detail {
  max-width: 800px;
  margin: 0 auto;
}

.article-title {
  font-size: 2rem;
  margin-bottom: 20px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
}

.author-name {
  font-weight: 600;
  color: #303133;
}

.publish-time {
  color: #909399;
  font-size: 0.9rem;
}

.article-actions {
  margin-top: 15px;
}

.article-tags-header {
  margin-top: 20px;
  padding: 12px 16px;
  background: #f8f9fa;
  border-radius: 8px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
}

.tags-label {
  font-size: 0.9rem;
  color: #909399;
  font-weight: 500;
}

.tag-item {
  font-size: 0.85rem;
  color: #409eff;
  cursor: pointer;
  padding: 4px 10px;
  background: #e8f4ff;
  border-radius: 4px;
  transition: all 0.3s;
}

.tag-item:hover {
  background: #409eff;
  color: white;
}

.article-content {
  margin-top: 30px;
  line-height: 2;
  color: #303133;
  font-size: 1.1rem;
  white-space: pre-wrap;
}

.article-stats {
  display: flex;
  gap: 20px;
  margin-top: 30px;
  padding: 15px;
  background: #fafafa;
  border-radius: 8px;
}

.article-stats span {
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 20px;
  transition: all 0.3s;
  color: #909399;
}

.article-stats span:hover:not(.disabled) {
  background: #e8f0fe;
}

.like-btn.liked {
  color: #f56c6c !important;
}

.like-btn.disabled,
.favorite-btn.disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.favorite-btn.favorited {
  color: #e6a23c !important;
}

.article-edit {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}

.comments-section {
  margin-top: 20px;
}

.comment-form {
  margin-bottom: 20px;
}

.comment-input {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  font-size: 14px;
  resize: vertical;
  box-sizing: border-box;
  font-family: inherit;
}

.comment-input:focus {
  outline: none;
  border-color: #409eff;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-primary {
  background: #409eff;
  color: #fff;
}

.btn-primary:hover {
  background: #66b1ff;
}

.btn-secondary {
  background: #f5f7fa;
  color: #606266;
  border: 1px solid #d9d9d9;
}

.btn-secondary:hover {
  background: #e4e7ed;
}

.btn-sm {
  padding: 6px 12px;
  font-size: 12px;
}

.reply-input {
  padding: 8px 12px;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  font-size: 14px;
  margin-right: 8px;
  width: 300px;
}

.reply-input:focus {
  outline: none;
  border-color: #409eff;
}

.comment-item.pinned {
  background: #fffbe6;
  border-left: 4px solid #e6a23c;
}

.pinned-badge {
  color: #e6a23c;
  font-size: 12px;
  font-weight: 600;
  margin-bottom: 8px;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.comment-author {
  font-weight: 600;
}

.comment-time {
  color: #909399;
  font-size: 0.85rem;
}

.comment-content {
  color: #303133;
  line-height: 1.6;
}

.comment-actions {
  display: flex;
  gap: 20px;
  margin-top: 10px;
  color: #909399;
  font-size: 0.9rem;
  cursor: pointer;
}

.comment-actions span:hover {
  color: #667eea;
}

.like-btn.disabled,
.favorite-btn.disabled,
.comment-like.disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.delete-link {
  color: #f56c6c !important;
}

.pin-link {
  color: #e6a23c !important;
}

.login-prompt {
  padding: 15px;
  background: #fffbe6;
  border: 1px solid #ffe58f;
  border-radius: 8px;
  margin-bottom: 20px;
}

.login-prompt a {
  color: #409eff;
  text-decoration: none;
}

.login-prompt a:hover {
  text-decoration: underline;
}

.reply-form {
  margin-top: 15px;
  padding: 10px;
  background: #fafafa;
  border-radius: 8px;
}

.reply-comments {
  margin-top: 15px;
}

.reply-author {
  font-weight: 600;
  margin-right: 8px;
}
</style>

<style>
.toast-message {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%) translateY(-100px);
  background: rgba(0, 0, 0, 0.8);
  color: #fff;
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 14px;
  z-index: 9999;
  opacity: 0;
  transition: all 0.3s ease;
}

.toast-message.show {
  opacity: 1;
  transform: translateX(-50%) translateY(0);
}
</style>