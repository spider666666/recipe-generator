package com.recipe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.recipe.entity.Ingredient;
import com.recipe.entity.ShoppingList;
import com.recipe.mapper.IngredientMapper;
import com.recipe.mapper.ShoppingListMapper;
import com.recipe.service.IShoppingListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 购物清单服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingListServiceImpl implements IShoppingListService {

    private final ShoppingListMapper shoppingListMapper;
    private final IngredientMapper ingredientMapper;

    @Override
    @Transactional
    public ShoppingList addItem(Long userId, Long ingredientId, String quantity, String note) {
        ShoppingList item = new ShoppingList();
        item.setUserId(userId);
        item.setIngredientId(ingredientId);
        item.setQuantity(quantity);
        item.setNote(note);
        item.setIsPurchased(false);
        shoppingListMapper.insert(item);

        log.info("用户{}添加购物清单项, 食材ID: {}", userId, ingredientId);
        return item;
    }

    @Override
    public List<ShoppingList> getUserShoppingList(Long userId) {
        LambdaQueryWrapper<ShoppingList> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingList::getUserId, userId)
               .orderByAsc(ShoppingList::getIsPurchased)
               .orderByDesc(ShoppingList::getCreateTime);

        List<ShoppingList> shoppingList = shoppingListMapper.selectList(wrapper);

        // 加载关联的食材信息
        for (ShoppingList item : shoppingList) {
            Ingredient ingredient = ingredientMapper.selectById(item.getIngredientId());
            item.setIngredient(ingredient);
        }

        return shoppingList;
    }

    @Override
    @Transactional
    public boolean updatePurchaseStatus(Long userId, Long itemId, Boolean isPurchased) {
        LambdaQueryWrapper<ShoppingList> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingList::getUserId, userId)
               .eq(ShoppingList::getId, itemId);

        ShoppingList item = shoppingListMapper.selectOne(wrapper);
        if (item == null) {
            return false;
        }

        item.setIsPurchased(isPurchased);
        int updated = shoppingListMapper.updateById(item);
        log.info("用户{}更新购物清单项{}状态为{}", userId, itemId, isPurchased);
        return updated > 0;
    }

    @Override
    @Transactional
    public boolean deleteItem(Long userId, Long itemId) {
        LambdaQueryWrapper<ShoppingList> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingList::getUserId, userId)
               .eq(ShoppingList::getId, itemId);

        int deleted = shoppingListMapper.delete(wrapper);
        log.info("用户{}删除购物清单项{}, 删除{}条记录", userId, itemId, deleted);
        return deleted > 0;
    }

    @Override
    @Transactional
    public boolean clearUserShoppingList(Long userId) {
        LambdaQueryWrapper<ShoppingList> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingList::getUserId, userId);

        int deleted = shoppingListMapper.delete(wrapper);
        log.info("用户{}清空购物清单, 删除{}条记录", userId, deleted);
        return deleted > 0;
    }
}
