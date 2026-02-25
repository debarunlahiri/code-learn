# AWS Networking: VPC, Route 53, CloudFront

## 1. VPC Basics

A VPC is your isolated virtual network in AWS.

### Core components
- CIDR block
- Subnets (public/private)
- Route tables
- Internet Gateway (IGW)
- NAT Gateway
- Security Groups
- Network ACLs

## 2. Public vs Private Subnet
- Public subnet: route to IGW
- Private subnet: no direct IGW route

Use private subnet for databases/internal services.

## 3. Security Groups vs NACL

### Security Group
- Instance-level firewall
- Stateful
- Allow rules only

### NACL
- Subnet-level firewall
- Stateless
- Allow and deny rules

## 4. Connectivity Options
- VPC Peering
- Transit Gateway
- Site-to-Site VPN
- AWS Direct Connect
- PrivateLink

## 5. Route 53
- DNS service
- Domain registration
- Health checks
- Traffic routing policies:
  - Simple
  - Weighted
  - Latency
  - Failover
  - Geolocation

## 6. CloudFront

Global CDN for low-latency content delivery.

### Use cases
- Static website acceleration
- API acceleration
- Video distribution
- DDoS risk reduction with AWS Shield

### Key features
- Cache behavior by path
- Origin Access Control for private S3
- Signed URLs/cookies
- WAF integration
