# AWS Advanced Interview Deep Dive

## 1. Well-Architected Framework Deep Dive

### Operational Excellence Pillar
- Design for operations using CloudFormation StackSets
- Implement blue-green deployments
- Use AWS CodePipeline for CI/CD
- Enable CloudWatch Application Insights for automatic monitoring
- Implement automated runbooks with Systems Manager Automation

### Security Pillar
- Implement defense in depth: IAM → VPC → SG → WAF → Shield
- Use AWS Private Certificate Authority for TLS certificates
- Implement AWS IAM Identity Center for workforce identity
- Use AWS Secrets Manager with automatic rotation
- Enable AWS Config for resource configuration tracking

### Reliability Pillar
- Design for failure with multi-AZ architectures
- Implement automated recovery with CloudWatch alarms + Lambda
- Use AWS Backup for centralized backup management
- Implement circuit breaker patterns
- Design graceful degradation strategies

### Performance Efficiency Pillar
- Right-size resources using Cost Explorer recommendations
- Use AWS Compute Optimizer for EC2, Lambda, EBS
- Implement caching layers with ElastiCache
- Use Aurora Auto Scaling for read replicas
- Leverage Lambda provisioned concurrency for predictable workloads

### Cost Optimization Pillar
- Use Savings Plans for compute commitment discounts
- Implement Spot Instances for fault-tolerant workloads
- Use S3 Intelligent-Tiering for automatic cost savings
- Implement lifecycle policies for resources
- Use Cost Allocation Tags for granular cost tracking

### Sustainability Pillar
- Use AWS Graviton2 instances for better performance/watt
- Implement serverless architectures to reduce idle capacity
- Use S3 Lifecycle policies to move data to colder storage
- Right-size compute to avoid over-provisioning
- Use CloudFront to reduce origin requests

---

## 2. Advanced Networking

### Transit Gateway
- Central hub for VPC-to-VPC and VPN connections
- Supports transitive routing between VPCs
- Can attach up to 5000 VPCs per Transit Gateway
- Use Transit Gateway Route Tables for routing control
- Integrates with AWS Direct Connect and VPN
- Share Transit Gateway across accounts using RAM

### AWS PrivateLink
- Access services privately without internet traversal
- Create Interface VPC Endpoints for AWS services
- Create Interface Endpoints for third-party SaaS services
- Endpoint services for sharing services with other accounts
- Traffic stays within AWS network
- Requires private DNS configuration

### Direct Connect
- Dedicated 1Gbps/10Gbps/100Gbps connections
- Supports LAG for link aggregation
- Use with Direct Connect Gateway for multi-region access
- Public and private virtual interfaces
- MacSec for layer 2 encryption
- Partner-hosted connections via AWS Direct Connect Partners

### VPN vs Direct Connect vs Transit Gateway
- **VPN**: Quick setup, internet-based, lower cost, higher latency
- **Direct Connect**: Dedicated connection, consistent low latency, higher cost, longer setup
- **Transit Gateway**: Simplifies complex routing, supports VPN + Direct Connect, scales better

### VPC Peering vs Transit Gateway
- **VPC Peering**: Point-to-point, no transitive routing, limited to 125 peerings per VPC
- **Transit Gateway**: Hub-and-spoke model, supports transitive routing, scales to thousands of VPCs

### DNS Design in VPC
- Enable DNS hostnames and DNS support
- Use Route 53 Resolver for hybrid DNS
- Create private hosted zones for internal domains
- Use Route 53 resolver rules for conditional forwarding
- Enable DNSSEC for additional security

---

## 3. Serverless Advanced Patterns

### AWS Step Functions
- Orchestrate microservices into workflows
- Use Standard Workflows for long-running workflows
- Use Express Workflows for high-volume, short-lived workflows
- Implement error handling with Retry and Catch
- Use Map state for parallel processing
- Integrate with X-Ray for distributed tracing
- Use callback pattern for task token patterns

### Amazon EventBridge
- Event bus for connecting applications
- Schema discovery and registry
- Event replay with custom event buses
- Cross-account event routing
- Dead letter queues for failed events
- Event filtering to reduce event size

### Lambda Cold Start Optimization
- Increase memory allocation (more CPU = faster init)
- Use Provisioned Concurrency for predictable performance
- Minimize deployment package size
- Use layers for shared dependencies
- Avoid initializing heavy objects at cold start
- Use lazy loading for external dependencies
- Consider VPC configuration impact

