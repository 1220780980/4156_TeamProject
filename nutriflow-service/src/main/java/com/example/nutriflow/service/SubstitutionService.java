package com.example.nutriflow.service;

import com.example.nutriflow.model.RecipeIngredient;
import com.example.nutriflow.model.SubstitutionRule;
import com.example.nutriflow.model.User;
import com.example.nutriflow.model.dto.OffenderDto;
import com.example.nutriflow.model.dto.SubstitutionCheckResponse;
import com.example.nutriflow.model.dto.SubstitutionSuggestionDto;
import com.example.nutriflow.service.repository.RecipeIngredientRepository;
import com.example.nutriflow.service.repository.RecipeRepository;
import com.example.nutriflow.service.repository.SubstitutionRuleRepository;
import com.example.nutriflow.service.repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Service layer that handles logic for ingredient substitutions.
 * Provides methods for checking allergens in recipes and suggesting
 * safe ingredient alternatives for users.
 */
@Service
@RequiredArgsConstructor
public class SubstitutionService {

    /** Repository for accessing user data. */
    private final UserRepository userRepository;

    /** Repository for accessing recipe data. */
    private final RecipeRepository recipeRepository;

    /** Repository for accessing recipe ingredient data. */
    private final RecipeIngredientRepository recipeIngredientRepository;

    /** Repository for accessing substitution rules. */
    private final SubstitutionRuleRepository substitutionRuleRepository;

    /**
     * Checks if a recipe contains ingredients that trigger a user's allergies
     * and provides substitution suggestions.
     *
     * <p>Throws {@link NoSuchElementException} when user or recipe is missing.
     * The controller maps that to HTTP 404.</p>
     *
     * @param recipeId the ID of the recipe to check
     * @param userId the ID of the user
     * @return a {@link SubstitutionCheckResponse} with results
     */
    public SubstitutionCheckResponse checkRecipeForUser(
            final Integer recipeId, final Integer userId) {

        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException(
                "User not found: " + userId));

        // Ensure the recipe exists; if not, signal as missing resource.
        if (!recipeRepository.existsById(recipeId)) {
            throw new NoSuchElementException(
                "Recipe not found: " + recipeId);
        }

        final List<String> allergies = Optional.ofNullable(user.getAllergies())
            .map(Arrays::asList)
            .orElseGet(Collections::emptyList);

        final List<RecipeIngredient> ingredients =
            recipeIngredientRepository.findByRecipeId(recipeId);

        final List<OffenderDto> offenders = new ArrayList<>();

        for (RecipeIngredient ri : ingredients) {
            final List<String> tags =
                Optional.ofNullable(ri.getAllergenTags())
                    .map(Arrays::asList)
                    .orElseGet(Collections::emptyList);

            final Optional<String> hit = tags.stream()
                .filter(t -> containsIgnoreCase(allergies, t))
                .findFirst();

            hit.ifPresent(allergen -> offenders.add(
                OffenderDto.builder()
                    .ingredient(ri.getIngredient())
                    .allergen(allergen)
                    .build()
            ));
        }

        final boolean hasAllergens = !offenders.isEmpty();

        List<SubstitutionSuggestionDto> suggestions =
            Collections.emptyList();

        if (hasAllergens) {
            suggestions = offenders.stream()
                .flatMap(off -> {
                    List<SubstitutionRule> rules =
                        substitutionRuleRepository
                            .findByIngredientIgnoreCaseAndAvoidIgnoreCase(
                                off.getIngredient(), off.getAllergen());
                    if (CollectionUtils.isEmpty(rules)) {
                        rules = substitutionRuleRepository
                            .findByIngredientIgnoreCase(
                                off.getIngredient());
                    }
                    return rules.stream().map(r ->
                        SubstitutionSuggestionDto.builder()
                            .ingredient(off.getIngredient())
                            .alt(r.getSubstitute())
                            .note(r.getNote())
                            .build()
                    );
                })
                .collect(Collectors.toList());
        }

        return SubstitutionCheckResponse.builder()
            .hasAllergens(hasAllergens)
            .offenders(offenders)
            .suggestions(suggestions)
            .build();
    }

    /**
     * Retrieves possible substitutions for an ingredient, optionally
     * avoiding a specific allergen or ingredient.
     *
     * @param ingredient the ingredient to substitute
     * @param avoidOpt optional allergen or ingredient to avoid
     * @return list of possible substitution suggestions
     */
    public List<SubstitutionSuggestionDto> findSubstitutions(
            final String ingredient, final Optional<String> avoidOpt) {

        final List<SubstitutionRule> rules = avoidOpt
            .filter(s -> !s.isBlank())
            .map(a -> substitutionRuleRepository
                .findByIngredientIgnoreCaseAndAvoidIgnoreCase(
                    ingredient, a))
            .orElseGet(() -> substitutionRuleRepository
                .findByIngredientIgnoreCase(ingredient));

        return rules.stream()
            .map(r -> SubstitutionSuggestionDto.builder()
                .ingredient(ingredient)
                .alt(r.getSubstitute())
                .note(r.getNote())
                .build())
            .collect(Collectors.toList());
    }

    /**
     * Checks whether a list contains a value, ignoring case.
     *
     * @param list the list to search
     * @param value the value to find
     * @return true if found, false otherwise
     */
    private boolean containsIgnoreCase(
            final List<String> list, final String value) {
        for (String s : list) {
            if (s != null && s.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}