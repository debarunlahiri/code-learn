# Complete AI/ML Success Cheat Sheet

Use this as a fast revision and response-building guide. It covers technical knowledge, coding, design, production judgment, project communication, and professional discussion.

## 1. A strong response pattern

For a definition or concept, use:

`definition -> intuition -> example -> limitation -> trade-off`

For a design problem, use:

`requirements -> scale -> data -> baseline -> model -> serving -> evaluation -> monitoring -> safety -> cost -> fallback`

For a coding problem, use:

`clarify -> examples -> edge cases -> simple solution -> code -> complexity -> tests -> optimization`

For a production incident, use:

`impact -> timeline -> evidence -> isolate -> mitigate -> root cause -> prevention`

For a past project, use:

`situation -> responsibility -> action -> measurable result -> lesson`

## 2. Essential AI and ML definitions

- Artificial intelligence: systems that perform tasks associated with human intelligence.
- Machine learning: algorithms that learn patterns from data rather than relying only on explicit rules.
- Supervised learning: learn from labelled input-output examples.
- Unsupervised learning: discover structure without target labels.
- Semi-supervised learning: combine a small labelled set with a larger unlabelled set.
- Self-supervised learning: create learning signals from the data itself.
- Reinforcement learning: learn actions from rewards through interaction with an environment.
- Classification: predict a discrete category.
- Regression: predict a continuous value.
- Clustering: group similar observations without labels.
- Dimensionality reduction: represent data with fewer informative dimensions.
- Feature: an input variable used by a model.
- Label or target: the outcome to predict.
- Parameter: learned from training data.
- Hyperparameter: chosen before or around training.
- Inference: using a trained model to produce an output.

## 3. Core workflow

1. Define the user decision and measurable business outcome.
2. Identify data sources, labels, constraints, and leakage risks.
3. Split data by a method that matches real use, often time-based or group-based.
4. Build a simple baseline.
5. Create reproducible preprocessing and features.
6. Train and tune without touching the final test set.
7. Evaluate by relevant slices and error costs.
8. Deploy through shadow, canary, or controlled rollout.
9. Monitor inputs, outputs, performance, latency, and cost.
10. Retrain or roll back using explicit triggers.

## 4. Data splitting and leakage

- Training set: fits parameters.
- Validation set: selects models and hyperparameters.
- Test set: estimates final generalization once decisions are complete.
- Cross-validation: rotates validation folds for a more stable estimate.
- Stratified split: preserves label proportions.
- Group split: keeps related records in one partition.
- Time split: trains on the past and validates on the future.

Leakage occurs when training uses information unavailable at prediction time. Common causes are preprocessing before splitting, future aggregates, target-derived features, duplicate entities across partitions, and incorrectly timestamped joins.

Fit imputers, scalers, encoders, and feature selection only on training data. Use point-in-time correct feature joins.

## 5. Bias, variance, fitting, and regularization

- Underfitting: poor performance on both training and validation data; increase useful capacity, improve features, or train longer.
- Overfitting: strong training performance but weak validation performance; add data, regularization, augmentation, early stopping, or reduce complexity.
- High bias: systematic error from restrictive assumptions.
- High variance: sensitivity to the particular training sample.

Regularization adds a complexity penalty:

`objective = data loss + λ × penalty`

- L1 promotes sparse weights and feature selection.
- L2 smoothly shrinks weights and handles correlated features more stably.
- Early stopping limits effective complexity.
- Dropout randomly removes activations during training.

## 6. Metrics at a glance

### Classification

`precision = TP / (TP + FP)`

`recall = TP / (TP + FN)`

`specificity = TN / (TN + FP)`

`F1 = 2 × precision × recall / (precision + recall)`

- Accuracy: useful when classes and error costs are balanced.
- Precision: important when false positives are costly.
- Recall: important when false negatives are costly.
- F1: balances precision and recall but ignores true negatives.
- ROC-AUC: ranking across thresholds; can look optimistic with extreme imbalance.
- PR-AUC: emphasizes positive-class performance and is often better for rare events.
- Log loss: rewards calibrated probabilities and strongly penalizes confident errors.
- Calibration: predicted probability should match observed frequency.

Choose the decision threshold using operational cost, capacity, or risk—not automatically `0.5`.

### Regression

- MAE: `mean(|y-ŷ|)`; interpretable and relatively robust.
- MSE: `mean((y-ŷ)²)`; heavily penalizes large errors.
- RMSE: square root of MSE; returns to target units.
- R²: fraction of variance explained relative to predicting the mean.
- MAPE: percentage error but unstable near zero.

### Ranking and retrieval

- Precision@k: relevant items among the first `k`.
- Recall@k: relevant items found among the first `k`.
- MRR: rewards an early first relevant result.
- nDCG: rewards correctly ordered graded relevance.
- Hit rate: whether at least one relevant item appears.

