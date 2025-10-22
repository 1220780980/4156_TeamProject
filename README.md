# NutriFlow
## Overview

NutriFlow is a personalized nutrition and recipe recommendation platform.
This milestone implements core backend modules for user data management, pantry tracking, and recipe retrieval, built with Spring Boot, Spring Data JPA, and PostgreSQL.

## How to Run
1. use Java 17
```shell
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH="$JAVA_HOME/bin:$PATH"
java -version
```  
(Ensure it prints a Java 17 version.)
2. Compile
```shell
mvn clean compile
```
3. Run the application
```
mvn spring-boot:run
```   
The service will start on `http://localhost:8080` by default.
## Implemented Features
### User

### Recipe Management

Purpose: Store and retrieve recipe data, including nutritional information and popularity ranking.

Modules:
- Entity: Recipe
    - Fields: title, cuisines, tags, ingredients (JSONB), nutrition (JSONB), macros, popularity score
- Repository: RecipeRepository
    - Custom query: findPopularRecipes(`Pageable pageable`)
- Service: RecipeService
    - `getRecipeById(Integer id)`
    - `getPopularRecipesDefault()` (top 5)
    - `getPopularRecipes(int limit)` (custom size)
    - `getUserFavoriteRecipes(Integer userId)`
- Controller: RecipeController
    - GET `/api/recipes/{id}` → retrieve recipe by ID
    - GET `/api/recipes/popular?limit={n}` → retrieve top N recipes
    - GET `/api/recipes/{userId}/favorites` → placeholder for favorite recipes

### Pantry Management

Purpose: Enable users to manage ingredients they currently have at home.

Modules:
- Entity: PantryItem
    - Fields: itemId, userId, name, quantity, unit, timestamps
    - Auto-managed timestamps via @PrePersist and @PreUpdate
- Repository: PantryRepository
    - `findByUserId(Integer userId)`
- Service: PantryService
    - `getPantryItems(Integer userId)`
    - `updatePantryItems(Integer userId, List<PantryItem> items)` replaces user’s pantry with the new list
- Controller: PantryController
    - `GET /api/users/{userId}/pantry` get all pantry items for user
    - `PUT /api/users/{userId}/pantry` replace all pantry items

### Favorites Management
Purpose: Allow users to mark recipes as favorites and track how often they use them.

Modules:

- Entity: FavoriteRecipe
    - Maps a user to recipes they’ve marked as favorites
    - Fields: favoriteId, userId, recipeId, timesUsed
- Repository: FavoriteRecipeRepository
- Methods:
    - `findByUserId(Integer userId)`
    - `findByUserIdAndRecipeId(Integer userId, Integer recipeId)`
- Service: FavoriteRecipeService
    - `addFavorite(Integer userId, Integer recipeId)` → Adds a recipe to favorites
    - `getFavoritesByUser(Integer userId)` → Returns user’s favorite recipes
    - `removeFavorite(Integer userId, Integer recipeId)` → Removes a recipe from favorites
- Controller: FavoriteRecipeController 
    - `POST /api/recipes/{userId}/favorites/{recipeId}` → Add a recipe to favorites
    - `GET /api/recipes/{userId}/favorites` → Retrieve user’s favorite recipes
    - `DELETE /api/recipes/{userId}/favorites/{recipeId}` → Remove a recipe from favorites


## Database & Data Seeding

Schema: nutriflow

Tables:
- users
- user_targets
- user_health_history
- recipes
- pantry_items
- favorite_recipes (future use)