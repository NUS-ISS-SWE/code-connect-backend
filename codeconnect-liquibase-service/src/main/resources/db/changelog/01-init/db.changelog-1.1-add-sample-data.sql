--liquibase formatted sql

-- changeset mingwei:add-sample-data

INSERT INTO app_user (username, password, email, status, role)
VALUES
-- Admin
('admin1', '$2a$10$51p/5rgfYcs9y6RW..7kUuFt58/7Lcjc5/GoBZVjVajpglzmDBYgG', 'admin@example.com', 'ACTIVE', 'ADMIN'),

-- Employers
('employer1', '$2a$10$51p/5rgfYcs9y6RW..7kUuFt58/7Lcjc5/GoBZVjVajpglzmDBYgG', 'employer1@company.com', 'ACTIVE', 'EMPLOYER'),
('employer2', '$2a$10$51p/5rgfYcs9y6RW..7kUuFt58/7Lcjc5/GoBZVjVajpglzmDBYgG', 'employer2@company.com', 'ACTIVE', 'EMPLOYER'),
('employer3', '$2a$10$51p/5rgfYcs9y6RW..7kUuFt58/7Lcjc5/GoBZVjVajpglzmDBYgG', 'employer3@company.com', 'ACTIVE', 'EMPLOYER'),

-- Employees
('employee1', '$2a$10$51p/5rgfYcs9y6RW..7kUuFt58/7Lcjc5/GoBZVjVajpglzmDBYgG', 'emp1@example.com', 'ACTIVE', 'EMPLOYEE'),
('employee2', '$2a$10$51p/5rgfYcs9y6RW..7kUuFt58/7Lcjc5/GoBZVjVajpglzmDBYgG', 'emp2@example.com', 'ACTIVE', 'EMPLOYEE'),
('employee3', '$2a$10$51p/5rgfYcs9y6RW..7kUuFt58/7Lcjc5/GoBZVjVajpglzmDBYgG', 'emp3@example.com', 'ACTIVE', 'EMPLOYEE'),
('employee4', '$2a$10$51p/5rgfYcs9y6RW..7kUuFt58/7Lcjc5/GoBZVjVajpglzmDBYgG', 'emp4@example.com', 'ACTIVE', 'EMPLOYEE'),
('employee5', '$2a$10$51p/5rgfYcs9y6RW..7kUuFt58/7Lcjc5/GoBZVjVajpglzmDBYgG', 'emp5@example.com', 'ACTIVE', 'EMPLOYEE');

INSERT INTO employer_profile (company_name, company_size, industry, app_user_id)
VALUES
('Tech Corp', '500-1000', 'Technology', (SELECT id FROM app_user WHERE username = 'employer1')),
('Health Inc', '200-500', 'Healthcare', (SELECT id FROM app_user WHERE username = 'employer2')),
('EduWorld', '100-200', 'Education', (SELECT id FROM app_user WHERE username = 'employer3'));

INSERT INTO employee_profile (
    full_name, job_title, current_company, location, phone, about_me, programming_languages, app_user_id
)
VALUES
('Alice Smith', 'Software Engineer', 'Tech Corp', 'New York, NY', '123-456-7890', 'Passionate about backend development.', 'Java, Python, SQL', (SELECT id FROM app_user WHERE username = 'employee1')),
('Bob Johnson', 'Product Manager', 'Health Inc', 'San Francisco, CA', '234-567-8901', 'Experienced in agile product development.', 'Php', (SELECT id FROM app_user WHERE username = 'employee2')),
('Carol Lee', 'Data Analyst', 'EduWorld', 'Chicago, IL', '345-678-9012', 'Loves data and visualization.', 'SQL, R, Python', (SELECT id FROM app_user WHERE username = 'employee3')),
('David Kim', 'UI/UX Designer', 'Freelancer', 'Austin, TX', '456-789-0123', 'Design thinker and creative problem solver.', 'Adobe', (SELECT id FROM app_user WHERE username = 'employee4')),
('Eva Brown', 'DevOps Engineer', 'Tech Corp', 'Seattle, WA', '567-890-1234', 'Automating everything.', 'Bash, Docker, Kubernetes, AWS', (SELECT id FROM app_user WHERE username = 'employee5'));
