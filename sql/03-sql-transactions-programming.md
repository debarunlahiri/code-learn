# SQL Transactions, Stored Procedures & Advanced Programming - Complete Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Transactions, Isolation Levels, Locking, Stored Procedures, Functions, Triggers, Cursors, Dynamic SQL, and tricky scenarios.

---

## Table of Contents

1. [Transactions Fundamentals](#transactions-fundamentals)
2. [Isolation Levels](#isolation-levels)
3. [Locking Mechanisms](#locking-mechanisms)
4. [Deadlocks](#deadlocks)
5. [Stored Procedures](#stored-procedures)
6. [User-Defined Functions](#user-defined-functions)
7. [Triggers](#triggers)
8. [Cursors](#cursors)
9. [Dynamic SQL](#dynamic-sql)
10. [Pivoting & Unpivoting](#pivoting-unpivoting)
11. [Recursive CTEs](#recursive-ctes)
12. [Complex Technical Scenarios](#complex-technical-scenarios)
13. [Key Topics & Explanations](#key-topics-explanations)

---

## 1. Transactions Fundamentals

A **transaction** is a unit of work that either **completely succeeds** or **completely fails** — no partial state.

### 1.1 ACID Properties

| Property | Description | Example |
|----------|-------------|---------|
| **Atomicity** | All-or-nothing | Transfer: debit + credit both succeed or both fail |
| **Consistency** | DB moves from one valid state to another | Balance never goes negative |
| **Isolation** | Concurrent transactions don't interfere | Two transfers don't mix up balances |
| **Durability** | Once committed, changes survive crashes | Committed data persists even if server restarts |

### 1.2 Basic Transaction Syntax

```sql
-- Start transaction
BEGIN TRANSACTION; -- or just BEGIN; (varies by DB)

-- Operations
UPDATE accounts SET balance = balance - 1000 WHERE id = 1;
UPDATE accounts SET balance = balance + 1000 WHERE id = 2;

-- If everything is OK
COMMIT; -- Makes changes permanent

-- If something went wrong
ROLLBACK; -- Undoes ALL changes since BEGIN
```

### 1.3 Savepoints

```sql
BEGIN TRANSACTION;

INSERT INTO orders (customer_id, total) VALUES (1, 500);
SAVEPOINT order_saved;

INSERT INTO order_items (order_id, product_id, qty) VALUES (1, 10, 2);
-- Oops, wrong quantity
ROLLBACK TO SAVEPOINT order_saved; -- Only undoes order_items insert

INSERT INTO order_items (order_id, product_id, qty) VALUES (1, 10, 5);
COMMIT; -- Commits order + corrected order_items
```

### 1.4 Auto-Commit

```sql
-- By default, most databases auto-commit each statement
-- Each INSERT/UPDATE/DELETE is its own transaction

-- Disable auto-commit (MySQL)
SET autocommit = 0;

-- Now you must explicitly COMMIT
UPDATE accounts SET balance = balance - 100 WHERE id = 1;
COMMIT; -- Without this, changes aren't saved

-- Re-enable
SET autocommit = 1;
```

---

## 2. Isolation Levels

Control how transactions **see** each other's changes.

### 2.1 Read Phenomena

| Problem | Description |
|---------|-------------|
| **Dirty Read** | Reading uncommitted data from another transaction. If that transaction rolls back, you read data that never existed. |
| **Non-Repeatable Read** | Reading same row twice gives different values because another transaction modified it between reads. |
| **Phantom Read** | Re-executing a query returns NEW rows that didn't exist before, because another transaction inserted them. |

### 2.2 Isolation Levels & What They Prevent

| Isolation Level | Dirty Read | Non-Repeatable Read | Phantom Read | Performance |
|----------------|-----------|-------------------|-------------|-------------|
| **READ UNCOMMITTED** | ❌ Possible | ❌ Possible | ❌ Possible | Fastest |
| **READ COMMITTED** | ✅ Prevented | ❌ Possible | ❌ Possible | Good |
| **REPEATABLE READ** | ✅ Prevented | ✅ Prevented | ❌ Possible | Moderate |
| **SERIALIZABLE** | ✅ Prevented | ✅ Prevented | ✅ Prevented | Slowest |

### 2.3 Setting Isolation Level

```sql
-- MySQL
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;

-- PostgreSQL
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;

-- SQL Server
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;

-- Check current level (MySQL)
SELECT @@transaction_isolation;
```

### 2.4 Practical Scenarios

```sql
-- DIRTY READ scenario
-- Transaction A:
BEGIN;
UPDATE accounts SET balance = 0 WHERE id = 1;
-- (not committed yet)

-- Transaction B (READ UNCOMMITTED):
SELECT balance FROM accounts WHERE id = 1; -- Reads 0 (dirty)

-- Transaction A:
ROLLBACK; -- Balance reverts to original
-- Transaction B used incorrect data!

-- FIX: Use READ COMMITTED or higher
```

```sql
-- NON-REPEATABLE READ scenario
-- Transaction A:
BEGIN;
SELECT balance FROM accounts WHERE id = 1; -- Returns 1000

-- Transaction B:
UPDATE accounts SET balance = 500 WHERE id = 1;
COMMIT;

-- Transaction A:
SELECT balance FROM accounts WHERE id = 1; -- Returns 500 (different!)

-- FIX: Use REPEATABLE READ or higher
```

---

## 3. Locking Mechanisms

### 3.1 Types of Locks

| Lock Type | Description | Compatibility |
|-----------|-------------|---------------|
| **Shared (S)** | For reading — multiple can coexist | Compatible with other Shared |
| **Exclusive (X)** | For writing — blocks everything | Incompatible with everything |
| **Intent Shared (IS)** | Signaling intent to read at row level | - |
| **Intent Exclusive (IX)** | Signaling intent to write at row level | - |

### 3.2 Explicit Locking

```sql
-- SELECT ... FOR UPDATE (Pessimistic Locking)
-- Locks the selected rows — others must wait
BEGIN;
SELECT * FROM accounts WHERE id = 1 FOR UPDATE;
-- Row is locked until this transaction ends
UPDATE accounts SET balance = balance - 100 WHERE id = 1;
COMMIT; -- Lock released

-- SELECT ... FOR SHARE (Shared Lock)
-- Others can read but NOT write
SELECT * FROM products WHERE id = 5 FOR SHARE;
```

### 3.3 Optimistic vs Pessimistic Locking

| Feature | Optimistic | Pessimistic |
|---------|-----------|-------------|
| **Approach** | Check before commit (version column) | Lock rows when reading |
| **When to use** | Low contention, more reads | High contention, many writes |
| **Performance** | Better for reads | Better for writes |
| **Mechanism** | Version number / timestamp | SELECT FOR UPDATE |

```sql
-- Optimistic Locking (using version column)
-- Read
SELECT id, name, balance, version FROM accounts WHERE id = 1;
-- Returns: id=1, balance=1000, version=5

-- Update with version check
UPDATE accounts 
SET balance = 900, version = version + 1
WHERE id = 1 AND version = 5;
-- If affected rows = 0 → someone else modified it → retry
```

---

## 4. Deadlocks

Two (or more) transactions waiting for each other's locks — neither can proceed.

```sql
-- Transaction A:
BEGIN;
UPDATE accounts SET balance = balance - 100 WHERE id = 1; -- Locks row 1
-- Needs to update row 2...

-- Transaction B:
BEGIN;
UPDATE accounts SET balance = balance - 50 WHERE id = 2;  -- Locks row 2
-- Needs to update row 1...

-- Transaction A:
UPDATE accounts SET balance = balance + 100 WHERE id = 2; -- BLOCKED (B has lock)

-- Transaction B:
UPDATE accounts SET balance = balance + 50 WHERE id = 1;  -- BLOCKED (A has lock)

-- DEADLOCK! 🔒 DB detects and kills one transaction (victim)
```

### 4.1 Prevention Strategies

1. **Access resources in the same order** — always update accounts in id order
2. **Keep transactions short** — less time holding locks
3. **Use proper isolation levels** — don't over-isolate
4. **Avoid user interaction within transactions** — never wait for user input
5. **Use deadlock detection timeout** — `SET innodb_lock_wait_timeout = 5;`

---

## 5. Stored Procedures

Precompiled SQL code stored in the database. Executed on the server side.

### 5.1 Basic Stored Procedure

```sql
-- MySQL syntax
DELIMITER //

CREATE PROCEDURE GetEmployeesByDepartment(
    IN dept_name VARCHAR(50)
)
BEGIN
    SELECT e.name, e.salary, d.dept_name
    FROM employees e
    JOIN departments d ON e.dept_id = d.id
    WHERE d.dept_name = dept_name;
END //

DELIMITER ;

-- Call the procedure
CALL GetEmployeesByDepartment('Engineering');
```

### 5.2 Procedure with Output Parameters

```sql
DELIMITER //

CREATE PROCEDURE GetDepartmentStats(
    IN dept_id INT,
    OUT emp_count INT,
    OUT avg_salary DECIMAL(10,2),
    OUT max_salary DECIMAL(10,2)
)
BEGIN
    SELECT COUNT(*), AVG(salary), MAX(salary)
    INTO emp_count, avg_salary, max_salary
    FROM employees
    WHERE department_id = dept_id;
END //

DELIMITER ;

-- Call with output
CALL GetDepartmentStats(1, @count, @avg, @max);
SELECT @count AS total, @avg AS average, @max AS maximum;
```

### 5.3 Procedure with Error Handling

```sql
DELIMITER //

CREATE PROCEDURE TransferFunds(
    IN from_account INT,
    IN to_account INT,
    IN amount DECIMAL(10,2)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'Transfer failed — rolled back' AS status;
    END;
    
    START TRANSACTION;
    
    -- Check balance
    IF (SELECT balance FROM accounts WHERE id = from_account) < amount THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Insufficient funds';
    END IF;
    
    -- Transfer
    UPDATE accounts SET balance = balance - amount WHERE id = from_account;
    UPDATE accounts SET balance = balance + amount WHERE id = to_account;
    
    COMMIT;
    SELECT 'Transfer successful' AS status;
END //

DELIMITER ;
```

### 5.4 Procedure with Conditional Logic

```sql
DELIMITER //

CREATE PROCEDURE CategorizeEmployee(
    IN emp_id INT
)
BEGIN
    DECLARE emp_salary DECIMAL(10,2);
    DECLARE category VARCHAR(20);
    
    SELECT salary INTO emp_salary FROM employees WHERE id = emp_id;
    
    IF emp_salary > 100000 THEN
        SET category = 'Senior';
    ELSEIF emp_salary > 50000 THEN
        SET category = 'Mid-Level';
    ELSE
        SET category = 'Junior';
    END IF;
    
    SELECT emp_id, emp_salary, category;
END //

DELIMITER ;
```

### 5.5 Procedure with Loop

```sql
DELIMITER //

CREATE PROCEDURE GenerateReport()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE dept_name VARCHAR(50);
    DECLARE dept_count INT;
    
    DECLARE dept_cursor CURSOR FOR 
        SELECT d.dept_name, COUNT(e.id)
        FROM departments d
        LEFT JOIN employees e ON d.id = e.dept_id
        GROUP BY d.dept_name;
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    CREATE TEMPORARY TABLE IF NOT EXISTS report (
        department VARCHAR(50),
        employee_count INT
    );
    
    OPEN dept_cursor;
    
    read_loop: LOOP
        FETCH dept_cursor INTO dept_name, dept_count;
        IF done THEN LEAVE read_loop; END IF;
        INSERT INTO report VALUES (dept_name, dept_count);
    END LOOP;
    
    CLOSE dept_cursor;
    
    SELECT * FROM report;
    DROP TEMPORARY TABLE report;
END //

DELIMITER ;
```

---

## 6. User-Defined Functions

### 6.1 Scalar Function (Returns single value)

```sql
DELIMITER //

CREATE FUNCTION CalculateBonus(
    salary DECIMAL(10,2),
    performance_rating INT
) RETURNS DECIMAL(10,2)
DETERMINISTIC
BEGIN
    DECLARE bonus DECIMAL(10,2);
    
    CASE performance_rating
        WHEN 5 THEN SET bonus = salary * 0.20;
        WHEN 4 THEN SET bonus = salary * 0.15;
        WHEN 3 THEN SET bonus = salary * 0.10;
        ELSE SET bonus = salary * 0.05;
    END CASE;
    
    RETURN bonus;
END //

DELIMITER ;

-- Usage
SELECT name, salary, CalculateBonus(salary, rating) AS bonus
FROM employees;
```

### 6.2 Stored Procedure vs Function

| Feature | Stored Procedure | Function |
|---------|-----------------|----------|
| **Return** | Can return 0 or many values (OUT params) | MUST return exactly 1 value |
| **Call from SQL** | ❌ Cannot use in SELECT | ✅ Can use in SELECT/WHERE |
| **DML operations** | ✅ INSERT/UPDATE/DELETE allowed | ❌ Not recommended (varies by DB) |
| **Transaction** | ✅ Can manage transactions | ❌ Cannot (typically) |
| **Invocation** | `CALL procedure_name()` | Used in expressions |

---

## 7. Triggers

Automatically executed SQL code when a specific event occurs (INSERT, UPDATE, DELETE).

### 7.1 BEFORE Trigger (Validation)

```sql
DELIMITER //

CREATE TRIGGER before_employee_insert
BEFORE INSERT ON employees
FOR EACH ROW
BEGIN
    -- Validate salary
    IF NEW.salary < 0 THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Salary cannot be negative';
    END IF;
    
    -- Auto-set created_at
    SET NEW.created_at = NOW();
    
    -- Capitalize name
    SET NEW.name = CONCAT(
        UPPER(LEFT(NEW.name, 1)), 
        LOWER(SUBSTRING(NEW.name, 2))
    );
END //

DELIMITER ;
```

### 7.2 AFTER Trigger (Audit Logging)

```sql
DELIMITER //

CREATE TRIGGER after_salary_update
AFTER UPDATE ON employees
FOR EACH ROW
BEGIN
    IF OLD.salary <> NEW.salary THEN
        INSERT INTO salary_audit (
            employee_id,
            old_salary,
            new_salary,
            changed_at,
            changed_by
        ) VALUES (
            NEW.id,
            OLD.salary,
            NEW.salary,
            NOW(),
            CURRENT_USER()
        );
    END IF;
END //

DELIMITER ;
```

### 7.3 DELETE Trigger (Archive)

```sql
DELIMITER //

CREATE TRIGGER before_employee_delete
BEFORE DELETE ON employees
FOR EACH ROW
BEGIN
    INSERT INTO employees_archive (
        id, name, salary, department_id, deleted_at
    ) VALUES (
        OLD.id, OLD.name, OLD.salary, OLD.department_id, NOW()
    );
END //

DELIMITER ;
```

### 7.4 Trigger — Key Points

- **BEFORE**: Can modify NEW values, validate, reject with SIGNAL
- **AFTER**: Cannot modify NEW/OLD, good for logging/auditing
- **NEW**: Refers to the new row (INSERT/UPDATE)
- **OLD**: Refers to the old row (UPDATE/DELETE)
- **Avoid**: Heavy logic, recursive triggers, calling procedures excessively

---

## 8. Cursors

Row-by-row processing of query results. Use only when set-based operations are insufficient.

```sql
DELIMITER //

CREATE PROCEDURE UpdateEmployeeGrades()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE emp_id INT;
    DECLARE emp_salary DECIMAL(10,2);
    DECLARE new_grade VARCHAR(1);
    
    -- Declare cursor
    DECLARE emp_cursor CURSOR FOR
        SELECT id, salary FROM employees;
    
    -- Handler for end of result set
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    OPEN emp_cursor;
    
    process_loop: LOOP
        FETCH emp_cursor INTO emp_id, emp_salary;
        IF done THEN LEAVE process_loop; END IF;
        
        -- Determine grade
        SET new_grade = CASE
            WHEN emp_salary > 80000 THEN 'A'
            WHEN emp_salary > 50000 THEN 'B'
            WHEN emp_salary > 30000 THEN 'C'
            ELSE 'D'
        END;
        
        UPDATE employees SET grade = new_grade WHERE id = emp_id;
    END LOOP;
    
    CLOSE emp_cursor;
END //

DELIMITER ;
```

### 8.1 Why Avoid Cursors?

- **Slow**: Row-by-row processing vs. set-based operations
- **Memory**: Holds result set in memory
- **Locks**: Can hold locks for extended periods
- **Alternative**: Window functions, CTEs, CASE expressions handle most cases better

---

## 9. Dynamic SQL

Build and execute SQL statements at runtime.

```sql
DELIMITER //

CREATE PROCEDURE SearchEmployees(
    IN search_name VARCHAR(50),
    IN search_dept VARCHAR(50),
    IN min_salary DECIMAL(10,2)
)
BEGIN
    SET @sql = 'SELECT * FROM employees WHERE 1=1';
    
    IF search_name IS NOT NULL THEN
        SET @sql = CONCAT(@sql, ' AND name LIKE ', QUOTE(CONCAT('%', search_name, '%')));
    END IF;
    
    IF search_dept IS NOT NULL THEN
        SET @sql = CONCAT(@sql, ' AND department = ', QUOTE(search_dept));
    END IF;
    
    IF min_salary IS NOT NULL THEN
        SET @sql = CONCAT(@sql, ' AND salary >= ', min_salary);
    END IF;
    
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END //

DELIMITER ;

-- ⚠️ IMPORTANT: Always use QUOTE() or prepared statements to prevent SQL Injection!
```

---

## 10. Pivoting & Unpivoting

### 10.1 PIVOT (Rows to Columns)

```sql
-- Data: sales table (month, product, amount)

-- MySQL (using CASE)
SELECT 
    product,
    SUM(CASE WHEN month = 'Jan' THEN amount ELSE 0 END) AS Jan,
    SUM(CASE WHEN month = 'Feb' THEN amount ELSE 0 END) AS Feb,
    SUM(CASE WHEN month = 'Mar' THEN amount ELSE 0 END) AS Mar
FROM sales
GROUP BY product;

-- SQL Server (native PIVOT)
SELECT * FROM (
    SELECT product, month, amount FROM sales
) AS src
PIVOT (
    SUM(amount) FOR month IN ([Jan], [Feb], [Mar])
) AS pvt;
```

### 10.2 UNPIVOT (Columns to Rows)

```sql
-- SQL Server
SELECT product, month, amount
FROM quarterly_sales
UNPIVOT (
    amount FOR month IN (Q1, Q2, Q3, Q4)
) AS unpvt;

-- MySQL (using UNION ALL)
SELECT product, 'Q1' AS quarter, q1 AS amount FROM quarterly_sales
UNION ALL
SELECT product, 'Q2', q2 FROM quarterly_sales
UNION ALL
SELECT product, 'Q3', q3 FROM quarterly_sales
UNION ALL
SELECT product, 'Q4', q4 FROM quarterly_sales;
```

---

## 11. Recursive CTEs

### 11.1 Employee Hierarchy (Manager-Employee Tree)

```sql
WITH RECURSIVE emp_hierarchy AS (
    -- Base case: Top-level managers (no manager)
    SELECT id, name, manager_id, 1 AS level, 
           CAST(name AS CHAR(500)) AS path
    FROM employees
    WHERE manager_id IS NULL
    
    UNION ALL
    
    -- Recursive case: Employees with managers
    SELECT e.id, e.name, e.manager_id, h.level + 1,
           CONCAT(h.path, ' → ', e.name)
    FROM employees e
    INNER JOIN emp_hierarchy h ON e.manager_id = h.id
)
SELECT id, name, level, path
FROM emp_hierarchy
ORDER BY path;

-- Output:
-- | id | name    | level | path                        |
-- |----|---------|-------|-----------------------------|
-- | 1  | CEO     | 1     | CEO                         |
-- | 2  | VP Eng  | 2     | CEO → VP Eng                |
-- | 4  | Dev Lead| 3     | CEO → VP Eng → Dev Lead     |
-- | 3  | VP Sales| 2     | CEO → VP Sales              |
```

### 11.2 Generate Number Series

```sql
WITH RECURSIVE numbers AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 100
)
SELECT n FROM numbers;
-- Generates 1 to 100
```

### 11.3 Find All Subordinates of a Manager

```sql
WITH RECURSIVE subordinates AS (
    SELECT id, name, manager_id
    FROM employees
    WHERE id = 5  -- Starting manager
    
    UNION ALL
    
    SELECT e.id, e.name, e.manager_id
    FROM employees e
    INNER JOIN subordinates s ON e.manager_id = s.id
)
SELECT * FROM subordinates;
```

---

## 12. Complex Technical Scenarios

### 12.1 What happens if you don't COMMIT a transaction?

**Explanation:**
- If the session ends or connection closes, the database automatically **ROLLBACK**s uncommitted transactions.
- Other sessions cannot see uncommitted changes (depending on isolation level).
- Resources (locks, memory) are held until COMMIT or ROLLBACK.
- **Always** explicitly COMMIT or ROLLBACK — never leave transactions open.

### 12.2 Can a trigger call another trigger?

**Explanation:** Yes — this is called **cascading triggers**. For example, an INSERT trigger on table A inserts into table B, which fires table B's INSERT trigger. This can lead to:
- Infinite loops (if circular)
- Performance problems
- Hard-to-debug behavior
Most databases have limits on nesting depth (e.g., MySQL 0-level, SQL Server 32 levels).

### 12.3 Stored Procedure vs Inline SQL — which is better?

**Explanation:**
- **Stored Procedures**: Precompiled, cached execution plan, reduced network traffic, security (no direct table access), but harder to version control and debug.
- **Inline SQL** (via ORM or code): Easier to maintain, version control, debug, and test. Modern ORMs handle parameterization and optimization.
- **Current trend**: Applications use ORMs or query builders. Stored Procedures used for complex batch operations, reporting, or legacy systems.

### 12.4 What is a Race Condition in SQL?

**Explanation:** Two concurrent transactions read the same data and make decisions based on stale reads, leading to incorrect results.
```sql
-- Both transactions read balance = 1000
-- Transaction A: balance - 500 = 500 → UPDATE to 500
-- Transaction B: balance - 300 = 700 → UPDATE to 700
-- Final balance: 700 (should be 200!)
-- FIX: SELECT FOR UPDATE or proper isolation level
```

---

## 13. Key Topics & Explanations

### 13.1 What is the default isolation level?

**Explanation:**
- **MySQL (InnoDB)**: REPEATABLE READ
- **PostgreSQL**: READ COMMITTED
- **SQL Server**: READ COMMITTED
- **Oracle**: READ COMMITTED
Most applications work fine with READ COMMITTED.

### 13.2 DELIMITER — why is it needed in MySQL stored procedures?

**Explanation:** MySQL uses `;` to end statements. Inside a procedure, there are multiple `;` semicolons. Setting `DELIMITER //` tells MySQL to treat `//` as the end of the entire procedure definition (not each `;` inside it). After the procedure, reset with `DELIMITER ;`.

### 13.3 What is SQL Injection and how to prevent it?

**Explanation:**
Attacker injects malicious SQL through user input:
```sql
-- Input: ' OR '1'='1
SELECT * FROM users WHERE name = '' OR '1'='1'; -- Returns ALL users
```
**Prevention:**
1. **Parameterized queries** (prepared statements) — #1 defense
2. Input validation and sanitization
3. Least privilege principle (limited DB user permissions)
4. Stored Procedures (with parameterized inputs)

### 13.4 TRUNCATE vs DELETE in a transaction?

**Explanation:**
- **DELETE**: Can be rolled back (logged operation). Fires triggers. Slower for large tables.
- **TRUNCATE**: Cannot be rolled back in most databases (DDL operation in MySQL). Does NOT fire triggers. Resets auto-increment. Much faster.

Note: PostgreSQL DOES support TRUNCATE inside transactions.
