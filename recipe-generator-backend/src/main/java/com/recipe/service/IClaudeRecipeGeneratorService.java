package com.recipe.service;

import com.recipe.dto.request.GenerateRecipeRequest;
import com.recipe.entity.Recipe;

/**
 * Claude AI食谱生成服务接口
 */
public interface IClaudeRecipeGeneratorService {

    /**
     * 使用Claude API生成食谱
     * @param request 生成食谱请求
     * @return 生成的食谱实体
     */
    Recipe generateRecipeWithClaude(GenerateRecipeRequest request);
}
