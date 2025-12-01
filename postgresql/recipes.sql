-- ========================
-- Recipes
-- ========================
INSERT INTO nutriflow.recipes
    (recipe_id, title, cook_time, calories, carbohydrates, fat, fiber, protein, popularity_score)
VALUES
    (1, 'Avocado Toast', 10, 320, 30, 18, 8, 9, 90),
    (2, 'Oatmeal with Banana', 8, 350, 60, 6, 7, 10, 80),
    (3, 'Grilled Chicken Salad', 20, 420, 12, 22, 5, 45, 95),
    (4, 'Vegetable Stir Fry', 15, 280, 35, 10, 6, 8, 70),
    (5, 'Greek Yogurt Parfait', 5, 250, 35, 5, 2, 14, 85),
    (6, 'Quinoa Bowl with Black Beans', 25, 460, 60, 10, 10, 18, 88),
    (7, 'Smoothie Bowl (Berry Blast)', 7, 310, 45, 8, 7, 9, 92),
    (8, 'Salmon with Asparagus', 25, 480, 10, 28, 4, 42, 97);

UPDATE nutriflow.recipes SET instructions =
  'Toast bread; mash avocado with salt and lemon; spread; top with egg or chili flakes; serve.'
WHERE recipe_id = 1;

UPDATE nutriflow.recipes SET instructions =
  'Simmer oats in milk or water; stir until creamy; top with sliced banana, honey, and nuts.'
WHERE recipe_id = 2;

UPDATE nutriflow.recipes SET instructions =
  'Season chicken; grill until cooked; slice; toss greens, tomatoes, cucumber; add chicken; drizzle vinaigrette.'
WHERE recipe_id = 3;

UPDATE nutriflow.recipes SET instructions =
  'Heat oil; stir fry mixed veggies with garlic and soy; add splash of water to steam; finish with sesame oil.'
WHERE recipe_id = 4;

UPDATE nutriflow.recipes SET instructions =
  'Layer yogurt, granola, and berries in a glass; drizzle honey; serve immediately.'
WHERE recipe_id = 5;

UPDATE nutriflow.recipes SET instructions =
  'Cook quinoa; sauté onions and peppers; add black beans and spices; serve over quinoa with lime and cilantro.'
WHERE recipe_id = 6;

UPDATE nutriflow.recipes SET instructions =
  'Blend frozen berries, banana, and milk until thick; pour into bowl; top with granola and sliced fruit.'
WHERE recipe_id = 7;

UPDATE nutriflow.recipes SET instructions =
  'Season salmon; sear or roast until flaky; roast asparagus with oil and salt; serve together with lemon.'
WHERE recipe_id = 8;

-- ========================
-- Favorite Recipes
-- ========================
INSERT INTO nutriflow.favorite_recipes
    (favorite_id, user_id, recipe_id, times_used, is_favorite)
VALUES
    (1, 1, 1, 5, TRUE),
    (2, 1, 3, 3, TRUE),
    (3, 1, 8, 1, TRUE),
    (4, 2, 2, 4, TRUE),
    (5, 2, 5, 2, TRUE);
