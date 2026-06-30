# 004. Palindrome Number

Platform: LeetCode  
Difficulty: Easy  
Topic: Math

## Problem Statement

Given an integer `x`, return `true` if it reads the same forward and backward. Otherwise, return `false`.

Negative numbers are not palindromes because of the minus sign.

## Constraints

- `-2^31 <= x <= 2^31 - 1`

## Example

Input:

```text
x = 121
```

Output:

```text
true
```

## Brute Force Approach

Convert the number to a string and compare characters from both ends.

```java
class Solution {
    public boolean isPalindrome(int x) {
        String value = String.valueOf(x);

        int left = 0;
        int right = value.length() - 1;

        while (left < right) {
            if (value.charAt(left) != value.charAt(right)) {
                return false;
            }

            left++;
            right--;
        }

        return true;
    }
}
```

Complexity:

- Time: `O(d)`
- Space: `O(d)`

Here, `d` is the number of digits.

## Best Approach

Reverse only the second half of the number. This avoids converting the number to a string and avoids full integer overflow.

```java
class Solution {
    public boolean isPalindrome(int x) {
        if (x < 0) {
            return false;
        }

        if (x != 0 && x % 10 == 0) {
            return false;
        }

        int reversedHalf = 0;

        while (x > reversedHalf) {
            int lastDigit = x % 10;
            reversedHalf = reversedHalf * 10 + lastDigit;
            x = x / 10;
        }

        return x == reversedHalf || x == reversedHalf / 10;
    }
}
```

Complexity:

- Time: `O(d)`
- Space: `O(1)`

