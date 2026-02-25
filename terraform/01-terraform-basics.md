# Terraform Basics

## 1. What is Terraform?
Terraform is an Infrastructure as Code (IaC) tool by HashiCorp. It lets you define infrastructure in declarative files and provision it consistently.

## 2. Why Terraform?
- Automation
- Repeatability
- Version control friendly
- Multi-cloud support
- Drift detection

## 3. Core Workflow
1. `terraform init`
2. `terraform plan`
3. `terraform apply`
4. `terraform destroy`

## 4. Simple example
```hcl
provider "aws" {
  region = "us-east-1"
}

resource "aws_s3_bucket" "demo" {
  bucket = "my-unique-demo-bucket-12345"
}
```

## 5. Important commands
- `terraform fmt`
- `terraform validate`
- `terraform show`
- `terraform output`
