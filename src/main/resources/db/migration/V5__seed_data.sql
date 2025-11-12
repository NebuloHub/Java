-- V5__seed_data.sql
-- This script populates the database with initial seed data.
-- It uses hardcoded IDs for users, posts, ratings, and comments to
-- reliably establish foreign key relationships.

-- 1. INSERT USERS (7 total)
INSERT ALL
    -- Admin User
    INTO users (id_user, username, email, password, role, created_at)
    VALUES (1, 'ADMIN', 'admin@gmail.com', '$2a$10$JV7Qhj9GUg3VJWCdT/xYCuc934oQCXWMXOMd7XS3Nn6XJOQxO7f16', 'ROLE_ADMIN', SYSTIMESTAMP)

    -- Test User
    INTO users (id_user, username, email, password, role, created_at)
    VALUES (2, 'TEST', 'teste@gmail.com', '$2a$10$e2.nSyMwaFga1vDIn5WeRuZCG15lQt6XfTiFQdV0GL22j./oXqrZW', 'ROLE_USER', SYSTIMESTAMP)

    -- Other 5 Users
    INTO users (id_user, username, email, password, role, created_at)
    VALUES (3, 'carlos.silva', 'carlos@gmail.com', '$2a$10$e2.nSyMwaFga1vDIn5WeRuZCG15lQt6XfTiFQdV0GL22j./oXqrZW', 'ROLE_USER', SYSTIMESTAMP - INTERVAL '1' DAY)

    INTO users (id_user, username, email, password, role, created_at)
    VALUES (4, 'ana.beatriz', 'ana@gmail.com', '$2a$10$e2.nSyMwaFga1vDIn5WeRuZCG15lQt6XfTiFQdV0GL22j./oXqrZW', 'ROLE_USER', SYSTIMESTAMP - INTERVAL '2' DAY)

    INTO users (id_user, username, email, password, role, created_at)
    VALUES (5, 'renato.gomes', 'renato@gmail.com', '$2a$10$e2.nSyMwaFga1vDIn5WeRuZCG15lQt6XfTiFQdV0GL22j./oXqrZW', 'ROLE_USER', SYSTIMESTAMP - INTERVAL '3' DAY)

    INTO users (id_user, username, email, password, role, created_at)
    VALUES (6, 'julia.lima', 'julia@gmail.com', '$2a$10$e2.nSyMwaFga1vDIn5WeRuZCG15lQt6XfTiFQdV0GL22j./oXqrZW', 'ROLE_USER', SYSTIMESTAMP - INTERVAL '4' DAY)

    INTO users (id_user, username, email, password, role, created_at)
    VALUES (7, 'm.fernandes', 'marta@gmail.com', '$2a$10$e2.nSyMwaFga1vDIn5WeRuZCG15lQt6XfTiFQdV0GL22j./oXqrZW', 'ROLE_USER', SYSTIMESTAMP - INTERVAL '5' DAY)
SELECT 1 FROM DUAL;

