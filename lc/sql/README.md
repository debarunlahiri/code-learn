# SQL Interview Questions

Platform: SQL Practice  
Difficulty: Easy to Hard  
Topic: SQL, Joins, Aggregation, Window Functions, CTEs

Use these questions to practice SQL interview patterns commonly asked for backend,
data, and full-stack roles.

Full query-only practice file:
[interview-queries.sql](interview-queries.sql).

## Sample Tables Used

Most questions assume these common interview tables:

```sql
employees(emp_id, emp_name, department_id, manager_id, salary, hire_date)
departments(department_id, department_name)
customers(customer_id, customer_name, city)
orders(order_id, customer_id, order_date, amount, status)
products(product_id, product_name, category, price)
order_items(order_id, product_id, quantity, unit_price)
students(student_id, student_name, class_id)
marks(student_id, subject, marks)
logins(user_id, login_date)
```

## Easy

1. Given an `employees` table, write a query to fetch all employees.

```sql
SELECT *
FROM employees;
```

2. Given an `employees` table, write a query to fetch employee names and salaries.

```sql
SELECT emp_name, salary
FROM employees;
```

3. Given an `employees` table, write a query to find employees earning more than `50000`.

```sql
SELECT emp_id, emp_name, salary
FROM employees
WHERE salary > 50000;
```

4. Given an `employees` table, write a query to find employees hired after
   `2023-01-01`.

```sql
SELECT emp_id, emp_name, hire_date
FROM employees
WHERE hire_date > DATE '2023-01-01';
```

5. Given an `employees` table, write a query to sort employees by salary descending.

```sql
SELECT emp_id, emp_name, salary
FROM employees
ORDER BY salary DESC;
```

6. Given an `employees` table, write a query to find employees whose name starts with
   `A`.

```sql
SELECT emp_id, emp_name
FROM employees
WHERE emp_name LIKE 'A%';
```

7. Given an `employees` table, write a query to find employees whose name contains
   `an`.

```sql
SELECT emp_id, emp_name
FROM employees
WHERE emp_name LIKE '%an%';
```

8. Given an `employees` table, write a query to fetch employees with salary between
   `40000` and `80000`.

```sql
SELECT emp_id, emp_name, salary
FROM employees
WHERE salary BETWEEN 40000 AND 80000;
```

9. Given an `employees` table, write a query to fetch employees from departments `10`,
   `20`, and `30`.

```sql
SELECT emp_id, emp_name, department_id
FROM employees
WHERE department_id IN (10, 20, 30);
```

10. Given an `employees` table, write a query to find employees without a manager.

```sql
SELECT emp_id, emp_name
FROM employees
WHERE manager_id IS NULL;
```

11. Given an `employees` table, write a query to count total employees.

```sql
SELECT COUNT(*) AS total_employees
FROM employees;
```

12. Given an `employees` table, write a query to find the maximum salary.

```sql
SELECT MAX(salary) AS max_salary
FROM employees;
```

13. Given an `employees` table, write a query to find the minimum salary.

```sql
SELECT MIN(salary) AS min_salary
FROM employees;
```

14. Given an `employees` table, write a query to find the average salary.

```sql
SELECT AVG(salary) AS average_salary
FROM employees;
```

15. Given an `employees` table, write a query to find total salary expense.

```sql
SELECT SUM(salary) AS total_salary
FROM employees;
```

16. Given an `employees` table, write a query to fetch unique department ids.

```sql
SELECT DISTINCT department_id
FROM employees;
```

17. Given a `customers` table, write a query to fetch unique cities.

```sql
SELECT DISTINCT city
FROM customers;
```

18. Given an `orders` table, write a query to fetch the latest `5` orders.

```sql
SELECT order_id, customer_id, order_date, amount
FROM orders
ORDER BY order_date DESC
LIMIT 5;
```

19. Given an `orders` table, write a query to find completed orders.

```sql
SELECT order_id, customer_id, amount, status
FROM orders
WHERE status = 'COMPLETED';
```

20. Given a `products` table, write a query to find products priced above `1000`.

