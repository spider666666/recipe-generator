package com.recipe.dto.request;

import com.recipe.enums.CuisineType;
import com.recipe.enums.DifficultyLevel;
import com.recipe.enums.FlavorType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class GenerateRecipeRequest {

    @NotEmpty(message = "食材列表不能为空")
    private List<IngredientInput> ingredients;

    @NotNull(message = "菜系不能为空")
    private CuisineType cuisineType;

    private List<FlavorType> flavorTypes;

    @NotNull(message = "烹饪时间不能为空")
    private Integer cookingTime;  // 分钟

    @NotNull(message = "难度等级不能为空")
    private DifficultyLevel difficultyLevel;

    @Data
    public static class IngredientInput {
        @NotNull(message = "食材名称不能为空")
        private String name;

        @NotNull(message = "食材数量不能为空")
        private String quantity;
    }
}
