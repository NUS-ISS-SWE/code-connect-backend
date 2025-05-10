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

INSERT INTO employer_profile (
    company_name, company_description, company_size, industry, company_location, app_user_id
)
VALUES
('Tech Corp', 'Leading tech company building scalable web applications.', 1000, 'Technology', 'New York, NY', (SELECT id FROM app_user WHERE username = 'employer1')),
('Health Inc', 'Healthcare solutions provider focused on wellness.', 500, 'Healthcare', 'San Francisco, CA', (SELECT id FROM app_user WHERE username = 'employer2')),
('EduWorld', 'EdTech company transforming education.', 100, 'Education', 'Chicago, IL', (SELECT id FROM app_user WHERE username = 'employer3'));

INSERT INTO employee_profile (
    full_name, job_title, current_company, location, phone, about_me, programming_language, education, experience, certification, skill_set, app_user_id
)
VALUES
('Alice Smith', 'Software Engineer', 'Tech Corp', 'New York, NY', '65123456',
 'Passionate about backend development.', 'Java, Python, SQL',
 'B.Sc. Computer Science', '3 years at Tech Corp', 'Oracle Java SE 11', 'Spring, Hibernate, Git',
 (SELECT id FROM app_user WHERE username = 'employee1')),

('Bob Johnson', 'Product Manager', 'Health Inc', 'San Francisco, CA', '234-567-8901',
 'Experienced in agile product development.', 'PHP',
 'MBA in Product Management', '5 years at Health Inc', 'Scrum Master Certified', 'Agile, JIRA, Roadmapping',
 (SELECT id FROM app_user WHERE username = 'employee2')),

('Carol Lee', 'Data Analyst', 'EduWorld', 'Chicago, IL', '345-678-9012',
 'Loves data and visualization.', 'SQL, R, Python',
 'M.Sc. Data Analytics', '2 years at EduWorld', 'Google Data Analytics Cert', 'Tableau, PowerBI, SQL',
 (SELECT id FROM app_user WHERE username = 'employee3')),

('David Kim', 'UI/UX Designer', 'Freelancer', 'Austin, TX', '456-789-0123',
 'Design thinker and creative problem solver.', 'Adobe',
 'B.Des in Visual Communication', 'Freelance for 4 years', 'Adobe XD Expert', 'Figma, XD, Sketch',
 (SELECT id FROM app_user WHERE username = 'employee4')),

('Eva Brown', 'DevOps Engineer', 'Tech Corp', 'Seattle, WA', '567-890-1234',
 'Automating everything.', 'Bash, Docker, Kubernetes, AWS',
 'B.Sc. in IT', '4 years at Tech Corp', 'AWS Certified DevOps Engineer', 'CI/CD, Terraform, Jenkins',
 (SELECT id FROM app_user WHERE username = 'employee5'));

INSERT INTO app_user_media (profile_picture, profile_picture_file_name, resume_content, resume_file_name, app_user_id)
VALUES
(NULL, NULL, NULL, NULL, (SELECT id FROM app_user WHERE username = 'employer1')),
(NULL, NULL, NULL, NULL, (SELECT id FROM app_user WHERE username = 'employer2')),
(NULL, NULL, NULL, NULL, (SELECT id FROM app_user WHERE username = 'employer3'));

INSERT INTO app_user_media (profile_picture, profile_picture_file_name, resume_content, resume_file_name, app_user_id)
VALUES
(NULL, NULL, NULL, NULL, (SELECT id FROM app_user WHERE username = 'employee1')),
(NULL, NULL, NULL, NULL, (SELECT id FROM app_user WHERE username = 'employee2')),
(NULL, NULL, NULL, NULL, (SELECT id FROM app_user WHERE username = 'employee3')),
(NULL, NULL, NULL, NULL, (SELECT id FROM app_user WHERE username = 'employee4')),
(NULL, NULL, NULL, NULL, (SELECT id FROM app_user WHERE username = 'employee5'));
