package com.example.nutriflow.mealplan.service;

import com.example.nutriflow.mealplan.dto.CreateDailyMealPlanRequestDTO;
import com.example.nutriflow.mealplan.dto.CreateWeeklyMealPlanRequestDTO;
import com.example.nutriflow.mealplan.dto.DailyMealPlanDTO;
import com.example.nutriflow.mealplan.dto.MealDTO;
import com.example.nutriflow.mealplan.dto.WeeklyMealPlanDTO;
import com.example.nutriflow.mealplan.model.DailyMealPlan;
import com.example.nutriflow.mealplan.model.Meal;
import com.example.nutriflow.mealplan.model.WeeklyMealPlan;
import com.example.nutriflow.mealplan.repository.DailyMealPlanRepository;
import com.example.nutriflow.mealplan.repository.MealRepository;
import com.example.nutriflow.mealplan.repository.WeeklyMealPlanRepository;
import com.example.nutriflow.model.Recipe;
import com.example.nutriflow.model.User;
import com.example.nutriflow.model.UserTarget;
import com.example.nutriflow.model.enums.MealType;
import com.example.nutriflow.service.RecipeService;
import com.example.nutriflow.service.UserService;
import com.example.nutriflow.service.UserTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for handling meal plan related business logic.
 * Integrates with Nutriflow API to retrieve recipes and generate
 * meal plans consistent with user targets.
 */
@Service
public class MealPlanService {

    /** Number of days in a week. */
    private static final int DAYS_IN_WEEK = 7;

    /** End day offset for weekly plan. */
    private static final int WEEK_END_OFFSET = 6;

    /** Calorie distribution for breakfast (30%). */
    private static final BigDecimal BREAKFAST_RATIO =
            new BigDecimal("0.30");

    /** Calorie distribution for lunch (35%). */
    private static final BigDecimal LUNCH_RATIO = new BigDecimal("0.35");

    /** Calorie distribution for dinner (35%). */
    private static final BigDecimal DINNER_RATIO = new BigDecimal("0.35");

    /** Tolerance percentage for calorie matching (20%). */
    private static final BigDecimal CALORIE_TOLERANCE =
            new BigDecimal("0.20");

    /** Minimum number of recipes needed for meal generation. */
    private static final int MIN_RECIPES_FOR_MEALS = 3;

    /** Repository for daily meal plans. */
    @Autowired
    private DailyMealPlanRepository dailyMealPlanRepository;

    /** Repository for weekly meal plans. */
    @Autowired
    private WeeklyMealPlanRepository weeklyMealPlanRepository;

    /** Repository for meals. */
    @Autowired
    private MealRepository mealRepository;

    /** Service for retrieving recipes from Nutriflow API. */
    @Autowired
    private RecipeService recipeService;

    /** Service for retrieving user information. */
    @Autowired
    private UserService userService;

    /** Service for retrieving user nutritional targets. */
    @Autowired
    private UserTargetService userTargetService;

    /**
     * Create a daily meal plan for a user.
     * Calls Nutriflow API to retrieve recipes based on user targets.
     *
     * @param request the request containing meal plan information
     * @return the created daily meal plan DTO
     */
    @Transactional
    public DailyMealPlanDTO createDailyMealPlan(
            final CreateDailyMealPlanRequestDTO request) {

        // Create daily meal plan entity
        DailyMealPlan dailyPlan = new DailyMealPlan();
        dailyPlan.setUserId(request.getUserId());
        dailyPlan.setPlanDate(request.getPlanDate() != null
                ? request.getPlanDate() : LocalDate.now());
        dailyPlan.setPlanName(request.getPlanName() != null
                ? request.getPlanName() : "Daily Meal Plan");

        DailyMealPlan savedPlan =
                dailyMealPlanRepository.save(dailyPlan);

        // Retrieve user info and targets from Nutriflow API
        Optional<User> userOpt =
                userService.getUserById(request.getUserId());
        Optional<UserTarget> targetOpt =
                userTargetService.getUserTargets(request.getUserId());

        List<Meal> generatedMeals = new ArrayList<>();

        if (userOpt.isPresent() && targetOpt.isPresent()) {
            User user = userOpt.get();
            UserTarget target = targetOpt.get();

            // Generate meals based on user targets
            generatedMeals = generateMealsForDay(
                    savedPlan.getDailyPlanId(),
                    user,
                    target,
                    request.getAllergens(),
                    request.getDietaryRestrictions());
        } else {
            // Fallback: use popular recipes if no user data
            generatedMeals = generateDefaultMeals(
                    savedPlan.getDailyPlanId());
        }

        return convertToDailyMealPlanDTO(savedPlan, generatedMeals);
    }

