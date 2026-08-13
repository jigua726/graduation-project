package com.example.blogcommunity.config;

import com.example.blogcommunity.entity.Category;
import com.example.blogcommunity.entity.TagCategory;
import com.example.blogcommunity.entity.User;
import com.example.blogcommunity.repository.CategoryRepository;
import com.example.blogcommunity.repository.TagCategoryRepository;
import com.example.blogcommunity.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 系统启动数据初始化器
 *
 * 【设计说明】
 * 本类只负责初始化【系统必需的元数据】，包括：
 *   1. 管理员账号（admin）
 *   2. 文章分类（10 个基础分类）
 *   3. 标签分类映射（13 个分类及其标签列表，用于自动打标签）
 *
 * 【不再做的事】
 *   - 不再硬编码普通用户（user1~user8）
 *   - 不再硬编码示例文章（32 篇）
 *   - 不再硬编码示例评论
 * 以上演示数据的来源改为：
 *   - 方案一：通过注册功能 + 写文章功能由用户自己创建
 *   - 方案二：执行根目录下的 h2-backup.sql 脚本导入已有演示数据
 *
 * 【初始化判断】
 *   所有初始化方法都有 exists/count 判断：
 *   - 如果对应数据表为空 → 写入初始数据（首次启动生效）
 *   - 如果对应数据表已有数据 → 直接跳过（不会重复插入）
 *   因此：清空 H2 数据库文件（./data/blogdb）后重启应用会重新执行初始化
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagCategoryRepository tagCategoryRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, CategoryRepository categoryRepository,
                          TagCategoryRepository tagCategoryRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagCategoryRepository = tagCategoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        initUsers();
        initCategories();
        initTagCategories();
        initArticles();
        initComments();
    }

    /**
     * 初始化标签分类数据
     * 对应实体 TagCategory，供 autoGenerateTags() 方法从数据库读取
     * 替代原本硬编码在 AdminServiceImpl 中的 Map<String, List<String>>
     */
    private void initTagCategories() {
        if (tagCategoryRepository.count() == 0) {
            List<TagCategory> tagCategories = Arrays.asList(
                    TagCategory.builder().categoryName("技术").tags("技术,编程,开发,代码,项目,教程").sortOrder(1).enabled(true).build(),
                    TagCategory.builder().categoryName("前端").tags("前端,Vue,React,JavaScript,HTML,CSS,TypeScript,Node.js").sortOrder(2).enabled(true).build(),
                    TagCategory.builder().categoryName("后端").tags("后端,Java,Python,Spring,数据库,MySQL,Redis,架构").sortOrder(3).enabled(true).build(),
                    TagCategory.builder().categoryName("AI").tags("AI,人工智能,机器学习,深度学习,大数据,云计算").sortOrder(4).enabled(true).build(),
                    TagCategory.builder().categoryName("生活").tags("生活,日常,生活感悟,生活方式,生活随笔,生活分享").sortOrder(5).enabled(true).build(),
                    TagCategory.builder().categoryName("读书").tags("读书,阅读,书单,书评,读书笔记,文学,小说,散文").sortOrder(6).enabled(true).build(),
                    TagCategory.builder().categoryName("旅行").tags("旅行,旅游,游记,风景,自然,人文,古镇,海岛").sortOrder(7).enabled(true).build(),
                    TagCategory.builder().categoryName("美食").tags("美食,烹饪,菜谱,家常菜,烘焙,甜点,食材").sortOrder(8).enabled(true).build(),
                    TagCategory.builder().categoryName("摄影").tags("摄影,拍照,相机,镜头,构图,光影,色彩").sortOrder(9).enabled(true).build(),
                    TagCategory.builder().categoryName("健康").tags("健康,健身,运动,跑步,瑜伽,冥想,养生").sortOrder(10).enabled(true).build(),
                    TagCategory.builder().categoryName("音乐").tags("音乐,听歌,唱歌,乐器,吉他,钢琴,歌单").sortOrder(11).enabled(true).build(),
                    TagCategory.builder().categoryName("科技").tags("科技,数码,手机,电脑,互联网,创新").sortOrder(12).enabled(true).build(),
                    TagCategory.builder().categoryName("职场").tags("职场,工作,职业发展,面试,简历,团队管理").sortOrder(13).enabled(true).build()
            );
            tagCategoryRepository.saveAll(tagCategories);
            System.out.println("[DataInitializer] 标签分类数据初始化完成，共 " + tagCategories.size() + " 个分类");
        }
    }

    /**
     * 初始化用户
     * 只创建系统管理员账号（admin / admin123），用于登录管理后台
     * 普通用户请通过注册功能自行创建，或执行 h2-backup.sql 恢复演示数据
     */
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
            System.out.println("[DataInitializer] 管理员账号初始化完成（用户名：admin / 密码：admin123）");
        }
    }

    /**
     * 初始化文章分类
     * 分类属于系统元数据，发文章时必须选择分类，因此必须预置
     * 如需增减分类，请在 H2 控制台 / 管理后台修改数据库即可
     */
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
            System.out.println("[DataInitializer] 文章分类初始化完成，共 " + categories.size() + " 个分类");
        }
    }

    /**
     * 初始化示例文章
     * 【已废弃】：不再在代码中硬编码示例文章
     *
     * 需要演示文章时，请二选一：
     *   1. 直接在网站中注册用户 → 写文章 → 真实创作
     *   2. 执行根目录下的 h2-backup.sql 脚本导入 32 篇示例文章
     */
    private void initArticles() {
        // 方法保留为占位：如未来需要自动初始化某些文章，可在此处扩展
        // 当前策略：代码不持有任何博客内容数据，由数据库作为唯一来源
    }

    /**
     * 初始化示例评论
     * 【已废弃】：不再在代码中硬编码示例评论
     *
     * 需要演示评论时，请二选一：
     *   1. 在网站中登录用户 → 对文章发表评论 → 真实创作
     *   2. 执行根目录下的 h2-backup.sql 脚本导入示例评论
     */
    private void initComments() {
        // 方法保留为占位：如未来需要自动初始化评论，可在此处扩展
        // 当前策略：代码不持有任何评论数据，由数据库作为唯一来源
    }
}