```sql
SELECT product_id, product_name, price
FROM products
WHERE price > 1000;
```

## Medium

21. Given `employees` and `departments`, write a query to fetch employee names with
    department names.

```sql
SELECT e.emp_id, e.emp_name, d.department_name
FROM employees e
JOIN departments d
  ON e.department_id = d.department_id;
```

22. Given `employees` and `departments`, write a query to fetch all employees even if
    they do not have a department.

```sql
SELECT e.emp_id, e.emp_name, d.department_name
FROM employees e
LEFT JOIN departments d
  ON e.department_id = d.department_id;
```

23. Given `departments` and `employees`, write a query to fetch departments that have no
    employees.

```sql
SELECT d.department_id, d.department_name
FROM departments d
LEFT JOIN employees e
  ON d.department_id = e.department_id
WHERE e.emp_id IS NULL;
```

24. Given an `employees` table, write a query to count employees in each department.

```sql
SELECT department_id, COUNT(*) AS employee_count
FROM employees
GROUP BY department_id;
```

25. Given an `employees` table, write a query to find average salary by department.

```sql
SELECT department_id, AVG(salary) AS average_salary
FROM employees
GROUP BY department_id;
```

26. Given an `employees` table, write a query to find departments having more than `5`
    employees.

```sql
SELECT department_id, COUNT(*) AS employee_count
FROM employees
GROUP BY department_id
HAVING COUNT(*) > 5;
```

27. Given an `employees` table, write a query to find departments where average salary is
    greater than `70000`.

```sql
SELECT department_id, AVG(salary) AS average_salary
FROM employees
GROUP BY department_id
HAVING AVG(salary) > 70000;
```

28. Given an `employees` table, write a query to find the highest salary in each
    department.

```sql
SELECT department_id, MAX(salary) AS highest_salary
FROM employees
GROUP BY department_id;
```

29. Given an `employees` table, write a query to find employees earning the highest salary
    in their department.

```sql
SELECT e.emp_id, e.emp_name, e.department_id, e.salary
FROM employees e
JOIN (
    SELECT department_id, MAX(salary) AS highest_salary
    FROM employees
    GROUP BY department_id
) x
  ON e.department_id = x.department_id
 AND e.salary = x.highest_salary;
```

30. Given an `employees` table, write a query to find the second-highest salary.

```sql
SELECT MAX(salary) AS second_highest_salary
FROM employees
WHERE salary < (
    SELECT MAX(salary)
    FROM employees
);
```

31. Given an `employees` table, write a query to find employees with duplicate names.

```sql
SELECT emp_name, COUNT(*) AS duplicate_count
FROM employees
GROUP BY emp_name
HAVING COUNT(*) > 1;
```

32. Given a `customers` table, write a query to find duplicate customer names.

```sql
SELECT customer_name, COUNT(*) AS duplicate_count
FROM customers
GROUP BY customer_name
HAVING COUNT(*) > 1;
```

33. Given `customers` and `orders`, write a query to fetch customers who placed at least
    one order.

```sql
SELECT DISTINCT c.customer_id, c.customer_name
FROM customers c
JOIN orders o
  ON c.customer_id = o.customer_id;
```

34. Given `customers` and `orders`, write a query to fetch customers who never placed an
    order.

```sql
SELECT c.customer_id, c.customer_name
FROM customers c
LEFT JOIN orders o
  ON c.customer_id = o.customer_id
WHERE o.order_id IS NULL;
```

35. Given an `orders` table, write a query to find total order amount by customer.

```sql
SELECT customer_id, SUM(amount) AS total_amount
FROM orders
GROUP BY customer_id;
```

36. Given an `orders` table, write a query to find customers whose total purchase amount
    is above `10000`.

```sql
SELECT customer_id, SUM(amount) AS total_amount
FROM orders
GROUP BY customer_id
HAVING SUM(amount) > 10000;
```

37. Given an `orders` table, write a query to find the latest order date for each
    customer.

```sql
SELECT customer_id, MAX(order_date) AS latest_order_date
FROM orders
GROUP BY customer_id;
```

