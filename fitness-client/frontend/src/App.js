import React, { useState } from 'react';
import LoginForm from './components/LoginForm';
import RegisterForm from './components/RegisterForm';
import './App.css';

function App() {
  const [isLogin, setIsLogin] = useState(true);
  const [loggedInUser, setLoggedInUser] = useState(null);

  const toggleForm = () => {
    setIsLogin(!isLogin);
  };

  const handleLoginSuccess = (userData) => {
    setLoggedInUser(userData);
    console.log('Login successful:', userData);
  };

  const handleRegisterSuccess = (userData) => {
    console.log('Registration successful:', userData);
    // Auto-switch to login after successful registration
    setIsLogin(true);
  };

  const handleLogout = () => {
    setLoggedInUser(null);
  };

  if (loggedInUser) {
    return (
      <div className="app">
        <div className="welcome-container">
          <div className="welcome-card">
            <h1>Welcome to Fitness Client!</h1>
            <div className="user-info">
              <h2>Your Profile</h2>
              <p><strong>Email:</strong> {loggedInUser.email}</p>
              <p><strong>Age:</strong> {loggedInUser.age} years</p>
              <p><strong>Height:</strong> {loggedInUser.height} cm</p>
              <p><strong>Weight:</strong> {loggedInUser.weight} kg</p>
              <p><strong>Sex:</strong> {loggedInUser.sex}</p>
              <p><strong>Activity Level:</strong> {loggedInUser.activityLevel}</p>
              <p><strong>Fitness Goal:</strong> {loggedInUser.fitnessGoal}</p>
              {loggedInUser.nutriflowUserId && (
                <p><strong>Nutriflow User ID:</strong> {loggedInUser.nutriflowUserId}</p>
              )}
            </div>
            <button className="logout-btn" onClick={handleLogout}>
              Logout
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="app">
      <div className="auth-container">
        <div className="auth-card">
          <div className="auth-header">
            <h1>Fitness Client</h1>
            <p className="subtitle">
              {isLogin ? 'Sign in to your account' : 'Create a new account'}
            </p>
          </div>

          {isLogin ? (
            <LoginForm onLoginSuccess={handleLoginSuccess} />
          ) : (
            <RegisterForm onRegisterSuccess={handleRegisterSuccess} />
          )}

          <div className="toggle-form">
            <p>
              {isLogin ? "Don't have an account? " : 'Already have an account? '}
              <button className="toggle-btn" onClick={toggleForm}>
                {isLogin ? 'Sign Up' : 'Sign In'}
              </button>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;