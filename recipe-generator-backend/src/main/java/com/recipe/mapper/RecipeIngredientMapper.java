package com.recipe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recipe.entity.RecipeIngredient;
import org.apache.ibatis.annotations.Mapper;

/**
 * 食谱食材关联Mapper接口
 */
@Mapper
public interface RecipeIngredientMapper extends BaseMapper<RecipeIngredient> {
}
