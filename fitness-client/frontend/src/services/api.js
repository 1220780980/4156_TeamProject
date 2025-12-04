import axios from 'axios';

// Configure the base URL for backend API
const API_BASE_URL = 'http://localhost:8081/api';

// Create axios instance with default config
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

/**
 * Register a new user
 * @param {Object} userData - User registration data
 * @returns {Promise} Response data from the server
 */
export const registerUser = async (userData) => {
  try {
    const response = await apiClient.post('/users/register', userData);
    return response.data;
  } catch (error) {
    if (error.response) {
      throw new Error(error.response.data.error || 'Registration failed');
    } else if (error.request) {
      // Request made but no response
      throw new Error('Cannot connect to server. Please check if the backend is running.');
    } else {
      throw new Error('An unexpected error occurred');
    }
  }
};

/**
 * Login user with email and password
 * @param {Object} credentials - User login credentials (email, password)
 * @returns {Promise} Response data from the server
 */
export const loginUser = async (credentials) => {
  try {
    const response = await apiClient.post('/users/login', credentials);
    return response.data;
  } catch (error) {
    if (error.response) {
      throw new Error(error.response.data.error || 'Login failed');
    } else if (error.request) {
      // Request made but no response
      throw new Error('Cannot connect to server. Please check if the backend is running.');
    } else {
      throw new Error('An unexpected error occurred');
    }
  }
};

/**
 * Request a meal plan from the backend
 * @param {Number} appUserId - The app user ID
 * @param {Object} preferences - Meal plan preferences (mealsPerDay, maxPrepTime, allergies, dislikedMeals, preferredIngredients)
 * @returns {Promise} Response data from the server
 */
export const requestMealPlan = async (appUserId, preferences) => {
  try {
    const response = await apiClient.post(`/mealplans/request/${appUserId}`, preferences);
    return response.data;
  } catch (error) {
    if (error.response) {
      throw new Error(error.response.data.error || 'Failed to request meal plan');
    } else if (error.request) {
      throw new Error('Cannot connect to server. Please check if the backend is running.');
    } else {
      throw new Error('An unexpected error occurred');
    }
  }
};

/**
 * Search for recipes by ingredient
 * NOTE: This endpoint needs to be implemented in the backend to proxy to NutriFlow API
 * @param {String} ingredient - The ingredient to search for
 * @returns {Promise} Recipe data from NutriFlow
 */
export const searchRecipeByIngredient = async (ingredient) => {
  try {
    // TODO: Backend needs to implement this endpoint to proxy to NutriFlow
    // For now, this will fail gracefully with a helpful error message
    // Expected endpoint: GET /api/recipes/ingredient/{ingredient}
    // This should call NutriFlow's /api/ai/recipes/ingredient/{ingredient}
    const response = await apiClient.get(`/recipes/ingredient/${encodeURIComponent(ingredient)}`);
    return response.data;
  } catch (error) {
    if (error.response) {
      throw new Error(error.response.data.error || 'Failed to search recipe');
    } else if (error.request) {
      throw new Error('Ingredient search endpoint not yet implemented. Please implement a backend endpoint that proxies to NutriFlow API.');
    } else {
      throw new Error('An unexpected error occurred');
    }
  }
};