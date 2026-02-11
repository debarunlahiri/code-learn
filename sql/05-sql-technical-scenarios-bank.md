# SQL Technical Proficiency Bank - Ultimate Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Purpose:** A comprehensive repository of 100+ technical inquiries and scenarios covering Database concepts, complex queries, performance tuning, and coding problems.

---

## Table of Contents

1. [Core Database Concepts](#1-core-database-concepts)
2. [SQL Statements & Operations](#2-sql-statements--operations)
3. [Joins & Relationships](#3-joins--relationships)
4. [Advanced Queries](#4-advanced-queries)
5. [Database Design & Normalization](#5-database-design--normalization)
6. [Performance Tuning & Indexing](#6-performance-tuning--indexing)
7. [Stored Procedures & Triggers](#7-stored-procedures--triggers)
8. [Window Functions - Deep Dive](#8-window-functions---deep-dive)
9. [Subqueries & CTEs - Deep Dive](#9-subqueries--ctes---deep-dive)
10. [Data Types & Conversions](#10-data-types--conversions)
11. [Transactions & Locking - Deep Dive](#11-transactions--locking---deep-dive)
12. [Coding Problems (Practical Queries)](#12-coding-problems-practical-queries)
13. [Tricky Output & Gotcha Questions](#13-tricky-output--gotcha-questions)
14. [Interview Questions & Answers](#14-interview-questions--answers)

---

## 1. Core Database Concepts

### Inquiry 1: ACID Properties
**Scenario:** Explain Atomicity in detail.
**Explanation:**
*   **A**tomicity: All-or-nothing. Transaction is a single unit. If one part fails, entire transaction rolls back.
*   **C**onsistency: DB moves from one valid state to another. Constraints (FK, Check) are satisfied.
*   **I**solation: Concurrent transactions are invisible to each other until commit.
*   **D**urability: Committed data is permanent (HDD/SSD).

### Inquiry 2: DELETE vs TRUNCATE vs DROP
**Scenario:** Explain Rollback capability.
**Explanation:**
*   **DELETE**: DML. Can rollback. Logs each row. Slower. Triggers fired.
*   **TRUNCATE**: DDL. Cannot rollback (Oracle/MySQL). Minimal logging (Page deallocation). Faster. Identity Reset. No Triggers.
*   **DROP**: DDL. Removes Table Structure + Data. Cannot rollback.

### Inquiry 3: View vs Materialized View
**Scenario:** Difference?
**Explanation:**
*   **View**: Virtual table. Query is executed every time view is accessed. No storage. Always real-time.
*   **Materialized View**: Physical copy of data. Query executed once. Storage used. Must be **refreshed** (on commit or schedule). Faster retrieval.

### Inquiry 4: Primary Key vs Unique Key
**Scenario:** Can PK be NULL?
**Explanation:**
*   **PK**: Not Null + Unique. Only 1 per table. Clustered Index (Default).
*   **Unique**: Allows 1 NULL (in SQL Server, unlimited in Postgres/Oracle). Multiple per table. Non-Clustered Index.

### Inquiry 5: Candidate Key vs Super Key
**Scenario:** Definition?
**Explanation:**
*   **Super Key**: Set of columns that uniquely identifies a row.
*   **Candidate Key**: Minimal Super Key (No redundant columns). PK is chosen from Candidates.

---

## 2. SQL Statements & Operations

### Inquiry 6: Constraints
**Scenario:** List types.
**Explanation:** `NOT NULL`, `UNIQUE`, `PRIMARY KEY`, `FOREIGN KEY`, `CHECK`, `DEFAULT`.

### Inquiry 7: `UNION` vs `UNION ALL`
**Scenario:** Performance?
**Explanation:** `UNION` removes duplicates (Sort operation = Slow). `UNION ALL` keeps duplicates (Append operation = Fast).

### Inquiry 8: `WHERE` vs `HAVING`
**Scenario:** Execution order?
**Explanation:**
*   `WHERE`: Before Grouping. Filters rows. Cannot use aggregates (`SUM`, `COUNT`).
*   `HAVING`: After Grouping. Filters groups. Can use aggregates.

### Inquiry 9: `RANK` vs `DENSE_RANK` vs `ROW_NUMBER`
**Scenario:** Explain output for values: 10, 20, 20, 30.
**Explanation:**
*   **ROW_NUMBER**: 1, 2, 3, 4 (Unique, sequential).
*   **RANK**: 1, 2, 2, 4 (Skips number for tie).
*   **DENSE_RANK**: 1, 2, 2, 3 (No skip).

---

## 3. Joins & Relationships

### Inquiry 10: Inner Join
**Scenario:** Venn Diagram?
**Explanation:** Intersection (A ∩ B). Only common rows.

### Inquiry 11: Left Join vs Right Join
**Scenario:** Explain NULLs.
**Explanation:**
*   **Left**: All from Left Table + Match from Right. (NULL for no match).
*   **Right**: All from Right Table + Match from Left.

### Inquiry 12: Cross Join
**Scenario:** Result count?
**Explanation:** Cartesian Product. Rows(A) * Rows(B). If A has 10, B has 5 -> Result 50.

### Inquiry 13: Self Join
**Scenario:** Usage?
**Explanation:** Joining table to itself. Use aliases (E1, E2). Finding Employee-Manager hierarchy.

### Inquiry 14: Full Outer Join
**Scenario:** Handling non-matching rows?
**Explanation:** Returns all rows from both tables. NULLs on both sides where mismatch occurs.

---

## 4. Advanced Queries

### Inquiry 15: Nth Highest Salary
**Scenario:** Query?
**Explanation:**
*   `SELECT salary FROM (SELECT salary, DENSE_RANK() OVER (ORDER BY salary DESC) as rnk FROM Emp) WHERE rnk = N;`
*   `LIMIT 1 OFFSET (N-1)` (MySQL).

### Inquiry 16: Duplicate Records
**Scenario:** Find duplicates.
**Explanation:**
*   `SELECT email, COUNT(*) FROM Users GROUP BY email HAVING COUNT(*) > 1;`

### Inquiry 17: Delete Duplicates
**Scenario:** Keep one.
**Explanation:** CTE + ROW_NUMBER.
*   `WITH CTE AS (SELECT *, ROW_NUMBER() OVER(PARTITION BY email ORDER BY id) as rn FROM Users) DELETE FROM CTE WHERE rn > 1;`

### Inquiry 18: `CASE` Statement
**Scenario:** If-Else logic in Select.
**Explanation:**
*   `CASE WHEN age >= 18 THEN 'Adult' ELSE 'Minor' END`.

### Inquiry 19: `COALESCE`
**Scenario:** Usage?
**Explanation:** Returns first NON-NULL value. `COALESCE(phone, mobile, 'No Number')`.

### Inquiry 20: `EXISTS` vs `IN`
**Scenario:** Performance?
**Explanation:**
*   `EXISTS`: Terminates as soon as match found (Short circuit). Better for large subqueries.
*   `IN`: Scans entire list. Better for small lists.

### Inquiry 21: Recursive CTE
**Scenario:** Hierarchical data?
**Explanation:** Querying tree structures (Org Chart, Category Tree). Typically uses `UNION ALL` inside CTE.

---

## 5. Database Design & Normalization

### Inquiry 22: First Normal Form (1NF)
**Scenario:** Requirement?
**Explanation:** Atomic values. No repeating groups/arrays within a single column.

### Inquiry 23: Second Normal Form (2NF)
**Scenario:** Requirement?
**Explanation:** 1NF + No Partial Dependency. (Non-key columns must depend on the **entire** composite PK).

### Inquiry 24: Third Normal Form (3NF)
**Scenario:** Requirement?
**Explanation:** 2NF + No Transitive Dependency. (Non-key columns must allow depend **only** on PK, not non-key columns).

### Inquiry 25: BCNF (Boyce-Codd)
**Scenario:** Definition?
**Explanation:** 3NF + For every functional dependency X -> Y, X must be a Super Key. Stronger version of 3NF.

### Inquiry 26: Denormalization
**Scenario:** Why do it?
**Explanation:** Performance optimization for Read-Heavy systems (Data Warehouse). Reduces Joins. Increases Redundancy.

---

## 6. Performance Tuning & Indexing

### Inquiry 27: Index
**Scenario:** Data Structure?
**Explanation:** B-Tree (Balanced Tree). Allows O(log n) search instead of O(n) scan.

### Inquiry 28: Clustered Index
**Scenario:** Limit?
**Explanation:** Only 1 per table. Physically sorts data rows. (Phonebook). Leaf nodes contain actual data.

### Inquiry 29: Non-Clustered Index
**Scenario:** Limit?
**Explanation:** Multiple allowed. Logical order. Leaf nodes contain pointers to data. (Book Index).

### Inquiry 30: Composite Index
**Scenario:** Order of columns?
**Explanation:** Crucial. `INDEX(A, B)` helps queries with `WHERE A=..` or `WHERE A=.. AND B=..`. Does **NOT** help `WHERE B=..` (Leftmost Prefix Rule).

### Inquiry 31: Index Scan vs Index Seek
**Scenario:** Which is better?
**Explanation:**
*   **Seek**: Direct lookup of specific rows using index. Fast.
*   **Scan**: Reading entire index/table. Slow.

### Inquiry 32: SARGable Queries
**Scenario:** Avoid functions on indexed columns.
**Explanation:**
*   Bad: `WHERE YEAR(date_col) = 2023`. (Index ignored).
*   Good: `WHERE date_col >= '2023-01-01' AND date_col < '2024-01-01'`. (Index used).

### Inquiry 33: Isolation Levels
**Scenario:** Default?
**Explanation:**
*   **Read Uncommitted**: Dirty Reads allowed.
*   **Read Committed**: Default (Oracle/Postgres). No Dirty reads.
*   **Repeatable Read**: Default (MySQL). No Non-Repeatable reads.
*   **Serializable**: Strict. No Phantom reads. Slowest.

### Inquiry 34: Deadlock in SQL
**Scenario:** Prevention?
**Explanation:** Access tables in same order. Keep transactions short. Use consistent locking hints.

---

## 7. Stored Procedures & Triggers

### Inquiry 35: Stored Procedure
**Scenario:** Benefits vs SQL Query?
**Explanation:**
*   **Precompiled**: Plan cached. Faster execution.
*   **Security**: Grants execute permission without table access.
*   **Network**: Reduces traffic (1 call vs multiple).

### Inquiry 36: Trigger
**Scenario:** Types?
**Explanation:** `BEFORE` / `AFTER` (Insert, Update, Delete). Example: Audit logging.

### Inquiry 37: Cursor
**Scenario:** Usage?
**Explanation:** Row-by-row processing. Slow. Use Set-based operations (`UPDATE table SET...`) whenever possible.

### Inquiry 38: Function vs Procedure
**Scenario:** Difference?
**Explanation:**
*   **Function**: Must return value. Can be used in SELECT. Cannot change DB state (DML) generally.
*   **Procedure**: Can return multiple values (OUT parameters). Cannot be used in SELECT. Can execute DML.

### Inquiry 39: SQL Injection
**Scenario:** Prevention?
**Explanation:** Use **Prepared Statements** (Parameterized Queries). Do not concatenate strings.

### Inquiry 40: Pivot
**Scenario:** Concept?
**Explanation:** Rotating rows into columns. Useful for reporting (e.g., Monthly sales sideways).

---

## 8. Window Functions - Deep Dive

### Inquiry 41: What are Window Functions?
**Scenario:** How do they differ from GROUP BY?
**Explanation:**
Window functions perform calculations across a set of rows **related to the current row** without collapsing them into a single output row (unlike GROUP BY).

```sql
-- GROUP BY: Collapses rows (1 row per department)
SELECT dept, AVG(salary) FROM Emp GROUP BY dept;

-- Window Function: Keeps all rows, adds computed column
SELECT name, dept, salary,
       AVG(salary) OVER (PARTITION BY dept) AS dept_avg
FROM Emp;
```

### Inquiry 42: PARTITION BY vs GROUP BY
**Scenario:** When to use which?
**Explanation:**

| Feature | GROUP BY | PARTITION BY |
|---------|----------|-------------|
| **Output rows** | One per group | All original rows retained |
| **Use with** | Aggregate functions | Window functions |
| **Access to individual rows** | No | Yes |
| **Can filter with** | HAVING | Cannot use HAVING |

### Inquiry 43: ROW_NUMBER, RANK, DENSE_RANK - Practical
**Scenario:** Given this data, what is the output?

```sql
CREATE TABLE Sales (id INT, name VARCHAR(50), amount INT);
INSERT INTO Sales VALUES (1,'Rahul',500),(2,'Amit',700),(3,'Sneha',700),(4,'Priya',300),(5,'Ravi',500);

SELECT name, amount,
    ROW_NUMBER() OVER (ORDER BY amount DESC) AS row_num,
    RANK()       OVER (ORDER BY amount DESC) AS rnk,
    DENSE_RANK() OVER (ORDER BY amount DESC) AS dense_rnk
FROM Sales;
```

**Output:**

| name  | amount | row_num | rnk | dense_rnk |
|-------|--------|---------|-----|-----------|
| Amit  | 700    | 1       | 1   | 1         |
| Sneha | 700    | 2       | 1   | 1         |
| Rahul | 500    | 3       | 3   | 2         |
| Ravi  | 500    | 4       | 3   | 2         |
| Priya | 300    | 5       | 5   | 3         |

### Inquiry 44: LEAD and LAG
**Scenario:** Compare current row with previous/next row.
**Explanation:**
*   `LAG(col, offset, default)` — Access **previous** row's value.
*   `LEAD(col, offset, default)` — Access **next** row's value.

```sql
-- Find salary difference from previous employee (ordered by hire date)
SELECT name, salary,
    LAG(salary, 1, 0) OVER (ORDER BY hire_date) AS prev_salary,
    salary - LAG(salary, 1, 0) OVER (ORDER BY hire_date) AS diff
FROM Emp;

-- Find next month's sales for comparison
SELECT month, revenue,
    LEAD(revenue, 1) OVER (ORDER BY month) AS next_month_revenue
FROM MonthlySales;
```

### Inquiry 45: FIRST_VALUE, LAST_VALUE, NTH_VALUE
**Scenario:** Get specific positional values within a window.
**Explanation:**

```sql
SELECT name, dept, salary,
    FIRST_VALUE(name) OVER (PARTITION BY dept ORDER BY salary DESC) AS highest_paid,
    LAST_VALUE(name) OVER (
        PARTITION BY dept ORDER BY salary DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) AS lowest_paid,
    NTH_VALUE(name, 2) OVER (PARTITION BY dept ORDER BY salary DESC) AS second_highest
FROM Emp;
```

**Gotcha:** `LAST_VALUE` default frame is `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW` — you must explicitly set `UNBOUNDED FOLLOWING` to get the actual last value.

### Inquiry 46: NTILE
**Scenario:** Divide rows into N equal buckets.
**Explanation:**

```sql
-- Divide employees into 4 salary quartiles
SELECT name, salary,
    NTILE(4) OVER (ORDER BY salary) AS quartile
FROM Emp;
-- Quartile 1 = lowest 25%, Quartile 4 = highest 25%
```

### Inquiry 47: Running Total / Cumulative Sum
**Scenario:** Calculate running total of sales.
**Explanation:**

```sql
SELECT order_date, amount,
    SUM(amount) OVER (ORDER BY order_date) AS running_total,
    SUM(amount) OVER (ORDER BY order_date ROWS BETWEEN 2 PRECEDING AND CURRENT ROW) AS moving_3day_sum,
    AVG(amount) OVER (ORDER BY order_date ROWS BETWEEN 6 PRECEDING AND CURRENT ROW) AS moving_7day_avg
FROM Orders;
```

### Inquiry 48: Window Frame Clauses
**Scenario:** Explain ROWS vs RANGE.
**Explanation:**

| Clause | Meaning |
|--------|---------|
| `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW` | All rows from start to current (default for ORDER BY) |
| `ROWS BETWEEN 1 PRECEDING AND 1 FOLLOWING` | Previous row + current + next row |
| `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING` | Entire partition |
| `RANGE BETWEEN ...` | Based on **value** range, not row count |

```sql
-- ROWS: counts physical rows
-- RANGE: considers value equality (groups same values together)
SELECT val,
    SUM(val) OVER (ORDER BY val ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS rows_sum,
    SUM(val) OVER (ORDER BY val RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS range_sum
FROM test;
-- For duplicate values, RANGE includes ALL rows with same value; ROWS does not
```

### Inquiry 49: PERCENT_RANK and CUME_DIST
**Scenario:** Statistical ranking.
**Explanation:**
*   `PERCENT_RANK()` = (rank - 1) / (total_rows - 1). Range: 0 to 1.
*   `CUME_DIST()` = rows with value <= current / total_rows. Range: > 0 to 1.

```sql
SELECT name, salary,
    PERCENT_RANK() OVER (ORDER BY salary) AS pct_rank,
    CUME_DIST() OVER (ORDER BY salary) AS cum_dist
FROM Emp;
```

---

## 9. Subqueries & CTEs - Deep Dive

### Inquiry 50: Types of Subqueries
**Scenario:** Classification?
**Explanation:**

| Type | Description | Example Location |
|------|-------------|-----------------|
| **Scalar** | Returns single value (1 row, 1 col) | SELECT, WHERE |
| **Row** | Returns single row (multiple cols) | WHERE |
| **Table** | Returns multiple rows & cols | FROM (derived table) |
| **Correlated** | References outer query | WHERE, SELECT |
| **Non-correlated** | Independent of outer query | Anywhere |

### Inquiry 51: Correlated vs Non-Correlated Subquery
**Scenario:** Performance difference?
**Explanation:**

```sql
-- Non-correlated: Executes ONCE, result reused
SELECT * FROM Emp WHERE dept_id IN (SELECT id FROM Dept WHERE location = 'Mumbai');

-- Correlated: Executes ONCE PER ROW of outer query (slower)
SELECT e.name, e.salary,
    (SELECT AVG(salary) FROM Emp e2 WHERE e2.dept_id = e.dept_id) AS dept_avg
FROM Emp e;
```

**Performance:** Non-correlated is generally faster. Correlated can be rewritten using JOINs for better performance.

### Inquiry 52: CTE (Common Table Expression)
**Scenario:** Syntax and benefits?
**Explanation:**

```sql
WITH HighEarners AS (
    SELECT name, salary, dept_id
    FROM Emp
    WHERE salary > 50000
)
SELECT h.name, h.salary, d.dept_name
FROM HighEarners h
JOIN Dept d ON h.dept_id = d.id;
```

**Benefits:**
*   Readable (named temporary result set).
*   Can reference itself (Recursive CTE).
*   Can be referenced multiple times in the same query.
*   Scope: Only within the immediately following SELECT/INSERT/UPDATE/DELETE.

### Inquiry 53: Recursive CTE - Practical
**Scenario:** Generate numbers 1 to 10.
**Explanation:**

```sql
WITH RECURSIVE Numbers AS (
    SELECT 1 AS n          -- Anchor member
    UNION ALL
    SELECT n + 1 FROM Numbers WHERE n < 10  -- Recursive member
)
SELECT n FROM Numbers;
-- Output: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
```

### Inquiry 54: Recursive CTE - Employee Hierarchy
**Scenario:** Find all reports under a manager.
**Explanation:**

```sql
WITH RECURSIVE EmpHierarchy AS (
    -- Anchor: Start with the top manager
    SELECT id, name, manager_id, 1 AS level
    FROM Emp WHERE manager_id IS NULL

    UNION ALL

    -- Recursive: Find direct reports
    SELECT e.id, e.name, e.manager_id, eh.level + 1
    FROM Emp e
    JOIN EmpHierarchy eh ON e.manager_id = eh.id
)
SELECT id, name, level,
    LPAD(' ', (level - 1) * 4) || name AS hierarchy
FROM EmpHierarchy
ORDER BY level, name;
```

### Inquiry 55: CTE vs Subquery vs Temp Table vs View
**Scenario:** When to use which?
**Explanation:**

| Feature | CTE | Subquery | Temp Table | View |
|---------|-----|----------|-----------|------|
| **Scope** | Single query | Single query | Session | Permanent |
| **Reusable in query** | Yes (multiple refs) | No | Yes | Yes |
| **Indexed** | No | No | Yes | No (unless materialized) |
| **Recursive** | Yes | No | No | No |
| **Performance** | Optimizer may inline | Same as CTE | Better for large data | Depends |
| **Best for** | Readability, recursion | Simple filters | Large intermediate results | Reusable logic |

### Inquiry 56: Subquery in Different Clauses
**Scenario:** Show subquery usage in SELECT, FROM, WHERE, HAVING.
**Explanation:**

```sql
-- In SELECT (scalar subquery)
SELECT name, salary,
    (SELECT AVG(salary) FROM Emp) AS company_avg
FROM Emp;

-- In FROM (derived table / inline view)
SELECT dept_name, avg_sal
FROM (SELECT dept_id, AVG(salary) AS avg_sal FROM Emp GROUP BY dept_id) AS DeptAvg
JOIN Dept d ON DeptAvg.dept_id = d.id;

-- In WHERE
SELECT * FROM Emp WHERE salary > (SELECT AVG(salary) FROM Emp);

-- In HAVING
SELECT dept_id, AVG(salary) AS avg_sal
FROM Emp
GROUP BY dept_id
HAVING AVG(salary) > (SELECT AVG(salary) FROM Emp);
```

### Inquiry 57: EXISTS vs IN vs JOIN for Subqueries
**Scenario:** Which performs best?
**Explanation:**

```sql
-- Using IN (loads entire subquery result into memory)
SELECT * FROM Orders WHERE customer_id IN (SELECT id FROM Customers WHERE city = 'Mumbai');

-- Using EXISTS (short-circuits on first match — better for large subqueries)
SELECT * FROM Orders o WHERE EXISTS (SELECT 1 FROM Customers c WHERE c.id = o.customer_id AND c.city = 'Mumbai');

-- Using JOIN (often best — optimizer can choose optimal plan)
SELECT o.* FROM Orders o JOIN Customers c ON o.customer_id = c.id WHERE c.city = 'Mumbai';
```

| Method | Best When |
|--------|-----------|
| **IN** | Small subquery result set |
| **EXISTS** | Large subquery, small outer query |
| **JOIN** | General purpose, optimizer-friendly |

---

## 10. Data Types & Conversions

### Inquiry 58: Common SQL Data Types
**Scenario:** List important data types.
**Explanation:**

| Category | Types | Notes |
|----------|-------|-------|
| **Integer** | `TINYINT`, `SMALLINT`, `INT`, `BIGINT` | TINYINT: 0-255, INT: ~2.1 billion |
| **Decimal** | `DECIMAL(p,s)`, `NUMERIC(p,s)` | Exact. Use for money. `DECIMAL(10,2)` = 8 digits + 2 decimal |
| **Float** | `FLOAT`, `DOUBLE`, `REAL` | Approximate. NOT for money (rounding errors) |
| **String** | `CHAR(n)`, `VARCHAR(n)`, `TEXT` | CHAR: fixed-length (padded). VARCHAR: variable-length |
| **Date/Time** | `DATE`, `TIME`, `DATETIME`, `TIMESTAMP` | TIMESTAMP auto-converts timezone (MySQL) |
| **Boolean** | `BOOLEAN`, `BIT` | MySQL: TINYINT(1). SQL Server: BIT |
| **Binary** | `BLOB`, `VARBINARY` | For images, files |

### Inquiry 59: CHAR vs VARCHAR
**Scenario:** When to use CHAR?
**Explanation:**
*   **CHAR(10)**: Always stores 10 bytes (padded with spaces). Faster for fixed-length data.
*   **VARCHAR(10)**: Stores actual length + 1-2 bytes overhead. Better for variable-length data.
*   Use `CHAR` for: country codes (`IN`, `US`), gender (`M`/`F`), status codes.
*   Use `VARCHAR` for: names, emails, addresses.

```sql
-- Gotcha: CHAR pads with spaces
SELECT LENGTH(CHAR_COL) FROM test; -- Always returns 10 for CHAR(10)
SELECT LENGTH(TRIM(CHAR_COL)) FROM test; -- Returns actual length

-- Comparison gotcha
SELECT * FROM test WHERE char_col = 'AB'; -- Matches 'AB        ' (padded)
```

### Inquiry 60: Implicit vs Explicit Conversion
**Scenario:** What is implicit conversion and why is it dangerous?
**Explanation:**

```sql
-- Implicit (automatic — can cause index issues!)
SELECT * FROM Emp WHERE emp_id = '123'; -- emp_id is INT, '123' is VARCHAR
-- DB converts '123' to 123 implicitly. May prevent index usage.

-- Explicit (manual — always preferred)
SELECT * FROM Emp WHERE emp_id = CAST('123' AS INT);
SELECT * FROM Emp WHERE emp_id = CONVERT(INT, '123'); -- SQL Server
```

**Danger of implicit conversion:**
*   Index may not be used (full table scan).
*   Unexpected results with date/string comparisons.
*   Silent data truncation.

### Inquiry 61: CAST vs CONVERT
**Scenario:** Difference?
**Explanation:**
*   `CAST(expr AS type)` — ANSI standard. Works everywhere.
*   `CONVERT(type, expr, style)` — SQL Server specific. Has style parameter for date formatting.

```sql
-- CAST (portable)
SELECT CAST(salary AS VARCHAR(10)) FROM Emp;
SELECT CAST('2024-01-15' AS DATE);

-- CONVERT (SQL Server — with date styles)
SELECT CONVERT(VARCHAR, GETDATE(), 103); -- dd/mm/yyyy
SELECT CONVERT(VARCHAR, GETDATE(), 120); -- yyyy-mm-dd hh:mi:ss
```

### Inquiry 62: NULL Handling
**Scenario:** Tricky NULL behavior.
**Explanation:**

```sql
-- NULL is NOT equal to anything (not even NULL)
SELECT NULL = NULL;    -- NULL (not TRUE)
SELECT NULL <> NULL;   -- NULL (not TRUE)
SELECT NULL > 5;       -- NULL

-- Correct way to check NULL
SELECT * FROM Emp WHERE manager_id IS NULL;
SELECT * FROM Emp WHERE manager_id IS NOT NULL;

-- NULL in arithmetic
SELECT 100 + NULL;     -- NULL
SELECT NULL * 5;       -- NULL

-- NULL in aggregates
SELECT COUNT(*)    FROM Emp; -- Counts all rows (including NULL columns)
SELECT COUNT(comm) FROM Emp; -- Counts only NON-NULL values of comm
SELECT AVG(comm)   FROM Emp; -- Ignores NULLs (does NOT treat as 0)

-- NULL-safe functions
SELECT COALESCE(comm, 0) FROM Emp;          -- Returns 0 if comm is NULL
SELECT IFNULL(comm, 0) FROM Emp;            -- MySQL specific
SELECT ISNULL(comm, 0) FROM Emp;            -- SQL Server specific
SELECT NVL(comm, 0) FROM Emp;              -- Oracle specific
SELECT NULLIF(a, b) FROM test;              -- Returns NULL if a = b, else a
```

### Inquiry 63: Date Functions
**Scenario:** Common date operations.
**Explanation:**

```sql
-- Current date/time
SELECT CURRENT_DATE;                        -- 2024-01-15
SELECT CURRENT_TIMESTAMP;                   -- 2024-01-15 10:30:00
SELECT NOW();                               -- MySQL: 2024-01-15 10:30:00
SELECT GETDATE();                           -- SQL Server

-- Date arithmetic
SELECT DATE_ADD('2024-01-15', INTERVAL 30 DAY);   -- MySQL
SELECT DATEADD(DAY, 30, '2024-01-15');             -- SQL Server
SELECT '2024-01-15'::DATE + INTERVAL '30 days';   -- PostgreSQL

-- Date difference
SELECT DATEDIFF('2024-03-15', '2024-01-15');       -- MySQL: 60 days
SELECT DATEDIFF(DAY, '2024-01-15', '2024-03-15');  -- SQL Server: 60

-- Extract parts
SELECT EXTRACT(YEAR FROM hire_date) FROM Emp;      -- Standard
SELECT YEAR(hire_date) FROM Emp;                   -- MySQL/SQL Server
SELECT DATE_FORMAT(hire_date, '%Y-%m') FROM Emp;   -- MySQL
SELECT FORMAT(hire_date, 'yyyy-MM') FROM Emp;      -- SQL Server
```

---

## 11. Transactions & Locking - Deep Dive

### Inquiry 64: Transaction Lifecycle
**Scenario:** Explain the flow.
**Explanation:**

```sql
BEGIN TRANSACTION;  -- or START TRANSACTION (MySQL)

    UPDATE Accounts SET balance = balance - 1000 WHERE id = 1;
    UPDATE Accounts SET balance = balance + 1000 WHERE id = 2;

    -- Check for errors
    IF @@ERROR <> 0  -- SQL Server
        ROLLBACK;
    ELSE
        COMMIT;
```

**States:** Active → Partially Committed → Committed (or Failed → Aborted → Rolled Back).

### Inquiry 65: SAVEPOINT
**Scenario:** Partial rollback?
**Explanation:**

```sql
BEGIN TRANSACTION;
    INSERT INTO Orders VALUES (1, 'Order A');
    SAVEPOINT sp1;

    INSERT INTO Orders VALUES (2, 'Order B');
    -- Something went wrong with Order B only
    ROLLBACK TO sp1;  -- Only Order B is rolled back

    INSERT INTO Orders VALUES (3, 'Order C');
COMMIT;  -- Order A and Order C are committed
```

### Inquiry 66: Isolation Levels - Deep Dive
**Scenario:** Explain each problem and which level prevents it.
**Explanation:**

| Problem | Description | Example |
|---------|-------------|---------|
| **Dirty Read** | Reading uncommitted data from another transaction | T1 updates row, T2 reads it, T1 rolls back — T2 has wrong data |
| **Non-Repeatable Read** | Same query returns different values in same transaction | T1 reads row, T2 updates & commits, T1 reads again — different value |
| **Phantom Read** | Same query returns different number of rows | T1 counts rows, T2 inserts new row & commits, T1 counts again — different count |

| Isolation Level | Dirty Read | Non-Repeatable Read | Phantom Read | Performance |
|----------------|------------|-------------------|-------------|-------------|
| **Read Uncommitted** | Possible | Possible | Possible | Fastest |
| **Read Committed** | Prevented | Possible | Possible | Fast |
| **Repeatable Read** | Prevented | Prevented | Possible | Moderate |
| **Serializable** | Prevented | Prevented | Prevented | Slowest |

```sql
-- Set isolation level
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;  -- SQL Server
SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ;  -- MySQL

-- Check current level
SELECT @@TRANSACTION_ISOLATION;  -- MySQL
DBCC USEROPTIONS;  -- SQL Server
```

### Inquiry 67: Locking Types
**Scenario:** Shared vs Exclusive locks.
**Explanation:**

| Lock Type | Abbreviation | Used For | Compatibility |
|-----------|-------------|----------|---------------|
| **Shared (S)** | Read lock | SELECT | Compatible with other S locks |
| **Exclusive (X)** | Write lock | INSERT/UPDATE/DELETE | Not compatible with any lock |
| **Update (U)** | Intent to write | UPDATE (during read phase) | Compatible with S, not X |
| **Intent (IS/IX)** | Table-level hint | Indicates row-level locks exist | Prevents table-level conflicts |

```sql
-- Explicit locking hints (SQL Server)
SELECT * FROM Emp WITH (NOLOCK);       -- Read uncommitted (dirty reads OK)
SELECT * FROM Emp WITH (HOLDLOCK);     -- Hold shared lock until transaction ends
SELECT * FROM Emp WITH (UPDLOCK);      -- Take update lock (prevent other updates)
SELECT * FROM Emp WITH (TABLOCKX);     -- Exclusive table lock

-- MySQL
SELECT * FROM Emp FOR UPDATE;          -- Exclusive lock on selected rows
SELECT * FROM Emp FOR SHARE;           -- Shared lock (MySQL 8+)
SELECT * FROM Emp LOCK IN SHARE MODE;  -- Shared lock (older MySQL)
```

### Inquiry 68: Deadlock - SQL Example
**Scenario:** How does deadlock happen in SQL?
**Explanation:**

```sql
-- Session 1:
BEGIN TRANSACTION;
UPDATE Accounts SET balance = 500 WHERE id = 1;  -- Locks row 1
-- Waits for row 2...
UPDATE Accounts SET balance = 300 WHERE id = 2;  -- BLOCKED by Session 2

-- Session 2:
BEGIN TRANSACTION;
UPDATE Accounts SET balance = 700 WHERE id = 2;  -- Locks row 2
-- Waits for row 1...
UPDATE Accounts SET balance = 100 WHERE id = 1;  -- BLOCKED by Session 1

-- DEADLOCK! DB engine detects and kills one session (victim)
```

**Prevention:**
*   Access tables/rows in the **same order** across all transactions.
*   Keep transactions **short**.
*   Use **lower isolation levels** when possible.
*   Add appropriate **indexes** (reduces lock duration).

### Inquiry 69: Optimistic vs Pessimistic Locking
**Scenario:** When to use which?
**Explanation:**

| Feature | Optimistic | Pessimistic |
|---------|-----------|-------------|
| **Approach** | No lock during read. Check for conflicts at write time | Lock during read. Hold until done |
| **Implementation** | Version column or timestamp | SELECT FOR UPDATE |
| **Conflict handling** | Retry on conflict | Block other transactions |
| **Best for** | Low contention, read-heavy | High contention, write-heavy |

```sql
-- Optimistic: Using version column
UPDATE Products
SET price = 29.99, version = version + 1
WHERE id = 1 AND version = 5;  -- Fails if someone else updated (version changed)
-- If 0 rows affected → conflict detected → retry

-- Pessimistic: Lock the row
BEGIN TRANSACTION;
SELECT * FROM Products WHERE id = 1 FOR UPDATE;  -- Lock acquired
-- Do calculations...
UPDATE Products SET price = 29.99 WHERE id = 1;
COMMIT;  -- Lock released
```

---

## 12. Coding Problems (Practical Queries)

### Sample Schema (Used across problems)

```sql
CREATE TABLE Employees (
    id INT PRIMARY KEY,
    name VARCHAR(50),
    dept_id INT,
    salary DECIMAL(10,2),
    manager_id INT,
    hire_date DATE
);

CREATE TABLE Departments (
    id INT PRIMARY KEY,
    dept_name VARCHAR(50)
);

CREATE TABLE Orders (
    id INT PRIMARY KEY,
    customer_id INT,
    order_date DATE,
    amount DECIMAL(10,2),
    status VARCHAR(20)
);

CREATE TABLE Customers (
    id INT PRIMARY KEY,
    name VARCHAR(50),
    city VARCHAR(50),
    created_at DATE
);
```

### Inquiry 70: Nth Highest Salary (Multiple Ways)
**Scenario:** Find the 3rd highest salary.

```sql
-- Method 1: DENSE_RANK (handles duplicates correctly)
SELECT salary FROM (
    SELECT salary, DENSE_RANK() OVER (ORDER BY salary DESC) AS rnk
    FROM Employees
) ranked
WHERE rnk = 3;

-- Method 2: LIMIT OFFSET (MySQL)
SELECT DISTINCT salary FROM Employees
ORDER BY salary DESC
LIMIT 1 OFFSET 2;  -- 0-indexed: skip 2, take 1

-- Method 3: Correlated subquery
SELECT DISTINCT salary FROM Employees e1
WHERE 3 = (SELECT COUNT(DISTINCT salary) FROM Employees e2 WHERE e2.salary >= e1.salary);

-- Method 4: Nth highest per department
SELECT dept_id, name, salary FROM (
    SELECT *, DENSE_RANK() OVER (PARTITION BY dept_id ORDER BY salary DESC) AS rnk
    FROM Employees
) ranked
WHERE rnk = 3;
```

### Inquiry 71: Find Duplicate Records
**Scenario:** Find and delete duplicates.

```sql
-- Find duplicates
SELECT name, email, COUNT(*) AS cnt
FROM Employees
GROUP BY name, email
HAVING COUNT(*) > 1;

-- Delete duplicates (keep lowest id)
DELETE FROM Employees
WHERE id NOT IN (
    SELECT MIN(id) FROM Employees GROUP BY name, email
);

-- Delete using CTE + ROW_NUMBER (SQL Server / PostgreSQL)
WITH CTE AS (
    SELECT *, ROW_NUMBER() OVER (PARTITION BY name, email ORDER BY id) AS rn
    FROM Employees
)
DELETE FROM CTE WHERE rn > 1;

-- MySQL: Delete using self-join
DELETE e1 FROM Employees e1
INNER JOIN Employees e2
ON e1.name = e2.name AND e1.email = e2.email AND e1.id > e2.id;
```

### Inquiry 72: Employees Earning More Than Their Manager
**Scenario:** Self-join problem.

```sql
SELECT e.name AS employee, e.salary AS emp_salary,
       m.name AS manager, m.salary AS mgr_salary
FROM Employees e
JOIN Employees m ON e.manager_id = m.id
WHERE e.salary > m.salary;
```

### Inquiry 73: Department with Highest Average Salary
**Scenario:** Aggregation + subquery.

```sql
-- Method 1: Subquery
SELECT d.dept_name, AVG(e.salary) AS avg_salary
FROM Employees e
JOIN Departments d ON e.dept_id = d.id
GROUP BY d.dept_name
ORDER BY avg_salary DESC
LIMIT 1;

-- Method 2: Using RANK
SELECT dept_name, avg_salary FROM (
    SELECT d.dept_name, AVG(e.salary) AS avg_salary,
           RANK() OVER (ORDER BY AVG(e.salary) DESC) AS rnk
    FROM Employees e
    JOIN Departments d ON e.dept_id = d.id
    GROUP BY d.dept_name
) ranked
WHERE rnk = 1;
```

### Inquiry 74: Consecutive Days Login
**Scenario:** Find users who logged in 3+ consecutive days.

```sql
-- Using LAG to detect gaps
WITH LoginDates AS (
    SELECT user_id, login_date,
        login_date - ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY login_date) * INTERVAL '1 day' AS grp
    FROM Logins
),
ConsecutiveGroups AS (
    SELECT user_id, grp, COUNT(*) AS consecutive_days
    FROM LoginDates
    GROUP BY user_id, grp
)
SELECT DISTINCT user_id
FROM ConsecutiveGroups
WHERE consecutive_days >= 3;
```

### Inquiry 75: Year-over-Year Growth
**Scenario:** Calculate revenue growth percentage.

```sql
WITH YearlyRevenue AS (
    SELECT EXTRACT(YEAR FROM order_date) AS year,
           SUM(amount) AS revenue
    FROM Orders
    GROUP BY EXTRACT(YEAR FROM order_date)
)
SELECT year, revenue,
    LAG(revenue) OVER (ORDER BY year) AS prev_year_revenue,
    ROUND((revenue - LAG(revenue) OVER (ORDER BY year)) * 100.0
          / LAG(revenue) OVER (ORDER BY year), 2) AS growth_pct
FROM YearlyRevenue;
```

### Inquiry 76: Running Total
**Scenario:** Cumulative sum of orders by date.

```sql
SELECT order_date, amount,
    SUM(amount) OVER (ORDER BY order_date) AS running_total,
    SUM(amount) OVER (PARTITION BY customer_id ORDER BY order_date) AS customer_running_total
FROM Orders
ORDER BY order_date;
```

### Inquiry 77: Pivot — Rows to Columns
**Scenario:** Show monthly sales as columns.

```sql
-- MySQL / PostgreSQL (CASE WHEN approach — works everywhere)
SELECT
    EXTRACT(YEAR FROM order_date) AS year,
    SUM(CASE WHEN EXTRACT(MONTH FROM order_date) = 1 THEN amount ELSE 0 END) AS Jan,
    SUM(CASE WHEN EXTRACT(MONTH FROM order_date) = 2 THEN amount ELSE 0 END) AS Feb,
    SUM(CASE WHEN EXTRACT(MONTH FROM order_date) = 3 THEN amount ELSE 0 END) AS Mar,
    SUM(CASE WHEN EXTRACT(MONTH FROM order_date) = 4 THEN amount ELSE 0 END) AS Apr
FROM Orders
GROUP BY EXTRACT(YEAR FROM order_date);

-- SQL Server (PIVOT keyword)
SELECT * FROM (
    SELECT YEAR(order_date) AS year, MONTH(order_date) AS month, amount
    FROM Orders
) src
PIVOT (SUM(amount) FOR month IN ([1],[2],[3],[4],[5],[6],[7],[8],[9],[10],[11],[12])) pvt;
```

### Inquiry 78: Find Gaps in Sequential Data
**Scenario:** Find missing IDs in a sequence.

```sql
-- Find gaps using LEAD
SELECT id + 1 AS gap_start,
       LEAD(id) OVER (ORDER BY id) - 1 AS gap_end
FROM Employees
WHERE LEAD(id) OVER (ORDER BY id) - id > 1;

-- Example: IDs are 1,2,3,5,8 → Gaps: 4-4, 6-7
```

### Inquiry 79: Customers Who Never Ordered
**Scenario:** LEFT JOIN vs NOT EXISTS.

```sql
-- Method 1: LEFT JOIN + NULL check
SELECT c.name
FROM Customers c
LEFT JOIN Orders o ON c.id = o.customer_id
WHERE o.id IS NULL;

-- Method 2: NOT EXISTS (often faster)
SELECT c.name FROM Customers c
WHERE NOT EXISTS (SELECT 1 FROM Orders o WHERE o.customer_id = c.id);

-- Method 3: NOT IN (careful with NULLs!)
SELECT name FROM Customers
WHERE id NOT IN (SELECT customer_id FROM Orders WHERE customer_id IS NOT NULL);
-- WARNING: If subquery returns NULL, NOT IN returns empty result!
```

### Inquiry 80: Top N per Group
**Scenario:** Top 3 highest paid employees per department.

```sql
SELECT dept_id, name, salary FROM (
    SELECT dept_id, name, salary,
        ROW_NUMBER() OVER (PARTITION BY dept_id ORDER BY salary DESC) AS rn
    FROM Employees
) ranked
WHERE rn <= 3;

-- With department name
SELECT d.dept_name, r.name, r.salary, r.rn
FROM (
    SELECT *, ROW_NUMBER() OVER (PARTITION BY dept_id ORDER BY salary DESC) AS rn
    FROM Employees
) r
JOIN Departments d ON r.dept_id = d.id
WHERE r.rn <= 3;
```

### Inquiry 81: Median Salary
**Scenario:** Calculate median (no built-in function in most DBs).

```sql
-- Method 1: Using PERCENTILE_CONT (PostgreSQL, SQL Server)
SELECT PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY salary) AS median_salary
FROM Employees;

-- Method 2: Manual calculation (works everywhere)
SELECT AVG(salary) AS median_salary FROM (
    SELECT salary, ROW_NUMBER() OVER (ORDER BY salary) AS rn,
           COUNT(*) OVER () AS total
    FROM Employees
) sub
WHERE rn IN (FLOOR((total + 1) / 2.0), CEIL((total + 1) / 2.0));
```

### Inquiry 82: Cumulative Percentage
**Scenario:** Each department's contribution to total salary.

```sql
SELECT dept_id,
    SUM(salary) AS dept_total,
    SUM(SUM(salary)) OVER () AS company_total,
    ROUND(SUM(salary) * 100.0 / SUM(SUM(salary)) OVER (), 2) AS pct_contribution
FROM Employees
GROUP BY dept_id
ORDER BY pct_contribution DESC;
```

### Inquiry 83: Self-Join — Employee-Manager Hierarchy
**Scenario:** Show employee with their manager name and manager's manager.

```sql
SELECT
    e.name AS employee,
    m.name AS manager,
    mm.name AS managers_manager
FROM Employees e
LEFT JOIN Employees m ON e.manager_id = m.id
LEFT JOIN Employees mm ON m.manager_id = mm.id;
```

### Inquiry 84: Date-based Problems
**Scenario:** Common date queries.

```sql
-- Employees hired in last 30 days
SELECT * FROM Employees WHERE hire_date >= CURRENT_DATE - INTERVAL '30 days';

-- Employees hired this year
SELECT * FROM Employees WHERE EXTRACT(YEAR FROM hire_date) = EXTRACT(YEAR FROM CURRENT_DATE);

-- Count hires per month
SELECT DATE_FORMAT(hire_date, '%Y-%m') AS month, COUNT(*) AS hires
FROM Employees
GROUP BY DATE_FORMAT(hire_date, '%Y-%m')
ORDER BY month;

-- Employees with work anniversary this month
SELECT name, hire_date FROM Employees
WHERE EXTRACT(MONTH FROM hire_date) = EXTRACT(MONTH FROM CURRENT_DATE)
  AND EXTRACT(DAY FROM hire_date) = EXTRACT(DAY FROM CURRENT_DATE);
```

### Inquiry 85: String Manipulation
**Scenario:** Common string operations.

```sql
-- Extract first name
SELECT SUBSTRING(name, 1, POSITION(' ' IN name) - 1) AS first_name FROM Employees;

-- Reverse a string
SELECT REVERSE(name) FROM Employees;

-- Count words in a string
SELECT name, LENGTH(name) - LENGTH(REPLACE(name, ' ', '')) + 1 AS word_count FROM Employees;

-- Capitalize first letter
SELECT CONCAT(UPPER(LEFT(name, 1)), LOWER(SUBSTRING(name, 2))) FROM Employees;

-- Email domain extraction
SELECT SUBSTRING(email FROM POSITION('@' IN email) + 1) AS domain FROM Users;
```

---

## 13. Tricky Output & Gotcha Questions

### Inquiry 86: NULL in WHERE clause
**Scenario:** What is the output?

```sql
SELECT * FROM Employees WHERE dept_id = NULL;
-- Output: EMPTY! (0 rows)
-- NULL cannot be compared with =. Use IS NULL.

SELECT * FROM Employees WHERE dept_id IS NULL;
-- Output: All employees with no department
```

### Inquiry 87: NULL in NOT IN
**Scenario:** Dangerous trap!

```sql
-- Table: Managers has ids: 1, 2, NULL
SELECT * FROM Employees WHERE id NOT IN (SELECT manager_id FROM Managers);
-- Output: EMPTY! (0 rows)
-- Because NOT IN with NULL → all comparisons return UNKNOWN → no rows match

-- Fix: Filter out NULLs
SELECT * FROM Employees WHERE id NOT IN (SELECT manager_id FROM Managers WHERE manager_id IS NOT NULL);
-- Or use NOT EXISTS (NULL-safe)
SELECT * FROM Employees e WHERE NOT EXISTS (SELECT 1 FROM Managers m WHERE m.manager_id = e.id);
```

### Inquiry 88: COUNT(*) vs COUNT(column)
**Scenario:** What's the difference?

```sql
-- Data: id=1 name='Rahul', id=2 name=NULL, id=3 name='Amit'

SELECT COUNT(*) FROM Employees;       -- 3 (counts all rows)
SELECT COUNT(name) FROM Employees;    -- 2 (ignores NULLs)
SELECT COUNT(DISTINCT name) FROM Employees; -- 2 (distinct non-null values)
```

### Inquiry 89: GROUP BY with NULL
**Scenario:** How does GROUP BY treat NULLs?

```sql
-- Data: dept_id values: 1, 1, 2, NULL, NULL

SELECT dept_id, COUNT(*) FROM Employees GROUP BY dept_id;
-- Output:
-- dept_id | count
-- 1       | 2
-- 2       | 1
-- NULL    | 2     ← All NULLs are grouped together!
```

### Inquiry 90: ORDER BY with NULL
**Scenario:** Where do NULLs appear?

```sql
-- Default behavior varies by DB:
-- PostgreSQL/Oracle: NULLs are LAST in ASC, FIRST in DESC
-- MySQL/SQL Server: NULLs are FIRST in ASC

-- Control explicitly:
SELECT * FROM Employees ORDER BY salary ASC NULLS LAST;   -- PostgreSQL/Oracle
SELECT * FROM Employees ORDER BY salary ASC NULLS FIRST;

-- MySQL workaround:
SELECT * FROM Employees ORDER BY salary IS NULL, salary ASC;
```

### Inquiry 91: Division by Zero
**Scenario:** How to handle?

```sql
-- This will ERROR:
SELECT 100 / 0;

-- Safe division:
SELECT CASE WHEN denominator = 0 THEN 0 ELSE numerator / denominator END AS result;

-- Using NULLIF (returns NULL instead of error):
SELECT numerator / NULLIF(denominator, 0) AS result;
-- If denominator is 0, NULLIF returns NULL, and anything / NULL = NULL (no error)
```

### Inquiry 92: UNION removes duplicates silently
**Scenario:** Data loss trap.

```sql
-- UNION removes duplicates (like DISTINCT)
SELECT name FROM Employees WHERE dept_id = 1
UNION
SELECT name FROM Employees WHERE dept_id = 2;
-- If "Rahul" is in both departments, appears ONCE

-- UNION ALL keeps all rows
SELECT name FROM Employees WHERE dept_id = 1
UNION ALL
SELECT name FROM Employees WHERE dept_id = 2;
-- "Rahul" appears TWICE (once from each query)
```

### Inquiry 93: BETWEEN is Inclusive
**Scenario:** Common mistake with dates.

```sql
-- BETWEEN includes both endpoints
SELECT * FROM Orders WHERE order_date BETWEEN '2024-01-01' AND '2024-01-31';
-- Includes Jan 1 AND Jan 31

-- Gotcha with DATETIME:
SELECT * FROM Orders WHERE order_date BETWEEN '2024-01-01' AND '2024-01-31';
-- If order_date is DATETIME, this misses '2024-01-31 10:30:00'!
-- Because BETWEEN '2024-01-31' means '2024-01-31 00:00:00'

-- Fix:
SELECT * FROM Orders WHERE order_date >= '2024-01-01' AND order_date < '2024-02-01';
```

### Inquiry 94: Execution Order of SQL
**Scenario:** In what order does SQL actually execute?

```sql
-- Written order:
SELECT → FROM → WHERE → GROUP BY → HAVING → ORDER BY → LIMIT

-- Actual execution order:
FROM → WHERE → GROUP BY → HAVING → SELECT → ORDER BY → LIMIT
```

| Step | Clause | What Happens |
|------|--------|-------------|
| 1 | **FROM** | Tables loaded, JOINs performed |
| 2 | **WHERE** | Rows filtered (before grouping) |
| 3 | **GROUP BY** | Rows grouped |
| 4 | **HAVING** | Groups filtered (after grouping) |
| 5 | **SELECT** | Columns selected, expressions evaluated |
| 6 | **ORDER BY** | Results sorted |
| 7 | **LIMIT** | Results truncated |

**Why this matters:**
```sql
-- This FAILS:
SELECT name, salary * 12 AS annual_salary FROM Employees WHERE annual_salary > 100000;
-- Error! annual_salary doesn't exist in WHERE (SELECT runs after WHERE)

-- Fix: Use subquery or repeat expression
SELECT name, salary * 12 AS annual_salary FROM Employees WHERE salary * 12 > 100000;
```

### Inquiry 95: Aggregate in WHERE vs HAVING
**Scenario:** Common error.

```sql
-- WRONG: Cannot use aggregate in WHERE
SELECT dept_id, AVG(salary) FROM Employees WHERE AVG(salary) > 50000 GROUP BY dept_id;
-- Error! WHERE is evaluated before GROUP BY

-- CORRECT: Use HAVING for aggregate conditions
SELECT dept_id, AVG(salary) FROM Employees GROUP BY dept_id HAVING AVG(salary) > 50000;
```

---

## 14. Interview Questions & Answers

### Inquiry 96: What is the difference between DDL, DML, DCL, TCL?
**Answer:**

| Category | Full Form | Commands | Description |
|----------|-----------|----------|-------------|
| **DDL** | Data Definition Language | CREATE, ALTER, DROP, TRUNCATE | Defines structure |
| **DML** | Data Manipulation Language | SELECT, INSERT, UPDATE, DELETE | Manipulates data |
| **DCL** | Data Control Language | GRANT, REVOKE | Controls access |
| **TCL** | Transaction Control Language | COMMIT, ROLLBACK, SAVEPOINT | Manages transactions |

### Inquiry 97: What is a Correlated Subquery?
**Answer:** A subquery that references columns from the outer query. Executes once per row of the outer query.

```sql
-- Find employees earning above their department average
SELECT name, salary, dept_id FROM Employees e
WHERE salary > (SELECT AVG(salary) FROM Employees WHERE dept_id = e.dept_id);
-- The inner query uses e.dept_id from the outer query → correlated
```

### Inquiry 98: Explain Query Execution Plan
**Answer:** A plan showing how the database engine will execute your query. Use `EXPLAIN` (MySQL/PostgreSQL) or `SET SHOWPLAN` (SQL Server).

```sql
EXPLAIN SELECT * FROM Employees WHERE salary > 50000;
-- Shows: Seq Scan vs Index Scan, estimated rows, cost

EXPLAIN ANALYZE SELECT * FROM Employees WHERE salary > 50000;
-- Shows actual execution time + plan
```

**What to look for:**
*   **Seq Scan / Table Scan** — Bad for large tables (no index used).
*   **Index Scan / Index Seek** — Good (index used).
*   **Nested Loop** — OK for small tables.
*   **Hash Join** — Good for large tables.
*   **Sort** — Expensive for large datasets.

### Inquiry 99: How to Optimize a Slow Query?
**Answer:**
1. **Run EXPLAIN** — Check if indexes are being used.
2. **Add indexes** — On columns in WHERE, JOIN, ORDER BY.
3. **Avoid SELECT \*** — Select only needed columns.
4. **Avoid functions on indexed columns** — `WHERE YEAR(date) = 2024` → `WHERE date >= '2024-01-01'`.
5. **Use EXISTS instead of IN** — For large subqueries.
6. **Limit results** — Use LIMIT/TOP for pagination.
7. **Avoid correlated subqueries** — Rewrite as JOINs.
8. **Denormalize** — For read-heavy reporting queries.
9. **Use covering indexes** — Index includes all columns needed by query.
10. **Partition large tables** — Split by date/range.

### Inquiry 100: What is a Covering Index?
**Answer:** An index that contains ALL columns needed by a query. The DB reads only the index, never touches the table (Index-Only Scan).

```sql
-- Query:
SELECT name, email FROM Users WHERE dept = 'IT';

-- Covering index:
CREATE INDEX idx_covering ON Users(dept, name, email);
-- All 3 columns are in the index → no table lookup needed → FAST
```

### Inquiry 101: What is Database Sharding?
**Answer:** Splitting a large database into smaller pieces (shards) across multiple servers. Each shard holds a subset of data.

*   **Horizontal sharding** — Split rows (e.g., users A-M on server 1, N-Z on server 2).
*   **Vertical sharding** — Split columns (e.g., user profile on server 1, user orders on server 2).
*   **Used by:** Instagram, Twitter, Uber.

### Inquiry 102: What is Database Replication?
**Answer:** Copying data from one DB server (master) to one or more servers (replicas).

| Type | Description |
|------|-------------|
| **Master-Slave** | Writes go to master, reads from replicas. Most common. |
| **Master-Master** | Both can accept writes. Complex conflict resolution. |
| **Synchronous** | Replica confirms write before master commits. Consistent but slow. |
| **Asynchronous** | Master commits immediately, replica catches up later. Fast but may lag. |

### Inquiry 103: ACID vs BASE
**Answer:**

| ACID (SQL) | BASE (NoSQL) |
|-----------|-------------|
| **A**tomicity | **B**asically **A**vailable |
| **C**onsistency | **S**oft state |
| **I**solation | **E**ventual consistency |
| **D**urability | |
| Strong consistency | High availability |
| Bank transactions | Social media feeds |

### Inquiry 104: When to Use NoSQL vs SQL?
**Answer:**

| Use SQL When | Use NoSQL When |
|-------------|---------------|
| Structured data with relationships | Unstructured/semi-structured data |
| ACID transactions needed | High write throughput needed |
| Complex queries (JOINs, aggregations) | Simple key-value lookups |
| Data integrity is critical | Horizontal scaling needed |
| Banking, ERP, HR systems | Real-time analytics, IoT, social media |

### Inquiry 105: What is Connection Pooling?
**Answer:** Maintaining a pool of pre-created database connections that are reused instead of creating new ones for each request. Creating a connection is expensive (~200-500ms).

*   **HikariCP** — Fastest, default in Spring Boot.
*   **C3P0**, **DBCP** — Older alternatives.
*   Typical pool size: 10-20 connections.

### Inquiry 106: Explain Stored Procedure vs Function vs Trigger
**Answer:**

| Feature | Stored Procedure | Function | Trigger |
|---------|-----------------|----------|---------|
| **Called by** | Application code (`CALL proc()`) | SQL query (`SELECT func()`) | Automatically (on INSERT/UPDATE/DELETE) |
| **Return** | Multiple values (OUT params) | Single value | Nothing |
| **DML allowed** | Yes | Generally No | Yes |
| **Transaction** | Can manage own transaction | Part of calling transaction | Part of triggering transaction |
| **Use case** | Business logic, batch ops | Calculations, formatting | Audit logging, validation |

### Inquiry 107: What is SQL Injection? How to Prevent?
**Answer:** Attacker inserts malicious SQL through user input.

```sql
-- Vulnerable code (string concatenation):
String sql = "SELECT * FROM Users WHERE name = '" + userInput + "'";
-- If userInput = "'; DROP TABLE Users; --"
-- Becomes: SELECT * FROM Users WHERE name = ''; DROP TABLE Users; --'

-- Prevention:
-- 1. Prepared Statements (BEST)
PreparedStatement ps = conn.prepareStatement("SELECT * FROM Users WHERE name = ?");
ps.setString(1, userInput);  -- Input is treated as data, not SQL

-- 2. Stored Procedures
-- 3. Input validation / whitelisting
-- 4. ORM frameworks (JPA/Hibernate) — parameterized by default
```

### Inquiry 108: What are Window Functions Used For in Real Projects?
**Answer:**
*   **Ranking** — Top N products, employee rankings.
*   **Running totals** — Cumulative sales, account balance.
*   **Moving averages** — 7-day average revenue.
*   **Comparing rows** — Current vs previous month (LAG/LEAD).
*   **Pagination** — ROW_NUMBER for efficient paging.
*   **De-duplication** — ROW_NUMBER + PARTITION BY to keep latest record.
*   **Percentiles** — Salary distribution, performance bands.