### AWS SAM (Serverless Application Model)
- Template shorthand for serverless resources
- Local testing with sam local
- Build and package with sam build
- Deploy with sam deploy
- Test with sam sync
- Package as container images

### Lambda@Edge
- Run Lambda at CloudFront edge locations
- Modify request/response headers
- Generate dynamic responses
- A/B testing at edge
- Request routing based on cookies
- Viewer vs origin request functions

---

## 4. Kubernetes on AWS (EKS)

### EKS Cluster Architecture
- Control plane managed by AWS
- Worker nodes in your VPC
- CNI plugin for pod networking
- CoreDNS for service discovery
- kube-proxy for network rules
- EKS Add-ons for cluster components

### Fargate vs EC2 for EKS
- **Fargate**: Serverless, pay per pod, less control, simpler management
- **EC2**: More control, better for consistent workloads, cost-effective at scale

### Karpenter
- Node auto-provisioning for Kubernetes
- Provisioner CRD for scaling rules
- Spot instances for cost savings
- Fast node replacement
- Consolidation to reduce costs

### EKS Add-ons
- CoreDNS for DNS service
- VPC CNI for networking
- kube-proxy for network rules
- AWS Load Balancer Controller
- EBS CSI Driver
- EFS CSI Driver

### Service Mesh on EKS
- AWS App Mesh (now AWS Mesh)
- Istio on EKS
- Linkerd on EKS
- Envoy sidecar pattern
- mTLS between services

---

## 5. Data Engineering on AWS

### Kinesis Data Streams
- Shards for parallel processing
- Kinesis Producer Library (KPL) for efficient producers
- Kinesis Consumer Library (KCL) for consumers
- Enhanced fan-out for dedicated consumers
- Partition key strategies for even distribution
- Retention up to 365 days

### Kinesis Data Firehose
- Fully managed delivery to S3, Redshift, Elasticsearch
- Automatic scaling
- Data transformation with Lambda
- Data format conversion (Parquet/ORC)
- Buffer size and interval configuration

### AWS Glue
- Serverless ETL service
- Glue Data Catalog for metadata
- Glue Jobs for ETL pipelines
- Glue Studio for visual ETL
- Glue DataBrew for data preparation
- Glue Elastic Views for materialized views

### Lake Formation
- Fine-grained access control for data lakes
- Table and column-level permissions
- Row-level security
- Data lake organization
- Integration with S3 and Lake Formation grants

### Redshift Spectrum
- Query data in S3 without loading
- Spectrum nodes for query processing
- Partition data for better performance
- Use Athena for ad-hoc queries
- RA3 nodes for managed storage

### Athena
- Serverless SQL on S3
- Partition and bucket data
- Use columnar formats (Parquet, ORC)
- Workgroups for query isolation
- Cost tracking per query

---

## 6. Advanced Security

### AWS GuardDuty
- Continuous security monitoring
- Machine learning for threat detection
- Multiple data sources: CloudTrail, VPC Flow Logs, DNS logs
- EKS audit log analysis
- S3 data events
- Findings in Security Hub

### AWS Security Hub
- Central security view
- Aggregates findings from multiple services
- Security standards (CIS, PCI DSS, AWS Best Practices)
- Custom insights
- Automated remediation with EventBridge
- Integration with AWS Config

### AWS Config
- Resource configuration tracking
- Config rules for compliance
- Conformance packs for multi-account compliance
- Remediation actions with Automation documents
- Configuration history and snapshots
- Query resources with advanced query

### AWS WAFv2
- Web ACLs with rules
- Rate-based rules for DDoS
- Rule groups for reusable rule sets
- AWS Managed Rules
- CloudFront, ALB, API Gateway integration
- Logging to Kinesis Data Firehose

### AWS Shield
- **Standard**: Free, always-on DDoS protection
- **Advanced**: Additional protection, 24/7 DDoS response team, WAF included, cost protection
- Shield Advanced for critical applications

### KMS Advanced Features
- Envelope encryption for large data
- Custom key stores (CloudHSM integration)
- Key rotation for symmetric keys
- Multi-Region keys for global applications
- Grants for temporary access
- Import your own key material

---

## 7. AWS Organizations & Control Tower

### Organization Units (OU) Design
- Root OU
- Sandbox OU for experimentation
- Production OU for production workloads
- Security OU for security tools
- Shared Services OU for common services
- Suspended OU for non-compliant accounts

