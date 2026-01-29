package com.recipe.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.config.ClaudeConfig;
import com.recipe.dto.request.GenerateRecipeRequest;
import com.recipe.entity.Recipe;
import com.recipe.entity.RecipeIngredient;
import com.recipe.entity.RecipeStep;
import com.recipe.enums.FlavorType;
import com.recipe.mapper.IngredientMapper;
import com.recipe.mapper.RecipeMapper;
import com.recipe.mapper.RecipeIngredientMapper;
import com.recipe.mapper.RecipeStepMapper;
import com.recipe.service.IClaudeRecipeGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Claude AI食谱生成服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClaudeRecipeGeneratorServiceImpl implements IClaudeRecipeGeneratorService {

    private final ClaudeConfig claudeConfig;
    private final RecipeMapper recipeMapper;
    private final RecipeIngredientMapper recipeIngredientMapper;
    private final RecipeStepMapper recipeStepMapper;
    private final IngredientMapper ingredientMapper;
    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public Recipe generateRecipeWithClaude(GenerateRecipeRequest request) {
        try {
            //根据请求构建，无所谓
            String prompt = buildPrompt(request);
            //todo 硬编码实现
            String claudeResponse = callClaudeAPI(prompt);
            //todo 硬编码实现
            return parseClaudeResponse(claudeResponse, request);
        } catch (Exception e) {
            log.error("调用Claude API失败", e);
            throw new RuntimeException("生成食谱失败: " + e.getMessage());
        }
    }

    /**
     * 构建发送给Claude的提示词
     */
    private String buildPrompt(GenerateRecipeRequest request) {
        String ingredients = request.getIngredients().stream()
                .map(i -> i.getName() + " " + i.getQuantity())
                .collect(Collectors.joining(", "));

        String flavors = request.getFlavorTypes() != null ?
                request.getFlavorTypes().stream()
                        .map(FlavorType::getDisplayName)
                        .collect(Collectors.joining("、")) : "不限";

        return String.format("""
                请根据以下条件生成一个详细的菜谱：

                食材：%s
                菜系：%s
                口味：%s
                烹饪时间：%d分钟以内
                难度：%s

                请以JSON格式返回，包含以下字段：
                {
                  "name": "菜品名称",
                  "description": "菜品描述",
                  "servings": 2,
                  "ingredients": [
                    {"name": "食材名", "quantity": "数量", "isRequired": true}
                  ],
                  "steps": [
                    {"stepNumber": 1, "description": "步骤描述", "duration": 5}
                  ]
                }
                注意：
                1. 尽量使用用户提供的食材
                2. 如果需要额外食材，请在ingredients中标注
                3. 步骤要详细清晰
                4. 总烹饪时间不超过%d分钟
                """,
                ingredients,
                request.getCuisineType().getDisplayName(),
                flavors,
                request.getCookingTime(),
                request.getDifficultyLevel().getDisplayName(),
                request.getCookingTime()
        );
    }

    /**
     * 调用Claude API
     */
    private String callClaudeAPI(String prompt) throws IOException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", claudeConfig.getModel());
        requestBody.put("max_tokens", claudeConfig.getMaxTokens());
        requestBody.put("temperature", claudeConfig.getTemperature());

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);
        requestBody.put("messages", messages);

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        log.info("调用Claude API: {}", claudeConfig.getApiUrl());
        log.info("使用模型: {}", claudeConfig.getModel());
        log.debug("请求体: {}", jsonBody);

        // 修改请求头配置 - 移除可能导致问题的头
        //todo 这里可能存在问题
