# Production Troubleshooting for ML Systems

## A disciplined incident approach

1. Determine user impact and stop unsafe automated actions.
2. Record the timeline, active versions, and recent changes.
3. Check service, data, model, and business signals separately.
4. Compare healthy and affected requests by meaningful slices.
5. Roll back or activate a fallback when evidence supports it.
6. Preserve logs and artifacts for root-cause analysis.
7. Add a prevention, detection, and mitigation action after recovery.

Avoid changing several components simultaneously because that destroys evidence about the cause.

## Scenario: latency suddenly doubled

Check p50, p95, and p99 rather than only average latency. Break the request into feature lookup, preprocessing, model compute, downstream calls, and serialization. Compare payload sizes and batch behavior. Inspect CPU/GPU utilization, memory pressure, garbage collection, network time, cache hit rate, autoscaling, and cold starts.

Possible responses include rolling back the artifact, reducing traffic, increasing replicas, disabling an expensive optional feature, or using a smaller fallback model. Capacity alone does not fix a newly introduced blocking call or unbounded input.

## Scenario: prediction distribution shifted overnight

First determine whether the population changed or the pipeline broke. Compare schema, null rate, min/max, categories, upstream row counts, feature freshness, timezone behavior, and deployed versions. Replay a known request through old and new pipelines and inspect feature values before inspecting model internals.

A shift may be legitimate after a campaign, holiday, policy change, or external event. Do not retrain automatically until the cause and performance impact are understood.

## Scenario: offline performance is high, live performance is poor

Investigate leakage, non-representative splits, training-serving skew, delayed labels, selection bias, threshold mismatch, online preprocessing differences, stale features, and a mismatch between the model metric and product goal. Reconstruct live inputs using the exact prediction timestamp.

Also test whether the model changes user behavior. Once actions are based on predictions, observed live outcomes may no longer match passively collected training data.

## Scenario: model returns the same class for almost everything

Inspect raw logits or probabilities, input scaling, feature order, missing-value handling, label mapping, serialization, and output postprocessing. Confirm the correct model artifact is loaded. Compare one known sample at each boundary: training notebook, exported artifact, container, and live service.

The issue is often a preprocessing or label-map mismatch rather than model collapse.

## Scenario: RAG answers are fluent but wrong

Separate retrieval from generation:

- Was the necessary source ingested?
- Did parsing preserve its text and structure?
- Was it chunked with adequate context?
- Did retrieval return it?
- Did reranking keep it?
- Did the prompt include it?
- Did generation follow it?

Measure recall@k on labelled queries. Inspect authorization filtering because a missing result may be correctly excluded. Improve grounding instructions and abstention only after retrieval has sufficient evidence.

## Scenario: vector search quality dropped after an upgrade

Verify that query and document vectors use the same embedding model and normalization. Check dimensions, distance metric, index build parameters, metadata filters, and backfill completion. Compare approximate results with exact search on a sample to isolate indexing recall from embedding quality.

Never mix vectors from unrelated embedding spaces in one index unless the model explicitly guarantees compatibility.

## Scenario: training job became much slower

Compare data volume, feature count, sequence length, model parameters, batch size, workers, storage throughput, cache behavior, accelerator utilization, and library versions. Profile the input pipeline independently. A GPU waiting for data indicates that a larger accelerator will not solve the bottleneck.

## Scenario: monitoring shows drift but quality is unchanged

Drift tests can be sensitive to harmless distribution changes. Identify which features moved, whether the model depends strongly on them, and whether movement occurs near the decision boundary. Continue monitoring ground-truth performance, calibration, and business results. Retraining without need can introduce regression and cost.

## Scenario: quality declined only for one segment

Confirm sample size and label quality, then compare feature availability, representation in training data, calibration, error types, and threshold behavior for the segment. A global threshold or objective may not satisfy every operational constraint. Any segment-specific treatment must be justified, lawful, monitored, and documented.

## Scenario: repeated duplicate predictions or writes

Retries in distributed systems can execute an operation more than once. Use an idempotency key derived from the logical request, enforce unique constraints, and make consumers commit offsets only after durable writes. Exactly-once marketing claims do not replace application-level idempotency.

## Root-cause report structure

- User and business impact
- Detection method and why it was or was not timely
- Precise timeline
- Technical root cause and contributing factors
- Immediate mitigation
- Why existing controls did not prevent it
- Prevention, detection, and mitigation actions with owners
- Evidence that each corrective action works

Focus on system improvement rather than individual blame.

