INSERT INTO recipes
(id, name, type, requires_appliance, avg_cost, protein, calories)
VALUES

-- 🟢 NO COOK (HOSTEL FRIENDLY)
(gen_random_uuid(), 'Banana + Peanuts', 'NO_COOK', false, 20, 6, 180),
(gen_random_uuid(), 'Boiled Eggs (2)', 'NO_COOK', false, 25, 12, 160),
(gen_random_uuid(), 'Milk + Banana', 'NO_COOK', false, 20, 8, 200),
(gen_random_uuid(), 'Sprouts Salad', 'NO_COOK', false, 25, 10, 150),

-- 🟡 HOME COOKED (SIMPLE)
(gen_random_uuid(), 'Poha', 'HOME_COOKED', true, 30, 6, 250),
(gen_random_uuid(), 'Upma', 'HOME_COOKED', true, 30, 7, 270),
(gen_random_uuid(), 'Rice + Dal', 'HOME_COOKED', true, 35, 12, 350),
(gen_random_uuid(), 'Omelette (2 eggs)', 'HOME_COOKED', true, 30, 14, 220),

-- 🔵 OUTSIDE FOOD (BUDGET)
(gen_random_uuid(), 'Idli (2)', 'OUTSIDE_FOOD', false, 35, 6, 220),
(gen_random_uuid(), 'Dosa', 'OUTSIDE_FOOD', false, 60, 7, 300),
(gen_random_uuid(), 'Veg Thali', 'OUTSIDE_FOOD', false, 80, 14, 450),
(gen_random_uuid(), 'Chicken Rice', 'OUTSIDE_FOOD', false, 80, 22, 520);