package com.recipe.dto.response;

import com.recipe.enums.CuisineType;
import com.recipe.enums.DifficultyLevel;
import com.recipe.enums.FlavorType;
import lombok.Data;

import java.util.List;

@Data
public class RecipeResponse {
    private Long id;
    private String name;
    private CuisineType cuisineType;
    private List<FlavorType> flavorTypes;
    private Integer cookingTime;
    private DifficultyLevel difficultyLevel;
    private String description;
    private String imageUrl;
    private Integer servings;
    private List<RecipeIngredientDTO> ingredients;
    private List<RecipeStepDTO> steps;
    private List<String> missingIngredients;
    private Integer matchScore;  // 匹配度 0-100

    @Data
    public static class RecipeIngredientDTO {
        private String name;
        private String quantity;
        private Boolean hasIngredient;  // 用户是否拥有该食材
        private Boolean isRequired;
    }

    @Data
    public static class RecipeStepDTO {
        private Integer stepNumber;
        private String description;
        private String imageUrl;
        private Integer duration;
    }
}
