<template>
  <div class="favorites-page">
    <div class="title-container">
      <img src="@/assets/images/厨师猫.png" alt="厨师猫" class="title-cat-left" />
      <h1 class="page-title">💖 我的收藏</h1>
      <img src="@/assets/images/厨师猫.png" alt="厨师猫" class="title-cat-right" />
    </div>

    <el-container>
      <el-main>

        <el-tabs v-model="activeTab" class="tabs">
          <el-tab-pane label="⭐ 收藏夹" name="favorites">
            <div v-if="favorites.length === 0" class="empty-state">
              <img src="@/assets/images/困惑猫.png" alt="困惑猫" class="empty-cat-icon" />
              <div class="empty-text">还没有收藏的菜谱喵~</div>
            </div>
            <div v-else class="recipes-grid">
              <el-card
                v-for="recipe in favorites"
                :key="recipe.id"
                class="recipe-card"
                shadow="hover"
              >
                <template #header>
                  <div class="card-header">
                    <span class="recipe-name">{{ recipe.name }}</span>
                    <el-button
                      :icon="Delete"
                      type="danger"
                      circle
                      @click="removeFavorite(recipe.id)"
                    />
                  </div>
                </template>

                <div class="recipe-content">
                  <div class="recipe-tags">
                    <el-tag v-if="recipe.cuisine" type="info">{{ getCuisineLabel(recipe.cuisine) }}</el-tag>
                    <el-tag type="success">{{ recipe.time }}分钟</el-tag>
                    <el-tag :type="getDifficultyType(recipe.difficulty)">
                      {{ getDifficultyLabel(recipe.difficulty) }}
                    </el-tag>
                  </div>

                  <div class="favorite-info">
                    <span class="favorite-date">
                      收藏于：{{ formatDate(recipe.favoritedAt) }}
                    </span>
                  </div>

                  <div class="recipe-actions">
                    <el-button type="primary" @click="viewRecipe(recipe)">
                      查看详情
                    </el-button>
                  </div>
                </div>
              </el-card>
            </div>
          </el-tab-pane>

          <el-tab-pane label="📜 历史记录" name="history">
            <div v-if="history.length === 0" class="empty-state">
              <img src="@/assets/images/困惑猫.png" alt="困惑猫" class="empty-cat-icon" />
              <div class="empty-text">还没有生成记录喵~</div>
            </div>
            <div v-else class="history-list">
              <el-card
                v-for="record in history"
                :key="record.id"
                class="history-card"
                shadow="hover"
              >
                <div class="history-header">
                  <div class="history-date">
                    {{ formatDate(record.createdAt) }}
                  </div>
                  <el-button
                    :icon="Delete"
                    type="danger"
                    text
                    @click="removeHistory(record.id)"
                  >
                    删除
                  </el-button>
                </div>

                <div class="history-content">
                  <div class="history-ingredients" v-if="record.ingredients && record.ingredients.length > 0">
                    <strong>使用食材：</strong>
                    <el-space wrap>
                      <el-tag
                        v-for="(ing, index) in record.ingredients"
                        :key="index"
                        size="small"
                      >
                        {{ ing.name }}
                      </el-tag>
                    </el-space>
                  </div>

                  <div class="history-filters" v-if="hasFilters(record.filters)">
                    <strong>筛选条件：</strong>
                    <el-space wrap>
                      <el-tag v-if="record.filters.cuisine" size="small" type="info">
                        {{ getCuisineLabel(record.filters.cuisine) }}
                      </el-tag>
                      <el-tag
                        v-for="taste in record.filters.tastes"
                        :key="taste"
                        size="small"
                        type="warning"
                      >
                        {{ getTasteLabel(taste) }}
                      </el-tag>
                      <el-tag v-if="record.filters.time" size="small" type="success">
                        {{ record.filters.time }}分钟
                      </el-tag>
                      <el-tag v-if="record.filters.difficulty" size="small">
                        {{ getDifficultyLabel(record.filters.difficulty) }}
                      </el-tag>
                    </el-space>
                  </div>

                  <div class="history-recipes">
                    <strong>生成了 {{ record.recipes.length }} 道菜谱：</strong>
                    <div class="recipe-names">
                      <span
                        v-for="(recipe, index) in record.recipes"
                        :key="index"
                        class="recipe-link"
                        @click="viewRecipe(recipe)"
                      >
                        {{ recipe.name }}
                      </span>
                    </div>
                  </div>

                  <el-button
                    type="primary"
                    size="small"
                    @click="regenerate(record)"
                  >
                    重新生成
                  </el-button>
                </div>
              </el-card>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-main>
    </el-container>

    <!-- 详情弹窗 -->
    <el-dialog
      v-model="detailVisible"
      :title="currentRecipe?.name"
      width="800px"
    >
      <div v-if="currentRecipe" class="recipe-detail">
        <div class="detail-tags">
          <el-tag v-if="currentRecipe.cuisine" type="info" size="large">
            {{ getCuisineLabel(currentRecipe.cuisine) }}
          </el-tag>
          <el-tag type="success" size="large">{{ currentRecipe.time }}分钟</el-tag>
          <el-tag :type="getDifficultyType(currentRecipe.difficulty)" size="large">
            {{ getDifficultyLabel(currentRecipe.difficulty) }}
          </el-tag>
        </div>

        <div class="detail-section">
          <h3>📝 所需食材</h3>
          <el-table :data="currentRecipe.ingredients" style="width: 100%">
            <el-table-column label="状态" width="80">
              <template #default="{ row }">
                <span :style="{ fontSize: '20px' }">
                  {{ row.available ? '✅' : '❌' }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="食材" />
            <el-table-column prop="amount" label="用量" />
          </el-table>
        </div>

        <div class="detail-section">
          <h3>👨‍🍳 烹饪步骤</h3>
          <el-steps direction="vertical" :active="currentRecipe.steps.length">
            <el-step
              v-for="(step, index) in currentRecipe.steps"
              :key="index"
              :title="`步骤 ${index + 1}`"
              :description="step"
            />
          </el-steps>
        </div>
      </div>

      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 背景装饰猫爪 -->
    <div class="paw-decoration paw-1"></div>
    <div class="paw-decoration paw-2"></div>
    <div class="paw-decoration paw-3"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'
import { getFavoritesAPI, removeFavoriteAPI, getHistoryAPI, deleteHistoryAPI } from '../utils/api'

const activeTab = ref('favorites')
const favorites = ref([])
const history = ref([])
const detailVisible = ref(false)
const currentRecipe = ref(null)

onMounted(() => {
  // 清除旧的 localStorage 数据
  localStorage.removeItem('favorites')
  localStorage.removeItem('recipe-history')
  loadData()
})

const loadData = async () => {
  // 先清空数据
  favorites.value = []
  history.value = []

  try {
    // 加载收藏
    const favResponse = await getFavoritesAPI()
    console.log('收藏数据响应:', favResponse)
    if (favResponse.data && Array.isArray(favResponse.data)) {
      favorites.value = favResponse.data.map(fav => {
        const recipe = fav.recipe
        return {
          id: fav.recipeId,
          name: recipe.name,
          cuisine: recipe.cuisineType,
          time: recipe.cookingTime,
          difficulty: recipe.difficultyLevel,
          ingredients: recipe.ingredients?.map(ing => ({
            ingredientId: ing.ingredientId,  // 保留食材ID
            name: ing.name,
            amount: ing.quantity,  // 后端字段是 quantity，前端期望 amount
            available: true
          })) || [],
          steps: recipe.steps?.map(step => step.description) || [],
          favoritedAt: fav.createTime
        }
      })
      console.log('加载了', favorites.value.length, '条收藏记录')
    }

    // 加载历史记录
    const historyResponse = await getHistoryAPI()
    console.log('历史记录响应:', historyResponse)
    if (historyResponse.data && Array.isArray(historyResponse.data)) {
      history.value = historyResponse.data.map(h => {
        const recipe = h.recipe
        const transformedRecipe = {
          id: recipe.id,
          name: recipe.name,
          cuisine: recipe.cuisineType,
          time: recipe.cookingTime,
          difficulty: recipe.difficultyLevel,
          ingredients: recipe.ingredients?.map(ing => ({
            ingredientId: ing.ingredientId,  // 保留食材ID
            name: ing.name,
            amount: ing.quantity,  // 后端字段是 quantity，前端期望 amount
            available: true
          })) || [],
          steps: recipe.steps?.map(step => step.description) || []
        }
        return {
          id: h.id,
          recipes: [transformedRecipe],
          createdAt: h.createTime,
          ingredients: [],
          filters: {}
        }
      })
      console.log('加载了', history.value.length, '条历史记录')
    }
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败: ' + (error.message || '未知错误'))
  }
}

const removeFavorite = async (id) => {
  try {
    await ElMessageBox.confirm('确定要取消收藏吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await removeFavoriteAPI(id)
    favorites.value = favorites.value.filter(fav => fav.id !== id)
    ElMessage.success('已取消收藏')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '操作失败')
    }
  }
}

