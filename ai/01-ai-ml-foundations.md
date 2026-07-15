# AI and ML Foundations

## Supervised, unsupervised, and reinforcement learning

- **Supervised learning:** learns a mapping from features to labelled targets. Examples: fraud classification and demand regression.
- **Unsupervised learning:** discovers structure without labelled targets. Examples: clustering and anomaly detection.
- **Reinforcement learning:** an agent learns a policy by receiving rewards from an environment.

## Bias and variance

Bias is error caused by restrictive assumptions; variance is sensitivity to training data. Underfitting usually has high bias. Overfitting usually has high variance. Control the trade-off with suitable model complexity, regularization, more representative data, cross-validation, and early stopping.

## Train, validation, and test sets

- Train parameters on the training set.
- Select features, thresholds, and hyperparameters on the validation set.
- Estimate final generalization once on the test set.
- Split time-dependent data chronologically.
- Split grouped entities, such as patients or customers, without placing one entity in multiple sets.

## Data leakage

Leakage occurs when training uses information unavailable at prediction time. Common causes include fitting preprocessing before splitting, using future aggregates, target-derived features, and duplicate entities across splits. Put preprocessing inside the cross-validation pipeline and verify feature availability timestamps.

## Classification metrics

From true positives (TP), false positives (FP), true negatives (TN), and false negatives (FN):

- Precision = TP / (TP + FP)
- Recall = TP / (TP + FN)
- F1 = 2 × precision × recall / (precision + recall)
- Specificity = TN / (TN + FP)

Accuracy is useful when classes and error costs are balanced. Prefer precision-recall AUC for rare positive classes. ROC-AUC measures ranking across thresholds but can look optimistic on highly imbalanced data.

## Regression metrics

- **MAE:** robust and directly interpretable in target units.
- **MSE/RMSE:** penalizes large errors more strongly.
- **R-squared:** fraction of variance explained relative to predicting the mean.
- **MAPE:** intuitive percentage error but unstable around zero.

Select a metric that reflects business cost. If underprediction costs twice as much, an asymmetric loss may be more suitable than RMSE.

## Cross-validation

K-fold cross-validation estimates generalization by rotating validation folds. Use stratified folds for class balance, group folds for entity isolation, and walk-forward validation for time series. Nested cross-validation reduces bias when both tuning and performance estimation use limited data.

## Regularization

- L1 adds the absolute value of weights and can produce sparse coefficients.
- L2 adds squared weights and smoothly shrinks them.
- Dropout, data augmentation, and early stopping regularize neural networks.

## Feature engineering

Useful techniques include log transforms for skewed values, cyclical encoding for time, interaction features, frequency encoding, target encoding with leakage-safe folds, missingness indicators, and domain aggregates. Tree models rarely need scaling; distance-based and gradient-based models usually do.

## Probability and statistics essentials

- Bayes theorem: `P(A|B) = P(B|A)P(A)/P(B)`.
- A confidence interval describes uncertainty in an estimated parameter under repeated sampling.
- A p-value is the probability of data at least as extreme under the null hypothesis; it is not the probability that the null is true.
- Statistical significance does not guarantee practical significance.
- Correlation does not establish causation because confounding, selection, and reverse causality may exist.

