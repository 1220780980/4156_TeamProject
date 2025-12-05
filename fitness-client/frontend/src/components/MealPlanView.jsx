import React, { useState } from 'react';
import MealCard from './MealCard';
import MealPlanForm from './MealPlanForm';
import { searchRecipeByIngredient, generateMealPlan } from '../services/api';

function MealPlanView({ appUserId, user }) {
  const [mealPlan, setMealPlan] = useState(null);
  const [showForm, setShowForm] = useState(true);
  const [ingredientSearch, setIngredientSearch] = useState('');
  const [searchResult, setSearchResult] = useState(null);
  const [searchLoading, setSearchLoading] = useState(false);
  const [searchError, setSearchError] = useState('');

  const resolvedUserId = user?.appUserId || user?.id || appUserId;

  const handleMealPlanGenerated = async (formData) => {
    try {
      const realPlan = await generateMealPlan(resolvedUserId, formData);

      setMealPlan(realPlan);
      setShowForm(false);

    } catch (err) {
      console.error("Meal plan error:", err);
      setMealPlan({
        message: "Error generating meal plan: " + err.message
      });
    }
  };

  const handleLikeMeal = (meal) => console.log("Liked meal:", meal);
  const handleDislikeMeal = (meal) => console.log("Disliked meal:", meal);

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

      {/* INGREDIENT SEARCH */}
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

      {/* MEAL PLAN GENERATION */}
      {showForm ? (
        <div className="meal-plan-form-section">
          <MealPlanForm
            appUserId={resolvedUserId}
            onMealPlanGenerated={handleMealPlanGenerated}
          />
        </div>
      ) : (
        <div className="meal-plan-display-section">
          {mealPlan && (
            <>
              <div className="meal-plan-actions">
                <button
                  onClick={handleGenerateNewPlan}
                  className="generate-new-btn"
                >
                  Generate New Meal Plan
                </button>
              </div>

              {/* ERROR MESSAGE FROM BACKEND */}
              {mealPlan.message ? (
                <div className="info-message">
                  <p>{mealPlan.message}</p>
                </div>
              ) : (
                <>
                  {/* REAL WEEKLY MEAL PLAN */}
                  {mealPlan.days?.length > 0 ? (
                    <div className="weekly-meal-plan">
                      {mealPlan.days.map((day, index) => (
                        <div key={index} className="daily-meal-plan">
                          <h3 className="day-header">
                            {day.day || `Day ${index + 1}`}
                          </h3>

                          <div className="meals-grid">
                            {day.meals?.map((meal, mealIndex) => (
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
