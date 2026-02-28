-- Enable UUID support (Postgres)
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ======================
-- USERS
-- ======================
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100),
    email VARCHAR(150) UNIQUE,
    goal VARCHAR(20) NOT NULL DEFAULT 'MAINTAIN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE users
ADD CONSTRAINT chk_user_goal
CHECK (goal IN ('WEIGHT_GAIN', 'MAINTAIN', 'WEIGHT_LOSS'));

-- ======================
-- INGREDIENTS
-- ======================
CREATE TABLE ingredients (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) UNIQUE NOT NULL,

    cost_per_100g REAL NOT NULL,

    protein REAL,
    carbs REAL,
    fats REAL,
    sugar REAL,
    calories REAL,

    category VARCHAR(50),

    is_high_sugar BOOLEAN DEFAULT FALSE,
    is_high_fat BOOLEAN DEFAULT FALSE
);

-- ======================
-- MEAL PLANS (MONTHLY)
-- ======================
CREATE TABLE meal_plans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id),

    month INTEGER NOT NULL,
    year INTEGER NOT NULL,

    budget INTEGER NOT NULL,
    meals_per_day INTEGER NOT NULL,
    diet_type VARCHAR(20),
    priority VARCHAR(30),

    total_cost INTEGER,
    avg_daily_protein REAL,

    version INTEGER DEFAULT 1,
    status VARCHAR(20) DEFAULT 'DRAFT',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ======================
-- PLAN DAYS
-- ======================
CREATE TABLE plan_days (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    meal_plan_id UUID REFERENCES meal_plans(id) ON DELETE CASCADE,

    day_number INTEGER NOT NULL,

    total_cost INTEGER,
    total_protein REAL,
    total_calories REAL,

    UNIQUE (meal_plan_id, day_number)
);

-- ======================
-- MEALS
-- ======================
CREATE TABLE meals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    plan_day_id UUID REFERENCES plan_days(id) ON DELETE CASCADE,

    meal_type VARCHAR(20), -- BREAKFAST / LUNCH / DINNER

    meal_cost INTEGER,
    protein REAL,
    calories REAL
);

-- ======================
-- MEAL ITEMS
-- ======================
CREATE TABLE meal_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    meal_id UUID REFERENCES meals(id) ON DELETE CASCADE,
    ingredient_id UUID REFERENCES ingredients(id),

    quantity_grams REAL NOT NULL,

    cost REAL,
    protein REAL,
    calories REAL
);