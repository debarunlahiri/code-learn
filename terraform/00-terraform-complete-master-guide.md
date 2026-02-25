# Terraform Complete Master Guide (Comprehensive)

This guide is a complete Terraform reference covering language, architecture, operations, security, enterprise usage, and production patterns. It is designed to provide both foundational knowledge and advanced insights for Terraform practitioners.

---

## 1. Infrastructure as Code Principles

### 1.1 Why IaC
- **Reproducibility**: Infrastructure can be recreated consistently across environments, reducing configuration drift.
- **Version control for infrastructure**: Changes to infrastructure are tracked in version control systems (e.g., Git), enabling rollbacks and collaboration.
- **Automated reviews**: Infrastructure changes can be reviewed and approved through CI/CD pipelines.
- **Reduced manual drift**: Automated provisioning ensures that infrastructure matches the desired state, avoiding manual configuration errors.

### 1.2 Declarative model
- **Desired state**: Terraform allows you to define the desired state of your infrastructure, and it calculates the necessary operations to achieve that state.
- **Idempotency**: Reapplying the same configuration does not result in unintended changes, ensuring predictable outcomes.

### 1.3 Immutable mindset
- **Replace over modify**: Immutable infrastructure encourages replacing resources instead of modifying them in place, reducing the risk of configuration drift and ensuring consistency.
- **Blue-green deployments**: Use immutable patterns for zero-downtime deployments by creating new resources and switching traffic to them.

---

## 2. Terraform Core Workflow

1. `terraform init`: Initializes the working directory, downloads provider plugins, and sets up the backend.
2. `terraform fmt`: Formats Terraform configuration files to a canonical style.
3. `terraform validate`: Validates the syntax and internal consistency of the configuration files.
4. `terraform plan`: Creates an execution plan, showing the changes Terraform will make without applying them.
5. `terraform apply`: Applies the changes required to reach the desired state of the configuration.
6. `terraform destroy`: Destroys all resources managed by the configuration (use cautiously).

### 2.1 Plan discipline
- **Review plans carefully**: Always review the execution plan (`terraform plan`) before applying changes, especially in shared or production environments.
- **Plan output**: Save the plan output (`terraform plan -out=planfile`) for review and approval workflows.

### 2.2 Change control
- **Pull requests**: Use pull requests to review and approve changes, attaching the plan output for visibility.
- **Environment segregation**: Apply changes to lower environments (e.g., dev, staging) before production to validate the impact.

---

## 3. Terraform Language Deep Dive

### 3.1 Core blocks
- **`terraform`**: Specifies the required Terraform version and backend configuration.
- **`provider`**: Configures the providers (e.g., AWS, Azure) used in the configuration.
- **`resource`**: Defines the infrastructure objects to create/manage (e.g., `aws_instance`, `azurerm_storage_account`).
- **`data`**: Reads data from external sources without creating resources (e.g., `aws_ami`).
- **`module`**: Encapsulates reusable configurations.
- **`variable`**: Defines input variables for parameterizing configurations.
- **`output`**: Exposes values from the configuration for use in other modules or outputs.
- **`locals`**: Defines local values for simplifying expressions and reusing logic.

### 3.2 Types
- **Primitive types**: Basic data types like `string`, `number`, and `bool`.
- **Collection types**: Aggregates like `list`, `set`, and `map`.
- **Structural types**: Complex types like `object` and `tuple` for advanced data modeling.

### 3.3 Expressions and functions
- **Conditionals**: Use `condition ? true_value : false_value` for dynamic logic.
- **For expressions**: Generate collections dynamically (e.g., `for_each` loops).
- **Built-in functions**: Utilize functions like `merge`, `lookup`, `coalesce`, and `jsonencode` for data manipulation.

### 3.4 Dynamic blocks
- **Use case**: Generate repeated nested arguments dynamically (e.g., multiple security group rules).
- **Example**:
  ```hcl
  dynamic "ingress" {
    for_each = var.ingress_rules
    content {
      from_port   = ingress.value.from_port
      to_port     = ingress.value.to_port
      protocol    = ingress.value.protocol
      cidr_blocks = ingress.value.cidr_blocks
    }
  }
  ```

