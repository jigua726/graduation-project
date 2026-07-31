import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      const publicPaths = ['/auth/', '/password/', '/articles/public/', '/categories/', '/files/']
      const isPublic = publicPaths.some(path => config.url.startsWith(path))
      if (!isPublic) {
        config.headers.Authorization = `Bearer ${token}`
      }
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    if (error.response.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export const authAPI = {
  login: (data) => api.post('/auth/login', data),
  register: (data) => api.post('/auth/register', data),
  requestPasswordReset: (data) => api.post('/password/reset/request', data),
  resetPassword: (data) => api.post('/password/reset', data),
  validateResetToken: (token) => api.get(`/password/reset/validate/${token}`)
}

export const userAPI = {
  getCurrentUser: () => api.get('/users/me'),
  getUserById: (id) => api.get(`/users/${id}`),
  getUserByUsername: (username) => api.get(`/users/username/${username}`),
  updateUser: (id, data) => api.put(`/users/${id}`, data),
  deleteUser: (id) => api.delete(`/users/${id}`),
  follow: (id) => api.post(`/users/${id}/follow`),
  unfollow: (id) => api.delete(`/users/${id}/follow`),
  isFollowing: (id) => api.get(`/users/${id}/is-following`),
  getUserCategoryStats: (id) => api.get(`/users/${id}/category-stats`),
  getUserFavorites: (id, params) => api.get(`/users/${id}/favorites`, { params }),
  getUserFollowings: (id, params) => api.get(`/users/${id}/followings`, { params }),
  getUserFollowers: (id, params) => api.get(`/users/${id}/followers`, { params })
}

export const articleAPI = {
  getAll: (params) => api.get('/articles/public', { params }),
  getById: (id) => api.get(`/articles/${id}`),
  getByCategory: (id, params) => api.get(`/articles/public/category/${id}`, { params }),
  getByTag: (name, params) => api.get(`/articles/public/tag/${name}`, { params }),
  getByUser: (id, params) => api.get(`/articles/public/user/${id}`, { params }),
  search: (params) => api.get('/articles/public/search', { params }),
  create: (data) => api.post('/articles', data),
  update: (id, data) => api.put(`/articles/${id}`, data),
  delete: (id) => api.delete(`/articles/${id}`),
  like: (id) => api.post(`/articles/${id}/like`),
  unlike: (id) => api.delete(`/articles/${id}/like`),
  favorite: (id) => api.post(`/articles/${id}/favorite`),
  unfavorite: (id) => api.delete(`/articles/${id}/favorite`)
}

export const fileAPI = {
  upload: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/files/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  }
}

export const commentAPI = {
  create: (data) => api.post('/comments', data),
  getByArticle: (id, params) => api.get(`/comments/article/${id}`, { params }),
  update: (id, content) => api.put(`/comments/${id}`, content),
  delete: (id) => api.delete(`/comments/${id}`),
  like: (id) => api.post(`/comments/${id}/like`),
  unlike: (id) => api.delete(`/comments/${id}/like`),
  pin: (id) => api.post(`/comments/${id}/pin`)
}

export const categoryAPI = {
  getAll: () => api.get('/categories'),
  getById: (id) => api.get(`/categories/${id}`),
  create: (params) => api.post('/categories', null, { params }),
  update: (id, params) => api.put(`/categories/${id}`, null, { params }),
  delete: (id) => api.delete(`/categories/${id}`)
}

export const tagAPI = {
  getAll: () => api.get('/tags'),
  getById: (id) => api.get(`/tags/${id}`),
  getByName: (name) => api.get(`/tags/name/${name}`),
  create: (data) => api.post('/tags', data),
  update: (id, data) => api.put(`/tags/${id}`, data),
  delete: (id) => api.delete(`/tags/${id}`)
}

export const adminAPI = {
  getStats: () => api.get('/admin/stats'),
  getAllUsers: (params) => api.get('/admin/users', { params }),
  updateUserRole: (id, params) => api.put(`/admin/users/${id}/role`, null, { params }),
  deleteUser: (id) => api.delete(`/admin/users/${id}`),
  disableUser: (id) => api.post(`/admin/users/${id}/disable`),
  enableUser: (id) => api.post(`/admin/users/${id}/enable`),
  getAllArticles: (params) => api.get('/admin/articles', { params }),
  deleteArticle: (id) => api.delete(`/admin/articles/${id}`),
  updateArticleStatus: (id, params) => api.put(`/admin/articles/${id}/status`, null, { params }),
  autoGenerateTags: () => api.post('/admin/articles/generate-tags')
}

export default api