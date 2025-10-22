SET search_path TO nutriflow;

-- -------------------------------------------
-- PANTRY ITEMS
-- -------------------------------------------

-- User 1: John Smith (allergies: peanuts, seafood; dislikes: cilantro, celery)
INSERT INTO pantry_items (user_id, name, quantity, unit)
VALUES
(1, 'white rice', 2.00, 'kg'),
(1, 'chicken breast', 1.50, 'kg'),
(1, 'olive oil', 750.00, 'ml'),
(1, 'broccoli', 3.00, 'pcs'),
(1, 'eggs', 12.00, 'pcs'),
(1, 'black beans (canned)', 4.00, 'cans'),
(1, 'garlic', 5.00, 'cloves'),
(1, 'greek yogurt', 500.00, 'g');

-- User 2: Maria Garcia (allergy: lactose; dislikes: bitter melon)
INSERT INTO pantry_items (user_id, name, quantity, unit)
VALUES
(2, 'brown rice', 1.50, 'kg'),
(2, 'tofu', 800.00, 'g'),
(2, 'chickpeas (canned)', 3.00, 'cans'),
(2, 'spinach', 2.00, 'bags'),
(2, 'almond milk (unsweetened)', 2.00, 'cartons'),
(2, 'avocado', 4.00, 'pcs'),
(2, 'olive oil', 500.00, 'ml'),
(2, 'quinoa', 1.00, 'kg');

-- User 3: David Johnson (dislikes: onions)
INSERT INTO pantry_items (user_id, name, quantity, unit)
VALUES
(3, 'whole wheat pasta', 1.00, 'kg'),
(3, 'ground turkey', 1.00, 'kg'),
(3, 'tomato sauce (no onion)', 3.00, 'jars'),
(3, 'parmesan cheese', 200.00, 'g'),
(3, 'zucchini', 5.00, 'pcs'),
(3, 'olive oil', 500.00, 'ml'),
(3, 'black pepper', 1.00, 'jar'),
(3, 'oats', 1.00, 'kg');

-- User 4: Priya Patel (allergies: shellfish, tree nuts; dislikes: fish sauce, organ meat)
INSERT INTO pantry_items (user_id, name, quantity, unit)
VALUES
(4, 'basmati rice', 2.00, 'kg'),
(4, 'red lentils', 1.50, 'kg'),
(4, 'ghee', 400.00, 'g'),
(4, 'turmeric powder', 1.00, 'jar'),
(4, 'cumin seeds', 1.00, 'jar'),
(4, 'garam masala', 1.00, 'jar'),
(4, 'tomatoes', 6.00, 'pcs'),
(4, 'spinach', 2.00, 'bags');

-- User 5: Alex Chen (allergy: gluten; dislikes: durian)
INSERT INTO pantry_items (user_id, name, quantity, unit)
VALUES
(5, 'jasmine rice', 2.00, 'kg'),
(5, 'rice noodles (gluten-free)', 1.00, 'kg'),
(5, 'tamari (gluten-free soy sauce)', 250.00, 'ml'),
(5, 'chicken thighs', 1.50, 'kg'),
(5, 'bok choy', 5.00, 'pcs'),
(5, 'tofu', 600.00, 'g'),
(5, 'sesame oil', 250.00, 'ml'),
(5, 'eggs', 12.00, 'pcs');

-- User 6: Sarah Connor (dislikes: bell peppers, eggplant)
INSERT INTO pantry_items (user_id, name, quantity, unit)
VALUES
(6, 'rolled oats', 1.50, 'kg'),
(6, 'chicken breast', 1.20, 'kg'),
(6, 'sweet potatoes', 6.00, 'pcs'),
(6, 'broccoli', 4.00, 'pcs'),
(6, 'olive oil', 500.00, 'ml'),
(6, 'greek yogurt', 500.00, 'g'),
(6, 'black beans (canned)', 3.00, 'cans'),
(6, 'bananas', 8.00, 'pcs');
