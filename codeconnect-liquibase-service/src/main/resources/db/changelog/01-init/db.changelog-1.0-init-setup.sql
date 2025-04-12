--liquibase formatted sql

-- changeset mingwei:init-setup-codeconnect-database
CREATE TABLE IF NOT EXISTS app_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('EMPLOYEE', 'EMPLOYER', 'ADMIN')),
    email VARCHAR(255) UNIQUE NOT NULL,
    status VARCHAR(50) DEFAULT 'INACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'PENDING_REVIEW')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS employee_profile (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(255),
    job_title VARCHAR(255),
    current_company VARCHAR(255),
    location VARCHAR(255),
    phone VARCHAR(255),
    about_me TEXT,
    programming_languages VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    app_user_id INT UNIQUE NOT NULL REFERENCES app_user(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_employee_app_user FOREIGN KEY (app_user_id) REFERENCES app_user(id)
);

CREATE TABLE IF NOT EXISTS employer_profile (
    id SERIAL PRIMARY KEY,
    company_name VARCHAR(255),
    company_size VARCHAR(255),
    industry VARCHAR(255),
    app_user_id BIGINT UNIQUE,
    CONSTRAINT fk_employer_app_user FOREIGN KEY (app_user_id) REFERENCES app_user(id)
);