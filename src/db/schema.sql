-- USERS TABLE
CREATE TABLE IF NOT EXISTS USERS (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- EXPENSE CATEGORIES TABLE
CREATE TABLE IF NOT EXISTS EXPENSE_CATEGORIES (
    category_id SERIAL PRIMARY KEY,
    user_id INT,  -- 🔥 IMPORTANT: categories should belong to a user
    category_name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT category_user_fk
        FOREIGN KEY (user_id)
        REFERENCES USERS(user_id)
        ON DELETE CASCADE
);


-- BUDGETS TABLE
CREATE TABLE IF NOT EXISTS BUDGETS (
    budget_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    month INT NOT NULL,   -- 🔥 FIX: use INT instead of VARCHAR
    year INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT budget_userid_fk
        FOREIGN KEY (user_id)
        REFERENCES USERS(user_id)
        ON DELETE CASCADE
);


-- EXPENSES TABLE
CREATE TABLE IF NOT EXISTS EXPENSES (
    expense_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    category_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    expense_date DATE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT expense_userid_fk
        FOREIGN KEY (user_id)
        REFERENCES USERS(user_id)
        ON DELETE CASCADE,

    CONSTRAINT expense_categoryid_fk
        FOREIGN KEY (category_id)
        REFERENCES EXPENSE_CATEGORIES(category_id)
        ON DELETE CASCADE
);


-- BUDGET VERSIONS TABLE
CREATE TABLE IF NOT EXISTS BUDGET_VERSIONS (
    version_id SERIAL PRIMARY KEY,
    budget_id INT NOT NULL,
    category_id INT NOT NULL,
    planned_amount DECIMAL(10,2) NOT NULL,
    version_number INT NOT NULL,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT budget_versions_budgetid_fk
        FOREIGN KEY (budget_id)
        REFERENCES BUDGETS(budget_id)
        ON DELETE CASCADE,

    CONSTRAINT budget_versions_categoryid_fk
        FOREIGN KEY (category_id)
        REFERENCES EXPENSE_CATEGORIES(category_id)
        ON DELETE CASCADE
);