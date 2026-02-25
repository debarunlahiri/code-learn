# AWS Storage and Backup

## 1. Amazon S3

Object storage for virtually unlimited scale.

### Storage classes
- Standard
- Standard-IA
- One Zone-IA
- Intelligent-Tiering
- Glacier Instant Retrieval
- Glacier Flexible Retrieval
- Glacier Deep Archive

### Important features
- Versioning
- Lifecycle policies
- Replication (CRR/SRR)
- Object Lock (WORM)
- Encryption (SSE-S3, SSE-KMS)

## 2. EBS (Block Storage)

Attached to EC2 instances.

### Volume types
- gp3 (general purpose)
- io1/io2 (high IOPS)
- st1/sc1 (throughput/cold)

### Best practices
- Snapshot regularly
- Use Multi-Attach only where supported
- Monitor IOPS/throughput limits

## 3. EFS (Managed NFS)
- Shared file system across multiple EC2
- Good for Linux shared storage
- Supports elastic scale

## 4. FSx Family
- FSx for Windows File Server
- FSx for Lustre
- FSx for NetApp ONTAP
- FSx for OpenZFS

## 5. Backup Strategy
- Use AWS Backup for centralized policy-based backup
- Define RPO and RTO per workload
- Test restore, not just backup success
- Use cross-region and cross-account copies for critical data
