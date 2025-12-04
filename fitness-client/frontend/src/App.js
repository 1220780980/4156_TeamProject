import React, { useState } from 'react';
import LoginForm from './components/LoginForm';
import RegisterForm from './components/RegisterForm';
import MealPlanView from './components/MealPlanView';
import './App.css';

function App() {
  const [isLogin, setIsLogin] = useState(true);
  const [loggedInUser, setLoggedInUser] = useState(null);
  const [currentView, setCurrentView] = useState('profile'); // 'profile' or 'mealplan'

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
    setCurrentView('profile');
  };

  if (loggedInUser) {
    return (
      <div className="app">
        <div className="main-container">
          <div className="sidebar">
            <div className="sidebar-header">
              <h2>Fitness Client</h2>
            </div>
            <nav className="sidebar-nav">
              <button
                className={`nav-btn ${currentView === 'profile' ? 'active' : ''}`}
                onClick={() => setCurrentView('profile')}
              >
                Profile
              </button>
              <button
                className={`nav-btn ${currentView === 'mealplan' ? 'active' : ''}`}
                onClick={() => setCurrentView('mealplan')}
              >
                Meal Plans
              </button>
            </nav>
            <button className="logout-btn" onClick={handleLogout}>
              Logout
            </button>
          </div>

          <div className="content-area">
            {currentView === 'profile' && (
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
                </div>
              </div>
            )}

            {currentView === 'mealplan' && (
              <MealPlanView
                appUserId={loggedInUser.id}
                user={loggedInUser}
              />
            )}
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