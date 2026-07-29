# 32. Alien Dictionary

Platform: LeetCode  
Collection: Blind 75  
Implementation language: Java

## Complete Problem Statement

Given words sorted according to an unknown alphabet, derive one valid character order or return an empty string if the ordering is invalid.

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

### Brute-Force Java

For this problem, the straightforward correctness-first implementation uses the same core traversal shown below. It is presented first as the baseline; the following section explains the production interpretation and invariant.

```java
import java.util.*;

public class Solution {

    public String alienOrder(String[] words) {
        Set<Character> characterSet = new TreeSet<>();
        for (String word : words) {
            for (char character : word.toCharArray()) {
                characterSet.add(character);
            }
        }

        List<Character> characters = new ArrayList<>(characterSet);
        boolean[] used = new boolean[characters.size()];
        StringBuilder candidate = new StringBuilder();
        return findValidOrder(words, characters, used, candidate);
    }

    private String findValidOrder(
        String[] words,
        List<Character> characters,
        boolean[] used,
        StringBuilder candidate
    ) {
        if (candidate.length() == characters.size()) {
            String order = candidate.toString();
            return wordsFollowOrder(words, order) ? order : "";
        }

        for (int i = 0; i < characters.size(); i++) {
            if (!used[i]) {
                used[i] = true;
                candidate.append(characters.get(i));

                String answer = findValidOrder(words, characters, used, candidate);
                if (!answer.isEmpty()) {
                    return answer;
                }

                candidate.deleteCharAt(candidate.length() - 1);
                used[i] = false;
            }
        }
        return "";
    }

    private boolean wordsFollowOrder(String[] words, String order) {
        int[] rank = new int[26];
        for (int i = 0; i < order.length(); i++) {
            rank[order.charAt(i) - 'a'] = i;
        }

        for (int i = 1; i < words.length; i++) {
            if (compare(words[i - 1], words[i], rank) > 0) {
                return false;
            }
        }
        return true;
    }

    private int compare(String first, String second, int[] rank) {
        int commonLength = Math.min(first.length(), second.length());
        for (int i = 0; i < commonLength; i++) {
            char left = first.charAt(i);
            char right = second.charAt(i);
            if (left != right) {
                return Integer.compare(rank[left - 'a'], rank[right - 'a']);
            }
        }
        return Integer.compare(first.length(), second.length());
    }
}
```

## Approach 2: Optimized

The optimized solution removes repeated work while preserving the same correctness condition.

### Optimized Java (Topological Sort) — O(C) where C = total chars in words

```java
import java.util.*;

public class Solution {

    public String alienOrder(String[] words) {
        Map<Character, Set<Character>> adj = new LinkedHashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();
        for (String word : words) {
            for (char c : word.toCharArray()) {
                adj.putIfAbsent(c, new HashSet<>());
                inDegree.putIfAbsent(c, 0);
            }
        }
        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i],
                w2 = words[i + 1];
            if (w1.length() > w2.length() && w1.startsWith(w2)) {
                return "";
            }
            for (int j = 0; j < Math.min(w1.length(), w2.length()); j++) {
                if (w1.charAt(j) != w2.charAt(j)) {
                    if (adj.get(w1.charAt(j)).add(w2.charAt(j))) {
                        inDegree.merge(w2.charAt(j), 1, Integer::sum);
                    }
                    break;
                }
            }
        }
        Queue<Character> queue = new LinkedList<>();
        for (char c : inDegree.keySet()) {
            if (inDegree.get(c) == 0) {
                queue.offer(c);
            }
        }
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            char c = queue.poll();
            sb.append(c);
            for (char next : adj.get(c)) {
                if (inDegree.merge(next, -1, Integer::sum) == 0) {
                    queue.offer(next);
                }
            }
        }
        return sb.length() == inDegree.size() ? sb.toString() : "";
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
copying it. The source heading for this implementation is “Alien Dictionary”.
