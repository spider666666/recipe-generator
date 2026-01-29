package com.recipe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recipe.entity.Ingredient;
import org.apache.ibatis.annotations.Mapper;

/**
 * 食材Mapper接口
 */
@Mapper
public interface IngredientMapper extends BaseMapper<Ingredient> {
}
