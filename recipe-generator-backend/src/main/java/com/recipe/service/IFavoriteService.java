package com.recipe.service;

import com.recipe.entity.Favorite;

import java.util.List;

/**
 * 收藏服务接口
 */
public interface IFavoriteService {

    /**
     * 添加收藏
     */
    Favorite addFavorite(Long userId, Long recipeId);

    /**
     * 取消收藏
     */
    boolean removeFavorite(Long userId, Long recipeId);

    /**
     * 获取用户的所有收藏
     */
    List<Favorite> getUserFavorites(Long userId);

    /**
     * 检查是否已收藏
     */
    boolean isFavorited(Long userId, Long recipeId);
}
