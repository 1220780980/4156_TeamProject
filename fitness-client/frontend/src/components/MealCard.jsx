import React, { useState } from 'react';

function MealCard({ meal, onLike, onDislike }) {
  const [isExpanded, setIsExpanded] = useState(false);
  const [userReaction, setUserReaction] = useState(null); // 'like' or 'dislike'

  const handleLike = () => {
    if (userReaction !== 'like') {
      setUserReaction('like');
      if (onLike) onLike(meal);
    } else {
      setUserReaction(null);
    }
  };

  const handleDislike = () => {
    if (userReaction !== 'dislike') {
      setUserReaction('dislike');
      if (onDislike) onDislike(meal);
    } else {
      setUserReaction(null);
    }
  };

  return (
    <div className={`meal-card ${userReaction ? `meal-card-${userReaction}` : ''}`}>
      <div className="meal-card-header">
        <h3 className="meal-name">{meal.name || 'Unnamed Meal'}</h3>
        <div className="meal-actions">
          <button
            className={`like-btn ${userReaction === 'like' ? 'active' : ''}`}
            onClick={handleLike}
            title="Like this meal"
          >
            {userReaction === 'like' ? '❤️' : '🤍'}
          </button>
          <button
            className={`dislike-btn ${userReaction === 'dislike' ? 'active' : ''}`}
            onClick={handleDislike}
            title="Dislike this meal"
          >
            {userReaction === 'dislike' ? '👎' : '👍'}
          </button>
        </div>
      </div>

      <div className="meal-nutrition">
        <div className="nutrition-item">
          <span className="nutrition-label">Calories:</span>
          <span className="nutrition-value">{meal.calories || 0}</span>
        </div>
        <div className="nutrition-item">
          <span className="nutrition-label">Protein:</span>
          <span className="nutrition-value">{meal.protein || 0}g</span>
        </div>
        <div className="nutrition-item">
          <span className="nutrition-label">Carbs:</span>
          <span className="nutrition-value">{meal.carbs || 0}g</span>
        </div>
        <div className="nutrition-item">
          <span className="nutrition-label">Fat:</span>
          <span className="nutrition-value">{meal.fat || 0}g</span>
        </div>
        {meal.prepTime && (
          <div className="nutrition-item">
            <span className="nutrition-label">Prep Time:</span>
            <span className="nutrition-value">{meal.prepTime} min</span>
          </div>
        )}
      </div>

      {meal.description && (
        <p className="meal-description">{meal.description}</p>
      )}

      {meal.instructions && (
        <div className="meal-instructions">
          <button
            className="toggle-instructions-btn"
            onClick={() => setIsExpanded(!isExpanded)}
          >
            {isExpanded ? 'Hide' : 'Show'} Cooking Instructions
            <span className={`arrow ${isExpanded ? 'expanded' : ''}`}>▼</span>
          </button>
          {isExpanded && (
            <div className="instructions-content">
              <p>{meal.instructions}</p>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default MealCard;

