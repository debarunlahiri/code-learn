# 19. Longest Common Subsequence

Platform: LeetCode  
Collection: Blind 75  
Implementation language: Java

## Complete Problem Statement

Given two strings, return the length of their longest common subsequence while preserving the order of characters in both strings.

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

### Brute-Force Java (Recursion) — O(2^(m+n)) time

```java
import java.util.*;

public class Solution {

    public int longestCommonSubsequence(String text1, String text2) {
        return lcs(text1, text2, 0, 0);
    }

    private int lcs(String s1, String s2, int i, int j) {
        if (i == s1.length() || j == s2.length()) {
            return 0;
        }
        if (s1.charAt(i) == s2.charAt(j)) {
            return 1 + lcs(s1, s2, i + 1, j + 1);
        }
        return Math.max(lcs(s1, s2, i + 1, j), lcs(s1, s2, i, j + 1));
    }
}
```

## Approach 2: Optimized

The optimized solution removes repeated work while preserving the same correctness condition.

### Optimized Java (Bottom-Up DP) — O(m×n) time, O(m×n) space

```java
import java.util.*;

public class Solution {

    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(),
            n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
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
copying it. The source heading for this implementation is “Longest Common Subsequence”.
