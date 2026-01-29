# Recipe Generator Backend

智能食谱生成系统后端 - 基于Spring Boot 3 + Claude AI

## 项目简介

根据用户冰箱里的食材，结合菜系、口味、时间、难度等筛选条件，使用Claude AI智能生成个性化食谱推荐。

## 技术栈

- **Java 17**
- **Spring Boot 3.2.2**
- **Spring Security + JWT** - 用户认证
- **Spring Data JPA** - 数据持久化
- **MySQL 8.0** - 主数据库
- **Redis** - 缓存
- **Claude API** - AI食谱生成
- **Swagger/OpenAPI** - API文档

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 2. 数据库配置

创建数据库：

```sql
CREATE DATABASE recipe_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 配置文件

修改 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/recipe_db
    username: your_username
    password: your_password

  data:
    redis:
      host: localhost
      port: 6379

jwt:
  secret: your-256-bit-secret-key-change-this-in-production

claude:
  api-key: ${CLAUDE_API_KEY}  # 设置环境变量或直接填写
```

### 4. 设置Claude API Key

**方式1：环境变量（推荐）**

Windows:
```cmd
set CLAUDE_API_KEY=your-claude-api-key
```

Linux/Mac:
```bash
export CLAUDE_API_KEY=your-claude-api-key
```

**方式2：直接修改配置文件**

在 `application.yml` 中：
```yaml
claude:
  api-key: sk-ant-xxxxx
```

### 5. 运行项目

```bash
# 编译
mvn clean package

# 运行
mvn spring-boot:run
```

或者使用IDE直接运行 `RecipeGeneratorApplication.java`

### 6. 访问API文档

启动后访问：http://localhost:8080/api/swagger-ui.html

## API接口

### 认证接口

#### 注册
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456",
  "email": "test@example.com",
  "nickname": "测试用户"
}
```

#### 登录
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456"
}
```

返回：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "username": "testuser"
  }
}
```

### 食谱接口

#### 生成食谱
```http
POST /api/recipes/generate
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "ingredients": [
    {"name": "鸡肉", "quantity": "200g"},
    {"name": "土豆", "quantity": "2个"}
  ],
  "cuisineType": "CHINESE",
  "flavorTypes": ["SPICY", "SALTY"],
  "cookingTime": 30,
  "difficultyLevel": "HOME_COOKING"
}
```

## 数据库表结构

### 核心表

- `user` - 用户表
- `ingredient` - 食材表
- `recipe` - 食谱表
- `recipe_ingredient` - 食谱食材关联表
- `recipe_step` - 烹饪步骤表
- `favorite` - 收藏表
- `history` - 历史记录表
- `shopping_list` - 购物清单表

详细设计见 [BACKEND_DESIGN.md](BACKEND_DESIGN.md)

## 项目结构

```
src/main/java/com/recipe/
├── RecipeGeneratorApplication.java    # 启动类
├── config/                             # 配置类
│   ├── CorsConfig.java
│   ├── SecurityConfig.java
│   ├── SwaggerConfig.java
│   └── ClaudeConfig.java
├── controller/                         # 控制器
│   ├── AuthController.java
│   └── RecipeController.java
├── service/                            # 服务层
│   ├── AuthService.java
│   └── ClaudeRecipeGeneratorService.java
├── repository/                         # 数据访问层
├── entity/                             # 实体类
├── dto/                                # 数据传输对象
├── enums/                              # 枚举类
├── security/                           # 安全相关
└── exception/                          # 异常处理
```

## 开发计划

### Phase 1: 基础功能 ✅
- [x] 项目搭建
- [x] 用户认证（JWT）
- [x] 数据库设计
- [x] Claude API集成

### Phase 2: 核心功能 🚧
- [ ] 食材管理API
- [ ] 食谱匹配算法优化
- [ ] 收藏功能
- [ ] 历史记录

### Phase 3: 扩展功能
- [ ] 购物清单
- [ ] 评价系统
- [ ] 图片上传
- [ ] 导出功能

### Phase 4: 优化
- [ ] 缓存优化
- [ ] 性能测试
- [ ] 单元测试
- [ ] Docker部署

## 常见问题

### 1. Claude API调用失败

检查：
- API Key是否正确
- 网络是否可访问 api.anthropic.com
- 账户余额是否充足

### 2. JWT认证失败

检查：
- Token是否过期
- Authorization header格式：`Bearer {token}`
- jwt.secret配置是否正确

### 3. 数据库连接失败

检查：
- MySQL服务是否启动
- 数据库名、用户名、密码是否正确
- 时区配置：`serverTimezone=Asia/Shanghai`

## 贡献指南

欢迎提交Issue和Pull Request！

## 许可证

MIT License

## 联系方式

- 项目地址：https://github.com/your-repo/recipe-generator-backend
- 问题反馈：https://github.com/your-repo/recipe-generator-backend/issues
