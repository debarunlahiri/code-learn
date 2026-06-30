# 047. Word Pattern

Platform: LeetCode  
Difficulty: Easy  
Topic: String, Hash Map

## Problem Statement

Given a pattern string and a sentence `s`, return `true` if the sentence follows the same pattern.

Each pattern character must map to exactly one word, and each word must map to exactly one pattern character.

## Constraints

- `1 <= pattern.length <= 300`
- `pattern` contains lowercase English letters.
- `1 <= s.length <= 3000`
- `s` contains lowercase English letters and spaces.

## Example

Input:

```text
pattern = "abba", s = "dog cat cat dog"
```

Output:

```text
true
```

## Brute Force Approach

For every pair of positions, compare whether pattern equality and word equality match.

```java
class Solution {
    public boolean wordPattern(String pattern, String s) {
        String[] words = s.split(" ");

        if (pattern.length() != words.length) {
            return false;
        }

        for (int i = 0; i < pattern.length(); i++) {
            for (int j = i + 1; j < pattern.length(); j++) {
                boolean samePattern = pattern.charAt(i) == pattern.charAt(j);
                boolean sameWord = words[i].equals(words[j]);

                if (samePattern != sameWord) {
                    return false;
                }
            }
        }

        return true;
    }
}
```

Complexity:

- Time: `O(n^2)`
- Space: `O(n)`

## Best Approach

Use two maps to enforce one-to-one mapping.

```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public boolean wordPattern(String pattern, String s) {
        String[] words = s.split(" ");

        if (pattern.length() != words.length) {
            return false;
        }

        Map<Character, String> charToWord = new HashMap<>();
        Map<String, Character> wordToChar = new HashMap<>();

        for (int i = 0; i < pattern.length(); i++) {
            char ch = pattern.charAt(i);
            String word = words[i];

            if (charToWord.containsKey(ch) && !charToWord.get(ch).equals(word)) {
                return false;
            }

            if (wordToChar.containsKey(word) && wordToChar.get(word) != ch) {
                return false;
            }

            charToWord.put(ch, word);
            wordToChar.put(word, ch);
        }

        return true;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

