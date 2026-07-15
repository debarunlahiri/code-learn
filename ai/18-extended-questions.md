# Extended Questions and Detailed Answers

## Why can accuracy be misleading?

Accuracy gives every correct prediction equal value and ignores error type. Suppose only 1% of payments are fraudulent. A classifier that always predicts legitimate achieves 99% accuracy but catches no fraud. The confusion matrix reveals this failure. Recall answers how much fraud is detected, precision answers how many alerts are truly fraud, and the operating threshold determines their trade-off.

Accuracy can still be useful when classes are balanced, error costs are similar, and class distribution is stable. The lesson is not that accuracy is always bad; it is that the metric must reflect the decision.

## What is the practical difference between parameters and hyperparameters?

Parameters are learned during training, such as regression coefficients or neural-network weights. Hyperparameters control the learning process or model structure, such as tree depth, regularization strength, batch size, and learning rate.

Hyperparameters are selected using validation data. Repeatedly choosing them based on test performance leaks information from the test set and makes the final estimate optimistic.

## Why does feature scaling matter?

Distance-based algorithms give larger numeric ranges more influence. Gradient descent also converges more smoothly when dimensions have similar scale. KNN, SVM, PCA, logistic regression with regularization, and neural networks usually benefit. Decision trees compare one feature at a time by thresholds, so monotonic scaling rarely changes their split choices.

Fit the scaler on training data only. Otherwise validation statistics influence training.

## What is multicollinearity?

Multicollinearity means predictor variables contain strongly overlapping linear information. Predictions may remain good, but individual coefficients can become unstable and hard to interpret. Detect it through correlation analysis, variance inflation factors, coefficient instability, and domain understanding.

Possible responses include removing redundant variables, combining them, applying regularization, or using dimensionality reduction. If prediction rather than coefficient interpretation is the goal, the issue may be less severe.

## Why do ensembles work?

Combining models can reduce error when their mistakes are not perfectly correlated. Bagging averages unstable learners to reduce variance. Boosting builds a sequence in which later learners focus on earlier errors. Stacking trains a meta-model on out-of-fold predictions from several base models.

Stacking must use out-of-fold predictions; training the meta-model on in-sample base predictions causes leakage. An ensemble adds latency, memory, and operational complexity, so its gain should justify those costs.

## What is calibration and why does it matter?

Calibration measures whether predicted probabilities match observed frequencies. Among cases scored near 0.7, roughly 70% should be positive for a well-calibrated model. Calibration matters when probabilities drive pricing, capacity planning, risk, or threshold policies.

A model can rank very well but be poorly calibrated. Evaluate with reliability plots and Brier score. Fit Platt scaling or isotonic regression on held-out data and recheck it after distribution shift.

## What is data drift versus concept drift?

Data drift means the input distribution `P(X)` changes. Concept drift means the mapping `P(Y|X)` changes. For example, customer age distribution changing is data drift; the same behavior becoming less predictive of churn is concept drift.

Input drift can be detected without labels, but concept drift generally requires outcomes or trusted proxies. Not all input drift hurts performance, and performance can decline without a large marginal feature shift due to relationship changes.

## What causes training-serving skew?

Common causes are different transformation code, different data sources, inconsistent time windows, category mappings, missing-value behavior, feature ordering, and stale online data. Prevent it with shared transformation definitions, point-in-time joins, schema contracts, parity tests on known samples, and logging of live feature values or hashes.

## How should class imbalance be handled?

First define costs and choose representative splits. Establish a baseline with class weights and threshold selection. Resampling can help, but it changes the training distribution. Synthetic oversampling should occur only inside each training fold and may create unrealistic examples.

Evaluate precision-recall curves, per-class metrics, calibration, capacity at the selected threshold, and relevant slices. Extreme imbalance may be better handled as anomaly detection or a two-stage retrieval-and-ranking problem.

## How does gradient boosting learn?

Gradient boosting starts with a simple prediction. Each new weak learner approximates the negative gradient of the loss—the direction that reduces error. The learning rate shrinks every learner's contribution, and multiple small trees build a strong non-linear model.

More trees and depth increase capacity. Subsampling, column sampling, regularization, shallow trees, and early stopping control overfitting. A smaller learning rate commonly needs more trees.

