-- SQL Interview Query Practice
-- Generated from README.md.
-- Tables assumed:
-- employees(emp_id, emp_name, department_id, manager_id, salary, hire_date)
-- departments(department_id, department_name)
-- customers(customer_id, customer_name, city)
-- orders(order_id, customer_id, order_date, amount, status)
-- products(product_id, product_name, category, price)
-- order_items(order_id, product_id, quantity, unit_price)
-- students(student_id, student_name, class_id)
-- marks(student_id, subject, marks)
-- logins(user_id, login_date)

-- Question 01
employees(emp_id, emp_name, department_id, manager_id, salary, hire_date)
departments(department_id, department_name)
customers(customer_id, customer_name, city)
orders(order_id, customer_id, order_date, amount, status)
products(product_id, product_name, category, price)
order_items(order_id, product_id, quantity, unit_price)
students(student_id, student_name, class_id)
marks(student_id, subject, marks)
logins(user_id, login_date);

-- Question 02
SELECT *
FROM employees;;

-- Question 03
SELECT emp_name, salary
FROM employees;;

-- Question 04
SELECT emp_id, emp_name, salary
FROM employees
WHERE salary > 50000;;

-- Question 05
SELECT emp_id, emp_name, hire_date
FROM employees
WHERE hire_date > DATE '2023-01-01';;

-- Question 06
SELECT emp_id, emp_name, salary
FROM employees
ORDER BY salary DESC;;

-- Question 07
SELECT emp_id, emp_name
FROM employees
WHERE emp_name LIKE 'A%';;

-- Question 08
SELECT emp_id, emp_name
FROM employees
WHERE emp_name LIKE '%an%';;

-- Question 09
SELECT emp_id, emp_name, salary
FROM employees
WHERE salary BETWEEN 40000 AND 80000;;

-- Question 10
SELECT emp_id, emp_name, department_id
FROM employees
WHERE department_id IN (10, 20, 30);;

-- Question 11
SELECT emp_id, emp_name
FROM employees
WHERE manager_id IS NULL;;

-- Question 12
SELECT COUNT(*) AS total_employees
FROM employees;;

-- Question 13
SELECT MAX(salary) AS max_salary
FROM employees;;

-- Question 14
SELECT MIN(salary) AS min_salary
FROM employees;;

-- Question 15
SELECT AVG(salary) AS average_salary
FROM employees;;

-- Question 16
SELECT SUM(salary) AS total_salary
FROM employees;;

-- Question 17
SELECT DISTINCT department_id
FROM employees;;

-- Question 18
SELECT DISTINCT city
FROM customers;;

-- Question 19
SELECT order_id, customer_id, order_date, amount
FROM orders
ORDER BY order_date DESC
LIMIT 5;;

-- Question 20
SELECT order_id, customer_id, amount, status
FROM orders
WHERE status = 'COMPLETED';;

-- Question 21
SELECT product_id, product_name, price
FROM products
WHERE price > 1000;;

-- Question 22
SELECT e.emp_id, e.emp_name, d.department_name
FROM employees e
JOIN departments d
  ON e.department_id = d.department_id;;

-- Question 23
SELECT e.emp_id, e.emp_name, d.department_name
FROM employees e
LEFT JOIN departments d
  ON e.department_id = d.department_id;;

-- Question 24
SELECT d.department_id, d.department_name
FROM departments d
LEFT JOIN employees e
  ON d.department_id = e.department_id
WHERE e.emp_id IS NULL;;

-- Question 25
SELECT department_id, COUNT(*) AS employee_count
FROM employees
GROUP BY department_id;;

-- Question 26
SELECT department_id, AVG(salary) AS average_salary
FROM employees
GROUP BY department_id;;

-- Question 27
SELECT department_id, COUNT(*) AS employee_count
FROM employees
GROUP BY department_id
HAVING COUNT(*) > 5;;

-- Question 28
SELECT department_id, AVG(salary) AS average_salary
FROM employees
GROUP BY department_id
HAVING AVG(salary) > 70000;;

