# Project Discussion Guide

Strong project explanations show judgment, ownership, depth, and measured results. Avoid listing technologies without explaining why they were selected.

## A complete project narrative

### 1. Context

Explain the user problem and previous process. Quantify the baseline: volume, delay, error rate, manual effort, cost, or missed opportunity. State the constraint that made the work difficult.

### 2. Your responsibility

Clearly separate your contribution from the team's. Mention the decisions you owned, components you implemented, reviews you led, and stakeholders you coordinated with.

### 3. Data and labels

Describe sources, size, freshness, schema, data-quality issues, label definition, delay, and potential bias. Explain the split strategy and why random splitting was or was not valid.

### 4. Baseline and modelling

Start with the simplest credible baseline. Explain candidate models, experiment protocol, metric selection, error analysis, and rejected alternatives. State why the final model was appropriate for latency, explainability, cost, or maintenance constraints.

### 5. Architecture

Walk through ingestion, feature computation, training, artifact storage, serving, monitoring, and feedback. Include scale and reliability numbers where possible. Explain the fallback behavior for missing features, service timeout, or bad deployment.

### 6. Result

Report offline and live outcomes separately. Useful results include precision at operating recall, hours saved, reduced loss, additional revenue, latency, cost per prediction, adoption, or reduced review volume. Explain how causality was established, such as through a controlled experiment.

### 7. Learning

Describe one decision you would change with current knowledge. This demonstrates reflection when paired with a concrete improvement.

## Detailed example: customer-support routing

### Context

Support requests were manually assigned across specialist queues. Incorrect routing increased first-response time and caused repeated transfers. The system needed to make a suggestion within existing API latency limits and support newly created categories.

### Data

Historical tickets contained free text, account metadata, and final resolver queue. The final queue was not always a clean label because tickets could be transferred for staffing reasons. The team removed clear operational transfers, grouped related categories, and split chronologically to simulate future traffic.

### Baseline and model

A keyword-rules baseline established operational value and revealed ambiguous categories. TF-IDF with logistic regression provided a fast, interpretable model. A transformer improved macro F1 but added latency and infrastructure cost. The chosen design used the linear model for common categories and rules for mandatory routing, while retaining the transformer as a later option after latency optimization.

### Release

The service first ran in shadow mode. Predictions, probabilities, model version, and final human choice were logged. A calibrated threshold allowed automatic routing only for confident cases; uncertain cases remained manual. Rollout expanded by queue after slice metrics and transfer rate remained within limits.

### What this example demonstrates

The important points are not the algorithm names. The explanation shows label scrutiny, a strong baseline, cost-aware model choice, confidence-based human fallback, gradual release, and a feedback path.

## Explaining a failure

Use a failure where you had meaningful responsibility. Explain the evidence available at the time, your decision, actual outcome, immediate correction, and lasting system change. Do not disguise an ordinary success as a failure.

Example pattern:

> A feature pipeline used processing time instead of event time. Offline metrics looked normal, but late events changed live aggregates. I traced mismatched predictions to the join window, rolled back the feature version, rebuilt point-in-time data, and added event-time contract tests plus freshness monitoring.

## Explaining technical trade-offs

Instead of saying “we used gradient boosting because it performed best,” explain:

- How much it improved the relevant metric.
- Which segments improved or regressed.
- Its inference latency and compute cost.
- Whether probabilities were calibrated.
- Why explainability was sufficient.
- How operational complexity compared with the baseline.

## Leadership at greater scope

At broader ownership levels, include how you aligned teams on success metrics, resolved disagreement with evidence, reduced delivery risk, delegated work, established review gates, managed incidents, and changed platform standards. Technical depth remains necessary, but personal coding volume is not the only evidence of impact.

## Follow-up questions to prepare for every project

- What was the simplest baseline?
- Why did the chosen metric match the product decision?
- What leakage risks existed?
- What were the largest error slices?
- What happened when a dependency failed?
- How did you measure live impact?
- How was the model rolled back?
- What was the monthly infrastructure cost?
- What would fail at ten times the traffic?
- What would you redesign now?