//        Request request = new Request.Builder()
//                .url(claudeConfig.getApiUrl())
//                .addHeader("Authorization", "Bearer " + claudeConfig.getApiKey())  // 标准认证方式
//                .addHeader("Content-Type", "application/json")  // 标准Content-Type（大写）
//                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
//                .build();
//
//        log.info("请求头: Authorization=Bearer {}, Content-Type=application/json",
//                claudeConfig.getApiKey().substring(0, Math.min(10, claudeConfig.getApiKey().length())) + "...");
//
//        try (Response response = httpClient.newCall(request).execute()) {
//            String responseBody = response.body() != null ? response.body().string() : "";
//
//            log.info("Claude API响应状态: {}", response.code());
//            log.info("响应消息: {}", response.message());
//            log.debug("响应体: {}", responseBody);
//
//            if (!response.isSuccessful()) {
//                log.error("Claude API调用失败 - 状态码: {}, 消息: {}, 响应体: {}",
//                        response.code(), response.message(), responseBody);
//                throw new IOException("Claude API调用失败: " + response.code() + " " + response.message() + " - " + responseBody);
//            }

//            JsonNode jsonNode = objectMapper.readTree(responseBody);

            // 提取Claude的回复内容
//            JsonNode contentArray = jsonNode.get("content");

//            if (contentArray != null && contentArray.isArray() && contentArray.size() > 0) {
//                JsonNode textNode = contentArray.get(0).get("text");
//                if (textNode != null) {
//                    String result = textNode.asText();
//                    log.info("成功获取Claude响应，长度: {}", result.length());
//                    return result;
//                }
//            }
        String result = "{\n" +
                "  \"name\": \"西红柿炒鸡蛋\",\n" +
                "  \"description\": \"经典中式家常菜，口感嫩滑、口味清淡，食材简单易获取，10分钟即可完成，适合烹饪新手\",\n" +
                "  \"servings\": 2,\n" +
                "  \"ingredients\": [\n" +
                "    {\n" +
                "      \"name\": \"西红柿\",\n" +
                "      \"quantity\": \"2个\",\n" +
                "      \"isRequired\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"鸡蛋\",\n" +
                "      \"quantity\": \"3个\",\n" +
                "      \"isRequired\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"食用盐\",\n" +
                "      \"quantity\": \"少许\",\n" +
                "      \"isRequired\": false\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"食用油\",\n" +
                "      \"quantity\": \"2勺\",\n" +
                "      \"isRequired\": false\n" +
                "    }\n" +
                "  ],\n" +
                "  \"steps\": [\n" +
                "    {\n" +
                "      \"stepNumber\": 1,\n" +
                "      \"description\": \"西红柿洗净，顶部划十字刀，用开水烫10秒剥去外皮，切成小块备用\",\n" +
                "      \"duration\": 3\n" +
                "    },\n" +
                "    {\n" +
                "      \"stepNumber\": 2,\n" +
                "      \"description\": \"鸡蛋打入碗中，加少许盐搅打至出现细腻泡沫，静置2分钟\",\n" +
                "      \"duration\": 2\n" +
                "    },\n" +
                "    {\n" +
                "      \"stepNumber\": 3,\n" +
                "      \"description\": \"锅中倒入食用油，油温烧至六成热（微微冒烟），倒入蛋液快速翻炒至定型，盛出备用\",\n" +
                "      \"duration\": 2\n" +
                "    },\n" +
                "    {\n" +
                "      \"stepNumber\": 4,\n" +
                "      \"description\": \"同一口锅留少许底油，放入西红柿块翻炒，压出西红柿汤汁，炒至软烂\",\n" +
                "      \"duration\": 3\n" +
                "    },\n" +
                "    {\n" +
                "      \"stepNumber\": 5,\n" +
                "      \"description\": \"倒入炒好的鸡蛋，与西红柿翻炒均匀，加少许盐调味，翻炒10秒即可出锅\",\n" +
                "      \"duration\": 2\n" +
                "    }\n" +
                "  ]\n" +
                "}";