## 7. Algorithms and when to use them

### Linear regression

Use for an interpretable continuous baseline and approximately linear relationships. Check residuals, nonlinearity, outliers, multicollinearity, and heteroscedasticity.

### Logistic regression

Use for an interpretable classification baseline and calibrated probabilities. It models log-odds linearly. Scale features when regularization or optimization benefits from it.

### Decision tree

Handles nonlinear relationships, mixed feature types, and interactions. It is easy to explain but unstable and prone to overfitting without depth, leaf-size, or pruning controls.

### Random forest

Bagged trees reduce variance and work well on tabular data. They are robust but can be larger and slower, and do not extrapolate regression trends well.

### Gradient boosting

Trees sequentially correct residual errors. It is often a strong choice for structured data. Tune learning rate, number of trees, depth, sampling, and regularization together.

### K-nearest neighbours

Simple non-parametric method for local patterns. It needs scaled features, becomes expensive at prediction time, and degrades in high dimensions.

### Support vector machine

Maximizes margin; kernels model nonlinear boundaries. Strong for medium-sized high-dimensional data but expensive at large scale and sensitive to `C`, kernel, and scaling.

### Naive Bayes

Fast probabilistic baseline, especially for sparse text. It assumes conditional feature independence but can still classify well when that assumption is imperfect.

### K-means

Alternates point assignment and centroid update. Use for compact, roughly spherical clusters under scaled Euclidean distance. Choose `k` with domain judgment plus elbow, silhouette, and stability analysis.

### PCA

Projects centred data onto orthogonal maximum-variance directions. Scale features when units differ. It helps compression and visualization but reduces direct interpretability.

## 8. Feature engineering checklist

- Missingness indicators when absence carries meaning
- Numeric scaling for distance-based or gradient-based models
- One-hot encoding for unordered categories
- Ordinal encoding only for meaningful order
- Frequency, target, or learned encoding with leakage controls
- Log transform for positive heavy-tailed features
- Cyclic encoding for hour, weekday, or month
- Time-since-event and rolling-window features
- Text normalization, TF-IDF, or embeddings
- Domain interactions and ratios with safe division
- Point-in-time correct aggregates

Tree models usually do not require scaling, while KNN, SVM, PCA, linear models with regularization, and neural networks often benefit from it.

## 9. Probability and statistics formulas

`P(A|B) = P(A∩B)/P(B)`

`P(A|B) = P(B|A)P(A)/P(B)`

`E[X] = ΣxP(X=x)` or `∫xf(x)dx`

`Var(X) = E[X²] - E[X]²`

`Cov(X,Y) = E[(X-E[X])(Y-E[Y])]`

`Correlation = Cov(X,Y)/(σXσY)`

- A p-value is the probability of data at least this extreme assuming the null; it is not the probability that the null is true.
- A confidence interval describes uncertainty in an estimation procedure.
- Type I error: false positive.
- Type II error: false negative.
- Power: probability of detecting a real effect, `1-β`.
- Correlation does not establish causation.
- Statistical significance does not guarantee practical importance.

## 10. Linear algebra and calculus formulas

`a · b = Σaᵢbᵢ = ||a||||b||cosθ`

`L1 = Σ|xᵢ|`

`L2 = sqrt(Σxᵢ²)`

`Av = λv`

`θ_new = θ_old - η∇L(θ)`

`d/dx f(g(x)) = f'(g(x))g'(x)`

- Gradient: vector of first partial derivatives.
- Jacobian: first derivatives of a vector-valued function.
- Hessian: second derivatives describing local curvature.
- SVD: `A = UΣVᵀ`; useful for compression, denoising, and low-rank approximation.
- PCA: eigenvectors of the covariance matrix or right singular vectors of centred data.

## 11. Deep-learning essentials

### Activation functions

- ReLU: `max(0,x)`; efficient default for many hidden layers.
- Leaky ReLU: retains a small negative slope to reduce dead units.
- Sigmoid: maps to `(0,1)`; common for binary output probabilities.
- Tanh: maps to `(-1,1)` but can saturate.
- Softmax: converts logits into a categorical probability distribution.

### Training problems

- Vanishing gradients: residual connections, normalization, appropriate activation and initialization.
- Exploding gradients: gradient clipping, lower learning rate, stable initialization.
- Overfitting: augmentation, dropout, weight decay, early stopping, more data.
- Unstable loss: inspect bad samples, scaling, mixed precision, batch size, and learning rate.
- Dead ReLUs: reduce learning rate or use leaky variants.

### Optimizers

- SGD: noisy updates; often strong generalization with a tuned schedule.
- Momentum: accelerates consistent directions and damps oscillation.
- RMSProp: normalizes using recent squared gradients.
- Adam: combines first and second moment estimates; useful default for many tasks.

### CNNs, sequence models, and transformers

