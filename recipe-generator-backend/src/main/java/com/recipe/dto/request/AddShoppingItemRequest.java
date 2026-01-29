package com.recipe.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 添加购物清单项请求
 */
@Data
public class AddShoppingItemRequest {

    @NotNull(message = "食材ID不能为空")
    private Long ingredientId;

    @NotNull(message = "数量不能为空")
    private String quantity;

    private String note;
}
