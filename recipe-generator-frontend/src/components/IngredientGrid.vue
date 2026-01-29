<template>
  <div class="ingredient-grid">
    <el-button
      v-for="ingredient in ingredients"
      :key="ingredient.name"
      class="ingredient-btn"
      @click="$emit('select', ingredient)"
    >
      <span class="ingredient-icon">{{ ingredient.icon }}</span>
      <span class="ingredient-name">{{ ingredient.name }}</span>
    </el-button>
  </div>
</template>

<script setup>
defineProps({
  ingredients: {
    type: Array,
    required: true
  }
})

defineEmits(['select'])
</script>

<style scoped>
.ingredient-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 12px;
}

.ingredient-btn {
  height: 80px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  border: 2px solid #e4e7ed;
  transition: all 0.3s;
}

.ingredient-btn:hover {
  border-color: #409eff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
}

.ingredient-icon {
  font-size: 28px;
}

.ingredient-name {
  font-size: 14px;
  color: #606266;
}
</style>