### 3.5 Validation and constraints
- **Variable validation**: Ensure input variables meet specific criteria.
  ```hcl
  variable "instance_type" {
    type    = string
    default = "t2.micro"
    validation {
      condition     = contains(["t2.micro", "t2.small"], var.instance_type)
      error_message = "Invalid instance type. Allowed: t2.micro, t2.small."
    }
  }
  ```
- **Preconditions/postconditions**: Validate resource attributes before and after creation.

---

## 4. Variables, Locals, and Outputs

### 4.1 Variable design
- **Explicit interfaces**: Define variable types and constraints to ensure clarity.
- **Sensible defaults**: Provide defaults only when they are safe and predictable.
- **Sensitive inputs**: Mark sensitive variables to prevent them from being logged.

### 4.2 Locals
- **Computed values**: Use locals to calculate values that simplify configurations.
  ```hcl
  locals {
    app_name = "my-app-${var.environment}"
  }
  ```

### 4.3 Output contracts
- **Minimal exposure**: Expose only the necessary outputs to downstream modules.
- **Example**:
  ```hcl
  output "bucket_arn" {
    value = aws_s3_bucket.my_bucket.arn
  }
  ```

### 4.4 Environment configuration patterns
- **Environment-specific files**: Use `*.tfvars` files for environment-specific values.
- **Avoid secrets in VCS**: Store sensitive values in a secure secret manager.

---

## 5. Providers and Authentication

### 5.1 Provider lifecycle
- **Independent versioning**: Providers are versioned independently of Terraform.
- **Example**:
  ```hcl
  provider "aws" {
    region = "us-east-1"
  }
  ```

### 5.2 Version pinning
- **Best practice**: Always pin provider versions to avoid unexpected changes.
  ```hcl
  terraform {
    required_providers {
      aws = {
        source  = "hashicorp/aws"
        version = "~> 4.0"
      }
    }
  }
  ```

### 5.3 Multi-provider setups
- **Aliased providers**: Use aliases for multi-region or multi-account setups.
  ```hcl
  provider "aws" {
    alias  = "us_west"
    region = "us-west-2"
  }
  ```

### 5.4 Authentication best practices
- **Short-lived credentials**: Use temporary credentials (e.g., AWS STS).
- **OIDC federation**: Use OIDC for CI/CD pipelines to avoid long-lived keys.

---

## 6. Resources, Data Sources, and Dependencies

### 6.1 Resources vs data sources
- **Resources**: Create and manage infrastructure objects.
- **Data sources**: Read existing infrastructure objects without creating them.

### 6.2 Dependency graph
- **Execution graph**: Terraform builds a dependency graph to determine the order of operations.
- **Example**:
  ```hcl
  resource "aws_instance" "web" {
    ami           = data.aws_ami.ubuntu.id
    instance_type = "t2.micro"
  }

  data "aws_ami" "ubuntu" {
    most_recent = true
    owners      = ["099720109477"]
  }
  ```

### 6.3 Explicit dependencies
- **`depends_on`**: Use only when implicit dependencies are insufficient.
  ```hcl
  resource "aws_instance" "example" {
    depends_on = [aws_s3_bucket.example]
  }
  ```

### 6.4 Lifecycle meta-argument
- **Examples**:
  - `create_before_destroy`: Replace resources without downtime.
  - `prevent_destroy`: Prevent accidental deletion.
  - `ignore_changes`: Ignore specific attribute changes.

---

## 7. Scaling Config with Meta-Arguments

### 7.1 `count`
- **Replication**: Create multiple instances of a resource.
  ```hcl
  resource "aws_instance" "example" {
    count         = 3
    instance_type = "t2.micro"
  }
  ```

