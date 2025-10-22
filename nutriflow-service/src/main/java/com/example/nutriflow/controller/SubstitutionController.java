package com.example.nutriflow.controller;

import com.example.nutriflow.model.dto.SubstitutionCheckRequest;
import com.example.nutriflow.model.dto.SubstitutionCheckResponse;
import com.example.nutriflow.model.dto.SubstitutionSuggestionDto;
import com.example.nutriflow.service.SubstitutionService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints for allergen check and ingredient substitutions.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/substitutions")
public final class SubstitutionController {

    /** Business service for substitution logic. */
    private final SubstitutionService substitutionService;

    /**
     * Check a recipe against a user's allergies and return suggestions.
     *
     * @param req request body with recipeId and userId
     * @return check result with offenders and suggestions
     */
    @PostMapping("/check")
    public ResponseEntity<SubstitutionCheckResponse> check(
            @RequestBody final SubstitutionCheckRequest req) {
        return ResponseEntity.ok(
                substitutionService.checkRecipeForUser(
                        req.getRecipeId(), req.getUserId()
                )
        );
    }

    /**
     * Query substitutions for an ingredient, optionally avoiding a tag.
     *
     * @param ingredient ingredient to replace
     * @param avoid optional tag to avoid (e.g. nuts, gluten)
     * @return substitution suggestions
     */
    @GetMapping
    public ResponseEntity<List<SubstitutionSuggestionDto>> query(
            @RequestParam final String ingredient,
            @RequestParam(required = false) final String avoid) {

        return ResponseEntity.ok(
                substitutionService.findSubstitutions(
                        ingredient, Optional.ofNullable(avoid)
                )
        );
    }
}