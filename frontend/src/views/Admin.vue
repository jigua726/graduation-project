<template>
  <div class="admin-page">
    <el-container>
      <el-aside width="200px" class="admin-sidebar">
        <el-menu :default-active="activeMenu" class="admin-menu">
          <el-menu-item index="stats" @click="activeMenu = 'stats'">
            <template #icon><span>📊</span></template>
            数据统计
          </el-menu-item>
          <el-menu-item index="users" @click="activeMenu = 'users'">
            <template #icon><span>👥</span></template>
            用户管理
          </el-menu-item>
          <el-menu-item index="articles" @click="activeMenu = 'articles'">
            <template #icon><span>📝</span></template>
            文章管理
          </el-menu-item>
          <el-menu-item index="categories" @click="activeMenu = 'categories'">
            <template #icon><span>📁</span></template>
            分类管理
          </el-menu-item>
          <el-menu-item index="tags" @click="activeMenu = 'tags'">
            <template #icon><span>🏷️</span></template>
            标签管理
          </el-menu-item>
          <el-menu-item index="tools" @click="goToTools">
            <template #icon><span>🔧</span></template>
            管理工具
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-main class="admin-content">
        <div v-if="activeMenu === 'stats'" class="stats-panel">
          <h2>📊 数据统计</h2>
          
          <div class="stats-overview">
            <div class="stat-card highlight">
              <div class="stat-icon">👥</div>
              <div class="stat-value">{{ stats.totalUsers || 0 }}</div>
              <div class="stat-label">总用户数</div>
            </div>
            <div class="stat-card highlight">
              <div class="stat-icon">📝</div>
              <div class="stat-value">{{ stats.totalArticles || 0 }}</div>
              <div class="stat-label">总文章数</div>
            </div>
            <div class="stat-card highlight">
              <div class="stat-icon">💬</div>
              <div class="stat-value">{{ stats.totalComments || 0 }}</div>
              <div class="stat-label">总评论数</div>
            </div>
            <div class="stat-card highlight">
              <div class="stat-icon">❤️</div>
              <div class="stat-value">{{ stats.totalLikes || 0 }}</div>
              <div class="stat-label">总点赞数</div>
            </div>
            <div class="stat-card highlight">
              <div class="stat-icon">⭐</div>
              <div class="stat-value">{{ stats.totalFavorites || 0 }}</div>
              <div class="stat-label">总收藏数</div>
            </div>
            <div class="stat-card highlight">
              <div class="stat-icon">🔗</div>
              <div class="stat-value">{{ stats.totalFollows || 0 }}</div>
              <div class="stat-label">总关注数</div>
            </div>
          </div>

          <div class="charts-section">
            <div class="chart-card">
              <h3>📈 用户增长趋势</h3>
              <div ref="userGrowthChart" class="chart-container"></div>
            </div>
            
            <div class="chart-card">
              <h3>📊 文章分类分布</h3>
              <div ref="categoryPieChart" class="chart-container"></div>
            </div>
          </div>

          <div class="charts-section">
            <div class="chart-card full-width">
              <h3>🔥 热门文章排行 TOP 10</h3>
              <div ref="popularArticlesChart" class="chart-container-bar"></div>
            </div>
          </div>
        </div>

        <div v-if="activeMenu === 'users'" class="users-panel">
          <h2>用户管理</h2>
          <el-table :data="users" border>
            <el-table-column prop="id" label="ID" width="80"></el-table-column>
            <el-table-column prop="username" label="用户名"></el-table-column>
            <el-table-column prop="email" label="邮箱"></el-table-column>
            <el-table-column prop="nickname" label="昵称"></el-table-column>
            <el-table-column prop="role" label="角色">
              <template #default="scope">
                <el-tag :type="scope.row.role === 'ADMIN' ? 'danger' : 'success'">
                  {{ scope.row.role === 'ADMIN' ? '管理员' : '普通用户' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="注册时间">
              <template #default="scope">
                {{ formatDate(scope.row.createdAt) }}
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template #default="scope">
                <el-button size="small" @click="updateRole(scope.row)">修改角色</el-button>
                <el-button size="small" type="danger" @click="deleteUser(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            :current-page="currentPage"
            :page-size="pageSize"
            :total="totalUsers"
            @current-change="handlePageChange"
          />
        </div>

        <div v-if="activeMenu === 'articles'" class="articles-panel">
          <h2>文章管理</h2>
          <el-table :data="articles" border>
            <el-table-column prop="id" label="ID" width="80"></el-table-column>
            <el-table-column prop="title" label="标题" min-width="200"></el-table-column>
            <el-table-column prop="author.nickname" label="作者">
              <template #default="scope">
                {{ scope.row.author?.nickname }}
              </template>
            </el-table-column>
            <el-table-column prop="category.name" label="分类">
              <template #default="scope">
                <el-tag v-if="scope.row.category" type="info">{{ scope.row.category.name }}</el-tag>
                <span v-else class="text-gray">未分类</span>
              </template>
            </el-table-column>
            <el-table-column prop="viewCount" label="浏览量" width="100"></el-table-column>
            <el-table-column prop="likeCount" label="点赞数" width="100"></el-table-column>
            <el-table-column prop="commentCount" label="评论数" width="100"></el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.published ? 'success' : 'warning'">
                  {{ scope.row.published ? '已发布' : '草稿' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="创建时间">
              <template #default="scope">
                {{ formatDate(scope.row.createdAt) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button size="small" @click="toggleArticleStatus(scope.row)">
                  {{ scope.row.published ? '下架' : '发布' }}
                </el-button>
                <el-button size="small" type="danger" @click="deleteArticle(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            :current-page="articlePage"
            :page-size="articlePageSize"
            :total="totalArticles"
            @current-change="handleArticlePageChange"
          />
        </div>

        <div v-if="activeMenu === 'categories'" class="categories-panel">
          <h2>分类管理</h2>
          <el-button type="primary" @click="showAddCategory = true">添加分类</el-button>
          <el-table :data="categories" border>
            <el-table-column prop="id" label="ID" width="80"></el-table-column>
            <el-table-column prop="name" label="名称"></el-table-column>
            <el-table-column prop="description" label="描述"></el-table-column>
            <el-table-column prop="articleCount" label="文章数"></el-table-column>
            <el-table-column label="操作">
              <template #default="scope">
                <el-button size="small" @click="editCategory(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteCategory(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div v-if="activeMenu === 'tags'" class="tags-panel">
          <h2>标签管理</h2>
          <div class="tags-header">
            <el-input
              v-model="tagSearch"
              placeholder="搜索标签..."
              class="tag-search"
              clearable
              @input="filterTags"
            >
              <template #prefix>
                <span>🔍</span>
              </template>
            </el-input>
            <el-button type="primary" @click="testAddTag">添加标签</el-button>
          </div>
          <div class="tags-stats">
            <div class="stat-card">
              <span class="stat-icon">🏷️</span>
              <div class="stat-info">
                <span class="stat-label">总标签数</span>
                <span class="stat-value">{{ filteredTags.length }}</span>
              </div>
            </div>
            <div class="stat-card">
              <span class="stat-icon">📝</span>
              <div class="stat-info">
                <span class="stat-label">总使用次数</span>
                <span class="stat-value">{{ totalTagUsage }}</span>
              </div>
            </div>
            <div class="stat-card">
              <span class="stat-icon">🔥</span>
              <div class="stat-info">
                <span class="stat-label">最热门标签</span>
                <span class="stat-value">{{ getTopTag }}</span>
              </div>
            </div>
          </div>
          <div class="tags-grid">
            <div 
              v-for="tag in filteredTags" 
              :key="tag.id" 
              class="tag-card"
              :class="getTagClass(tag.articleCount)"
            >
              <div class="tag-header">
                <el-tag type="primary" effect="plain" class="tag-name">{{ tag.name }}</el-tag>
                <button class="tag-menu-btn" @click="showTagMenu(tag, $event)">⋮</button>
              </div>
              <div class="tag-stats">
                <span class="tag-count">📊 {{ tag.articleCount }} 篇文章</span>
                <span class="tag-percentage">使用率 {{ getTagPercentage(tag.articleCount) }}%</span>
              </div>
              <div class="tag-progress">
                <el-progress
                  :percentage="getTagPercentage(tag.articleCount)"
                  :color="getTagColor(tag.articleCount)"
                  :stroke-width="6"
                />
              </div>
              <div class="tag-actions">
                <el-button size="mini" @click="editTag(tag)">编辑</el-button>
                <el-button size="mini" type="danger" @click="deleteTag(tag)">删除</el-button>
              </div>
            </div>
          </div>
          <div v-if="filteredTags.length === 0" class="empty-state">
            <p>暂无标签</p>
            <el-button type="primary" @click="showAddTag = true">添加第一个标签</el-button>
          </div>
        </div>
      </el-main>
    </el-container>

    <el-dialog title="添加分类" :visible.sync="showAddCategory">
      <el-form>
        <el-form-item label="名称">
          <el-input v-model="categoryForm.name"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="categoryForm.description"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddCategory = false">取消</el-button>
        <el-button type="primary" @click="addCategory">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog title="添加标签" :visible.sync="showAddTag">
      <el-form>
        <el-form-item label="名称">
          <el-input v-model="tagForm.name"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddTag = false">取消</el-button>
        <el-button type="primary" @click="addTag">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { adminAPI, categoryAPI, tagAPI } from '../api'
import * as echarts from 'echarts'

const activeMenu = ref('stats')
const stats = ref({})
const users = ref([])
const articles = ref([])
const categories = ref([])
const tags = ref([])
const filteredTags = ref([])
const tagSearch = ref('')
const totalTagUsage = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const totalUsers = ref(0)
const articlePage = ref(1)
const articlePageSize = ref(10)
const totalArticles = ref(0)

const showAddCategory = ref(false)
const categoryForm = ref({ name: '', description: '' })

const showAddTag = ref(false)
const tagForm = ref({ name: '' })

const userGrowthChart = ref(null)
const categoryPieChart = ref(null)
const popularArticlesChart = ref(null)

let userGrowthChartInstance = null
let categoryPieChartInstance = null
let popularArticlesChartInstance = null

const initCharts = () => {
  if (activeMenu.value !== 'stats') return
  
  setTimeout(() => {
    initUserGrowthChart()
    initCategoryPieChart()
    initPopularArticlesChart()
  }, 100)
}

const initUserGrowthChart = () => {
  const chartDom = userGrowthChart.value
  if (!chartDom) return
  
  if (userGrowthChartInstance) {
    userGrowthChartInstance.dispose()
  }
  
  userGrowthChartInstance = echarts.init(chartDom)
  const months = ['1月', '2月', '3月', '4月', '5月', '6月']
  const userData = [12, 25, 38, 52, 68, 85]
  const articleData = [5, 15, 28, 45, 62, 80]
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      }
    },
    legend: {
      data: ['用户数', '文章数'],
      bottom: 0
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: months
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '用户数',
        type: 'line',
        smooth: true,
        data: userData,
        itemStyle: {
          color: '#667eea'
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(102, 126, 234, 0.3)' },
            { offset: 1, color: 'rgba(102, 126, 234, 0.05)' }
          ])
        }
      },
      {
        name: '文章数',
        type: 'line',
        smooth: true,
        data: articleData,
        itemStyle: {
          color: '#764ba2'
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(118, 75, 162, 0.3)' },
            { offset: 1, color: 'rgba(118, 75, 162, 0.05)' }
          ])
        }
      }
    ]
  }
  
  userGrowthChartInstance.setOption(option)
}

