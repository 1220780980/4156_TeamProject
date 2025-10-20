SET search_path TO nutriflow;

--
-- insert data into 'users' table
--
INSERT INTO users (user_id, name, height, weight, age, sex, allergies, dislikes, budget, cooking_skill_level, equipments, created_at, updated_at)
VALUES (1, 'John Smith', 175.00, 70.50, 28, 'MALE', ARRAY['peanuts', 'seafood'], ARRAY['cilantro', 'celery'], 500.00, 'INTERMEDIATE', ARRAY['oven', 'microwave', 'air fryer'], '2024-01-15 10:00:00', '2024-01-15 10:00:00');

INSERT INTO users (user_id, name, height, weight, age, sex, allergies, dislikes, budget, cooking_skill_level, equipments, created_at, updated_at)
VALUES (2, 'Maria Garcia', 165.00, 55.00, 32, 'FEMALE', ARRAY['lactose'], ARRAY['bitter melon'], 800.00, 'ADVANCED', ARRAY['oven', 'rice cooker', 'blender'], '2024-02-01 09:30:00', '2024-02-01 09:30:00');

INSERT INTO users (user_id, name, height, weight, age, sex, allergies, dislikes, budget, cooking_skill_level, equipments, created_at, updated_at)
VALUES (3, 'David Johnson', 180.00, 85.00, 35, 'MALE', ARRAY[]::text[], ARRAY['onions'], 300.00, 'BEGINNER', ARRAY['microwave'], '2024-02-10 14:20:00', '2024-02-10 14:20:00');

INSERT INTO users (user_id, name, height, weight, age, sex, allergies, dislikes, budget, cooking_skill_level, equipments, created_at, updated_at)
VALUES (4, 'Priya Patel', 160.00, 52.00, 25, 'FEMALE', ARRAY['shellfish', 'tree nuts'], ARRAY['fish sauce', 'organ meat'], 1000.00, 'EXPERT', ARRAY['oven', 'microwave', 'rice cooker', 'air fryer', 'slow cooker'], '2024-02-15 11:00:00', '2024-02-15 11:00:00');

INSERT INTO users (user_id, name, height, weight, age, sex, allergies, dislikes, budget, cooking_skill_level, equipments, created_at, updated_at)
VALUES (5, 'Alex Chen', 172.00, 68.00, 30, 'OTHER', ARRAY['gluten'], ARRAY['durian'], 600.00, 'INTERMEDIATE', ARRAY['rice cooker', 'blender'], '2024-03-01 08:15:00', '2024-03-01 08:15:00');

INSERT INTO users (user_id, name, height, weight, age, sex, allergies, dislikes, budget, cooking_skill_level, equipments, created_at, updated_at)
VALUES (6, 'Sarah Connor', 168.00, 60.00, 29, 'FEMALE', ARRAY[]::text[], ARRAY['bell peppers', 'eggplant'], 450.00, 'BEGINNER', ARRAY['microwave', 'rice cooker'], '2024-03-05 16:30:00', '2024-03-05 16:30:00');


--
-- insert data into 'user_targets' table
--
INSERT INTO user_targets (target_id, user_id, calories, protein, fiber, fat, carbs, iron, calcium, vitamin_a, vitamin_c, vitamin_d, sodium, potassium, created_at, updated_at)
VALUES (1, 1, 2200.00, 110.00, 30.00, 70.00, 280.00, 15.00, 1000.00, 900.00, 90.00, 20.00, 2300.00, 3500.00, '2024-01-15 10:05:00', '2024-01-15 10:05:00');

INSERT INTO user_targets (target_id, user_id, calories, protein, fiber, fat, carbs, iron, calcium, vitamin_a, vitamin_c, vitamin_d, sodium, potassium, created_at, updated_at)
VALUES (2, 2, 1800.00, 80.00, 25.00, 60.00, 220.00, 18.00, 1200.00, 700.00, 75.00, 15.00, 2000.00, 3000.00, '2024-02-01 09:35:00', '2024-02-01 09:35:00');

