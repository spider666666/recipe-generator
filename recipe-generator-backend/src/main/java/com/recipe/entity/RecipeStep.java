package com.recipe.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("recipe_step")
public class RecipeStep {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long recipeId;

    private Integer stepNumber;

    private String description;

    private String imageUrl;

    private Integer duration;

    @TableField(exist = false)
    private Recipe recipe;
}
