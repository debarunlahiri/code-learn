# Data Engineering and MLOps

## Reproducible training

Version source code, data snapshots, feature definitions, configuration, dependencies, random seeds, and model artifacts. Record metrics and environment details in an experiment tracker. A seed alone is not a guarantee of bitwise reproducibility across hardware and library versions.

## Feature pipelines

Training-serving skew occurs when offline and online feature logic differs. Reduce it by sharing transformations, defining point-in-time-correct joins, versioning schemas, validating ranges, and monitoring feature distributions. A feature store can help reuse consistent definitions but adds operational complexity.

## Deployment patterns

- **Batch:** best for high-throughput predictions that tolerate delay.
- **Online:** request-response serving with strict latency needs.
- **Streaming:** event-driven scoring with near-real-time state.
- **Edge:** low latency or privacy near the device, with constrained resources.

Release strategies include shadow deployment, canary rollout, blue-green deployment, and A/B testing. A model passing offline checks should still be gradually exposed.

## Model registry and CI/CD/CT

A registry records versions, lineage, metrics, approvals, and stages. CI tests code and data contracts. CD safely releases artifacts. Continuous training retrains after a trigger such as schedule, drift, new labels, or performance decline; it should not promote automatically without quality gates.

## Monitoring

Monitor four layers:

1. Service: latency, throughput, errors, saturation, and availability.
2. Data: schema, nulls, ranges, freshness, and category changes.
3. Model: prediction distributions, calibration, slice metrics, and delayed ground-truth performance.
4. Business: conversion, losses prevented, review load, or another outcome.

## Drift

- Data drift: `P(X)` changes.
- Label drift: `P(Y)` changes.
- Concept drift: `P(Y|X)` changes.

PSI, KS tests, Jensen-Shannon divergence, and feature monitoring detect distribution movement, but drift does not always mean performance degradation. Validate against labels and operational outcomes before retraining.

## Reliability

Define service-level objectives, capacity limits, timeouts, retries with backoff, circuit breakers, and fallbacks. Cache only when staleness is acceptable. Make batch writes and asynchronous handlers idempotent. Preserve a last-known-good model for rollback.

## Governance

Maintain lineage, access control, audit logs, model cards, intended-use boundaries, retention rules, and approval history. Evaluate fairness across relevant slices using metrics appropriate to the harm. Privacy measures can include minimization, encryption, tokenization, differential privacy, and federated learning.

