-- =========================
-- USER PROFILE EXTENSIONS
-- =========================
ALTER TABLE users
ADD COLUMN age INTEGER,
ADD COLUMN height_cm DOUBLE PRECISION,
ADD COLUMN weight_kg DOUBLE PRECISION,
ADD COLUMN meals_per_day INTEGER,
ADD COLUMN monthly_budget INTEGER,
ADD COLUMN has_cooking_appliance BOOLEAN DEFAULT TRUE;

-- =========================
-- RECIPES TABLE (NEW)
-- =========================
CREATE TABLE recipes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    name VARCHAR(150) NOT NULL,

    type VARCHAR(30) NOT NULL,
    -- HOME_COOKED, NO_COOK, OUTSIDE_FOOD

    requires_appliance BOOLEAN DEFAULT FALSE,

    avg_cost INTEGER,
    protein REAL,
    calories REAL
);

ALTER TABLE recipes
ADD CONSTRAINT chk_recipe_type
CHECK (type IN ('HOME_COOKED', 'NO_COOK', 'OUTSIDE_FOOD'));