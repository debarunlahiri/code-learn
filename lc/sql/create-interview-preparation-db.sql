-- MySQL/MariaDB interview practice database.
-- Run with:
-- /Applications/XAMPP/xamppfiles/bin/mysql -u debarunlahiri -p8ivhaah8 < lc/sql/create-interview-preparation-db.sql

DROP DATABASE IF EXISTS interview_preparation;
CREATE DATABASE interview_preparation
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE interview_preparation;

CREATE TABLE departments (
  department_id INT PRIMARY KEY,
  department_name VARCHAR(80) NOT NULL UNIQUE,
  location VARCHAR(80) NOT NULL,
  budget DECIMAL(14,2) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE employees (
  emp_id INT PRIMARY KEY,
  emp_name VARCHAR(100) NOT NULL,
  department_id INT NULL,
  manager_id INT NULL,
  salary DECIMAL(12,2) NOT NULL,
  hire_date DATE NOT NULL,
  job_title VARCHAR(100) NOT NULL,
  email VARCHAR(140) NOT NULL UNIQUE,
  city VARCHAR(80) NOT NULL,
  status ENUM('ACTIVE','ON_LEAVE','RESIGNED') NOT NULL DEFAULT 'ACTIVE',
  bonus DECIMAL(12,2) NULL,
  CONSTRAINT fk_employees_department
    FOREIGN KEY (department_id) REFERENCES departments(department_id),
  CONSTRAINT fk_employees_manager
    FOREIGN KEY (manager_id) REFERENCES employees(emp_id)
) ENGINE=InnoDB;

CREATE TABLE customers (
  customer_id INT PRIMARY KEY,
  customer_name VARCHAR(100) NOT NULL,
  city VARCHAR(80) NOT NULL,
  segment ENUM('Consumer','Corporate','Small Business','Enterprise') NOT NULL,
  signup_date DATE NOT NULL,
  email VARCHAR(140) NOT NULL UNIQUE,
  referred_by INT NULL,
  CONSTRAINT fk_customers_referrer
    FOREIGN KEY (referred_by) REFERENCES customers(customer_id)
) ENGINE=InnoDB;

CREATE TABLE products (
  product_id INT PRIMARY KEY,
  product_name VARCHAR(120) NOT NULL,
  category VARCHAR(80) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  cost DECIMAL(10,2) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

CREATE TABLE orders (
  order_id INT PRIMARY KEY,
  customer_id INT NOT NULL,
  sales_rep_id INT NULL,
  order_date DATE NOT NULL,
  shipped_date DATE NULL,
  amount DECIMAL(12,2) NOT NULL DEFAULT 0,
  status ENUM('PENDING','COMPLETED','CANCELLED','RETURNED') NOT NULL,
  payment_method ENUM('CARD','UPI','NETBANKING','CASH','WALLET') NOT NULL,
  CONSTRAINT fk_orders_customer
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
  CONSTRAINT fk_orders_sales_rep
    FOREIGN KEY (sales_rep_id) REFERENCES employees(emp_id)
) ENGINE=InnoDB;

CREATE TABLE order_items (
  order_item_id INT AUTO_INCREMENT PRIMARY KEY,
  order_id INT NOT NULL,
  product_id INT NOT NULL,
  quantity INT NOT NULL,
  unit_price DECIMAL(10,2) NOT NULL,
  discount_pct DECIMAL(5,2) NOT NULL DEFAULT 0,
  CONSTRAINT fk_order_items_order
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
  CONSTRAINT fk_order_items_product
    FOREIGN KEY (product_id) REFERENCES products(product_id)
) ENGINE=InnoDB;

CREATE TABLE classes (
  class_id INT PRIMARY KEY,
  class_name VARCHAR(80) NOT NULL,
  teacher_emp_id INT NULL,
  CONSTRAINT fk_classes_teacher
    FOREIGN KEY (teacher_emp_id) REFERENCES employees(emp_id)
) ENGINE=InnoDB;

CREATE TABLE students (
  student_id INT PRIMARY KEY,
  student_name VARCHAR(100) NOT NULL,
  class_id INT NOT NULL,
  city VARCHAR(80) NOT NULL,
  admission_date DATE NOT NULL,
  CONSTRAINT fk_students_class
    FOREIGN KEY (class_id) REFERENCES classes(class_id)
) ENGINE=InnoDB;

CREATE TABLE marks (
  mark_id INT AUTO_INCREMENT PRIMARY KEY,
  student_id INT NOT NULL,
  subject VARCHAR(80) NOT NULL,
  marks INT NOT NULL,
  exam_date DATE NOT NULL,
  CONSTRAINT fk_marks_student
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE logins (
  login_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  login_date DATE NOT NULL,
  login_time TIME NOT NULL,
  device ENUM('WEB','ANDROID','IOS') NOT NULL,
  success BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

CREATE TABLE tickets (
  ticket_id INT PRIMARY KEY,
  customer_id INT NOT NULL,
  assigned_emp_id INT NULL,
  created_date DATE NOT NULL,
  resolved_date DATE NULL,
  priority ENUM('LOW','MEDIUM','HIGH','CRITICAL') NOT NULL,
  status ENUM('OPEN','IN_PROGRESS','RESOLVED','CLOSED') NOT NULL,
  CONSTRAINT fk_tickets_customer
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
  CONSTRAINT fk_tickets_employee
    FOREIGN KEY (assigned_emp_id) REFERENCES employees(emp_id)
) ENGINE=InnoDB;

CREATE TABLE audit_events (
  event_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  entity_type VARCHAR(40) NOT NULL,
  entity_id INT NOT NULL,
  action VARCHAR(40) NOT NULL,
  event_time DATETIME NOT NULL,
  actor_emp_id INT NULL,
  CONSTRAINT fk_audit_events_actor
    FOREIGN KEY (actor_emp_id) REFERENCES employees(emp_id)
) ENGINE=InnoDB;

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

DELIMITER //
CREATE PROCEDURE seed_interview_data()
BEGIN
  DECLARE i INT DEFAULT 1;
  DECLARE j INT DEFAULT 1;
  DECLARE order_total DECIMAL(12,2);

  WHILE i <= 240 DO
    INSERT INTO employees (
      emp_id, emp_name, department_id, manager_id, salary, hire_date,
      job_title, email, city, status, bonus
    ) VALUES (
      i,
      CASE WHEN i IN (42, 142, 212) THEN 'Alex Kumar' ELSE CONCAT('Employee ', LPAD(i, 3, '0')) END,
      CASE WHEN i % 37 = 0 THEN NULL ELSE (((i - 1) % 12) + 1) * 10 END,
      CASE WHEN i <= 12 THEN NULL ELSE ((i - 1) % 12) + 1 END,
      30000 + (i % 90) * 1100 + (i % 7) * 250,
      DATE_ADD('2018-01-01', INTERVAL (i * 17) DAY),
      ELT((i % 8) + 1, 'Software Engineer', 'Senior Engineer', 'Sales Executive', 'Analyst', 'Manager', 'Support Specialist', 'Product Manager', 'Accountant'),
      CONCAT('employee', i, '@example.com'),
      ELT((i % 10) + 1, 'Kolkata', 'Bengaluru', 'Mumbai', 'Delhi', 'Pune', 'Hyderabad', 'Chennai', 'Noida', 'Gurugram', 'Ahmedabad'),
      CASE WHEN i % 41 = 0 THEN 'RESIGNED' WHEN i % 29 = 0 THEN 'ON_LEAVE' ELSE 'ACTIVE' END,
      CASE WHEN i % 9 = 0 THEN NULL ELSE 5000 + (i % 20) * 750 END
    );
    SET i = i + 1;
  END WHILE;

  SET i = 1;
  WHILE i <= 180 DO
    INSERT INTO customers (
      customer_id, customer_name, city, segment, signup_date, email, referred_by
    ) VALUES (
      i,
      CASE WHEN i IN (30, 90, 150) THEN 'Same Name Customer' ELSE CONCAT('Customer ', LPAD(i, 3, '0')) END,
      ELT((i % 12) + 1, 'Kolkata', 'Bengaluru', 'Mumbai', 'Delhi', 'Pune', 'Hyderabad', 'Chennai', 'Noida', 'Gurugram', 'Ahmedabad', 'Jaipur', 'Kochi'),
      ELT((i % 4) + 1, 'Consumer', 'Corporate', 'Small Business', 'Enterprise'),
      DATE_ADD('2020-01-01', INTERVAL (i * 11) DAY),
      CONCAT('customer', i, '@example.com'),
      CASE WHEN i > 20 AND i % 5 = 0 THEN i - 10 ELSE NULL END
    );
    SET i = i + 1;
  END WHILE;

  SET i = 1;
  WHILE i <= 80 DO
    INSERT INTO products (product_id, product_name, category, price, cost, active) VALUES (
      i,
      CONCAT(ELT((i % 8) + 1, 'Laptop', 'Monitor', 'Keyboard', 'Mouse', 'Phone', 'Tablet', 'Chair', 'Desk'), ' ', LPAD(i, 2, '0')),
      ELT((i % 8) + 1, 'Electronics', 'Electronics', 'Accessories', 'Accessories', 'Mobiles', 'Mobiles', 'Furniture', 'Furniture'),
      250 + (i % 25) * 375 + (i % 3) * 49,
      150 + (i % 25) * 250,
      i % 17 <> 0
    );
    SET i = i + 1;
  END WHILE;

  INSERT INTO classes (class_id, class_name, teacher_emp_id) VALUES
  (1,'SQL Basics',121),(2,'Advanced SQL',122),(3,'Java Backend',123),
  (4,'Spring Boot',124),(5,'Cloud Fundamentals',125),(6,'System Design',126);

  SET i = 1;
  WHILE i <= 150 DO
    INSERT INTO students (student_id, student_name, class_id, city, admission_date) VALUES (
      i,
      CONCAT('Student ', LPAD(i, 3, '0')),
      ((i - 1) % 6) + 1,
      ELT((i % 8) + 1, 'Kolkata', 'Bengaluru', 'Mumbai', 'Delhi', 'Pune', 'Hyderabad', 'Chennai', 'Noida'),
      DATE_ADD('2022-06-01', INTERVAL (i * 3) DAY)
    );

    SET j = 1;
    WHILE j <= 6 DO
      INSERT INTO marks (student_id, subject, marks, exam_date) VALUES (
        i,
        ELT(j, 'SQL', 'Java', 'Spring Boot', 'DSA', 'System Design', 'Cloud'),
        35 + ((i * j * 7) % 66),
        DATE_ADD('2024-01-10', INTERVAL (j * 20 + i % 15) DAY)
      );
      SET j = j + 1;
    END WHILE;

    SET i = i + 1;
  END WHILE;

  SET i = 1;
  WHILE i <= 750 DO
    INSERT INTO orders (
      order_id, customer_id, sales_rep_id, order_date, shipped_date,
      amount, status, payment_method
    ) VALUES (
      i,
      ((i - 1) % 180) + 1,
      CASE WHEN i % 13 = 0 THEN NULL ELSE ((i - 1) % 240) + 1 END,
      DATE_ADD('2023-01-01', INTERVAL (i * 2) DAY),
      CASE WHEN i % 11 = 0 THEN NULL ELSE DATE_ADD('2023-01-01', INTERVAL (i * 2 + (i % 6) + 1) DAY) END,
      0,
      CASE WHEN i % 19 = 0 THEN 'CANCELLED' WHEN i % 23 = 0 THEN 'RETURNED' WHEN i % 7 = 0 THEN 'PENDING' ELSE 'COMPLETED' END,
      ELT((i % 5) + 1, 'CARD', 'UPI', 'NETBANKING', 'CASH', 'WALLET')
    );

    SET j = 1;
    WHILE j <= ((i % 4) + 1) DO
      INSERT INTO order_items (order_id, product_id, quantity, unit_price, discount_pct) VALUES (
        i,
        ((i * j) % 80) + 1,
        ((i + j) % 5) + 1,
        250 + (((i * j) % 25) * 375) + (((i * j) % 3) * 49),
        CASE WHEN (i + j) % 10 = 0 THEN 15 WHEN (i + j) % 6 = 0 THEN 10 ELSE 0 END
      );
      SET j = j + 1;
    END WHILE;

    SELECT SUM(quantity * unit_price * (1 - discount_pct / 100))
      INTO order_total
      FROM order_items
      WHERE order_id = i;

    UPDATE orders
      SET amount = order_total
      WHERE order_id = i;

    SET i = i + 1;
  END WHILE;

  SET i = 1;
  WHILE i <= 1200 DO
    INSERT INTO logins (user_id, login_date, login_time, device, success) VALUES (
      ((i - 1) % 180) + 1,
      DATE_ADD('2024-01-01', INTERVAL (i % 180) DAY),
      MAKETIME(i % 24, (i * 7) % 60, (i * 13) % 60),
      ELT((i % 3) + 1, 'WEB', 'ANDROID', 'IOS'),
      i % 14 <> 0
    );
    SET i = i + 1;
  END WHILE;

  SET i = 1;
  WHILE i <= 320 DO
    INSERT INTO tickets (
      ticket_id, customer_id, assigned_emp_id, created_date, resolved_date, priority, status
    ) VALUES (
      i,
      ((i - 1) % 180) + 1,
      CASE WHEN i % 17 = 0 THEN NULL ELSE ((i - 1) % 240) + 1 END,
      DATE_ADD('2024-01-01', INTERVAL i DAY),
      CASE WHEN i % 9 = 0 THEN NULL ELSE DATE_ADD('2024-01-01', INTERVAL (i + (i % 8) + 1) DAY) END,
      ELT((i % 4) + 1, 'LOW', 'MEDIUM', 'HIGH', 'CRITICAL'),
      CASE WHEN i % 9 = 0 THEN 'OPEN' WHEN i % 5 = 0 THEN 'IN_PROGRESS' WHEN i % 7 = 0 THEN 'CLOSED' ELSE 'RESOLVED' END
    );
    SET i = i + 1;
  END WHILE;

  SET i = 1;
  WHILE i <= 900 DO
    INSERT INTO audit_events (entity_type, entity_id, action, event_time, actor_emp_id) VALUES (
      ELT((i % 4) + 1, 'ORDER', 'CUSTOMER', 'TICKET', 'EMPLOYEE'),
      ((i - 1) % 250) + 1,
      ELT((i % 5) + 1, 'CREATE', 'UPDATE', 'APPROVE', 'CANCEL', 'LOGIN'),
      DATE_ADD('2024-01-01 08:00:00', INTERVAL (i * 37) MINUTE),
      CASE WHEN i % 21 = 0 THEN NULL ELSE ((i - 1) % 240) + 1 END
    );
    SET i = i + 1;
  END WHILE;
END//
DELIMITER ;

CALL seed_interview_data();
DROP PROCEDURE seed_interview_data;

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
UNION ALL SELECT 'audit_events', COUNT(*) FROM audit_events;
