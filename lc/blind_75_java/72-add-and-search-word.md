# 72. Add And Search Word

Platform: LeetCode  
Collection: Blind 75  
Implementation language: Java

## Complete Problem Statement

Implement a word dictionary supporting insertion and search, where a dot in a search pattern matches any single letter.

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

class WordDictionary {

    private final List<String> words = new ArrayList<>();

    public void addWord(String word) {
        words.add(word);
    }

    public boolean search(String pattern) {
        for (String word : words) {
            if (matches(word, pattern)) {
                return true;
            }
        }
        return false;
    }

    private boolean matches(String word, String pattern) {
        if (word.length() != pattern.length()) {
            return false;
        }

        for (int i = 0; i < word.length(); i++) {
            char expected = pattern.charAt(i);
            if (expected != '.' && expected != word.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}
```

## Approach 2: Optimized

The optimized solution removes repeated work while preserving the same correctness condition.

### Optimized Java (Trie + DFS for wildcards) — O(m) average, O(26^m) worst

```java
import java.util.*;

class WordDictionary {

    private TrieNode root = new TrieNode();

    class TrieNode {

        TrieNode[] children = new TrieNode[26];
        boolean isEnd;
    }

    public void addWord(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (node.children[c - 'a'] == null) {
                node.children[c - 'a'] = new TrieNode();
            }
            node = node.children[c - 'a'];
        }
        node.isEnd = true;
    }

    public boolean search(String word) {
        return dfs(word, 0, root);
    }

    private boolean dfs(String word, int i, TrieNode node) {
        if (i == word.length()) {
            return node.isEnd;
        }
        char c = word.charAt(i);
        if (c == '.') {
            for (TrieNode child : node.children) {
                if (child != null && dfs(word, i + 1, child)) {
                    return true;
                }
            }
            return false;
        }
        if (node.children[c - 'a'] == null) {
            return false;
        }
        return dfs(word, i + 1, node.children[c - 'a']);
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
copying it. The source heading for this implementation is “Add and Search Word”.
