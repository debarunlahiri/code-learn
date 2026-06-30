# 049. Reverse String

Platform: LeetCode  
Difficulty: Easy  
Topic: String, Two Pointers

## Problem Statement

Given a character array `s`, reverse it in-place.

You must modify the input array directly.

## Constraints

- `1 <= s.length <= 10^5`
- `s[i]` is a printable ASCII character.

## Example

Input:

```text
s = ['h', 'e', 'l', 'l', 'o']
```

Output:

```text
['o', 'l', 'l', 'e', 'h']
```

## Brute Force Approach

Create another array in reversed order, then copy it back.

```java
class Solution {
    public void reverseString(char[] s) {
        char[] reversed = new char[s.length];

        for (int i = 0; i < s.length; i++) {
            reversed[i] = s[s.length - 1 - i];
        }

        for (int i = 0; i < s.length; i++) {
            s[i] = reversed[i];
        }
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Use two pointers and swap characters.

```java
class Solution {
    public void reverseString(char[] s) {
        int left = 0;
        int right = s.length - 1;

        while (left < right) {
            char temp = s[left];
            s[left] = s[right];
            s[right] = temp;

            left++;
            right--;
        }
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

