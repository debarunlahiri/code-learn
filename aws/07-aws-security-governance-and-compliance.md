# AWS Security, Governance, and Compliance

## 1. Core Security Services
- IAM
- KMS
- Secrets Manager
- WAF
- Shield
- GuardDuty
- Security Hub
- Inspector
- Macie

## 2. Encryption Strategy

### At rest
- S3 SSE-KMS
- EBS encryption
- RDS encryption

### In transit
- TLS everywhere
- ACM for certificate management

## 3. Secrets Management
- Use Secrets Manager/Parameter Store
- Never hardcode secrets in code or AMI
- Rotate secrets automatically where possible

## 4. Threat Detection and Posture
- GuardDuty for threat detection
- Security Hub for centralized findings
- Config for compliance rules
- Detective for security investigation

## 5. Governance at Scale
- AWS Organizations
- Organizational Units (OU)
- Service Control Policies (SCP)
- Centralized logging account

## 6. Compliance Practices
- Enable CloudTrail in all regions
- Enable Config rules for drift/compliance
- Maintain audit-ready logs
- Tagging strategy for ownership and cost attribution
