package com.recipe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recipe.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
