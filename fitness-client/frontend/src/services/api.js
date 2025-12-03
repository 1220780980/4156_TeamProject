import axios from 'axios';

// Configure the base URL for backend API
const API_BASE_URL = 'http://localhost:8081/api/users';

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
    const response = await apiClient.post('/register', userData);
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
    const response = await apiClient.post('/login', credentials);
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