# 52. Minimum Window Substring

Platform: LeetCode  
Collection: Blind 75  
Implementation language: Java

## Complete Problem Statement

Given strings s and t, return the shortest substring of s containing every character of t with at least the required multiplicity.

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

### Brute-Force Java — O(n² × |charset|) time

```java
import java.util.*;

public class Solution {

    public String minWindow(String s, String t) {
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            for (int j = i; j < s.length(); j++) {
                String sub = s.substring(i, j + 1);
                if (
                    contains(sub, t) &&
                    (result.isEmpty() || sub.length() < result.length())
                ) {
                    result = sub;
                }
            }
        }
        return result;
    }

    private boolean contains(String window, String t) {
        int[] count = new int[128];
        for (char c : t.toCharArray()) {
            count[c]++;
        }
        for (char c : window.toCharArray()) {
            count[c]--;
        }
        for (int c : count) {
            if (c > 0) {
                return false;
            }
        }
        return true;
    }
}
```

## Approach 2: Optimized

The optimized solution removes repeated work while preserving the same correctness condition.

### Optimized Java (Sliding Window) — O(n) time, O(|charset|) space

```java
import java.util.*;

public class Solution {

    public String minWindow(String s, String t) {
        int[] need = new int[128];
        int have = 0,
            required = 0;
        for (char c : t.toCharArray()) {
            if (need[c]++ == 0) {
                required++;
            }
        }
        int left = 0,
            minLen = Integer.MAX_VALUE,
            start = 0;
        int[] window = new int[128];
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (++window[c] == need[c]) {
                have++;
            }
            while (have == required) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    start = left;
                }
                if (--window[s.charAt(left)] < need[s.charAt(left)]) {
                    have--;
                }
                left++;
            }
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
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
copying it. The source heading for this implementation is “Minimum Window Substring”.
