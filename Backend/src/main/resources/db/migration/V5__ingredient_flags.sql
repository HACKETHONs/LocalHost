UPDATE ingredients
SET is_high_sugar = TRUE
WHERE name IN ('Sugar');

UPDATE ingredients
SET is_high_fat = TRUE
WHERE name IN ('Ghee');