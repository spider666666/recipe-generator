package com.recipe.enums;

public enum FlavorType {
    SPICY("辣"),
    SWEET("甜"),
    SALTY("咸"),
    SOUR("酸"),
    MILD("清淡");

    private final String displayName;

    FlavorType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
