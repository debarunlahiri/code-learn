# AWS Architecture Patterns and Best Practices

## 1. High Availability
- Multi-AZ deployment
- Load balancers across AZs
- DB Multi-AZ

## 2. Fault Isolation
- Cell-based architecture
- Decouple with SQS/SNS/EventBridge
- Graceful degradation

## 3. Scalability Patterns
- Horizontal scale over vertical where possible
- Auto Scaling with stateless services
- Use caching and CDN

## 4. Microservices on AWS
- Compute: ECS/EKS/Lambda
- Communication: API Gateway + EventBridge/SQS
- Data: database per service (where sensible)

## 5. Serverless Reference Pattern
- API Gateway -> Lambda -> DynamoDB
- S3 + CloudFront for frontend hosting
- Cognito for authentication

## 6. Multi-account Landing Zone Pattern
- Separate accounts: prod, staging, dev, shared services, security, logging
- Centralized identity and audit

## 7. Disaster Recovery Strategies
- Backup and restore
- Pilot light
- Warm standby
- Multi-site active-active

Choose based on RTO/RPO targets and cost constraints.