const removeHistory = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这条记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await deleteHistoryAPI(id)
    history.value = history.value.filter(h => h.id !== id)
    ElMessage.success('已删除记录')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '操作失败')
    }
  }
}

const viewRecipe = (recipe) => {
  currentRecipe.value = recipe
  detailVisible.value = true
}

const regenerate = (record) => {
  // 恢复选择的食材和筛选条件
  localStorage.setItem('restore-ingredients', JSON.stringify(record.ingredients))
  localStorage.setItem('restore-filters', JSON.stringify(record.filters))

  ElMessage.success('已恢复选择，请前往首页重新生成')

  // 跳转到首页
  setTimeout(() => {
    window.dispatchEvent(new CustomEvent('navigate', { detail: 'home' }))
  }, 500)
}

const hasFilters = (filters) => {
  return filters.cuisine || filters.tastes?.length > 0 || filters.time || filters.difficulty
}

const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const getCuisineLabel = (cuisine) => {
  const map = {
    // 前端格式
    chinese: '中餐',
    western: '西餐',
    japanese: '日韩料理',
    southeast: '东南亚菜',
    // 后端枚举格式
    CHINESE: '中餐',
    WESTERN: '西餐',
    JAPANESE_KOREAN: '日韩料理',
    SOUTHEAST_ASIAN: '东南亚菜'
  }
  return map[cuisine] || cuisine
}

