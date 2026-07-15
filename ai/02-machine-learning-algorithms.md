# Machine-Learning Algorithms

## Linear and logistic regression

Linear regression models a continuous target as a weighted sum. Its common assumptions are linearity, independent errors, constant error variance, and limited multicollinearity. Logistic regression models log-odds and produces probabilities through the sigmoid function. Both are strong interpretable baselines.

## Decision trees

A tree recursively selects splits that reduce impurity. Classification commonly uses Gini impurity or entropy; regression uses squared error. Trees capture non-linear interactions but overfit unless depth, leaf size, or pruning is controlled.

## Random forest versus gradient boosting

Random forest trains many independent trees on bootstrapped samples and averages them, primarily reducing variance. Gradient boosting trains trees sequentially to correct residual error, often achieving higher tabular accuracy but requiring more careful tuning. Boosting parameters include learning rate, number of estimators, tree depth, subsampling, and column sampling.

## Support vector machines

An SVM maximizes the margin between classes. The kernel trick permits non-linear boundaries without explicitly constructing the transformed feature space. SVMs work well on medium-sized, high-dimensional data but scale poorly with sample count and require feature scaling.

## K-nearest neighbors

KNN predicts from nearby training points. Small `k` has lower bias and higher variance. Distance scaling, dimensionality, and inference cost are major concerns. Approximate nearest-neighbor indexes help at scale.

## Clustering

- **K-means:** fast and simple; assumes roughly spherical clusters and requires `k`.
- **DBSCAN:** identifies arbitrary shapes and noise; struggles with varying densities.
- **Hierarchical clustering:** produces a dendrogram but can be expensive.
- **Gaussian mixtures:** provide soft probabilistic membership with elliptical clusters.

Evaluate with silhouette score, stability, and—most importantly—usefulness to the downstream task.

## Dimensionality reduction

PCA finds orthogonal directions of maximum variance and supports compression or de-noising. t-SNE and UMAP are mainly visualization tools; distances and clusters in their plots should not automatically be treated as proof of true groups.

## Handling imbalance

Use stratified splits, class weights, threshold tuning, focal loss, under/oversampling, or carefully applied SMOTE. Never oversample before the split. Evaluate using per-class metrics, precision-recall curves, and costs at the operating threshold.

## Hyperparameter optimization

Random search is stronger than grid search when only a few dimensions matter. Bayesian optimization uses previous trials to choose promising configurations. Successive halving and pruning stop weak trials early. Track the search space, random seeds, data version, metric, and compute budget.

## Calibration and thresholding

A calibrated 0.8 prediction should be correct about 80% of the time. Check reliability diagrams and Brier score. Platt scaling and isotonic regression can calibrate held-out predictions. Pick a decision threshold from operational cost or capacity, not automatically 0.5.

## Explainability

Global feature importance describes overall behavior; local explanations describe a prediction. Permutation importance measures performance loss after shuffling a feature. SHAP provides additive attributions but may be slow and can be misleading with correlated features. Explanations do not prove causality.

