package com.recipe.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("shopping_list")
public class ShoppingList {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String userId;

    private String ingredientName;

    private String quantity;

    private String category;

    private String note;

    private Boolean isPurchased;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
