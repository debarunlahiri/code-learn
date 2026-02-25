# AWS Cost Optimization

## 1. Cost Visibility
- Use Cost Explorer
- Use AWS Budgets alerts
- Enable CUR (Cost and Usage Report)
- Tag resources for cost allocation

## 2. Compute Cost Optimization
- Right-size EC2
- Use Savings Plans/Reserved Instances for steady workloads
- Use Spot for fault-tolerant workloads
- Scale down non-prod environments off-hours

## 3. Storage Cost Optimization
- S3 lifecycle transitions
- Delete orphaned EBS volumes/snapshots
- Choose right storage class

## 4. Database Cost Optimization
- Right-size DB instances
- Stop non-prod DB when possible
- Use Aurora Serverless / DynamoDB on-demand for variable workloads

## 5. Data Transfer Awareness
- Inter-region transfer can be expensive
- Minimize NAT Gateway-heavy patterns where avoidable
- Use CloudFront to reduce origin egress

## 6. Governance for Cost
- Budget alarms per account/team
- SCP/policy guardrails to avoid costly resources by default
- Monthly architecture-cost review
