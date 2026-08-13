-- ============================================================================
-- 标签分类初始化数据 SQL 脚本
-- 对应实体: com.example.blogcommunity.entity.TagCategory
-- 表名: tag_categories
-- 字段:
--   id           - 主键（自增）
--   category_name- 分类名称（唯一）
--   tags         - 该分类下的标签列表（以英文逗号分隔）
--   sort_order   - 排序权重
--   enabled      - 是否启用
--   created_at   - 创建时间
--   updated_at   - 更新时间
-- 使用方式:
--   1. 通过 H2 控制台执行: http://localhost:8082/h2-console
--      JDBC URL: jdbc:h2:file:./data/blogdb
--      用户名: sa  密码: (空)
--   2. 或在 application.yml 中配置 spring.sql.init 模式自动执行
-- ============================================================================

-- 清空旧数据（仅当需要重置时取消注释）
-- DELETE FROM tag_categories;

-- 插入 13 个标签分类数据
-- 使用 MERGE 语句避免重复插入（H2 兼容语法）
MERGE INTO tag_categories (category_name, tags, sort_order, enabled, created_at) KEY (category_name) VALUES
('技术', '技术,编程,开发,代码,项目,教程', 1, TRUE, CURRENT_TIMESTAMP);
MERGE INTO tag_categories (category_name, tags, sort_order, enabled, created_at) KEY (category_name) VALUES
('前端', '前端,Vue,React,JavaScript,HTML,CSS,TypeScript,Node.js', 2, TRUE, CURRENT_TIMESTAMP);
MERGE INTO tag_categories (category_name, tags, sort_order, enabled, created_at) KEY (category_name) VALUES
('后端', '后端,Java,Python,Spring,数据库,MySQL,Redis,架构', 3, TRUE, CURRENT_TIMESTAMP);
MERGE INTO tag_categories (category_name, tags, sort_order, enabled, created_at) KEY (category_name) VALUES
('AI', 'AI,人工智能,机器学习,深度学习,大数据,云计算', 4, TRUE, CURRENT_TIMESTAMP);
MERGE INTO tag_categories (category_name, tags, sort_order, enabled, created_at) KEY (category_name) VALUES
('生活', '生活,日常,生活感悟,生活方式,生活随笔,生活分享', 5, TRUE, CURRENT_TIMESTAMP);
MERGE INTO tag_categories (category_name, tags, sort_order, enabled, created_at) KEY (category_name) VALUES
('读书', '读书,阅读,书单,书评,读书笔记,文学,小说,散文', 6, TRUE, CURRENT_TIMESTAMP);
MERGE INTO tag_categories (category_name, tags, sort_order, enabled, created_at) KEY (category_name) VALUES
('旅行', '旅行,旅游,游记,风景,自然,人文,古镇,海岛', 7, TRUE, CURRENT_TIMESTAMP);
MERGE INTO tag_categories (category_name, tags, sort_order, enabled, created_at) KEY (category_name) VALUES
('美食', '美食,烹饪,菜谱,家常菜,烘焙,甜点,食材', 8, TRUE, CURRENT_TIMESTAMP);
MERGE INTO tag_categories (category_name, tags, sort_order, enabled, created_at) KEY (category_name) VALUES
('摄影', '摄影,拍照,相机,镜头,构图,光影,色彩', 9, TRUE, CURRENT_TIMESTAMP);
MERGE INTO tag_categories (category_name, tags, sort_order, enabled, created_at) KEY (category_name) VALUES
('健康', '健康,健身,运动,跑步,瑜伽,冥想,养生', 10, TRUE, CURRENT_TIMESTAMP);
MERGE INTO tag_categories (category_name, tags, sort_order, enabled, created_at) KEY (category_name) VALUES
('音乐', '音乐,听歌,唱歌,乐器,吉他,钢琴,歌单', 11, TRUE, CURRENT_TIMESTAMP);
MERGE INTO tag_categories (category_name, tags, sort_order, enabled, created_at) KEY (category_name) VALUES
('科技', '科技,数码,手机,电脑,互联网,创新', 12, TRUE, CURRENT_TIMESTAMP);
MERGE INTO tag_categories (category_name, tags, sort_order, enabled, created_at) KEY (category_name) VALUES
('职场', '职场,工作,职业发展,面试,简历,团队管理', 13, TRUE, CURRENT_TIMESTAMP);

-- 验证查询
SELECT id, category_name, tags, sort_order, enabled FROM tag_categories ORDER BY sort_order;
