import React, { useState } from 'react';
import { registerUser } from '../services/api';

function RegisterForm({ onRegisterSuccess }) {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    confirmPassword: '',
    age: '',
    height: '',
    weight: '',
    sex: '',
    activityLevel: '',
    fitnessGoal: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    // Clear error when user types
    if (error) setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    // Validate password match
    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    // Validate required fields
    if (!formData.email || !formData.password) {
      setError('Email and password are required');
      return;
    }

    setLoading(true);

    try {
      // Prepare data for API
      const { confirmPassword, ...registrationData } = formData;
      
      const payload = {
        ...registrationData,
        age: formData.age ? parseInt(formData.age) : null,
        height: formData.height ? parseFloat(formData.height) : null,
        weight: formData.weight ? parseFloat(formData.weight) : null
      };

      const response = await registerUser(payload);
      onRegisterSuccess(response);
    } catch (err) {
      setError(err.message || 'Registration failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="auth-form register-form" onSubmit={handleSubmit}>
      {error && <div className="error-message">{error}</div>}
      
      <div className="form-group">
        <label htmlFor="email">Email Address *</label>
        <input
          type="email"
          id="email"
          name="email"
          value={formData.email}
          onChange={handleChange}
          placeholder="Enter your email"
          required
        />
      </div>

      <div className="form-group">
        <label htmlFor="password">Password *</label>
        <input
          type="password"
          id="password"
          name="password"
          value={formData.password}
          onChange={handleChange}
          placeholder="Create a password"
          required
          minLength="6"
        />
      </div>

      <div className="form-group">
        <label htmlFor="confirmPassword">Confirm Password *</label>
        <input
          type="password"
          id="confirmPassword"
          name="confirmPassword"
          value={formData.confirmPassword}
          onChange={handleChange}
          placeholder="Confirm your password"
          required
          minLength="6"
        />
      </div>

      <div className="form-row">
        <div className="form-group">
          <label htmlFor="age">Age</label>
          <input
            type="number"
            id="age"
            name="age"
            value={formData.age}
            onChange={handleChange}
            placeholder="Age"
            min="1"
            max="120"
          />
        </div>

        <div className="form-group">
          <label htmlFor="sex">Sex</label>
          <select
            id="sex"
            name="sex"
            value={formData.sex}
            onChange={handleChange}
          >
            <option value="">Select</option>
            <option value="MALE">Male</option>
            <option value="FEMALE">Female</option>
            <option value="OTHER">Other</option>
          </select>
        </div>
      </div>

      <div className="form-row">
        <div className="form-group">
          <label htmlFor="height">Height (cm)</label>
          <input
            type="number"
            id="height"
            name="height"
            value={formData.height}
            onChange={handleChange}
            placeholder="Height"
            min="1"
            step="0.01"
          />
        </div>

        <div className="form-group">
          <label htmlFor="weight">Weight (kg)</label>
          <input
            type="number"
            id="weight"
            name="weight"
            value={formData.weight}
            onChange={handleChange}
            placeholder="Weight"
            min="1"
            step="0.01"
          />
        </div>
      </div>

      <div className="form-group">
        <label htmlFor="activityLevel">Activity Level</label>
        <select
          id="activityLevel"
          name="activityLevel"
          value={formData.activityLevel}
          onChange={handleChange}
        >
          <option value="">Select Activity Level</option>
          <option value="SEDENTARY">Sedentary (little or no exercise)</option>
          <option value="LIGHTLY_ACTIVE">Lightly Active (exercise 1-3 days/week)</option>
          <option value="MODERATELY_ACTIVE">Moderately Active (exercise 3-5 days/week)</option>
          <option value="VERY_ACTIVE">Very Active (exercise 6-7 days/week)</option>
          <option value="EXTREMELY_ACTIVE">Extremely Active (physical job or training twice per day)</option>
        </select>
      </div>

      <div className="form-group">
        <label htmlFor="fitnessGoal">Fitness Goal</label>
        <select
          id="fitnessGoal"
          name="fitnessGoal"
          value={formData.fitnessGoal}
          onChange={handleChange}
        >
          <option value="">Select Fitness Goal</option>
          <option value="LOSE_WEIGHT">Lose Weight</option>
          <option value="GAIN_MUSCLE">Gain Muscle</option>
          <option value="MAINTAIN">Maintain Weight</option>
          <option value="IMPROVE_ENDURANCE">Improve Endurance</option>
          <option value="GENERAL_FITNESS">General Fitness</option>
        </select>
      </div>

      <button 
        type="submit" 
        className="submit-btn"
        disabled={loading}
      >
        {loading ? 'Creating Account...' : 'Create Account'}
      </button>

      <p className="form-note">* Required fields</p>
    </form>
  );
}

export default RegisterForm;