-- Question 29
SELECT department_id, MAX(salary) AS highest_salary
FROM employees
GROUP BY department_id;;

-- Question 30
SELECT e.emp_id, e.emp_name, e.department_id, e.salary
FROM employees e
JOIN (
    SELECT department_id, MAX(salary) AS highest_salary
    FROM employees
    GROUP BY department_id
) x
  ON e.department_id = x.department_id
 AND e.salary = x.highest_salary;;

-- Question 31
SELECT MAX(salary) AS second_highest_salary
FROM employees
WHERE salary < (
    SELECT MAX(salary)
    FROM employees
);;

-- Question 32
SELECT emp_name, COUNT(*) AS duplicate_count
FROM employees
GROUP BY emp_name
HAVING COUNT(*) > 1;;

-- Question 33
SELECT customer_name, COUNT(*) AS duplicate_count
FROM customers
GROUP BY customer_name
HAVING COUNT(*) > 1;;

-- Question 34
SELECT DISTINCT c.customer_id, c.customer_name
FROM customers c
JOIN orders o
  ON c.customer_id = o.customer_id;;

-- Question 35
SELECT c.customer_id, c.customer_name
FROM customers c
LEFT JOIN orders o
  ON c.customer_id = o.customer_id
WHERE o.order_id IS NULL;;

-- Question 36
SELECT customer_id, SUM(amount) AS total_amount
FROM orders
GROUP BY customer_id;;

-- Question 37
SELECT customer_id, SUM(amount) AS total_amount
FROM orders
GROUP BY customer_id
HAVING SUM(amount) > 10000;;

-- Question 38
SELECT customer_id, MAX(order_date) AS latest_order_date
FROM orders
GROUP BY customer_id;;

-- Question 39
SELECT o.order_id, o.customer_id, o.order_date, o.amount
FROM orders o
JOIN (
    SELECT customer_id, MAX(order_date) AS latest_order_date
    FROM orders
    GROUP BY customer_id
) x
  ON o.customer_id = x.customer_id
 AND o.order_date = x.latest_order_date;;

-- Question 40
SELECT o.order_id, SUM(oi.quantity) AS total_quantity
FROM orders o
JOIN order_items oi
  ON o.order_id = oi.order_id
GROUP BY o.order_id;;

-- Question 41
SELECT p.product_id, p.product_name, SUM(oi.quantity) AS total_quantity
FROM products p
JOIN order_items oi
  ON p.product_id = oi.product_id
GROUP BY p.product_id, p.product_name;;

-- Question 42
SELECT p.product_id, p.product_name
FROM products p
LEFT JOIN order_items oi
  ON p.product_id = oi.product_id
WHERE oi.product_id IS NULL;;

-- Question 43
SELECT s.student_id, s.student_name, SUM(m.marks) AS total_marks
FROM students s
JOIN marks m
  ON s.student_id = m.student_id
GROUP BY s.student_id, s.student_name;;

-- Question 44
SELECT DISTINCT s.student_id, s.student_name
FROM students s
JOIN marks m
  ON s.student_id = m.student_id
WHERE m.marks < 35;;

-- Question 45
SELECT subject, MAX(marks) AS highest_marks
FROM marks
GROUP BY subject;;

-- Question 46
SELECT m.student_id, m.subject, m.marks
FROM marks m
JOIN (
    SELECT subject, MAX(marks) AS highest_marks
    FROM marks
    GROUP BY subject
) x
  ON m.subject = x.subject
 AND m.marks = x.highest_marks;;

-- Question 47
SELECT emp_id,
       emp_name,
       salary,
       ROW_NUMBER() OVER (ORDER BY salary DESC) AS salary_rank
FROM employees;;

-- Question 48
SELECT emp_id,
       emp_name,
       salary,
       RANK() OVER (ORDER BY salary DESC) AS salary_rank
FROM employees;;

-- Question 49
SELECT emp_id,
       emp_name,
       salary,
       DENSE_RANK() OVER (ORDER BY salary DESC) AS salary_rank
