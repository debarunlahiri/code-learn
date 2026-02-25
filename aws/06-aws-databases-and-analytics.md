# AWS Databases and Analytics

## 1. Relational Databases: RDS and Aurora

### RDS
Managed MySQL/PostgreSQL/MariaDB/Oracle/SQL Server.

### Aurora
AWS cloud-native relational DB (MySQL/PostgreSQL compatible), higher performance and availability.

### Key concepts
- Multi-AZ for HA
- Read replicas for scaling reads
- Automated backups and snapshots

## 2. DynamoDB (NoSQL)

Serverless key-value/document DB.

### Design model
- Partition key
- Sort key
- Access-pattern-driven schema design

### Features
- On-demand/provisioned capacity
- Global tables
- TTL
- Streams

## 3. ElastiCache
- Redis and Memcached managed caching
- Reduces DB load and latency

## 4. Redshift
- Data warehouse for analytics at scale
- Columnar storage and MPP architecture

## 5. Data Lake Analytics Stack
- S3 as data lake
- Glue for ETL + data catalog
- Athena for serverless SQL queries on S3
- EMR for big-data frameworks (Spark/Hadoop)

## 6. Database Selection Cheat Sheet
- OLTP relational: RDS/Aurora
- High-scale NoSQL: DynamoDB
- In-memory cache: ElastiCache
- Analytics warehouse: Redshift
