# 食谱生成系统 - 后端设计文档

## 1. 技术栈

### 核心框架
- **Spring Boot 3.2.x** - 主框架
- **Spring Web** - RESTful API
- **Spring Data JPA** - 数据持久化
- **Spring Validation** - 参数校验
- **Spring Cache** - 缓存支持

### 数据库
- **MySQL 8.0+** - 主数据库
- **Redis** - 缓存和会话管理

### 其他依赖
- **Lombok** - 简化代码
- **MapStruct** - 对象映射
- **Swagger/OpenAPI** - API文档
- **JWT** - 用户认证（可选）

## 2. 项目结构

```
src/main/java/com/recipe/
├── RecipeGeneratorApplication.java
├── config/                    # 配置类
│   ├── CorsConfig.java
│   ├── RedisConfig.java
│   └── SwaggerConfig.java
├── controller/                # 控制器层
│   ├── IngredientController.java
│   ├── RecipeController.java
│   ├── FavoriteController.java
│   ├── HistoryController.java
│   └── ShoppingListController.java
├── service/                   # 服务层
│   ├── IngredientService.java
│   ├── RecipeService.java
│   ├── RecipeGeneratorService.java
│   ├── FavoriteService.java
│   ├── HistoryService.java
│   └── ShoppingListService.java
├── repository/                # 数据访问层
│   ├── IngredientRepository.java
│   ├── RecipeRepository.java
│   ├── FavoriteRepository.java
│   ├── HistoryRepository.java
│   └── ShoppingListRepository.java
├── entity/                    # 实体类
│   ├── Ingredient.java
│   ├── Recipe.java
│   ├── RecipeStep.java
│   ├── Favorite.java
│   ├── History.java
│   └── ShoppingList.java
├── dto/                       # 数据传输对象
│   ├── request/
│   │   ├── GenerateRecipeRequest.java
│   │   ├── AddIngredientRequest.java
│   │   └── UpdateShoppingListRequest.java
│   └── response/
│       ├── RecipeResponse.java
│       ├── IngredientResponse.java
│       └── ApiResponse.java
├── enums/                     # 枚举类
│   ├── CuisineType.java      # 菜系
│   ├── FlavorType.java       # 口味
│   ├── DifficultyLevel.java  # 难度
│   ├── CookingTime.java      # 烹饪时间
│   └── IngredientCategory.java # 食材分类
├── exception/                 # 异常处理
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── BusinessException.java
└── util/                      # 工具类
    ├── RecipeMatchUtil.java
    └── DateUtil.java
```

## 3. 数据库设计

