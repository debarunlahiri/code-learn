# AWS Interview Questions and Answers (Advanced)

## 1. What is the difference between horizontal and vertical scaling in AWS?
Vertical scaling means increasing instance size (e.g., `t3.medium` to `m6i.2xlarge`).  
Horizontal scaling means adding more instances behind a load balancer.  
AWS strongly favors horizontal scaling for high availability and fault tolerance.

## 2. How do you design a secure VPC for a 3-tier app?
- Public subnets: ALB only
- Private app subnets: ECS/EC2/Lambda
- Private DB subnets: RDS/Aurora
- Security groups with least privilege
- NAT Gateway for controlled outbound internet from private subnets
- VPC endpoints for private access to AWS services (S3, DynamoDB, etc.)

## 3. Multi-AZ vs Multi-Region?
- Multi-AZ: protects against AZ failure inside one region (HA).
- Multi-Region: protects against regional outage and supports geo-latency/compliance goals (DR/global architecture).

## 4. How would you reduce AWS bill without reducing reliability?
- Right-size compute and DB instances
- Use Savings Plans/Reserved pricing for baseline workloads
- Move burst workloads to Spot where safe
- Apply S3 lifecycle rules
- Remove idle resources (orphaned EBS, unattached EIP)
- Introduce autoscaling and scheduled shutdown for non-prod

## 5. S3 strong consistency - what does it change?
S3 now provides strong read-after-write and list consistency for all operations.  
This simplifies many data workflows because immediate reads/lists reflect latest writes.

## 6. Explain eventual consistency scenarios still relevant in AWS architecture.
Even if many services are strongly consistent in their own scope, distributed systems still have eventual behavior across regions, async replication, caches, and event-driven pipelines.  
Design with retries, idempotency, and observability.

## 7. How do you secure secrets in AWS?
- Store in Secrets Manager or SSM Parameter Store
- Encrypt with KMS
- Access using IAM role permissions
- Rotate automatically where possible
- Never store secrets in code, AMI, container image, or plain environment files

## 8. How do you handle throttling from AWS APIs/services?
- Exponential backoff with jitter
- Request rate control
- Queue-based buffering (SQS)
- Service quota review and increase requests where justified

## 9. What is the purpose of dead-letter queues (DLQ)?
DLQ stores messages/events that fail processing after retry limits, so failures are isolated and can be replayed/debugged without blocking healthy traffic.

## 10. How do you design idempotent serverless workflows?
- Use request IDs / idempotency keys
- Persist processed IDs in DynamoDB
- Make writes conditional
- Treat retries as normal behavior

## 11. What metrics would you monitor for production APIs?
- Availability (success rate)
- Latency (p50/p95/p99)
- Error rate by endpoint
- Request volume
- Saturation metrics (CPU/memory/connections/queue depth)

## 12. EC2 vs Lambda decision framework?
- Lambda: event-driven, bursty, short-lived, minimal ops
- EC2: long-running processes, custom OS/runtime, predictable steady usage
- Consider cold start, execution limits, cost profile, and operational ownership

## 13. How do you design disaster recovery for critical systems?
- Define RTO/RPO first
- Choose strategy: backup/restore, pilot light, warm standby, active-active
- Automate recovery runbooks
- Regularly test failover and failback

## 14. What is blast radius and how to reduce it?
Blast radius is the potential impact area of a failure/change.  
Reduce via isolation boundaries: multi-account setup, segmented VPCs, cell architecture, scoped IAM, progressive deployments.

## 15. Explain SCP vs IAM policy.
- IAM policy: permissions for principal/resources inside account.
- SCP (Service Control Policy): guardrail at organization/OU/account level; it sets maximum allowed actions.
- Effective permission must pass both.

## 16. What are common root causes of “AccessDenied” in AWS?
- Missing IAM allow
- Explicit deny in IAM/SCP/resource policy
- Wrong assumed role/session
- Missing KMS permission
- Region/resource ARN mismatch

## 17. Why use private subnets for app servers?
Improves security posture by removing direct inbound internet exposure. Ingress is controlled via load balancer or API gateway.

## 18. How do you migrate a monolith to AWS safely?
- Start with strangler pattern
- Introduce observability first
- Split by bounded contexts
- Move one slice at a time
- Keep rollback path for each migration wave

## 19. How do you improve reliability of event-driven systems?
- Retries with backoff
- DLQ and replay tooling
- Idempotent consumers
- Schema versioning
- End-to-end tracing and alerting

## 20. Final interview tip
For each architecture answer, always mention:
1. Security
2. Availability
3. Scalability
4. Cost
5. Operational complexity

