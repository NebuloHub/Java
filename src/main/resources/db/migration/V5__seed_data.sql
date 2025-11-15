-- V5__seed_data.sql
-- This script populates the database with initial seed data.
-- It uses hardcoded IDs for users, posts, ratings, and comments to
-- reliably establish foreign key relationships.

-- 1. INSERT USERS (7 total)
-- (ADMIN and TEST users are preserved with their specific passwords)
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
INSERT ALL
    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (1, 'EcoDrive - AI for optimizing truck routes',
    'The logistics industry is the backbone of our economy, but it''s also one of the largest contributors to carbon emissions. Fuel costs represent over 30% of total operational expenses for many freight companies. Our platform, EcoDrive, addresses this head-on.

We are developing a SaaS platform that uses a multi-layered AI model to plan the most fuel-efficient routes for logistics and shipping companies. This isn''t just about finding the shortest path; our algorithm analyzes real-time traffic, weather patterns, road elevation grades, and even vehicle-specific data (like load weight and engine type) to minimize fuel consumption.

Our initial projections, based on beta testing with a local partner, show a potential reduction in fuel costs by 15-20% and a significant decrease in CO2 emissions.

The core technology combines a predictive model for traffic flow with a dynamic routing engine. We plan to monetize via a per-truck, per-month subscription model, offering a clear ROI to our clients from the first month of use. We are seeking seed funding to scale our data ingestion pipeline and onboard our first 10 enterprise clients.',
    3, SYSTIMESTAMP - INTERVAL '1' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (2, 'NutriAI - Personalized meal plans',
    'Everyone wants to eat healthier, but the friction of meal planning is immense. What to buy? What to cook? How to use ingredients before they spoil? NutriAI is a mobile-first solution designed to eliminate this friction entirely.

The user starts by setting their dietary goals (e.g., lose weight, build muscle, low-carb) and listing preferences or allergies. Then, the magic happens: the user takes a photo of their fridge and pantry. Our computer vision AI identifies all available ingredients, quantities, and even estimates expiration dates.

Based on this real-time inventory and the user''s goals, NutriAI generates a complete weekly meal plan, complete with recipes. It prioritizes using ingredients that are about to spoil, dramatically reducing food waste.

It also generates a "smart" grocery list containing only the items needed to complete the week''s recipes. We plan to monetize through a freemium model: free users get 3 plans per month, while premium subscribers get unlimited plans, advanced dietary tracking, and grocery list integration with online supermarkets (generating affiliate revenue).',
    4, SYSTIMESTAMP - INTERVAL '2' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (3, 'CodeBuddy - AI-powered pair programming assistant',
    'GitHub Copilot is great for writing *new* code, but it''s less effective at refactoring complex, existing codebases or enforcing team-specific style guides. CodeBuddy is an IDE plugin that acts less like an auto-complete and more like a senior developer or architect doing a real-time code review.

CodeBuddy integrates directly into VS Code and IntelliJ. As a developer writes code, it proactively highlights "code smells," potential bugs (like null pointer exceptions or race conditions), and deviations from the project''s established design patterns.

The key feature is "Refactor Suggestions." A developer can highlight a complex function or class and ask CodeBuddy to refactor it for readability, performance, or testability. It will then provide a "diff" view of the proposed changes, explaining *why* it''s making each change, which also serves as a powerful learning tool for junior and mid-level devs.

We are aiming for a B2B model, selling licenses to development teams. The AI can be fine-tuned on a company''s private codebase to learn their specific patterns and best practices, making its suggestions hyper-relevant.',
    5, SYSTIMESTAMP - INTERVAL '3' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (4, 'PetConnect - Tinder for pet adoption',
    'The problem with pet adoption is mismatch. People adopt a pet based on a cute photo, only to find the animal''s energy level or temperament doesn''t fit their lifestyle, leading to high return rates. PetConnect aims to solve this by focusing on compatibility first.

Our platform (web and mobile) replaces endless scrolling with a "matchmaking" process. Potential adopters fill out a detailed lifestyle questionnaire: Are you active? Do you live in an apartment? Do you have kids? Are you a first-time owner?

Animal shelters and rescue organizations create profiles for their animals, detailing their temperament, energy level, medical needs, and history. Our algorithm then provides users with a "compatibility score" for each pet, showing them their best matches first.

Users can "swipe" on profiles, and if a shelter "matches" back (confirming the adopter is a good fit), a chat is initiated to schedule a meet-and-greet. This pre-screening saves time for both the shelters and the adopters, leading to more successful, permanent adoptions.',
    6, SYSTIMESTAMP - INTERVAL '4' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (5, 'FinanZen - Mindfulness app for day traders',
    'Day trading is one of the most high-stress professions in the world. Decisions worth thousands of dollars are made in seconds, leading to anxiety, "revenge trading," and burnout. FinanZen is a specialized mindfulness and mental wellness app designed specifically for this high-stakes audience.

The app combines real-time financial news alerts with guided mindfulness exercises. For example, if the S&P 500 drops 2% suddenly, a notification can pop up: "The market is volatile. Take 60 seconds to breathe before making your next trade."

Key features include:
1.  **Guided Meditations:** 5-minute audio sessions focused on themes like "Handling a Loss," "Avoiding FOMO," and "Sticking to Your Strategy."
2.  **Biofeedback Integration:** Connects with smartwatches to track heart rate variability (HRV) during trading sessions and alerts the user when stress levels are peaking.
3.  **Smart Journal:** A simple journal for traders to log their "emotional state" before and after trades, helping them identify patterns between mood and performance.

Monetization will be a simple monthly/annual subscription. We believe the potential ROI in preventing a single emotionally-driven bad trade far outweighs the cost of the subscription.',
    7, SYSTIMESTAMP - INTERVAL '5' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (6, 'AquaPure - Smart home water filtration monitoring',
    'Home water filters (like Brita pitchers or under-sink systems) are great, but they have a critical flaw: people forget to change the filter. An old filter doesn''t just stop working; it can become a breeding ground for bacteria, making the water *worse* than tap water.

AquaPure is a small, universal IoT device that attaches to any existing water filter. It uses a combination of a flow sensor and a time-based algorithm to *actually* track filter usage, not just a simple calendar reminder.

The device connects to a mobile app via Wi-Fi and displays:
1.  **Filter Lifespan:** A precise percentage of remaining filter life based on actual water consumption.
2.  **Water Quality:** (On premium models) Basic TDS (Total Dissolved Solids) readings to show the filter''s effectiveness.
3.  **Consumption Analytics:** Tracks how much water the household is drinking.

The app provides push notifications when the filter is at 10% life and features an "auto-order" button that integrates with Amazon or the filter manufacturer''s site. We will sell the hardware as the primary product and explore partnerships with filter manufacturers to become their official "smart" solution.',
    3, SYSTIMESTAMP - INTERVAL '6' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (7, 'LingoLeap - VR language immersion',
    'Language learning apps like Duolingo are good for vocabulary, but they fail to teach real-world confidence. You can get a 500-day streak and still be terrified to actually order a coffee in Spanish. LingoLeap bridges this gap using Virtual Reality.

Instead of flashcards, users are immersed in realistic, AI-powered virtual environments. In our "Spanish" module, you''ll be in a virtual Madrid, tasked with navigating the metro, ordering tapas at a busy bar, or haggling at a market.

The virtual locals (NPCs) are powered by a generative AI. You speak to them, and they understand and respond to you. They can correct your pronunciation, teach you slang, and react dynamically. If you speak too slowly, they might look impatient. If you use the wrong word, they might look confused.

This is active, contextual learning that builds muscle memory for real conversations. Our target market is serious learners who have hit the "intermediate plateau." We will sell modules per language on platforms like the Meta Quest Store.',
    4, SYSTIMESTAMP - INTERVAL '7' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (8, 'GigFlow - Project management for freelancers',
    'As a freelancer, you''re forced to juggle 5-6 different tools: one for time-tracking, one for invoicing, one for project tasks (Trello/Asana), one for client communication (Slack/Email), and another for proposals (Word/PDFs). It''s a chaotic and expensive mess.

GigFlow is an all-in-one, opinionated tool designed specifically for solo freelancers. It integrates the entire client lifecycle into one simple interface:
1.  **Proposals:** Create, send, and get digital signatures on simple proposals.
2.  **Projects:** When a proposal is signed, it automatically converts to a project with a simple Kanban board for tasks.
3.  **Time Tracking:** Track time directly against tasks on the project board.
4.  **Invoicing:** With one click, generate an invoice based on tracked time or project milestones. Integrates with Stripe/PayPal.
5.  **Client Portal:** A simple portal where clients can view project progress, approve invoices, and see all related files.

By focusing *only* on the needs of a freelancer (not large teams), we can provide a streamlined, simple experience that other complex PM tools can''t match. Monetization will be a single, affordable monthly subscription.',
    5, SYSTIMESTAMP - INTERVAL '8' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (9, 'SeniServe - On-demand services for the elderly',
    'My parents are getting older, and while they are independent, they struggle with "small" tasks: carrying heavy groceries, setting up a new printer, or even just needing someone to help change a high lightbulb. Current senior care options are geared towards full-time medical help, which is expensive and unnecessary for this.

SeniServe is an "Uber" for non-medical senior assistance. It''s a simple, large-print mobile app (and phone service) that connects seniors with trusted, vetted local helpers for on-demand tasks.

The key to this entire idea is TRUST. Every "helper" on our platform must pass a rigorous background check, be insured, and complete training on senior sensitivity. We are not a medical service; we are a "helping hand" service.

Services would include:
-   **Around the Home:** Grocery shopping, light housekeeping, meal prep.
-   **Tech Help:** Setting up Wi-Fi, using a smartphone, troubleshooting a TV.
-   **Companionship:** Driving to an appointment, playing a game of cards, going for a walk.

Revenue will be a commission-based model, taking a percentage of each "task" fee. This is a massive, underserved market.',
    6, SYSTIMESTAMP - INTERVAL '9' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (10, 'Craftly - Etsy competitor for handmade digital art',
    'Etsy is a great platform, but it has two major problems for digital creators: (1) It''s overcrowded with physical items, making digital-only products hard to find, and (2) Its fees are high and constantly increasing.

Craftly is a new marketplace built *exclusively* for handmade digital art and assets. We are targeting the growing "creator economy."
Our categories will be focused:
-   Procreate/Photoshop Brushes
-   Website & UI Templates
-   Custom Fonts & Typefaces
-   Digital Planners & Stickers
-   Stock Photography & Mockups

By focusing on this niche, we can provide a much better discovery experience for buyers. For sellers, our value prop is simple: lower fees and a targeted audience that is *only* looking for digital goods.

We will build our initial seller base by inviting prominent digital creators from platforms like Instagram and TikTok, offering them a "Founder" status with permanently low transaction fees. Revenue is a simple 10% commission on all sales, which is significantly lower than Etsy''s complex fee structure.',
    7, SYSTIMESTAMP - INTERVAL '10' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (11, 'MediCache - Blockchain for medical records',
    'The inability to easily share medical records between different doctors and hospitals is a critical failure in our healthcare system. The current method relies on faxes, CDs, or insecure email, which is slow and prone to error.

MediCache is a platform that uses blockchain technology to create a secure, patient-controlled, and interoperable medical record system.
Here''s how it works:
1.  **Identity:** A patient is verified once and given a private key, giving them sole ownership of their record.
2.  **Data:** Hospitals and clinics, as "trusted nodes" on the network, can *append* new records (like lab results, x-rays, diagnoses) to the patient''s file. These records are encrypted and timestamped.
3.  **Access:** When a patient visits a new doctor, they can grant temporary, view-only access to their entire medical history by scanning a QR code. They can revoke this access at any time.

This system puts the patient in control of their own data. It eliminates redundant testing, provides doctors with a complete history in seconds (critical in emergencies), and is highly secure.

Our business model is to sell the platform as a service to hospital networks and insurance providers, who stand to save billions from the efficiencies gained.',
    3, SYSTIMESTAMP - INTERVAL '11' DAY, 0, 0)

    INTO posts (id_post, title, description, id_user, created_at, avg_rating, rating_count)
    VALUES (12, 'UrbanHarvest - Marketplace for local garden produce',
    'Every summer, home gardeners are overwhelmed with produce. One zucchini plant can produce 10lbs of zucchini. Most of it ends up being given away or rotting. At the same time, their neighbors are going to the supermarket to buy bland, trucked-in produce.

UrbanHarvest is a hyperlocal mobile app that connects these two groups.
For **Gardeners (Sellers):** If you have 10 extra tomatoes and 5 extra cucumbers, you post them on the app for a few dollars.
For **Neighbors (Buyers):** You open the app and see a map of what''s available *right now*, within walking or biking distance. You reserve your items, pay in the app, and go pick them up.

This is the "circular economy" for food. It''s fresher, cheaper, supports neighbors, and has zero food miles.

We would take a small transaction fee (e.g., 15%) for facilitating the payment and connection. The initial focus would be on dense suburban neighborhoods and community gardens to build initial liquidity. We are solving a problem of "hyper-local surplus" that no one else is addressing.',
    4, SYSTIMESTAMP - INTERVAL '12' DAY, 0, 0)
SELECT 1 FROM DUAL;

-- 3. INSERT RATINGS (40 total)
-- (This section is UNCHANGED from your original file)
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

-- 4. INSERT COMMENTS (NOW 30 total, was 20)
INSERT ALL
    -- Original 20 Comments
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (1, 'This is a fantastic idea! Logistics needs this disruption.', 2, 1, SYSTIMESTAMP - INTERVAL '10' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (2, 'How do you handle real-time traffic data? That seems like the biggest hurdle.', 4, 1, SYSTIMESTAMP - INTERVAL '9' HOUR)

    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (3, 'I need this! My fridge is a mess.', 3, 2, SYSTIMESTAMP - INTERVAL '20' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (4, 'Great concept, but what about privacy? Scanning my fridge feels invasive.', 5, 2, SYSTIMESTAMP - INTERVAL '19' HOUR)

    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (5, 'Shut up and take my money. GitHub Copilot is good, but this sounds next-level.', 4, 3, SYSTIMESTAMP - INTERVAL '30' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (6, 'I worry this would make junior devs lazy and dependent on the AI.', 6, 3, SYSTIMESTAMP - INTERVAL '29' HOUR)

    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (7, 'This is so wholesome! I love the personality quiz idea.', 5, 4, SYSTIMESTAMP - INTERVAL '40' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (8, 'How will you verify the shelters and rescue centers?', 7, 4, SYSTIMESTAMP - INTERVAL '39' HOUR)

    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (9, 'Interesting... "Stop-loss and chill"?', 6, 5, SYSTIMESTAMP - INTERVAL '50' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (10, 'I feel like the target audience (day traders) would be the least likely to use a mindfulness app.', 2, 5, SYSTIMESTAMP - INTERVAL '49' HOUR)

    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (11, 'This is a solid IoT play. Subscription model for the filters?', 4, 6, SYSTIMESTAMP - INTERVAL '60' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (12, 'I like it. Could expand into air filters too.', 7, 6, SYSTIMESTAMP - INTERVAL '59' HOUR)

    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (13, 'This is the future of learning. Duolingo is shaking.', 3, 7, SYSTIMESTAMP - INTERVAL '70' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (14, 'Hardware cost is a big barrier. Have you considered a non-VR mobile version first?', 5, 7, SYSTIMESTAMP - INTERVAL '69' HOUR)

    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (15, 'Yes! Please! The freelancer stack is a nightmare. Make it simple.', 2, 8, SYSTIMESTAMP - INTERVAL '80' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (16, 'How will this be better than the 100 other tools that claim to do this? (e.g., Notion, Trello, etc.)', 6, 8, SYSTIMESTAMP - INTERVAL '79' HOUR)

    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (17, 'My parents need this. The trust and safety aspect is everything here. How do you vet helpers?', 3, 9, SYSTIMESTAMP - INTERVAL '90' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (18, 'This is a huge market. Liability insurance is going to be your biggest expense.', 7, 9, SYSTIMESTAMP - INTERVAL '89' HOUR)

    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (19, 'Etsy''s fees are killing creators. If you can beat them on price, you''ll win.', 4, 10, SYSTIMESTAMP - INTERVAL '100' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (20, 'This is a very, very saturated market. What is your unique value prop?', 5, 10, SYSTIMESTAMP - INTERVAL '99' HOUR)

    -- New 10 Comments (IDs 21-30)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (21, 'Blockchain for medical records sounds innovative, but patient data privacy and HIPAA compliance will be massive challenges. How do you plan to handle that?', 4, 11, SYSTIMESTAMP - INTERVAL '10' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (22, 'I signed up for UrbanHarvest. I have way too many tomatoes! Happy to see this.', 7, 12, SYSTIMESTAMP - INTERVAL '11' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (23, 'Re: CodeBuddy. What''s the business model? Per-seat subscription? Or a one-time purchase?', 2, 3, SYSTIMESTAMP - INTERVAL '28' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (24, 'PetConnect is a fantastic name. The compatibility quiz is the key - if you get that right, this could genuinely reduce return rates.', 3, 4, SYSTIMESTAMP - INTERVAL '38' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (25, 'This is a great idea. I would definitely use UrbanHarvest. I''m tired of flavorless supermarket produce.', 6, 12, SYSTIMESTAMP - INTERVAL '10' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (26, 'SeniServe is desperately needed. My grandmother could use this. Vetting the helpers will be your entire business model, focus on that.', 5, 9, SYSTIMESTAMP - INTERVAL '88' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (27, 'The real-time data for EcoDrive is the hard part. Are you partnering with existing telematics providers or building your own hardware?', 7, 1, SYSTIMESTAMP - INTERVAL '8' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (28, 'Love the freelancer tool (GigFlow). If you can genuinely replace 3-4 other subscriptions for one low price, I''m in.', 7, 8, SYSTIMESTAMP - INTERVAL '78' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (29, 'I''ve seen 10 "blockchain for X" ideas. Most are solutions looking for a problem. Is MediCache truly better than a centralized, encrypted database?', 5, 11, SYSTIMESTAMP - INTERVAL '9' HOUR)
    INTO comments (id_comment, content, id_user, id_post, created_at)
    VALUES (30, 'The AI in NutriAI identifying food from a photo sounds like "Not Hotdog". How accurate is this really?', 6, 2, SYSTIMESTAMP - INTERVAL '18' HOUR)
SELECT 1 FROM DUAL;

-- 5. UPDATE POSTS with correct avg_rating and rating_count
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