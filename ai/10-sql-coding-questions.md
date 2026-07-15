# SQL Coding Questions and Answers

The examples use broadly portable SQL; date functions may need adjustment for a specific database.

## 1. Find the latest prediction for each entity

```sql
WITH ranked AS (
    SELECT
        entity_id,
        model_version,
        score,
        predicted_at,
        ROW_NUMBER() OVER (
            PARTITION BY entity_id
            ORDER BY predicted_at DESC
        ) AS row_num
    FROM predictions
)
SELECT entity_id, model_version, score, predicted_at
FROM ranked
WHERE row_num = 1;
```

Add a deterministic tie-breaker such as `prediction_id DESC` if timestamps can match.

## 2. Calculate daily precision and recall

```sql
WITH counts AS (
    SELECT
        CAST(predicted_at AS DATE) AS prediction_date,
        SUM(CASE WHEN prediction = 1 AND actual = 1 THEN 1 ELSE 0 END) AS tp,
        SUM(CASE WHEN prediction = 1 AND actual = 0 THEN 1 ELSE 0 END) AS fp,
        SUM(CASE WHEN prediction = 0 AND actual = 1 THEN 1 ELSE 0 END) AS fn
    FROM scored_outcomes
    GROUP BY CAST(predicted_at AS DATE)
)
SELECT
    prediction_date,
    1.0 * tp / NULLIF(tp + fp, 0) AS precision,
    1.0 * tp / NULLIF(tp + fn, 0) AS recall
FROM counts
ORDER BY prediction_date;
```

`NULLIF` avoids division by zero, and `1.0 *` avoids integer division in many engines.

## 3. Detect duplicate events

```sql
SELECT event_id, COUNT(*) AS occurrences
FROM events
GROUP BY event_id
HAVING COUNT(*) > 1;
```

For logical duplicates, group by the actual business key rather than a generated row ID.

## 4. Build a seven-day rolling average

```sql
SELECT
    event_date,
    daily_count,
    AVG(daily_count) OVER (
        ORDER BY event_date
        ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
    ) AS rolling_7_row_average
FROM daily_events
ORDER BY event_date;
```

This is seven rows, not necessarily seven calendar days. Join to a calendar table first when missing dates are possible.

## 5. Compare champion and challenger outcomes

```sql
SELECT
    model_version,
    COUNT(*) AS predictions,
    AVG(CASE WHEN prediction = actual THEN 1.0 ELSE 0.0 END) AS accuracy,
    AVG(latency_ms) AS average_latency_ms
FROM scored_outcomes
WHERE model_version IN ('champion', 'challenger')
GROUP BY model_version;
```

A valid comparison also needs comparable traffic assignment and the task-appropriate quality metric.

## 6. Create point-in-time-correct customer features

```sql
SELECT
    p.prediction_id,
    p.customer_id,
    p.prediction_time,
    COUNT(t.transaction_id) AS prior_transaction_count,
    COALESCE(SUM(t.amount), 0) AS prior_transaction_amount
FROM prediction_events p
LEFT JOIN transactions t
    ON t.customer_id = p.customer_id
   AND t.transaction_time < p.prediction_time
GROUP BY p.prediction_id, p.customer_id, p.prediction_time;
```

The strict timestamp condition prevents future transactions from leaking into historical training rows.

## 7. Find entities whose score changed sharply

```sql
WITH changes AS (
    SELECT
        entity_id,
        predicted_at,
        score,
        LAG(score) OVER (
            PARTITION BY entity_id
            ORDER BY predicted_at
        ) AS previous_score
    FROM predictions
)
SELECT entity_id, predicted_at, score, previous_score
FROM changes
WHERE ABS(score - previous_score) >= 0.40;
```

In a monitoring system, tune the threshold by score calibration and normal volatility.