    /**
     * Generate meals for a day based on user nutritional targets.
     * Calls Nutriflow API to retrieve suitable recipes.
     *
     * @param dailyPlanId the daily plan ID
     * @param user the user information
     * @param target the user's nutritional targets
     * @param allergens optional allergens to avoid
     * @param dietaryRestrictions optional dietary restrictions
     * @return list of generated meals
     */
    private List<Meal> generateMealsForDay(
            final Integer dailyPlanId,
            final User user,
            final UserTarget target,
            final String[] allergens,
            final String[] dietaryRestrictions) {

        List<Meal> meals = new ArrayList<>();

        // Calculate target calories for each meal type
        BigDecimal dailyCalories = target.getCalories() != null
                ? target.getCalories() : new BigDecimal("2000");

        BigDecimal breakfastCal =
                dailyCalories.multiply(BREAKFAST_RATIO);
        BigDecimal lunchCal = dailyCalories.multiply(LUNCH_RATIO);
        BigDecimal dinnerCal = dailyCalories.multiply(DINNER_RATIO);

        // Call Nutriflow API to get all available recipes
        List<Recipe> allRecipes = recipeService.getAllRecipes();

        // Filter recipes based on user allergies and dislikes
        List<Recipe> filteredRecipes =
                filterRecipesByUserPreferences(
                        allRecipes, user, allergens);

        // Generate breakfast
        Recipe breakfastRecipe = findBestRecipeForCalories(
                filteredRecipes, breakfastCal, "breakfast");
        if (breakfastRecipe != null) {
            meals.add(createMealFromRecipe(
                    dailyPlanId, breakfastRecipe, MealType.BREAKFAST));
        }

        // Generate lunch
        Recipe lunchRecipe = findBestRecipeForCalories(
                filteredRecipes, lunchCal, "lunch");
        if (lunchRecipe != null) {
            meals.add(createMealFromRecipe(
                    dailyPlanId, lunchRecipe, MealType.LUNCH));
        }

        // Generate dinner
        Recipe dinnerRecipe = findBestRecipeForCalories(
                filteredRecipes, dinnerCal, "dinner");
        if (dinnerRecipe != null) {
            meals.add(createMealFromRecipe(
                    dailyPlanId, dinnerRecipe, MealType.DINNER));
        }

        // Save all meals to database
        return mealRepository.saveAll(meals);
    }

    /**
     * Filter recipes based on user preferences and restrictions.
     *
     * @param recipes list of all recipes
     * @param user the user information
     * @param allergens additional allergens to avoid
     * @return filtered list of recipes
     */
    private List<Recipe> filterRecipesByUserPreferences(
            final List<Recipe> recipes,
            final User user,
            final String[] allergens) {

        List<String> avoidList = new ArrayList<>();

        // Add user allergies
        if (user.getAllergies() != null) {
            avoidList.addAll(Arrays.asList(user.getAllergies()));
        }

        // Add user dislikes
        if (user.getDislikes() != null) {
            avoidList.addAll(Arrays.asList(user.getDislikes()));
        }

        // Add additional allergens from request
        if (allergens != null) {
            avoidList.addAll(Arrays.asList(allergens));
        }

        // Filter recipes
        return recipes.stream()
                .filter(recipe -> !containsAnyIngredient(
                        recipe, avoidList))
                .collect(Collectors.toList());
    }

