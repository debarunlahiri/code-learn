# AWS Services Complete Reference Guide

## 1. EC2 (Elastic Compute Cloud)

### What is EC2?
Virtual servers in the cloud for running applications.

### Instance Types
- **General Purpose (t, m, mac)**: Balanced compute/memory, web servers, dev environments
- **Compute Optimized (c)**: High performance processors, HPC, batch processing
- **Memory Optimized (r, x, z)**: Large datasets, in-memory caches
- **Accelerated Computing (p, g, inf)**: GPU workloads, machine learning, video encoding
- **Storage Optimized (i, d, h)**: High IOPS, data warehousing, distributed file systems

### Purchasing Options
- **On-Demand**: Pay per second, no commitment
- **Savings Plans**: Commitment-based discounts (1 or 3 years)
- **Reserved Instances**: Up to 72% discount, 1 or 3 years
- **Spot Instances**: Up to 90% discount, can be interrupted
- **Dedicated Hosts**: Physical servers for compliance
- **Dedicated Instances**: Isolated hardware

### Storage Options
- **Instance Store**: Ephemeral, local SSD storage
- **EBS Volumes**: Persistent block storage
- **EFS**: Network file system
- **FSx**: Managed file systems (Windows, Lustre, NetApp)

### Key Features
- Auto Scaling Groups
- Elastic Load Balancers
- Placement Groups (cluster, partition, spread)
- Elastic IP Addresses
- Instance Metadata and User Data
- Key Pairs for SSH access

---

## 2. EBS (Elastic Block Store)

### What is EBS?
Persistent block storage volumes for EC2 instances.

### Volume Types
- **gp3**: General purpose SSD, 3000 IOPS, 125 MB/s, cheapest
- **gp2**: General purpose SSD, up to 16000 IOPS, baseline 3 IOPS/GB
- **io2**: Provisioned IOPS SSD, 99.999% durability, 64000 IOPS max
- **io2 Block Express**: Highest performance, up to 256000 IOPS
- **st1**: Throughput optimized HDD, frequently accessed workloads
- **sc1**: Cold HDD, infrequent access, cheapest per GB

### Key Features
- Snapshots for backup
- Create volumes from snapshots
- Multi-attach for io2 (multiple instances)
- Encryption at rest (KMS)
- Volume types can be changed
- Fast snapshot restore

### Operations
- Create, attach, detach volumes
- Resize volumes (online expansion)
- Create RAID configurations
- Cross-region snapshot copy
- Lifecycle Manager for automation
- Delete on termination option

---

## 3. S3 (Simple Storage Service)

### What is S3?
Object storage for any amount of data.

### Storage Classes
- **Standard**: 99.99% availability, frequent access
- **Intelligent-Tiering**: Auto-moves based on access patterns
- **Standard-IA**: Infrequent access, lower cost
- **One Zone-IA**: Single AZ, cheaper
- **Glacier**: Archival, retrieval minutes to hours
- **Glacier Deep Archive**: Long-term, 12+ hours retrieval
- **Outposts**: On-premises S3

### Key Features
- Versioning
- Lifecycle policies
- Cross-region replication (CRR)
- Same-region replication (SRR)
- Server-side encryption
- Bucket policies
- Access control lists
- Pre-signed URLs
- Event notifications
- Transfer acceleration
- Multipart upload

### Advanced Features
- S3 Select (query in place)
- S3 Batch Operations
- S3 Access Points
- S3 Object Lambda
- S3 Inventory
- S3 Analytics

---

## 4. RDS (Relational Database Service)

### What is RDS?
Managed relational databases.

### Database Engines
- **PostgreSQL**: Open source, JSON support, strong community
- **MySQL**: Popular open source, wide compatibility
- **MariaDB**: MySQL fork, better performance
- **Oracle**: Enterprise features, licensing options
- **SQL Server**: Microsoft enterprise DB
- **Aurora**: AWS proprietary, MySQL/PostgreSQL compatible

