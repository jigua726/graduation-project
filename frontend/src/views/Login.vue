<template>
  <div class="auth-page">
    <div class="auth-bg"></div>
    <div class="auth-container">
      <div class="auth-card">
        <div class="auth-header">
          <div class="logo">
            <img src="https://picsum.photos/seed/bloglogo/60/60" alt="博客社区" class="logo-image" />
            <span class="logo-text">博客社区</span>
          </div>
          <h2 class="auth-title">欢迎回来</h2>
          <p class="auth-subtitle">登录您的账号，继续探索精彩内容</p>
        </div>
        
        <el-form :model="loginForm" label-width="0" class="auth-form">
          <el-form-item prop="username">
            <el-input 
              v-model="loginForm.username" 
              placeholder="用户名"
              @keyup.enter="handleLogin"
              class="auth-input"
            >
              <template #prefix>
                <span class="input-icon">👤</span>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item prop="password">
            <el-input 
              v-model="loginForm.password" 
              type="password" 
              placeholder="密码"
              @keyup.enter="handleLogin"
              class="auth-input"
            >
              <template #prefix>
                <span class="input-icon">🔒</span>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" class="auth-btn" style="width: 100%;" @click="handleLogin">
              登录
            </el-button>
          </el-form-item>
          
          <div class="auth-link">
            <a href="/forgot-password" class="link-btn">忘记密码？</a>
          </div>
          <div class="auth-link">
            <span>还没有账号？</span>
            <a href="/register" class="link-btn">立即注册</a>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { authAPI } from '../api'

const loginForm = ref({
  username: '',
  password: ''
})

const handleLogin = async () => {
  try {
    const response = await authAPI.login(loginForm.value)
    if (response.success) {
      localStorage.setItem('token', response.data.accessToken)
      localStorage.setItem('user', JSON.stringify(response.data.user))
      window.location.href = '/'
    }
  } catch (error) {
    if (error.response && error.response.data) {
      alert(error.response.data.message || '登录失败')
    } else {
      alert('登录失败，请检查网络连接')
    }
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.auth-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
  animation: gradientShift 15s ease infinite;
}

@keyframes gradientShift {
  0%, 100% {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
  }
  50% {
    background: linear-gradient(135deg, #764ba2 0%, #f093fb 50%, #667eea 100%);
  }
}

.auth-container {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 400px;
  padding: 20px;
}

.auth-card {
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  padding: 40px;
}

.auth-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-bottom: 20px;
}

.logo-image {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  object-fit: cover;
}

.logo-text {
  font-size: 1.5rem;
  font-weight: bold;
  color: #303133;
}

.auth-title {
  font-size: 1.8rem;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.auth-subtitle {
  font-size: 0.95rem;
  color: #909399;
}

.auth-form {
  width: 100%;
}

.auth-input {
  height: 48px;
  border-radius: 12px;
  font-size: 1rem;
  padding: 0 16px;
}

.input-icon {
  font-size: 1.1rem;
  margin-right: 8px;
}

.auth-btn {
  height: 48px;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.auth-btn:hover {
  background: linear-gradient(135deg, #5a6fd6 0%, #6a4191 100%);
}

.auth-link {
  text-align: center;
  margin-top: 20px;
  color: #909399;
  font-size: 0.95rem;
}

.link-btn {
  color: #667eea;
  font-weight: 500;
  margin-left: 4px;
  text-decoration: none;
}

.link-btn:hover {
  text-decoration: underline;
}
</style>