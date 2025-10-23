SET search_path TO nutriflow;

-- Seafood case
INSERT INTO substitution_rules (ingredient, avoid, substitute, note)
SELECT 'salmon', 'seafood', 'chicken breast', 'Similar protein; adjust cook time.'
WHERE NOT EXISTS (
  SELECT 1 FROM substitution_rules
  WHERE ingredient='salmon' AND COALESCE(avoid,'')='seafood' AND substitute='chicken breast'
);

-- Lactose cases
INSERT INTO substitution_rules (ingredient, avoid, substitute, note)
SELECT 'greek yogurt', 'lactose', 'coconut yogurt', 'Dairy-free option.'
WHERE NOT EXISTS (
  SELECT 1 FROM substitution_rules
  WHERE ingredient='greek yogurt' AND COALESCE(avoid,'')='lactose' AND substitute='coconut yogurt'
);

INSERT INTO substitution_rules (ingredient, avoid, substitute, note)
SELECT 'milk', 'lactose', 'almond milk (unsweetened)', 'Use 1:1.'
WHERE NOT EXISTS (
  SELECT 1 FROM substitution_rules
  WHERE ingredient='milk' AND COALESCE(avoid,'')='lactose' AND substitute='almond milk (unsweetened)'
);

INSERT INTO substitution_rules (ingredient, avoid, substitute, note)
SELECT 'parmesan cheese', 'lactose', 'nutritional yeast', 'Cheesy flavor; 1–2 tbsp.'
WHERE NOT EXISTS (
  SELECT 1 FROM substitution_rules
  WHERE ingredient='parmesan cheese' AND COALESCE(avoid,'')='lactose' AND substitute='nutritional yeast'
);

-- Gluten cases
INSERT INTO substitution_rules (ingredient, avoid, substitute, note)
SELECT 'bread', 'gluten', 'gluten-free bread', 'Same slices.'
WHERE NOT EXISTS (
  SELECT 1 FROM substitution_rules
  WHERE ingredient='bread' AND COALESCE(avoid,'')='gluten' AND substitute='gluten-free bread'
);

INSERT INTO substitution_rules (ingredient, avoid, substitute, note)
SELECT 'granola', 'gluten', 'gluten-free granola', '1:1.'
WHERE NOT EXISTS (
  SELECT 1 FROM substitution_rules
  WHERE ingredient='granola' AND COALESCE(avoid,'')='gluten' AND substitute='gluten-free granola'
);

INSERT INTO substitution_rules (ingredient, avoid, substitute, note)
SELECT 'soy sauce', 'gluten', 'tamari (gluten-free soy sauce)', 'Reduce slightly for saltiness.'
WHERE NOT EXISTS (
  SELECT 1 FROM substitution_rules
  WHERE ingredient='soy sauce' AND COALESCE(avoid,'')='gluten' AND substitute='tamari (gluten-free soy sauce)'
);

-- General (avoid is NULL)
INSERT INTO substitution_rules (ingredient, avoid, substitute, note)
SELECT 'milk', NULL, 'oat milk', 'Neutral flavor.'
WHERE NOT EXISTS (
  SELECT 1 FROM substitution_rules
  WHERE ingredient='milk' AND avoid IS NULL AND substitute='oat milk'
);

INSERT INTO substitution_rules (ingredient, avoid, substitute, note)
SELECT 'greek yogurt', NULL, 'plain yogurt', 'Similar texture.'
WHERE NOT EXISTS (
  SELECT 1 FROM substitution_rules
  WHERE ingredient='greek yogurt' AND avoid IS NULL AND substitute='plain yogurt'
);