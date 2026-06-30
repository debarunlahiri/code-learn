# 038. Excel Sheet Column Number

Platform: LeetCode  
Difficulty: Easy  
Topic: Math, String

## Problem Statement

Given a string `columnTitle` representing an Excel column title, return its corresponding column number.

For example, `A` is `1`, `B` is `2`, and `AA` is `27`.

## Constraints

- `1 <= columnTitle.length <= 7`
- `columnTitle` contains uppercase English letters.

## Example

Input:

```text
columnTitle = "AB"
```

Output:

```text
28
```

## Brute Force Approach

Process the string from right to left and multiply each character value by the correct power of `26`.

```java
class Solution {
    public int titleToNumber(String columnTitle) {
        int result = 0;
        int power = 1;

        for (int i = columnTitle.length() - 1; i >= 0; i--) {
            int value = columnTitle.charAt(i) - 'A' + 1;
            result += value * power;
            power *= 26;
        }

        return result;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

## Best Approach

Read left to right like a base-26 number.

```java
class Solution {
    public int titleToNumber(String columnTitle) {
        int result = 0;

        for (char ch : columnTitle.toCharArray()) {
            int value = ch - 'A' + 1;
            result = result * 26 + value;
        }

        return result;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

