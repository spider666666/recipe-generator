package com.recipe.enums;

public enum DifficultyLevel {
    BEGINNER("新手"),
    HOME_COOKING("家常"),
    CHEF("大厨");

    private final String displayName;

    DifficultyLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
