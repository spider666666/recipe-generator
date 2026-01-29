package com.recipe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recipe.entity.ShoppingList;
import org.apache.ibatis.annotations.Mapper;

/**
 * 购物清单Mapper接口
 */
@Mapper
public interface ShoppingListMapper extends BaseMapper<ShoppingList> {
}
