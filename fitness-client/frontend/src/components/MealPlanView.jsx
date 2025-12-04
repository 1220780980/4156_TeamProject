import React, { useState } from 'react';
import MealCard from './MealCard';
import MealPlanForm from './MealPlanForm';
import { searchRecipeByIngredient } from '../services/api';

function MealPlanView({ appUserId, user }) {
  const [mealPlan, setMealPlan] = useState(null);
  const [showForm, setShowForm] = useState(true);
  const [ingredientSearch, setIngredientSearch] = useState('');
  const [searchResult, setSearchResult] = useState(null);
  const [searchLoading, setSearchLoading] = useState(false);
  const [searchError, setSearchError] = useState('');

  const handleMealPlanGenerated = (data) => {
    // The backend returns the enriched request DTO
    // In a real implementation, this would trigger a call to NutriFlow
    // and return the actual meal plan. For now, we'll create a mock structure
    console.log('Meal plan request generated:', data);
    
    // TODO: This should call NutriFlow API to get actual meal plan
    // For now, we'll show a message that the request was created
    setMealPlan({
      request: data,
      message: 'Meal plan request created. In a full implementation, this would fetch the actual meal plan from NutriFlow.'
    });
    setShowForm(false);
  };

  const handleLikeMeal = (meal) => {
    console.log('Liked meal:', meal);
    // TODO: Implement logic to save liked meals or request alternatives
  };

  const handleDislikeMeal = (meal) => {
    console.log('Disliked meal:', meal);
    // TODO: Implement logic to request alternative meals
    // This could trigger a new meal plan generation excluding this meal
  };

  const handleIngredientSearch = async (e) => {
    e.preventDefault();
    if (!ingredientSearch.trim()) return;

    setSearchLoading(true);
    setSearchError('');
    setSearchResult(null);

    try {
      const result = await searchRecipeByIngredient(ingredientSearch);
      setSearchResult(result);
    } catch (err) {
      setSearchError(err.message || 'Failed to search for recipe');
    } finally {
      setSearchLoading(false);
    }
  };

  const handleGenerateNewPlan = () => {
    setMealPlan(null);
    setShowForm(true);
  };

  return (
    <div className="meal-plan-view">
      <div className="meal-plan-header">
        <h1>Meal Planning</h1>
        <p>Create personalized meal plans based on your preferences</p>
      </div>

      {/* Ingredient-based Search */}
      <div className="ingredient-search-section">
        <h2>Search Recipes by Ingredient</h2>
        <form onSubmit={handleIngredientSearch} className="search-form">
          <input
            type="text"
            value={ingredientSearch}
            onChange={(e) => setIngredientSearch(e.target.value)}
            placeholder="Enter an ingredient (e.g., chicken, broccoli)"
            className="search-input"
          />
          <button type="submit" disabled={searchLoading} className="search-btn">
            {searchLoading ? 'Searching...' : 'Search'}
          </button>
        </form>
        {searchError && <div className="error-message">{searchError}</div>}
        {searchResult && (
          <div className="search-result">
            <MealCard
              meal={searchResult}
              onLike={handleLikeMeal}
              onDislike={handleDislikeMeal}
            />
          </div>
        )}
      </div>

      {/* Meal Plan Generation */}
      {showForm ? (
        <div className="meal-plan-form-section">
          <MealPlanForm
            appUserId={appUserId}
            onMealPlanGenerated={handleMealPlanGenerated}
          />
        </div>
      ) : (
        <div className="meal-plan-display-section">
          {mealPlan && (
            <>
              <div className="meal-plan-actions">
                <button onClick={handleGenerateNewPlan} className="generate-new-btn">
                  Generate New Meal Plan
                </button>
              </div>
              
              {mealPlan.message ? (
                <div className="info-message">
                  <p>{mealPlan.message}</p>
                  <p>Request Details:</p>
                  <ul>
                    <li>Meals per day: {mealPlan.request.mealsPerDay}</li>
                    <li>Max prep time: {mealPlan.request.maxPrepTime} minutes</li>
                    {mealPlan.request.allergies && mealPlan.request.allergies.length > 0 && (
                      <li>Allergies: {mealPlan.request.allergies.join(', ')}</li>
                    )}
                    {mealPlan.request.dislikedMeals && mealPlan.request.dislikedMeals.length > 0 && (
                      <li>Disliked meals: {mealPlan.request.dislikedMeals.join(', ')}</li>
                    )}
                    {mealPlan.request.preferredIngredients && mealPlan.request.preferredIngredients.length > 0 && (
                      <li>Preferred ingredients: {mealPlan.request.preferredIngredients.join(', ')}</li>
                    )}
                  </ul>
                </div>
              ) : (
                <>
                  {mealPlan.days && mealPlan.days.length > 0 ? (
                    <div className="weekly-meal-plan">
                      {mealPlan.days.map((day, index) => (
                        <div key={index} className="daily-meal-plan">
                          <h3 className="day-header">{day.day || `Day ${index + 1}`}</h3>
                          <div className="meals-grid">
                            {day.meals && day.meals.map((meal, mealIndex) => (
                              <MealCard
                                key={mealIndex}
                                meal={meal}
                                onLike={handleLikeMeal}
                                onDislike={handleDislikeMeal}
                              />
                            ))}
                          </div>
                        </div>
                      ))}
                    </div>
                  ) : (
                    <div className="no-meals">
                      <p>No meals found in this meal plan.</p>
                    </div>
                  )}
                </>
              )}
            </>
          )}
        </div>
      )}
    </div>
  );
}

export default MealPlanView;

