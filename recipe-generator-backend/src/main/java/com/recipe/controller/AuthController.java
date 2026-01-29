package com.recipe.controller;

import com.recipe.dto.request.LoginRequest;
import com.recipe.dto.request.RegisterRequest;
import com.recipe.dto.response.ApiResponse;
import com.recipe.dto.response.JwtResponse;
import com.recipe.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户注册、登录、刷新token")
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public ApiResponse<JwtResponse> register(@Valid @RequestBody RegisterRequest request) {
        JwtResponse response = authService.register(request);
        return ApiResponse.success("注册成功", response);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public ApiResponse<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse response = authService.login(request);
        return ApiResponse.success("登录成功", response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新访问token")
    public ApiResponse<JwtResponse> refreshToken(@RequestParam String refreshToken) {
        JwtResponse response = authService.refreshToken(refreshToken);
        return ApiResponse.success("刷新成功", response);
    }
}
