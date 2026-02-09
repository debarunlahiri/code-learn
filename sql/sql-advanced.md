# Advanced SQL - Complete Guide (Subqueries, Window Functions, Performance)

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Subqueries, CTEs, Window Functions (RANK, DENSE_RANK), Indexes, Optimization, Normalization, and complex scenarios.

--

## Table of Contents

1. [Subqueries (Nested Queries)](#subqueries-nested-queries)
2. [Window Functions](#window-functions)
3. [CTEs (Common Table Expressions)](#ctes-common-table-expressions)
4. [Indexes & Performance Tuning](#indexes-performance-tuning)
5. [Normalization & Denormalization](#normalization-denormalization)
6. [Materialized Views](#materialized-views)
7. [Stored Procedures & Triggers](#stored-procedures-triggers)
8. [Advanced Coding Problems](#advanced-coding-problems)
9. [Key Topics & Explanations](#key-topics-explanations)

--

## 1. Subqueries (Nested Queries)

A query inside another query.

### Types of Subqueries

1. **Scalar Subquery**: Returns a single value (one row, one column).
   ```sql
   SELECT Name, Salary 
   FROM Employees 
   WHERE Salary > (SELECT AVG(Salary) FROM Employees);
   ```

2. **Row Subquery**: Returns a single row (one or more columns).
   ```sql
   SELECT * FROM Employees 
   WHERE (DeptId, ManagerId) = (SELECT DeptId, ManagerId FROM Departments WHERE Name = 'IT');
   ```

3. **Table Subquery**: Returns a table (multiple rows/columns).
   ```sql
   SELECT Name 
   FROM Employees 
   WHERE DeptId IN (SELECT DeptId FROM Departments WHERE Location = 'Pune');
   ```

4. **Correlated Subquery**: Dependent heavily on the outer query (Executed row-by-row).
   ```sql
   - Employees earning more than average of THEIR OWN department
   SELECT Name, Salary, DeptId 
   FROM Employees E1
   WHERE Salary > (
       SELECT AVG(Salary) 
       FROM Employees E2 
       WHERE E2.DeptId = E1.DeptId
   );
   ```

--

## 2. Window Functions

Functions that perform calculations across a set of table rows that are related to the current row. Unlike aggregate functions, they do not collapse rows.

**Syntax**: `FUNCTION_NAME() OVER (PARTITION BY col ORDER BY col)`

### Ranking Functions

1. **ROW_NUMBER()**: Unique sequential number (1, 2, 3, 4).
2. **RANK()**: Gaps for ties (1, 2, 2, 4).
3. **DENSE_RANK()**: No gaps for ties (1, 2, 2, 3).

**Example**:
| Name | Salary | Row_Number | Rank | Dense_Rank |
|---|----|------|---|------|
| A | 5000 | 1 | 1 | 1 |
| B | 5000 | 2 | 1 | 1 |
| C | 4000 | 3 | 3 | 2 |

```sql
SELECT Name, Salary,
ROW_NUMBER() OVER(ORDER BY Salary DESC) as RN,
RANK() OVER(ORDER BY Salary DESC) as Rnk,
DENSE_RANK() OVER(ORDER BY Salary DESC) as DRnk
FROM Employees;
```

### Aggregate Window Functions

- `SUM()`, `AVG()`, `COUNT()`, `MIN()`, `MAX()`
- Used for **Running Totals**.

```sql
SELECT OrderId, Amount,
SUM(Amount) OVER (ORDER BY OrderDate) as RunningTotal
FROM Orders;
```

### Lead & Lag (Analytic Functions)

- `LAG(col, n)`: Access value from previous row.
- `LEAD(col, n)`: Access value from next row.

```sql
- Compare current month sales with previous month
SELECT Month, Sales,
LAG(Sales, 1) OVER (ORDER BY Month) as PrevMonthSales,
(Sales - LAG(Sales, 1) OVER (ORDER BY Month)) as Growth
FROM MonthlySales;
```

--

## 3. CTEs (Common Table Expressions)

Temporary result set defined within the execution scope of a single statement. Makes complex queries readable.

**Syntax**: `WITH name AS (query) SELECT ...`

```sql
WITH DeptAvg AS (
    SELECT DeptId, AVG(Salary) as AvgSal
    FROM Employees
    GROUP BY DeptId
)
SELECT E.Name, E.Salary
FROM Employees E
JOIN DeptAvg D ON E.DeptId = D.DeptId
WHERE E.Salary > D.AvgSal;
```

### Recursive CTE (Advanced)

Used for hierarchical data (Organizational Chart).

```sql
WITH RECURSIVE OrgChart AS (
    SELECT Id, Name, ManagerId, 1 as Level
    FROM Employees WHERE ManagerId IS NULL - CEO
    UNION ALL
    SELECT E.Id, E.Name, E.ManagerId, O.Level + 1
    FROM Employees E
    JOIN OrgChart O ON E.ManagerId = O.Id
)
SELECT * FROM OrgChart;
```

--

## 4. Indexes & Performance Tuning

### B-Tree Index Structure

- **Clustered Index**: Stores the actual data rows at the leaf level. Only 1 per table (usually PK). Faster for range queries.
- **Non-Clustered Index**: Stores pointers to data rows. Does not reorder data. Multiple allowed.

### When to Index?

- **Good**: Columns in `WHERE`, `JOIN`, `ORDER BY`, `GROUP BY`. High cardinality (many unique values).
- **Bad**: Small tables, columns with few unique values (Gender: M/F), frequently updated columns (Index maintenance overhead).

### Explain Plan

Use `EXPLAIN SELECT ...` to see how the database executes a query. Look for:
- **Table Scan**: Reading entire table (Bad for large tables).
- **Index Seek**: Using index to find specific rows (Good).
- **Index Scan**: Reading entire index (Better than table scan, but slower than seek).

### Optimization Tips (SARGable Queries)

- Avoid functions on columns in WHERE clause.
  - Bad: `WHERE YEAR(Date) = 2023`
  - Good: `WHERE Date BETWEEN '2023-01-01' AND '2023-12-31'`
- Avoid wildcard at start of LIKE.
  - Bad: `LIKE '%abc'` (Index Scan)
  - Good: `LIKE 'abc%'` (Index Seek)
- Select only needed columns (Avoid `SELECT *`).

--

## 5. Normalization & Denormalization

### Normal Forms

- **1NF**: Eliminate repeating groups (Atomic values).
- **2NF**: Eliminate partial dependency (All non-key cols depend on Whole PK). Needed for Composite Keys.
- **3NF**: Eliminate transitive dependency (Non-key col depends on another non-key col). E.g., City depends on ZipCode, not EmployeeId.
- **BCNF**: Strict 3NF.

### Denormalization

Used in **OLAP** (Data Warehousing) systems (Star Schema). Adds redundancy to improve read performance by avoiding Joins.

--

## 6. Materialized Views

A database object that contains the results of a query.
- **Standard View**: Virtual table, computed on-the-fly (Real-time).
- **Materialized View**: Physical copy of data, refreshed periodically (Snapshot). Faster reads, stale data.
- **Use Case**: Complex aggregations for reporting dashboards.

--

## 7. Stored Procedures & Triggers

### Stored Procedures

Precompiled collection of SQL statements valid as a single unit.
- **Benefits**: Performance (Execution plan cached), Security (Grant execute only), Network Traffic Reduced, Maintainability.
- **Drawbacks**: Hard to debug, Vendor lock-in.

```sql
CREATE PROCEDURE GetEmpByDept(@deptId INT)
AS
BEGIN
    SELECT * FROM Employees WHERE DeptId = @deptId;
END;
```

### Triggers

Special stored proc that runs automatically when an event occurs (INSERT, UPDATE, DELETE).
- **Types**: `BEFORE`, `AFTER`, `INSTEAD OF`.
- **Use Case**: Audit logs, enforcing complex integrity rules.

```sql
CREATE TRIGGER AuditUpdate
AFTER UPDATE ON Employees
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog(EmpId, OldSal, NewSal)
    VALUES (OLD.Id, OLD.Salary, NEW.Salary);
END;
```

--

## 8. Advanced Coding Problems

### Problem 1: Find Nth Highest Salary (Generic)

```sql
- Using DENSE_RANK (Handles duplicates correctly)
SELECT DISTINCT Salary 
FROM (
    SELECT Salary, DENSE_RANK() OVER (ORDER BY Salary DESC) as Rnk
    FROM Employees
) T
WHERE Rnk = @N;

- Using LIMIT/OFFSET (Postgres/MySQL)
SELECT DISTINCT Salary 
FROM Employees 
ORDER BY Salary DESC 
LIMIT 1 OFFSET (@N - 1);
```

### Problem 2: Find duplicates and delete them

```sql
DELETE FROM Employees
WHERE Id NOT IN (
    SELECT MIN(Id) 
    FROM Employees 
    GROUP BY Email
);
- Note: In SQL Server use CTE with DELETE
```

### Problem 3: Pivot Table (Rows to Columns)

Transform:
| Month | Sales |
|----|----|
| Jan | 100 |
| Feb | 200 |

To:
| Jan | Feb |
|---|---|
| 100 | 200 |

```sql
- Using CASE
SELECT 
    SUM(CASE WHEN Month = 'Jan' THEN Sales ELSE 0 END) as Jan,
    SUM(CASE WHEN Month = 'Feb' THEN Sales ELSE 0 END) as Feb
FROM MonthlySales;
```

### Problem 4: Consecutive Login Days (Gaps and Islands)

Find users who logged in for 3 consecutive days.

```sql
SELECT DISTINCT Id 
FROM (
    SELECT Id, LoginDate,
    LEAD(LoginDate, 1) OVER (PARTITION BY Id ORDER BY LoginDate) as Next1,
    LEAD(LoginDate, 2) OVER (PARTITION BY Id ORDER BY LoginDate) as Next2
    FROM UserLogins
) T
WHERE DATEDIFF(day, LoginDate, Next1) = 1 
AND DATEDIFF(day, Next1, Next2) = 1;
```

--

## 9. Key Topics & Explanations

### Topic 1: Difference between Rank, Dense_Rank, and Row_Number?
**Answer:**
- `Row_Number`: Always unique sequential (1, 2, 3, 4).
- `Rank`: Skips for ties (1, 1, 3, 4).
- `Dense_Rank`: No skips (1, 1, 2, 3).
Use Dense_Rank for finding "Nth highest" to handle ties gracefully.

### Topic 2: What is a Correlated Subquery?
**Answer:** A subquery that uses values from the outer query. It executes once for EVERY row returned by the outer query, making it slow for large datasets.
Example: Finding employees earning more than the average salary of their *specific* department.

### Topic 3: Clustered vs Non-Clustered Index?
**Answer:**
- Clustered: Physical sorting of data. Only 1 per table. Leaf nodes = Data pages.
- Non-Clustered: Logical sorting. Multiple allowed. Leaf nodes = Pointers to data pages.
Analogy: Clustered is the Phonebook itself. Non-Clustered is the Index at the back of a book.

### Topic 4: ACID Properties?
**Answer:**
- **A**tomicity: Transaction is all-or-nothing.
- **C**onsistency: Data remains valid (integrity constraints) before/after.
- **I**solation: Concurrent transactions run independently.
- **D**urability: Committed data persists despite power loss.

### Topic 5: How to optimize a slow query?
**Answer:**
1. Check execution plan (`EXPLAIN`).
2. Identify full table scans.
3. Add appropriate indexes on filtering/joining columns.
4. Remove `SELECT *`.
5. Check for locking/blocking issues.
6. Update statistics.
7. Rewrite subqueries as JOINs where possible.

### Topic 6: UNION vs UNION ALL?
**Answer:**
- `UNION`: Removes duplicates, performs sort (Expensive).
- `UNION ALL`: Keeps duplicates, no sort (Fast).
Always prefer `UNION ALL` unless unique rows are required.

### Topic 7: Stored Procedure vs Function?
**Answer:**
- **Procedure**: Can perform DML (Insert/Update/Delete). Can return 0 or n values. Used for business logic.
- **Function**: Must return a single value (or table). Is generally read-only (mostly cannot modify DB state). Used for computations.
