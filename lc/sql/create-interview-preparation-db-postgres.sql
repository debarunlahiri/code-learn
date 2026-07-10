-- PostgreSQL interview practice database.
-- Run with:
-- PGPASSWORD=8ivhaah8 psql -U debarunlahiri -h localhost -f lc/sql/create-interview-preparation-db-postgres.sql

\set ON_ERROR_STOP on

DROP DATABASE IF EXISTS interview_preparation;
CREATE DATABASE interview_preparation
  WITH ENCODING 'UTF8'
  TEMPLATE template0;

\connect interview_preparation

CREATE TABLE departments (
  department_id INT PRIMARY KEY,
  department_name VARCHAR(80) NOT NULL UNIQUE,
  location VARCHAR(80) NOT NULL,
  budget NUMERIC(14,2) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE employees (
  emp_id INT PRIMARY KEY,
  emp_name VARCHAR(100) NOT NULL,
  department_id INT NULL REFERENCES departments(department_id),
  manager_id INT NULL REFERENCES employees(emp_id),
  salary NUMERIC(12,2) NOT NULL,
  hire_date DATE NOT NULL,
  job_title VARCHAR(100) NOT NULL,
  email VARCHAR(140) NOT NULL UNIQUE,
  city VARCHAR(80) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','ON_LEAVE','RESIGNED')),
  bonus NUMERIC(12,2) NULL
);

CREATE TABLE customers (
  customer_id INT PRIMARY KEY,
  customer_name VARCHAR(100) NOT NULL,
  city VARCHAR(80) NOT NULL,
  segment VARCHAR(30) NOT NULL
    CHECK (segment IN ('Consumer','Corporate','Small Business','Enterprise')),
  signup_date DATE NOT NULL,
  email VARCHAR(140) NOT NULL UNIQUE,
  referred_by INT NULL REFERENCES customers(customer_id)
);

CREATE TABLE products (
  product_id INT PRIMARY KEY,
  product_name VARCHAR(120) NOT NULL,
  category VARCHAR(80) NOT NULL,
  price NUMERIC(10,2) NOT NULL,
  cost NUMERIC(10,2) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE orders (
  order_id INT PRIMARY KEY,
  customer_id INT NOT NULL REFERENCES customers(customer_id),
  sales_rep_id INT NULL REFERENCES employees(emp_id),
  order_date DATE NOT NULL,
  shipped_date DATE NULL,
  amount NUMERIC(12,2) NOT NULL DEFAULT 0,
  status VARCHAR(20) NOT NULL
    CHECK (status IN ('PENDING','COMPLETED','CANCELLED','RETURNED')),
  payment_method VARCHAR(20) NOT NULL
    CHECK (payment_method IN ('CARD','UPI','NETBANKING','CASH','WALLET'))
);

CREATE TABLE order_items (
  order_item_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  order_id INT NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE,
  product_id INT NOT NULL REFERENCES products(product_id),
  quantity INT NOT NULL,
  unit_price NUMERIC(10,2) NOT NULL,
  discount_pct NUMERIC(5,2) NOT NULL DEFAULT 0
);

CREATE TABLE classes (
  class_id INT PRIMARY KEY,
  class_name VARCHAR(80) NOT NULL,
  teacher_emp_id INT NULL REFERENCES employees(emp_id)
);

CREATE TABLE students (
  student_id INT PRIMARY KEY,
  student_name VARCHAR(100) NOT NULL,
  class_id INT NOT NULL REFERENCES classes(class_id),
  city VARCHAR(80) NOT NULL,
  admission_date DATE NOT NULL
);

CREATE TABLE marks (
  mark_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  student_id INT NOT NULL REFERENCES students(student_id) ON DELETE CASCADE,
  subject VARCHAR(80) NOT NULL,
  marks INT NOT NULL,
  exam_date DATE NOT NULL
);

CREATE TABLE logins (
  login_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  user_id INT NOT NULL,
  login_date DATE NOT NULL,
  login_time TIME NOT NULL,
  device VARCHAR(20) NOT NULL CHECK (device IN ('WEB','ANDROID','IOS')),
  success BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE tickets (
  ticket_id INT PRIMARY KEY,
  customer_id INT NOT NULL REFERENCES customers(customer_id),
  assigned_emp_id INT NULL REFERENCES employees(emp_id),
  created_date DATE NOT NULL,
  resolved_date DATE NULL,
  priority VARCHAR(20) NOT NULL CHECK (priority IN ('LOW','MEDIUM','HIGH','CRITICAL')),
  status VARCHAR(20) NOT NULL CHECK (status IN ('OPEN','IN_PROGRESS','RESOLVED','CLOSED'))
);

CREATE TABLE audit_events (
  event_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  entity_type VARCHAR(40) NOT NULL,
  entity_id INT NOT NULL,
  action VARCHAR(40) NOT NULL,
  event_time TIMESTAMP NOT NULL,
  actor_emp_id INT NULL REFERENCES employees(emp_id)
);

CREATE INDEX idx_employees_dept_salary ON employees(department_id, salary);
CREATE INDEX idx_employees_manager ON employees(manager_id);
CREATE INDEX idx_orders_customer_date ON orders(customer_id, order_date);
CREATE INDEX idx_orders_status_date ON orders(status, order_date);
CREATE INDEX idx_order_items_product ON order_items(product_id);
CREATE INDEX idx_marks_student_subject ON marks(student_id, subject);
CREATE INDEX idx_logins_user_date ON logins(user_id, login_date);
CREATE INDEX idx_tickets_status_priority ON tickets(status, priority);

INSERT INTO departments (department_id, department_name, location, budget) VALUES
(10,'Engineering','Bengaluru',15000000),(20,'Sales','Mumbai',9000000),
(30,'Marketing','Delhi',6500000),(40,'Finance','Pune',7000000),
(50,'Human Resources','Kolkata',3500000),(60,'Operations','Hyderabad',8000000),
(70,'Customer Support','Chennai',4500000),(80,'Product','Bengaluru',10000000),
(90,'Data Analytics','Gurugram',8500000),(100,'Security','Noida',6000000),
(110,'Legal','Mumbai',3000000),(120,'Training','Kolkata',2800000);

INSERT INTO employees (
  emp_id, emp_name, department_id, manager_id, salary, hire_date,
  job_title, email, city, status, bonus
)
SELECT
  i,
  CASE WHEN i IN (42, 142, 212) THEN 'Alex Kumar' ELSE 'Employee ' || LPAD(i::TEXT, 3, '0') END,
  CASE WHEN i % 37 = 0 THEN NULL ELSE (((i - 1) % 12) + 1) * 10 END,
  CASE WHEN i <= 12 THEN NULL ELSE ((i - 1) % 12) + 1 END,
  30000 + (i % 90) * 1100 + (i % 7) * 250,
  DATE '2018-01-01' + (i * 17),
  (ARRAY['Software Engineer','Senior Engineer','Sales Executive','Analyst','Manager','Support Specialist','Product Manager','Accountant'])[(i % 8) + 1],
  'employee' || i || '@example.com',
  (ARRAY['Kolkata','Bengaluru','Mumbai','Delhi','Pune','Hyderabad','Chennai','Noida','Gurugram','Ahmedabad'])[(i % 10) + 1],
  CASE WHEN i % 41 = 0 THEN 'RESIGNED' WHEN i % 29 = 0 THEN 'ON_LEAVE' ELSE 'ACTIVE' END,
  CASE WHEN i % 9 = 0 THEN NULL ELSE 5000 + (i % 20) * 750 END
FROM generate_series(1, 240) AS s(i);

INSERT INTO customers (
  customer_id, customer_name, city, segment, signup_date, email, referred_by
)
SELECT
  i,
  CASE WHEN i IN (30, 90, 150) THEN 'Same Name Customer' ELSE 'Customer ' || LPAD(i::TEXT, 3, '0') END,
  (ARRAY['Kolkata','Bengaluru','Mumbai','Delhi','Pune','Hyderabad','Chennai','Noida','Gurugram','Ahmedabad','Jaipur','Kochi'])[(i % 12) + 1],
  (ARRAY['Consumer','Corporate','Small Business','Enterprise'])[(i % 4) + 1],
  DATE '2020-01-01' + (i * 11),
  'customer' || i || '@example.com',
  CASE WHEN i > 20 AND i % 5 = 0 THEN i - 10 ELSE NULL END
FROM generate_series(1, 180) AS s(i);

INSERT INTO products (product_id, product_name, category, price, cost, active)
SELECT
  i,
  (ARRAY['Laptop','Monitor','Keyboard','Mouse','Phone','Tablet','Chair','Desk'])[(i % 8) + 1] || ' ' || LPAD(i::TEXT, 2, '0'),
  (ARRAY['Electronics','Electronics','Accessories','Accessories','Mobiles','Mobiles','Furniture','Furniture'])[(i % 8) + 1],
  250 + (i % 25) * 375 + (i % 3) * 49,
  150 + (i % 25) * 250,
  i % 17 <> 0
FROM generate_series(1, 80) AS s(i);

INSERT INTO classes (class_id, class_name, teacher_emp_id) VALUES
(1,'SQL Basics',121),(2,'Advanced SQL',122),(3,'Java Backend',123),
(4,'Spring Boot',124),(5,'Cloud Fundamentals',125),(6,'System Design',126);

INSERT INTO students (student_id, student_name, class_id, city, admission_date)
SELECT
  i,
  'Student ' || LPAD(i::TEXT, 3, '0'),
  ((i - 1) % 6) + 1,
  (ARRAY['Kolkata','Bengaluru','Mumbai','Delhi','Pune','Hyderabad','Chennai','Noida'])[(i % 8) + 1],
  DATE '2022-06-01' + (i * 3)
FROM generate_series(1, 150) AS s(i);

INSERT INTO marks (student_id, subject, marks, exam_date)
SELECT
  i,
  (ARRAY['SQL','Java','Spring Boot','DSA','System Design','Cloud'])[j],
  35 + ((i * j * 7) % 66),
  DATE '2024-01-10' + (j * 20 + i % 15)
FROM generate_series(1, 150) AS s(i)
CROSS JOIN generate_series(1, 6) AS subjects(j);

INSERT INTO orders (
  order_id, customer_id, sales_rep_id, order_date, shipped_date,
  amount, status, payment_method
)
SELECT
  i,
  ((i - 1) % 180) + 1,
  CASE WHEN i % 13 = 0 THEN NULL ELSE ((i - 1) % 240) + 1 END,
  DATE '2023-01-01' + (i * 2),
  CASE WHEN i % 11 = 0 THEN NULL ELSE DATE '2023-01-01' + (i * 2 + (i % 6) + 1) END,
  0,
  CASE WHEN i % 19 = 0 THEN 'CANCELLED' WHEN i % 23 = 0 THEN 'RETURNED' WHEN i % 7 = 0 THEN 'PENDING' ELSE 'COMPLETED' END,
  (ARRAY['CARD','UPI','NETBANKING','CASH','WALLET'])[(i % 5) + 1]
FROM generate_series(1, 750) AS s(i);

INSERT INTO order_items (order_id, product_id, quantity, unit_price, discount_pct)
SELECT
  i,
  ((i * j) % 80) + 1,
  ((i + j) % 5) + 1,
  250 + (((i * j) % 25) * 375) + (((i * j) % 3) * 49),
  CASE WHEN (i + j) % 10 = 0 THEN 15 WHEN (i + j) % 6 = 0 THEN 10 ELSE 0 END
FROM generate_series(1, 750) AS s(i)
CROSS JOIN LATERAL generate_series(1, (i % 4) + 1) AS items(j);

UPDATE orders o
SET amount = x.order_total
FROM (
  SELECT order_id, SUM(quantity * unit_price * (1 - discount_pct / 100)) AS order_total
  FROM order_items
  GROUP BY order_id
) x
WHERE o.order_id = x.order_id;

INSERT INTO logins (user_id, login_date, login_time, device, success)
SELECT
  ((i - 1) % 180) + 1,
  DATE '2024-01-01' + (i % 180),
  MAKE_TIME(i % 24, (i * 7) % 60, (i * 13) % 60),
  (ARRAY['WEB','ANDROID','IOS'])[(i % 3) + 1],
  i % 14 <> 0
FROM generate_series(1, 1200) AS s(i);

INSERT INTO tickets (
  ticket_id, customer_id, assigned_emp_id, created_date, resolved_date, priority, status
)
SELECT
  i,
  ((i - 1) % 180) + 1,
  CASE WHEN i % 17 = 0 THEN NULL ELSE ((i - 1) % 240) + 1 END,
  DATE '2024-01-01' + i,
  CASE WHEN i % 9 = 0 THEN NULL ELSE DATE '2024-01-01' + (i + (i % 8) + 1) END,
  (ARRAY['LOW','MEDIUM','HIGH','CRITICAL'])[(i % 4) + 1],
  CASE WHEN i % 9 = 0 THEN 'OPEN' WHEN i % 5 = 0 THEN 'IN_PROGRESS' WHEN i % 7 = 0 THEN 'CLOSED' ELSE 'RESOLVED' END
FROM generate_series(1, 320) AS s(i);

INSERT INTO audit_events (entity_type, entity_id, action, event_time, actor_emp_id)
SELECT
  (ARRAY['ORDER','CUSTOMER','TICKET','EMPLOYEE'])[(i % 4) + 1],
  ((i - 1) % 250) + 1,
  (ARRAY['CREATE','UPDATE','APPROVE','CANCEL','LOGIN'])[(i % 5) + 1],
  TIMESTAMP '2024-01-01 08:00:00' + (i * INTERVAL '37 minutes'),
  CASE WHEN i % 21 = 0 THEN NULL ELSE ((i - 1) % 240) + 1 END
FROM generate_series(1, 900) AS s(i);

SELECT 'departments' AS table_name, COUNT(*) AS row_count FROM departments
UNION ALL SELECT 'employees', COUNT(*) FROM employees
UNION ALL SELECT 'customers', COUNT(*) FROM customers
UNION ALL SELECT 'products', COUNT(*) FROM products
UNION ALL SELECT 'orders', COUNT(*) FROM orders
UNION ALL SELECT 'order_items', COUNT(*) FROM order_items
UNION ALL SELECT 'classes', COUNT(*) FROM classes
UNION ALL SELECT 'students', COUNT(*) FROM students
UNION ALL SELECT 'marks', COUNT(*) FROM marks
UNION ALL SELECT 'logins', COUNT(*) FROM logins
UNION ALL SELECT 'tickets', COUNT(*) FROM tickets
UNION ALL SELECT 'audit_events', COUNT(*) FROM audit_events
ORDER BY table_name;
