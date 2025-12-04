import React, { useState } from 'react';
import { requestMealPlan } from '../services/api';

function MealPlanForm({ appUserId, onMealPlanGenerated }) {
  const [formData, setFormData] = useState({
    mealsPerDay: 3,
    maxPrepTime: 60,
    allergies: [],
    dislikedMeals: [],
    preferredIngredients: []
  });
  const [allergyInput, setAllergyInput] = useState('');
  const [dislikedMealInput, setDislikedMealInput] = useState('');
  const [ingredientInput, setIngredientInput] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === 'mealsPerDay' || name === 'maxPrepTime' ? parseInt(value) || 0 : value
    }));
    if (error) setError('');
  };

  const handleAddAllergy = () => {
    if (allergyInput.trim() && !formData.allergies.includes(allergyInput.trim())) {
      setFormData(prev => ({
        ...prev,
        allergies: [...prev.allergies, allergyInput.trim()]
      }));
      setAllergyInput('');
    }
  };

  const handleRemoveAllergy = (allergy) => {
    setFormData(prev => ({
      ...prev,
      allergies: prev.allergies.filter(a => a !== allergy)
    }));
  };

  const handleAddDislikedMeal = () => {
    if (dislikedMealInput.trim() && !formData.dislikedMeals.includes(dislikedMealInput.trim())) {
      setFormData(prev => ({
        ...prev,
        dislikedMeals: [...prev.dislikedMeals, dislikedMealInput.trim()]
      }));
      setDislikedMealInput('');
    }
  };

  const handleRemoveDislikedMeal = (meal) => {
    setFormData(prev => ({
      ...prev,
      dislikedMeals: prev.dislikedMeals.filter(m => m !== meal)
    }));
  };

  const handleAddIngredient = () => {
    if (ingredientInput.trim() && !formData.preferredIngredients.includes(ingredientInput.trim())) {
      setFormData(prev => ({
        ...prev,
        preferredIngredients: [...prev.preferredIngredients, ingredientInput.trim()]
      }));
      setIngredientInput('');
    }
  };

  const handleRemoveIngredient = (ingredient) => {
    setFormData(prev => ({
      ...prev,
      preferredIngredients: prev.preferredIngredients.filter(i => i !== ingredient)
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await requestMealPlan(appUserId, formData);
      onMealPlanGenerated(response);
    } catch (err) {
      setError(err.message || 'Failed to generate meal plan. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="meal-plan-form" onSubmit={handleSubmit}>
      <h2>Generate Your Meal Plan</h2>
      {error && <div className="error-message">{error}</div>}
      
      <div className="form-group">
        <label htmlFor="mealsPerDay">Meals per Day</label>
        <input
          type="number"
          id="mealsPerDay"
          name="mealsPerDay"
          value={formData.mealsPerDay}
          onChange={handleChange}
          min="1"
          max="6"
          required
        />
      </div>

      <div className="form-group">
        <label htmlFor="maxPrepTime">Maximum Prep Time (minutes)</label>
        <input
          type="number"
          id="maxPrepTime"
          name="maxPrepTime"
          value={formData.maxPrepTime}
          onChange={handleChange}
          min="5"
          max="300"
          required
        />
      </div>

      <div className="form-group">
        <label htmlFor="allergies">Allergies</label>
        <div className="tag-input-group">
          <input
            type="text"
            id="allergies"
            value={allergyInput}
            onChange={(e) => setAllergyInput(e.target.value)}
            onKeyPress={(e) => {
              if (e.key === 'Enter') {
                e.preventDefault();
                handleAddAllergy();
              }
            }}
            placeholder="Enter an allergy and press Enter"
          />
          <button type="button" onClick={handleAddAllergy} className="add-btn">
            Add
          </button>
        </div>
        <div className="tag-list">
          {formData.allergies.map((allergy, index) => (
            <span key={index} className="tag">
              {allergy}
              <button
                type="button"
                onClick={() => handleRemoveAllergy(allergy)}
                className="tag-remove"
              >
                ×
              </button>
            </span>
          ))}
        </div>
      </div>

      <div className="form-group">
        <label htmlFor="dislikedMeals">Disliked Meals</label>
        <div className="tag-input-group">
          <input
            type="text"
            id="dislikedMeals"
            value={dislikedMealInput}
            onChange={(e) => setDislikedMealInput(e.target.value)}
            onKeyPress={(e) => {
              if (e.key === 'Enter') {
                e.preventDefault();
                handleAddDislikedMeal();
              }
            }}
            placeholder="Enter a disliked meal and press Enter"
          />
          <button type="button" onClick={handleAddDislikedMeal} className="add-btn">
            Add
          </button>
        </div>
        <div className="tag-list">
          {formData.dislikedMeals.map((meal, index) => (
            <span key={index} className="tag">
              {meal}
              <button
                type="button"
                onClick={() => handleRemoveDislikedMeal(meal)}
                className="tag-remove"
              >
                ×
              </button>
            </span>
          ))}
        </div>
      </div>

      <div className="form-group">
        <label htmlFor="preferredIngredients">Preferred Ingredients (Optional)</label>
        <div className="tag-input-group">
          <input
            type="text"
            id="preferredIngredients"
            value={ingredientInput}
            onChange={(e) => setIngredientInput(e.target.value)}
            onKeyPress={(e) => {
              if (e.key === 'Enter') {
                e.preventDefault();
                handleAddIngredient();
              }
            }}
            placeholder="Enter an ingredient and press Enter"
          />
          <button type="button" onClick={handleAddIngredient} className="add-btn">
            Add
          </button>
        </div>
        <div className="tag-list">
          {formData.preferredIngredients.map((ingredient, index) => (
            <span key={index} className="tag">
              {ingredient}
              <button
                type="button"
                onClick={() => handleRemoveIngredient(ingredient)}
                className="tag-remove"
              >
                ×
              </button>
            </span>
          ))}
        </div>
      </div>

      <button 
        type="submit" 
        className="submit-btn"
        disabled={loading}
      >
        {loading ? 'Generating Meal Plan...' : 'Generate Meal Plan'}
      </button>
    </form>
  );
}

export default MealPlanForm;