    /**
     * Check if recipe contains any of the avoided ingredients.
     *
     * @param recipe the recipe to check
     * @param avoidList list of ingredients to avoid
     * @return true if recipe contains avoided ingredients
     */
    private boolean containsAnyIngredient(
            final Recipe recipe, final List<String> avoidList) {
        if (avoidList.isEmpty()) {
            return false;
        }

        String ingredients = recipe.getIngredients();
        if (ingredients == null) {
            return false;
        }

        String lowerIngredients = ingredients.toLowerCase();
        return avoidList.stream()
                .anyMatch(avoid -> lowerIngredients
                        .contains(avoid.toLowerCase()));
    }

    /**
     * Find the best recipe matching target calories.
     *
     * @param recipes filtered list of recipes
     * @param targetCalories target calorie amount
     * @param mealTag optional meal tag for filtering
     * @return best matching recipe or null
     */
    private Recipe findBestRecipeForCalories(
            final List<Recipe> recipes,
            final BigDecimal targetCalories,
            final String mealTag) {

        BigDecimal minDiff = new BigDecimal(Double.MAX_VALUE);
        Recipe bestMatch = null;

        for (Recipe recipe : recipes) {
            if (recipe.getCalories() == null) {
                continue;
            }

            // Calculate difference from target
            BigDecimal diff = recipe.getCalories()
                    .subtract(targetCalories).abs();

            // Check if within tolerance
            BigDecimal tolerance =
                    targetCalories.multiply(CALORIE_TOLERANCE);

            if (diff.compareTo(tolerance) <= 0
                    && diff.compareTo(minDiff) < 0) {
                minDiff = diff;
                bestMatch = recipe;
            }
        }

        // If no match found, return most popular recipe
        if (bestMatch == null && !recipes.isEmpty()) {
            bestMatch = recipes.get(0);
        }

        return bestMatch;
    }

    /**
     * Create a Meal entity from a Recipe.
     *
     * @param dailyPlanId the daily plan ID
     * @param recipe the recipe from Nutriflow API
     * @param mealType the type of meal
     * @return created meal entity
     */
    private Meal createMealFromRecipe(
            final Integer dailyPlanId,
            final Recipe recipe,
            final MealType mealType) {

        Meal meal = new Meal();
        meal.setDailyPlanId(dailyPlanId);
        meal.setRecipeId(recipe.getRecipeId());
        meal.setMealType(mealType);
        meal.setMealName(recipe.getTitle());
        meal.setCalories(recipe.getCalories());
        meal.setProtein(recipe.getProtein());
        meal.setCarbs(recipe.getCarbohydrates());
        meal.setFat(recipe.getFat());
        meal.setFiber(recipe.getFiber());
        return meal;
    }

    /**
     * Generate default meals using popular recipes.
     *
     * @param dailyPlanId the daily plan ID
     * @return list of default meals
     */
    private List<Meal> generateDefaultMeals(
            final Integer dailyPlanId) {

        List<Meal> meals = new ArrayList<>();

        // Get popular recipes from Nutriflow API
        List<Recipe> popularRecipes =
                recipeService.getPopularRecipes(DAYS_IN_WEEK);

        if (popularRecipes.size() >= MIN_RECIPES_FOR_MEALS) {
            meals.add(createMealFromRecipe(
                    dailyPlanId,
                    popularRecipes.get(0),
                    MealType.BREAKFAST));
            meals.add(createMealFromRecipe(
                    dailyPlanId,
                    popularRecipes.get(1),
                    MealType.LUNCH));
            meals.add(createMealFromRecipe(
                    dailyPlanId,
                    popularRecipes.get(2),
                    MealType.DINNER));
        }

        return mealRepository.saveAll(meals);
    }

