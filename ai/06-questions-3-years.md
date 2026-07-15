# Question Bank: Around 3 Years of Experience

## 1. How do you prevent leakage in a preprocessing pipeline?

Split first, then fit imputers, scalers, encoders, and feature selection only on training data. Put all steps in a pipeline so each cross-validation fold learns transformations independently. For temporal data, confirm that every feature existed at the prediction timestamp.

## 2. Why might F1 be better than accuracy?

With a rare positive class, a model can have high accuracy by predicting only the majority class. F1 balances precision and recall. It is suitable when both false positives and false negatives matter, although an explicit cost function is better when their costs differ.

## 3. Bagging versus boosting?

Bagging trains models largely independently and averages them to reduce variance. Boosting trains sequentially, focusing on residual errors, and primarily reduces bias while also controlling variance through regularization.

## 4. How do you handle missing values?

Understand why values are missing, inspect missingness by target and slice, and establish a baseline. Options include constant/median imputation, model-native handling, a missing indicator, or discarding a feature when justified. Fit imputation only on training data.

## 5. What would you do when training performance is good but validation performance is poor?

Check leakage and split correctness first. Then reduce complexity, add regularization, gather representative data, remove unstable features, use augmentation, or stop training earlier. Compare learning curves to decide whether more data is likely to help.

## 6. How do you choose a classification threshold?

Convert false positives and false negatives into cost or operational capacity. Evaluate the precision-recall trade-off on a validation set, choose the threshold that satisfies constraints, and recheck calibration and slice behavior in production.

## 7. Why put preprocessing and a model in one pipeline?

It prevents inconsistent transformations and leakage, simplifies cross-validation, preserves feature order, and packages the exact inference path into one versioned artifact.

## 8. When would you choose a tree model over a neural network?

For moderate tabular data, mixed feature types, limited compute, and a need for a strong explainable baseline. Neural networks are more attractive for large unstructured datasets or representation learning.

## 9. How do you expose a model as a service?

Serialize a versioned artifact, build a typed request/response API, reproduce preprocessing, validate inputs, add health checks and structured logs, containerize it, load test it, and deploy gradually with monitoring and rollback.

## 10. What do you monitor after release?

Latency, errors, throughput, resource saturation, schema violations, missing values, feature and prediction drift, delayed performance, calibration, slice behavior, and business outcomes.

## 11. What is an embedding?

It is a learned dense vector representing semantic or behavioural similarity. Embeddings support retrieval, clustering, recommendation, and downstream features. Their usefulness must be measured on the actual task.

## 12. How do you improve a weak RAG system?

Label a small evaluation set and separate retrieval failures from generation failures. Improve parsing, chunk boundaries, metadata filters, hybrid retrieval, query rewriting, top-k, and reranking before increasing prompt or model complexity.

## 13. Describe a difficult project issue.

Use situation, responsibility, action, and measurable result. Include the diagnostic evidence, trade-off chosen, collaborators involved, and what you changed to prevent recurrence.

## 14. How do you ensure reproducibility?

Version code, datasets, configuration, features, dependencies, seeds, and artifacts. Record lineage and metrics in an experiment tracker, and rebuild the environment in a container or locked dependency setup.

## 15. Batch or online inference?

Choose batch when predictions can be prepared ahead, throughput matters, and some staleness is acceptable. Choose online when each request needs fresh context and strict response time. A hybrid often precomputes expensive features and applies a small online scorer.

