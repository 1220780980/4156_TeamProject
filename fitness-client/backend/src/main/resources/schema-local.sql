CREATE SCHEMA IF NOT EXISTS fitness_client;

CREATE TABLE fitness_client.app_user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    age INT,
    height DECIMAL(5,2),
    weight DECIMAL(5,2),
    sex VARCHAR(50),
    activity_level VARCHAR(50),
    fitness_goal VARCHAR(255),
    nutriflow_user_id BIGINT,
    PRIMARY KEY (id)
);
