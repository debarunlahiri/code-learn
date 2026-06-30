# 065. Keyboard Row

Platform: LeetCode  
Difficulty: Easy  
Topic: String, Hash Set

## Problem Statement

Given an array of words, return the words that can be typed using letters from only one row of an American keyboard.

## Constraints

- `1 <= words.length <= 20`
- `1 <= words[i].length <= 100`
- Words contain English letters.

## Example

Input:

```text
words = ["Hello", "Alaska", "Dad", "Peace"]
```

Output:

```text
["Alaska", "Dad"]
```

## Brute Force Approach

For each word, check whether all letters belong to row one, row two, or row three.

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    public String[] findWords(String[] words) {
        List<String> result = new ArrayList<>();

        for (String word : words) {
            String lower = word.toLowerCase();

            if (canType(lower, "qwertyuiop")
                    || canType(lower, "asdfghjkl")
                    || canType(lower, "zxcvbnm")) {
                result.add(word);
            }
        }

        return result.toArray(new String[0]);
    }

    private boolean canType(String word, String row) {
        for (char ch : word.toCharArray()) {
            if (row.indexOf(ch) == -1) {
                return false;
            }
        }

        return true;
    }
}
```

Complexity:

- Time: `O(total characters)`
- Space: `O(1)` apart from output.

## Best Approach

Map every letter to its keyboard row and check each word in one pass.

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    public String[] findWords(String[] words) {
        int[] row = new int[26];
        fill(row, "qwertyuiop", 1);
        fill(row, "asdfghjkl", 2);
        fill(row, "zxcvbnm", 3);

        List<String> result = new ArrayList<>();

        for (String word : words) {
            String lower = word.toLowerCase();
            int expectedRow = row[lower.charAt(0) - 'a'];
            boolean valid = true;

            for (char ch : lower.toCharArray()) {
                if (row[ch - 'a'] != expectedRow) {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                result.add(word);
            }
        }

        return result.toArray(new String[0]);
    }

    private void fill(int[] row, String letters, int rowNumber) {
        for (char ch : letters.toCharArray()) {
            row[ch - 'a'] = rowNumber;
        }
    }
}
```

Complexity:

- Time: `O(total characters)`
- Space: `O(1)` apart from output.