### Aurora Features
- Aurora Serverless: Auto-scaling
- Aurora Global Database: Cross-region replication
- Aurora Multi-Master: Multiple writers
- Aurora Replicas: Up to 15 read replicas
- Backtrack: Point-in-time restore
- Performance Insights: Query monitoring

### Key Features
- Multi-AZ deployment
- Read replicas (up to 5)
- Automated backups
- Manual snapshots
- Point-in-time recovery
- Parameter groups
- Option groups
- Enhanced monitoring
- Performance Insights

---

## 5. DynamoDB

### What is DynamoDB?
Fully managed NoSQL database.

### Key Concepts
- **Tables**: Collection of items
- **Items**: Row of data
- **Attributes**: Columns
- **Primary Key**: Partition key or composite (partition + sort)
- **Secondary Indexes**: GSI (global) and LSI (local)

### Capacity Modes
- **On-Demand**: Pay per request, no capacity planning
- **Provisioned**: RCU/WCU capacity, cost-effective for predictable

### Key Features
- TTL (Time to Live)
- DAX (in-memory cache)
- Streams for change data capture
- Global Tables (multi-region)
- Point-in-time recovery
- Backups on demand
- Encryption at rest
- VPC endpoints

### Advanced Features
- PartiQL (SQL-like queries)
- DynamoDB Accelerator
- WorkBench for queries
- Transactions
- Batch operations

---

## 6. Lambda

### What is Lambda?
Serverless compute service.

### Configuration Options
- **Memory**: 128MB to 10GB
- **Timeout**: Up to 15 minutes
- **Ephemeral Disk**: 512MB to 10GB (/tmp)
- **Concurrency**: Up to 1000 (can request increase)

### Invocation Types
- **Synchronous**: API Gateway, wait for response
- **Asynchronous**: S3, SNS, wait and forget
- **Event Source Mapping**: Kinesis, DynamoDB Streams, poll-based

### Key Features
- Layers for shared dependencies
- Extensions for monitoring
- Environment variables
- VPC configuration
- Dead letter queues
- Provisioned concurrency
- SnapStart for Java
- Lambda@Edge

### Cold Start Optimization
- Increase memory (more CPU)
- Provisioned concurrency
- Minimize package size
- Lazy load dependencies
- Avoid VPC if possible

---

## 7. VPC (Virtual Private Cloud)

### What is VPC?
Isolated cloud network.

### Components
- **Subnets**: AZ-specific network segments
- **Route Tables**: Routing rules
- **Internet Gateway**: Internet access
- **NAT Gateway**: Outbound internet for private subnets
- **Egress Only IGW**: IPv6 outbound
- **VPC Endpoints**: Private access to AWS services
- **Security Groups**: Instance-level firewall
- **NACL**: Subnet-level firewall

### Connectivity Options
- **Internet Gateway**: Public internet
- **NAT Gateway**: Private to internet
- **VPN**: Site-to-site VPN
- **Direct Connect**: Dedicated connection
- **VPC Peering**: Direct VPC-to-VPC
- **Transit Gateway**: Hub for multiple VPCs
- **PrivateLink**: Private service access

### Advanced Features
- VPC Flow Logs
- DHCP Options Set
- IP CIDR blocks
- Secondary CIDR blocks
- VPC sharing (RAM)
- DNS settings

---

## 8. IAM (Identity and Access Management)

### What is IAM?
AWS identity and access management.

### Components
- **Users**: Long-term credentials
- **Groups**: Collection of users
- **Roles**: Temporary credentials
- **Policies**: JSON permission documents

### Policy Types
- **Identity-based**: Attached to users/roles/groups
- **Resource-based**: Attached to resources (S3, SQS)
- **Service Control Policies**: Organization level
- **Permission Boundaries**: Max permissions for entities

### Key Features
- MFA support
- Password policy
- Access keys rotation
- Service-linked roles
- Cross-account access
- Federation (SAML, OIDC)
- Policy evaluation logic
- Permission boundaries

