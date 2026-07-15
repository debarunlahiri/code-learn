# Recommendation Systems

## Problem formulation

A recommendation system selects and orders items for a user or context. Clarify whether the objective is click, purchase, watch time, discovery, retention, or long-term value. Optimizing a short-term proxy can harm satisfaction through repetition or low-quality engagement.

## Common signals

Explicit feedback includes ratings and declared preferences. Implicit feedback includes views, clicks, dwell time, purchases, skips, and hides. Implicit absence is ambiguous: a user may not dislike an item; they may never have seen it.

Log impression data as well as actions. Without impressions, the system cannot distinguish ignored items from items that were never offered.

## Collaborative filtering

User-based methods find similar users; item-based methods find items consumed by similar users. Matrix factorization approximates the interaction matrix as user and item latent vectors. Their dot product estimates affinity.

Collaborative filtering captures behavioral patterns but struggles with new users and items, sparse data, and rapidly changing preferences.

## Content-based recommendation

Content-based methods compare user-history representations with item attributes or embeddings. They handle new items better when metadata exists, but can over-specialize and reduce discovery.

## Two-stage architecture

At large scale, scoring every item is too expensive:

1. **Candidate generation** retrieves hundreds or thousands of likely items using multiple sources.
2. **Ranking** applies a richer model to a small candidate set.
3. **Re-ranking** enforces diversity, freshness, availability, safety, and business rules.

Candidate sources can include collaborative retrieval, semantic vectors, recent popularity, subscriptions, and editorial collections. Track which source contributed each candidate.

## Ranking models

Features may describe user history, item quality, context, interaction crosses, recency, and source. Logistic regression provides a baseline. Gradient boosting performs well on engineered tabular features. Deep models can learn embeddings and complex interactions from large-scale sparse data.

Pointwise loss treats each item independently. Pairwise loss learns that one item should rank above another. Listwise methods optimize a complete ordered list more directly but are more complex.

## Negative sampling

Positive interactions are sparse, so training commonly samples negatives. Random unseen items are easy negatives and may teach little. Impression-but-not-clicked items are harder, but position and presentation bias affect them. Mix strategies and ensure the sampled training distribution does not distort probability calibration.

## Evaluation

- Precision@k: fraction of top-k items that are relevant.
- Recall@k: fraction of relevant items recovered in top-k.
- NDCG@k: rewards placing relevant items earlier, with graded relevance.
- MRR: reciprocal position of the first relevant item.
- Coverage: fraction of the catalog or users receiving useful results.
- Diversity and novelty: whether recommendations avoid excessive similarity and obvious popularity.

Offline evaluation uses logged behavior shaped by an older policy. It cannot fully predict causal online impact. Use controlled experiments with guardrails for complaints, latency, diversity, and long-term behavior.

## Cold start

For a new user, use onboarding preferences, geography when appropriate, context, and high-quality popularity priors. For a new item, use content embeddings, metadata, creator signals, or limited exploration. Exploration must respect safety and business constraints.

## Feedback loops and bias

Popular items receive more exposure, producing more interaction and further popularity. Position bias makes top-ranked items more likely to receive clicks independent of relevance. Randomized exploration, propensity logging, counterfactual evaluation, and source diversity can reduce these problems.

## Real-time personalization

Long-term preference can be precomputed, while recent-session features update from a stream. Store online features with event time and freshness. If the real-time store fails, fall back to cached long-term preferences or non-personalized candidates rather than failing the entire request.

