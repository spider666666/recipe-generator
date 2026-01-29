package com.recipe.service;

import com.recipe.dto.request.LoginRequest;
import com.recipe.dto.request.RegisterRequest;
import com.recipe.dto.response.JwtResponse;

/**
 * 认证服务接口
 */
public interface IAuthService {

    /**
     * 用户注册
     * @param request 注册请求
     * @return JWT响应
     */
    JwtResponse register(RegisterRequest request);

    /**
     * 用户登录
     * @param request 登录请求
     * @return JWT响应
     */
    JwtResponse login(LoginRequest request);

    /**
     * 刷新访问令牌
     * @param refreshToken 刷新令牌
     * @return JWT响应
     */
    JwtResponse refreshToken(String refreshToken);
}