### SCP Deep Dive
- Deny lists vs Allow lists
- Prevent accidental deletions
- Restrict regions
- Enforce encryption standards
- Deny access to specific services
- Combine with IAM for least privilege

### AWS Control Tower
- Automated landing zone
- Guardrails for governance
- Account Factory for provisioning
- Detective controls
- Preventive controls
- Shared accounts (audit, log archive)

### AWS Service Catalog
- Portfolio management
- Product definitions
- Tag options for products
- Access control
- Self-service provisioning
- Versioning and updates

---

## 8. Container Deep Dive

### ECS Task Definitions
- Container definitions
- Task-level IAM role
- Network mode (bridge, host, awsvpc)
- Storage options (EFS, bind mounts)
- Log configuration
- Health checks

### Amazon ECR
- Container image registry
- Image scanning for vulnerabilities
- Lifecycle policies
- Cross-region replication
- Pull through cache rules
- Public registry for containers

### Fargate vs EC2 Decision
- **Fargate**: DevOps simplicity, serverless, pay per task, no node management
- **EC2**: Cost control, custom AMIs, persistent storage, GPUs

### Service Discovery
- Cloud Map for service registration
- DNS-based discovery
- Health checking
- ECS Service Connect
- AWS Cloud Map API

### ECS Blue-Green Deployment
- CodeDeploy integration
- Traffic shifting
- Original and replacement tasks
- Automatic rollback
- Manual approval option

---

## 9. API Gateway Advanced

### Usage Plans and API Keys
- Throttling limits (rate and burst)
- Quota limits
- API key association
- Usage plan scoping
- Client tracking

### Request/Response Mapping
- Mapping templates with VTL
- Integration with Lambda
- Default mappings
- Custom header handling
- Body transformation

### Caching Strategies
- Cache at API Gateway level
- Cache key parameters
- TTL configuration
- Cache invalidation
- Encryption at rest
- Cache responses for GET only

### WebSocket APIs
- Connection management
- Callback URL configuration
- Message routing with $routeKey
- DynamoDB for connection state
- Integration with Lambda

### API Gateway Advanced Security
- Lambda authorizer (token and request)
- IAM authorization
- Cognito User Pools
- Resource policies
- WAF integration
- TLS 1.2 minimum

---

## 10. Caching Strategies

### ElastiCache Redis
- Cluster modes: Standalone, Cluster-enabled
- Read replicas for read scaling
- Redis Auth for authentication
- Backup and restore
- Multi-AZ with auto-failover
- Pub/Sub for messaging

### ElastiCache Memcached
- Multi-node with auto discovery
- Larger node types
- No persistence
- Memcached protocol
- Auto discovery client required

### CloudFront Caching
- Cache behaviors by path pattern
- Origin settings and headers
- Query string forwarding
- Cookie forwarding
- TTL and default TTL
- Invalidation patterns

### DAX (DynamoDB Accelerator)
- In-memory cache for DynamoDB
- Microsecond latency
- Fully managed
- Point-in-time recovery compatible
- Read-through cache
- Eventual consistency

---

## 11. Infrastructure as Code Deep Dive

### CloudFormation
- Templates (JSON/YAML)
- Nested stacks for modularity
- StackSets for multi-account
- Drift detection
- Change sets for preview
- Custom resources with Lambda

### Terraform vs CloudFormation
- **Terraform**: Multi-cloud, state management, plan/apply, larger ecosystem
- **CloudFormation**: Native AWS, no state file, rollback support, better integration

