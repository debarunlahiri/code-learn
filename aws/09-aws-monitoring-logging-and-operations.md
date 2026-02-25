# AWS Monitoring, Logging, and Operations

## 1. CloudWatch

### Capabilities
- Metrics
- Logs
- Alarms
- Dashboards
- Events/EventBridge integration

### Best practices
- Define SLO-oriented alarms
- Use composite alarms to reduce noise
- Use log retention policies

## 2. CloudTrail

Records API activity for audit and incident investigation.

### Best practices
- Organization trail
- Multi-region enabled
- Send logs to secure S3 bucket

## 3. AWS X-Ray
- Distributed tracing for microservices
- Analyze latency and service map

## 4. Systems Manager
- Session Manager
- Patch Manager
- Parameter Store
- Run Command

## 5. Incident Response Checklist
1. Detect via alarms/findings
2. Contain blast radius
3. Collect evidence (logs/events)
4. Eradicate root cause
5. Recover services
6. Postmortem and preventive action