const getTasteLabel = (taste) => {
  const map = {
    spicy: '辣',
    sweet: '甜',
    salty: '咸',
    sour: '酸',
    light: '清淡'
  }
  return map[taste] || taste
}

const getDifficultyLabel = (difficulty) => {
  const map = {
    // 前端格式
    easy: '新手',
    medium: '家常',
    hard: '大厨',
    // 后端枚举格式
    BEGINNER: '新手',
    HOME_COOKING: '家常',
    CHEF: '大厨'
  }
  return map[difficulty] || difficulty
}

const getDifficultyType = (difficulty) => {
  const map = {
    // 前端格式
    easy: 'success',
    medium: 'warning',
    hard: 'danger',
    // 后端枚举格式
    BEGINNER: 'success',
    HOME_COOKING: 'warning',
    CHEF: 'danger'
  }
  return map[difficulty] || 'info'
}
</script>

<style scoped>
.favorites-page {
  padding: 30px 20px;
  max-width: 1400px;
  margin: 0 auto;
  min-height: 100vh;
  background:
    radial-gradient(circle at 20% 50%, rgba(255, 179, 120, 0.12) 0%, transparent 50%),
    radial-gradient(circle at 80% 80%, rgba(255, 140, 158, 0.12) 0%, transparent 50%),
    radial-gradient(circle at 40% 20%, rgba(255, 200, 124, 0.08) 0%, transparent 50%),
    linear-gradient(135deg, #fff8f0 0%, #ffe8f0 50%, #fff5e8 100%);
  position: relative;
  overflow-x: hidden;
}

/* 标题容器 */
.title-container {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  margin-bottom: 40px;
  animation: slideDown 0.6s ease-out;
}

.title-cat-left,
.title-cat-right {
  width: 60px;
  height: 60px;
  object-fit: contain;
  animation: bounce 2s ease-in-out infinite;
}

.title-cat-right {
  transform: scaleX(-1);
  animation-delay: 0.3s;
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.page-title {
  font-size: 42px;
  font-weight: 800;
  text-align: center;
  background: linear-gradient(135deg, #ff8c69 0%, #ff6b9d 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: -1px;
  margin: 0;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 标签页 */
.tabs {
  margin-bottom: 24px;
}

.tabs :deep(.el-tabs__nav-wrap::after) {
  display: none;
}

.tabs :deep(.el-tabs__item) {
  font-size: 16px;
  font-weight: 600;
  padding: 0 24px;
  transition: all 0.3s ease;
}

.tabs :deep(.el-tabs__item:hover) {
  color: #ff8c69;
}

.tabs :deep(.el-tabs__item.is-active) {
  color: #ff8c69;
}

.tabs :deep(.el-tabs__active-bar) {
  background: linear-gradient(90deg, #ff8c69 0%, #ff6b9d 100%);
  height: 3px;
}

/* 空状态 */
.empty-state {
  padding: 80px 0;
  text-align: center;
  animation: fadeIn 0.6s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.empty-cat-icon {
  width: 120px;
  height: 120px;
  object-fit: contain;
  margin-bottom: 20px;
  animation: wiggle 2s ease-in-out infinite;
}

@keyframes wiggle {
  0%, 100% {
    transform: rotate(0deg);
  }
  25% {
    transform: rotate(-5deg);
  }
  75% {
    transform: rotate(5deg);
  }
}

.empty-text {
  font-size: 18px;
  color: #909399;
}

/* 菜谱网格 */
.recipes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 24px;
  animation: fadeIn 0.6s ease-out;
}

/* 菜谱卡片 */
.recipe-card {
  border-radius: 16px;
  border: 2px solid rgba(255, 140, 158, 0.15);
  overflow: hidden;
  transition: all 0.3s ease;
  background: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.recipe-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 28px rgba(255, 140, 158, 0.25);
  border-color: rgba(255, 140, 158, 0.3);
}

.recipe-card :deep(.el-card__header) {
  background: linear-gradient(135deg, rgba(255, 179, 120, 0.12) 0%, rgba(255, 140, 158, 0.12) 100%);
  border-bottom: 2px solid rgba(255, 140, 158, 0.2);
  padding: 18px 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.recipe-name {
  font-size: 20px;
  font-weight: 700;
  color: #303133;
  flex: 1;
}

.recipe-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 4px;
}

.recipe-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.recipe-tags :deep(.el-tag) {
  border-radius: 12px;
  padding: 6px 14px;
  font-weight: 500;
}

.favorite-info {
  color: #909399;
  font-size: 13px;
  padding: 8px 12px;
  background: rgba(255, 179, 120, 0.08);
  border-radius: 8px;
}

.favorite-date {
  font-weight: 500;
}

.recipe-actions .el-button {
  width: 100%;
  border-radius: 12px;
  font-weight: 500;
  background: linear-gradient(135deg, #ff8c69 0%, #ff6b9d 100%);
  border: none;
  transition: all 0.3s ease;
}

.recipe-actions .el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(255, 140, 158, 0.4);
}

/* 历史记录样式 */
.history-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
  animation: fadeIn 0.6s ease-out;
}

.history-card {
  border-radius: 16px;
  border: 2px solid rgba(255, 140, 158, 0.15);
  overflow: hidden;
  transition: all 0.3s ease;
  background: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.history-card:hover {
  transform: translateX(6px);
  box-shadow: 0 8px 20px rgba(255, 140, 158, 0.25);
  border-color: rgba(255, 140, 158, 0.3);
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 2px solid rgba(255, 140, 158, 0.15);
}

.history-date {
  font-size: 16px;
  font-weight: 700;
  color: #606266;
  background: linear-gradient(135deg, #ff8c69 0%, #ff6b9d 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.history-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.history-ingredients,
.history-filters,
.history-recipes {
  font-size: 14px;
  color: #606266;
  padding: 12px;
  background: rgba(255, 179, 120, 0.05);
  border-radius: 8px;
}

.history-ingredients strong,
.history-filters strong,
.history-recipes strong {
  color: #303133;
  font-weight: 600;
  display: block;
  margin-bottom: 8px;
}

.history-ingredients :deep(.el-tag),
.history-filters :deep(.el-tag) {
  border-radius: 10px;
  margin: 2px;
}

.recipe-names {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 8px;
}

.recipe-link {
  color: #ff8c69;
  cursor: pointer;
  font-weight: 500;
  padding: 4px 12px;
  background: rgba(255, 140, 158, 0.1);
  border-radius: 8px;
  transition: all 0.3s ease;
}

.recipe-link:hover {
  background: rgba(255, 140, 158, 0.2);
  transform: translateY(-2px);
}

.history-content .el-button {
  border-radius: 12px;
  font-weight: 500;
  background: linear-gradient(135deg, #ff8c69 0%, #ff6b9d 100%);
  border: none;
  transition: all 0.3s ease;
}

.history-content .el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(255, 140, 158, 0.4);
}

/* 详情弹窗样式 */
.recipe-detail {
  max-height: 70vh;
  overflow-y: auto;
  padding: 4px;
}

.recipe-detail::-webkit-scrollbar {
  width: 6px;
}

.recipe-detail::-webkit-scrollbar-thumb {
  background: rgba(255, 140, 158, 0.3);
  border-radius: 3px;
}

.recipe-detail::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 140, 158, 0.5);
}

.detail-tags {
  display: flex;
  gap: 10px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.detail-tags :deep(.el-tag) {
  border-radius: 12px;
  padding: 8px 16px;
  font-weight: 500;
}

.detail-section {
  margin-bottom: 32px;
}

.detail-section h3 {
  font-size: 20px;
  margin-bottom: 16px;
  color: #303133;
  font-weight: 700;
}

.detail-section :deep(.el-table) {
  border-radius: 12px;
  overflow: hidden;
}

.detail-section :deep(.el-steps) {
  padding: 12px;
  background: rgba(255, 179, 120, 0.05);
  border-radius: 12px;
}

/* 背景装饰猫爪 */
.paw-decoration {
  position: fixed;
  width: 80px;
  height: 80px;
  background-image: url('@/assets/images/猫爪.png');
  background-size: contain;
  background-repeat: no-repeat;
  opacity: 0.08;
  pointer-events: none;
  z-index: 0;
}

.paw-1 {
  top: 15%;
  left: 5%;
  transform: rotate(-15deg);
  animation: float 6s ease-in-out infinite;
}

.paw-2 {
  top: 60%;
  right: 8%;
  transform: rotate(25deg);
  animation: float 7s ease-in-out infinite 1s;
}

.paw-3 {
  bottom: 20%;
  left: 10%;
  transform: rotate(45deg);
  animation: float 8s ease-in-out infinite 2s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(var(--rotation, 0deg));
  }
  50% {
    transform: translateY(-20px) rotate(var(--rotation, 0deg));
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .favorites-page {
    padding: 20px 15px;
  }

  .page-title {
    font-size: 32px;
  }

  .title-cat-left,
  .title-cat-right {
    width: 45px;
    height: 45px;
  }

  .recipes-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .history-card:hover {
    transform: translateX(0);
  }
}
</style>