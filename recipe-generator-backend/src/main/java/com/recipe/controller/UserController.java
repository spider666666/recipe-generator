package com.recipe.controller;

import com.recipe.dto.response.ApiResponse;
import com.recipe.dto.response.UserInfoResponse;
import com.recipe.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "获取用户信息")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final IUserService userService;

    @GetMapping("/current")
    @Operation(summary = "获取当前登录用户信息")
    public ApiResponse<UserInfoResponse> getCurrentUser() {
        // 从SecurityContext中获取当前认证的用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ApiResponse.error(401, "未登录");
        }

        String username = authentication.getName();
        UserInfoResponse userInfo = userService.getCurrentUserInfo(username);

        return ApiResponse.success("获取成功", userInfo);
    }
}