### AWS CDK
- Infrastructure as actual code
- Multiple language support (TypeScript, Python, Java, C#, Go)
- Constructs for reusable components
- CloudFormation synthesis
- CDK Pipelines for CI/CD
- Aspects for cross-cutting concerns

### IaC Best Practices
- Version control for all templates
- Modular components
- Parameterization
- Guardrails and validations
- Automated testing (cfn-lint, checkov)
- State management (remote for Terraform)

---

## 12. Performance Optimization

### CloudWatch RUM
- Real user monitoring
- Performance metrics
- JavaScript errors
- Page load times
- User sessions
- Integration with X-Ray

### AWS X-Ray
- Distributed tracing
- Sampling rules
- Service maps
- Annotations and metadata
- Segment vs subsegment
- Integration with Lambda, ECS, EC2

### Performance Insights
- Database performance monitoring
- Wait events analysis
- Performance metrics dashboard
- SQL query analysis
- Aurora and RDS support

### RDS Performance Insights
- Enhanced monitoring
- Performance metrics
- SQL statement analysis
- Wait event breakdown
- DB load visualization

---

## 13. Disaster Recovery Deep Dive

### RTO/RPO Planning
- RTO (Recovery Time Objective): Max acceptable downtime
- RPO (Recovery Point Objective): Max acceptable data loss
- Define based on business requirements

### DR Strategies Comparison
| Strategy | RTO | RPO | Cost | Description |
|----------|-----|-----|------|-------------|
| Backup/Restore | Hours | Hours | Low | Restore from backups |
| Pilot Light | Minutes | Minutes | Medium | Core services running minimal |
| Warm Standby | Minutes | Seconds | High | Full scale but smaller |
| Active-Active | Near Zero | Near Zero | Highest | Full deployment in both sites |

### Backup Strategies
- Automated daily backups
- Cross-region backup replication
- Point-in-time recovery
- Snapshot copy to another region
- AWS Backup for centralized management

### Chaos Engineering
- AWS Fault Injection Simulator
- Chaos experiments on ECS, EKS, EC2
- Controlled failure injection
- Build resilient systems

---

## 14. Cost Management Advanced

### Savings Plans
- Compute Savings Plans (EC2, Lambda, Fargate)
- EC2 Instance Savings Plans
- Up to 72% savings
- Commitment in USD per hour
- Flexible instance family, size, AZ

### Reserved Instance Pricing
- Standard: Up to 72% discount, 1 or 3 year
- Convertible: Up to 54% discount, exchange for different RI
- Scheduled: For predictable recurring workloads

### Spot Fleet
- Automatic replacement
- Allocation strategies: lowestPrice, diversified, capacityOptimized
- Spot block for uninterrupted workloads
- Max price configuration

### Cost Explorer
- Visualize spending patterns
- Forecast future costs
- Identify savings opportunities
- Filter by service, account, tag
- CUR (Cost and Usage Report) integration

### Cost Allocation Tags
- AWS generated tags (e.g., aws:createdBy)
- User-defined tags
- Tag policies enforcement
- Cost categorization
- Granular cost tracking

---

## 15. Additional Advanced Topics

### AWS Global Accelerator
- Anycast IP addresses
- 2-3x latency improvement
- Health checks and failover
- Integration with ALB
- Traffic dials for percentage routing

### AWS AppSync
- GraphQL API service
- Real-time subscriptions
- Offline client support
- Multiple data sources (DynamoDB, Lambda, HTTP)
- Conflict resolution

### AWS Amplify
- Frontend web/mobile framework
- CI/CD built-in
- Hosting with global CDN
- Authentication
- DataStore for offline sync

### AWS Cognito
- User pools for app users
- Identity pools for AWS access
- Social identity providers
- MFA support
- Lambda triggers for customization
- Advanced security features

### AWS Systems Manager
- Parameter Store for configuration
- Session Manager for EC2 access
- Automation for runbooks
- State Manager for compliance
- Patch Manager for updates
- Incident Manager for responses

### AWS Backup
- Centralized backup service
- Cross-region backup
- Backup policies
- Lifecycle management
- Copy on demand
- Vault lock for compliance

---

## 16. System Design Interview Patterns

### Design URL Shortener
- DynamoDB for URL storage
- Lambda for business logic
- API Gateway for endpoints
- S3 for static assets
- Route 53 for DNS

### Design Notification System
- SNS for topic-based pub/sub
- SQS for queue-based processing
- Kinesis for high-volume events
- Push notification services (Pinpoint)

### Design Video Upload System
- S3 for storage
- Lambda for processing
- MediaConvert for transcoding
- CloudFront for delivery
- DynamoDB for metadata

### Design Chat Application
- API Gateway + WebSocket
- Lambda for business logic
- DynamoDB for message storage
- ElastiCache for session/caching
- Cognito for authentication

---

## 17. Final Interview Tips

### Answer Structure
1. Clarify requirements with questions
2. High-level architecture
3. Component breakdown
4. Data flow
5. Failure scenarios
6. Scaling considerations

### Key Principles to Mention
- Security at every layer
- Design for failure
- Automate everything
- Monitor everything
- Cost-conscious architecture
- Use managed services

### Common Trade-offs to Discuss
- Consistency vs Availability (CAP theorem)
- Cost vs Performance
- Simplicity vs Flexibility
- Latency vs Throughput
- Build vs Buy (managed services)
