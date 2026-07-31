package com.example.blogcommunity.config;

import com.example.blogcommunity.entity.Article;
import com.example.blogcommunity.entity.Category;
import com.example.blogcommunity.entity.Comment;
import com.example.blogcommunity.entity.User;
import com.example.blogcommunity.repository.ArticleRepository;
import com.example.blogcommunity.repository.CategoryRepository;
import com.example.blogcommunity.repository.CommentRepository;
import com.example.blogcommunity.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, CategoryRepository categoryRepository,
                          ArticleRepository articleRepository, CommentRepository commentRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        initUsers();
        initCategories();
        initArticles();
        initComments();
    }

    private void initUsers() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin@example.com")
                    .nickname("管理员")
                    .avatar("https://api.dicebear.com/7.x/avataaars/svg?seed=admin")
                    .bio("系统管理员，热爱技术，热爱生活")
                    .role("ADMIN")
                    .enabled(true)
                    .build();
            userRepository.save(admin);
        }

        if (!userRepository.existsByUsername("user1")) {
            User user1 = User.builder()
                    .username("user1")
                    .password(passwordEncoder.encode("123456"))
                    .email("user1@example.com")
                    .nickname("技术达人")
                    .avatar("https://api.dicebear.com/7.x/avataaars/svg?seed=user1")
                    .bio("全栈开发者，专注于 Java 和 Vue.js，热爱分享技术心得")
                    .role("USER")
                    .enabled(true)
                    .build();
            userRepository.save(user1);
        }

        if (!userRepository.existsByUsername("user2")) {
            User user2 = User.builder()
                    .username("user2")
                    .password(passwordEncoder.encode("123456"))
                    .email("user2@example.com")
                    .nickname("生活博主")
                    .avatar("https://api.dicebear.com/7.x/avataaars/svg?seed=user2")
                    .bio("记录生活点滴，分享生活感悟，倡导极简生活方式")
                    .role("USER")
                    .enabled(true)
                    .build();
            userRepository.save(user2);
        }

        if (!userRepository.existsByUsername("user3")) {
            User user3 = User.builder()
                    .username("user3")
                    .password(passwordEncoder.encode("123456"))
                    .email("user3@example.com")
                    .nickname("旅行家")
                    .avatar("https://api.dicebear.com/7.x/avataaars/svg?seed=user3")
                    .bio("环游世界的旅行者，用相机记录旅途美景")
                    .role("USER")
                    .enabled(true)
                    .build();
            userRepository.save(user3);
        }

        if (!userRepository.existsByUsername("user4")) {
            User user4 = User.builder()
                    .username("user4")
                    .password(passwordEncoder.encode("123456"))
                    .email("user4@example.com")
                    .nickname("美食家")
                    .avatar("https://api.dicebear.com/7.x/avataaars/svg?seed=user4")
                    .bio("热爱美食，喜欢研究各种菜谱，分享烹饪心得")
                    .role("USER")
                    .enabled(true)
                    .build();
            userRepository.save(user4);
        }

        if (!userRepository.existsByUsername("user5")) {
            User user5 = User.builder()
                    .username("user5")
                    .password(passwordEncoder.encode("123456"))
                    .email("user5@example.com")
                    .nickname("书虫")
                    .avatar("https://api.dicebear.com/7.x/avataaars/svg?seed=user5")
                    .bio("热爱阅读，每月阅读5本书，分享读书感悟")
                    .role("USER")
                    .enabled(true)
                    .build();
            userRepository.save(user5);
        }

        if (!userRepository.existsByUsername("user6")) {
            User user6 = User.builder()
                    .username("user6")
                    .password(passwordEncoder.encode("123456"))
                    .email("user6@example.com")
                    .nickname("摄影师")
                    .avatar("https://api.dicebear.com/7.x/avataaars/svg?seed=user6")
                    .bio("用镜头记录生活，热爱自然风光摄影")
                    .role("USER")
                    .enabled(true)
                    .build();
            userRepository.save(user6);
        }

        if (!userRepository.existsByUsername("user7")) {
            User user7 = User.builder()
                    .username("user7")
                    .password(passwordEncoder.encode("123456"))
                    .email("user7@example.com")
                    .nickname("健身达人")
                    .avatar("https://api.dicebear.com/7.x/avataaars/svg?seed=user7")
                    .bio("坚持健身5年，分享健康生活方式")
                    .role("USER")
                    .enabled(true)
                    .build();
            userRepository.save(user7);
        }

        if (!userRepository.existsByUsername("user8")) {
            User user8 = User.builder()
                    .username("user8")
                    .password(passwordEncoder.encode("123456"))
                    .email("user8@example.com")
                    .nickname("音乐人")
                    .avatar("https://api.dicebear.com/7.x/avataaars/svg?seed=user8")
                    .bio("热爱音乐创作，吉他手，乐队主唱")
                    .role("USER")
                    .enabled(true)
                    .build();
            userRepository.save(user8);
        }
    }

    private void initCategories() {
        if (categoryRepository.count() == 0) {
            List<Category> categories = Arrays.asList(
                    Category.builder().name("技术").description("技术分享与编程心得，包括Java、前端、数据库等技术话题").articleCount(0).build(),
                    Category.builder().name("生活").description("生活感悟与日常分享，记录生活点滴").articleCount(0).build(),
                    Category.builder().name("读书").description("读书笔记与推荐，分享阅读感悟").articleCount(0).build(),
                    Category.builder().name("旅行").description("旅行见闻与攻略，记录旅途美好").articleCount(0).build(),
                    Category.builder().name("美食").description("美食制作与推荐，分享烹饪心得").articleCount(0).build(),
                    Category.builder().name("摄影").description("摄影技巧分享，展示优秀作品").articleCount(0).build(),
                    Category.builder().name("健康").description("健康养生知识，分享健身经验").articleCount(0).build(),
                    Category.builder().name("音乐").description("音乐欣赏与创作，分享音乐感悟").articleCount(0).build(),
                    Category.builder().name("科技").description("科技资讯与产品评测").articleCount(0).build(),
                    Category.builder().name("职场").description("职场经验分享，职业发展建议").articleCount(0).build()
            );
            categoryRepository.saveAll(categories);
        }
    }

    private void initArticles() {
        if (articleRepository.count() > 0) {
            return;
        }

        User user1 = userRepository.findByUsername("user1").orElse(null);
        User user2 = userRepository.findByUsername("user2").orElse(null);
        User user3 = userRepository.findByUsername("user3").orElse(null);
        User user4 = userRepository.findByUsername("user4").orElse(null);
        User user5 = userRepository.findByUsername("user5").orElse(null);
        User user6 = userRepository.findByUsername("user6").orElse(null);
        User user7 = userRepository.findByUsername("user7").orElse(null);
        User user8 = userRepository.findByUsername("user8").orElse(null);
        
        Category tech = categoryRepository.findByName("技术").orElse(null);
        Category life = categoryRepository.findByName("生活").orElse(null);
        Category reading = categoryRepository.findByName("读书").orElse(null);
        Category travel = categoryRepository.findByName("旅行").orElse(null);
        Category food = categoryRepository.findByName("美食").orElse(null);
        Category photography = categoryRepository.findByName("摄影").orElse(null);
        Category health = categoryRepository.findByName("健康").orElse(null);
        Category music = categoryRepository.findByName("音乐").orElse(null);
        Category science = categoryRepository.findByName("科技").orElse(null);
        Category career = categoryRepository.findByName("职场").orElse(null);

        if (user1 != null && tech != null) {
            Article a1 = Article.builder()
                    .title("Spring Boot 入门指南")
                    .content("## Spring Boot 入门指南\n\n欢迎来到 Spring Boot 的世界！作为 Java 开发者，Spring Boot 是你必须掌握的框架之一。\n\n### 什么是 Spring Boot\n\nSpring Boot 是由 Pivotal 团队开发的框架，旨在简化 Spring 应用的创建和部署过程。它通过自动配置机制，让开发者可以专注于业务逻辑而不是繁琐的配置。\n\n### 主要特点\n\n1. **自动配置**：根据项目依赖自动配置 Spring 应用，无需手动配置 XML\n2. **约定优于配置**：提供合理的默认配置，减少配置文件\n3. **嵌入式服务器**：内置 Tomcat/Jetty，一键启动\n4. **生产就绪**：提供健康检查、监控、指标等生产级功能\n\n### 快速开始\n\n**环境要求**：\n- JDK 8 或更高版本\n- Maven 3.6+ 或 Gradle 6+\n\n**创建项目**：\n\n1. 使用 Spring Initializr 创建项目\n2. 添加所需依赖（Web、JPA、Security 等）\n3. 编写主启动类\n4. 运行项目\n\n### Hello World 示例\n\n```java\n@SpringBootApplication\n@RestController\npublic class Application {\n    public static void main(String[] args) {\n        SpringApplication.run(Application.class, args);\n    }\n    \n    @GetMapping(\"/\")\n    public String hello() {\n        return \"Hello Spring Boot!\";\n    }\n}\n```\n\n### 常用注解\n\n- `@SpringBootApplication`: 组合注解，包含配置、扫描、自动配置\n- `@RestController`: REST 控制器\n- `@GetMapping/@PostMapping`: 请求映射\n- `@Autowired`: 依赖注入\n- `@Service/@Repository`: 服务层和数据层标记\n\n### 配置文件\n\n支持 properties 和 yaml 两种格式：\n\n```yaml\nserver:\n  port: 8080\nspring:\n  datasource:\n    url: jdbc:mysql://localhost:3306/example\n    username: root\n    password: password\n```\n\n### 数据访问\n\nSpring Boot 支持多种数据访问方式：\n- Spring Data JPA\n- Spring JDBC\n- Spring Data Redis\n- MongoDB\n\n### 安全配置\n\nSpring Security 集成：\n```java\n@Configuration\npublic class SecurityConfig extends WebSecurityConfigurerAdapter {\n    @Override\n    protected void configure(HttpSecurity http) throws Exception {\n        http.authorizeRequests()\n            .antMatchers(\"/public/**\").permitAll()\n            .anyRequest().authenticated();\n    }\n}\n```\n\n### 总结\n\nSpring Boot 让 Java Web 开发变得简单高效，是现代企业级应用的首选框架。")
                    .summary("本文详细介绍了 Spring Boot 的基本概念、主要特点、快速入门方法以及常用注解和配置，适合 Java 开发者学习。")
                    .user(user1)
                    .category(tech)
                    .coverImage("/images/articles/spring-boot-programming.jpg")
                    .viewCount(5623)
                    .likeCount(456)
                    .commentCount(89)
                    .published(true)
                    .build();
            articleRepository.save(a1);

            Article a2 = Article.builder()
                    .title("Vue3 组合式 API 详解")
                    .content("## Vue3 组合式 API 详解\n\nVue3 引入的组合式 API 是前端开发的一大革新，让代码组织更加灵活高效。\n\n### 组合式 API 的优势\n\n1. **更好的逻辑复用**：将相关逻辑提取到可复用的函数中\n2. **更灵活的代码组织**：按功能组织代码，而非按选项类型\n3. **更好的类型推断**：对 TypeScript 更友好\n4. **更小的生产包体积**：Tree-shaking 更加高效\n\n### ref 和 reactive\n\n**ref** 用于创建响应式的基本类型数据：\n\n```javascript\nimport { ref } from 'vue'\n\nconst count = ref(0)\nconst name = ref('Vue3')\n\n// 访问时需要 .value\nconsole.log(count.value) // 0\n```\n\n**reactive** 用于创建响应式对象：\n\n```javascript\nimport { reactive } from 'vue'\n\nconst state = reactive({\n  name: 'Vue3',\n  version: '3.4.0',\n  features: ['Composition API', 'Teleport', 'Suspense']\n})\n\n// 直接访问\nconsole.log(state.name) // 'Vue3'\n```\n\n### computed 和 watch\n\n**computed** 计算属性：\n\n```javascript\nconst firstName = ref('张')\nconst lastName = ref('三')\nconst fullName = computed(() => firstName.value + lastName.value)\n```\n\n**watch** 监听器：\n\n```javascript\nwatch(count, (newVal, oldVal) => {\n  console.log(`count changed from ${oldVal} to ${newVal}`)\n})\n```\n\n### setup 函数\n\nsetup 是组合式 API 的入口：\n\n```javascript\nimport { ref, onMounted, onUnmounted } from 'vue'\n\nexport default {\n  setup() {\n    const count = ref(0)\n    \n    const increment = () => {\n      count.value++\n    }\n    \n    onMounted(() => {\n      console.log('Component mounted')\n    })\n    \n    return { count, increment }\n  }\n}\n```\n\n### 响应式工具函数\n\n- `isRef()`: 检查是否为 ref\n- `toRef()`: 将对象属性转换为 ref\n- `toRefs()`: 将对象所有属性转换为 ref\n- `shallowRef()`: 创建浅层响应式 ref\n- `readonly()`: 创建只读响应式对象\n\n### 组合式函数\n\n创建可复用的组合式函数：\n\n```javascript\nimport { ref, onMounted, onUnmounted } from 'vue'\n\nexport function useMousePosition() {\n  const x = ref(0)\n  const y = ref(0)\n  \n  function update(event) {\n    x.value = event.pageX\n    y.value = event.pageY\n  }\n  \n  onMounted(() => {\n    window.addEventListener('mousemove', update)\n  })\n  \n  onUnmounted(() => {\n    window.removeEventListener('mousemove', update)\n  })\n  \n  return { x, y }\n}\n```\n\n### 总结\n\n组合式 API 为 Vue3 带来了更现代的开发体验，是学习 Vue3 的核心内容。")
                    .summary("深入理解 Vue3 组合式 API 的使用方式，包括 ref、reactive、computed、watch 等核心概念，附详细代码示例。")
                    .user(user1)
                    .category(tech)
                    .coverImage("/images/articles/vuejs-frontend.jpg")
                    .viewCount(4321)
                    .likeCount(345)
                    .commentCount(76)
                    .published(true)
                    .build();
            articleRepository.save(a2);

            Article a3 = Article.builder()
                    .title("Java 并发编程实战")
                    .content("## Java 并发编程实战\n\n并发编程是 Java 开发中不可或缺的技能，掌握并发编程能显著提升程序性能。\n\n### 为什么需要并发\n\n1. **提高性能**：充分利用多核 CPU\n2. **提升响应性**：避免阻塞主线程\n3. **资源利用率**：等待 IO 时处理其他任务\n\n### 线程创建方式\n\n**方式一：继承 Thread**\n\n```java\nclass MyThread extends Thread {\n    @Override\n    public void run() {\n        System.out.println(\"Thread running\");\n    }\n}\n```\n\n**方式二：实现 Runnable**\n\n```java\nclass MyRunnable implements Runnable {\n    @Override\n    public void run() {\n        System.out.println(\"Runnable running\");\n    }\n}\n```\n\n**方式三：Lambda 表达式**\n\n```java\nThread thread = new Thread(() -> {\n    System.out.println(\"Lambda thread\");\n});\n```\n\n### 线程安全\n\n**synchronized 关键字**：\n\n```java\npublic synchronized void increment() {\n    count++;\n}\n```\n\n**Lock 接口**：\n\n```java\nLock lock = new ReentrantLock();\nlock.lock();\ntry {\n    count++;\n} finally {\n    lock.unlock();\n}\n```\n\n**原子类**：\n\n```java\nAtomicInteger count = new AtomicInteger(0);\ncount.incrementAndGet();\n```\n\n### 线程池\n\n```java\nExecutorService executor = Executors.newFixedThreadPool(10);\nexecutor.submit(() -> {\n    // 执行任务\n});\nexecutor.shutdown();\n```\n\n### 常用并发工具\n\n- `CountDownLatch`: 等待多个线程完成\n- `CyclicBarrier`: 同步多个线程\n- `Semaphore`: 控制并发访问数量\n- `Future`: 获取异步任务结果\n- `CompletableFuture`: 异步编程的终极方案\n\n### 并发设计模式\n\n- **生产者-消费者模式**\n- **读写锁模式**\n- **线程本地存储模式**\n- **工作线程模式**\n\n### 总结\n\n掌握 Java 并发编程是成为高级开发者的必经之路。")
                    .summary("详细介绍 Java 并发编程的基础知识，包括线程创建、线程安全、线程池等核心内容。")
                    .user(user1)
                    .category(tech)
                    .coverImage("/images/articles/java-concurrency-programming.jpg")
                    .viewCount(3876)
                    .likeCount(289)
                    .commentCount(64)
                    .published(true)
                    .build();
            articleRepository.save(a3);

            Article a4 = Article.builder()
                    .title("Docker 容器化部署完全指南")
                    .content("## Docker 容器化部署完全指南\n\nDocker 已经成为现代应用部署的标准工具，掌握 Docker 是开发者的必备技能。\n\n### Docker 基础概念\n\n- **镜像(Image)**：容器的只读模板\n- **容器(Container)**：镜像的运行实例\n- **仓库(Repository)**：存放镜像的地方\n\n### Dockerfile 编写\n\n```dockerfile\nFROM openjdk:8-jdk-alpine\nWORKDIR /app\nCOPY target/myapp.jar app.jar\nEXPOSE 8080\nENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]\n```\n\n### 常用命令\n\n```bash\n# 构建镜像\ndocker build -t myapp:1.0 .\n\n# 运行容器\ndocker run -d -p 8080:8080 myapp:1.0\n\n# 查看容器\ndocker ps\n\n# 查看日志\ndocker logs container-id\n```\n\n### Docker Compose\n\n```yaml\nversion: '3.8'\nservices:\n  web:\n    build: .\n    ports:\n      - \"8080:8080\"\n    depends_on:\n      - db\n  db:\n    image: mysql:5.7\n    environment:\n      MYSQL_ROOT_PASSWORD: password\n```\n\n### Docker 网络\n\n```bash\ndocker network create my-network\ndocker run --network my-network myapp\n```\n\n### 数据持久化\n\n```bash\ndocker run -v /host/path:/container/path myapp\n```\n\n### Docker Swarm\n\n```bash\ndocker swarm init\ndocker stack deploy -c docker-compose.yml myapp\n```\n\n### 镜像优化\n\n- 使用多阶段构建\n- 选择更小的基础镜像\n- 清理不必要的文件\n- 使用 .dockerignore\n\n### 总结\n\nDocker 简化了应用部署，是 DevOps 实践的核心工具。")
                    .summary("全面介绍 Docker 容器化部署，包括 Dockerfile 编写、Docker Compose 使用、网络配置和数据持久化。")
                    .user(user1)
                    .category(tech)
                    .coverImage("/images/articles/docker-container-deployment.jpg")
                    .viewCount(4532)
                    .likeCount(367)
                    .commentCount(82)
                    .published(true)
                    .build();
            articleRepository.save(a4);

            Article a5 = Article.builder()
                    .title("Redis 缓存实战与最佳实践")
                    .content("## Redis 缓存实战与最佳实践\n\nRedis 是一款高性能的内存数据库，广泛应用于缓存、会话管理、消息队列等场景。\n\n### Redis 基础数据结构\n\n1. **String**: 字符串类型\n2. **Hash**: 哈希表\n3. **List**: 列表\n4. **Set**: 集合\n5. **Sorted Set**: 有序集合\n\n### 常用命令\n\n```bash\n# 设置值\nSET key value\n\n# 获取值\nGET key\n\n# 设置过期时间\nSET key value EX 3600\n\n# 哈希操作\nHSET user:1 name \"John\"\nHGET user:1 name\n\n# 列表操作\nLPUSH list item\nLRANGE list 0 -1\n```\n\n### 缓存策略\n\n**缓存穿透**：\n- 使用布隆过滤器\n- 缓存空值\n\n**缓存击穿**：\n- 使用互斥锁\n- 热点数据永不过期\n\n**缓存雪崩**：\n- 过期时间随机化\n- 多级缓存\n- 集群部署\n\n### 分布式锁\n\n```java\nString lockKey = \"lock:order:\" + orderId;\nString requestId = UUID.randomUUID().toString();\n\ntry {\n    Boolean result = stringRedisTemplate.opsForValue()\n        .setIfAbsent(lockKey, requestId, 30, TimeUnit.SECONDS);\n    if (!Boolean.TRUE.equals(result)) {\n        return \"请稍后再试\";\n    }\n    // 执行业务逻辑\n} finally {\n    if (requestId.equals(stringRedisTemplate.opsForValue().get(lockKey))) {\n        stringRedisTemplate.delete(lockKey);\n    }\n}\n```\n\n### 性能优化\n\n- 使用连接池\n- 批量操作\n- 管道技术\n- Lua 脚本\n\n### 总结\n\nRedis 是构建高性能系统的必备工具，掌握其最佳实践至关重要。")
                    .summary("深入讲解 Redis 的使用技巧和最佳实践，包括缓存策略、分布式锁等核心内容。")
                    .user(user1)
                    .category(tech)
                    .coverImage("/images/articles/redis-cache-database.jpg")
                    .viewCount(3245)
                    .likeCount(234)
                    .commentCount(56)
                    .published(true)
                    .build();
            articleRepository.save(a5);

            Article a6 = Article.builder()
                    .title("MySQL 索引优化深度解析")
                    .content("## MySQL 索引优化深度解析\n\n索引是提升数据库查询性能的关键，合理使用索引可以显著提升查询效率。\n\n### 索引类型\n\n1. **B-Tree 索引**: 最常用的索引类型\n2. **Hash 索引**: 等值查询效率高\n3. **Full-Text 索引**: 全文搜索\n4. **Spatial 索引**: 空间数据索引\n\n### 创建索引\n\n```sql\nCREATE INDEX idx_name ON users(name);\nCREATE UNIQUE INDEX idx_email ON users(email);\nCREATE INDEX idx_name_age ON users(name, age);\n```\n\n### 索引使用场景\n\n**适合创建索引的场景**：\n- WHERE 条件列\n- JOIN 条件列\n- ORDER BY 列\n- GROUP BY 列\n\n**不适合创建索引的场景**：\n- 频繁更新的列\n- 基数低的列\n- 查询条件中使用函数的列\n\n### 执行计划分析\n\n```sql\nEXPLAIN SELECT * FROM users WHERE name = 'John';\n```\n\n### 索引失效场景\n\n1. 使用 `!=` 或 `<>` 操作符\n2. 使用 `NOT IN`\n3. 使用 `LIKE '%xxx'`\n4. 类型转换\n5. OR 条件中有未索引的列\n\n### 索引优化策略\n\n- 选择合适的索引类型\n- 复合索引遵循最左前缀原则\n- 定期重建索引\n- 使用覆盖索引\n\n### 总结\n\n合理的索引设计是数据库性能优化的核心。")
                    .summary("详细介绍 MySQL 索引的原理、使用场景和优化策略，帮助你提升数据库性能。")
                    .user(user1)
                    .category(tech)
                    .coverImage("/images/articles/mysql-database-optimization.jpg")
                    .viewCount(2876)
                    .likeCount(212)
                    .commentCount(48)
                    .published(true)
                    .build();
            articleRepository.save(a6);
        }

        if (user2 != null && life != null) {
            Article a7 = Article.builder()
                    .title("周末宅家的完美一天")
                    .content("## 周末宅家的完美一天\n\n周末不用上班，如何度过一个充实又惬意的一天？\n\n### 早晨时光\n\n**7:30 - 起床**\n\n睡到自然醒，拉开窗帘，让阳光洒进房间。伸个懒腰，开启美好的一天。\n\n**8:00 - 早餐**\n\n给自己做一顿丰盛的早餐：\n- 煎鸡蛋 + 烤面包\n- 一杯香浓的拿铁咖啡\n- 新鲜水果沙拉\n\n**9:00 - 阅读**\n\n坐在阳台的小桌旁，读一本喜欢的书。阳光、咖啡、好书，完美的早晨。\n\n### 午后时光\n\n**12:00 - 午餐**\n\n简单而美味的午餐，比如番茄鸡蛋面或三明治。\n\n**13:00 - 午休**\n\n小憩一会儿，恢复精力。\n\n**14:00 - 兴趣爱好**\n\n做一些自己喜欢的事情：\n- 看一部喜欢的电影\n- 练习瑜伽或冥想\n- 画画或写日记\n- 整理房间\n\n### 傍晚时光\n\n**18:00 - 晚餐**\n\n准备一顿丰盛的晚餐，可以是火锅、烤肉或者家常菜。\n\n**20:00 - 放松**\n\n泡个热水澡，喝杯热茶，看一部轻松的电视剧。\n\n**22:00 - 睡觉**\n\n早点休息，养精蓄锐，迎接新的一周。\n\n### 周末宅家小贴士\n\n1. 远离电子产品，享受独处时光\n2. 做点平时没时间做的事情\n3. 给自己做一顿大餐\n4. 整理房间，创造舒适环境\n\n### 总结\n\n有时候，宅家比出去玩更舒服。享受独处的时光，给自己充充电。")
                    .summary("分享周末宅家的完美日程安排，让你的周末既充实又惬意。")
                    .user(user2)
                    .category(life)
                    .coverImage("/images/articles/cozy-home-weekend.jpg")
                    .viewCount(4567)
                    .likeCount(345)
                    .commentCount(89)
                    .published(true)
                    .build();
            articleRepository.save(a7);

            Article a8 = Article.builder()
                    .title("极简生活方式分享")
                    .content("## 极简生活方式分享\n\n极简生活不是一无所有，而是拥有真正需要的东西。\n\n### 什么是极简生活\n\n极简主义是一种生活态度，强调：\n- 减少物质拥有\n- 关注内心需求\n- 追求品质而非数量\n- 珍惜当下\n\n### 如何开始极简生活\n\n**第一步：清理物品**\n\n从衣柜开始，清理不再穿的衣服。遵循 \"一年法则\"：一年没穿过的衣服就可以扔掉或捐赠。\n\n**第二步：简化空间**\n\n保持家居整洁，每个物品都有固定的位置。减少装饰品，让空间更加通透。\n\n**第三步：控制消费**\n\n购物前问自己：\n- 我真的需要这个吗？\n- 它能给我带来什么价值？\n- 有没有更便宜的替代品？\n\n**第四步：关注内心**\n\n减少社交媒体时间，多花时间陪伴家人朋友，培养兴趣爱好。\n\n### 极简生活的好处\n\n1. **减少压力**：少即是多，简单的生活更轻松\n2. **节省时间**：不用花太多时间整理和寻找物品\n3. **节省金钱**：减少不必要的消费\n4. **更加专注**：更少的干扰，更容易专注于重要的事情\n\n### 实践心得\n\n我实践极简生活已经一年了，生活变得更加简单、平静、充实。推荐大家也试试！\n\n### 极简生活误区\n\n- 极简不是苦行僧式的生活\n- 不是要扔掉所有东西\n- 不是追求完美\n- 是一种持续的过程\n\n### 总结\n\n极简生活是一种选择，让我们更专注于真正重要的事情。")
                    .summary("分享极简生活的理念和实践方法，让生活变得更加简单、平静、充实。")
                    .user(user2)
                    .category(life)
                    .coverImage("/images/articles/minimalist-lifestyle-simple.jpg")
                    .viewCount(3890)
                    .likeCount(278)
                    .commentCount(76)
                    .published(true)
                    .build();
            articleRepository.save(a8);

            Article a9 = Article.builder()
                    .title("如何培养阅读习惯")
                    .content("## 如何培养阅读习惯\n\n阅读是获取知识、开阔视野的最佳方式之一。如何培养良好的阅读习惯？\n\n### 设定阅读目标\n\n**明确目标**：\n- 每月读几本书\n- 每周读多少页\n- 读完后写读书笔记\n\n### 创造阅读环境\n\n- 打造舒适的阅读角落\n- 减少干扰（手机静音）\n- 保持光线充足\n\n### 选择适合的书籍\n\n**入门建议**：\n- 从感兴趣的题材开始\n- 选择难度适中的书\n- 多种类型交替阅读\n\n### 阅读方法\n\n**精读**：深入理解，做笔记\n**泛读**：快速获取信息\n**速读**：提高阅读效率\n\n### 时间管理\n\n- 每天固定阅读时间\n- 利用碎片时间阅读\n- 随身携带书籍或电子书\n\n### 坚持与复盘\n\n- 记录阅读进度\n- 定期总结收获\n- 分享阅读感悟\n\n### 推荐书单\n\n- 《如何阅读一本书》\n- 《刻意练习》\n- 《原子习惯》\n\n### 总结\n\n阅读习惯需要长期坚持，每天进步一点点，终将收获满满。")
                    .summary("分享培养阅读习惯的实用方法，让阅读成为生活的一部分。")
                    .user(user2)
                    .category(life)
                    .coverImage("/images/articles/reading-habit-cultivation.jpg")
                    .viewCount(3245)
                    .likeCount(234)
                    .commentCount(65)
                    .published(true)
                    .build();
            articleRepository.save(a9);

            Article a10 = Article.builder()
                    .title("断舍离：整理心灵的艺术")
                    .content("## 断舍离：整理心灵的艺术\n\n断舍离不仅仅是整理物品，更是整理内心的过程。\n\n### 什么是断舍离\n\n- **断**：断绝不需要的东西\n- **舍**：舍弃多余的废物\n- **离**：脱离对物品的执念\n\n### 断舍离的步骤\n\n**第一步：审视物品**\n\n拿起每一件物品，问自己：\n- 我现在还需要它吗？\n- 它给我带来快乐吗？\n- 它的价值是什么？\n\n**第二步：分类处理**\n\n- **保留**：真正需要和喜爱的物品\n- **捐赠**：还有使用价值的物品\n- **丢弃**：没有价值的废物\n\n**第三步：整理收纳**\n\n- 每个物品都有固定位置\n- 使用收纳工具\n- 保持整洁\n\n### 断舍离的好处\n\n1. **空间变大**：房间变得宽敞明亮\n2. **心情舒畅**：减少视觉干扰\n3. **节省时间**：找东西更方便\n4. **提升品质**：只保留真正有价值的东西\n\n### 常见困难\n\n- 舍不得丢弃旧物\n- 担心以后会用到\n- 不知道如何开始\n\n### 克服方法\n\n- 从小空间开始\n- 设定明确规则\n- 寻求家人支持\n\n### 总结\n\n断舍离是一种生活态度，让我们学会珍惜当下。")
                    .summary("介绍断舍离的理念和实践方法，帮助你整理生活，找回内心的平静。")
                    .user(user2)
                    .category(life)
                    .coverImage("/images/articles/minimalism-declutter.jpg")
                    .viewCount(2876)
                    .likeCount(212)
                    .commentCount(54)
                    .published(true)
                    .build();
            articleRepository.save(a10);
        }

        if (user3 != null && travel != null) {
            Article a11 = Article.builder()
                    .title("云南大理深度旅行攻略")
                    .content("## 云南大理深度旅行攻略\n\n大理是一个让人魂牵梦绕的地方，这里有苍山洱海，有古城风情。\n\n### 必去景点\n\n**1. 洱海**\n\n租一辆电动车环海骑行，沿途风景美不胜收。建议顺时针骑行，路况更好。\n\n**2. 大理古城**\n\n漫步青石板路，感受历史文化氛围。晚上的古城更有韵味，灯火辉煌。\n\n**3. 苍山**\n\n坐索道上山，俯瞰洱海全景。山上气候凉爽，夏天避暑的好地方。\n\n**4. 喜洲古镇**\n\n比大理古城更安静，保留了更多原生态的白族民居。一定要尝尝喜洲粑粑。\n\n**5. 双廊古镇**\n\n洱海边的美丽小镇，适合住上一晚，看日出日落。\n\n### 美食推荐\n\n- **过桥米线**：云南特色，一定要尝尝\n- **酸辣鱼**：酸辣可口，非常开胃\n- **烤乳扇**：当地特色小吃，有点像奶酪\n- **饵块**：可以当早餐，有多种吃法\n\n### 行程安排\n\n**Day 1**：抵达大理，逛古城，品尝美食\n**Day 2**：环洱海骑行\n**Day 3**：登苍山，喜洲古镇\n**Day 4**：双廊古镇一日游\n**Day 5**：返程\n\n### 旅行贴士\n\n- 最佳季节：春季和秋季\n- 防晒很重要，紫外线强\n- 提前预订住宿，旺季人很多\n\n### 住宿推荐\n\n- 古城内：方便游玩，但较吵\n- 洱海边：风景优美，适合度假\n- 喜洲：安静舒适，体验当地生活\n\n### 总结\n\n大理是一个值得一去再去的地方，每次去都有新的发现。")
                    .summary("详细的云南大理旅行攻略，包括必去景点、美食推荐和行程安排。")
                    .user(user3)
                    .category(travel)
                    .coverImage("/images/articles/yunnan-dali-lake-mountain.jpg")
                    .viewCount(5678)
                    .likeCount(432)
                    .commentCount(98)
                    .published(true)
                    .build();
            articleRepository.save(a11);

            Article a12 = Article.builder()
                    .title("西藏拉萨朝圣之旅")
                    .content("## 西藏拉萨朝圣之旅\n\n拉萨是一个让人心灵得到净化的地方，值得一生去一次。\n\n### 必去景点\n\n**1. 布达拉宫**\n\n雄伟的布达拉宫让人震撼！建议提前预约门票，避开旅游高峰。内部金碧辉煌，充满了历史感。\n\n**2. 大昭寺**\n\n虔诚的信徒们在这里朝拜，让人感受到信仰的力量。八廓街上转经的人们，让人感受到宁静。\n\n**3. 纳木错**\n\n美丽的纳木错湖，湖水湛蓝湛蓝的，像宝石一样。远处的雪山倒映在湖水中，景色如画。\n\n**4. 羊卓雍措**\n\n羊卓雍措也是必去的景点，湖水的颜色会随着光线变化，非常神奇。\n\n### 美食推荐\n\n- **藏面**：味道独特，值得一试\n- **酥油茶**：刚开始可能不习惯，但喝多了会上瘾\n- **牦牛肉火锅**：非常美味\n\n### 注意事项\n\n- **高原反应**：提前服用红景天，慢慢适应\n- **防晒**：紫外线非常强\n- **尊重当地文化**：了解藏族的风俗习惯\n\n### 最佳旅行时间\n\n每年5-10月是最佳季节，气候适宜。\n\n### 行程建议\n\n**Day 1-2**：拉萨市区适应\n**Day 3**：布达拉宫、大昭寺\n**Day 4**：纳木错一日游\n**Day 5**：羊卓雍措\n**Day 6**：返程\n\n### 总结\n\n拉萨之旅是一次心灵之旅，让人终身难忘。")
                    .summary("记录拉萨之旅的美好时光，感受高原的魅力和信仰的力量。")
                    .user(user3)
                    .category(travel)
                    .coverImage("/images/articles/tibet-lhasa-potala-palace.jpg")
                    .viewCount(4567)
                    .likeCount(356)
                    .commentCount(87)
                    .published(true)
                    .build();
            articleRepository.save(a12);

            Article a13 = Article.builder()
                    .title("日本京都深度游")
                    .content("## 日本京都深度游\n\n京都是日本传统文化的代表，值得细细品味。\n\n### 必去景点\n\n**1. 清水寺**\n\n京都最著名的寺庙，春季樱花季和秋季红叶季尤其美丽。\n\n**2. 金阁寺**\n\n金碧辉煌的舍利殿，倒映在池塘中，美不胜收。\n\n**3. 伏见稻荷大社**\n\n千本鸟居是拍照打卡的绝佳地点，红色的鸟居绵延不绝。\n\n**4. 祇园艺伎区**\n\n傍晚时分可以看到艺伎穿行于街道，感受传统文化。\n\n**5. 岚山**\n\n竹林小径和渡月桥是必去之处，风景如画。\n\n### 美食推荐\n\n- **怀石料理**：精致的日本料理\n- **抹茶甜品**：京都抹茶世界闻名\n- **汤豆腐**：清淡美味\n- **京料理**：传统京都美食\n\n### 季节推荐\n\n- **春季**：樱花盛开\n- **秋季**：红叶满山\n- **冬季**：静谧祥和\n\n### 行程安排\n\n**Day 1**：清水寺、二年坂、三年坂\n**Day 2**：金阁寺、龙安寺、大德寺\n**Day 3**：伏见稻荷大社、东福寺\n**Day 4**：岚山一日游\n**Day 5**：祇园艺伎区、哲学之道\n\n### 总结\n\n京都的每一处都充满了历史韵味，让人流连忘返。")
                    .summary("详细的日本京都旅行攻略，带你领略千年古都的魅力。")
                    .user(user3)
                    .category(travel)
                    .coverImage("/images/articles/japan-kyoto-temple.jpg")
                    .viewCount(4234)
                    .likeCount(312)
                    .commentCount(78)
                    .published(true)
                    .build();
            articleRepository.save(a13);

            Article a14 = Article.builder()
                    .title("新疆伊犁草原之旅")
                    .content("## 新疆伊犁草原之旅\n\n伊犁河谷是新疆最美的地方之一，草原、雪山、湖泊应有尽有。\n\n### 必去景点\n\n**1. 那拉提草原**\n\n空中草原名不虚传，一望无际的草原如同绿色的海洋。\n\n**2. 喀拉峻草原**\n\n世界自然遗产，人体草原的曲线美不胜收。\n\n**3. 赛里木湖**\n\n大西洋最后一滴眼泪，湖水湛蓝清澈。\n\n**4. 巴音布鲁克**\n\n九曲十八弯，日落时分可以看到九个太阳。\n\n**5. 独库公路**\n\n中国最美公路之一，穿越天山山脉。\n\n### 最佳季节\n\n6-8月是最佳旅游季节，草原绿意盎然，野花盛开。\n\n### 行程安排\n\n**Day 1**：抵达伊宁\n**Day 2**：赛里木湖\n**Day 3**：那拉提草原\n**Day 4**：巴音布鲁克\n**Day 5**：独库公路\n**Day 6**：喀拉峻草原\n**Day 7**：返程\n\n### 注意事项\n\n- 做好防晒\n- 尊重当地少数民族文化\n- 提前预订住宿\n\n### 总结\n\n伊犁的美无法用语言形容，只有亲身经历才能体会。")
                    .summary("详细的新疆伊犁旅行攻略，带你领略中国最美草原的魅力。")
                    .user(user3)
                    .category(travel)
                    .coverImage("/images/articles/xinjiang-yili-grassland.jpg")
                    .viewCount(3890)
                    .likeCount(289)
                    .commentCount(72)
                    .published(true)
                    .build();
            articleRepository.save(a14);
        }

        if (user4 != null && food != null) {
            Article a15 = Article.builder()
                    .title("家常红烧肉详细做法")
                    .content("## 家常红烧肉详细做法\n\n红烧肉是一道经典的中式菜肴，学会这道菜，家人都会赞不绝口。\n\n### 食材准备\n\n- 五花肉 500克\n- 姜片 5片\n- 葱段 适量\n- 料酒 2勺\n- 生抽 2勺\n- 老抽 1勺\n- 冰糖 20克\n- 八角 2颗\n- 桂皮 1小块\n- 香叶 2片\n- 盐 适量\n\n### 做法步骤\n\n**步骤1：五花肉切块**\n\n将五花肉切成2-3厘米见方的块，大小均匀。\n\n**步骤2：焯水**\n\n冷水下锅，放入五花肉块，加入姜片和料酒，大火烧开后撇去浮沫，捞出沥干水分。\n\n**步骤3：炒糖色**\n\n锅中放少许油，小火加热，放入冰糖慢慢融化，待糖变成浅褐色时立即倒入五花肉块翻炒上色。\n\n**步骤4：调味**\n\n加入生抽、老抽翻炒均匀，然后加入葱段、姜片、八角、桂皮、香叶炒出香味。\n\n**步骤5：炖煮**\n\n加入开水，水量要没过肉块，大火烧开后转小火慢炖40分钟。\n\n**步骤6：收汁**\n\n40分钟后加入适量盐调味，然后开大火收汁，直到汤汁变得浓稠发亮即可。\n\n### 小贴士\n\n- 炒糖色时火候要小，避免炒糊\n- 一定要用开水炖煮，这样肉质更嫩\n- 炖煮时间要足够，这样肥肉才会软糯\n- 收汁时要不停翻炒，防止糊底\n\n### 常见问题\n\n**问题**：肉炖不烂怎么办？\n**解决**：延长炖煮时间，或者使用高压锅\n\n**问题**：颜色不够红亮怎么办？\n**解决**：可以适当增加老抽的用量\n\n### 搭配建议\n\n红烧肉配米饭简直绝了！也可以搭配土豆、鹌鹑蛋一起炖，味道更丰富。")
                    .summary("家常红烧肉的详细做法，从食材准备到烹饪步骤，手把手教你做出美味红烧肉。")
                    .user(user4)
                    .category(food)
                    .coverImage("/images/articles/chinese-braised-pork-belly.jpg")
                    .viewCount(6789)
                    .likeCount(543)
                    .commentCount(123)
                    .published(true)
                    .build();
            articleRepository.save(a15);

            Article a16 = Article.builder()
                    .title("手工披萨制作教程")
                    .content("## 手工披萨制作教程\n\n在家也能做出美味的披萨！自己动手，乐趣多多。\n\n### 面团材料\n\n- 高筋面粉 200克\n- 酵母 3克\n- 温水 120毫升（约30-35度）\n- 盐 2克\n- 橄榄油 10毫升\n- 糖 5克（可选，帮助发酵）\n\n### 馅料推荐\n\n- 番茄酱\n- 马苏里拉奶酪（一定要多放！）\n- 火腿/培根/香肠\n- 蘑菇/洋葱/彩椒\n- 黑橄榄/玉米粒\n- 香草碎（披萨草、牛至叶）\n\n### 制作步骤\n\n**步骤1：揉面团**\n\n将所有材料混合，揉成光滑的面团，放在温暖的地方发酵至两倍大（约1-2小时）。\n\n**步骤2：擀面团**\n\n将发酵好的面团取出，揉匀排气，擀成薄饼形状，可以做成圆形或方形。用叉子在饼底戳一些小孔，防止烤的时候鼓起。\n\n**步骤3：准备馅料**\n\n在饼底上均匀涂抹一层番茄酱，撒上一层马苏里拉奶酪，然后放上各种馅料。\n\n**步骤4：烘烤**\n\n烤箱预热至200度，放入披萨烤15-20分钟，直到饼底金黄，奶酪融化起泡即可。\n\n### 小贴士\n\n- 面团发酵要到位，这样饼底才松软\n- 奶酪一定要用马苏里拉，才能拉丝\n- 馅料不要放太多，否则饼底容易湿软\n- 可以在饼边刷一层橄榄油，颜色更金黄\n\n### 创意搭配\n\n- 经典玛格丽特：番茄、马苏里拉、罗勒\n- 肉食爱好者：火腿、培根、香肠\n- 素食主义：各种蔬菜\n- 夏威夷风味：火腿、菠萝\n\n### 工具推荐\n\n如果经常做披萨，可以买一个披萨石，烤出来的饼底更酥脆。")
                    .summary("在家制作美味披萨的详细教程，从面团制作到烘烤完成，一步一步教你做披萨。")
                    .user(user4)
                    .category(food)
                    .coverImage("/images/articles/homemade-pizza-delicious.jpg")
                    .viewCount(5432)
                    .likeCount(423)
                    .commentCount(98)
                    .published(true)
                    .build();
            articleRepository.save(a16);

            Article a17 = Article.builder()
                    .title("日式拉面制作指南")
                    .content("## 日式拉面制作指南\n\n日式拉面是日本最受欢迎的美食之一，浓郁的汤底和劲道的面条让人回味无穷。\n\n### 汤底制作\n\n**豚骨汤底**：\n\n1. 准备猪大骨、猪皮、鸡骨\n2. 焯水去血沫\n3. 加入姜片、葱段\n4. 大火煮沸后转小火熬制8-12小时\n5. 过滤掉杂质\n\n**味噌汤底**：\n\n1. 准备昆布、柴鱼片\n2. 制作日式高汤（出汁）\n3. 加入味噌酱调和\n\n### 面条制作\n\n- 高筋面粉+碱水\n- 手工揉面至光滑\n- 醒面30分钟\n- 压延切条\n\n### 配料准备\n\n- 叉烧肉：五花肉卤制\n- 溏心蛋：65度水煮7分钟\n- 笋干：切片焯水\n- 海苔、葱花\n\n### 组装拉面\n\n1. 煮面条\n2. 碗中盛入汤底\n3. 放入面条\n4. 摆上配料\n\n### 小贴士\n\n- 汤底要熬够时间才浓郁\n- 面条不要煮太烂\n- 配料可以根据喜好调整\n\n### 总结\n\n自己动手做拉面，成就感满满！")
                    .summary("详细的日式拉面制作教程，从汤底到配料，手把手教你做出正宗日式拉面。")
                    .user(user4)
                    .category(food)
                    .coverImage("/images/articles/japanese-ramen-noodle.jpg")
                    .viewCount(4567)
                    .likeCount(345)
                    .commentCount(87)
                    .published(true)
                    .build();
            articleRepository.save(a17);

            Article a18 = Article.builder()
                    .title("烘焙入门：戚风蛋糕制作")
                    .content("## 烘焙入门：戚风蛋糕制作\n\n戚风蛋糕是烘焙入门的经典选择，口感轻盈蓬松。\n\n### 食材准备\n\n**蛋黄糊**：\n- 蛋黄 4个\n- 牛奶 60ml\n- 玉米油 50ml\n- 低筋面粉 80g\n- 细砂糖 30g\n\n**蛋白霜**：\n- 蛋白 4个\n- 细砂糖 60g\n- 柠檬汁或白醋 几滴\n\n### 制作步骤\n\n**步骤1：准备工作**\n\n- 模具涂抹黄油\n- 烤箱预热至170度\n\n**步骤2：制作蛋黄糊**\n\n1. 蛋黄加入细砂糖搅拌均匀\n2. 加入玉米油和牛奶\n3. 筛入低筋面粉\n4. 搅拌至无颗粒\n\n**步骤3：制作蛋白霜**\n\n1. 蛋白加入柠檬汁\n2. 分三次加入细砂糖\n3. 打发至硬性发泡\n\n**步骤4：混合面糊**\n\n1. 取1/3蛋白霜加入蛋黄糊\n2. 翻拌均匀\n3. 倒入剩余蛋白霜\n4. 轻轻翻拌\n\n**步骤5：烘烤**\n\n1. 倒入模具\n2. 轻轻震出气泡\n3. 放入烤箱烤40-45分钟\n4. 出炉后倒扣冷却\n\n### 常见问题\n\n**问题**：蛋糕塌陷怎么办？\n**解决**：蛋白霜要打发到位，烘烤时间要足够\n\n**问题**：底部湿黏怎么办？\n**解决**：可以延长烘烤时间\n\n### 装饰建议\n\n- 奶油霜抹面\n- 新鲜水果装饰\n- 巧克力淋面\n\n### 总结\n\n戚风蛋糕虽然有点难度，但只要掌握技巧就能成功！")
                    .summary("详细的戚风蛋糕制作教程，适合烘焙新手学习。")
                    .user(user4)
                    .category(food)
                    .coverImage("/images/articles/chiffon-cake-baking.jpg")
                    .viewCount(3890)
                    .likeCount(289)
                    .commentCount(76)
                    .published(true)
                    .build();
            articleRepository.save(a18);
        }

        if (user5 != null && reading != null) {
            Article a19 = Article.builder()
                    .title("《三体》三部曲深度解读")
                    .content("## 《三体》三部曲深度解读\n\n刘慈欣的科幻巨作，中国科幻的里程碑。读完这套书，我被深深震撼了。\n\n### 三体文明\n\n三体人生活在一个有三个太阳的星系，生存环境极为恶劣。他们的星球被三颗太阳的引力拉扯，气候极端不稳定，文明多次毁灭又重建。\n\n为了生存，他们决定寻找新的家园，于是发现了地球。\n\n### 黑暗森林法则\n\n\"宇宙就是一座黑暗森林，每个文明都是带枪的猎人，像幽灵般潜行于林间。如果他发现了别的生命，唯一的选择就是开枪消灭之。\"\n\n这个理论让人不寒而栗，但仔细想想，又觉得很有道理。在宇宙的尺度上，文明之间的距离如此遥远，无法建立信任，生存是第一法则。\n\n### 技术爆炸\n\n文明的技术发展可能在短时间内实现飞跃。三体人害怕地球文明的技术爆炸，所以派出了智子来锁死人类的科学发展。\n\n### 面壁者计划\n\n为了对抗三体人，联合国启动了面壁者计划，选出四位面壁者，他们可以动用全球资源来制定对抗三体人的计划。\n\n罗辑是最成功的面壁者，他发现了黑暗森林法则，并以此威胁三体人。\n\n### 太阳系的结局\n\n最终，太阳系被二维化，人类文明几乎毁灭。只有一小部分人乘坐光速飞船逃离。\n\n### 人物分析\n\n**叶文洁**：悲剧性的人物，她的选择改变了人类的命运\n**罗辑**：从普通人成长为拯救人类的英雄\n**程心**：善良但软弱，她的善意导致了灾难\n\n### 哲学思考\n\n- 文明的本质是什么？\n- 生存与道德哪个更重要？\n- 人类的未来在哪里？\n\n### 感想\n\n这本书让我重新思考人类在宇宙中的位置。我们是如此渺小，但又如此伟大。\n\n### 推荐理由\n\n强烈推荐给所有喜欢科幻的朋友，这是一部值得反复阅读的作品。")
                    .summary("《三体》三部曲的深度解读，探索宇宙的奥秘和文明的意义。")
                    .user(user5)
                    .category(reading)
                    .coverImage("/images/articles/science-fiction-three-body-universe.jpg")
                    .viewCount(6789)
                    .likeCount(543)
                    .commentCount(134)
                    .published(true)
                    .build();
            articleRepository.save(a19);

            Article a20 = Article.builder()
                    .title("《活着》中的生命启示")
                    .content("## 《活着》中的生命启示\n\n余华的《活着》是一部让人感动的作品，主人公福贵的一生经历了太多的苦难。\n\n### 苦难中的坚持\n\n福贵的一生经历了太多苦难：\n- 年轻时赌博输光家产\n- 父亲被气死\n- 母亲去世\n- 女儿变成哑巴\n- 儿子有庆被抽血抽死\n- 女儿凤霞难产去世\n- 妻子家珍去世\n- 外孙苦根被豆子撑死\n\n但他依然选择活着，和老牛一起度过晚年。\n\n### 亲情的力量\n\n家人是福贵活下去的动力。即使一个个离他而去，他依然记得他们，讲述他们的故事。\n\n\"我看着那条弯曲着通向城里的小路，听不到我儿子赤脚跑来的声音，月光照在路上，像是撒满了盐。\"这句话让我泪流满面。\n\n### 活着的意义\n\n\"人是为活着本身而活着，而不是为活着之外的任何事物所活着。\"\n\n活着不需要任何理由，活着本身就是意义。\n\n### 珍惜当下\n\n读完这本书，我更加珍惜现在的生活。我们常常抱怨，但和福贵相比，我们的困难真的不算什么。\n\n### 生命的韧性\n\n生命是如此脆弱，又是如此坚韧。福贵的故事让我相信，无论遇到多大的困难，人都能活下去。\n\n### 文学手法\n\n余华的文字朴实无华，却蕴含着巨大的力量。他用最简单的语言讲述最深刻的道理。\n\n### 推荐理由\n\n这是一本让你重新思考生命意义的书，强烈推荐！")
                    .summary("读《活着》的深刻感悟，思考生命的意义和珍惜当下。")
                    .user(user5)
                    .category(reading)
                    .coverImage("/images/articles/life-philosophy-farming-old-man.jpg")
                    .viewCount(5432)
                    .likeCount(423)
                    .commentCount(102)
                    .published(true)
                    .build();
            articleRepository.save(a20);

            Article a21 = Article.builder()
                    .title("《小王子》的哲学思考")
                    .content("## 《小王子》的哲学思考\n\n一本写给大人的童话书，蕴含着深刻的人生哲理。\n\n### 小王子的旅程\n\n小王子访问了六个星球，遇见了各种各样的人：\n\n1. **国王**：沉迷于权力，却一无所有\n2. **虚荣的人**：只喜欢听赞美\n3. **商人**：忙着计算和占有\n4. **点灯人**：盲目地执行命令\n5. **地理学家**：只研究地图，从不亲自探索\n6. **酒鬼**：为了忘记羞愧而喝酒\n\n这些人代表了成人世界的各种弊病。\n\n### 狐狸的话\n\n\"如果你驯服了我，我们就互相不可缺少了。你对我来说，就是世界上独一无二的了；我对你来说，也是世界上独一无二的了。\"\n\n这段话让我思考人与人之间的关系。真正的关系需要时间和用心去培养。\n\n### 爱是责任\n\n\"你在你的玫瑰花身上耗费的时间使得你的玫瑰花变得如此重要。\"\n\n我们常常忽略身边最珍贵的东西，因为我们没有意识到它们的价值。\n\n### 回到原点\n\n最后，小王子明白了爱的真谛，回到了他的星球。他的玫瑰花虽然和花园里的五千朵玫瑰一样，但因为他的用心呵护，她变得独一无二。\n\n### 献给每一个曾经是孩子的大人\n\n这本书提醒我们，不要忘记自己曾经是个孩子，保持内心的纯真和美好。\n\n### 插图艺术\n\n圣埃克苏佩里的插图简洁而富有表现力，与文字相得益彰。\n\n### 推荐理由\n\n这是一本值得一生阅读的书，每个年龄段读都会有不同的感悟。")
                    .summary("《小王子》中的人生哲理，让我们重新思考爱与责任。")
                    .user(user5)
                    .category(reading)
                    .coverImage("/images/articles/little-prince-stars-rose.jpg")
                    .viewCount(4567)
                    .likeCount(356)
                    .commentCount(89)
                    .published(true)
                    .build();
            articleRepository.save(a21);

            Article a22 = Article.builder()
                    .title("《百年孤独》魔幻现实主义之旅")
                    .content("## 《百年孤独》魔幻现实主义之旅\n\n马尔克斯的代表作，魔幻现实主义文学的巅峰之作。\n\n### 布恩迪亚家族\n\n布恩迪亚家族七代人的故事，充满了传奇色彩。\n\n**第一代**：何塞·阿卡迪奥·布恩迪亚，马孔多的创建者\n**第二代**：上校奥雷里亚诺，参加无数战争\n**第七代**：长着猪尾巴的孩子，被蚂蚁吃掉\n\n### 魔幻元素\n\n- 会飞的地毯\n- 下了四年的雨\n- 幽灵般的鬼魂\n- 升天的蕾梅黛丝\n\n### 孤独主题\n\n书中每个人都是孤独的，孤独是人类的本质。\n\n### 时间循环\n\n书中的名字不断重复，暗示着历史的循环。\n\n### 写作风格\n\n马尔克斯的文字如诗如画，将魔幻与现实完美融合。\n\n### 阅读建议\n\n第一次读可能会觉得混乱，坚持下去会发现其中的奥秘。\n\n### 推荐理由\n\n这是一部不朽的文学巨著，值得细细品味。")
                    .summary("深入解读《百年孤独》的魔幻世界，感受马尔克斯笔下的孤独与命运。")
                    .user(user5)
                    .category(reading)
                    .coverImage("/images/articles/magic-realism-book.jpg")
                    .viewCount(3890)
                    .likeCount(289)
                    .commentCount(76)
                    .published(true)
                    .build();
            articleRepository.save(a22);
        }

        if (user6 != null && photography != null) {
            Article a23 = Article.builder()
                    .title("风光摄影入门指南")
                    .content("## 风光摄影入门指南\n\n风光摄影是最受欢迎的摄影类型之一，让我们一起探索大自然的美丽。\n\n### 器材选择\n\n**相机**：\n- 单反或微单\n- 全画幅更好\n\n**镜头**：\n- 广角镜头（16-35mm）\n- 长焦镜头（70-200mm）\n- 三脚架必不可少\n\n### 拍摄技巧\n\n**构图**：\n- 三分法\n- 引导线\n- 对称构图\n- 框架构图\n\n**光线**：\n- 黄金时段拍摄\n- 避免正午强光\n- 使用滤镜（ND镜、渐变镜）\n\n**参数设置**：\n- 小光圈（f/8-f/16）\n- 低ISO（100-400）\n- 快门速度根据场景调整\n\n### 后期处理\n\n- Lightroom 基础调色\n- Photoshop 精细处理\n- 保持自然风格\n\n### 拍摄地点推荐\n\n- 日出日落地点\n- 高山湖泊\n- 海边礁石\n- 森林溪流\n\n### 注意事项\n\n- 注意安全\n- 尊重自然\n- 保护环境\n\n### 总结\n\n风光摄影需要耐心和等待，好照片来自于不懈的努力。")
                    .summary("风光摄影入门教程，从器材选择到拍摄技巧，帮助你拍出美丽的风景照。")
                    .user(user6)
                    .category(photography)
                    .coverImage("/images/articles/landscape-photography-camera.jpg")
                    .viewCount(3456)
                    .likeCount(256)
                    .commentCount(67)
                    .published(true)
                    .build();
            articleRepository.save(a23);

            Article a24 = Article.builder()
                    .title("人像摄影技巧分享")
                    .content("## 人像摄影技巧分享\n\n人像摄影是摄影的重要分支，捕捉人物的美丽瞬间。\n\n### 器材选择\n\n**相机**：\n- 大光圈镜头（50mm f/1.4）\n- 85mm f/1.8是人像神器\n\n**灯光**：\n- 自然光最佳\n- 反光板必备\n- 外置闪光灯\n\n### 构图技巧\n\n- 人物位置\n- 眼神交流\n- 肢体语言\n- 背景选择\n\n### 光线运用\n\n**顺光**：明亮清晰\n**侧光**：有立体感\n**逆光**：剪影效果\n**黄金时段**：柔和温暖\n\n### 姿势指导\n\n- 自然放松\n- 避免僵硬\n- 手部姿势\n- 头部角度\n\n### 后期处理\n\n- 肤色调整\n- 磨皮技巧\n- 色调风格\n- 保持真实\n\n### 拍摄场景\n\n- 室内拍摄\n- 户外拍摄\n- 城市街拍\n- 自然风光\n\n### 总结\n\n人像摄影需要沟通和引导，拍出人物的个性和魅力。")
                    .summary("人像摄影技巧分享，帮助你拍出专业的人像照片。")
                    .user(user6)
                    .category(photography)
                    .coverImage("/images/articles/portrait-photography-lighting.jpg")
                    .viewCount(2876)
                    .likeCount(212)
                    .commentCount(54)
                    .published(true)
                    .build();
            articleRepository.save(a24);
        }

        if (user7 != null && health != null) {
            Article a25 = Article.builder()
                    .title("健身新手入门指南")
                    .content("## 健身新手入门指南\n\n开始健身是改变生活的第一步，让我们一起开启健康之旅。\n\n### 设定目标\n\n- 增肌\n- 减脂\n- 塑形\n- 提高体能\n\n### 器材选择\n\n**健身房**：\n- 力量训练器械\n- 有氧运动器械\n\n**居家**：\n- 瑜伽垫\n- 哑铃\n- 弹力带\n\n### 训练计划\n\n**力量训练**：\n- 每周3-4次\n- 全身训练或分化训练\n- 循序渐进\n\n**有氧运动**：\n- 跑步\n- 游泳\n- 骑行\n- HIIT\n\n### 饮食搭配\n\n- 蛋白质摄入\n- 碳水化合物\n- 健康脂肪\n- 充足水分\n\n### 休息恢复\n\n- 睡眠充足\n- 拉伸放松\n- 避免过度训练\n\n### 常见误区\n\n- 只做有氧运动\n- 忽视热身\n- 盲目追求大重量\n- 不注重饮食\n\n### 坚持与进步\n\n- 记录训练日志\n- 定期评估\n- 调整计划\n\n### 总结\n\n健身是长期的过程，坚持就是胜利！")
                    .summary("健身新手入门指南，帮助你开始健康的健身之旅。")
                    .user(user7)
                    .category(health)
                    .coverImage("/images/articles/fitness-workout-gym.jpg")
                    .viewCount(4567)
                    .likeCount(345)
                    .commentCount(89)
                    .published(true)
                    .build();
            articleRepository.save(a25);

            Article a26 = Article.builder()
                    .title("瑜伽入门与身心放松")
                    .content("## 瑜伽入门与身心放松\n\n瑜伽不仅是一种运动，更是一种生活方式。\n\n### 什么是瑜伽\n\n- 起源于印度\n- 身心合一的练习\n- 包含体位法、呼吸法、冥想\n\n### 准备工作\n\n- 瑜伽垫\n- 舒适的服装\n- 安静的环境\n\n### 基础体式\n\n**坐姿**：\n- 莲花坐\n- 简易坐\n\n**站姿**：\n- 山式\n- 战士式\n- 三角式\n\n**前屈**：\n- 下犬式\n- 婴儿式\n\n### 呼吸练习\n\n- 腹式呼吸\n- 乌加依呼吸\n- 呼吸冥想\n\n### 冥想入门\n\n- 专注当下\n- 观察呼吸\n- 接纳情绪\n\n### 练习建议\n\n- 每天坚持10-15分钟\n- 选择固定时间\n- 循序渐进\n\n### 瑜伽的好处\n\n- 改善柔韧性\n- 缓解压力\n- 提升专注力\n- 促进身心健康\n\n### 总结\n\n瑜伽适合所有人，从初学者到高级练习者都能找到适合自己的方式。")
                    .summary("瑜伽入门指南，帮助你开始身心放松的练习之旅。")
                    .user(user7)
                    .category(health)
                    .coverImage("/images/articles/yoga-meditation-health.jpg")
                    .viewCount(3245)
                    .likeCount(234)
                    .commentCount(65)
                    .published(true)
                    .build();
            articleRepository.save(a26);
        }

        if (user8 != null && music != null) {
            Article a27 = Article.builder()
                    .title("吉他入门：从零开始学吉他")
                    .content("## 吉他入门：从零开始学吉他\n\n吉他是最受欢迎的乐器之一，让我们一起开始吉他之旅。")
                    .summary("吉他入门教程，帮助你从零开始学习吉他。")
                    .user(user8)
                    .category(music)
                    .coverImage("/images/articles/guitar-music-learning.jpg")
                    .viewCount(3890)
                    .likeCount(289)
                    .commentCount(76)
                    .published(true)
                    .build();
            articleRepository.save(a27);

            Article a28 = Article.builder()
                    .title("音乐创作心得分享")
                    .content("## 音乐创作心得分享\n\n创作音乐是一件非常有成就感的事情。")
                    .summary("音乐创作心得分享，帮助你开启音乐创作之旅。")
                    .user(user8)
                    .category(music)
                    .coverImage("/images/articles/music-composition-creative.jpg")
                    .viewCount(2876)
                    .likeCount(212)
                    .commentCount(54)
                    .published(true)
                    .build();
            articleRepository.save(a28);
        }

        if (user1 != null && science != null) {
            Article a29 = Article.builder()
                    .title("人工智能发展趋势")
                    .content("## 人工智能发展趋势\n\n人工智能正在改变我们的生活。")
                    .summary("探讨人工智能的发展趋势和未来展望。")
                    .user(user1)
                    .category(science)
                    .coverImage("/images/articles/artificial-intelligence-future.jpg")
                    .viewCount(4567)
                    .likeCount(345)
                    .commentCount(89)
                    .published(true)
                    .build();
            articleRepository.save(a29);

            Article a30 = Article.builder()
                    .title("元宇宙概念解析")
                    .content("## 元宇宙概念解析\n\n元宇宙是未来互联网的发展方向。")
                    .summary("解析元宇宙概念，探讨其技术和应用前景。")
                    .user(user1)
                    .category(science)
                    .coverImage("/images/articles/metaverse-virtual-world.jpg")
                    .viewCount(3890)
                    .likeCount(289)
                    .commentCount(76)
                    .published(true)
                    .build();
            articleRepository.save(a30);
        }

        if (user2 != null && career != null) {
            Article a31 = Article.builder()
                    .title("职场新人成长指南")
                    .content("## 职场新人成长指南\n\n初入职场，如何快速成长？")
                    .summary("职场新人成长指南，帮助你快速适应职场环境。")
                    .user(user2)
                    .category(career)
                    .coverImage("/images/articles/career-newbie-guide.jpg")
                    .viewCount(4234)
                    .likeCount(312)
                    .commentCount(78)
                    .published(true)
                    .build();
            articleRepository.save(a31);

            Article a32 = Article.builder()
                    .title("时间管理技巧分享")
                    .content("## 时间管理技巧分享\n\n高效管理时间，提高工作效率。")
                    .summary("时间管理技巧分享，帮助你提高工作和生活效率。")
                    .user(user2)
                    .category(career)
                    .coverImage("/images/articles/time-management-productivity.jpg")
                    .viewCount(3567)
                    .likeCount(267)
                    .commentCount(72)
                    .published(true)
                    .build();
            articleRepository.save(a32);
        }
    }

    private void initComments() {
        if (commentRepository.count() > 0) {
            return;
        }

        User user1 = userRepository.findByUsername("user1").orElse(null);
        User user2 = userRepository.findByUsername("user2").orElse(null);
        User user3 = userRepository.findByUsername("user3").orElse(null);
        User user4 = userRepository.findByUsername("user4").orElse(null);
        User user5 = userRepository.findByUsername("user5").orElse(null);
        User user6 = userRepository.findByUsername("user6").orElse(null);
        User user7 = userRepository.findByUsername("user7").orElse(null);
        User user8 = userRepository.findByUsername("user8").orElse(null);
        
        List<Article> articles = articleRepository.findAll();
        
        for (Article article : articles) {
            if (article.getTitle().contains("Spring Boot")) {
                Comment c1 = Comment.builder()
                        .content("写得非常详细！作为一个 Spring Boot 新手，跟着教程一步步来就学会了，非常感谢分享！")
                        .article(article)
                        .user(user2 != null ? user2 : user1)
                        .likeCount(45)
                        .build();
                commentRepository.save(c1);

                Comment c2 = Comment.builder()
                        .content("请问 Spring Boot 和 Spring MVC 有什么区别呢？")
                        .article(article)
                        .user(user4 != null ? user4 : user1)
                        .likeCount(23)
                        .build();
                commentRepository.save(c2);

                Comment reply1 = Comment.builder()
                        .content("简单来说，Spring MVC 是一个 Web 框架，而 Spring Boot 是一个快速构建 Spring 应用的工具。Spring Boot 可以很方便地集成 Spring MVC。")
                        .article(article)
                        .user(user1)
                        .parent(c2)
                        .likeCount(34)
                        .build();
                commentRepository.save(reply1);

                Comment c3 = Comment.builder()
                        .content("太实用了！收藏了，以后项目中可以参考。")
                        .article(article)
                        .user(user5 != null ? user5 : user1)
                        .likeCount(18)
                        .build();
                commentRepository.save(c3);
            }

            if (article.getTitle().contains("Vue3")) {
                Comment c1 = Comment.builder()
                        .content("Vue3 的组合式 API 确实很好用，代码组织更加清晰了。之前一直用 Vue2，现在准备升级！")
                        .article(article)
                        .user(user5 != null ? user5 : user1)
                        .likeCount(34)
                        .build();
                commentRepository.save(c1);

                Comment c2 = Comment.builder()
                        .content("请问 ref 和 reactive 有什么区别呢？")
                        .article(article)
                        .user(user3 != null ? user3 : user1)
                        .likeCount(15)
                        .build();
                commentRepository.save(c2);

                Comment reply1 = Comment.builder()
                        .content("ref 用于基本类型，需要通过 .value 访问；reactive 用于对象，直接访问属性即可。")
                        .article(article)
                        .user(user1)
                        .parent(c2)
                        .likeCount(28)
                        .build();
                commentRepository.save(reply1);
            }

            if (article.getTitle().contains("红烧肉")) {
                Comment c1 = Comment.builder()
                        .content("跟着做了一次，非常成功！家人都说好吃，已经列入家庭菜谱了！")
                        .article(article)
                        .user(user3 != null ? user3 : user1)
                        .likeCount(56)
                        .build();
                commentRepository.save(c1);

                Comment c2 = Comment.builder()
                        .content("炒糖色的时候总是炒糊，有什么技巧吗？")
                        .article(article)
                        .user(user5 != null ? user5 : user1)
                        .likeCount(23)
                        .build();
                commentRepository.save(c2);

                Comment reply1 = Comment.builder()
                        .content("火候一定要小，慢慢融化冰糖，看到颜色变浅褐色就立即下肉，不要等变黑！")
                        .article(article)
                        .user(user4 != null ? user4 : user1)
                        .parent(c2)
                        .likeCount(45)
                        .build();
                commentRepository.save(reply1);

                Comment c3 = Comment.builder()
                        .content("下次试试加土豆一起炖，味道更丰富！")
                        .article(article)
                        .user(user2 != null ? user2 : user1)
                        .likeCount(19)
                        .build();
                commentRepository.save(c3);
            }

            if (article.getTitle().contains("三体")) {
                Comment c1 = Comment.builder()
                        .content("《三体》真的太震撼了！黑暗森林法则让人细思极恐，每次读都有新的体会。")
                        .article(article)
                        .user(user1)
                        .likeCount(67)
                        .build();
                commentRepository.save(c1);

                Comment c2 = Comment.builder()
                        .content("最喜欢罗辑这个角色，真正的面壁者！他的故事让人感动。")
                        .article(article)
                        .user(user3 != null ? user3 : user1)
                        .likeCount(54)
                        .build();
                commentRepository.save(c2);

                Comment c3 = Comment.builder()
                        .content("二向箔那段看得我头皮发麻，太震撼了！")
                        .article(article)
                        .user(user5 != null ? user5 : user1)
                        .likeCount(43)
                        .build();
                commentRepository.save(c3);
            }

            if (article.getTitle().contains("大理")) {
                Comment c1 = Comment.builder()
                        .content("去年去过大理，确实很美！攻略很实用，收藏了下次去参考。")
                        .article(article)
                        .user(user4 != null ? user4 : user1)
                        .likeCount(45)
                        .build();
                commentRepository.save(c1);

                Comment c2 = Comment.builder()
                        .content("环洱海骑行真的很棒，风景太美了！")
                        .article(article)
                        .user(user3 != null ? user3 : user1)
                        .likeCount(32)
                        .build();
                commentRepository.save(c2);
            }

            if (article.getTitle().contains("活着")) {
                Comment c1 = Comment.builder()
                        .content("看哭了好几次，福贵太惨了，但他的坚持让人感动。")
                        .article(article)
                        .user(user2 != null ? user2 : user1)
                        .likeCount(78)
                        .build();
                commentRepository.save(c1);

                Comment c2 = Comment.builder()
                        .content("余华的文字太有力量了，朴实无华却直击人心。")
                        .article(article)
                        .user(user5 != null ? user5 : user1)
                        .likeCount(56)
                        .build();
                commentRepository.save(c2);
            }

            if (article.getTitle().contains("健身")) {
                Comment c1 = Comment.builder()
                        .content("新手必备！收藏了，准备开始健身之旅。")
                        .article(article)
                        .user(user7 != null ? user7 : user1)
                        .likeCount(34)
                        .build();
                commentRepository.save(c1);

                Comment c2 = Comment.builder()
                        .content("请问新手应该从什么开始练起？")
                        .article(article)
                        .user(user4 != null ? user4 : user1)
                        .likeCount(21)
                        .build();
                commentRepository.save(c2);

                Comment reply1 = Comment.builder()
                        .content("建议从自重训练开始，比如深蹲、俯卧撑、平板支撑，打好基础最重要！")
                        .article(article)
                        .user(user7 != null ? user7 : user1)
                        .parent(c2)
                        .likeCount(38)
                        .build();
                commentRepository.save(reply1);
            }

            if (article.getTitle().contains("旅行")) {
                Comment c1 = Comment.builder()
                        .content("太美了！好想去这里旅游，收藏了！")
                        .article(article)
                        .user(user2 != null ? user2 : user1)
                        .likeCount(42)
                        .build();
                commentRepository.save(c1);

                Comment c2 = Comment.builder()
                        .content("攻略很详细，收藏起来下次旅行用！")
                        .article(article)
                        .user(user4 != null ? user4 : user1)
                        .likeCount(28)
                        .build();
                commentRepository.save(c2);
            }

            if (article.getTitle().contains("阅读")) {
                Comment c1 = Comment.builder()
                        .content("非常实用的建议，准备开始培养阅读习惯！")
                        .article(article)
                        .user(user5 != null ? user5 : user1)
                        .likeCount(36)
                        .build();
                commentRepository.save(c1);
            }

            if (article.getTitle().contains("摄影")) {
                Comment c1 = Comment.builder()
                        .content("摄影新手来学习了，非常实用的技巧！")
                        .article(article)
                        .user(user6 != null ? user6 : user1)
                        .likeCount(29)
                        .build();
                commentRepository.save(c1);
            }

            if (article.getTitle().contains("职场")) {
                Comment c1 = Comment.builder()
                        .content("作为职场新人，这些建议太有用了！")
                        .article(article)
                        .user(user2 != null ? user2 : user1)
                        .likeCount(43)
                        .build();
                commentRepository.save(c1);
            }

            if (article.getTitle().contains("AI") || article.getTitle().contains("人工智能")) {
                Comment c1 = Comment.builder()
                        .content("AI时代来了，我们都要跟上时代的步伐！")
                        .article(article)
                        .user(user1)
                        .likeCount(52)
                        .build();
                commentRepository.save(c1);
            }

            if (article.getTitle().contains("吉他") || article.getTitle().contains("音乐")) {
                Comment c1 = Comment.builder()
                        .content("想学吉他很久了，这个教程很实用！")
                        .article(article)
                        .user(user8 != null ? user8 : user1)
                        .likeCount(38)
                        .build();
                commentRepository.save(c1);
            }

            Comment extra1 = Comment.builder()
                    .content("写得真好！受益匪浅，期待更多分享！")
                    .article(article)
                    .user(user1)
                    .likeCount((int)(Math.random() * 30) + 10)
                    .build();
            commentRepository.save(extra1);

            Comment extra2 = Comment.builder()
                    .content("收藏了，感谢分享！")
                    .article(article)
                    .user(user2 != null ? user2 : user1)
                    .likeCount((int)(Math.random() * 20) + 5)
                    .build();
            commentRepository.save(extra2);
        }
    }
}