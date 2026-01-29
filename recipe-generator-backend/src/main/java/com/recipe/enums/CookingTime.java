package com.recipe.enums;

public enum CookingTime {
    QUICK(15, "15分钟"),
    MEDIUM(30, "30分钟"),
    LONG(60, "1小时");

    private final int minutes;
    private final String displayName;

    CookingTime(int minutes, String displayName) {
        this.minutes = minutes;
        this.displayName = displayName;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getDisplayName() {
        return displayName;
    }
}
