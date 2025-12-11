package com.example.nutriflow.service;

import com.example.nutriflow.model.RecipeIngredient;
import com.example.nutriflow.model.SubstitutionRule;
import com.example.nutriflow.model.User;
import com.example.nutriflow.model.dto.SubstitutionCheckResponse;
import com.example.nutriflow.model.dto.SubstitutionSuggestionDto;
import com.example.nutriflow.service.repository.RecipeIngredientRepository;
import com.example.nutriflow.service.repository.RecipeRepository;
import com.example.nutriflow.service.repository.SubstitutionRuleRepository;
import com.example.nutriflow.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit test for SubstitutionService.
 * Covers core allergen detection, substitution rule lookup,
 * edge cases, error handling, and boundary conditions.
 */
class SubstitutionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeIngredientRepository recipeIngredientRepository;

    @Mock
    private SubstitutionRuleRepository substitutionRuleRepository;

    @InjectMocks
    private SubstitutionService substitutionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case: recipe has allergen ingredient for the user.
     * Should detect allergen and provide substitution suggestion.
     */
    @Test
    void testCheckRecipeForUser_withAllergen() {
        User user = new User();
        user.setAllergies(new String[]{"lactose"});

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(recipeRepository.existsById(3)).thenReturn(true);

        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.setIngredient("milk");
        ingredient.setAllergenTags(new String[]{"lactose"});
        ingredient.setRecipeId(3);

        when(recipeIngredientRepository.findByRecipeId(3))
                .thenReturn(List.of(ingredient));

        SubstitutionRule rule = new SubstitutionRule();
        rule.setIngredient("milk");
        rule.setAvoid("lactose");
        rule.setSubstitute("almond milk");
        rule.setNote("Use 1:1");

        when(substitutionRuleRepository
                .findByIngredientIgnoreCaseAndAvoidIgnoreCase("milk", "lactose"))
                .thenReturn(List.of(rule));

        SubstitutionCheckResponse response =
                substitutionService.checkRecipeForUser(3, 1);

        assertTrue(response.isHasAllergens());
        assertEquals(1, response.getOffenders().size());
        assertEquals("milk", response.getOffenders().get(0).getIngredient());
        assertEquals("lactose", response.getOffenders().get(0).getAllergen());
        assertEquals("almond milk", response.getSuggestions().get(0).getAlt());
    }

    /**
     * Test case: user has no allergies or ingredients are safe.
     * Should return no offenders.
     */
    @Test
    void testCheckRecipeForUser_noAllergens() {
        User user = new User();
        user.setAllergies(new String[]{});

        when(userRepository.findById(2)).thenReturn(Optional.of(user));
        when(recipeRepository.existsById(5)).thenReturn(true);
        when(recipeIngredientRepository.findByRecipeId(5))
                .thenReturn(Collections.emptyList());

        SubstitutionCheckResponse response =
                substitutionService.checkRecipeForUser(5, 2);

        assertFalse(response.isHasAllergens());
        assertTrue(response.getOffenders().isEmpty());
        assertTrue(response.getSuggestions().isEmpty());
    }

    /**
     * Test case: ingredient substitution lookup (GET endpoint behavior).
     */
    @Test
    void testFindSubstitutions_basicLookup() {
        SubstitutionRule rule = new SubstitutionRule();
        rule.setIngredient("bread");
        rule.setSubstitute("gluten-free bread");
        rule.setNote("Same slices.");

        when(substitutionRuleRepository.findByIngredientIgnoreCase("bread"))
                .thenReturn(List.of(rule));

        var result = substitutionService.findSubstitutions("bread", Optional.empty());

        assertEquals(1, result.size());
        assertEquals("gluten-free bread", result.get(0).getAlt());
    }

    /**
     * Test case: user not found should throw NoSuchElementException.
     */
    @Test
    void testCheckRecipeForUser_userNotFound() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> substitutionService.checkRecipeForUser(1, 999)
        );

        assertTrue(exception.getMessage().contains("User not found: 999"));
        verify(recipeRepository, never()).existsById(anyInt());
    }

    /**
     * Test case: recipe not found should throw NoSuchElementException.
     */
    @Test
    void testCheckRecipeForUser_recipeNotFound() {
        User user = new User();
        user.setAllergies(new String[]{"lactose"});

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(recipeRepository.existsById(999)).thenReturn(false);

        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> substitutionService.checkRecipeForUser(999, 1)
        );

        assertTrue(exception.getMessage().contains("Recipe not found: 999"));
        verify(recipeIngredientRepository, never()).findByRecipeId(anyInt());
    }

    /**
     * Test case: user has null allergies array.
     * Should handle gracefully and return no allergens.
     */
    @Test
    void testCheckRecipeForUser_nullAllergies() {
        User user = new User();
        user.setAllergies(null);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(recipeRepository.existsById(1)).thenReturn(true);

        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.setIngredient("milk");
        ingredient.setAllergenTags(new String[]{"lactose"});
        ingredient.setRecipeId(1);

        when(recipeIngredientRepository.findByRecipeId(1))
                .thenReturn(List.of(ingredient));

        SubstitutionCheckResponse response =
                substitutionService.checkRecipeForUser(1, 1);

        assertFalse(response.isHasAllergens());
        assertTrue(response.getOffenders().isEmpty());
        assertTrue(response.getSuggestions().isEmpty());
    }

    /**
     * Test case: ingredient has null allergen tags.
     * Should handle gracefully and not trigger allergen detection.
     */
    @Test
    void testCheckRecipeForUser_nullAllergenTags() {
        User user = new User();
        user.setAllergies(new String[]{"lactose"});

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(recipeRepository.existsById(1)).thenReturn(true);

        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.setIngredient("salt");
        ingredient.setAllergenTags(null);
        ingredient.setRecipeId(1);

        when(recipeIngredientRepository.findByRecipeId(1))
                .thenReturn(List.of(ingredient));

        SubstitutionCheckResponse response =
                substitutionService.checkRecipeForUser(1, 1);

        assertFalse(response.isHasAllergens());
        assertTrue(response.getOffenders().isEmpty());
    }

    /**
     * Test case: ingredient has empty allergen tags array.
     */
    @Test
    void testCheckRecipeForUser_emptyAllergenTags() {
        User user = new User();
        user.setAllergies(new String[]{"lactose"});

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(recipeRepository.existsById(1)).thenReturn(true);

        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.setIngredient("water");
        ingredient.setAllergenTags(new String[]{});
        ingredient.setRecipeId(1);

        when(recipeIngredientRepository.findByRecipeId(1))
                .thenReturn(List.of(ingredient));

        SubstitutionCheckResponse response =
                substitutionService.checkRecipeForUser(1, 1);

        assertFalse(response.isHasAllergens());
        assertTrue(response.getOffenders().isEmpty());
    }

    /**
     * Test case: case-insensitive allergen matching.
     * Should match allergens regardless of case.
     */
    @Test
    void testCheckRecipeForUser_caseInsensitiveMatching() {
        User user = new User();
        user.setAllergies(new String[]{"LACTOSE"}); // uppercase

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(recipeRepository.existsById(1)).thenReturn(true);

        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.setIngredient("milk");
        ingredient.setAllergenTags(new String[]{"lactose"}); // lowercase
        ingredient.setRecipeId(1);

        when(recipeIngredientRepository.findByRecipeId(1))
                .thenReturn(List.of(ingredient));

        SubstitutionRule rule = new SubstitutionRule();
        rule.setIngredient("milk");
        rule.setAvoid("lactose");
        rule.setSubstitute("almond milk");
        rule.setNote("Use 1:1");

        when(substitutionRuleRepository
                .findByIngredientIgnoreCaseAndAvoidIgnoreCase("milk", "lactose"))
                .thenReturn(List.of(rule));

        SubstitutionCheckResponse response =
                substitutionService.checkRecipeForUser(1, 1);

        assertTrue(response.isHasAllergens());
        assertEquals(1, response.getOffenders().size());
    }

    /**
     * Test case: multiple allergens for one user.
     */
    @Test
    void testCheckRecipeForUser_multipleAllergens() {
        User user = new User();
        user.setAllergies(new String[]{"lactose", "gluten", "nuts"});

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(recipeRepository.existsById(1)).thenReturn(true);

        RecipeIngredient ingredient1 = new RecipeIngredient();
        ingredient1.setIngredient("milk");
        ingredient1.setAllergenTags(new String[]{"lactose"});
        ingredient1.setRecipeId(1);

        RecipeIngredient ingredient2 = new RecipeIngredient();
        ingredient2.setIngredient("wheat flour");
        ingredient2.setAllergenTags(new String[]{"gluten"});
        ingredient2.setRecipeId(1);

        when(recipeIngredientRepository.findByRecipeId(1))
                .thenReturn(List.of(ingredient1, ingredient2));

        SubstitutionRule rule1 = new SubstitutionRule();
        rule1.setIngredient("milk");
        rule1.setAvoid("lactose");
        rule1.setSubstitute("almond milk");
        rule1.setNote("Use 1:1");

        SubstitutionRule rule2 = new SubstitutionRule();
        rule2.setIngredient("wheat flour");
        rule2.setAvoid("gluten");
        rule2.setSubstitute("rice flour");
        rule2.setNote("Use 1:1");

        when(substitutionRuleRepository
                .findByIngredientIgnoreCaseAndAvoidIgnoreCase("milk", "lactose"))
                .thenReturn(List.of(rule1));

        when(substitutionRuleRepository
                .findByIngredientIgnoreCaseAndAvoidIgnoreCase("wheat flour", "gluten"))
                .thenReturn(List.of(rule2));

        SubstitutionCheckResponse response =
                substitutionService.checkRecipeForUser(1, 1);

        assertTrue(response.isHasAllergens());
        assertEquals(2, response.getOffenders().size());
        assertEquals(2, response.getSuggestions().size());
    }

    /**
     * Test case: multiple allergen tags on single ingredient.
     * Should match first allergen found.
     */
    @Test
    void testCheckRecipeForUser_multipleAllergenTags() {
        User user = new User();
        user.setAllergies(new String[]{"nuts"});

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(recipeRepository.existsById(1)).thenReturn(true);

        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.setIngredient("almond milk");
        ingredient.setAllergenTags(new String[]{"lactose", "nuts", "soy"});
        ingredient.setRecipeId(1);

        when(recipeIngredientRepository.findByRecipeId(1))
                .thenReturn(List.of(ingredient));

        SubstitutionRule rule = new SubstitutionRule();
        rule.setIngredient("almond milk");
        rule.setAvoid("nuts");
        rule.setSubstitute("oat milk");
        rule.setNote("Use 1:1");

        when(substitutionRuleRepository
                .findByIngredientIgnoreCaseAndAvoidIgnoreCase("almond milk", "nuts"))
                .thenReturn(List.of(rule));

        SubstitutionCheckResponse response =
                substitutionService.checkRecipeForUser(1, 1);

        assertTrue(response.isHasAllergens());
        assertEquals(1, response.getOffenders().size());
        assertEquals("nuts", response.getOffenders().get(0).getAllergen());
    }

    /**
     * Test case: no specific substitution rule found,
     * should fallback to general ingredient-based rules.
     */
    @Test
    void testCheckRecipeForUser_fallbackToGeneralRule() {
        User user = new User();
        user.setAllergies(new String[]{"lactose"});

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(recipeRepository.existsById(1)).thenReturn(true);

        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.setIngredient("milk");
        ingredient.setAllergenTags(new String[]{"lactose"});
        ingredient.setRecipeId(1);

        when(recipeIngredientRepository.findByRecipeId(1))
                .thenReturn(List.of(ingredient));

        // No specific rule found with avoid parameter
        when(substitutionRuleRepository
                .findByIngredientIgnoreCaseAndAvoidIgnoreCase("milk", "lactose"))
                .thenReturn(Collections.emptyList());

        // Fallback to general rule
        SubstitutionRule generalRule = new SubstitutionRule();
        generalRule.setIngredient("milk");
        generalRule.setAvoid(null);
        generalRule.setSubstitute("soy milk");
        generalRule.setNote("General substitute");

        when(substitutionRuleRepository
                .findByIngredientIgnoreCase("milk"))
                .thenReturn(List.of(generalRule));

        SubstitutionCheckResponse response =
                substitutionService.checkRecipeForUser(1, 1);

        assertTrue(response.isHasAllergens());
        assertEquals(1, response.getOffenders().size());
        assertEquals(1, response.getSuggestions().size());
        assertEquals("soy milk", response.getSuggestions().get(0).getAlt());
    }

    /**
     * Test case: no substitution rules found at all.
     * Should still report allergens but no suggestions.
     */
    @Test
    void testCheckRecipeForUser_noSubstitutionRules() {
        User user = new User();
        user.setAllergies(new String[]{"lactose"});

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(recipeRepository.existsById(1)).thenReturn(true);

        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.setIngredient("milk");
        ingredient.setAllergenTags(new String[]{"lactose"});
        ingredient.setRecipeId(1);

        when(recipeIngredientRepository.findByRecipeId(1))
                .thenReturn(List.of(ingredient));

        when(substitutionRuleRepository
                .findByIngredientIgnoreCaseAndAvoidIgnoreCase("milk", "lactose"))
                .thenReturn(Collections.emptyList());

        when(substitutionRuleRepository
                .findByIngredientIgnoreCase("milk"))
                .thenReturn(Collections.emptyList());

        SubstitutionCheckResponse response =
                substitutionService.checkRecipeForUser(1, 1);

        assertTrue(response.isHasAllergens());
        assertEquals(1, response.getOffenders().size());
        assertTrue(response.getSuggestions().isEmpty());
    }

    /**
     * Test case: multiple substitution rules for same ingredient.
     */
    @Test
    void testCheckRecipeForUser_multipleSubstitutionRules() {
        User user = new User();
        user.setAllergies(new String[]{"lactose"});

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(recipeRepository.existsById(1)).thenReturn(true);

        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.setIngredient("milk");
        ingredient.setAllergenTags(new String[]{"lactose"});
        ingredient.setRecipeId(1);

        when(recipeIngredientRepository.findByRecipeId(1))
                .thenReturn(List.of(ingredient));

        SubstitutionRule rule1 = new SubstitutionRule();
        rule1.setIngredient("milk");
        rule1.setAvoid("lactose");
        rule1.setSubstitute("almond milk");
        rule1.setNote("Option 1");

        SubstitutionRule rule2 = new SubstitutionRule();
        rule2.setIngredient("milk");
        rule2.setAvoid("lactose");
        rule2.setSubstitute("soy milk");
        rule2.setNote("Option 2");

        when(substitutionRuleRepository
                .findByIngredientIgnoreCaseAndAvoidIgnoreCase("milk", "lactose"))
                .thenReturn(List.of(rule1, rule2));

        SubstitutionCheckResponse response =
                substitutionService.checkRecipeForUser(1, 1);

        assertTrue(response.isHasAllergens());
        assertEquals(1, response.getOffenders().size());
        assertEquals(2, response.getSuggestions().size());
    }

    /**
     * Test case: ingredient substitution lookup with avoid parameter.
     */
    @Test
    void testFindSubstitutions_withAvoidParameter() {
        SubstitutionRule rule = new SubstitutionRule();
        rule.setIngredient("flour");
        rule.setAvoid("gluten");
        rule.setSubstitute("rice flour");
        rule.setNote("Use 1:1 ratio");

        when(substitutionRuleRepository
                .findByIngredientIgnoreCaseAndAvoidIgnoreCase(anyString(), anyString()))
                .thenReturn(List.of(rule));

        List<SubstitutionSuggestionDto> result =
                substitutionService.findSubstitutions("flour", Optional.of("gluten"));

        assertEquals(1, result.size());
        assertEquals("flour", result.get(0).getIngredient());
        assertEquals("rice flour", result.get(0).getAlt());
        assertEquals("Use 1:1 ratio", result.get(0).getNote());
        verify(substitutionRuleRepository)
                .findByIngredientIgnoreCaseAndAvoidIgnoreCase("flour", "gluten");
    }

    /**
     * Test case: ingredient substitution with blank avoid parameter.
     * Should treat as empty and use general lookup.
     */
    @Test
    void testFindSubstitutions_blankAvoidParameter() {
        SubstitutionRule rule = new SubstitutionRule();
        rule.setIngredient("flour");
        rule.setSubstitute("whole wheat flour");
        rule.setNote("Healthier option");

        when(substitutionRuleRepository.findByIngredientIgnoreCase("flour"))
                .thenReturn(List.of(rule));

        List<SubstitutionSuggestionDto> result =
                substitutionService.findSubstitutions("flour", Optional.of("   "));

        assertEquals(1, result.size());
        assertEquals("whole wheat flour", result.get(0).getAlt());
        // Verify blank string triggers general lookup, not avoid-specific
        verify(substitutionRuleRepository, never())
                .findByIngredientIgnoreCaseAndAvoidIgnoreCase(anyString(), anyString());
    }

    /**
     * Test case: no substitution rules found.
     * Should return empty list.
     */
    @Test
    void testFindSubstitutions_noRulesFound() {
        when(substitutionRuleRepository.findByIngredientIgnoreCase("unknown"))
                .thenReturn(Collections.emptyList());

        List<SubstitutionSuggestionDto> result =
                substitutionService.findSubstitutions("unknown", Optional.empty());

        assertTrue(result.isEmpty());
    }

    /**
     * Test case: multiple substitution rules for same ingredient.
     */
    @Test
    void testFindSubstitutions_multipleRules() {
        SubstitutionRule rule1 = new SubstitutionRule();
        rule1.setIngredient("milk");
        rule1.setSubstitute("almond milk");
        rule1.setNote("Option 1");

        SubstitutionRule rule2 = new SubstitutionRule();
        rule2.setIngredient("milk");
        rule2.setSubstitute("soy milk");
        rule2.setNote("Option 2");

        when(substitutionRuleRepository.findByIngredientIgnoreCase("milk"))
                .thenReturn(List.of(rule1, rule2));

        List<SubstitutionSuggestionDto> result =
                substitutionService.findSubstitutions("milk", Optional.empty());

        assertEquals(2, result.size());
        assertEquals("milk", result.get(0).getIngredient());
        assertEquals("almond milk", result.get(0).getAlt());
        assertEquals("soy milk", result.get(1).getAlt());
    }

    /**
     * Test case: case-insensitive ingredient lookup.
     */
    @Test
    void testFindSubstitutions_caseInsensitive() {
        SubstitutionRule rule = new SubstitutionRule();
        rule.setIngredient("BREAD");
        rule.setSubstitute("gluten-free bread");
        rule.setNote("Same slices.");

        when(substitutionRuleRepository.findByIngredientIgnoreCase("bread"))
                .thenReturn(List.of(rule));

        List<SubstitutionSuggestionDto> result =
                substitutionService.findSubstitutions("bread", Optional.empty());

        assertEquals(1, result.size());
        assertEquals("bread", result.get(0).getIngredient());
        // Verify repository was called with case-insensitive method
        verify(substitutionRuleRepository).findByIngredientIgnoreCase("bread");
    }

    /**
     * Test case: case-insensitive avoid parameter lookup.
     */
    @Test
    void testFindSubstitutions_caseInsensitiveAvoid() {
        SubstitutionRule rule = new SubstitutionRule();
        rule.setIngredient("flour");
        rule.setAvoid("GLUTEN");
        rule.setSubstitute("rice flour");
        rule.setNote("Use 1:1");

        when(substitutionRuleRepository
                .findByIngredientIgnoreCaseAndAvoidIgnoreCase("flour", "GLUTEN"))
                .thenReturn(List.of(rule));

        List<SubstitutionSuggestionDto> result =
                substitutionService.findSubstitutions("flour", Optional.of("GLUTEN"));

        assertEquals(1, result.size());
        assertEquals("flour", result.get(0).getIngredient());
        assertEquals("rice flour", result.get(0).getAlt());
        // Verify case-insensitive lookup was used
        verify(substitutionRuleRepository)
                .findByIngredientIgnoreCaseAndAvoidIgnoreCase("flour", "GLUTEN");
    }

    /**
     * Test case: ingredient with no allergen match should not trigger detection.
     */
    @Test
    void testCheckRecipeForUser_noAllergenMatch() {
        User user = new User();
        user.setAllergies(new String[]{"lactose"});

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(recipeRepository.existsById(1)).thenReturn(true);

        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.setIngredient("chicken");
        ingredient.setAllergenTags(new String[]{"meat"}); // different allergen
        ingredient.setRecipeId(1);

        when(recipeIngredientRepository.findByRecipeId(1))
                .thenReturn(List.of(ingredient));

        SubstitutionCheckResponse response =
                substitutionService.checkRecipeForUser(1, 1);

        assertFalse(response.isHasAllergens());
        assertTrue(response.getOffenders().isEmpty());
    }
}