### 7.2 `for_each`
- **Stable identity**: Use `for_each` for resources with unique keys.
  ```hcl
  resource "aws_s3_bucket" "example" {
    for_each = var.bucket_names
    bucket   = each.value
  }
  ```

### 7.3 Choosing between `count` and `for_each`
- **`count`**: Use for simple numeric replication.
- **`for_each`**: Use for maps or sets to avoid index-shift issues.

### 7.4 Conditional creation
- **Example**:
  ```hcl
  resource "aws_instance" "example" {
    count = var.create_instance ? 1 : 0
  }
  ```

---

## 8. State: Internals and Operations

### 8.1 What state stores
- **Resource instance bindings**: Links between configuration and real-world resources.
- **Attribute values and metadata**: Current values of resource attributes.
- **Dependency data**: Information about resource dependencies for planning.

### 8.2 Why state matters
- **Source of truth**: State is the authoritative source for resource attributes and metadata.
- **Diff planning**: Terraform uses state to determine changes between the current and desired configuration.

### 8.3 Sensitive data caveat
- **Secure storage**: State may contain sensitive data; use secure backends and restrict access.
- **Example**: Encrypt state files at rest and in transit.

### 8.4 State commands
- **`terraform state list`**: List resources in the state.
- **`terraform state show`**: Show detailed information about a resource in the state.
- **`terraform state mv`**: Move a resource from one location to another in the state.
- **`terraform state rm`**: Remove a resource from the state without destroying it.

Operate carefully and with review.

---

## 9. Backends, Locking, and Collaboration

### 9.1 Local backend risks
- **Not for teams**: Local backend is not suitable for team environments; it can lead to state corruption.

### 9.2 Remote backend patterns
- **S3 backend + DynamoDB locking (AWS)**: Common pattern for remote state with locking.
- **Terraform Cloud/Enterprise backend**: Managed backend with additional features.

### 9.3 Locking
- **Prevent concurrent applies**: Locking prevents multiple applies from corrupting the state.
- **Example**: DynamoDB table used for state locking in AWS.

### 9.4 Backend security
- **Encryption at rest**: Encrypt state files in the backend.
- **Access controls with least privilege**: Restrict access to the state backend.
- **Backup/versioning of state storage**: Enable versioning and backups for recovery.

---

## 10. Workspaces and Environment Strategy

### 10.1 Workspaces
- **Multiple state instances**: Workspaces allow multiple instances of state for the same configuration.
- **Default workspace**: The `default` workspace is created automatically.

### 10.2 Recommended environment isolation
- **Strong isolation**: Prefer separate backends/stacks/accounts for production isolation.
- **Workspaces for variation**: Use workspaces for variations of the same environment (e.g., dev, stage).

### 10.3 Environment architecture models
- **Mono-repo with stack folders**: All stacks in one repository, organized by folders.
- **Environment repos**: Separate repositories for each environment.
- **Platform modules + service stacks**: Abstracted platform components and service-specific stacks.

---

## 11. Module Architecture and Reusability

### 11.1 Module design principles
- **Single responsibility**: Each module should have a single, well-defined purpose.
- **Stable interface**: Modules should have a consistent and predictable interface.
- **Minimal side effects**: Modules should not have unintended impacts on other resources.

### 11.2 Module versioning
- **Semantic versioning**: Use semantic versioning (MAJOR.MINOR.PATCH) for module versions.
- **Tagged releases**: Create Git tags for module releases.
- **Explicit source version pinning**: Pin module sources to specific versions.

### 11.3 Module testing
- **Example usage tests**: Include examples and tests in the module repository.
- **Validation checks**: Use `terraform validate` and other tools to check module validity.
- **Policy checks in CI**: Integrate policy checks (e.g., Sentinel, OPA) in the CI pipeline.

### 11.4 Public/private registries
- **Terraform Registry**: Use the public Terraform Registry for sharing modules.
- **Private module registry patterns**: Set up private registries for internal module sharing.

---

## 12. Import, Refactor, and Safe Change Management