FROM employees;;

-- Question 50
WITH ranked_salaries AS (
    SELECT salary,
           DENSE_RANK() OVER (ORDER BY salary DESC) AS salary_rank
    FROM employees
)
SELECT salary
FROM ranked_salaries
WHERE salary_rank = :n;;

-- Question 51
WITH ranked_employees AS (
    SELECT emp_id,
           emp_name,
           department_id,
           salary,
           ROW_NUMBER() OVER (
               PARTITION BY department_id
               ORDER BY salary DESC
           ) AS rn
    FROM employees
)
SELECT emp_id, emp_name, department_id, salary
FROM ranked_employees
WHERE rn <= 3;;

-- Question 52
WITH department_avg AS (
    SELECT department_id, AVG(salary) AS avg_salary
    FROM employees
    GROUP BY department_id
)
SELECT e.emp_id, e.emp_name, e.department_id, e.salary
FROM employees e
JOIN department_avg d
  ON e.department_id = d.department_id
WHERE e.salary > d.avg_salary;;

-- Question 53
SELECT e.emp_id,
       e.emp_name,
       e.salary AS employee_salary,
       m.emp_name AS manager_name,
       m.salary AS manager_salary
FROM employees e
JOIN employees m
  ON e.manager_id = m.emp_id
WHERE e.salary > m.salary;;

-- Question 54
SELECT m.emp_id, m.emp_name, COUNT(e.emp_id) AS direct_reports
FROM employees m
JOIN employees e
  ON m.emp_id = e.manager_id
GROUP BY m.emp_id, m.emp_name
HAVING COUNT(e.emp_id) >= 5;;

-- Question 55
SELECT emp_id, emp_name, hire_date
FROM employees
WHERE hire_date >= CURRENT_DATE - INTERVAL '30 days';;

-- Question 56
SELECT order_id,
       customer_id,
       order_date,
       amount,
       SUM(amount) OVER (
           PARTITION BY customer_id
           ORDER BY order_date
       ) AS running_total
FROM orders;;

-- Question 57
SELECT order_id,
       customer_id,
       order_date,
       amount,
       LAG(amount) OVER (
           PARTITION BY customer_id
           ORDER BY order_date
       ) AS previous_order_amount
FROM orders;;

-- Question 58
SELECT order_id,
       customer_id,
       order_date,
       LEAD(order_date) OVER (
           PARTITION BY customer_id
           ORDER BY order_date
       ) AS next_order_date
FROM orders;;

-- Question 59
WITH order_diff AS (
    SELECT order_id,
           customer_id,
           order_date,
           amount,
           LAG(amount) OVER (
               PARTITION BY customer_id
               ORDER BY order_date
           ) AS previous_amount
    FROM orders
)
SELECT order_id, customer_id, order_date, amount, previous_amount
FROM order_diff
WHERE amount > previous_amount;;

-- Question 60
WITH login_days AS (
    SELECT DISTINCT user_id, login_date
    FROM logins
),
with_previous AS (
    SELECT user_id,
           login_date,
           LAG(login_date) OVER (
               PARTITION BY user_id
               ORDER BY login_date
           ) AS previous_login_date
    FROM login_days
)
SELECT DISTINCT user_id
FROM with_previous
WHERE login_date = previous_login_date + INTERVAL '1 day';;

-- Question 61
WITH login_days AS (
    SELECT DISTINCT user_id, login_date
    FROM logins
),
numbered AS (
    SELECT user_id,
           login_date,
           login_date - (ROW_NUMBER() OVER (
               PARTITION BY user_id
               ORDER BY login_date
           ) * INTERVAL '1 day') AS group_key
    FROM login_days
),
streaks AS (
    SELECT user_id, COUNT(*) AS streak_length
    FROM numbered
    GROUP BY user_id, group_key
)
SELECT DISTINCT user_id
FROM streaks
WHERE streak_length >= 3;;

