package com.recipe.service;

import com.recipe.dto.response.UserInfoResponse;
import com.recipe.entity.User;

/**
 * 用户服务接口
 */
public interface IUserService {

    /**
     * 获取当前登录用户信息
     * @param username 用户名
     * @return 用户信息
     */
    UserInfoResponse getCurrentUserInfo(String username);

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户实体
     */
    User findByUsername(String username);
}