### 12.1 Import existing resources
- **Import workflow**: Use `terraform import` to bring existing resources under Terraform management.
- **Example**:
  ```bash
  terraform import aws_instance.example i-1234567890abcdef0
  ```

### 12.2 Refactor without recreation
- **`moved` blocks (modern approach)**: Use `moved` blocks in the configuration to indicate resource moves.
- **`terraform state mv` for controlled state remapping**: Use for precise control over state changes.

### 12.3 Zero-downtime refactors
- **Plan in small steps**: Break down changes into smaller, manageable steps.
- **Use lifecycle settings where needed**: Temporarily adjust lifecycle settings to facilitate changes.
- **Validate dependencies and outputs between stages**: Ensure correctness at each stage of the refactor.

### 12.4 Decommission strategy
- **Soft-disable traffic**: Gradually reduce traffic to the resources being decommissioned.
- **Backup/export state/data**: Ensure backups are taken before decommissioning.
- **Destroy in controlled order**: Destroy resources in a controlled and reviewed manner.

---

## 13. Testing, Quality, and Policy

### 13.1 Quality gates
- **`terraform fmt -check`**: Check formatting of Terraform files.
- **`terraform validate`**: Validate Terraform configuration files.
- **Static analysis tools (`tflint`, `tfsec`, `checkov`)**: Use tools to analyze code for best practices and security.

### 13.2 Policy as code
- **Sentinel**: Use Sentinel for policy as code in Terraform Enterprise.
- **OPA/Conftest**: Use Open Policy Agent (OPA) and Conftest for policy enforcement.
- **Enforce org guardrails (regions, tags, encryption)**: Ensure compliance with organizational policies.

### 13.3 Integration testing
- **Sandbox applies in CI**: Apply configurations in a sandbox environment in CI/CD pipelines.
- **Validate expected outputs**: Check that the outputs and resource states are as expected.

### 13.4 Drift detection
- **Scheduled plans and drift alerts for critical stacks**: Regularly check for configuration drift and alert on changes.

---

## 14. Terraform Security and Secret Handling

### 14.1 Secrets handling
- **Pull secrets from secret manager**: Use secret managers to store and access secrets.
- **Never commit secrets in code**: Ensure secrets are not hard-coded or committed to version control.
- **Restrict state access heavily**: Limit access to state files and encrypt sensitive data.

### 14.2 Least privilege execution roles
- **Scoped permissions per stack**: Assign permissions based on the principle of least privilege.

### 14.3 Supply chain trust
- **Pin provider checksums**: Pin the checksums of provider binaries to prevent tampering.
- **Use trusted module sources**: Only use modules from trusted sources and review their code.

### 14.4 Compliance posture
- **Mandatory tagging**: Enforce tagging policies for resources.
- **Encryption by default policies**: Ensure encryption is enabled for all applicable resources.
- **Audit logging for applies and state access**: Enable logging to track changes and access to state.

---

## 15. CI/CD and Delivery Patterns

### 15.1 Typical pipeline stages
1. Lint/format/validate
2. Security and policy checks
3. Plan
4. Human approval for higher environments
5. Apply
6. Post-apply verification

### 15.2 Branch/environment strategy
- **Feature branch plans**: Create plans in feature branches for isolated testing.
- **Main branch apply to dev/stage**: Apply changes from the main branch to development and staging environments.
- **Promoted artifacts/plans for prod**: Promote approved plans/artifacts to production.

### 15.3 Plan artifact controls
- **Immutable plan artifacts**: Store plan artifacts in an immutable manner to ensure integrity.

### 15.4 Rollback model
- **Controlled remediation**: Have a plan for controlled rollback and remediation in case of issues.

---

## 16. Terraform with AWS (Practical Mapping)

### 16.1 Common stack boundaries
- **Network foundation stack**: VPC, subnets, route tables, internet gateways.
- **Security baseline stack**: IAM roles, policies, security groups.
- **Platform services stack**: ECS/EKS, RDS, ElastiCache.
- **App/service stack**: Application-specific resources.

