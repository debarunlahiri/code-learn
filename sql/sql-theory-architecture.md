# Database Design & Architecture - Theory Guide

**Target:** Senior Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** SQL vs NoSQL, CAP Theorem, ACID vs BASE, Scaling, Sharding, and Database Design.

--

## Table of Contents

1. [SQL vs NoSQL](#sql-vs-nosql)
2. [CAP Theorem](#cap-theorem)
3. [ACID vs BASE](#acid-vs-base)
4. [Scaling Strategies (Vertical vs Horizontal)](#scaling-strategies-vertical-vs-horizontal)
5. [Sharding & Partitioning](#sharding-partitioning)
6. [Database Normalization (Recap)](#database-normalization-recap)
7. [ER Diagrams (Entity-Relationship)](#er-diagrams-entity-relationship)
8. [Key Topics & Explanations](#key-topics-explanations)

--

## 1. SQL vs NoSQL

| Feature | SQL (Relational) | NoSQL (Non-Relational) |
|-----|----------|-------------|
| **Structure** | Tables, Rows, Columns (Schema-based) | Document, Key-Value, Graph, Columnar (Schema-less) |
| **Scalability** | Vertical (Scale Up - bigger server) | Horizontal (Scale Out - add servers) |
| **Joins** | Efficient Joins | No Joins (or limited/expensive) |
| **ACID** | Yes (Atomicity, Consistency, Isolation, Durability) | Sometimes (BASE model often used) |
| **Use Case** | Financial systems, structured data relates | Big Data, Real-time apps, Unstructured data |
| **Examples** | MySQL, PostgreSQL, Oracle, SQL Server | MongoDB, Redis, Cassandra, Neo4j, Couchbase |

### When to choose NoSQL?
1. **Dynamic Schema**: Data structure changes frequently.
2. **Horizontal Scaling**: Need to handle massive data volume cheaply.
3. **High Write Throughput**: Logging, IoT data.
4. **Specific Data Models**: Graphs (Social networks), Key-Value (Caching).

--

## 2. CAP Theorem

In a distributed system, you can only pick **two** of the following three guarantees:

1. **Consistency (C)**: Every read receives the most recent write or an error. (All nodes see same data at same time).
2. **Availability (A)**: Every request receives a (non-error) response, without guarantee that it contains the most recent write.
3. **Partition Tolerance (P)**: System continues to operate despite arbitrary message loss or failure of part of the system.

**Since network failures (P) are inevitable, you MUST choose between CP and AP:**

- **CP (Consistency + Partition Tolerance)**: MongoDB, HBase, Redis (if configured). 
  - *Trade-off*: If a partition occurs, the system stops accepting writes to maintain consistency.
- **AP (Availability + Partition Tolerance)**: Cassandra, DynamoDB, Couchbase.
  - *Trade-off*: System remains available, but might return stale data (EVENTUAL Consistency).

**CA (Consistency + Availability)**: RDBMS (MySQL, Postgres).
  - *Trade-off*: Cannot handle network partitions well. Generally single-node master.

--

## 3. ACID vs BASE

### ACID (SQL)
- **Atomicity**: Transaction is all or nothing.
- **Consistency**: Database valid before/after transaction.
- **Isolation**: Transactions don't interfere.
- **Durability**: Data saved permanently.

### BASE (NoSQL)
- **Basically Available**: System guarantees availability (AP).
- **Soft state**: State of system may change over time (due to eventual consistency).
- **Eventual consistency**: System will become consistent over time, given that system doesn't receive input during that time.

--

## 4. Scaling Strategies (Vertical vs Horizontal)

### Vertical Scaling (Scale Up)
- **Definition**: Adding more power (CPU, RAM) to an existing machine.
- **Pros**: Simple (no code changes).
- **Cons**: Expensive, Single Point of Failure (SPOF), Hardware limits.
- **Use Case**: RDBMS (MySQL, Oracle).

### Horizontal Scaling (Scale Out)
- **Definition**: Adding more machines to the resource pool.
- **Pros**: Cheaper (commodity hardware), unlimited theoretical scale, improved fault tolerance.
- **Cons**: Complex management, data consistency issues (CAP theorem).
- **Use Case**: NoSQL (Cassandra, MongoDB).

--

## 5. Sharding & Partitioning

### Sharding (Horizontal Partitioning)
Splitting a large dataset into smaller chunks (shards) distributed across multiple servers.

- **Example**: User ID 1-1000 -> Server A, 1001-2000 -> Server B.
- **Pros**: Infinite write scalability.
- **Cons**: Joins across shards are impossible/expensive. Rebalancing is hard.

### Partitioning Strategies
1. **Range Based**: By ID range or Date (e.g., Jan data -> Server 1). *Problem: Hotspots.*
2. **Hash Based**: `Hash(ID) % N` determines server. *Pros: Even distribution.*
3. **Directory Based**: Lookup table maps ID to Server.

--

## 6. Database Normalization (Recap)

Organizing data to **reduce redundancy** and improve **integrity**.

- **1NF**: Atomic values.
- **2NF**: 1NF + No partial dependency.
- **3NF**: 2NF + No transitive dependency.
- **BCNF**: Strict 3NF.

**Denormalization**: Intentionally adding redundancy (e.g., repeating `UserName` in `Comments` table) to avoid expensive JOINs in read-heavy applications.

--

## 7. ER Diagrams (Entity-Relationship)

Visual representation of data.

### Relationships
1. **One-to-One (1:1)**: User <-> Profile. (Rare, usually merged).
2. **One-to-Many (1:N)**: User <-> Orders. (Foreign Key in Orders table).
3. **Many-to-Many (N:N)**: Students <-> Courses. (Requires **Junction Table** `StudentCourses`).

--

## 8. Key Topics & Explanations

### Topic 1: What is the N+1 Query Problem?
**Answer:** A performance issue where the application executes 1 initial query to get N records, and then N additional queries to fetch related data for each record.
- **Bad**: `SELECT * FROM Users` -> Loop users -> `SELECT * FROM Comments WHERE UserId = ?`.
- **Fix**: Use JOINs (`SELECT * FROM Users JOIN Comments...`) or Batch Fetching (`WHERE UserId IN (...)`).

### Topic 2: Indexing Trade-offs?
**Answer:**
- **Pros**: Faster READS (Select).
- **Cons**: Slower WRITES (Insert/Update/Delete) because index must be updated. Requires more disk space.

### Topic 3: When to use Denormalization?
**Answer:** In read-heavy systems (e.g., Analytics, Reporting) where JOIN performance is the bottleneck. Trade-off is data consistency becomes harder to maintain (update anomalies).

### Topic 4: Explain "Eventual Consistency".
**Answer:** A consistency model used in distributed systems (AP) where, if no new updates are made, eventually all accesses will return the last updated value. Real-world example: DNS updates, Facebook likes count (might be different on different servers for a few seconds).