- CNN: shared local filters; strong for spatial and local patterns.
- RNN: recurrent state for sequences; difficult to parallelize.
- LSTM/GRU: gates improve long-range gradient flow.
- Transformer: self-attention processes relationships between tokens in parallel.

`Attention(Q,K,V) = softmax(QKᵀ/sqrt(d_k))V`

## 12. Generative systems and RAG

Choose:

- Prompting for instructions, examples, and formatting.
- RAG for private, changing, or citable knowledge.
- Fine-tuning for repeated behavioral or task adaptation.
- Agents for genuinely dynamic multi-step tool selection.

A RAG flow is:

`ingest -> parse -> chunk -> embed -> index -> retrieve -> rerank -> generate -> cite -> evaluate`

Measure retrieval separately from answer generation. Retrieval metrics include recall@k, precision@k, MRR, and nDCG. Answer measures include correctness, groundedness, completeness, citation validity, abstention quality, latency, and cost.

Treat prompts, user input, retrieved documents, tool arguments, and model outputs according to their trust boundaries. Enforce access control during retrieval, validate tool calls server-side, and require approval for consequential actions.

## 13. MLOps essentials

Version:

- Code and configuration
- Data snapshots and schemas
- Feature definitions
- Model artifacts and dependencies
- Prompts and embedding models
- Evaluation datasets and thresholds

A model registry stores artifact location, lineage, metrics, stage, approvals, and deployment status. Reproducibility requires pinned dependencies, deterministic seeds where possible, environment capture, data lineage, and saved configuration.

### Rollout methods

- Shadow: new model observes live traffic without affecting responses.
- Canary: small traffic percentage receives the new model.
- Blue-green: two complete environments support rapid switching.
- A/B test: randomized groups measure causal product impact.

Always define rollback conditions and retain the previous safe artifact.

## 14. Drift and monitoring

- Data drift: input distribution changes.
- Label drift: target prevalence changes.
- Concept drift: relationship between inputs and target changes.
- Training-serving skew: production features differ from training features.

Monitor:

- Schema, missing values, ranges, categories, and freshness
- Feature distributions by important slice
- Prediction distribution and confidence
- Delayed outcome metrics when labels arrive
- Calibration and decision thresholds
- Latency, throughput, saturation, and error rate
- Business outcome, cost, fairness, and safety

Drift does not automatically require retraining. Confirm performance impact, investigate pipeline defects, and compare against a stable baseline.

## 15. System-design checklist

### Requirements

- Who uses the prediction and what decision follows?
- Batch, online, streaming, or edge?
- QPS, payload size, latency percentiles, freshness, and availability?
- False-positive and false-negative costs?
- Privacy, security, fairness, explainability, and budget?

### Data and modelling

- Sources, ownership, lineage, labels, delay, and retention
- Leakage and feedback-loop risks
- Baseline and model selection trade-offs
- Offline metrics aligned with business value
- Slice-based evaluation and uncertainty

### Serving

- Preprocessing parity and feature freshness
- Online store, cache, model server, and autoscaling
- Timeouts, bounded retries, circuit breaker, and load shedding
- Fallback model, rules, cached response, or graceful refusal
- Idempotency for retryable writes

### Operations

- Tracing, logs, metrics, dashboards, and alerts
- Shadow or canary release and rollback
- Model, feature, prompt, and data lineage
- Retraining triggers and approval gates
- Capacity, accelerator utilization, and cost per successful task

## 16. Production diagnosis map

### Quality dropped suddenly

Check recent deployments, feature freshness, schema changes, missing values, category mappings, model version, label pipeline, and upstream incidents. Compare affected slices and replay known requests against the previous model.

### Quality declined gradually

Investigate drift, seasonality, changing user behavior, feedback loops, stale training data, and calibration decay. Compare performance by cohort and time.

### Latency increased

Break down network, preprocessing, feature lookup, queue, inference, postprocessing, and downstream calls. Inspect p95/p99, saturation, batch behavior, payload size, cache hit rate, and retries.

### Predictions differ between offline and online

Check feature code parity, timestamps, defaults, encoders, scaler versions, numerical precision, request transformations, model artifacts, and nondeterminism.

### Cost increased

Check traffic, input/output size, model routing, cache hit rate, retries, unused context, underfilled batches, idle accelerators, and expensive fallback paths.

### Safe incident response

Reduce impact first through rollback, fallback, throttling, or feature disabling. Preserve evidence. Identify the root cause after stabilization, then add a regression test, monitoring signal, owner, and prevention action.

## 17. Python coding reminders

- State time and space complexity.
- Avoid mutable default arguments.
- Use generators for lazy large sequences.
- Prefer sets for average `O(1)` membership.
- Use dictionaries for counting and indexing.
- Use `collections.Counter`, `defaultdict`, and `deque` appropriately.
- Handle empty input, duplicates, negatives, nulls, overflow, and invalid types.
- Keep preprocessing pure and testable.
- Vectorize only when it improves clarity and measured performance.
- Set random seeds but understand that some accelerator operations remain nondeterministic.

