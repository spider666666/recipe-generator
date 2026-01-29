package com.recipe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recipe.entity.RecipeStep;
import org.apache.ibatis.annotations.Mapper;

/**
 * 食谱步骤Mapper接口
 */
@Mapper
public interface RecipeStepMapper extends BaseMapper<RecipeStep> {
}
