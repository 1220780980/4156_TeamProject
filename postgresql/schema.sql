CREATE SCHEMA IF NOT EXISTS nutriflow;
SET search_path TO nutriflow;

-- gender type
CREATE TYPE sex_type AS ENUM ('MALE', 'FEMALE', 'OTHER');

-- cooking skill level type
CREATE TYPE cooking_skill_level AS ENUM ('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT');

--
-- create table 'users'
--
CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    height DECIMAL(5,2),
    weight DECIMAL(5,2),
    age INTEGER,
    sex sex_type,
    allergies TEXT[],
    dislikes TEXT[],
    budget DECIMAL(10,2),
    cooking_skill_level cooking_skill_level DEFAULT 'BEGINNER',
    equipments TEXT[],
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_height CHECK (height IS NULL OR (height > 0 AND height <= 300)),
    CONSTRAINT check_age CHECK (age IS NULL OR (age > 0 AND age <= 150)),
    CONSTRAINT check_budget CHECK (budget IS NULL OR budget >= 0)
);

--
-- create table 'user_targets'
--
CREATE TABLE IF NOT EXISTS user_targets (
    target_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    calories DECIMAL(7,2),
    protein DECIMAL(6,2),
    fiber DECIMAL(6,2),
    fat DECIMAL(6,2),
    carbs DECIMAL(6,2),
    iron DECIMAL(6,2),
    calcium DECIMAL(6,2),
    vitamin_a DECIMAL(6,2),
    vitamin_c DECIMAL(6,2),
    vitamin_d DECIMAL(6,2),
    sodium DECIMAL(6,2),
    potassium DECIMAL(6,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT user_targets_fk 
        FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT check_calories CHECK (calories IS NULL OR calories >= 0),
    CONSTRAINT check_protein CHECK (protein IS NULL OR protein >= 0),
    CONSTRAINT check_fiber CHECK (fiber IS NULL OR fiber >= 0),
    CONSTRAINT check_fat CHECK (fat IS NULL OR fat >= 0),
    CONSTRAINT check_carbs CHECK (carbs IS NULL OR carbs >= 0),
    CONSTRAINT check_iron CHECK (iron IS NULL OR iron >= 0),
    CONSTRAINT check_calcium CHECK (calcium IS NULL OR calcium >= 0),
    CONSTRAINT check_vitamin_a CHECK (vitamin_a IS NULL OR vitamin_a >= 0),
    CONSTRAINT check_vitamin_c CHECK (vitamin_c IS NULL OR vitamin_c >= 0),
    CONSTRAINT check_vitamin_d CHECK (vitamin_d IS NULL OR vitamin_d >= 0),
    CONSTRAINT check_sodium CHECK (sodium IS NULL OR sodium >= 0),
    CONSTRAINT check_potassium CHECK (potassium IS NULL OR potassium >= 0)
);

--
-- create table 'user_health_history'
--
CREATE TABLE IF NOT EXISTS user_health_history (
    history_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    weight DECIMAL(5,2) NOT NULL,
    height DECIMAL(5,2) NOT NULL,
    bmi DECIMAL(5,2) GENERATED ALWAYS AS (weight / ((height / 100) * (height / 100))) STORED,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT user_health_history_fk 
        FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT check_history_height CHECK (height > 0 AND height <= 300)
);

--
-- create trigger function 'update_updated_at_column'
--
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

--
-- create trigger 'update_users_updated_at'
--
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

--
-- create trigger 'update_user_targets_updated_at'
--
CREATE TRIGGER update_user_targets_updated_at
    BEFORE UPDATE ON user_targets
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

--
-- create table 'pantry_items'
--
CREATE TABLE IF NOT EXISTS pantry_items (
    item_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    quantity DECIMAL(10,2) DEFAULT 0,
    unit VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pantry_user_fk FOREIGN KEY (user_id)
        REFERENCES nutriflow.users(user_id) ON DELETE CASCADE
);

--
-- create table 'recipes'
--
CREATE TABLE IF NOT EXISTS recipes (
    recipe_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    cook_time INTEGER,
    cuisines TEXT[],
    tags TEXT[],
    ingredients JSONB,
    nutrition JSONB,
    calories DECIMAL(7,2),
    carbohydrates DECIMAL(7,2),
    fat DECIMAL(7,2),
    fiber DECIMAL(7,2),
    protein DECIMAL(7,2),
    popularity_score INTEGER DEFAULT 0
);

--
-- create table 'favorite_recipes'
--
CREATE TABLE IF NOT EXISTS favorite_recipes (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    recipe_id INTEGER NOT NULL,
    times_used INTEGER DEFAULT 0,
    is_favorite BOOLEAN DEFAULT FALSE,
    CONSTRAINT favorite_recipe_user_fk FOREIGN KEY (user_id)
        REFERENCES nutriflow.users(user_id) ON DELETE CASCADE,
    CONSTRAINT favorite_recipe_fk FOREIGN KEY (recipe_id)
        REFERENCES nutriflow.recipes(recipe_id) ON DELETE CASCADE
);
