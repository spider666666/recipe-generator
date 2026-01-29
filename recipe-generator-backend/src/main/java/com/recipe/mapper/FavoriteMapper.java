package com.recipe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recipe.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收藏Mapper接口
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
}
