# Terraform CI/CD, Testing, and Security

## 1. CI/CD pipeline stages
1. `terraform fmt -check`
2. `terraform validate`
3. Security scan (tfsec/checkov)
4. `terraform plan`
5. Approval
6. `terraform apply`

## 2. Policy as Code
- Sentinel
- OPA/Conftest
- Enforce guardrails (no public S3, approved regions only, etc.)

## 3. Security best practices
- Use least privilege IAM for Terraform execution role
- Store state securely and encrypt
- Avoid hardcoded credentials
- Use temporary credentials (OIDC/STS)

## 4. Testing strategies
- Static validation
- Unit/module tests (where possible)
- Integration tests in sandbox account

## 5. Drift handling
- Detect drift with regular plans
- Reconcile drift carefully
- Restrict manual console edits
