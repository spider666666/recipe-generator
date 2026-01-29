package com.recipe.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.recipe.enums.CuisineType;
import com.recipe.enums.DifficultyLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("recipe")
public class Recipe {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private CuisineType cuisineType;

    private String flavorTypes;

    private Integer cookingTime;

    private DifficultyLevel difficultyLevel;

    private String description;

    private String imageUrl;

    private Integer servings;

    @TableField(exist = false)
    private List<RecipeIngredient> recipeIngredients;

    @TableField(exist = false)
    private List<RecipeStep> steps;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