### Best Practices
- Use roles instead of access keys
- Enable MFA
- Least privilege principle
- Regular access reviews
- Use managed policies
- Enable CloudTrail

---

## 9. CloudFront

### What is CloudFront?
Content delivery network (CDN).

### Features
- **Origins**: S3, ALB, EC2, custom HTTP
- **Cache Behaviors**: Path-specific rules
- **Signed URLs**: Private content access
- **Signed Cookies**: Multiple file access
- **Field-level encryption**: Sensitive data
- **Geo-restrictions**: Country blocking
- **Lambda@Edge**: Edge computing

### Cache Options
- **TTL**: Time to live in cache
- **Query Strings**: Forward or not
- **Cookies**: Forward or not
- **Headers**: Whitelist options
- **Compression**: Brotli/Gzip

### Advanced Features
- Origin Failover (primary/secondary)
- Real-time logs
- CloudFront Functions
- Cache invalidation
- Function associations

---

## 10. Route 53

### What is Route 53?
DNS web service.

### Record Types
- **A**: IPv4 address
- **AAAA**: IPv6 address
- **CNAME**: Canonical name
- **Alias**: AWS resource mapping
- **MX**: Mail servers
- **TXT**: Text records
- **NS**: Name servers
- **SOA**: Start of authority

### Routing Policies
- **Simple**: Single value
- **Weighted**: Traffic split by weight
- **Latency**: Lowest latency routing
- **Failover**: Primary/secondary
- **Geolocation**: Geographic routing
- **Multivalue**: Multiple healthy answers
- **IP-based**: Client IP routing

### Features
- Health checks
- DNSSEC
- Private hosted zones
- Resolver (hybrid DNS)
- Traffic flow (visual editor)

---

## 11. EKS (Elastic Kubernetes Service)

### What is EKS?
Managed Kubernetes service.

### Components
- **Control Plane**: Managed by AWS
- **Worker Nodes**: EC2 or Fargate
- **Add-ons**: CoreDNS, VPC CNI, kube-proxy
- **Fargate**: Serverless containers

### Node Options
- **Self-managed**: You manage nodes
- **Managed node groups**: AWS manages
- **Fargate**: Serverless pods

### Key Features
- Kubernetes version support
- Cluster autoscaler
- Karpenter for provisioning
- EKS Add-ons
- EKS Distro (downstream)
- App Mesh integration
- Secrets encryption

### Networking
- VPC CNI plugin
- Security groups for pods
- CoreDNS
- Service discovery

---

## 12. ECS (Elastic Container Service)

### What is ECS?
Container orchestration service.

### Launch Types
- **EC2**: Self-managed infrastructure
- **Fargate**: Serverless containers

### Components
- **Clusters**: Container instances
- **Task Definitions**: Container specs
- **Tasks**: Running containers
- **Services**: Long-running tasks
- **Task Sets**: Rolling updates

### Key Features
- Service discovery
- Load balancing
- Auto scaling
- Blue-green deployment
- Task IAM roles
- CloudWatch Logs
- Private registry (ECR)

---

## 13. ECR (Elastic Container Registry)

### What is ECR?
Container image registry.

### Features
- Image scanning
- Lifecycle policies
- Cross-region replication
- Pull through cache
- Public registry
- Image tags
- Scan on push

---

## 14. API Gateway

### What is API Gateway?
API creation and management.

### Types
- **REST API**: Traditional REST APIs
- **HTTP API**: Lightweight, lower cost
- **WebSocket API**: Real-time apps

### Key Features
- Usage plans
- API keys
- Caching
- Throttling
- Request/response transforms
- Authorizers (Lambda, IAM, Cognito)
- Custom domains
- ACM certificates
- Swagger/OpenAPI import

---

## 15. SNS (Simple Notification Service)

### What is SNS?
Pub/sub messaging and mobile notifications.

