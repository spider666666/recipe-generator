package com.recipe.enums;

public enum CuisineType {
    CHINESE("中餐"),
    WESTERN("西餐"),
    JAPANESE_KOREAN("日韩"),
    SOUTHEAST_ASIAN("东南亚");

    private final String displayName;

    CuisineType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
