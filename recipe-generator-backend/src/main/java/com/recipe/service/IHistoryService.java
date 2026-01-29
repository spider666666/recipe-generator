package com.recipe.service;

import com.recipe.entity.History;

import java.util.List;

/**
 * 历史记录服务接口
 */
public interface IHistoryService {

    /**
     * 添加历史记录
     */
    History addHistory(Long userId, Long recipeId);

    /**
     * 获取用户的历史记录
     */
    List<History> getUserHistory(Long userId);

    /**
     * 删除历史记录
     */
    boolean deleteHistory(Long userId, Long historyId);

    /**
     * 清空用户的所有历史记录
     */
    boolean clearUserHistory(Long userId);
}