### Features
- **Topics**: Message categories
- **Subscriptions**: Email, SMS, HTTP, Lambda, SQS
- **FIFO**: Ordered delivery
- **Message filtering**: Filter by attributes
- **Dead letter queues**: Failed delivery
- **Platform applications**: Push notifications

---

## 16. SQS (Simple Queue Service)

### What is SQS?
Message queuing service.

### Queue Types
- **Standard**: Unlimited throughput, at-least-once
- **FIFO**: Exactly-once processing, ordered

### Key Features
- Dead letter queues
- Delay queues
- Message timers
- Visibility timeout
- Long polling
- Maximum message size (256KB)
- Extended client (large messages in S3)

---

## 17. Step Functions

### What is Step Functions?
Serverless workflow orchestration.

### Workflow Types
- **Standard**: Long-running, audit, exactly-once
- **Express**: High-volume, at-least-once

### States
- Pass
- Task (Lambda, Batch, ECS)
- Choice (branching)
- Parallel
- Map (iterations)
- Wait
- Succeed/Fail

### Key Features
- Intrinsic functions
- Nested workflows
- Callback pattern
- Activity tasks
- Map state
- Wait for callback

---

## 18. EventBridge

### What is EventBridge?
Serverless event bus.

### Components
- **Event Bus**: Central hub
- **Rules**: Event routing
- **Targets**: Actions to trigger
- **Schemas**: Event structure

### Features
- Custom event buses
- Scheduled rules
- Event replay
- Cross-account events
- Dead letter targets
- Event filtering

---

## 19. Kinesis

### What is Kinesis?
Real-time data streaming.

### Services
- **Data Streams**: Real-time streaming
- **Data Firehose**: Delivery to storage
- **Data Analytics**: SQL on streams
- **Video Streams**: Video streaming

### Key Features
- **Data Streams**: Shards, retention up to 365 days
- **Producers**: KPL, SDK, agents
- **Consumers**: KCL, Lambda, Firehose
- **Enhanced fan-out**: Dedicated consumers

---

## 20. ElastiCache

### What is ElastiCache?
In-memory caching service.

### Engines
- **Redis**: Advanced features, pub/sub, sorted sets
- **Memcached**: Simple, multi-node

### Redis Features
- Cluster mode
- Read replicas
- Auto failover
- Backup/restore
- Redis Auth
- Multi-AZ

### Memcached Features
- Auto discovery
- Multi-node
- Large node types

---

## 21. S3 Glacier

### What is S3 Glacier?
Archival storage.

### Vaults vs Archive
- **Vaults**: Containers for archives
- **Archives**: Individual stored objects

### Retrieval Options
- **Expedited**: 1-5 minutes
- **Standard**: 3-5 hours
- **Bulk**: 5-12 hours

### Features
- Vault lock policies
- Inventory reports
- Notification configuration
- Multi-part upload

---

## 22. CloudWatch

### What is CloudWatch?
Monitoring and observability.

### Components
- **Metrics**: Time series data
- **Alarms**: Threshold-based alerts
- **Logs**: Application logs
- **Events**: Scheduled/event-driven
- **Dashboards**: Visualizations

### Features
- Custom metrics
- Metric math
- Composite alarms
- Anomaly detection
- Contributor insights
- RUM (Real User Monitoring)
- Synthetics (Canary testing)

---

## 23. X-Ray

### What is X-Ray?
Distributed tracing.

### Concepts
- **Traces**: Request path
- **Segments**: Service-level data
- **Subsegments**: Detailed segments
- **Service map**: Visual representation

### Features
- Sampling rules
- Grouping traces
- Annotations/metadata
- Trace IDs propagation
- Integration with many AWS services

---

## 24. Systems Manager

### What is Systems Manager?
Operations hub for AWS.