-- 2. INSERT POSTS (12 total)
-- (avg_rating and rating_count are set to 0 initially, updated later)
INSERT ALL
    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (1, 'EcoDrive - AI for optimizing truck routes', 'A platform using machine learning to find the most fuel-efficient routes for logistics companies. Reduces fuel cost by 15%.', 3, SYSTIMESTAMP - INTERVAL '1' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (2, 'NutriAI - Personalized meal plans', 'Mobile app that scans your fridge and generates weekly meal plans based on your dietary goals and available ingredients.', 4, SYSTIMESTAMP - INTERVAL '2' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (3, 'CodeBuddy - AI-powered pair programming assistant', 'An IDE plugin that acts like a senior developer, reviewing your code in real-time and suggesting refactors and bug fixes.', 5, SYSTIMESTAMP - INTERVAL '3' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (4, 'PetConnect - Tinder for pet adoption', 'Connecting rescue animals with potential owners based on a personality compatibility quiz. Swipe right for your new best friend.', 6, SYSTIMESTAMP - INTERVAL '4' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (5, 'FinanZen - Mindfulness app for day traders', 'Combines financial news alerts with mindfulness exercises to help high-stress traders make calmer, more rational decisions.', 7, SYSTIMESTAMP - INTERVAL '5' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (6, 'AquaPure - Smart home water filtration monitoring', 'A device that attaches to your home water filter and tracks filter life, water quality, and consumption, ordering replacements automatically.', 3, SYSTIMESTAMP - INTERVAL '6' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (7, 'LingoLeap - VR language immersion', 'Learn Spanish by "living" in a virtual Madrid. Practice ordering coffee or navigating the metro with AI-powered virtual locals.', 4, SYSTIMESTAMP - INTERVAL '7' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (8, 'GigFlow - Project management for freelancers', 'A simple tool that combines invoicing, time-tracking, and project boards. Stop juggling 5 different apps.', 5, SYSTIMESTAMP - INTERVAL '8' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (9, 'SeniServe - On-demand services for the elderly', 'An "Uber" for senior assistance. Connects verified local helpers for tasks like grocery shopping, tech support, or companionship.', 6, SYSTIMESTAMP - INTERVAL '9' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (10, 'Craftly - Etsy competitor for handmade digital art', 'A marketplace focused exclusively on digital downloads like custom fonts, Procreate brushes, and website templates.', 7, SYSTIMESTAMP - INTERVAL '10' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (11, 'MediCache - Blockchain for medical records', 'A secure, patient-controlled platform for sharing medical history between different hospitals and doctors. No more faxing records.', 3, SYSTIMESTAMP - INTERVAL '11' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (12, 'UrbanHarvest - Marketplace for local garden produce', 'Connects home gardeners who have surplus (e.g., too many tomatoes) with neighbors who want to buy fresh, local produce.', 4, SYSTIMESTAMP - INTERVAL '12' DAY, 0, 0)
SELECT 1 FROM DUAL;

-- 3. INSERT RATINGS (40 total)
-- Note: Users cannot rate their own posts.
INSERT ALL
    -- Ratings for Post 1 (by User 3)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (1, 8, 2, 1)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (2, 9, 4, 1)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (3, 7, 5, 1)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (4, 10, 6, 1)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (5, 8, 7, 1) -- 5 ratings, sum=42, avg=8.4

    -- Ratings for Post 2 (by User 4)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (6, 7, 2, 2)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (7, 9, 3, 2)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (8, 9, 5, 2)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (9, 8, 6, 2)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (10, 10, 7, 2) -- 5 ratings, sum=43, avg=8.6

    -- Ratings for Post 3 (by User 5)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (11, 10, 2, 3)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (12, 10, 3, 3)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (13, 9, 4, 3)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (14, 8, 6, 3)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (15, 7, 7, 3) -- 5 ratings, sum=44, avg=8.8

    -- Ratings for Post 4 (by User 6)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (16, 9, 2, 4)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (17, 8, 3, 4)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (18, 9, 4, 4)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (19, 10, 5, 4)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (20, 9, 7, 4) -- 5 ratings, sum=45, avg=9.0

    -- Ratings for Post 5 (by User 7)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (21, 5, 2, 5)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (22, 6, 3, 5)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (23, 7, 4, 5)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (24, 5, 5, 5)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (25, 6, 6, 5) -- 5 ratings, sum=29, avg=5.8

    -- Ratings for Post 6 (by User 3)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (26, 7, 2, 6)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (27, 8, 4, 6)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (28, 7, 5, 6) -- 3 ratings, sum=22, avg=7.33

    -- Ratings for Post 7 (by User 4)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (29, 9, 2, 7)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (30, 9, 3, 7)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (31, 10, 5, 7) -- 3 ratings, sum=28, avg=9.33

    -- Ratings for Post 8 (by User 5)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (32, 8, 2, 8)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (33, 7, 3, 8)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (34, 8, 4, 8) -- 3 ratings, sum=23, avg=7.66

    -- Ratings for Post 9 (by User 6)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (35, 10, 2, 9)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (36, 9, 3, 9)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (37, 8, 7, 9) -- 3 ratings, sum=27, avg=9.0

    -- Ratings for Post 10 (by User 7)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (38, 6, 2, 10)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (39, 7, 4, 10) -- 2 ratings, sum=13, avg=6.5

    -- Ratings for Post 12 (by User 4)
    INTO ratings (id_rating, rating_value, id_user, id_post) VALUES (40, 7, 2, 12) -- 1 rating, sum=7, avg=7.0
SELECT 1 FROM DUAL;

-- 4. INSERT COMMENTS (20 total)
INSERT ALL
    -- Post 1
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (1, 'This is a fantastic idea! Logistics needs this disruption.', 2, 1, SYSTIMESTAMP - INTERVAL '10' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (2, 'How do you handle real-time traffic data? That seems like the biggest hurdle.', 4, 1, SYSTIMESTAMP - INTERVAL '9' HOUR)

    -- Post 2
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (3, 'I need this! My fridge is a mess.', 3, 2, SYSTIMESTAMP - INTERVAL '20' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (4, 'Great concept, but what about privacy? Scanning my fridge feels invasive.', 5, 2, SYSTIMESTAMP - INTERVAL '19' HOUR)

    -- Post 3
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (5, 'Shut up and take my money. GitHub Copilot is good, but this sounds next-level.', 4, 3, SYSTIMESTAMP - INTERVAL '30' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (6, 'I worry this would make junior devs lazy and dependent on the AI.', 6, 3, SYSTIMESTAMP - INTERVAL '29' HOUR)

    -- Post 4
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (7, 'This is so wholesome! I love the personality quiz idea.', 5, 4, SYSTIMESTAMP - INTERVAL '40' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (8, 'How will you verify the shelters and rescue centers?', 7, 4, SYSTIMESTAMP - INTERVAL '39' HOUR)

    -- Post 5
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (9, 'Interesting... "Stop-loss and chill"?', 6, 5, SYSTIMESTAMP - INTERVAL '50' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (10, 'I feel like the target audience (day traders) would be the least likely to use a mindfulness app.', 2, 5, SYSTIMESTAMP - INTERVAL '49' HOUR)

    -- Post 6
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (11, 'This is a solid IoT play. Subscription model for the filters?', 4, 6, SYSTIMESTAMP - INTERVAL '60' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (12, 'I like it. Could expand into air filters too.', 7, 6, SYSTIMESTAMP - INTERVAL '59' HOUR)

    -- Post 7
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (13, 'This is the future of learning. Duolingo is shaking.', 3, 7, SYSTIMESTAMP - INTERVAL '70' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (14, 'Hardware cost is a big barrier. Have you considered a non-VR mobile version first?', 5, 7, SYSTIMESTAMP - INTERVAL '69' HOUR)

    -- Post 8
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (15, 'Yes! Please! The freelancer stack is a nightmare. Make it simple.', 2, 8, SYSTIMESTAMP - INTERVAL '80' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (16, 'How will this be better than the 100 other tools that claim to do this? (e.g., Notion, Trello, etc.)', 6, 8, SYSTIMESTAMP - INTERVAL '79' HOUR)

    -- Post 9
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (17, 'My parents need this. The trust and safety aspect is everything here. How do you vet helpers?', 3, 9, SYSTIMESTAMP - INTERVAL '90' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (18, 'This is a huge market. Liability insurance is going to be your biggest expense.', 7, 9, SYSTIMESTAMP - INTERVAL '89' HOUR)

    -- Post 10
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (19, 'Etsy''s fees are killing creators. If you can beat them on price, you''ll win.', 4, 10, SYSTIMESTAMP - INTERVAL '100' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (20, 'This is a very, very saturated market. What is your unique value prop?', 5, 10, SYSTIMESTAMP - INTERVAL '99' HOUR)
SELECT 1 FROM DUAL;

-- 5. UPDATE POSTS with correct avg_rating and rating_count
-- (Calculated from the ratings above)
UPDATE posts SET avg_rating = 8.4, rating_count = 5 WHERE id_post = 1;
UPDATE posts SET avg_rating = 8.6, rating_count = 5 WHERE id_post = 2;
UPDATE posts SET avg_rating = 8.8, rating_count = 5 WHERE id_post = 3;
UPDATE posts SET avg_rating = 9.0, rating_count = 5 WHERE id_post = 4;
UPDATE posts SET avg_rating = 5.8, rating_count = 5 WHERE id_post = 5;
UPDATE posts SET avg_rating = 7.3, rating_count = 3 WHERE id_post = 6; -- 22/3 = 7.33
UPDATE posts SET avg_rating = 9.3, rating_count = 3 WHERE id_post = 7; -- 28/3 = 9.33
UPDATE posts SET avg_rating = 7.7, rating_count = 3 WHERE id_post = 8; -- 23/3 = 7.66
UPDATE posts SET avg_rating = 9.0, rating_count = 3 WHERE id_post = 9; -- 27/3 = 9.0
UPDATE posts SET avg_rating = 6.5, rating_count = 2 WHERE id_post = 10; -- 13/2 = 6.5
UPDATE posts SET avg_rating = 0,   rating_count = 0 WHERE id_post = 11; -- No ratings
UPDATE posts SET avg_rating = 7.0, rating_count = 1 WHERE id_post = 12; -- 7/1 = 7.0

-- Commit all changes
COMMIT;