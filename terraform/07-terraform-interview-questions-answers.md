# Terraform Interview Questions and Answers

## 1. What is Terraform state and why is it needed?
State maps configuration resources to real infrastructure objects and stores metadata/dependencies.

## 2. `count` vs `for_each`?
- `count` is index-based and suitable for simple replicas.
- `for_each` is key-based and better for stable identity and map/set inputs.

## 3. What happens during `terraform plan`?
Terraform compares desired config with state and provider data, then shows create/update/delete actions.

## 4. Why remote backend + locking?
To avoid concurrent state corruption and support team collaboration.

## 5. How to manage multiple environments?
Use separate state files/backends, workspace strategy, and environment-specific variable files.

## 6. How to import existing resource?
Use `terraform import` and then align HCL config with imported resource attributes.

## 7. What is drift?
Drift occurs when real infrastructure changes outside Terraform. Terraform plan reveals drift.

## 8. How to prevent secrets leakage in Terraform?
- Avoid plaintext vars
- Use secret managers
- Restrict state access
- Mark sensitive outputs

## 9. Module best practices?
- Small focused modules
- Clear input/output contracts
- Version pinning
- Documentation and examples

## 10. `taint` and `-replace` use case?
Force recreation of problematic resources when update-in-place is unsafe or impossible.

## 11. How to safely refactor resources?
Use `terraform state mv` and planned incremental changes; avoid destroy/recreate of critical resources.

## 12. Common production pitfalls
- Unpinned provider versions
- Shared mutable state
- No policy checks
- Overly broad IAM permissions
- Manual console edits