38. Given an `orders` table, write a query to find each customer's latest order row.

```sql
SELECT o.order_id, o.customer_id, o.order_date, o.amount
FROM orders o
JOIN (
    SELECT customer_id, MAX(order_date) AS latest_order_date
    FROM orders
    GROUP BY customer_id
) x
  ON o.customer_id = x.customer_id
 AND o.order_date = x.latest_order_date;
```

39. Given `orders` and `order_items`, write a query to find total quantity sold per order.

```sql
SELECT o.order_id, SUM(oi.quantity) AS total_quantity
FROM orders o
JOIN order_items oi
  ON o.order_id = oi.order_id
GROUP BY o.order_id;
```

40. Given `products` and `order_items`, write a query to find total quantity sold per
    product.

```sql
SELECT p.product_id, p.product_name, SUM(oi.quantity) AS total_quantity
FROM products p
JOIN order_items oi
  ON p.product_id = oi.product_id
GROUP BY p.product_id, p.product_name;
```

41. Given `products` and `order_items`, write a query to find products that were never
    sold.

```sql
SELECT p.product_id, p.product_name
FROM products p
LEFT JOIN order_items oi
  ON p.product_id = oi.product_id
WHERE oi.product_id IS NULL;
```

42. Given `students` and `marks`, write a query to find total marks for each student.

```sql
SELECT s.student_id, s.student_name, SUM(m.marks) AS total_marks
FROM students s
JOIN marks m
  ON s.student_id = m.student_id
GROUP BY s.student_id, s.student_name;
```

43. Given `students` and `marks`, write a query to find students who failed any subject
    where passing marks are `35`.

```sql
SELECT DISTINCT s.student_id, s.student_name
FROM students s
JOIN marks m
  ON s.student_id = m.student_id
WHERE m.marks < 35;
```

44. Given `marks`, write a query to find the highest marks in each subject.

```sql
SELECT subject, MAX(marks) AS highest_marks
FROM marks
GROUP BY subject;
```

45. Given `marks`, write a query to find students who scored highest marks in each
    subject.

```sql
SELECT m.student_id, m.subject, m.marks
FROM marks m
JOIN (
    SELECT subject, MAX(marks) AS highest_marks
    FROM marks
    GROUP BY subject
) x
  ON m.subject = x.subject
 AND m.marks = x.highest_marks;
```

## Hard

46. Given an `employees` table, write a query to rank employees by salary using
    `ROW_NUMBER`.

```sql
SELECT emp_id,
       emp_name,
       salary,
       ROW_NUMBER() OVER (ORDER BY salary DESC) AS salary_rank
FROM employees;
```

47. Given an `employees` table, write a query to rank employees by salary using `RANK`.

```sql
SELECT emp_id,
       emp_name,
       salary,
       RANK() OVER (ORDER BY salary DESC) AS salary_rank
FROM employees;
```

48. Given an `employees` table, write a query to rank employees by salary using
    `DENSE_RANK`.

```sql
SELECT emp_id,
       emp_name,
       salary,
       DENSE_RANK() OVER (ORDER BY salary DESC) AS salary_rank
FROM employees;
```

49. Given an `employees` table, write a query to find the nth highest distinct salary
    using `DENSE_RANK`.

```sql
WITH ranked_salaries AS (
    SELECT salary,
           DENSE_RANK() OVER (ORDER BY salary DESC) AS salary_rank
    FROM employees
)
SELECT salary
FROM ranked_salaries
WHERE salary_rank = :n;
```

50. Given an `employees` table, write a query to find the top `3` highest-paid employees
    in each department.

```sql
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
WHERE rn <= 3;
```

51. Given an `employees` table, write a query to find employees earning more than their
    department average salary.

```sql
WITH department_avg AS (
    SELECT department_id, AVG(salary) AS avg_salary
    FROM employees
    GROUP BY department_id
)
SELECT e.emp_id, e.emp_name, e.department_id, e.salary
FROM employees e
JOIN department_avg d
  ON e.department_id = d.department_id
WHERE e.salary > d.avg_salary;
```

