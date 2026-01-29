package com.recipe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.recipe.entity.Favorite;
import com.recipe.entity.Recipe;
import com.recipe.mapper.FavoriteMapper;
import com.recipe.mapper.RecipeMapper;
import com.recipe.service.IFavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收藏服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteServiceImpl implements IFavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final RecipeMapper recipeMapper;

    @Override
    @Transactional
    public Favorite addFavorite(Long userId, Long recipeId) {
        // 检查是否已收藏
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
               .eq(Favorite::getRecipeId, recipeId);

        Favorite existing = favoriteMapper.selectOne(wrapper);
        if (existing != null) {
            log.info("用户{}已收藏食谱{}", userId, recipeId);
            return existing;
        }

        // 创建新收藏
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setRecipeId(recipeId);
        favoriteMapper.insert(favorite);

        log.info("用户{}收藏食谱{}成功", userId, recipeId);
        return favorite;
    }

    @Override
    @Transactional
    public boolean removeFavorite(Long userId, Long recipeId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
               .eq(Favorite::getRecipeId, recipeId);

        int deleted = favoriteMapper.delete(wrapper);
        log.info("用户{}取消收藏食谱{}, 删除{}条记录", userId, recipeId, deleted);
        return deleted > 0;
    }

    @Override
    public List<Favorite> getUserFavorites(Long userId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
               .orderByDesc(Favorite::getCreateTime);

        List<Favorite> favorites = favoriteMapper.selectList(wrapper);

        // 加载关联的食谱信息
        for (Favorite favorite : favorites) {
            Recipe recipe = recipeMapper.selectById(favorite.getRecipeId());
            favorite.setRecipe(recipe);
        }

        return favorites;
    }

    @Override
    public boolean isFavorited(Long userId, Long recipeId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
               .eq(Favorite::getRecipeId, recipeId);

        return favoriteMapper.selectCount(wrapper) > 0;
    }
}
