# 056. Number of Segments in a String

Platform: LeetCode  
Difficulty: Easy  
Topic: String

## Problem Statement

Given a string `s`, return the number of segments in it.

A segment is a continuous sequence of non-space characters.

## Constraints

- `0 <= s.length <= 300`
- `s` contains lowercase letters, uppercase letters, digits, and spaces.

## Example

Input:

```text
s = "Hello, my name is John"
```

Output:

```text
5
```

## Brute Force Approach

Trim the string and split by one or more spaces.

```java
class Solution {
    public int countSegments(String s) {
        String trimmed = s.trim();

        if (trimmed.isEmpty()) {
            return 0;
        }

        return trimmed.split("\\s+").length;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Count the start of each segment. A segment starts when a non-space character appears after either the beginning or a space.

```java
class Solution {
    public int countSegments(String s) {
        int count = 0;

        for (int i = 0; i < s.length(); i++) {
            boolean isCurrentCharNonSpace = s.charAt(i) != ' ';
            boolean isStart = i == 0 || s.charAt(i - 1) == ' ';

            if (isCurrentCharNonSpace && isStart) {
                count++;
            }
        }

        return count;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

