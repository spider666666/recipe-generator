-- 创建数据库并指定字符集（避免中文乱码）
CREATE DATABASE IF NOT EXISTS recipe_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE recipe_db;

-- 1. 创建食材表：存储食材基础信息，适配食谱生成的食材匹配、营养计算
DROP TABLE IF EXISTS ingredient;
CREATE TABLE ingredient (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '食材唯一ID',
                            name VARCHAR(50) NOT NULL COMMENT '食材名称（如西红柿、鸡肉）',
                            category VARCHAR(20) NOT NULL COMMENT '食材分类（VEGETABLE/MEAT/SEAFOOD/STAPLE/SEASONING）',
                            common_unit VARCHAR(10) NOT NULL COMMENT '常用单位（个/g/ml/根/瓣）',
                            calories INT NOT NULL DEFAULT 0 COMMENT '每单位食材热量（大卡）',
                            create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            UNIQUE KEY uk_name (name) COMMENT '食材名称唯一，避免重复'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='食材基础信息表';

-- 2. 创建用户表：存储用户账号信息，适配登录、个性化食谱收藏
DROP TABLE IF EXISTS user;
CREATE TABLE user (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户唯一ID',
                      username VARCHAR(50) NOT NULL COMMENT '登录用户名',
                      password VARCHAR(100) NOT NULL COMMENT '登录密码（BCrypt加密，如示例123456）',
                      email VARCHAR(100) DEFAULT '' COMMENT '用户邮箱（用于找回密码）',
                      nickname VARCHAR(50) DEFAULT '' COMMENT '用户昵称',
                      enabled TINYINT(1) NOT NULL DEFAULT 1 COMMENT '账号是否启用（1-启用，0-禁用）',
                      create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
                      update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '信息更新时间',
                      UNIQUE KEY uk_username (username) COMMENT '用户名唯一',
                      UNIQUE KEY uk_email (email) COMMENT '邮箱唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户账号信息表';

-- 插入测试食材数据
INSERT INTO ingredient (name, category, common_unit, calories) VALUES
-- 蔬菜类
('西红柿', 'VEGETABLE', '个', 20),
('土豆', 'VEGETABLE', '个', 77),
('白菜', 'VEGETABLE', 'g', 17),
('黄瓜', 'VEGETABLE', '根', 15),
('胡萝卜', 'VEGETABLE', '根', 41),
('洋葱', 'VEGETABLE', '个', 40),
('青椒', 'VEGETABLE', '个', 22),
('茄子', 'VEGETABLE', '个', 25),

-- 肉类
('鸡肉', 'MEAT', 'g', 167),
('猪肉', 'MEAT', 'g', 242),
('牛肉', 'MEAT', 'g', 250),
('羊肉', 'MEAT', 'g', 203),

-- 海鲜类
('鱼', 'SEAFOOD', 'g', 100),
('虾', 'SEAFOOD', 'g', 85),
('螃蟹', 'SEAFOOD', 'g', 95),

-- 主食类
('米饭', 'STAPLE', 'g', 116),
('面条', 'STAPLE', 'g', 137),
('饺子', 'STAPLE', '个', 40),
('鸡蛋', 'STAPLE', '个', 147),

-- 调味料
('盐', 'SEASONING', 'g', 0),
('酱油', 'SEASONING', 'ml', 63),
('醋', 'SEASONING', 'ml', 18),
('糖', 'SEASONING', 'g', 387),
('料酒', 'SEASONING', 'ml', 0),
('葱', 'SEASONING', '根', 30),
('姜', 'SEASONING', 'g', 41),
('蒜', 'SEASONING', '瓣', 128);

-- 插入测试用户（密码均为123456，BCrypt加密）
INSERT INTO user (username, password, email, nickname, enabled) VALUES
                                                                    ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'admin@recipe.com', '管理员', true),
                                                                    ('testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'test@recipe.com', '测试用户', true);

-- 提交事务
COMMIT;