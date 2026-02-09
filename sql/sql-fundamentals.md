# SQL Fundamentals - Complete Guide (Basics to Intermediate)

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** DBMS Concepts, DDL/DML, SELECT, Joins, Aggregation, and tricky technical scenarios.

--

## Table of Contents

1. [DBMS & RDBMS Overview](#dbms-rdbms-overview)
2. [SQL Commands (DDL, DML, DCL, TCL)](#sql-commands-ddl-dml-dcl-tcl)
3. [SELECT Statement & Filtering](#select-statement-filtering)
4. [Joins - The Most Important Topic](#joins---the-most-important-topic)
5. [Aggregation (GROUP BY & HAVING)](#aggregation-group-by-having)
6. [Set Operations (UNION, INTERSECT)](#set-operations-union-intersect)
7. [Constraints & Keys](#constraints-keys)
8. [Complex Technical Scenarios](#complex-technical-scenarios)
9. [Common Coding Challenges](#common-coding-challenges)
10. [Key Topics & Explanations](#key-topics-explanations)

--

## 1. DBMS & RDBMS Overview

- **Data**: Raw facts (e.g., "Rahul", 25).
- **Information**: Processed data (e.g., "Rahul is 25 years old").
- **Database**: Organized collection of data.
- **DBMS**: Software to manage database (MySQL, Oracle, PostgreSQL).
- **RDBMS**: Relational DBMS based on tables (Rows/Columns) and relationships.

### ACID Properties (Transaction Management)

1. **Atomicity**: All or nothing. Transaction is either fully completed or fully rolled back.
2. **Consistency**: Database remains in a valid state before and after transaction.
3. **Isolation**: Concurrent transactions don't interfere with each other.
4. **Durability**: Committed data is saved permanently, even after power failure.

--

## 2. SQL Commands (DDL, DML, DCL, TCL)

### 1. DDL (Data Definition Language) - Structure

| Command | Description | Example |
|-----|-------|-----|
| `CREATE` | Create table/db | `CREATE TABLE Users (id INT, name VARCHAR(50));` |
| `ALTER` | Modify structure | `ALTER TABLE Users ADD email VARCHAR(100);` |
| `DROP` | Delete table + data | `DROP TABLE Users;` |
| `TRUNCATE`| Delete data only (Fast) | `TRUNCATE TABLE Users;` |
| `RENAME` | Rename object | `RENAME TABLE Users TO Employees;` |

### 2. DML (Data Manipulation Language) - Data

| Command | Description | Example |
|-----|-------|-----|
| `INSERT` | Add rows | `INSERT INTO Users VALUES (1, 'Rahul');` |
| `UPDATE` | Modify rows | `UPDATE Users SET name='Amit' WHERE id=1;` |
| `DELETE` | Remove rows | `DELETE FROM Users WHERE id=1;` |

### 3. DCL (Data Control Language) - Permissions

- `GRANT`: Give access.
- `REVOKE`: Remove access.

### 4. TCL (Transaction Control Language)

- `COMMIT`: Save changes permanently.
- `ROLLBACK`: Undo changes since last commit.
- `SAVEPOINT`: Mark a point in transaction to rollback to.

--

## 3. SELECT Statement & Filtering

```sql
- Basic Structure (Order is important!)
SELECT column1, column2
FROM table_name
WHERE condition
GROUP BY column
HAVING condition
ORDER BY column ASC/DESC;
- LIMIT/TOP/OFFSET (depends on DB - MySQL/Postgres uses LIMIT)
```

### Filtering with WHERE

```sql
SELECT * FROM Employees WHERE Salary > 50000;
SELECT * FROM Employees WHERE Name = 'Rahul' AND Age >= 25;
SELECT * FROM Employees WHERE Dept IN ('IT', 'HR', 'Admin');
SELECT * FROM Employees WHERE Name LIKE 'R%'; - Starts with R
SELECT * FROM Employees WHERE Name LIKE '%a%'; - Contains 'a'
SELECT * FROM Employees WHERE ManagerId IS NULL; - Check for NULL
```

### Sorting with ORDER BY

```sql
SELECT * FROM Employees ORDER BY Salary DESC;
SELECT * FROM Employees ORDER BY Dept ASC, Salary DESC;
```

--

## 4. Joins - The Most Important Topic

Joins combine rows from two or more tables based on a related column.

### Types of Joins

| Join Type | Description | Venn Diagram Visualization |
|------|-------|--------------|
| **INNER** | Matching rows in BOTH tables | Intersection (A ∩ B) |
| **LEFT** (Outer) | All rows from Left + Matching from Right | Left Circle (A) |
| **RIGHT** (Outer)| All rows from Right + Matching from Left | Right Circle (B) |
| **FULL** (Outer) | All rows from Both (Match or NULL) | Union (A ∪ B) |
| **CROSS** | Cartesian Product (Every row × Every row) | A × B |
| **SELF** | Join table with itself | - |

### Examples

Assume Tables:
**Employees (A)**: {1:Rahul, 2:Amit, 3:Sneha}
**Departments (B)**: {1:IT, 2:HR} - Rahul(1) is in IT, Amit(2) in HR, Sneha(3) has NO Dept. A new Dept(3:Marketing) exists.

```sql
- 1. INNER JOIN (Only matching)
SELECT E.Name, D.DeptName 
FROM Employees E
INNER JOIN Departments D ON E.DeptId = D.DeptId;
- Output: Rahul-IT, Amit-HR (Sneha excluded, Marketing excluded)

- 2. LEFT JOIN (All Employees)
SELECT E.Name, D.DeptName
FROM Employees E
LEFT JOIN Departments D ON E.DeptId = D.DeptId;
- Output: Rahul-IT, Amit-HR, Sneha-NULL

- 3. RIGHT JOIN (All Departments)
SELECT E.Name, D.DeptName
FROM Employees E
RIGHT JOIN Departments D ON E.DeptId = D.DeptId;
- Output: Rahul-IT, Amit-HR, NULL-Marketing

- 4. CROSS JOIN
SELECT E.Name, D.DeptName FROM Employees E CROSS JOIN Departments D;
- Output: 3 employees * 3 departments = 9 rows
```

--

## 5. Aggregation (GROUP BY & HAVING)

Functions that perform specific operations on a set of values (column).

### Aggregate Functions

- `COUNT(col)`: Counts rows (ignores NULLs, unless `COUNT(*)`).
- `SUM(col)`: Sum of values.
- `AVG(col)`: Average value.
- `MIN(col)`: Minimum value.
- `MAX(col)`: Maximum value.

### GROUP BY

Groups rows that have the same values into summary rows.

```sql
- Count employees in each department
SELECT DeptId, COUNT(*) as EmpCount
FROM Employees
GROUP BY DeptId;
```

### HAVING vs WHERE

- **WHERE**: Filters **rows** BEFORE grouping.
- **HAVING**: Filters **groups** AFTER grouping.

```sql
- Find departments with more than 5 employees
SELECT DeptId, COUNT(*)
FROM Employees
GROUP BY DeptId
HAVING COUNT(*) > 5;
```

--

## 6. Set Operations (UNION, INTERSECT)

Combine results from multiple SELECT queries.

1. **UNION**: Combines unique rows (Removes duplicates).
2. **UNION ALL**: Combines all rows (Keeps duplicates - Faster).
3. **INTERSECT**: Returns common rows.
4. **MINUS / EXCEPT**: Returns rows in first but not second.

```sql
SELECT Name FROM Employees WHERE Dept = 'IT'
UNION
SELECT Name FROM Contractor WHERE Dept = 'IT';
```

--

## 7. Constraints & Keys

Rules enforced on data columns.

### Types of Constraints

| Constraint | Description |
|------|-------|
| `NOT NULL` | Cannot be NULL |
| `UNIQUE` | All values must be different |
| `PRIMARY KEY` | Unique + Not Null (Uniquely identifies row) |
| `FOREIGN KEY` | Links to Primary Key in another table (Ref integrity) |
| `CHECK` | Ensures condition is true (e.g., Age >= 18) |
| `DEFAULT` | Default value if none provided |

### Primary Key vs Unique Key

| Feature | Primary Key | Unique Key |
|-----|-------|------|
| **Nulls** | Not Allowed | One NULL allowed |
| **Count** | Only 1 per table | Multiple allowed |
| **Index** | Creates Clustered Index (usually) | Creates Non-Clustered Index |

### Primary Key vs Foreign Key

- **PK**: Parent table unique identifier.
- **FK**: Child table reference to Parent PK.

--

## 8. Complex Technical Scenarios

### Topic 1: DELETE vs TRUNCATE vs DROP?

| Feature | DELETE | TRUNCATE | DROP |
|-----|----|-----|---|
| **Type** | DML | DDL | DDL |
| **Speed** | Slow (row by row) | Fast (page deallocation) | Fastest |
| **Where** | Can use WHERE | Cannot use WHERE | Cannot use WHERE |
| **Rollback**| Yes | No (mostly) | No |
| **Identity**| Continues ID | Resets ID seeds | Removes Table |

### Topic 2: Order of Execution of SQL Query?

1. `FROM` / `JOIN`
2. `WHERE`
3. `GROUP BY`
4. `HAVING`
5. `SELECT`
6. `ORDER BY`
7. `LIMIT`

### Topic 3: How to find duplicate records?
```sql
SELECT Email, COUNT(*)
FROM Users
GROUP BY Email
HAVING COUNT(*) > 1;
```

### Topic 4: How to delete duplicate records (keep one)?
**Using CTE (Common Table Expression)**:
```sql
WITH CTE AS (
    SELECT *, ROW_NUMBER() OVER(PARTITION BY Email ORDER BY Id) as RN
    FROM Users
)
DELETE FROM CTE WHERE RN > 1;
```

--

## 9. Common Coding Challenges

### Problem 1: Find 2nd Highest Salary
**Approach 1 (Subquery)**:
```sql
SELECT MAX(Salary) FROM Employees 
WHERE Salary < (SELECT MAX(Salary) FROM Employees);
```
**Approach 2 (Limit/Offset)**:
```sql
SELECT Salary FROM Employees 
ORDER BY Salary DESC 
LIMIT 1 OFFSET 1; - Postgres/MySQL
- Microsoft SQL: TOP 1 ...
```

### Problem 2: Employees earning more than their Managers
**Self Join**:
```sql
SELECT E.Name 
FROM Employees E
JOIN Employees M ON E.ManagerId = M.Id
WHERE E.Salary > M.Salary;
```

### Problem 3: Departments with no employees
**Using LEFT JOIN**:
```sql
SELECT D.Name 
FROM Departments D
LEFT JOIN Employees E ON D.Id = E.DeptId
WHERE E.Id IS NULL;
```
**Using NOT EXISTS**:
```sql
SELECT Name FROM Departments D
WHERE NOT EXISTS (SELECT 1 FROM Employees E WHERE E.DeptId = D.Id);
```

--

## 10. Key Topics & Explanations

### Topic 1: What is a View?
**Answer:** A virtual table based on the result-set of an SQL statement. It doesn't store data itself but displays data stored in other tables. Used for security (hiding columns) and simplifying complex joins.

### Topic 2: What is Normalization?
**Answer:** The process of organizing data to reduce redundancy (duplication) and improve data integrity.
- **1NF**: Atomic values (No comma-separated lists).
- **2NF**: 1NF + No Partial Dependency (All non-key cols depend on Full PK).
- **3NF**: 2NF + No Transitive Dependency (Non-key col shouldn't depend on another non-key col).

### Topic 3: What is Denormalization?
**Answer:** Adding redundancy back to a normalized schema to improve read performance (avoiding expensive joins). Used in Data Warehousing.

### Topic 4: HAVING vs WHERE?
**Answer:** WHERE filters rows **before** aggregation. HAVING filters groups **after** aggregation. WHERE cannot use aggregate functions like `COUNT() > 5`; HAVING can.

### Topic 5: What is an Index?
**Answer:** A data structure (B-Tree) that improves the speed of data retrieval operations. It works like a book index.
- **Clustered Index**: Sorts the physical data rows (Only 1 per table).
- **Non-Clustered Index**: Separate structure pointing to data rows (Multiple allowed).

### Topic 6: Can we rollback a TRUNCATE command?
**Answer:** Generally NO in MySQL/Oracle as it is DDL (auto-commit). However, in SQL Server, if wrapped in a TRANSACTION, it can be rolled back.
