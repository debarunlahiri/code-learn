# AWS DevOps, CI/CD, and Infrastructure as Code

## 1. DevOps on AWS

Goal: faster, safer releases with automation.

## 2. CI/CD Services
- CodeCommit (git repo)
- CodeBuild (build/test)
- CodeDeploy (deployment)
- CodePipeline (orchestration)

## 3. Infrastructure as Code

### CloudFormation
AWS native IaC using templates.

### CDK
Define infrastructure in code (TypeScript/Python/Java/etc), synthesized to CloudFormation.

### Terraform
Popular third-party IaC tool, multi-cloud support.

## 4. Deployment Strategies
- Blue/Green
- Rolling
- Canary
- Feature flags

## 5. Container DevOps
- ECR for container registry
- ECS/EKS deployment pipelines
- Image scanning in CI

## 6. Quality Gates in Pipeline
- Unit tests
- Integration tests
- Security scans (SAST/dependency scan)
- Manual approval stage for production
