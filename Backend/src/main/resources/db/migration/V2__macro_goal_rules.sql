CREATE TABLE macro_goal_rules (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    goal VARCHAR(20) NOT NULL,

    protein_weight REAL NOT NULL,
    carbs_weight REAL NOT NULL,
    fat_weight REAL NOT NULL,
    sugar_weight REAL NOT NULL,

    min_protein_per_day REAL,
    max_calories_per_day REAL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE (goal)
);

ALTER TABLE macro_goal_rules
ADD CONSTRAINT chk_macro_goal
CHECK (goal IN ('WEIGHT_GAIN', 'MAINTAIN', 'WEIGHT_LOSS'));