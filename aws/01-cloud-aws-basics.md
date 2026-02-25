# Cloud and AWS Basics

## 1. What is Cloud Computing?

Cloud computing means renting IT resources (servers, storage, databases, networking, software) over the internet.

### Key benefits
- No upfront hardware purchase
- Faster provisioning
- Elastic scaling
- Pay-as-you-go
- Global availability

## 2. Cloud Service Models

### IaaS (Infrastructure as a Service)
You manage OS, runtime, app; provider manages hardware.
Example: EC2.

### PaaS (Platform as a Service)
You manage application code; provider manages platform/runtime.
Examples: Elastic Beanstalk, AWS App Runner.

### SaaS (Software as a Service)
Ready-made software consumed by users.
Example: Salesforce, Google Workspace.

## 3. Deployment Models
- Public cloud
- Private cloud
- Hybrid cloud
- Multi-cloud

## 4. Why AWS?
- Largest service catalog
- Mature ecosystem
- Global regions and availability zones
- Strong security controls
- Broad enterprise adoption

## 5. Shared Responsibility Model

AWS secures "of the cloud":
- Physical security
- Hardware
- Hypervisor
- Core managed services platform

Customer secures "in the cloud":
- IAM users/roles/policies
- OS patching (on EC2)
- App security
- Data encryption and backup strategy
- Network controls (security groups, NACL)

## 6. Core AWS Concepts
- Region: geographic area (e.g., us-east-1)
- Availability Zone (AZ): isolated datacenter within region
- Edge Location: POP used by CloudFront
- Account: billing/security boundary

## 7. AWS Well-Architected Pillars
- Operational Excellence
- Security
- Reliability
- Performance Efficiency
- Cost Optimization
- Sustainability
