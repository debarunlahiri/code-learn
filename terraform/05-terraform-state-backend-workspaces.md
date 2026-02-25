# Terraform State, Backend, and Workspaces

## 1. Terraform state
State tracks real infrastructure and resource mapping.

## 2. Why remote state?
- Team collaboration
- Locking
- Better security
- Durability

## 3. S3 backend with DynamoDB lock
```hcl
terraform {
  backend "s3" {
    bucket         = "my-tf-state-bucket"
    key            = "network/terraform.tfstate"
    region         = "us-east-1"
    dynamodb_table = "terraform-state-lock"
    encrypt        = true
  }
}
```

## 4. Workspaces
Workspaces allow environment separation (`dev`, `staging`, `prod`) in one config.

## 5. State operations (careful)
- `terraform state list`
- `terraform state show`
- `terraform state mv`
- `terraform import`

## 6. Common mistakes
- Manual changes outside Terraform
- Shared state without locking
- Secrets in plaintext state
