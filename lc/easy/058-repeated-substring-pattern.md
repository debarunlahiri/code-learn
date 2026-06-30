# 058. Repeated Substring Pattern

Platform: LeetCode  
Difficulty: Easy  
Topic: String

## Problem Statement

Given a string `s`, return `true` if it can be built by repeating one of its non-empty substrings multiple times.

## Constraints

- `1 <= s.length <= 10^4`
- `s` contains lowercase English letters.

## Example

Input:

```text
s = "abab"
```

Output:

```text
true
```

## Brute Force Approach

Try every possible substring length that divides the full string length.

```java
class Solution {
    public boolean repeatedSubstringPattern(String s) {
        int n = s.length();

        for (int length = 1; length <= n / 2; length++) {
            if (n % length != 0) {
                continue;
            }

            String part = s.substring(0, length);
            StringBuilder built = new StringBuilder();

            while (built.length() < n) {
                built.append(part);
            }

            if (built.toString().equals(s)) {
                return true;
            }
        }

        return false;
    }
}
```

Complexity:

- Time: `O(n^2)`
- Space: `O(n)`

## Best Approach

If `s` is made by repeating a substring, then `s` appears inside `(s + s)` after removing the first and last character.

```java
class Solution {
    public boolean repeatedSubstringPattern(String s) {
        String doubled = s + s;
        String middle = doubled.substring(1, doubled.length() - 1);
        return middle.contains(s);
    }
}
```

Complexity:

- Time: `O(n^2)` in the worst case depending on `contains`
- Space: `O(n)`