## Why are transformers effective for sequences?

Self-attention lets every token directly combine information from other tokens, creating short paths for long-range dependencies and enabling parallel training. Positional information is added because attention alone does not encode order.

Standard attention has quadratic time and memory in sequence length, which makes long contexts expensive. Sparse attention, chunking, recurrence, retrieval, and optimized kernels address different parts of this limitation.

## What does temperature do in language generation?

Temperature scales logits before sampling. Lower temperature makes the distribution sharper and outputs more deterministic. Higher temperature increases diversity and risk. At temperature zero, many systems use greedy selection, though exact reproducibility can still depend on infrastructure and implementation.

Temperature does not repair weak evidence or an unclear prompt. Grounding, validation, and evaluation remain necessary.

## What is chunking in RAG?

Chunking divides source content into retrieval units. Chunks that are too small lose context; chunks that are too large dilute relevance and consume context-window space. Prefer structure-aware boundaries such as headings, paragraphs, tables, and code blocks, with limited overlap when needed.

Store source, heading path, page, timestamp, access-control metadata, and content version. Evaluate chunking by retrieval success on real questions rather than selecting a size only by token count.

## Hybrid search versus vector search?

Vector search retrieves semantically similar content and handles paraphrases. Keyword search is strong for exact names, identifiers, error codes, and rare terms. Hybrid search combines both, often normalizing or reranking their results.

Use a labelled query set to tune weights. A reranker can apply a more expensive relevance model to the combined candidate list.

## What is quantization?

Quantization represents weights or activations with lower precision, such as 8-bit integers instead of 32-bit floats. It reduces memory bandwidth, storage, and sometimes latency. Post-training quantization is easy but may reduce accuracy. Quantization-aware training simulates lower precision during training and often preserves more quality.

Benchmark on actual hardware because theoretical compression does not guarantee speedup if the runtime lacks optimized kernels.

## How do batch and online features remain consistent?

Define each feature once with event-time semantics. Offline training queries reconstruct values as they existed at each historical prediction time. Streaming or online computation uses equivalent windows and late-event rules. Version definitions and run parity tests using sampled events through both paths.

A feature store can centralize these definitions but cannot correct an ambiguous timestamp or label by itself.

## When is a rule-based system preferable?

Rules are suitable when logic is stable, explicit, legally required, low-dimensional, or must be perfectly traceable. They are also useful as a baseline and safety layer. ML is useful when patterns are numerous, noisy, and difficult to enumerate.

Hybrid systems often work best: mandatory rules enforce hard constraints while a model handles uncertain ranking or prediction.

## How do you estimate serving capacity?

Measure realistic request latency, batch size, and resource utilization. Little's Law gives average concurrency as arrival rate multiplied by average time in system. Then account for p95/p99 latency, bursts, redundancy, autoscaling delay, dependency limits, and maintenance.

Load test the complete path. Model-only benchmarks ignore serialization, feature retrieval, networking, and queueing.

## What makes a model explainable?

Interpretability can mean transparent structure, global behavior, local reasons, example-based evidence, counterfactuals, or traceability of data and version. Choose the form based on the user need. A developer debugging drift needs different evidence from a customer receiving an adverse decision.

Feature attribution describes association within the model, not causal influence. Correlated features can divide or reassign attribution unpredictably.

## How do you evaluate a generative system?

Define tasks and failure categories, then build a versioned dataset with normal, hard, adversarial, and out-of-domain cases. Measure retrieval recall, groundedness, task correctness, citation accuracy, safety, latency, and cost. Use deterministic checks where possible, human rubrics for nuanced quality, and calibrated model-based graders for scale.

Track results by slice and inspect disagreements among evaluators. A single average score hides regressions.

## How do you decide between prompting, RAG, and fine-tuning?

Start with prompting for task instructions and format. Add RAG when answers need private, changing, or citeable knowledge. Fine-tune when many examples are needed to reliably shape behavior, terminology, or output style, and prompting becomes too long or inconsistent.

Fine-tuning is not a reliable database of current facts. RAG does not automatically teach a model a new behavior. Evaluate combinations against quality, latency, maintenance, privacy, and cost.
