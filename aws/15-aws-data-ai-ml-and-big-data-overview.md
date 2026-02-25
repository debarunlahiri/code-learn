# AWS Data, AI/ML, and Big Data Overview

## 1. Data Engineering Stack
- Ingest: Kinesis, MSK, Glue connectors
- Store: S3 data lake
- Process: Glue, EMR, Lambda
- Query: Athena, Redshift
- Govern: Lake Formation

## 2. Streaming Services
- Kinesis Data Streams
- Kinesis Data Firehose
- Kinesis Data Analytics

## 3. Machine Learning Services
- Amazon SageMaker (build/train/deploy ML models)
- Bedrock (foundation models and generative AI)
- Rekognition, Comprehend, Textract, Transcribe (AI APIs)

## 4. BI and Visualization
- Amazon QuickSight for dashboards and BI analytics

## 5. Data Governance
- Glue Data Catalog
- Lake Formation permissions
- Encryption with KMS
- PII discovery with Macie

## 6. Common Architecture
- Source systems -> Kinesis/Glue -> S3
- Curated data -> Athena/Redshift
- ML model in SageMaker
- Dashboard in QuickSight