INSERT INTO user_targets (target_id, user_id, calories, protein, fiber, fat, carbs, iron, calcium, vitamin_a, vitamin_c, vitamin_d, sodium, potassium, created_at, updated_at)
VALUES (3, 3, 2500.00, 130.00, 35.00, 85.00, 300.00, 16.00, 1000.00, 900.00, 90.00, 20.00, 2300.00, 3500.00, '2024-02-10 14:25:00', '2024-02-10 14:25:00');

INSERT INTO user_targets (target_id, user_id, calories, protein, fiber, fat, carbs, iron, calcium, vitamin_a, vitamin_c, vitamin_d, sodium, potassium, created_at, updated_at)
VALUES (4, 4, 1600.00, 70.00, 28.00, 50.00, 200.00, 18.00, 1200.00, 700.00, 75.00, 15.00, 1800.00, 2800.00, '2024-02-15 11:05:00', '2024-02-15 11:05:00');

INSERT INTO user_targets (target_id, user_id, calories, protein, fiber, fat, carbs, iron, calcium, vitamin_a, vitamin_c, vitamin_d, sodium, potassium, created_at, updated_at)
VALUES (5, 5, 2100.00, 105.00, 30.00, 70.00, 260.00, 15.00, 1000.00, 900.00, 90.00, 20.00, 2200.00, 3400.00, '2024-03-01 08:20:00', '2024-03-01 08:20:00');

INSERT INTO user_targets (target_id, user_id, calories, protein, fiber, fat, carbs, iron, calcium, vitamin_a, vitamin_c, vitamin_d, sodium, potassium, created_at, updated_at)
VALUES (6, 6, 1900.00, 85.00, 26.00, 63.00, 240.00, 18.00, 1200.00, 700.00, 75.00, 15.00, 2000.00, 3200.00, '2024-03-05 16:35:00', '2024-03-05 16:35:00');


--
-- insert data into 'user_health_history' table
--
INSERT INTO user_health_history (history_id, user_id, weight, height, recorded_at)
VALUES (1, 1, 72.00, 175.00, '2024-01-01 08:00:00');

INSERT INTO user_health_history (history_id, user_id, weight, height, recorded_at)
VALUES (2, 1, 71.50, 175.00, '2024-02-01 08:00:00');

INSERT INTO user_health_history (history_id, user_id, weight, height, recorded_at)
VALUES (3, 1, 70.50, 175.00, '2024-03-01 08:00:00');

INSERT INTO user_health_history (history_id, user_id, weight, height, recorded_at)
VALUES (4, 2, 57.00, 165.00, '2024-01-15 09:00:00');

INSERT INTO user_health_history (history_id, user_id, weight, height, recorded_at)
VALUES (5, 2, 56.00, 165.00, '2024-02-15 09:00:00');

INSERT INTO user_health_history (history_id, user_id, weight, height, recorded_at)
VALUES (6, 2, 55.00, 165.00, '2024-03-15 09:00:00');

INSERT INTO user_health_history (history_id, user_id, weight, height, recorded_at)
VALUES (7, 3, 88.00, 180.00, '2024-02-10 10:00:00');

INSERT INTO user_health_history (history_id, user_id, weight, height, recorded_at)
VALUES (8, 3, 86.50, 180.00, '2024-03-10 10:00:00');

INSERT INTO user_health_history (history_id, user_id, weight, height, recorded_at)
VALUES (9, 4, 54.00, 160.00, '2024-02-15 11:00:00');

INSERT INTO user_health_history (history_id, user_id, weight, height, recorded_at)
VALUES (10, 4, 52.00, 160.00, '2024-03-15 11:00:00');

INSERT INTO user_health_history (history_id, user_id, weight, height, recorded_at)
VALUES (11, 5, 70.00, 172.00, '2024-03-01 08:00:00');

INSERT INTO user_health_history (history_id, user_id, weight, height, recorded_at)
VALUES (12, 5, 68.00, 172.00, '2024-04-01 08:00:00');
