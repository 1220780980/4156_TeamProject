package com.example.nutriflow.service;

import com.example.nutriflow.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.HttpOptions;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;

/**
 * Service class for handling AI logic related to recipes.
 * Provides methods for fetching recipes with desired ingredients.
 */
@Service
public class AIRecipeService {
    @Autowired
    public AIRecipeService(Client client, String llmModel) {
        this.client = client;
        this.model = llmModel; //"gemini-2.5-flash";
    }

    public String getAIRecipe(String ingredients){
            String finalPrompt = "Generate a delicious recipe with the following ingredients" + ingredients;
            return modelRequest(finalPrompt);
    }

    public String getAIRecommendedRecipe(){
        String finalPrompt = "Generate a delicious recipe";
        return modelRequest(finalPrompt);
}

    public String modelRequest(String prompt){
        Schema responseSchema = Schema.builder()
                .type("OBJECT")
                .properties(Map.of(
                        "title", Schema.builder().type("STRING").build(),
                        "cook_time", Schema.builder().type("Integer").build(),
                        "cuisines", Schema.builder().type("ARRAY")
                            .items(Schema.builder().type("STRING").build())
                            .build(),
                        "tags", Schema.builder().type("ARRAY")
                            .items(Schema.builder().type("STRING").build())
                            .build(),
                        "ingredients", Schema.builder().type("ARRAY")
                            .items(Schema.builder().type("STRING").build())
                            .build(),
                        "instructions", Schema.builder().type("STRING").build()
                ))
                .required(List.of("title", "cook_time", "ingredients"))
                .build();
            GenerateContentConfig config =
                GenerateContentConfig.builder()
                    .responseMimeType("application/json")
                    .responseSchema(responseSchema)
                    .build();
            GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash", prompt, config);
            String text_response = response.text();
        return text_response;
    }


}
