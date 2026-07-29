# 54. Group Anagrams

Platform: LeetCode  
Collection: Blind 75  
Implementation language: Java

## Complete Problem Statement

Group a list of strings so that strings containing exactly the same character counts appear in the same group.

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

### Brute-Force Java — O(n² × k log k) time

```java
import java.util.*;

public class Solution {

    public List<List<String>> groupAnagrams(String[] strings) {
        boolean[] grouped = new boolean[strings.length];
        List<List<String>> result = new ArrayList<>();

        for (int i = 0; i < strings.length; i++) {
            if (grouped[i]) {
                continue;
            }

            List<String> group = new ArrayList<>();
            group.add(strings[i]);
            grouped[i] = true;

            for (int j = i + 1; j < strings.length; j++) {
                if (!grouped[j] && areAnagrams(strings[i], strings[j])) {
                    group.add(strings[j]);
                    grouped[j] = true;
                }
            }

            result.add(group);
        }

        return result;
    }

    private boolean areAnagrams(String first, String second) {
        if (first.length() != second.length()) {
            return false;
        }

        char[] firstCharacters = first.toCharArray();
        char[] secondCharacters = second.toCharArray();
        Arrays.sort(firstCharacters);
        Arrays.sort(secondCharacters);

        return Arrays.equals(firstCharacters, secondCharacters);
    }
}
```

## Approach 2: Optimized

The optimized solution removes repeated work while preserving the same correctness condition.

### Optimized Java — O(n × k log k) time, O(n×k) space

```java
import java.util.*;

public class Solution {

    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            char[] arr = s.toCharArray();
            Arrays.sort(arr);
            String key = new String(arr);
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }
        return new ArrayList<>(map.values());
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
copying it. The source heading for this implementation is “Group Anagrams”.