const initCategoryPieChart = () => {
  const chartDom = categoryPieChart.value
  if (!chartDom) return
  
  if (categoryPieChartInstance) {
    categoryPieChartInstance.dispose()
  }
  
  categoryPieChartInstance = echarts.init(chartDom)
  
  const categoryData = categories.value.length > 0 
    ? categories.value.map((cat, index) => ({
        name: cat.name,
        value: Math.floor(Math.random() * 50) + 10
      }))
    : [
        { name: '技术', value: 45 },
        { name: '生活', value: 30 },
        { name: '读书', value: 25 },
        { name: '旅行', value: 20 },
        { name: '美食', value: 15 },
        { name: '其他', value: 25 }
      ]
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      right: '5%',
      top: 'center',
      textStyle: {
        fontSize: 12
      }
    },
    series: [
      {
        name: '文章分类',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['35%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: categoryData,
        color: ['#667eea', '#764ba2', '#f093fb', '#f5576c', '#4facfe', '#00f2fe', '#43e97b', '#38f9d7']
      }
    ]
  }
  
  categoryPieChartInstance.setOption(option)
}

const initPopularArticlesChart = () => {
  const chartDom = popularArticlesChart.value
  if (!chartDom) return
  
  if (popularArticlesChartInstance) {
    popularArticlesChartInstance.dispose()
  }
  
  popularArticlesChartInstance = echarts.init(chartDom)
  
  const topArticles = articles.value.slice(0, 10).map((article, index) => ({
    name: article.title ? article.title.substring(0, 15) + '...' : `文章${index + 1}`,
    value: article.likeCount || Math.floor(Math.random() * 100)
  }))
  
  if (topArticles.length === 0) {
    topArticles.push(
      { name: 'Vue3实战教程', value: 156 },
      { name: 'Spring Boot入门', value: 132 },
      { name: 'Python数据分析', value: 98 },
      { name: 'React组件开发', value: 87 },
      { name: 'Docker容器化', value: 76 },
      { name: 'MySQL优化技巧', value: 65 },
      { name: 'Java并发编程', value: 54 },
      { name: '前端工程化', value: 48 },
      { name: 'Redis缓存', value: 42 },
      { name: 'Git使用指南', value: 38 }
    )
  }
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value'
    },
    yAxis: {
      type: 'category',
      data: topArticles.map(item => item.name).reverse(),
      axisLabel: {
        interval: 0,
        fontSize: 11
      }
    },
    series: [
      {
        name: '点赞数',
        type: 'bar',
        data: topArticles.map(item => item.value).reverse(),
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
            { offset: 0, color: '#667eea' },
            { offset: 1, color: '#764ba2' }
          ]),
          borderRadius: [0, 4, 4, 0]
        },
        label: {
          show: true,
          position: 'right',
          formatter: '{c}'
        }
      }
    ]
  }
  
  popularArticlesChartInstance.setOption(option)
}

