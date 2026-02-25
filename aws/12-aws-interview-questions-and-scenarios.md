# AWS Interview Questions and Scenarios

## 1. Concept Questions

### Q1: Explain Security Group vs NACL.
Security Group is stateful and instance-level. NACL is stateless and subnet-level.

### Q2: Difference between RDS Multi-AZ and Read Replica?
Multi-AZ for high availability/failover. Read replica for read scaling.

### Q3: Why use IAM role instead of access keys in EC2?
Roles provide temporary credentials and reduce secret management risk.

## 2. Architecture Scenario Questions

### Scenario 1: Design highly available web app
- ALB in front
- Auto Scaling EC2/ECS across at least 2 AZ
- RDS Multi-AZ
- CloudFront + WAF
- S3 for static content

### Scenario 2: Build serverless order API
- API Gateway
- Lambda
- DynamoDB
- SQS for async processing
- CloudWatch + X-Ray for observability

### Scenario 3: Secure multi-account enterprise setup
- AWS Organizations
- SCP guardrails
- Centralized logging/security account
- IAM Identity Center for federated access

## 3. Troubleshooting Questions

### EC2 unreachable
Check: SG, NACL, route table, IGW, instance status, key pair, OS firewall.

### High Lambda latency
Check: cold start, memory setting, external dependency latency, VPC config.

### S3 Access Denied
Check: IAM policy, bucket policy, block public access, KMS key permissions.

## 4. Decision Questions

### ECS vs EKS?
- ECS for simpler AWS-native container operations.
- EKS for Kubernetes portability and ecosystem.

### RDS vs DynamoDB?
- RDS for relational SQL/joins/transactions with fixed schema.
- DynamoDB for massive scale, low-latency key-value/document patterns.

## 5. Final Preparation Checklist
1. Revise IAM, VPC, EC2, S3, RDS, Lambda deeply.
2. Practice 3 end-to-end architectures.
3. Prepare tradeoff-based answers, not only definitions.
4. Be ready with cost + security + scaling decisions.
