# 071. Detect Capital

Platform: LeetCode  
Difficulty: Easy  
Topic: String

## Problem Statement

Given a word, return `true` if its capital usage is correct.

Capital usage is correct when one of these is true:

- All letters are uppercase.
- All letters are lowercase.
- Only the first letter is uppercase.

## Constraints

- `1 <= word.length <= 100`
- `word` contains lowercase and uppercase English letters.

## Example

Input:

```text
word = "USA"
```

Output:

```text
true
```

## Brute Force Approach

Check whether the word matches any of the three valid forms.

```java
class Solution {
    public boolean detectCapitalUse(String word) {
        return word.equals(word.toUpperCase())
                || word.equals(word.toLowerCase())
                || word.equals(firstUpperRestLower(word));
    }

    private String firstUpperRestLower(String word) {
        return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Count uppercase letters and validate the count.

```java
class Solution {
    public boolean detectCapitalUse(String word) {
        int uppercaseCount = 0;

        for (char ch : word.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                uppercaseCount++;
            }
        }

        if (uppercaseCount == 0 || uppercaseCount == word.length()) {
            return true;
        }

        return uppercaseCount == 1 && Character.isUpperCase(word.charAt(0));
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

