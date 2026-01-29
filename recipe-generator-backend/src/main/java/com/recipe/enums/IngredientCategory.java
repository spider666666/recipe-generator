package com.recipe.enums;

public enum IngredientCategory {
    VEGETABLE("蔬菜类"),
    MEAT("肉类"),
    SEAFOOD("海鲜类"),
    STAPLE("主食类"),
    SEASONING("调味料"),
    OTHER("其他");

    private final String displayName;

    IngredientCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
