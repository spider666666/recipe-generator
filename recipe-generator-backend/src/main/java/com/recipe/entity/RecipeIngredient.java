package com.recipe.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("recipe_ingredient")
public class RecipeIngredient {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long recipeId;

    private Long ingredientId;

    private String quantity;

    private Boolean isRequired;

    @TableField(exist = false)
    private Recipe recipe;

    @TableField(exist = false)
    private Ingredient ingredient;
}
