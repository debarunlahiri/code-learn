# 24. Decode Ways

Platform: LeetCode  
Collection: Blind 75  
Implementation language: Java

## Complete Problem Statement

Digits map from 1 through 26 to letters A through Z. Return the number of valid ways to decode the complete digit string.

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

### Brute-Force Java (Recursion) — O(2ⁿ) time

```java
public int numDecodings(String s) {
    return decode(s, 0);
}
private int decode(String s, int i) {
    if (i == s.length()) return 1;
    if (s.charAt(i) == '0') return 0;
    int ways = decode(s, i + 1);
    if (i + 1 < s.length()) {
        int two = Integer.parseInt(s.substring(i, i + 2));
        if (two >= 10 && two <= 26) ways += decode(s, i + 2);
    }
    return ways;
}
```

## Approach 2: Optimized

The optimized solution removes repeated work while preserving the same correctness condition.

### Optimized Java (DP) — O(n) time, O(1) space

```java
public int numDecodings(String s) {
    int n = s.length(), dp1 = 1, dp2 = 0;
    if (s.charAt(0) == '0') return 0;
    dp2 = 1;
    for (int i = 1; i < n; i++) {
        int curr = 0;
        if (s.charAt(i) != '0') curr = dp2;
        int two = Integer.parseInt(s.substring(i - 1, i + 1));
        if (two >= 10 && two <= 26) curr += dp1;
        dp1 = dp2; dp2 = curr;
    }
    return dp2;
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
copying it. The source heading for this implementation is “Decode Ways”.
