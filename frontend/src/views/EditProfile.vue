<template>
  <div class="edit-profile">
    <div class="card">
      <h2 class="form-title">编辑资料</h2>
      
      <el-form :model="profileForm" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input 
            v-model="profileForm.username" 
            disabled
          ></el-input>
        </el-form-item>
        
        <el-form-item label="邮箱" prop="email">
          <el-input 
            v-model="profileForm.email" 
            placeholder="请输入邮箱"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="昵称" prop="nickname">
          <el-input 
            v-model="profileForm.nickname" 
            placeholder="请输入昵称"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="头像" prop="avatar">
          <el-input 
            v-model="profileForm.avatar" 
            placeholder="头像URL"
          ></el-input>
          <div v-if="profileForm.avatar" class="avatar-preview">
            <img :src="profileForm.avatar" alt="preview" />
          </div>
        </el-form-item>
        
        <el-form-item label="个人简介" prop="bio">
          <el-textarea 
            v-model="profileForm.bio" 
            placeholder="介绍一下你自己..."
            :rows="3"
          ></el-textarea>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" class="btn-primary" @click="handleSubmit">保存更改</el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { userAPI } from '../api'

const profileForm = ref({
  username: '',
  email: '',
  nickname: '',
  avatar: '',
  bio: ''
})

onMounted(() => {
  loadProfile()
})

const loadProfile = async () => {
  try {
    const response = await userAPI.getCurrentUser()
    if (response.success) {
      const user = response.data
      profileForm.value = {
        username: user.username,
        email: user.email,
        nickname: user.nickname,
        avatar: user.avatar,
        bio: user.bio || ''
      }
    }
  } catch (error) {
    console.error('Failed to load profile:', error)
  }
}

const handleSubmit = async () => {
  const userId = JSON.parse(localStorage.getItem('user')).id
  
  try {
    const response = await userAPI.updateUser(userId, profileForm.value)
    if (response.success) {
      localStorage.setItem('user', JSON.stringify(response.data))
      alert('更新成功')
      window.location.href = `/user/${userId}`
    }
  } catch (error) {
    if (error.response && error.response.data) {
      alert(error.response.data.message || '更新失败')
    } else {
      alert('更新失败')
    }
  }
}

const goBack = () => {
  window.history.back()
}
</script>

<style scoped>
.edit-profile {
  max-width: 500px;
  margin: 0 auto;
}

.avatar-preview {
  margin-top: 10px;
}

.avatar-preview img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
}
</style>