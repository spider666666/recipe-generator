# 快速启动指南

## 🚀 快速开始

### 1. 安装依赖
```bash
npm install
```

### 2. 启动开发服务器
```bash
npm run dev
```

服务器将在 http://localhost:3000 或 http://localhost:3001 启动

### 3. 访问应用
在浏览器中打开显示的地址即可使用

## 📋 功能清单

### 首页 - 食材选择
- 浏览 70+ 种预设食材
- 按分类查看（蔬菜、肉类、海鲜、主食、调味料）
- 搜索食材
- 添加自定义食材
- 设置筛选条件（菜系、口味、时间、难度）
- 生成食谱

### 菜谱页 - 查看推荐
- 查看生成的菜谱
- 查看详细步骤
- 收藏喜欢的菜谱
- 添加食材到购物清单
- 评分和评论

### 收藏夹 - 管理收藏
- 查看收藏的菜谱
- 查看历史记录
- 重新生成菜谱
- 导出菜谱

### 购物清单 - 采购管理
- 查看需要购买的食材
- 按分类管理
- 勾选已购买
- 添加备注
- 导出清单

## 🎯 使用流程

1. **选择食材** → 在首页选择你拥有的食材
2. **设置筛选** → 选择菜系、口味、时间、难度
3. **生成菜谱** → 点击"生成食谱"按钮
4. **查看推荐** → 浏览推荐的菜谱
5. **查看详情** → 点击卡片查看详细步骤
6. **收藏菜谱** → 收藏喜欢的菜谱
7. **购物清单** → 添加缺少的食材到购物清单

## 🔧 开发命令

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build

# 预览生产版本
npm run preview
```

## 📦 项目结构

```
recipe-generator-frontend/
├── src/
│   ├── components/          # 组件
│   │   └── IngredientGrid.vue
│   ├── views/              # 页面
│   │   ├── HomePage.vue
│   │   ├── RecipesPage.vue
│   │   ├── FavoritesPage.vue
│   │   └── ShoppingPage.vue
│   ├── utils/              # 工具
│   │   ├── api.js
│   │   └── ingredientsData.js
│   ├── App.vue
│   ├── main.js
│   └── style.css
├── index.html
├── vite.config.js
└── package.json
```

## 🌐 API 配置

前端默认连接到 `http://localhost:8000` 的后端服务。

如需修改，请编辑 `vite.config.js` 中的 proxy 配置。

## 💾 数据存储

应用使用 LocalStorage 存储数据：
- 当前菜谱
- 收藏列表
- 历史记录
- 评分评论
- 购物清单
- 常用组合

## 🎨 技术栈

- Vue 3 (Composition API)
- Element Plus
- Vite
- Axios

## ❓ 常见问题

### Q: 端口被占用怎么办？
A: Vite 会自动尝试下一个可用端口（3001, 3002...）

### Q: 如何清除所有数据？
A: 打开浏览器开发者工具 → Application → Local Storage → 删除所有项

### Q: 后端 API 地址如何配置？
A: 修改 `vite.config.js` 中的 `proxy.target` 配置

## 📝 注意事项

1. 需要后端服务运行在 8000 端口
2. 首次使用需要先选择食材
3. 数据存储在浏览器本地
4. 建议使用 Chrome 浏览器

## 🎉 开始使用

现在你可以开始使用智能食谱生成器了！

1. 启动开发服务器：`npm run dev`
2. 打开浏览器访问显示的地址
3. 开始选择食材，生成美味菜谱！

祝你烹饪愉快！🍳
