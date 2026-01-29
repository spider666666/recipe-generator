package com.recipe.controller;

import com.recipe.dto.request.GenerateRecipeRequest;
import com.recipe.dto.response.ApiResponse;
import com.recipe.dto.response.RecipeResponse;
import com.recipe.service.IClaudeRecipeGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
@Tag(name = "食谱管理", description = "食谱生成、查询")
@SecurityRequirement(name = "Bearer Authentication")
public class RecipeController {

    private final IClaudeRecipeGeneratorService recipeGeneratorService;

    @PostMapping("/generate")
    @Operation(summary = "生成食谱推荐")
    public ApiResponse<?> generateRecipe(@Valid @RequestBody GenerateRecipeRequest request) {
        try {
            var recipe = recipeGeneratorService.generateRecipeWithClaude(request);
            return ApiResponse.success("生成成功", recipe);
        } catch (Exception e) {
            return ApiResponse.error("生成失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取食谱详情")
    public ApiResponse<RecipeResponse> getRecipe(@PathVariable Long id) {
        // TODO: 实现获取食谱详情
        return ApiResponse.success(null);
    }
}