### Features
- **Parameter Store**: Configuration/secrets
- **Session Manager**: EC2 access
- **Run Command**: Execute commands
- **Automation**: Runbooks
- **Patch Manager**: OS updates
- **State Manager**: Configuration compliance
- **Inventory**: Resource details
- **OpsCenter**: Ops items

---

## 25. KMS (Key Management Service)

### What is KMS?
Key management and encryption.

### Key Types
- **Symmetric**: Single key for encrypt/decrypt
- **Asymmetric**: Public/private key pair

### Features
- Key rotation
- Aliases
- Key policies
- Grants
- Custom key stores
- Multi-region keys
- Import key material

---

## 26. Secrets Manager

### What is Secrets Manager?
Secret storage and rotation.

### Features
- Automatic rotation
- Multi-region secrets
- Integration with RDS
- Versioning
- Access control
- Resource policies
- Cross-account access

---

## 27. CloudFormation

### What is CloudFormation?
Infrastructure as code.

### Concepts
- **Templates**: JSON/YAML definitions
- **Stacks**: Template deployments
- **StackSets**: Multi-account/region
- **Change Sets**: Preview changes
- **Nested Stacks**: Modular templates

### Features
- Drift detection
- Rollback
- Custom resources
- Stack policies
- Import existing resources
- Stack reuse with StackSets

---

## 28. Terraform

### What is Terraform?
Multi-cloud IaC tool.

### Concepts
- **Providers**: AWS, Azure, GCP
- **Resources**: Infrastructure
- **Data Sources**: Read-only data
- **Variables**: Parameterization
- **Modules**: Reusable templates
- **State**: Resource tracking

### Features
- Plan/apply
- Remote state
- Workspaces
- Provider registry
- Modules registry

---

## 29. CDK

### What is CDK?
IaC with programming languages.

### Constructs
- **L1**: CloudFormation resources
- **L2**: Curated abstractions
- **L3**: Patterns/solutions

### Features
- Multiple languages
- Synthesize to CloudFormation
- CDK Pipelines
- Aspects
- CLI tools

---

## 30. CodePipeline

### What is CodePipeline?
CI/CD service.

### Components
- **Source**: CodeCommit, S3, GitHub
- **Build**: CodeBuild
- **Test**: CodeBuild, other providers
- **Deploy**: CodeDeploy, CloudFormation, ECS

### Features
- Approval actions
- Manual gates
- Artifact storage
- Cross-region deployments

---

## 31. CodeBuild

### What is CodeBuild?
Managed build service.

### Features
- Managed builds
- Custom build environments
- Buildspec.yml
- Build badges
- Test reports
- Caching
- Environment variables

---

## 32. CodeDeploy

### What is CodeDeploy?
Deployment service.

### Deployment Types
- **In-place**: Update existing instances
- **Blue-green**: New environment swap

### Features
- AppSpec files
- Deployment groups
- Rollback
- Traffic shifting
- ECS/ Lambda deployments

---

## 33. Direct Connect

### What is Direct Connect?
Dedicated network connection.

### Components
- **Connection**: Physical connection
- **Virtual Interface**: Network routing
- **LAG**: Link aggregation
- **Gateway**: Multi-region access

### Virtual Interfaces
- **Private**: VPC access
- **Public**: AWS public services
- **Transit**: Transit Gateway

---

## 34. Global Accelerator

### What is Global Accelerator?
Improve global performance.

### Features
- Anycast IP
- Health checks
- Traffic dials
- Client affinity
- Cross-region failover

---

## 35. WAF

### What is WAF?
Web application firewall.

### Features
- Web ACLs
- Rule groups
- Managed rules
- Rate-based rules
- Bot Control
- Logging to Kinesis

---

## 36. Shield

### What is Shield?
DDoS protection.

### Tiers
- **Standard**: Free, always-on
- **Advanced**: Paid, more protection, DRG

### Features
- Always-on detection
- Automatic mitigation
- 24/7 response team
- Cost protection
- WAF included

---

## 37. Config

### What is Config?
Resource configuration tracking.

