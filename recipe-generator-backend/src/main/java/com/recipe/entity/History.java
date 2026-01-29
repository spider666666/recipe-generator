package com.recipe.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("history")
public class History {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String userId;

    private Long recipeId;

    private String inputIngredients;

    private String filterConditions;

    @TableField(exist = false)
    private Recipe recipe;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
