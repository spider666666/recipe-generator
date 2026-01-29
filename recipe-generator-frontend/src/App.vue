<template>
  <div id="app">
    <!-- 导航栏 -->
    <el-menu
      :default-active="activeMenu"
      mode="horizontal"
      @select="handleMenuSelect"
      class="app-menu"
    >
      <div class="logo">🍳 智能食谱生成器</div>
      <el-menu-item index="home">
        <el-icon><HomeFilled /></el-icon>
        <span>首页</span>
      </el-menu-item>
      <el-menu-item index="recipes">
        <el-icon><Reading /></el-icon>
        <span>菜谱</span>
      </el-menu-item>
      <el-menu-item index="favorites">
        <el-icon><Star /></el-icon>
        <span>收藏夹</span>
      </el-menu-item>
      <el-menu-item index="shopping">
        <el-icon><ShoppingCart /></el-icon>
        <span>购物清单</span>
      </el-menu-item>
    </el-menu>

    <!-- 主内容区 -->
    <div class="main-content">
      <component :is="currentView" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, markRaw, onMounted, onUnmounted } from 'vue'
import { HomeFilled, Reading, Star, ShoppingCart } from '@element-plus/icons-vue'
import HomePage from './views/HomePage.vue'
import RecipesPage from './views/RecipesPage.vue'
import FavoritesPage from './views/FavoritesPage.vue'
import ShoppingPage from './views/ShoppingPage.vue'

const activeMenu = ref('home')

const views = {
  home: markRaw(HomePage),
  recipes: markRaw(RecipesPage),
  favorites: markRaw(FavoritesPage),
  shopping: markRaw(ShoppingPage)
}

const currentView = computed(() => views[activeMenu.value])

const handleMenuSelect = (index) => {
  activeMenu.value = index
}

// 监听自定义导航事件
const handleNavigate = (event) => {
  activeMenu.value = event.detail
}

onMounted(() => {
  window.addEventListener('navigate', handleNavigate)
})

onUnmounted(() => {
  window.removeEventListener('navigate', handleNavigate)
})
</script>

<style scoped>
.app-menu {
  position: sticky;
  top: 0;
  z-index: 1000;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.logo {
  font-size: 20px;
  font-weight: bold;
  padding: 0 20px;
  line-height: 60px;
  color: #409eff;
}

.main-content {
  min-height: calc(100vh - 60px);
  background: #f5f7fa;
}
</style>
