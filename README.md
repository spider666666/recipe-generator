# 🍳 AI智能菜谱生成器

<div align="center">

![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-brightgreen.svg)
![Vue](https://img.shields.io/badge/Vue-3.x-green.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)

基于Claude AI的智能菜谱推荐系统，根据现有食材智能生成个性化菜谱

[功能特性](#功能特性) • [技术栈](#技术栈) • [快速开始](#快速开始) • [项目结构](#项目结构) • [API文档](#api文档)

</div>

---

## 📖 项目简介

AI智能菜谱生成器是一个基于Claude AI的智能烹饪助手，帮助用户根据现有食材快速生成个性化菜谱。系统支持多种菜系、口味偏好和难度等级，让做饭变得更简单、更有趣。

### ✨ 核心亮点

- 🤖 **AI智能推荐**：集成Claude Sonnet 4.5，根据食材智能生成菜谱
- 🎯 **个性化定制**：支持菜系、口味、难度、烹饪时间等多维度筛选
- 📝 **详细步骤**：提供完整的食材清单和分步烹饪指导
- 🛒 **购物清单**：一键生成缺失食材的购物清单
- ⭐ **收藏管理**：收藏喜欢的菜谱，随时查看历史记录
- 💾 **组合保存**：保存常用食材组合，快速生成菜谱

---

## 🎯 功能特性

### 1. 智能菜谱生成
- 根据现有食材智能匹配菜谱
- 支持中餐、西餐、日韩料理、东南亚菜等多种菜系
- 可选择辣、甜、咸、酸、清淡等口味
- 新手、家常、大厨三种难度等级
- 自定义烹饪时间和份数

### 2. 食材管理
- 92+种常见食材库
- 智能食材搜索（支持模糊匹配）
- 食材分类管理（蔬菜、肉类、海鲜、主食、调味料）
- 常用食材组合保存

### 3. 购物清单
- 一键添加菜谱所需食材
- 智能合并相同食材用量
- 购买状态标记
- 分类展示（按食材类别）

### 4. 收藏与历史
- 收藏喜欢的菜谱
- 查看生成历史
- 菜谱详情查看
- 菜谱导出功能

### 5. 用户系统
- JWT身份认证
- 用户注册/登录
- Token自动刷新（24小时有效期）

---

## 🛠 技术栈

### 后端技术
- **框架**: Spring Boot 3.2.2
- **数据库**: MySQL 8.0
- **ORM**: MyBatis-Plus 3.5.5
- **安全**: Spring Security + JWT
- **API文档**: Swagger/OpenAPI 3.0
- **AI集成**: Claude API (Sonnet 4.5)
- **构建工具**: Maven

### 前端技术
- **框架**: Vue 3 (Composition API)
- **UI组件**: Element Plus
- **HTTP客户端**: Axios
- **构建工具**: Vite

### 数据库设计
- 用户表 (user)
- 食材表 (ingredient)
- 菜谱表 (recipe)
- 菜谱-食材关联表 (recipe_ingredient)
- 菜谱步骤表 (recipe_step)
- 收藏表 (favorite)
- 历史记录表 (history)
- 购物清单表 (shopping_list)
- 食材组合表 (ingredient_combo)

---

## 🚀 快速开始

### 环境要求

- Java 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

### 后端启动

1. **克隆项目**
```bash
git clone https://github.com/yourusername/recipe-generator.git
cd recipe-generator
```

2. **配置数据库**
```bash
# 创建数据库
mysql -u root -p < recipe-generator-backend/init.sql
```

3. **配置Claude API**

编辑 `recipe-generator-backend/src/main/resources/application.yml`：
```yaml
claude:
  api-key: your-claude-api-key-here
  api-url: https://api.anthropic.com/v1/messages
```

4. **启动后端服务**
```bash
cd recipe-generator-backend
mvn clean package
java -jar target/recipe-generator-backend-1.0.0.jar
```

后端服务将在 `http://localhost:8080/api` ��动

### 前端启动

1. **安装依赖**
```bash
cd recipe-generator-frontend
npm install
```

2. **配置API地址**

编辑 `recipe-generator-frontend/src/utils/api.js`：
```javascript
const API_BASE_URL = 'http://localhost:8080/api'
```

3. **启动前端服务**
```bash
npm run dev
```

前端服务将在 `http://localhost:5173` 启动

### 默认账号

- 用户名: `admin`
- 密码: `123456`

---

## 📁 项目结构

```
recipe-generator/
├── recipe-generator-backend/          # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/recipe/
│   │   │   │   ├── controller/       # 控制器层
│   │   │   │   ├── service/          # 服务层
│   │   │   │   ├── mapper/           # 数据访问层
│   │   │   │   ├── entity/           # 实体类
│   │   │   │   ├── dto/              # 数据传输对象
│   │   │   │   ├── config/           # 配置类
│   │   │   │   └── security/         # 安全配置
│   │   │   └── resources/
│   │   │       └── application.yml   # 配置文件
│   │   └── test/                     # 测试代码
│   ├── init.sql                      # 数据库初始化脚本
│   └── pom.xml                       # Maven配置
│
├── recipe-generator-frontend/         # 前端项目
│   ├── src/
│   │   ├── views/                    # 页面组件
│   │   │   ├── HomePage.vue         # 首页（食材选择）
│   │   │   ├── RecipesPage.vue      # ���谱列表
│   │   │   ├── FavoritesPage.vue    # 收藏页面
│   │   │   ├── ShoppingPage.vue     # 购物清单
│   │   │   └── LoginPage.vue        # 登录页面
│   │   ├── components/               # 公共组件
│   │   ├── utils/                    # 工具函数
│   │   │   └── api.js               # API接口
│   │   ├── App.vue                   # 根组件
│   │   └── main.js                   # 入口文件
│   ├── index.html                    # HTML模板
│   └── package.json                  # NPM配置
│
└── README.md                          # 项目文档
```

---

## 📡 API文档

### 认证接口

#### 用户注册
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "user",
  "password": "password",
  "email": "user@example.com"
}
```

#### 用户登录
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "user",
  "password": "password"
}
```

### 菜谱接口

#### 生成菜谱
```http
POST /api/recipes/generate
Authorization: Bearer {token}
Content-Type: application/json

{
  "ingredientIds": [1, 2, 3],
  "cuisineType": "CHINESE",
  "flavorTypes": ["SPICY"],
  "difficultyLevel": "HOME_COOKING",
  "maxCookingTime": 30,
  "servings": 2
}
```

#### 删除菜谱
```http
DELETE /api/recipes/{id}
Authorization: Bearer {token}
```

### 食材接口

#### 获取所有食材
```http
GET /api/ingredients
Authorization: Bearer {token}
```

#### 搜索食材
```http
GET /api/ingredients/search?name=土豆
Authorization: Bearer {token}
```

### 购物清单接口

#### 添加购物清单项
```http
POST /api/shopping-list
Authorization: Bearer {token}
Content-Type: application/json

{
  "ingredientId": 1,
  "quantity": "2个",
  "note": ""
}
```

#### 获取购物清单
```http
GET /api/shopping-list
Authorization: Bearer {token}
```

#### 更新购买状态
```http
PUT /api/shopping-list/{itemId}/purchase?isPurchased=true
Authorization: Bearer {token}
```

### 收藏接口

#### 添加收藏
```http
POST /api/favorites/{recipeId}
Authorization: Bearer {token}
```

#### 获取收藏列表
```http
GET /api/favorites
Authorization: Bearer {token}
```

#### 取消收藏
```http
DELETE /api/favorites/{recipeId}
Authorization: Bearer {token}
```

### 历史记录接口

#### 获取历史记录
```http
GET /api/history
Authorization: Bearer {token}
```

完整API文档请访问：`http://localhost:8080/api/swagger-ui.html`

---

## 🎨 界面预览

### 首页 - 食材选择
选择现有食材，设置菜谱偏好，一键生成个性化菜谱。

### 菜谱列表
展示生成的菜谱，包含食材清单、烹饪步骤、难度等级等信息。

### 购物清单
自动生成缺失食材的购物清单，支持分类展示和购买状态管理。

### 收藏管理
收藏喜欢的菜谱，随时查看详细信息。

---

## 🔧 配置说明

### JWT配置
```yaml
jwt:
  secret: your-secret-key              # JWT密钥（生产环境请修改）
  expiration: 86400000                 # 访问令牌有效期（24小时）
  refresh-expiration: 604800000        # 刷新令牌有效期（7天）
```

### Claude API配置
```yaml
claude:
  api-key: ${CLAUDE_API_KEY}           # Claude API密钥
  api-url: https://api.anthropic.com/v1/messages
  model: claude-sonnet-4-5-20250929    # 使用的模型
  max-tokens: 4096                     # 最大token数
  temperature: 0.7                     # 温度参数
  timeout: 60000                       # 超时时间（毫秒）
```

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/recipe_db
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### CORS配置
```yaml
cors:
  allowed-origins: http://localhost:3000,http://localhost:5173
  allowed-methods: GET,POST,PUT,DELETE,OPTIONS
  allowed-headers: "*"
  allow-credentials: true
```

---

## 🤝 贡献指南

欢迎贡献代码、报告问题或提出建议！

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

---

## 📝 开发计划

- [ ] 支持更多菜系和食材
- [ ] 添加菜谱评分和评论功能
- [ ] 实现菜谱分享功能
- [ ] 添加营养成分分析
- [ ] 支持图片上传和识别
- [ ] 移动端适配
- [ ] 多语言支持

---

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

---

## 👥 作者

- **Your Name** - *Initial work*

---

## 🙏 致谢

- [Claude AI](https://www.anthropic.com/) - 提供强大的AI能力
- [Spring Boot](https://spring.io/projects/spring-boot) - 优秀的Java框架
- [Vue.js](https://vuejs.org/) - 渐进式JavaScript框架
- [Element Plus](https://element-plus.org/) - 优秀的Vue 3组件库

---

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 提交 [Issue](https://github.com/yourusername/recipe-generator/issues)
- 发送邮件至：your.email@example.com

---

<div align="center">

**⭐ 如果这个项目对你有帮助，请给一个Star！⭐**

Made with ❤️ by [Your Name]

</div>
