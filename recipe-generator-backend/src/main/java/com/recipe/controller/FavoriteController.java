package com.recipe.controller;

import com.recipe.dto.response.ApiResponse;
import com.recipe.entity.Favorite;
import com.recipe.entity.User;
import com.recipe.service.IFavoriteService;
import com.recipe.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收藏管理控制器
 */
@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
@Tag(name = "收藏管理", description = "食谱收藏相关接口")
@SecurityRequirement(name = "Bearer Authentication")
public class FavoriteController {

    private final IFavoriteService favoriteService;
    private final IUserService userService;

    @PostMapping("/{recipeId}")
    @Operation(summary = "添加收藏")
    public ApiResponse<Favorite> addFavorite(@PathVariable Long recipeId) {
        Long userId = getCurrentUserId();
        Favorite favorite = favoriteService.addFavorite(userId, recipeId);
        return ApiResponse.success("收藏成功", favorite);
    }

    @DeleteMapping("/{recipeId}")
    @Operation(summary = "取消收藏")
    public ApiResponse<Void> removeFavorite(@PathVariable Long recipeId) {
        Long userId = getCurrentUserId();
        boolean success = favoriteService.removeFavorite(userId, recipeId);
        if (success) {
            return ApiResponse.success("取消收藏成功", null);
        } else {
            return ApiResponse.error("取消收藏失败");
        }
    }

    @GetMapping
    @Operation(summary = "获取我的收藏列表")
    public ApiResponse<List<Favorite>> getMyFavorites() {
        Long userId = getCurrentUserId();
        List<Favorite> favorites = favoriteService.getUserFavorites(userId);
        return ApiResponse.success("获取成功", favorites);
    }

    @GetMapping("/check/{recipeId}")
    @Operation(summary = "检查是否已收藏")
    public ApiResponse<Boolean> checkFavorite(@PathVariable Long recipeId) {
        Long userId = getCurrentUserId();
        boolean isFavorited = favoriteService.isFavorited(userId, recipeId);
        return ApiResponse.success("查询成功", isFavorited);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        return user.getId();
    }
}
