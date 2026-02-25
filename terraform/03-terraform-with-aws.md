# Terraform with AWS

## 1. AWS provider setup
```hcl
provider "aws" {
  region = var.region
}
```

## 2. VPC + subnet example
```hcl
resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
}

resource "aws_subnet" "public_a" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.1.0/24"
  map_public_ip_on_launch = true
}
```

## 3. EC2 example
```hcl
resource "aws_instance" "web" {
  ami           = "ami-1234567890abcdef0"
  instance_type = "t3.micro"
  subnet_id     = aws_subnet.public_a.id
}
```

## 4. IAM with Terraform
Manage users/roles/policies in code, but avoid hardcoding secrets.

## 5. Common AWS + Terraform best practices
- Use remote state (S3 backend)
- Use state locking (DynamoDB)
- Split environments
- Use modules for reuse