//            // todo尝试其他可能的响应格式
//            if (jsonNode.has("response")) {
//                return jsonNode.get("response").asText();
//            }
//            if (jsonNode.has("text")) {
//                return jsonNode.get("text").asText();
//            }
//
//            log.error("Claude API返回格式错误: {}", responseBody);
//            throw new IOException("Claude API返回格式错误: " + responseBody);

        return result;

    }

    /**
     * 解析Claude返回的JSON并创建Recipe对象
     */
    private Recipe parseClaudeResponse(String claudeResponse, GenerateRecipeRequest request) {
        try {
            // 提取JSON部分（Claude可能返回带有额外文本的响应）
            String jsonStr = extractJSON(claudeResponse);
            JsonNode recipeJson = objectMapper.readTree(jsonStr);

            Recipe recipe = new Recipe();
            recipe.setName(recipeJson.get("name").asText());
            recipe.setDescription(recipeJson.has("description") ? recipeJson.get("description").asText() : "");
            recipe.setCuisineType(request.getCuisineType());
            recipe.setDifficultyLevel(request.getDifficultyLevel());
            recipe.setCookingTime(request.getCookingTime());
            recipe.setServings(recipeJson.has("servings") ? recipeJson.get("servings").asInt() : 2);

            // 设置口味
            if (request.getFlavorTypes() != null && !request.getFlavorTypes().isEmpty()) {
                String flavors = request.getFlavorTypes().stream()
                        .map(Enum::name)
                        .collect(Collectors.joining(","));
                recipe.setFlavorTypes(flavors);
            }

            // 保存食谱
            recipeMapper.insert(recipe);

            // 解析并保存食材
            JsonNode ingredientsNode = recipeJson.get("ingredients");
            if (ingredientsNode != null && ingredientsNode.isArray()) {
                for (JsonNode ingNode : ingredientsNode) {
                    String ingredientName = ingNode.get("name").asText();

                    // 查找或创建食材
                    com.recipe.entity.Ingredient ingredient = ingredientMapper.selectOne(
                        new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.recipe.entity.Ingredient>()
                            .eq(com.recipe.entity.Ingredient::getName, ingredientName)
                    );

                    if (ingredient == null) {
                        // 创建新食材，设置默认值
                        ingredient = new com.recipe.entity.Ingredient();
                        ingredient.setName(ingredientName);
                        ingredient.setCategory(com.recipe.enums.IngredientCategory.OTHER);  // 默认分类
                        ingredient.setCommonUnit("适量");  // 默认单位
                        ingredient.setCalories(0);  // 默认热量
                        ingredientMapper.insert(ingredient);
                    }

                    RecipeIngredient ri = new RecipeIngredient();
                    ri.setRecipeId(recipe.getId());
                    ri.setIngredientId(ingredient.getId());  // 设置食材ID
                    ri.setQuantity(ingNode.get("quantity").asText());
                    ri.setIsRequired(ingNode.has("isRequired") ? ingNode.get("isRequired").asBoolean() : true);
                    recipeIngredientMapper.insert(ri);
                }
            }

            // 解析并保存步骤
            JsonNode stepsNode = recipeJson.get("steps");
            if (stepsNode != null && stepsNode.isArray()) {
                for (JsonNode stepNode : stepsNode) {
                    RecipeStep step = new RecipeStep();
                    step.setRecipeId(recipe.getId());
                    step.setStepNumber(stepNode.get("stepNumber").asInt());
                    step.setDescription(stepNode.get("description").asText());
                    if (stepNode.has("duration")) {
                        step.setDuration(stepNode.get("duration").asInt());
                    }
                    recipeStepMapper.insert(step);
                }
            }

            return recipe;

        } catch (Exception e) {
            log.error("解析Claude响应失败", e);
            throw new RuntimeException("解析食谱数据失败: " + e.getMessage());
        }
    }

    /**
     * 从Claude响应中提取JSON部分
     */
    private String extractJSON(String response) {
        // 查找JSON的开始和结束位置
        int start = response.indexOf("{");
        int end = response.lastIndexOf("}");

        if (start != -1 && end != -1 && end > start) {
            return response.substring(start, end + 1);
        }

        return response;
    }
}
