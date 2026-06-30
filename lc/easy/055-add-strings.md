# 055. Add Strings

Platform: LeetCode  
Difficulty: Easy  
Topic: String, Math

## Problem Statement

Given two non-negative integers `num1` and `num2` as strings, return their sum as a string.

Do not convert the whole strings directly into built-in integer types.

## Constraints

- `1 <= num1.length, num2.length <= 10^4`
- `num1` and `num2` contain digits only.
- `num1` and `num2` do not contain leading zeroes except for `"0"`.

## Example

Input:

```text
num1 = "11", num2 = "123"
```

Output:

```text
"134"
```

## Brute Force Approach

For small inputs, parsing to a number would work, but it fails for large strings because of overflow.

```java
class Solution {
    public String addStrings(String num1, String num2) {
        long first = Long.parseLong(num1);
        long second = Long.parseLong(num2);
        return String.valueOf(first + second);
    }
}
```

Complexity:

- Time: `O(n + m)`
- Space: `O(1)`

This approach is not valid for full constraints.

## Best Approach

Add digits from right to left, just like manual addition.

```java
class Solution {
    public String addStrings(String num1, String num2) {
        StringBuilder result = new StringBuilder();
        int i = num1.length() - 1;
        int j = num2.length() - 1;
        int carry = 0;

        while (i >= 0 || j >= 0 || carry > 0) {
            int digit1 = i >= 0 ? num1.charAt(i) - '0' : 0;
            int digit2 = j >= 0 ? num2.charAt(j) - '0' : 0;

            int sum = digit1 + digit2 + carry;
            result.append(sum % 10);
            carry = sum / 10;

            i--;
            j--;
        }

        return result.reverse().toString();
    }
}
```

Complexity:

- Time: `O(max(n, m))`
- Space: `O(max(n, m))`

