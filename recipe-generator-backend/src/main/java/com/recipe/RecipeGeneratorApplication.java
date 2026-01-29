package com.recipe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@MapperScan("com.recipe.mapper")
public class RecipeGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecipeGeneratorApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("🍳 Recipe Generator Backend Started!");
        System.out.println("📖 API Docs: http://localhost:8080/api/swagger-ui.html");
        System.out.println("========================================\n");
    }
}
