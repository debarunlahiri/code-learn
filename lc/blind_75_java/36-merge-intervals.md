# 36. Merge Intervals

Platform: LeetCode  
Collection: Blind 75  
Implementation language: Java

## Complete Problem Statement

Given a collection of intervals, merge every overlapping group and return the resulting non-overlapping intervals.

Your solution must return the requested value or data structure; printing alone
does not solve the problem. Treat the input as read-only unless the statement
explicitly requires an in-place transformation. A correct solution must handle
the smallest valid input, the largest valid input, duplicate or negative values
when the problem permits them, and cases where the answer occurs at an input
boundary.

The central challenge is not only to produce a correct result, but to recognize
and remove repeated work. Begin with the direct exhaustive method because it
makes the correctness condition visible. Then compare it with the optimized
method and identify the invariant, cached state, ordering property, or data
structure that prevents the same work from being performed again.

## Requirements and Edge Cases

- Do not silently assume extra ordering or uniqueness beyond what the statement
  guarantees.
- Preserve the required output order when the problem defines one.
- Consider empty intermediate results, single-element structures, and answers
  found at the first or last legal position.
- Avoid integer overflow where a sum, product, distance, or boundary can exceed
  the input element range.
- The Java methods below focus on the algorithm and use conventional LeetCode
  model classes such as `ListNode`, `TreeNode`, or `Node` where needed.

## Approach 1: Brute Force

Start from the definition of a valid answer and enumerate the possible choices
directly. This approach is intentionally straightforward: it is useful for
understanding the search space, checking small examples by hand, and serving as
a correctness oracle for randomized tests. Its weakness is repeated work, which
becomes too expensive near the upper input limits.

### Brute-Force Java — O(n² log n) time

```java
import java.util.*;

public class Solution {

    public int[][] merge(int[][] intervals) {
        List<int[]> remaining = new ArrayList<>();
        for (int[] interval : intervals) {
            remaining.add(new int[] { interval[0], interval[1] });
        }

        boolean merged;
        do {
            merged = false;

            for (int i = 0; i < remaining.size() && !merged; i++) {
                for (int j = i + 1; j < remaining.size(); j++) {
                    int[] first = remaining.get(i);
                    int[] second = remaining.get(j);

                    if (first[0] <= second[1] && second[0] <= first[1]) {
                        first[0] = Math.min(first[0], second[0]);
                        first[1] = Math.max(first[1], second[1]);
                        remaining.remove(j);
                        merged = true;
                        break;
                    }
                }
            }
        } while (merged);

        return remaining.toArray(new int[remaining.size()][]);
    }
}
```

## Approach 2: Optimized

The optimized solution removes repeated work while preserving the same correctness condition.

### Optimized Java — O(n log n) time, O(n) space

```java
import java.util.*;

public class Solution {

    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        List<int[]> result = new ArrayList<>();
        for (int[] interval : intervals) {
            if (result.isEmpty() || result.get(result.size() - 1)[1] < interval[0]) {
                result.add(interval);
            } else {
                result.get(result.size() - 1)[1] = Math.max(
                    result.get(result.size() - 1)[1],
                    interval[1]
                );
            }
        }
        return result.toArray(new int[0][]);
    }
}
```

## Why the Optimized Approach Is Correct

The optimized method keeps exactly the information required to make the next
decision. At each iteration or recursive call, its maintained state represents
all relevant choices processed so far. Updating that state preserves the
problem's validity condition, while discarded choices cannot produce a better
or different valid answer. When the algorithm terminates, every candidate that
could affect the result has therefore been considered either explicitly or
through the stored summary.

## Final Review

Before moving to the next problem, trace both approaches on a normal case and an
edge case. Explain why the brute-force version repeats work, state the optimized
invariant in one sentence, and reproduce the optimized Java method without
copying it. The source heading for this implementation is “Merge Intervals”.
