# AWS Global Infrastructure and IAM

## 1. Global Infrastructure

### Regions
A region is a separate geographic location. Choose region based on:
- Latency for users
- Compliance/data residency
- Service availability
- Cost

### Availability Zones
Each region has multiple AZs with independent power/networking. High availability needs Multi-AZ design.

### Edge Locations
CloudFront and Route 53 use edge locations to reduce latency globally.

## 2. IAM Fundamentals

IAM controls who can do what on which resource.

### IAM entities
- User: long-term identity for person/application
- Group: collection of users
- Role: temporary identity assumed by trusted entity
- Policy: JSON permission rules

### Policy structure
- Effect: Allow/Deny
- Action: API operations
- Resource: ARN
- Condition: optional constraints

## 3. IAM Best Practices
- Enable MFA for root and privileged users
- Never use root user for daily work
- Prefer roles over access keys
- Use least privilege access
- Rotate secrets/keys
- Use AWS Organizations SCPs for guardrails

## 4. Authentication and Federation
- AWS Identity Center (SSO)
- SAML/OIDC federation
- AssumeRole cross-account access

## 5. Resource-level security
- Resource-based policies (S3 bucket policy, KMS key policy)
- Identity-based policies (attached to user/role)
- Permission boundaries for delegated admin control

## 6. Access Troubleshooting Checklist
1. Is principal correct?
2. Is explicit deny present?
3. IAM policy allows action/resource?
4. SCP allows action?
5. Resource policy allows principal?
6. KMS policy grants decrypt/encrypt if needed?
