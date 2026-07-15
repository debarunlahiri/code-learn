# ML System Design

## A reusable design framework

1. Clarify the user, decision, prediction target, and time horizon.
2. Define business and ML metrics plus unacceptable failures.
3. Estimate scale: records, QPS, latency, freshness, and budget.
4. Describe data sources, labels, leakage risks, and feedback loops.
5. Establish a simple baseline.
6. Design training, validation, versioning, and orchestration.
7. Choose batch, online, streaming, or edge serving.
8. Plan rollout, monitoring, fallback, retraining, and governance.

## Example: fraud detection

### Requirements

Score a payment within 100 ms, reduce monetary loss, and keep false declines below an agreed rate. Labels arrive after chargeback resolution and are delayed.

### Design

- Stream transaction events and compute point-in-time account features.
- Fetch low-latency features from an online store.
- Combine a rules layer with a calibrated boosted-tree model.
- Return approve, review, or decline based on cost-sensitive thresholds.
- Log features, model version, score, decision, and later outcome.
- Shadow and canary new models; retain the previous model and a rules-only fallback.

### Risks

Attackers adapt, review decisions can bias future labels, and delayed labels slow measurement. Monitor by geography, payment method, account age, and other valid operational slices.

## Example: recommendation service

Use a two-stage system. Candidate generation retrieves hundreds of items using collaborative signals, embeddings, popularity, and business constraints. A ranking model scores the candidates with user, item, and context features. A final policy applies diversity, freshness, inventory, and safety constraints.

Offline ranking metrics do not fully capture user value. Use guarded online experiments and monitor novelty, diversity, long-term engagement, latency, and filter bubbles.

## Example: enterprise RAG assistant

- Ingest permitted documents incrementally.
- Parse structure, attach ACL and source metadata, and create semantic chunks.
- Use hybrid keyword/vector retrieval followed by reranking.
- Enforce access filters before context reaches the model.
- Generate only from evidence, cite sources, and abstain when retrieval is weak.
- Cache safe repeated work, not authorization decisions.
- Evaluate retrieval and generation separately.

## Capacity calculation example

At 500 requests/second and 40 ms average model compute, the theoretical concurrent compute is `500 × 0.04 = 20` requests. Provision above this for tail latency, bursts, failures, and maintenance. Benchmark realistic payloads because averages hide p95 and p99 behavior.

## Trade-offs to state explicitly

- Accuracy versus latency and cost
- Freshness versus pipeline complexity
- Personalization versus privacy
- Model complexity versus explainability
- Consistency versus availability
- Build versus buy
- Automated action versus human review