watch(activeMenu, (newVal) => {
  if (newVal === 'stats') {
    initCharts()
  }
})

watch(categories, () => {
  if (activeMenu.value === 'stats') {
    setTimeout(() => {
      initCategoryPieChart()
    }, 100)
  }
})

watch(articles, () => {
  if (activeMenu.value === 'stats') {
    setTimeout(() => {
      initPopularArticlesChart()
    }, 100)
  }
})

onMounted(() => {
  loadStats()
  loadUsers()
  loadArticles()
  loadCategories()
  loadTags()
  initCharts()
})

const loadStats = async () => {
  try {
    const response = await adminAPI.getStats()
    if (response.success) {
      stats.value = response.data
    }
  } catch (error) {
    console.error('Failed to load stats:', error)
  }
}

const loadUsers = async () => {
  try {
    const response = await adminAPI.getAllUsers({ page: currentPage.value - 1, size: pageSize.value })
    if (response.success) {
      users.value = response.data.content
      totalUsers.value = response.data.totalElements
    }
  } catch (error) {
    console.error('Failed to load users:', error)
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

const goToTools = () => {
  window.location.href = '/admin/tools'
}

const loadTags = async () => {
  try {
    const response = await tagAPI.getAll()
    if (response.success) {
      tags.value = response.data
      filteredTags.value = response.data
      totalTagUsage.value = response.data.reduce((sum, tag) => sum + tag.articleCount, 0)
    }
  } catch (error) {
    console.error('Failed to load tags:', error)
  }
}

const filterTags = () => {
  const search = tagSearch.value.toLowerCase().trim()
  if (!search) {
    filteredTags.value = tags.value
  } else {
    filteredTags.value = tags.value.filter(tag =>
      tag.name.toLowerCase().includes(search)
    )
  }
}

const getTagPercentage = (count) => {
  if (totalTagUsage.value === 0) return 0
  return Math.round((count / totalTagUsage.value) * 100)
}

const getTagColor = (count) => {
  const percentage = getTagPercentage(count)
  if (percentage >= 30) return '#67C23A'
  if (percentage >= 15) return '#409EFF'
  if (percentage >= 5) return '#E6A23C'
  return '#909399'
}

const getTagClass = (count) => {
  const percentage = getTagPercentage(count)
  if (percentage >= 30) return 'hot'
  if (percentage >= 15) return 'medium'
  if (percentage >= 5) return 'normal'
  return 'cold'
}

const getTopTag = computed(() => {
  if (filteredTags.value.length === 0) return '暂无'
  const top = filteredTags.value.reduce((prev, curr) => 
    curr.articleCount > prev.articleCount ? curr : prev
  )
  return top.name
})

const editTag = (tag) => {
  const newName = prompt('输入新标签名称:', tag.name)
  if (newName && newName.trim() && newName !== tag.name) {
    tagAPI.update(tag.id, { name: newName.trim() }).then(() => {
      tag.name = newName.trim()
      filterTags()
    }).catch(() => {
      alert('更新失败，标签名称可能已存在')
    })
  }
}

const loadArticles = async () => {
  try {
    const response = await adminAPI.getAllArticles({ page: articlePage.value - 1, size: articlePageSize.value })
    if (response.success) {
      articles.value = response.data.content
      totalArticles.value = response.data.totalElements
    }
  } catch (error) {
    console.error('Failed to load articles:', error)
  }
}

const handleArticlePageChange = (page) => {
  articlePage.value = page
  loadArticles()
}

const toggleArticleStatus = (article) => {
  const newStatus = !article.published
  if (confirm(`确定将文章 "${article.title}" ${newStatus ? '发布' : '下架'}？`)) {
    adminAPI.updateArticleStatus(article.id, { published: newStatus }).then(() => {
      article.published = newStatus
    }).catch(() => {
      alert('操作失败')
    })
  }
}

const deleteArticle = (articleId) => {
  if (confirm('确定删除该文章？此操作无法撤销。')) {
    adminAPI.deleteArticle(articleId).then(() => {
      loadArticles()
      alert('删除成功')
    }).catch(() => {
      alert('删除失败')
    })
  }
}

const handlePageChange = (page) => {
  currentPage.value = page
  loadUsers()
}

const updateRole = (user) => {
  const newRole = user.role === 'ADMIN' ? 'USER' : 'ADMIN'
  if (confirm(`确定将用户 ${user.username} 的角色改为 ${newRole === 'ADMIN' ? '管理员' : '普通用户'}？`)) {
    adminAPI.updateUserRole(user.id, { role: newRole }).then(() => {
      user.role = newRole
    })
  }
}

const deleteUser = (userId) => {
  if (confirm('确定删除该用户？')) {
    adminAPI.deleteUser(userId).then(() => {
      loadUsers()
    })
  }
}

const addCategory = async () => {
  if (!categoryForm.value.name.trim()) {
    alert('请输入分类名称')
    return
  }
  
  try {
    await categoryAPI.create({ name: categoryForm.value.name, description: categoryForm.value.description })
    showAddCategory.value = false
    categoryForm.value = { name: '', description: '' }
    loadCategories()
  } catch (error) {
    alert('添加失败')
  }
}

const editCategory = (category) => {
  const newName = prompt('输入新名称:', category.name)
  if (newName && newName.trim()) {
    categoryAPI.update(category.id, { name: newName }).then(() => {
      category.name = newName
    })
  }
}

const deleteCategory = (categoryId) => {
  if (confirm('确定删除该分类？')) {
    categoryAPI.delete(categoryId).then(() => {
      loadCategories()
    }).catch(() => {
      alert('删除失败，该分类下存在文章')
    })
  }
}

const testAddTag = () => {
  const tagName = prompt('请输入标签名称:')
  if (tagName && tagName.trim()) {
    addTagDirectly(tagName.trim())
  }
}

const addTagDirectly = async (name) => {
  try {
    console.log('准备添加标签:', name)
    const response = await tagAPI.create({ name })
    console.log('添加标签响应:', response)
    if (response.success) {
      alert('标签 "' + name + '" 添加成功！')
      loadTags()
    } else {
      alert('添加失败：' + (response.message || '未知错误'))
    }
  } catch (error) {
    console.error('添加标签失败:', error)
    alert('添加失败，请检查控制台错误信息')
  }
}

const addTag = async () => {
  if (!tagForm.value.name.trim()) {
    alert('请输入标签名称')
    return
  }
  
  try {
    console.log('准备添加标签:', tagForm.value.name)
    const response = await tagAPI.create({ name: tagForm.value.name })
    console.log('添加标签响应:', response)
    if (response.success) {
      alert('标签添加成功！')
      showAddTag.value = false
      tagForm.value = { name: '' }
      loadTags()
    } else {
      alert('添加失败：' + (response.message || '未知错误'))
    }
  } catch (error) {
    console.error('添加标签失败:', error)
    alert('添加失败，请检查控制台错误信息')
  }
}

const deleteTag = (tag) => {
  if (confirm(`确定删除标签 "${tag.name}" 吗？`)) {
    tagAPI.delete(tag.id).then(() => {
      loadTags()
      alert('删除成功')
    }).catch(() => {
      alert('删除失败，该标签下存在文章')
    })
  }
}

const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}
</script>

<style scoped>
.admin-page {
  min-height: 100vh;
}

.admin-content {
  padding: 20px;
}

.stats-overview {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 20px;
  margin: 20px 0;
}

.stat-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 12px;
  padding: 24px 20px;
  text-align: center;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.stat-icon {
  font-size: 2.5rem;
  margin-bottom: 12px;
}

.stat-value {
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 1rem;
  opacity: 0.95;
}

.charts-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(450px, 1fr));
  gap: 24px;
  margin: 30px 0;
}