    /**
     * Get a daily meal plan by ID.
     *
     * @param dailyPlanId the daily plan ID
     * @return optional daily meal plan DTO
     */
    public Optional<DailyMealPlanDTO> getDailyMealPlan(
            final Integer dailyPlanId) {
        Optional<DailyMealPlan> planOpt =
                dailyMealPlanRepository.findById(dailyPlanId);

        if (planOpt.isEmpty()) {
            return Optional.empty();
        }

        DailyMealPlan plan = planOpt.get();
        List<Meal> meals =
                mealRepository.findByDailyPlanId(dailyPlanId);

        return Optional.of(convertToDailyMealPlanDTO(plan, meals));
    }

    /**
     * Get all daily meal plans for a user.
     *
     * @param userId the user ID
     * @return list of daily meal plan DTOs
     */
    public List<DailyMealPlanDTO> getDailyMealPlansByUserId(
            final Integer userId) {
        List<DailyMealPlan> plans =
                dailyMealPlanRepository.findByUserId(userId);

        return plans.stream()
                .map(plan -> {
                    List<Meal> meals = mealRepository
                            .findByDailyPlanId(plan.getDailyPlanId());
                    return convertToDailyMealPlanDTO(plan, meals);
                })
                .collect(Collectors.toList());
    }

    /**
     * Create a weekly meal plan for a user.
     * Generates 7 daily plans by calling Nutriflow API.
     *
     * @param request the request containing weekly meal plan info
     * @return the created weekly meal plan DTO
     */
    @Transactional
    public WeeklyMealPlanDTO createWeeklyMealPlan(
            final CreateWeeklyMealPlanRequestDTO request) {

        WeeklyMealPlan weeklyPlan = new WeeklyMealPlan();
        weeklyPlan.setUserId(request.getUserId());
        weeklyPlan.setStartDate(request.getStartDate() != null
                ? request.getStartDate() : LocalDate.now());
        weeklyPlan.setEndDate(weeklyPlan.getStartDate()
                .plusDays(WEEK_END_OFFSET));
        weeklyPlan.setPlanName(request.getPlanName() != null
                ? request.getPlanName() : "Weekly Meal Plan");

        WeeklyMealPlan savedPlan =
                weeklyMealPlanRepository.save(weeklyPlan);

        // Retrieve user info and targets
        Optional<User> userOpt =
                userService.getUserById(request.getUserId());
        Optional<UserTarget> targetOpt =
                userTargetService
                        .getUserTargets(request.getUserId());

        // Create daily plans for each day of the week
        List<DailyMealPlanDTO> dailyPlans = new ArrayList<>();
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            LocalDate date = savedPlan.getStartDate().plusDays(i);

            DailyMealPlan dailyPlan = new DailyMealPlan();
            dailyPlan.setUserId(savedPlan.getUserId());
            dailyPlan.setWeeklyPlanId(
                    savedPlan.getWeeklyPlanId());
            dailyPlan.setPlanDate(date);
            dailyPlan.setPlanName("Day " + (i + 1));

            DailyMealPlan savedDaily =
                    dailyMealPlanRepository.save(dailyPlan);

            // Generate meals for each day
            List<Meal> dailyMeals = new ArrayList<>();
            if (userOpt.isPresent() && targetOpt.isPresent()) {
                dailyMeals = generateMealsForDay(
                        savedDaily.getDailyPlanId(),
                        userOpt.get(),
                        targetOpt.get(),
                        request.getAllergens(),
                        request.getDietaryRestrictions());
            } else {
                dailyMeals = generateDefaultMeals(
                        savedDaily.getDailyPlanId());
            }

            dailyPlans.add(convertToDailyMealPlanDTO(
                    savedDaily, dailyMeals));
        }

