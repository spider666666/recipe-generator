package com.recipe.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("favorite")
public class Favorite {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String userId;

    private Long recipeId;

    private Integer rating;

    private String comment;

    @TableField(exist = false)
    private Recipe recipe;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
