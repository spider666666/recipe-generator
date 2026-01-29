package com.recipe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recipe.entity.History;
import org.apache.ibatis.annotations.Mapper;

/**
 * 历史记录Mapper接口
 */
@Mapper
public interface HistoryMapper extends BaseMapper<History> {
}