### Features
- Configuration recorder
- Config rules
- Conformance packs
- Remediation
- Configuration history
- Advanced query

---

## 38. GuardDuty

### What is GuardDuty?
Threat detection.

### Data Sources
- CloudTrail events
- VPC Flow Logs
- DNS logs
- EKS audit logs
- S3 data events
- EKS Runtime Monitoring

### Features
- Machine learning
- Threat intelligence
- Findings
- Suppressions
- Integrations

---

## 39. Security Hub

### What is Security Hub?
Central security view.

### Features
- Security standards
- Findings aggregation
- Custom insights
- Automated response
- Compliance status

---

## 40. Cognito

### What is Cognito?
User identity management.

### Components
- **User Pools**: App users
- **Identity Pools**: AWS access

### Features
- Sign up/in
- Social login
- MFA
- Lambda triggers
- Custom attributes
- Adaptive auth

---

## 41. Amplify

### What is Amplify?
Frontend development platform.

### Features
- Hosting
- Authentication
- API (GraphQL/REST)
- Storage
- Analytics
- Push notifications

---

## 42. AppSync

### What is AppSync?
Managed GraphQL service.

### Features
- GraphQL APIs
- Real-time subscriptions
- Offline support
- Multiple data sources
- Lambda resolvers
- Cognito authorization

---

## 43. Glue

### What is Glue?
Serverless ETL.

### Components
- **Data Catalog**: Metadata
- **Jobs**: ETL scripts
- **Crawlers**: Schema discovery
- **Studio**: Visual ETL
- **DataBrew**: Data preparation

---

## 44. Athena

### What is Athena?
SQL queries on S3.

### Features
- Serverless
- Schema on read
- Partitioning
- Compression formats
- Workgroups
- Query results in S3

---

## 45. Redshift

### What is Redshift?
Data warehouse.

### Node Types
- **RA3**: Managed storage
- **DC2**: Dense compute
- **DS2**: Dense storage

### Features
- Spectrum (query S3)
- Concurrency scaling
- Elastic resize
- Auto pause
- Data sharing
- Machine learning

---

## 46. Lake Formation

### What is Lake Formation?
Data lake security.

### Features
- Fine-grained access
- Table/column permissions
- Row-level security
- Data catalog integration
- LF-tags

---

## 47. Backup

### What is AWS Backup?
Centralized backup.

### Features
- Cross-region backup
- Lifecycle management
- Copy on demand
- Backup policies
- Vault lock
- Compliance reporting

---

## 48. Control Tower

### What is Control Tower?
Landing zone management.

### Features
- Account factory
- Guardrails
- Detective controls
- Preventive controls
- Organization setup

---

## 49. Organizations

### What is Organizations?
Multi-account management.

### Features
- Organizational units
- Service control policies
- Consolidated billing
- Cross-account access
- AI opt-out

---

## 50. Budgets

### What is Budgets?
Cost management.

### Features
- Cost budgets
- Usage budgets
- Reservation budgets
- Alerts
- Forecasts
- Actions

---

## 51. Cost Explorer

### What is Cost Explorer?
Cost visualization.

### Features
- Cost and usage charts
- Forecasts
- RI recommendations
- Savings Plan recommendations
- Filter by tag/service

---

## 52. Trusted Advisor

### What is Trusted Advisor?
Optimization recommendations.

### Checks
- Cost optimization
- Performance
- Security
- Fault tolerance
- Service limits

---

## 53. Service Catalog

### What is Service Catalog?
Self-service provisioning.

### Features
- Portfolios
- Products
- Constraints
- Tag options
- Access control

---

## 54. Personal Health Dashboard

### What is Personal Health Dashboard?
Personalized health alerts.

### Features
- Service health
- Scheduled changes
- Remediation guidance
- AWS-wide events

---

## 55. Health

### What is Health?
AWS health events.

### Features
- Service health
- Account-specific events
- Event aggregation
- CloudTrail integration
