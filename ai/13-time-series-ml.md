# Time-Series Machine Learning

Time-series problems require explicit reasoning about time. Random splitting, future-derived features, and revised historical data can all create unrealistically strong results.

## Problem types

- Forecast a continuous value, such as hourly demand.
- Predict an event within a horizon, such as equipment failure in seven days.
- Detect anomalies in a stream.
- Classify an entire sequence, such as an activity from sensor readings.

Define the forecast origin, horizon, frequency, entity level, and allowed information before constructing features.

## Components of a time series

- **Trend:** long-term direction.
- **Seasonality:** repeating pattern at a known period.
- **Cycle:** longer movement without a fixed period.
- **Noise:** unexplained random variation.
- **Level shift:** a persistent change after an event.

Decomposition helps exploration, but the components can interact. Multiplicative seasonality grows with the series level, while additive seasonality remains roughly constant.

## Stationarity

A weakly stationary series has a stable mean, variance, and autocovariance over time. Classical ARIMA-style models often assume stationarity after transformations. Differencing can remove trend, and seasonal differencing can remove repeating patterns. Over-differencing adds noise and makes interpretation harder.

Tree and neural models do not require stationarity in exactly the same way, but non-stationary behavior still affects generalization.

## Baselines

Always compare against simple baselines:

- Last observed value.
- Same period from the previous season.
- Moving average.
- Historical mean by weekday or hour.

Complex models that cannot beat a seasonal naive baseline do not justify operational complexity.

## Time-based validation

Train on the past and validate on the future. Walk-forward validation uses multiple cutoffs:

- Fold 1: train January–March, validate April.
- Fold 2: train January–April, validate May.
- Fold 3: train January–May, validate June.

An expanding window keeps all history. A sliding window retains only recent history and may adapt better to drift. Leave a gap between train and validation when feature windows or delayed labels could cross the boundary.

## Feature engineering

Useful features include:

- Lagged targets and lagged external variables.
- Rolling mean, minimum, maximum, standard deviation, and quantiles.
- Hour, weekday, month, holiday, and pay-cycle indicators.
- Time since last event.
- Known future information such as scheduled promotions.
- Static entity attributes.

Rolling features must exclude the current target. In pandas, compute a group-wise `shift(1)` before rolling. At prediction time, verify that every external variable is genuinely known for the whole horizon.

## Classical models

AR models use past values, MA models use past errors, and ARIMA combines autoregression, differencing, and moving-average errors. SARIMA adds seasonal terms. Exponential smoothing models level, trend, and seasonality with decreasing weights for older observations.

Classical models are interpretable and effective for smaller collections of regular series. They may struggle with many external variables and complex cross-series relationships.

## Machine-learning approaches

Convert forecasting into supervised learning with lag and calendar features. Gradient-boosted trees are strong for many business datasets. A global model trains across multiple related entities and can share statistical strength, while a local model trains separately for each series.

Neural approaches such as temporal convolution, recurrent networks, and transformer-based architectures can learn complex patterns across many series. They require sufficient data, careful scaling, and strong baselines.

## Multi-step forecasting

- **Recursive:** predict one step and feed it back. Simple, but errors accumulate.
- **Direct:** train a separate model for each horizon. More compute, less recursive drift.
- **Multi-output:** predict the complete horizon jointly. Captures relationships across future steps.

The choice depends on horizon length, data volume, compute, and whether the future path must be coherent.

## Metrics

MAE is interpretable and robust. RMSE emphasizes large errors. MAPE breaks near zero and unfairly weights low-volume series. WAPE aggregates absolute error relative to aggregate actual volume. MASE scales error relative to a naive forecast and supports comparison across series.

Report metrics by horizon, region, product class, and volume band. An overall metric can hide severe failures for important segments.

## Prediction intervals

A point forecast alone hides uncertainty. Quantile regression can predict values such as the 10th, 50th, and 90th percentiles. Conformal prediction can produce empirically calibrated intervals under assumptions about exchangeability. Monitor coverage and interval width; wide intervals may be calibrated but operationally useless.

## Production considerations

Handle late data, missing intervals, time zones, daylight-saving changes, revisions, new entities, and discontinued products. Version the exact data cutoff. Monitor forecast bias because consistently underforecasting and overforecasting create different business costs.

