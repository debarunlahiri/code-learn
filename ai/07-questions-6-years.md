# Question Bank: Around 6 Years of Experience

## 1. How do you translate a business goal into an ML objective?

Identify the decision being improved, define the unit and prediction horizon, quantify error costs, establish a non-ML baseline, and choose an offline metric known to correlate with the business outcome. Document guardrails so optimization cannot create unacceptable side effects.

## 2. Offline metrics improved but business results fell. Why?

Possible causes include metric misalignment, distribution shift, feedback loops, calibration errors, latency, changed product placement, biased experiment assignment, novelty effects, or harm to an unmeasured segment. Validate instrumentation and analyze the complete decision pipeline by slice.

## 3. Design a safe model rollout.

Validate artifacts and contracts, replay historical traffic, use shadow mode, expose a small canary, compare service and model metrics, then increase traffic behind automatic and manual gates. Keep immutable artifacts, a last-known-good version, and a tested rollback path.

## 4. When should a system retrain?

Retrain when enough representative labels arrive, performance falls, material drift occurs, or scheduled freshness is required. A trigger should start evaluation, not automatic promotion. Compare against the current champion and pass quality, fairness, cost, and reliability gates.

## 5. How do you manage delayed ground truth?

Monitor leading indicators such as score distribution and data quality while waiting. Join predictions to outcomes with immutable IDs, report performance by prediction date, backfill metrics, and avoid labelling unresolved cases as negatives.

## 6. How do you reduce inference cost without unacceptable quality loss?

Profile first. Consider batching, caching, quantization, distillation, pruning, smaller candidate sets, approximate search, model routing, shorter prompts, precomputed features, and autoscaling. Evaluate quality and tail latency on realistic traffic after every change.

## 7. How would you migrate an embedding model?

Create a new versioned index because vector spaces are incompatible. Dual-write or backfill embeddings, evaluate retrieval on labelled queries, shadow reads, shift traffic gradually, and retain rollback until the new index is stable.

## 8. How do you handle feedback loops?

Log exposure and decision propensities, retain exploration where safe, distinguish observed outcomes from counterfactual outcomes, monitor population changes, and consider inverse-propensity weighting or randomized experiments. Do not train blindly on outcomes produced by the previous policy.

## 9. How do you lead model selection across a team?

Agree on metrics, constraints, datasets, validation protocol, and compute budget before experimentation. Require reproducible baselines and ablations. Select the simplest option meeting objectives, document the decision, and separate scientific comparison from release approval.

## 10. Build versus buy for a model platform?

Compare strategic differentiation, data sensitivity, required customization, integration, lock-in, operational skill, reliability, compliance, total cost, and exit strategy. Prototype the highest-risk assumptions and decide component by component rather than treating the platform as one indivisible choice.

## 11. How do you investigate a sudden prediction shift?

Confirm monitoring and deployment timestamps, compare model and feature versions, inspect schemas and upstream jobs, segment the shift, replay known samples, and trace a few requests end to end. Stop or roll back automated actions if impact is unsafe, while preserving evidence.

## 12. What belongs in an ML architecture review?

Requirements, baseline, data contracts, label definition, leakage analysis, evaluation slices, serving capacity, failure modes, security, privacy, fairness, rollout, observability, ownership, cost, rollback, and retirement criteria.

## 13. How do you make an LLM workflow reliable?

Prefer deterministic orchestration where possible. Use typed tool schemas, input/output validation, bounded loops, least privilege, timeouts, idempotency, trace logging, evaluation suites, and human approval for high-impact operations.

## 14. How do you mentor less-experienced engineers?

Set a clear outcome and constraints, explain decision context, review reasoning as well as code, give ownership with safe boundaries, provide timely specific feedback, and gradually expand scope. Measure success by growing independent judgment, not dependence on the reviewer.

## 15. How do you prioritize ML technical debt?

Inventory debt in data, features, models, infrastructure, and monitoring. Rank it by user risk, incident probability, development drag, compliance exposure, and cost. Attach debt removal to measurable reliability or delivery outcomes and assign ownership.