-- Question 62
WITH ranked_orders AS (
    SELECT order_id,
           customer_id,
           order_date,
           amount,
           ROW_NUMBER() OVER (
               PARTITION BY customer_id
               ORDER BY order_date
           ) AS rn
    FROM orders
)
SELECT order_id, customer_id, order_date, amount
FROM ranked_orders
WHERE rn = 1;;

-- Question 63
WITH ranked_orders AS (
    SELECT order_id,
           customer_id,
           order_date,
           amount,
           ROW_NUMBER() OVER (
               PARTITION BY customer_id
               ORDER BY order_date
           ) AS rn
    FROM orders
)
SELECT order_id, customer_id, order_date, amount
FROM ranked_orders
WHERE rn = 2;;

-- Question 64
WITH first_orders AS (
    SELECT order_id,
           customer_id,
           order_date,
           amount,
           ROW_NUMBER() OVER (
               PARTITION BY customer_id
               ORDER BY order_date
           ) AS rn
    FROM orders
)
SELECT customer_id, order_id, amount
FROM first_orders
WHERE rn = 1
  AND amount > 5000;;

-- Question 65
SELECT DATE_TRUNC('month', order_date) AS sales_month,
       SUM(amount) AS total_sales
FROM orders
GROUP BY DATE_TRUNC('month', order_date)
ORDER BY sales_month;;

-- Question 66
WITH monthly_sales AS (
    SELECT DATE_TRUNC('month', order_date) AS sales_month,
           SUM(amount) AS total_sales
    FROM orders
    GROUP BY DATE_TRUNC('month', order_date)
),
with_previous AS (
    SELECT sales_month,
           total_sales,
           LAG(total_sales) OVER (ORDER BY sales_month) AS previous_sales
    FROM monthly_sales
)
SELECT sales_month,
       total_sales,
       previous_sales,
       ROUND(
           (total_sales - previous_sales) * 100.0 / NULLIF(previous_sales, 0),
           2
       ) AS growth_percentage
FROM with_previous;;

-- Question 67
WITH monthly_customer_sales AS (
    SELECT DATE_TRUNC('month', order_date) AS sales_month,
           customer_id,
           SUM(amount) AS total_sales
    FROM orders
    GROUP BY DATE_TRUNC('month', order_date), customer_id
),
ranked AS (
    SELECT sales_month,
           customer_id,
           total_sales,
           RANK() OVER (
               PARTITION BY sales_month
               ORDER BY total_sales DESC
           ) AS rn
    FROM monthly_customer_sales
)
SELECT sales_month, customer_id, total_sales
FROM ranked
WHERE rn = 1;;

-- Question 68
SELECT product_id,
       SUM(quantity * unit_price) AS revenue
FROM order_items
GROUP BY product_id
ORDER BY revenue DESC
LIMIT 3;;

-- Question 69
WITH product_revenue AS (
    SELECT p.category,
           p.product_id,
           p.product_name,
           SUM(oi.quantity * oi.unit_price) AS revenue
    FROM products p
    JOIN order_items oi
      ON p.product_id = oi.product_id
    GROUP BY p.category, p.product_id, p.product_name
),
ranked AS (
    SELECT category,
           product_id,
           product_name,
           revenue,
           RANK() OVER (
               PARTITION BY category
               ORDER BY revenue DESC
           ) AS rn
    FROM product_revenue
)
SELECT category, product_id, product_name, revenue
FROM ranked
WHERE rn = 1;;

-- Question 70
SELECT emp_id,
       emp_name,
       department_id,
       salary,
       LEAD(salary) OVER (
           PARTITION BY department_id
           ORDER BY salary
       ) - salary AS gap_to_next_salary
FROM employees;;

-- Question 71
WITH duplicates AS (
    SELECT emp_id,
           ROW_NUMBER() OVER (
               PARTITION BY emp_name
               ORDER BY emp_id
           ) AS rn
    FROM employees
)
DELETE FROM employees
WHERE emp_id IN (
    SELECT emp_id
    FROM duplicates
    WHERE rn > 1
);;