.chart-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.chart-card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
}

.chart-card h3 {
  font-size: 1.2rem;
  margin-bottom: 20px;
  color: #303133;
  border-bottom: 2px solid #667eea;
  padding-bottom: 10px;
}

.chart-container {
  width: 100%;
  height: 350px;
}

.chart-container-bar {
  width: 100%;
  height: 400px;
}

.full-width {
  grid-column: 1 / -1;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.stat-value {
  font-size: 2rem;
  font-weight: bold;
}

.stat-label {
  font-size: 0.9rem;
  opacity: 0.9;
}

.tags-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  gap: 10px;
}

.tag-search {
  width: 300px;
}

.tags-stats {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 8px;
}

.stat-item {
  font-size: 14px;
  color: #606266;
}

.stat-item strong {
  color: #409EFF;
  font-size: 16px;
}

.text-gray {
  color: #909399;
}

.tags-stats {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.tags-stats .stat-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 20px;
  background: #f5f7fa;
  border-radius: 8px;
}

.tags-stats .stat-icon {
  font-size: 24px;
}

.tags-stats .stat-info {
  display: flex;
  flex-direction: column;
}

.tags-stats .stat-label {
  font-size: 12px;
  color: #909399;
}

.tags-stats .stat-value {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.tags-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}

.tag-card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
  border-left: 4px solid #e4e7ed;
}

.tag-card.hot {
  border-left-color: #67C23A;
  background: linear-gradient(135deg, #f0fff4 0%, #fff 100%);
}

.tag-card.medium {
  border-left-color: #409EFF;
  background: linear-gradient(135deg, #f0f5ff 0%, #fff 100%);
}

.tag-card.normal {
  border-left-color: #E6A23C;
  background: linear-gradient(135deg, #fffbe6 0%, #fff 100%);
}

.tag-card.cold {
  border-left-color: #909399;
}

.tag-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.tag-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.tag-name {
  font-size: 16px;
  padding: 4px 12px;
}

.tag-menu-btn {
  background: none;
  border: none;
  font-size: 18px;
  color: #909399;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
}

.tag-menu-btn:hover {
  background: #f5f7fa;
}

.tag-stats {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
  font-size: 13px;
  color: #606266;
}

.tag-count {
  display: flex;
  align-items: center;
  gap: 4px;
}

.tag-percentage {
  color: #409EFF;
  font-weight: 500;
}

.tag-progress {
  margin-bottom: 12px;
}

.tag-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: #f5f7fa;
  border-radius: 12px;
}

.empty-state p {
  margin-bottom: 16px;
  color: #909399;
}
</style>