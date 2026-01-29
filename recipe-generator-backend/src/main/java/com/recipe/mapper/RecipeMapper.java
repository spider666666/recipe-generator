package com.recipe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recipe.entity.Recipe;
import org.apache.ibatis.annotations.Mapper;

/**
 * 食谱Mapper接口
 */
@Mapper
public interface RecipeMapper extends BaseMapper<Recipe> {
}
