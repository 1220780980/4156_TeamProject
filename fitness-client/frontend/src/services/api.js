import axios from 'axios';

// Configure the base URL for backend API
const API_BASE_URL = 'http://localhost:8081/api';

// Create axios instance with default config
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' }
});

/**
 * Register a new user
 * @param {Object} userData - User registration data
 * @returns {Promise} Response data from the server
 */
export const registerUser = async (userData) => {
  const response = await apiClient.post('/users/register', userData);
  return response.data;
};

/**
 * Login user with email and password
 * @param {Object} credentials - User login credentials (email, password)
 * @returns {Promise} Response data from the server
 */
export const loginUser = async (credentials) => {
  const response = await apiClient.post('/users/login', credentials);
  return response.data;
};

/**
 * Request a meal plan from the backend
 * @param {Number} appUserId - The app user ID
 * @param {Object} preferences - Meal plan preferences (mealsPerDay, maxPrepTime, allergies, dislikedMeals, preferredIngredients)
 * @returns {Promise} Response data from the server
 */
export const requestMealPlan = async (appUserId, preferences) => {
  const response = await apiClient.post(`/mealplans/request/${appUserId}`, preferences);
  return response.data;
};

/**
 * Search for recipes by ingredient
 * NOTE: This endpoint needs to be implemented in the backend to proxy to NutriFlow API
 * @param {String} ingredient - The ingredient to search for
 * @returns {Promise} Recipe data from NutriFlow
 */
export const searchRecipeByIngredient = async (ingredient) => {
  const response = await apiClient.get(
    `/proxy/recipes/ingredient/${encodeURIComponent(ingredient)}`
  );
  return response.data;
};


export const generateMealPlan = async (appUserId, prefs) => {
  const response = await apiClient.post(
    `/mealplans/generate/${appUserId}`,
    {
      mealsPerDay: prefs.mealsPerDay,
      maxPrepTime: prefs.maxPrepTime,
      allergies: prefs.allergies,
      dislikedMeals: prefs.dislikedMeals,
      preferredIngredients: prefs.preferredIngredients
    }
  );
  return response.data;
};
