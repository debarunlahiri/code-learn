# ML Coding Questions and Answers

Examples use NumPy, pandas, and scikit-learn.

## 1. Create a leakage-safe classification pipeline

```python
from sklearn.compose import ColumnTransformer
from sklearn.impute import SimpleImputer
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import OneHotEncoder, StandardScaler

numeric = ["age", "income"]
categorical = ["country", "device"]

preprocessor = ColumnTransformer([
    ("num", Pipeline([
        ("impute", SimpleImputer(strategy="median")),
        ("scale", StandardScaler()),
    ]), numeric),
    ("cat", Pipeline([
        ("impute", SimpleImputer(strategy="most_frequent")),
        ("encode", OneHotEncoder(handle_unknown="ignore")),
    ]), categorical),
])

model = Pipeline([
    ("preprocess", preprocessor),
    ("classifier", LogisticRegression(class_weight="balanced", max_iter=1000)),
])

# model.fit(X_train, y_train)
```

The transformations are learned only from data passed to `fit`, so the pipeline works safely inside cross-validation.

## 2. Implement binary precision, recall, and F1

```python
def binary_metrics(y_true, y_pred):
    if len(y_true) != len(y_pred):
        raise ValueError("inputs must have equal length")

    tp = sum(t == 1 and p == 1 for t, p in zip(y_true, y_pred))
    fp = sum(t == 0 and p == 1 for t, p in zip(y_true, y_pred))
    fn = sum(t == 1 and p == 0 for t, p in zip(y_true, y_pred))

    precision = tp / (tp + fp) if tp + fp else 0.0
    recall = tp / (tp + fn) if tp + fn else 0.0
    f1 = 2 * precision * recall / (precision + recall) if precision + recall else 0.0
    return {"precision": precision, "recall": recall, "f1": f1}


assert binary_metrics([1, 1, 0, 0], [1, 0, 1, 0]) == {
    "precision": 0.5, "recall": 0.5, "f1": 0.5
}
```

## 3. Implement standardization without leakage

```python
import numpy as np


class Standardizer:
    def fit(self, X):
        X = np.asarray(X, dtype=float)
        self.mean_ = X.mean(axis=0)
        self.scale_ = X.std(axis=0)
        self.scale_[self.scale_ == 0] = 1.0
        return self

    def transform(self, X):
        if not hasattr(self, "mean_"):
            raise RuntimeError("fit must be called first")
        return (np.asarray(X, dtype=float) - self.mean_) / self.scale_

    def fit_transform(self, X):
        return self.fit(X).transform(X)
```

Call `fit` only on training data and `transform` on validation, test, and live data.

## 4. Implement mini-batch gradient descent for linear regression

```python
import numpy as np


def fit_linear_regression(X, y, learning_rate=0.01, epochs=500, batch_size=32, seed=7):
    X = np.asarray(X, dtype=float)
    y = np.asarray(y, dtype=float)
    X = np.c_[np.ones(len(X)), X]
    weights = np.zeros(X.shape[1])
    rng = np.random.default_rng(seed)

    for _ in range(epochs):
        order = rng.permutation(len(X))
        for start in range(0, len(X), batch_size):
            index = order[start:start + batch_size]
            xb, yb = X[index], y[index]
            error = xb @ weights - yb
            gradient = (2 / len(xb)) * xb.T @ error
            weights -= learning_rate * gradient
    return weights
```

The first weight is the intercept. In real training, monitor validation loss and scale features.

## 5. Compute cosine similarity and return nearest vectors

```python
import numpy as np


def nearest(query, matrix, k=5):
    query = np.asarray(query, dtype=float)
    matrix = np.asarray(matrix, dtype=float)
    if k < 1 or k > len(matrix):
        raise ValueError("k is outside the matrix size")

    query_norm = np.linalg.norm(query)
    row_norms = np.linalg.norm(matrix, axis=1)
    if query_norm == 0 or np.any(row_norms == 0):
        raise ValueError("zero vectors have undefined cosine similarity")

    scores = matrix @ query / (row_norms * query_norm)
    indices = np.argpartition(scores, -k)[-k:]
    indices = indices[np.argsort(scores[indices])[::-1]]
    return [(int(i), float(scores[i])) for i in indices]
```

This exact search is `O(nd)`. Large systems use approximate indexes and validate recall against exact search.

## 6. Produce time-safe lag features

```python
import pandas as pd


def add_lag_features(frame, entity_col, time_col, value_col, lags=(1, 7)):
    result = frame.sort_values([entity_col, time_col]).copy()
    grouped = result.groupby(entity_col, sort=False)[value_col]
    for lag in lags:
        result[f"{value_col}_lag_{lag}"] = grouped.shift(lag)
    return result
```

`shift` ensures a row does not use its own or future target value. Production pipelines must also account for late-arriving events.

## 7. Select a threshold under a minimum recall constraint

```python
import numpy as np
from sklearn.metrics import precision_recall_curve


def threshold_for_recall(y_true, probability, minimum_recall):
    precision, recall, thresholds = precision_recall_curve(y_true, probability)
    candidates = [
        (p, r, t)
        for p, r, t in zip(precision[:-1], recall[:-1], thresholds)
        if r >= minimum_recall
    ]
    if not candidates:
        raise ValueError("recall constraint cannot be satisfied")
    best_precision, achieved_recall, threshold = max(candidates, key=lambda row: row[0])
    return {
        "threshold": float(threshold),
        "precision": float(best_precision),
        "recall": float(achieved_recall),
    }
```

Choose on validation data and confirm capacity, calibration, and slice metrics before release.

