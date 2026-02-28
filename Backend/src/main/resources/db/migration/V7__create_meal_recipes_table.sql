CREATE TABLE meal_recipes (
    meal_id UUID NOT NULL,
    recipe_id UUID NOT NULL,

    CONSTRAINT pk_meal_recipes PRIMARY KEY (meal_id, recipe_id),

    CONSTRAINT fk_meal_recipes_meal
        FOREIGN KEY (meal_id)
        REFERENCES meals(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_meal_recipes_recipe
        FOREIGN KEY (recipe_id)
        REFERENCES recipes(id)
        ON DELETE CASCADE
);