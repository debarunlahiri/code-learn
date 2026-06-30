# 014. Length of Last Word

Platform: LeetCode  
Difficulty: Easy  
Topic: String

## Problem Statement

Given a string `s` containing words and spaces, return the length of the last word.

A word is a maximal substring containing only non-space characters.

## Constraints

- `1 <= s.length <= 10^4`
- `s` contains English letters and spaces.
- There is at least one word in `s`.

## Example

Input:

```text
s = "Hello World"
```

Output:

```text
5
```

## Brute Force Approach

Trim extra spaces, split by spaces, and return the length of the last part.

```java
class Solution {
    public int lengthOfLastWord(String s) {
        String[] words = s.trim().split(" ");
        return words[words.length - 1].length();
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Scan from the end. Skip spaces first, then count characters in the last word.

```java
class Solution {
    public int lengthOfLastWord(String s) {
        int index = s.length() - 1;

        while (index >= 0 && s.charAt(index) == ' ') {
            index--;
        }

        int length = 0;

        while (index >= 0 && s.charAt(index) != ' ') {
            length++;
            index--;
        }

        return length;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