52. Given an `employees` table, write a query to find employees earning more than their
    manager.

```sql
SELECT e.emp_id,
       e.emp_name,
       e.salary AS employee_salary,
       m.emp_name AS manager_name,
       m.salary AS manager_salary
FROM employees e
JOIN employees m
  ON e.manager_id = m.emp_id
WHERE e.salary > m.salary;
```

53. Given an `employees` table, write a query to find managers with at least `5` direct
    reports.

```sql
SELECT m.emp_id, m.emp_name, COUNT(e.emp_id) AS direct_reports
FROM employees m
JOIN employees e
  ON m.emp_id = e.manager_id
GROUP BY m.emp_id, m.emp_name
HAVING COUNT(e.emp_id) >= 5;
```

54. Given an `employees` table, write a query to find employees who joined in the last
    `30` days.

```sql
SELECT emp_id, emp_name, hire_date
FROM employees
WHERE hire_date >= CURRENT_DATE - INTERVAL '30 days';
```

55. Given an `orders` table, write a query to calculate running total order amount by
    customer.

```sql
SELECT order_id,
       customer_id,
       order_date,
       amount,
       SUM(amount) OVER (
           PARTITION BY customer_id
           ORDER BY order_date
       ) AS running_total
FROM orders;
```

56. Given an `orders` table, write a query to find the previous order amount for each
    customer.

```sql
SELECT order_id,
       customer_id,
       order_date,
       amount,
       LAG(amount) OVER (
           PARTITION BY customer_id
           ORDER BY order_date
       ) AS previous_order_amount
FROM orders;
```

57. Given an `orders` table, write a query to find the next order date for each customer.

```sql
SELECT order_id,
       customer_id,
       order_date,
       LEAD(order_date) OVER (
           PARTITION BY customer_id
           ORDER BY order_date
       ) AS next_order_date
FROM orders;
```

58. Given an `orders` table, write a query to find customers whose order amount increased
    from their previous order.

```sql
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
WHERE amount > previous_amount;
```

59. Given a `logins` table, write a query to find users who logged in on consecutive days.

```sql
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
WHERE login_date = previous_login_date + INTERVAL '1 day';
```

60. Given a `logins` table, write a query to find users with at least `3` consecutive login
    days.

```sql
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
WHERE streak_length >= 3;
```

61. Given an `orders` table, write a query to find each customer's first order.

```sql
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
WHERE rn = 1;
```

62. Given an `orders` table, write a query to find each customer's second order.

```sql
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
WHERE rn = 2;
```

63. Given `orders`, write a query to find customers whose first order amount is greater
    than `5000`.

```sql
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
  AND amount > 5000;
```

64. Given `orders`, write a query to calculate monthly sales.

```sql
SELECT DATE_TRUNC('month', order_date) AS sales_month,
       SUM(amount) AS total_sales
FROM orders
GROUP BY DATE_TRUNC('month', order_date)
ORDER BY sales_month;
```

65. Given `orders`, write a query to calculate month-over-month sales growth.

```sql
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
FROM with_previous;
```

66. Given `orders`, write a query to find the top customer by total sales in each month.

```sql
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
WHERE rn = 1;
```

67. Given `order_items`, write a query to find the top `3` products by revenue.

```sql
SELECT product_id,
       SUM(quantity * unit_price) AS revenue
FROM order_items
GROUP BY product_id
ORDER BY revenue DESC
LIMIT 3;
```

68. Given `products` and `order_items`, write a query to find the top product by revenue
    in each category.

```sql
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
WHERE rn = 1;
```

69. Given `employees`, write a query to find salary gaps between each employee and the
    next higher salary in the same department.

```sql
SELECT emp_id,
       emp_name,
       department_id,
       salary,
       LEAD(salary) OVER (
           PARTITION BY department_id
           ORDER BY salary
       ) - salary AS gap_to_next_salary
FROM employees;
```

70. Given `employees`, write a query to delete duplicate employee rows while keeping the
    lowest `emp_id` for each duplicate name.

```sql
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
);
```

