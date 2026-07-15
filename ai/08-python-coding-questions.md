# Python Coding Questions and Answers

## 1. Flatten a nested list

```python
def flatten(values):
    result = []
    for value in values:
        if isinstance(value, list):
            result.extend(flatten(value))
        else:
            result.append(value)
    return result


assert flatten([1, [2, [3, 4]], 5]) == [1, 2, 3, 4, 5]
```

Time is `O(n)` over all elements; recursion depth equals nesting depth.

## 2. Return the top-k frequent items

```python
from collections import Counter


def top_k_frequent(items, k):
    if k < 0:
        raise ValueError("k must be non-negative")
    return [item for item, _ in Counter(items).most_common(k)]


assert top_k_frequent(["a", "b", "a", "c", "b", "a"], 2) == ["a", "b"]
```

Counting is `O(n)`; selecting with a heap is approximately `O(m log k)` for `m` distinct items.

## 3. Implement a fixed-size moving average

```python
from collections import deque


class MovingAverage:
    def __init__(self, window_size):
        if window_size <= 0:
            raise ValueError("window_size must be positive")
        self.values = deque()
        self.window_size = window_size
        self.total = 0.0

    def add(self, value):
        self.values.append(value)
        self.total += value
        if len(self.values) > self.window_size:
            self.total -= self.values.popleft()
        return self.total / len(self.values)


avg = MovingAverage(3)
assert avg.add(3) == 3
assert avg.add(6) == 4.5
assert avg.add(9) == 6
assert avg.add(12) == 9
```

Each update is `O(1)` time and the object uses `O(window_size)` space.

## 4. Build a batch generator

```python
def batches(items, batch_size):
    if batch_size <= 0:
        raise ValueError("batch_size must be positive")
    batch = []
    for item in items:
        batch.append(item)
        if len(batch) == batch_size:
            yield batch
            batch = []
    if batch:
        yield batch


assert list(batches(range(5), 2)) == [[0, 1], [2, 3], [4]]
```

The generator supports streaming iterables without loading the entire input.

## 5. Implement an LRU cache

```python
from collections import OrderedDict


class LRUCache:
    def __init__(self, capacity):
        if capacity <= 0:
            raise ValueError("capacity must be positive")
        self.capacity = capacity
        self.data = OrderedDict()

    def get(self, key, default=None):
        if key not in self.data:
            return default
        self.data.move_to_end(key)
        return self.data[key]

    def put(self, key, value):
        if key in self.data:
            self.data.move_to_end(key)
        self.data[key] = value
        if len(self.data) > self.capacity:
            self.data.popitem(last=False)


cache = LRUCache(2)
cache.put("a", 1)
cache.put("b", 2)
assert cache.get("a") == 1
cache.put("c", 3)
assert cache.get("b") is None
```

Average `get` and `put` time is `O(1)`.

## 6. Retry a transient operation with exponential backoff

```python
import time


def retry(operation, attempts=3, base_delay=0.1, retry_on=(Exception,)):
    if attempts <= 0:
        raise ValueError("attempts must be positive")
    for attempt in range(attempts):
        try:
            return operation()
        except retry_on:
            if attempt == attempts - 1:
                raise
            time.sleep(base_delay * (2 ** attempt))
```

In production, add jitter, retry only transient failures, cap delay, and ensure the operation is idempotent.

## 7. Find duplicate records by composite key

```python
from collections import defaultdict


def duplicate_groups(records, key_fields):
    groups = defaultdict(list)
    for record in records:
        key = tuple(record[field] for field in key_fields)
        groups[key].append(record)
    return {key: rows for key, rows in groups.items() if len(rows) > 1}
```

Time and space are both `O(n)` for fixed-size keys.