### 16.2 AWS best practices in Terraform
- **Remote state in dedicated infra account**: Use a dedicated account for infrastructure state.
- **Cross-account role assumption**: Use IAM roles for cross-account access.
- **Region/account abstraction with provider aliases**: Abstract region and account details using provider aliases.
- **Tagging and naming standards via locals/modules**: Centralize tagging and naming conventions.

### 16.3 Common AWS resources to model
- **VPC, subnets, routing, gateways**: Model the network infrastructure.
- **IAM roles/policies**: Define roles and policies for access control.
- **EC2/ASG/ALB**: Model compute resources and load balancers.
- **ECS/EKS baseline**: Define the baseline configuration for container orchestration.
- **S3, KMS, CloudWatch, alarms**: Model storage, encryption, logging, and monitoring resources.

---

## 17. Terraform Cloud/Enterprise Concepts

### 17.1 Workspaces and runs
- **Remote execution**: Terraform Cloud/Enterprise manages the execution of Terraform runs.
- **Variables and variable sets**: Define variables at the workspace or organization level.
- **Run triggers**: Automatically trigger runs based on VCS changes or other events.

### 17.2 Governance and teams
- **RBAC, policy checks, run tasks**: Role-based access control and policy enforcement.
- **Private module registry**: Use a private registry for sharing modules within the organization.

### 17.3 Operational controls
- **Cost estimation**: Estimate the cost of infrastructure changes before applying.
- **Drift detection workflows**: Detect and manage drift in infrastructure.
- **Audit trails**: Maintain a detailed audit trail of changes and accesses.

---

## 18. Troubleshooting Playbook

### 18.1 Common failures
- **Auth errors**: Check authentication credentials and permissions.
- **Provider schema/version mismatch**: Ensure provider versions are compatible with the configuration.
- **Resource already exists conflict**: Resolve conflicts with existing resources.
- **Rate limits or API throttling**: Handle API rate limits and throttling errors.
- **Dependency cycles**: Break dependency cycles in resource references.

### 18.2 Debug workflow
1. Re-run with detailed logs only when needed
2. Inspect state and graph
3. Validate provider credentials/context
4. Isolate failing module/resource
5. Apply minimal corrective changes

### 18.3 State mismatch issues
- **Import unmanaged resources**: Import resources that were created outside of Terraform.
- **Use moved blocks for rename/refactor**: Use `moved` blocks to indicate resource renames or moves.
- **Avoid manual state edits unless unavoidable and reviewed**: Manual edits can lead to inconsistencies; avoid them if possible.

---

## 19. Anti-Patterns to Avoid

- **Single giant root module for entire enterprise**: Leads to unmanageable and hard-to-navigate configurations.
- **Unpinned provider/module versions**: Can cause unexpected changes and breakages.
- **Shared state without locking**: Risks state corruption and conflicts.
- **Manual console changes in managed stacks**: Can lead to drift and inconsistencies.
- **Broad admin permissions for all Terraform runs**: Violates the principle of least privilege.
- **Ignoring plan warnings and replacing critical resources blindly**: Can result in data loss or downtime.

---

## 20. Interview and Real-World Decision Framework

For Terraform architecture questions:
1. Repository and module structure
2. State and backend isolation strategy
3. Security model (roles, secrets, policy)
4. Promotion workflow across environments
5. Drift and governance strategy
6. Operational ownership and incident response

---

## 21. Comprehensive Topic Checklist

- IaC principles and Terraform workflow
- Language syntax, types, expressions, functions
- Resources/data/meta-arguments
- State internals and backend strategy
- Workspaces and environment isolation
- Module design and versioning
- Import/refactor/migration safety
- Testing, linting, policy enforcement
- Security and secret management
- CI/CD and enterprise operations
- Provider/auth architecture
- Troubleshooting and anti-pattern prevention

If each checklist area has standards + examples + CI enforcement, Terraform adoption is production-ready.
