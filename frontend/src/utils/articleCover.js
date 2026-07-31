const availableImages = [
  'spring-boot-programming.jpg',
  'vuejs-frontend.jpg',
  'java-concurrency-programming.jpg',
  'docker-container-deployment.jpg',
  'redis-cache-database.jpg',
  'mysql-database-optimization.jpg',
  'cozy-home-weekend.jpg',
  'minimalist-lifestyle-simple.jpg',
  'reading-habit-cultivation.jpg',
  'minimalism-declutter.jpg',
  'yunnan-dali-lake-mountain.jpg',
  'tibet-lhasa-potala-palace.jpg',
  'japan-kyoto-temple.jpg',
  'xinjiang-yili-grassland.jpg',
  'chinese-braised-pork-belly.jpg',
  'homemade-pizza-delicious.jpg',
  'japanese-ramen-noodle.jpg',
  'chiffon-cake-baking.jpg',
  'science-fiction-three-body-universe.jpg',
  'life-philosophy-farming-old-man.jpg',
  'little-prince-stars-rose.jpg',
  'magic-realism-book.jpg',
  'landscape-photography-camera.jpg',
  'portrait-photography-lighting.jpg',
  'fitness-workout-gym.jpg',
  'yoga-meditation-health.jpg',
  'guitar-music-learning.jpg',
  'music-composition-creative.jpg',
  'artificial-intelligence-future.jpg',
  'metaverse-virtual-world.jpg',
  'career-newbie-guide.jpg',
  'time-management-productivity.jpg'
]

const categoryImages = {
  '技术': 'spring-boot-programming.jpg',
  '生活': 'cozy-home-weekend.jpg',
  '旅行': 'yunnan-dali-lake-mountain.jpg',
  '美食': 'chinese-braised-pork-belly.jpg',
  '读书': 'life-philosophy-farming-old-man.jpg',
  '摄影': 'landscape-photography-camera.jpg',
  '健康': 'fitness-workout-gym.jpg',
  '音乐': 'guitar-music-learning.jpg',
  '科技': 'artificial-intelligence-future.jpg',
  '职场': 'career-newbie-guide.jpg'
}

const titleKeywords = {
  'Spring Boot': 'spring-boot-programming.jpg',
  'Vue3': 'vuejs-frontend.jpg',
  'Java 并发': 'java-concurrency-programming.jpg',
  'Docker': 'docker-container-deployment.jpg',
  'Redis': 'redis-cache-database.jpg',
  'MySQL': 'mysql-database-optimization.jpg',
  '周末宅家': 'cozy-home-weekend.jpg',
  '极简生活': 'minimalist-lifestyle-simple.jpg',
  '阅读习惯': 'reading-habit-cultivation.jpg',
  '断舍离': 'minimalism-declutter.jpg',
  '云南大理': 'yunnan-dali-lake-mountain.jpg',
  '西藏拉萨': 'tibet-lhasa-potala-palace.jpg',
  '日本京都': 'japan-kyoto-temple.jpg',
  '新疆伊犁': 'xinjiang-yili-grassland.jpg',
  '红烧肉': 'chinese-braised-pork-belly.jpg',
  '披萨': 'homemade-pizza-delicious.jpg',
  '日式拉面': 'japanese-ramen-noodle.jpg',
  '戚风蛋糕': 'chiffon-cake-baking.jpg',
  '三体': 'science-fiction-three-body-universe.jpg',
  '活着': 'life-philosophy-farming-old-man.jpg',
  '小王子': 'little-prince-stars-rose.jpg',
  '百年孤独': 'magic-realism-book.jpg',
  '风光摄影': 'landscape-photography-camera.jpg',
  '人像摄影': 'portrait-photography-lighting.jpg',
  '健身入门': 'fitness-workout-gym.jpg',
  '瑜伽入门': 'yoga-meditation-health.jpg',
  '吉他入门': 'guitar-music-learning.jpg',
  '音乐创作': 'music-composition-creative.jpg',
  '人工智能': 'artificial-intelligence-future.jpg',
  '元宇宙': 'metaverse-virtual-world.jpg',
  '职场新人': 'career-newbie-guide.jpg',
  '时间管理': 'time-management-productivity.jpg'
}

export const getArticleCover = (article) => {
  if (!article) {
    return null
  }
  
  if (article.coverImage) {
    return article.coverImage
  }
  
  return null
}
