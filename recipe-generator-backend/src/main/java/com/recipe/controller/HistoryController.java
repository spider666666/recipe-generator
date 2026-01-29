package com.recipe.controller;

import com.recipe.dto.response.ApiResponse;
import com.recipe.entity.History;
import com.recipe.entity.User;
import com.recipe.service.IHistoryService;
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
 * 历史记录管理控制器
 */
@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
@Tag(name = "历史记录管理", description = "食谱生成历史记录相关接口")
@SecurityRequirement(name = "Bearer Authentication")
public class HistoryController {

    private final IHistoryService historyService;
    private final IUserService userService;

    @PostMapping("/{recipeId}")
    @Operation(summary = "添加历史记录")
    public ApiResponse<History> addHistory(@PathVariable Long recipeId) {
        Long userId = getCurrentUserId();
        History history = historyService.addHistory(userId, recipeId);
        return ApiResponse.success("添加成功", history);
    }

    @GetMapping
    @Operation(summary = "获取我的历史记录")
    public ApiResponse<List<History>> getMyHistory() {
        Long userId = getCurrentUserId();
        List<History> historyList = historyService.getUserHistory(userId);
        return ApiResponse.success("获取成功", historyList);
    }

    @DeleteMapping("/{historyId}")
    @Operation(summary = "删除历史记录")
    public ApiResponse<Void> deleteHistory(@PathVariable Long historyId) {
        Long userId = getCurrentUserId();
        boolean success = historyService.deleteHistory(userId, historyId);
        if (success) {
            return ApiResponse.success("删除成功", null);
        } else {
            return ApiResponse.error("删除失败");
        }
    }

    @DeleteMapping
    @Operation(summary = "清空历史记录")
    public ApiResponse<Void> clearHistory() {
        Long userId = getCurrentUserId();
        boolean success = historyService.clearUserHistory(userId);
        if (success) {
            return ApiResponse.success("清空成功", null);
        } else {
            return ApiResponse.error("清空失败");
        }
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        return user.getId();
    }
}