        return convertToWeeklyMealPlanDTO(savedPlan, dailyPlans);
    }

    /**
     * Get a weekly meal plan by ID.
     *
     * @param weeklyPlanId the weekly plan ID
     * @return optional weekly meal plan DTO
     */
    public Optional<WeeklyMealPlanDTO> getWeeklyMealPlan(
            final Integer weeklyPlanId) {
        Optional<WeeklyMealPlan> planOpt =
                weeklyMealPlanRepository.findById(weeklyPlanId);

        if (planOpt.isEmpty()) {
            return Optional.empty();
        }

        WeeklyMealPlan plan = planOpt.get();
        List<DailyMealPlan> dailyPlans =
                dailyMealPlanRepository
                        .findByWeeklyPlanId(weeklyPlanId);

        List<DailyMealPlanDTO> dailyPlanDTOs = dailyPlans.stream()
                .map(daily -> {
                    List<Meal> meals = mealRepository
                            .findByDailyPlanId(daily.getDailyPlanId());
                    return convertToDailyMealPlanDTO(daily, meals);
                })
                .collect(Collectors.toList());

        return Optional.of(convertToWeeklyMealPlanDTO(plan,
                dailyPlanDTOs));
    }

    /**
     * Get all weekly meal plans for a user.
     *
     * @param userId the user ID
     * @return list of weekly meal plan DTOs
     */
    public List<WeeklyMealPlanDTO> getWeeklyMealPlansByUserId(
            final Integer userId) {
        List<WeeklyMealPlan> plans =
                weeklyMealPlanRepository.findByUserId(userId);

        return plans.stream()
                .map(plan -> {
                    List<DailyMealPlan> dailyPlans =
                            dailyMealPlanRepository
                                    .findByWeeklyPlanId(
                                            plan.getWeeklyPlanId());

                    List<DailyMealPlanDTO> dailyPlanDTOs =
                            dailyPlans.stream()
                            .map(daily -> {
                                List<Meal> meals = mealRepository
                                        .findByDailyPlanId(
                                                daily.getDailyPlanId());
                                return convertToDailyMealPlanDTO(daily,
                                        meals);
                            })
                            .collect(Collectors.toList());

                    return convertToWeeklyMealPlanDTO(plan,
                            dailyPlanDTOs);
                })
                .collect(Collectors.toList());
    }

    /**
     * Convert a Meal entity to MealDTO.
     *
     * @param meal the meal entity
     * @return meal DTO
     */
    private MealDTO convertToMealDTO(final Meal meal) {
        MealDTO dto = new MealDTO();
        dto.setMealId(meal.getMealId());
        dto.setRecipeId(meal.getRecipeId());
        dto.setMealType(meal.getMealType());
        dto.setMealName(meal.getMealName());
        dto.setCalories(meal.getCalories());
        dto.setProtein(meal.getProtein());
        dto.setCarbs(meal.getCarbs());
        dto.setFat(meal.getFat());
        dto.setFiber(meal.getFiber());
        return dto;
    }

    /**
     * Convert a DailyMealPlan entity to DailyMealPlanDTO.
     *
     * @param plan the daily meal plan entity
     * @param meals the list of meals
     * @return daily meal plan DTO
     */
    private DailyMealPlanDTO convertToDailyMealPlanDTO(
            final DailyMealPlan plan, final List<Meal> meals) {

        DailyMealPlanDTO dto = new DailyMealPlanDTO();
        dto.setDailyPlanId(plan.getDailyPlanId());
        dto.setUserId(plan.getUserId());
        dto.setPlanDate(plan.getPlanDate());
        dto.setPlanName(plan.getPlanName());
        dto.setMeals(meals.stream()
                .map(this::convertToMealDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    /**
     * Convert a WeeklyMealPlan entity to WeeklyMealPlanDTO.
     *
     * @param plan the weekly meal plan entity
     * @param dailyPlans the list of daily meal plan DTOs
     * @return weekly meal plan DTO
     */
    private WeeklyMealPlanDTO convertToWeeklyMealPlanDTO(
            final WeeklyMealPlan plan,
            final List<DailyMealPlanDTO> dailyPlans) {

        WeeklyMealPlanDTO dto = new WeeklyMealPlanDTO();
        dto.setWeeklyPlanId(plan.getWeeklyPlanId());
        dto.setUserId(plan.getUserId());
        dto.setStartDate(plan.getStartDate());
        dto.setEndDate(plan.getEndDate());
        dto.setPlanName(plan.getPlanName());
        dto.setDailyPlans(dailyPlans);
        return dto;
    }
}
