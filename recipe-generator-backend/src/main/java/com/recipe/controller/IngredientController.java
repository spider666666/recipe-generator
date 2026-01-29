package com.recipe.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.recipe.dto.response.ApiResponse;
import com.recipe.entity.Ingredient;
import com.recipe.mapper.IngredientMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 食材管理控制器
 */
@RestController
@RequestMapping("/ingredients")
@RequiredArgsConstructor
@Tag(name = "食材管理", description = "食材查询相关接口")
@SecurityRequirement(name = "Bearer Authentication")
public class IngredientController {

    private final IngredientMapper ingredientMapper;

    @GetMapping
    @Operation(summary = "获取所有食材")
    public ApiResponse<List<Ingredient>> getAllIngredients() {
        List<Ingredient> ingredients = ingredientMapper.selectList(null);
        return ApiResponse.success("获取成功", ingredients);
    }

    @GetMapping("/search")
    @Operation(summary = "根据名称搜索食材")
    public ApiResponse<Ingredient> searchByName(@RequestParam String name) {
        LambdaQueryWrapper<Ingredient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Ingredient::getName, name);
        Ingredient ingredient = ingredientMapper.selectOne(wrapper);
        return ApiResponse.success("查询成功", ingredient);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取食材")
    public ApiResponse<Ingredient> getById(@PathVariable Long id) {
        Ingredient ingredient = ingredientMapper.selectById(id);
        return ApiResponse.success("获取成功", ingredient);
    }
}
