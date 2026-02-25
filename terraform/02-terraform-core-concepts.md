# Terraform Core Concepts

## 1. Providers
Providers are plugins for APIs (AWS, Azure, GCP, Kubernetes).

## 2. Resources
Resources represent infrastructure objects (`aws_instance`, `aws_s3_bucket`, etc.).

## 3. Variables
Use input variables to make code reusable.

```hcl
variable "region" {
  type    = string
  default = "us-east-1"
}
```

## 4. Outputs
Expose useful values after apply.

```hcl
output "bucket_name" {
  value = aws_s3_bucket.demo.bucket
}
```

## 5. Locals
Use `locals` for reusable expressions.

## 6. Data sources
Read existing infrastructure without creating it.

## 7. Dependency graph
Terraform creates a graph of dependencies and executes in correct order.

## 8. Meta-arguments
- `count`
- `for_each`
- `depends_on`
- `lifecycle`
