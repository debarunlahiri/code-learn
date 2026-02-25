# AWS Compute Services

## 1. EC2 (Virtual Servers)

### When to use
- Full OS-level control needed
- Legacy workloads
- Custom runtime requirements

### Important concepts
- Instance types (general, compute, memory, storage optimized)
- AMIs
- EBS volumes
- Auto Scaling Group (ASG)
- Elastic Load Balancer (ALB/NLB)

### Pricing models
- On-Demand
- Savings Plans
- Reserved Instances
- Spot Instances

## 2. Elastic Load Balancing
- ALB: HTTP/HTTPS, layer 7 routing
- NLB: TCP/UDP, high performance
- GWLB: security appliances

## 3. Auto Scaling
- Dynamic scaling (CPU/requests/custom metrics)
- Scheduled scaling
- Predictive scaling

## 4. ECS and EKS

### ECS
Managed container orchestration by AWS.

### EKS
Managed Kubernetes service.

Use ECS if you want simpler AWS-native container ops.
Use EKS if Kubernetes portability/ecosystem is mandatory.

## 5. AWS Lambda (Serverless)

Run code without managing servers.

### Key points
- Event-driven
- Pay per request + compute time
- Cold starts possible
- Max execution time limits

### Common triggers
- API Gateway
- S3
- EventBridge
- SQS
- DynamoDB streams

## 6. API Gateway
- Front door for APIs
- Auth integration (Cognito, IAM, Lambda authorizer)
- Rate limiting/throttling
- Caching

## 7. Elastic Beanstalk and App Runner
- Beanstalk: PaaS-like managed deployment for web apps
- App Runner: simple container/web app deployment with minimal ops
