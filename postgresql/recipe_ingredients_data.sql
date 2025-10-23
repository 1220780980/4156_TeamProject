SET search_path TO nutriflow;

-- (1) Avocado Toast
INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 1, 'bread', 2, 'slices', ARRAY['gluten']
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=1 AND ingredient='bread');

INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 1, 'avocado', 1, 'pcs', NULL
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=1 AND ingredient='avocado');

INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 1, 'olive oil', 10, 'ml', NULL
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=1 AND ingredient='olive oil');

-- (2) Oatmeal with Banana
INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 2, 'rolled oats', 80, 'g', NULL
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=2 AND ingredient='rolled oats');

INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 2, 'banana', 1, 'pcs', NULL
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=2 AND ingredient='banana');

INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 2, 'milk', 200, 'ml', ARRAY['lactose']
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=2 AND ingredient='milk');

-- (3) Grilled Chicken Salad
INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 3, 'chicken breast', 200, 'g', NULL
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=3 AND ingredient='chicken breast');

INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 3, 'lettuce', 100, 'g', NULL
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=3 AND ingredient='lettuce');

INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 3, 'parmesan cheese', 20, 'g', ARRAY['lactose']
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=3 AND ingredient='parmesan cheese');

-- (4) Vegetable Stir Fry
INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 4, 'soy sauce', 15, 'ml', ARRAY['gluten']
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=4 AND ingredient='soy sauce');

INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 4, 'tofu', 200, 'g', NULL
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=4 AND ingredient='tofu');

INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 4, 'broccoli', 150, 'g', NULL
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=4 AND ingredient='broccoli');

-- (5) Greek Yogurt Parfait
INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 5, 'greek yogurt', 200, 'g', ARRAY['lactose']
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=5 AND ingredient='greek yogurt');

INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 5, 'granola', 50, 'g', ARRAY['gluten']
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=5 AND ingredient='granola');

INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 5, 'berries', 80, 'g', NULL
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=5 AND ingredient='berries');

-- (6) Quinoa Bowl with Black Beans
INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 6, 'quinoa', 80, 'g', NULL
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=6 AND ingredient='quinoa');

INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 6, 'black beans', 150, 'g', NULL
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=6 AND ingredient='black beans');

INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 6, 'avocado', 1, 'pcs', NULL
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=6 AND ingredient='avocado');

-- (7) Smoothie Bowl (Berry Blast)
INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 7, 'milk', 150, 'ml', ARRAY['lactose']
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=7 AND ingredient='milk');

INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 7, 'mixed berries', 120, 'g', NULL
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=7 AND ingredient='mixed berries');

INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 7, 'banana', 1, 'pcs', NULL
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=7 AND ingredient='banana');

-- (8) Salmon with Asparagus
INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 8, 'salmon', 200, 'g', ARRAY['seafood']
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=8 AND ingredient='salmon');

INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit, allergen_tags)
SELECT 8, 'asparagus', 150, 'g', NULL
WHERE NOT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id=8 AND ingredient='asparagus');