# EC2 (Elastic Compute Cloud) - Complete Guide

## What is EC2?
Amazon EC2 provides resizable compute capacity in the cloud. It allows you to launch virtual servers (instances) with various configurations of CPU, memory, storage, and networking.

## Instance Types

### 1. General Purpose
- **t-series (Burstable)**: t3, t3a, t4g - Good for web servers, dev environments
- **m-series**: m5, m6i, m7g - Balanced compute/memory
- **mac**: mac1, mac2 - Apple hardware for Xcode

### 2. Compute Optimized
- **c-series**: c5, c6i, c7g - High performance processors
- Use cases: Batch processing, HPC, video encoding, gaming servers

### 3. Memory Optimized
- **r-series**: r5, r6i, r7g - Large memory
- **x-series**: x1, x1e - Extremely large memory
- **z-series**: z1d - High frequency + memory
- Use cases: In-memory caches, big data analytics

### 4. Accelerated Computing
- **p-series**: p3, p4d, p5 - GPU for ML, HPC
- **g-series**: g4dn, g5 - Graphics/ML
- **inf series**: inf1, inf2 - AWS Inferentia chips

### 5. Storage Optimized
- **i-series**: i3, i4i - High IOPS NVMe SSD
- **d-series**: d3, d3en - Dense storage
- **h-series**: hpc6a, hpc7g - High performance

## Purchasing Options

### On-Demand
- Pay per second/hour
- No commitment
- Highest cost
- Use: Development, short-term tests

### Savings Plans
- Commitment in USD/hour
- 1 or 3 year terms
- Up to 72% savings
- Flexible: Can switch instance families

### Reserved Instances
- 1 or 3 year commitment
- Up to 72% discount
- **Standard**: Can't change attributes
- **Convertible**: Can exchange for different RI
- **Scheduled**: For predictable recurring times

### Spot Instances
- Up to 90% discount
- Can be interrupted with 2-minute warning
- Use: Fault-tolerant, batch jobs, stateless workloads
- Best practices: Use spot fleet, diverse allocation

### Dedicated Hosts
- Physical servers for your use
- Regulatory compliance needs
- Per-host billing

### Dedicated Instances
- Hardware dedicated to your account
- Instances run on isolated hardware

## Key Features

### 1. Auto Scaling Groups (ASG)
- Automatically adjust capacity
- Scale based on: CPU, memory, custom metrics, scheduled
- Components: Launch Template, ASG, Scaling Policies

### 2. Elastic Load Balancers
- **Application LB (L7)**: HTTP/HTTPS
- **Network LB (L4)**: TCP, UDP
- **Gateway LB**: Third-party appliances

### 3. Placement Groups
- **Cluster**: Same AZ, low latency
- **Partition**: Distributed across partitions
- **Spread**: Across AZs, critical instances

### 4. Key Pairs
- Public key stored, private key downloaded
- PEM for Linux, PPK for PuTTY

### 5. User Data
- Scripts that run on first boot
- Cloud-init for configuration

### 6. Instance Metadata
- Access: http://169.254.169.254/latest/meta-data/
- User data: http://169.254.169.254/latest/user-data/

## Storage Options

### Instance Store
- Ephemeral, temporary storage
- Data lost on stop/terminate
- Included in instance price
- High I/O performance

### EBS (Elastic Block Store)
- Persistent storage
- Types: gp3, gp2, io2, st1, sc1
- Can be detached/attached
- Encrypted with KMS

### EFS (Elastic File System)
- Network file system
- Multi-AZ, scalable
- Pay per use

### FSx
- **FSx for Windows**: Windows file share
- **FSx for Lustre**: HPC file system
- **FSx for NetApp**: ONTAP
- **FSx for OpenZFS**: ZFS

## Security

### Security Groups
- Stateful firewall
- Inbound/Outbound rules
- Allow rules only
- References by security group ID

### Key Pairs
- RSA or ED25519
- Windows: .ppk, Linux/Mac: .pem

### IAM Roles
- Attach IAM role to instance
- Temporary credentials via instance metadata

## Interview Questions & Answers

### Q1: What is the difference between On-Demand and Spot instances?
**Answer**: On-Demand instances have no commitment and you pay full price, but they won't be interrupted. Spot instances can be terminated with 2-minute warning when AWS needs capacity, but cost up to 90% less. Spot is suitable for fault-tolerant workloads like batch processing, while On-Demand is for applications that cannot tolerate interruption.

### Q2: How do you secure EC2 instances?
**Answer**: Use security groups as firewalls, key pairs for SSH access, IAM roles instead of access keys, enable detailed monitoring, use VPC for network isolation, apply OS patches via Systems Manager, use encrypted EBS volumes, and implement least privilege in IAM policies.

### Q3: What is a Placement Group and when would you use each type?
**Answer**: Cluster placement groups put instances in the same AZ for low latency (good for HPC). Spread placement groups distribute across AZs for fault tolerance (good for critical apps). Partition placement groups distribute across hardware partitions (good for large distributed workloads like HDFS).

### Q4: How does Auto Scaling work?
**Answer**: Auto Scaling uses a Launch Template to define instance configuration. You define minimum, maximum, and desired capacity. Scaling policies can be based on CloudWatch metrics (CPU, memory, request count) or scheduled. ASG maintains desired capacity by launching/terminating instances.

### Q5: What happens when you stop an EC2 instance?
**Answer**: The instance shuts down, data on instance store is lost, EBS data persists. You're not charged for stopped instances. Private IP remains same. When started, instance may get different public IP (unless using EIP).

### Q6: How do you SSH into an EC2 instance?
**Answer**: Ensure security group allows SSH (port 22). Use key pair (.pem file). Run: `chmod 400 key.pem`, then `ssh -i key.pem ec2-user@public-ip`. For Windows, use PuTTY with .ppk key.

### Q7: What is the difference between Stop and Terminate?
**Answer**: Stop: Instance shuts down, EBS persists, you're not billed for compute. Terminate: Instance and all EBS (unless DeleteOnTermination=false) are deleted permanently.

### Q8: How do you resize an EC2 instance?
**Answer**: You cannot directly resize. Must: Stop instance → Change instance type in console → Start instance. Or use AWS CLI: `aws ec2 modify-instance-attribute --instance-type t3.medium`.

### Q9: What is EC2 Hibernate?
**Answer**: Allows you to pause instances and resume later. Memory (RAM) is saved to EBS root volume. Faster restart than stop/start. Must be enabled in launch template.

### Q10: How do you recover from impaired instance?
**Answer**: If instance status check fails: Check System Log, check CloudWatch for metrics, try recovering with AWS CLI `aws ec2 reboot-instances --instance-ids i-xxx`. If hardware issues, may need to stop/start to move to new hardware.