### 3.1 食材表 (ingredient)
```sql
CREATE TABLE ingredient (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '食材名称',
    category VARCHAR(50) NOT NULL COMMENT '分类：蔬菜/肉类/海鲜/主食/调味料',
    common_unit VARCHAR(20) COMMENT '常用单位：g/个/根',
    calories INT COMMENT '热量(可选)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category (category),
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 3.2 食谱表 (recipe)
```sql
CREATE TABLE recipe (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL COMMENT '菜品名称',
    cuisine_type VARCHAR(50) NOT NULL COMMENT '菜系：中餐/西餐/日韩/东南亚',
    flavor_types VARCHAR(100) COMMENT '口味：辣,甜,咸（多选）',
    cooking_time INT NOT NULL COMMENT '烹饪时间（分钟）',
    difficulty_level VARCHAR(20) NOT NULL COMMENT '难度：新手/家常/大厨',
    description TEXT COMMENT '菜品描述',
    image_url VARCHAR(500) COMMENT '图片URL',
    servings INT DEFAULT 2 COMMENT '份数',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_cuisine (cuisine_type),
    INDEX idx_time (cooking_time),
    INDEX idx_difficulty (difficulty_level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 3.3 食谱食材关联表 (recipe_ingredient)
```sql
CREATE TABLE recipe_ingredient (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recipe_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    quantity VARCHAR(50) NOT NULL COMMENT '数量：200g/2个',
    is_required BOOLEAN DEFAULT TRUE COMMENT '是否必需',
    FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredient(id),
    INDEX idx_recipe (recipe_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 3.4 烹饪步骤表 (recipe_step)
```sql
CREATE TABLE recipe_step (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recipe_id BIGINT NOT NULL,
    step_number INT NOT NULL COMMENT '步骤序号',
    description TEXT NOT NULL COMMENT '步骤描述',
    image_url VARCHAR(500) COMMENT '步骤图片',
    duration INT COMMENT '该步骤耗时（分钟）',
    FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE,
    INDEX idx_recipe (recipe_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 3.5 收藏表 (favorite)
```sql
CREATE TABLE favorite (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(100) NOT NULL COMMENT '用户ID（前期可用sessionId）',
    recipe_id BIGINT NOT NULL,
    rating INT COMMENT '评分：1-5星',
    comment TEXT COMMENT '评论',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_recipe (user_id, recipe_id),
    INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 3.6 历史记录表 (history)
```sql
CREATE TABLE history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(100) NOT NULL,
    recipe_id BIGINT NOT NULL,
    input_ingredients TEXT COMMENT '输入的食材JSON',
    filter_conditions TEXT COMMENT '筛选条件JSON',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE,
    INDEX idx_user (user_id),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 3.7 购物清单表 (shopping_list)
```sql
CREATE TABLE shopping_list (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(100) NOT NULL,
    ingredient_name VARCHAR(100) NOT NULL,
    quantity VARCHAR(50) NOT NULL,
    category VARCHAR(50) COMMENT '区域分类',
    note TEXT COMMENT '备注',
    is_purchased BOOLEAN DEFAULT FALSE COMMENT '是否已购买',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

## 4. API接口设计

### 4.1 食材管理 API

#### 获取食材分类列表
```
GET /api/ingredients/categories
Response: {
  "code": 200,
  "data": ["蔬菜类", "肉类", "海鲜类", "主食类", "调味料"]
}
```

#### 根据分类获取食材
```
GET /api/ingredients?category=蔬菜类
Response: {
  "code": 200,
  "data": [
    {"id": 1, "name": "西红柿", "category": "蔬菜类", "commonUnit": "个"},
    {"id": 2, "name": "土豆", "category": "蔬菜类", "commonUnit": "个"}
  ]
}
```

#### 搜索食材
```
GET /api/ingredients/search?keyword=鸡
Response: {
  "code": 200,
  "data": [
    {"id": 10, "name": "鸡肉", "category": "肉类", "commonUnit": "g"},
    {"id": 11, "name": "鸡蛋", "category": "主食类", "commonUnit": "个"}
  ]
}
```

### 4.2 食谱生成 API

#### 生成食谱推荐
```
POST /api/recipes/generate
Request: {
  "ingredients": [
    {"name": "鸡肉", "quantity": "200g"},
    {"name": "土豆", "quantity": "2个"}
  ],
  "cuisineType": "中餐",
  "flavorTypes": ["辣", "咸"],
  "cookingTime": 30,
  "difficultyLevel": "家常"
}
Response: {
  "code": 200,
  "data": [
    {
      "id": 1,
      "name": "宫保鸡丁",
      "cuisineType": "中餐",
      "flavorTypes": ["辣", "咸"],
      "cookingTime": 25,
      "difficultyLevel": "家常",
      "ingredients": [
        {"name": "鸡肉", "quantity": "200g", "hasIngredient": true},
        {"name": "花生", "quantity": "50g", "hasIngredient": false}
      ],
      "steps": [
        {"stepNumber": 1, "description": "鸡肉切丁，加料酒腌制10分钟"},
        {"stepNumber": 2, "description": "热油爆香葱姜蒜"}
      ],
      "missingIngredients": ["花生", "干辣椒"]
    }
  ]
}
```

#### 获取食谱详情
```
GET /api/recipes/{id}
Response: {
  "code": 200,
  "data": {
    "id": 1,
    "name": "宫保鸡丁",
    "description": "经典川菜",
    "ingredients": [...],
    "steps": [...]
  }
}
```

### 4.3 收藏 API

#### 添加收藏
```
POST /api/favorites
Request: {
  "userId": "user123",
  "recipeId": 1
}
```

#### 获取收藏列表
```
GET /api/favorites?userId=user123
Response: {
  "code": 200,
  "data": [
    {
      "id": 1,
      "recipe": {...},
      "rating": 5,
      "comment": "很好吃",
      "createdAt": "2026-01-29T10:00:00"
    }
  ]
}
```

#### 评价收藏
```
PUT /api/favorites/{id}/rate
Request: {
  "rating": 5,
  "comment": "非常好吃"
}
```

#### 删除收藏
```
DELETE /api/favorites/{id}
```

### 4.4 历史记录 API

#### 获取历史记录
```
GET /api/history?userId=user123&page=0&size=10
Response: {
  "code": 200,
  "data": {
    "content": [
      {
        "id": 1,
        "recipe": {...},
        "inputIngredients": [...],
        "createdAt": "2026-01-29T10:00:00"
      }
    ],
    "totalPages": 5,
    "totalElements": 50
  }
}
```

#### 删除历史记录
```
DELETE /api/history/{id}
```

### 4.5 购物清单 API

#### 获取购物清单
```
GET /api/shopping-list?userId=user123
Response: {
  "code": 200,
  "data": [
    {
      "id": 1,
      "ingredientName": "鸡肉",
      "quantity": "500g",
      "category": "肉类区",
      "note": "买新鲜的",
      "isPurchased": false
    }
  ]
}
```

#### 添加到购物清单
```
POST /api/shopping-list
Request: {
  "userId": "user123",
  "items": [
    {"ingredientName": "鸡肉", "quantity": "500g", "category": "肉类区"}
  ]
}
```

#### 更新购物清单项
```
PUT /api/shopping-list/{id}
Request: {
  "quantity": "600g",
  "note": "买土鸡",
  "isPurchased": true
}
```

#### 清空购物清单
```
DELETE /api/shopping-list?userId=user123
```

## 5. 核心业务逻辑

### 5.1 食谱匹配算法

```java
public class RecipeMatchUtil {
    /**
     * 计算食谱匹配度
     * @param userIngredients 用户拥有的食材
     * @param recipeIngredients 食谱需要的食材
     * @return 匹配度 0-100
     */
    public static int calculateMatchScore(
        List<String> userIngredients,
        List<RecipeIngredient> recipeIngredients
    ) {
        int totalRequired = 0;
        int matched = 0;

        for (RecipeIngredient ri : recipeIngredients) {
            if (ri.isRequired()) {
                totalRequired++;
                if (userIngredients.contains(ri.getIngredient().getName())) {
                    matched++;
                }
            }
        }

        return totalRequired == 0 ? 0 : (matched * 100 / totalRequired);
    }
}
```

### 5.2 食谱生成策略

1. **基于数据库匹配**（初期方案）
   - 根据用户食材和筛选条件查询数据库
   - 计算匹配度排序
   - 返回Top N结果

2. **AI生成**（进阶方案）
   - 集成OpenAI API
   - 根据食材和条件生成新食谱
   - 保存到数据库供后续使用

## 6. 配置文件

### application.yml
```yaml
spring:
  application:
    name: recipe-generator
  datasource:
    url: jdbc:mysql://localhost:3306/recipe_db?useUnicode=true&characterEncoding=utf8
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  redis:
    host: localhost
    port: 6379

server:
  port: 8080

# CORS配置
cors:
  allowed-origins: http://localhost:3000
  allowed-methods: GET,POST,PUT,DELETE

# 缓存配置
cache:
  recipe-ttl: 3600  # 食谱缓存1小时
```

## 7. 开发优先级

### Phase 1: 基础功能（1-2天）
- ✅ 项目搭建（Spring Boot + MySQL）
- ✅ 数据库表创建
- ✅ 食材管理API
- ✅ 基础食谱查询API

### Phase 2: 核心功能（2-3天）
- ✅ 食谱生成逻辑
- ✅ 匹配算法实现
- ✅ 收藏功能
- ✅ 历史记录

### Phase 3: 扩展功能（1-2天）
- ✅ 购物清单
- ✅ 评价功能
- ✅ 缓存优化

### Phase 4: 优化（1天）
- ✅ API文档完善
- ✅ 异常处理
- ✅ 性能优化

## 8. 测试数据准备

需要准备：
- 100+ 常见食材数据
- 50+ 常见菜谱数据
- 每个菜谱包含完整的食材和步骤

## 9. 部署建议

- 使用Docker容器化
- MySQL + Redis 独立容器
- Nginx反向代理
- 配置HTTPS证书
