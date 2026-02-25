# AWS Integration and Messaging Services

## 1. SQS (Queue)

### Types
- Standard queue: high throughput, at-least-once delivery
- FIFO queue: exactly-once processing (within dedup window), strict order

### Key design settings
- Visibility timeout
- Dead-letter queue (DLQ)
- Long polling

## 2. SNS (Pub/Sub)
- Fan-out messaging
- Targets: SQS, Lambda, HTTP, email, SMS
- Good for event notifications and decoupled broadcast

## 3. EventBridge
- Event bus for routing events between services/apps
- Rules filter events and route to targets
- Supports SaaS integrations and custom events

## 4. Step Functions
- Workflow orchestration for long-running/multi-step business processes
- Visual workflow, retries, error handling
- Standard and Express workflows

## 5. Amazon MQ and MSK
- Amazon MQ: managed ActiveMQ/RabbitMQ
- MSK: managed Kafka for streaming platform use cases

## 6. Typical patterns
- API -> SQS -> Worker (async processing)
- Event source -> EventBridge -> multiple targets
- Saga workflow via Step Functions
