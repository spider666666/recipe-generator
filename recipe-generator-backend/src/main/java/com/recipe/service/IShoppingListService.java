package com.recipe.service;

import com.recipe.entity.ShoppingList;

import java.util.List;

/**
 * 购物清单服务接口
 */
public interface IShoppingListService {

    /**
     * 添加购物清单项
     */
    ShoppingList addItem(Long userId, Long ingredientId, String quantity, String note);

    /**
     * 获取用户的购物清单
     */
    List<ShoppingList> getUserShoppingList(Long userId);

    /**
     * 更新购物清单项的购买状态
     */
    boolean updatePurchaseStatus(Long userId, Long itemId, Boolean isPurchased);

    /**
     * 删除购物清单项
     */
    boolean deleteItem(Long userId, Long itemId);

    /**
     * 清空用户的购物清单
     */
    boolean clearUserShoppingList(Long userId);
}
