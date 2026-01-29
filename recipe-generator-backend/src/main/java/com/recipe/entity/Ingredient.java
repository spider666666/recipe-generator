package com.recipe.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.recipe.enums.IngredientCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("ingredient")
public class Ingredient {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private IngredientCategory category;

    private String commonUnit;

    private Integer calories;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
