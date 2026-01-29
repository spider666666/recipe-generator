# 智能食谱生成器 - 前端

基于 Vue 3 + Element Plus 的智能食谱生成器前端应用。

## 功能特性

### ✅ 已实现功能

#### 阶段1：基础布局
- ✅ 创建主页面结构
- ✅ 全局样式配置
- ✅ 导航栏（Logo + 4个主要页面入口）
- ✅ 单页应用（SPA）页面切换逻辑

#### 阶段2：食材输入模块
- ✅ 食材分类标签页（蔬菜、肉类、海鲜、主食、调味料）
- ✅ 实时搜索过滤功能
- ✅ 自由输入框（添加未列出的食材）
- ✅ 数量输入功能
- ✅ 已选食材展示区（带删除功能）
- ✅ 保存常用组合（localStorage存储）

#### 阶段3：筛选器UI
- ✅ 菜系选择（中餐/西餐/日韩/东南亚）
- ✅ 口味多选（辣/甜/咸/酸/清淡）
- ✅ 时间筛选（15分/30分/1小时）
- ✅ 难度筛选（新手/家常/大厨）
- ✅ 生成食谱按钮

#### 阶段4：菜谱展示页
- ✅ 菜谱卡片组件设计
- ✅ 食材清单（区分已有/缺少）
- ✅ 烹饪步骤展示
- ✅ 时间 + 难度标签
- ✅ 收藏功能
- ✅ 详情弹窗查看
- ✅ 加入购物清单功能

#### 阶段5：收藏夹与历史
- ✅ 收藏夹页面
- ✅ 历史记录页面
- ✅ 评价功能（星级评分 + 文字评论）
- ✅ 删除收藏/历史记录
- ✅ 导出菜谱功能

#### 阶段6：购物清单页面
- ✅ 按分类展示购物清单
- ✅ 编辑数量功能
- ✅ 添加备注功能
- ✅ 导出清单功能
- ✅ 清空清单功能

## 技术栈

- **框架**: Vue 3 (Composition API)
- **UI组件库**: Element Plus
- **构建工具**: Vite
- **HTTP客户端**: Axios
- **数据存储**: LocalStorage

## 项目结构

```
recipe-generator-frontend/
├── src/
│   ├── components/          # 可复用组件
│   │   └── IngredientGrid.vue
│   ├── views/              # 页面组件
│   │   ├── HomePage.vue
│   │   ├── RecipesPage.vue
│   │   ├── FavoritesPage.vue
│   │   └── ShoppingPage.vue
│   ├── utils/              # 工具函数
│   │   ├── api.js
│   │   └── ingredientsData.js
│   ├── App.vue             # 根组件
│   ├── main.js             # 入口文件
│   └── style.css           # 全局样式
├── index.html              # HTML模板
├── vite.config.js          # Vite配置
└── package.json            # 项目配置

```

## 快速开始

### 安装依赖

```bash
npm install
```

### 开发模式

```bash
npm run dev
```

访问 http://localhost:3000

### 构建生产版本

```bash
npm run build
```

### 预览生产版本

```bash
npm run preview
```

## API 配置

前端通过 Vite 代理连接后端 API：

- 开发环境：`/api` 代理到 `http://localhost:8000`
- 生产环境：需要配置实际的后端地址

## 数据存储

应用使用 LocalStorage 存储以下数据：

- `current-recipes`: 当前生成的菜谱
- `favorites`: 收藏的菜谱
- `recipe-history`: 生成历史记录
- `recipe-ratings`: 菜谱评分和评论
- `shopping-list`: 购物清单
- `ingredient-combos`: 常用食材组合

## 主要功能说明

### 1. 食材选择
- 支持按分类浏览食材
- 实时搜索功能
- 自定义添加食材
- 保存常用组合

### 2. 智能筛选
- 多维度筛选条件
- 菜系、口味、时间、难度
- 灵活组合筛选

### 3. 菜谱展示
- 卡片式布局
- 食材可用性标识
- 详细步骤说明
- 一键收藏

### 4. 收藏管理
- 收藏夹功能
- 历史记录追踪
- 评分评论系统
- 导出功能

### 5. 购物清单
- 按分类管理
- 勾选已购买
- 添加备注
- 导出清单

## 浏览器支持

- Chrome (推荐)
- Firefox
- Safari
- Edge

## 开发说明

### 组件通信
- 使用自定义事件 `navigate` 进行页面跳转
- 使用 LocalStorage 进行数据持久化

### 样式规范
- 使用 Element Plus 主题色
- 响应式设计
- 统一的间距和圆角

## 后续优化

- [ ] 添加图片上传功能
- [ ] 实现菜谱分享功能
- [ ] 添加用户登录系统
- [ ] 优化移动端体验
- [ ] 添加国际化支持

## License

MIT
