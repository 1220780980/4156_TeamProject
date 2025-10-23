package com.example.nutriflow.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.nutriflow.model.Recipe;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.HttpOptions;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;


/**
 * Service class for handling AI logic related to recipes.
 * Provides methods for fetching recipes with desired ingredients.
 */
@Service
public class AIRecipeService {
    private final Client client;
    private final String model;
    private final ObjectMapper objectMapper;

    public AIRecipeService(
        @Value("${GOOGLE_API_KEY}") String apiKey, 
        @Value("${GOOGLE_MODEL_NAME}") String model,
        ObjectMapper objectMapper) {
        this.client = Client.builder().apiKey(apiKey).build();
        this.model = model;
        this.objectMapper = objectMapper;
    }
    public Recipe getAIRecipe(String ingredients){
            String finalPrompt = "Generate a delicious recipe with the following ingredients" + ingredients;
            return requestRecipe(finalPrompt);
    }

    public Recipe getAIRecommendedRecipe(){
        String finalPrompt = "Generate a delicious recipe";
        return requestRecipe(finalPrompt);
}


    private Recipe requestRecipe(String prompt) {
        Schema responseSchema = Schema.builder()
                .type("OBJECT")
                .properties(Map.ofEntries(
                    Map.entry("title", Schema.builder().type("STRING").build()),
                    Map.entry("cookTime", Schema.builder().type("INTEGER").build()),
                    Map.entry("cuisines", Schema.builder()
                            .type("ARRAY")
                            .items(Schema.builder().type("STRING").build())
                            .build()),
                    Map.entry("tags", Schema.builder()
                            .type("ARRAY")
                            .items(Schema.builder().type("STRING").build())
                            .build()),
                    Map.entry("ingredients", Schema.builder()
                            .type("OBJECT")
                            .properties(Map.of(
                                "required", Schema.builder().type("STRING").build(),
                                "optional", Schema.builder().type("STRING").build()
                            ))
                            .build()),
                    // Map.entry("nutrition", Schema.builder()
                    //         .type("OBJECT")
                    //         .properties(Map.of(
                    //             "summary", Schema.builder().type("STRING").build()
                    //         ))
                    //         .build()),
                    Map.entry("calories", Schema.builder().type("NUMBER").build()),
                    Map.entry("carbohydrates", Schema.builder().type("NUMBER").build()),
                    Map.entry("fat", Schema.builder().type("NUMBER").build()),
                    Map.entry("fiber", Schema.builder().type("NUMBER").build()),
                    Map.entry("protein", Schema.builder().type("NUMBER").build())
                    // Map.entry("popularityScore", Schema.builder().type("INTEGER").build())
                ))
                .required(List.of("title", "ingredients"))
                .build();

        GenerateContentConfig config = GenerateContentConfig.builder()
                .responseMimeType("application/json")
                .responseSchema(responseSchema)
                .build();

        GenerateContentResponse response =
                client.models.generateContent(model, prompt, config);

        return parseRecipe(response.text());
    }


    private Recipe parseRecipe(String json) {
        try {
            JsonNode node = objectMapper.readTree(json);
            Recipe recipe = new Recipe();

            recipe.setTitle(node.path("title").asText(null));
            if (node.hasNonNull("cookTime")) {
                recipe.setCookTime(node.get("cookTime").asInt());
            }
            recipe.setCuisines(readStringArray(node.get("cuisines")));
            recipe.setTags(readStringArray(node.get("tags")));
            recipe.setIngredients(writeJsonOrEmpty(node.get("ingredients")));
            if (node.hasNonNull("nutrition")) {
                recipe.setNutrition(writeJsonOrEmpty(node.get("nutrition")));
            }
            recipe.setCalories(readDecimal(node, "calories"));
            recipe.setCarbohydrates(readDecimal(node, "carbohydrates"));
            recipe.setFat(readDecimal(node, "fat"));
            recipe.setFiber(readDecimal(node, "fiber"));
            recipe.setProtein(readDecimal(node, "protein"));
            if (node.hasNonNull("popularityScore")) {
                recipe.setPopularityScore(node.get("popularityScore").asInt());
            }
            return recipe;
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to parse recipe response", ex);
        }
    }

    private String[] readStringArray(JsonNode node) {
        if (node == null || !node.isArray()) {
            return null;
        }
        List<String> values = new ArrayList<>();
        node.forEach(item -> values.add(item.asText()));
        return values.toArray(String[]::new);
    }

    private String writeJsonOrEmpty(JsonNode node) throws JsonProcessingException {
        return (node == null || node.isNull())
                ? "{}"
                : objectMapper.writeValueAsString(node);
    }

    private BigDecimal readDecimal(JsonNode node, String field) {
        JsonNode valueNode = node.get(field);
        return valueNode != null && valueNode.isNumber()
                ? valueNode.decimalValue()
                : null;
    }

}
