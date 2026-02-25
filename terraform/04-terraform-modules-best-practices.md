# Terraform Modules and Best Practices

## 1. What is a module?
A module is a container for multiple resources used together.

## 2. Module structure
- `main.tf`
- `variables.tf`
- `outputs.tf`
- `README.md`

## 3. Reusable module example
```hcl
module "network" {
  source = "./modules/network"
  cidr   = "10.0.0.0/16"
}
```

## 4. Versioning strategy
- Pin provider versions
- Pin module versions
- Use semantic versioning for custom modules

## 5. Best practices
- Keep modules focused
- Avoid giant modules
- Document variables/outputs
- Validate inputs
- Avoid duplicated code
