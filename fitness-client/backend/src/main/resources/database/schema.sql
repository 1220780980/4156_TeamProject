SET search_path TO fitness_client;

---
-- create table 'app_user'
--
CREATE TABLE IF NOT EXISTS app_user (
    id                BIGSERIAL PRIMARY KEY,
    email             VARCHAR(255) UNIQUE NOT NULL,
    password_hash     VARCHAR(255) NOT NULL,
    age               INT,
    height            NUMERIC(5,2),
    weight            NUMERIC(5,2),
    sex               VARCHAR(10),
    activity_level    VARCHAR(20),
    fitness_goal      VARCHAR(20),
    nutriflow_user_id BIGINT,
    created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT app_user_age_check CHECK (age IS NULL OR (age >= 0 AND age <= 150)),
    CONSTRAINT app_user_height_check CHECK (height IS NULL OR height >= 0),
    CONSTRAINT app_user_weight_check CHECK (weight IS NULL OR weight >= 0),
    CONSTRAINT app_user_users_fk FOREIGN KEY (nutriflow_user_id)
        REFERENCES nutriflow.users(user_id) ON DELETE SET NULL
);

---
-- create trigger 'app_user_updated_at'
--
CREATE TRIGGER app_user_updated_at
    BEFORE UPDATE ON app_user
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