Common patterns:

- Two pointers: sorted pairs, partitions, intervals.
- Sliding window: contiguous substring or subarray conditions.
- Hash map: frequency, lookup, deduplication.
- Heap: top-k and streaming extrema.
- BFS/DFS: graphs, trees, connected components.
- Dynamic programming: repeated overlapping subproblems.
- Binary search: monotonic search spaces, not only sorted arrays.

## 18. SQL reminders

Know joins, grouping, conditional aggregation, subqueries, common table expressions, window functions, null handling, date arithmetic, and deduplication.

- `ROW_NUMBER`: unique sequence within a partition.
- `RANK`: ties share rank and leave gaps.
- `DENSE_RANK`: ties share rank without gaps.
- `LAG/LEAD`: previous or next row without self-join.
- Aggregate after ensuring join cardinality will not duplicate facts.
- `COUNT(*)` counts rows; `COUNT(column)` excludes nulls.
- Comparisons with null use `IS NULL`, not `= NULL`.
- Filter groups with `HAVING`, rows with `WHERE`.

For ML datasets, verify point-in-time correctness, entity grain, label windows, duplicates, late events, and train-production query parity.

## 19. Project explanation template

### One-minute version

“The goal was to improve `[business outcome]` for `[users]`. I owned `[specific scope]`. We used `[data and approach]` because `[trade-off]`. The system operated at `[scale/latency]`. I solved `[important challenge]` by `[action]`, improving `[metric]` from `[before]` to `[after]`. We monitored `[signals]` and retained `[fallback]`.”

### Details to prepare

- Why the problem mattered
- Your exact contribution versus the team's work
- Dataset size, freshness, labels, imbalance, and leakage controls
- Baseline and rejected alternatives
- Metric selection and threshold decision
- Architecture, scale, latency, and cost
- Deployment and rollback method
- Failure encountered and root cause
- Quantified outcome and remaining limitation
- What you would change with more time

Never invent precision. Use honest ranges or explain when a number was not measured.

## 20. Professional scenario patterns

### Disagreement

Clarify the shared goal, compare options using evidence and constraints, run a small reversible test, document the decision, and support the chosen direction.

### Missed expectation

State what happened without blame, explain impact, describe immediate correction, identify the process gap, and show how recurrence was prevented.

### Ambiguous requirement

Identify the user decision, propose measurable acceptance criteria, surface assumptions, build the smallest useful baseline, and shorten the feedback loop.

### Competing priorities

Compare business impact, urgency, dependencies, risk, and effort. Communicate trade-offs early and confirm the ordering with accountable stakeholders.

### Technical leadership

Demonstrate decision quality, risk reduction, mentoring, cross-team clarity, and measurable outcomes—not only code volume or title.

## 21. Common traps

- Reporting accuracy for a severely imbalanced target
- Tuning against the final test set
- Randomly splitting time-dependent records
- Fitting preprocessing before splitting
- Claiming correlation proves causation
- Treating a p-value as the probability a hypothesis is true
- Using an unnecessarily complex model without a baseline
- Discussing only offline model metrics, not user value
- Ignoring calibration and threshold selection
- Suggesting retraining as the automatic response to every drift alert
- Omitting fallback, rollback, security, privacy, and cost
- Describing team outcomes without clarifying personal contribution
- Giving a memorized definition without an example or limitation
- Starting to code before confirming inputs and edge cases

## 22. Rapid revision plan

### Seven days before

1. Review foundations, metrics, algorithms, and mathematics.
2. Practise Python, SQL, and one modelling task.
3. Complete two end-to-end architecture designs.
4. Review deep learning, generative systems, and MLOps.
5. Prepare three project stories and three professional scenarios.
6. Run a timed mixed practice session and identify weak areas.
7. Review this sheet, formulas, trade-offs, and concise explanations.

### Final hour

- Revisit metric formulas and error trade-offs.
- Review leakage, bias-variance, regularization, drift, and rollout.
- Recall one strong example for each major algorithm family.
- Review the design and incident-response frameworks.
- Rehearse project impact numbers and personal ownership.
- Slow down, clarify first, and think aloud in a structured way.

## 23. Final quality checklist

- Did I answer the actual question first?
- Did I state assumptions and constraints?
- Did I give an intuitive explanation and a concrete example?
- Did I mention the main limitation or trade-off?
- Did I connect the metric to business cost?
- Did I prevent leakage and evaluate relevant slices?
- Did I cover rollout, monitoring, fallback, safety, and cost?
- Did I distinguish my contribution from the team's contribution?
- Did I quantify results honestly?
- Did I keep the response structured and concise?
