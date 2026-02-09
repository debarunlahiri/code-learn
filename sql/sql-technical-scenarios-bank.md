# SQL Technical Proficiency Bank - Ultimate Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Purpose:** A comprehensive repository of 40+ technical inquiries and scenarios covering Database concepts, complex queries, and performance tuning.

---

## Table of Contents

1. [Core Database Concepts](#1-core-database-concepts)
2. [SQL Statements & Operations](#2-sql-statements--operations)
3. [Joins & Relationships](#3-joins--relationships)
4. [Advanced Queries](#4-advanced-queries)
5. [Database Design & Normalization](#5-database-design--normalization)
6. [Performance Tuning & Indexing](#6-performance-tuning--indexing)
7. [Stored Procedures & Triggers](#7-stored-procedures--triggers)

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
