package com.recipe.service;

import com.recipe.dto.response.UserInfoResponse;

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
}
