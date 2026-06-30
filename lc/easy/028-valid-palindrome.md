# 028. Valid Palindrome

Platform: LeetCode  
Difficulty: Easy  
Topic: String, Two Pointers

## Problem Statement

Given a string `s`, return `true` if it is a palindrome after converting uppercase letters to lowercase and removing all non-alphanumeric characters.

## Constraints

- `1 <= s.length <= 2 * 10^5`
- `s` contains printable ASCII characters.

## Example

Input:

```text
s = "A man, a plan, a canal: Panama"
```

Output:

```text
true
```

## Brute Force Approach

Create a cleaned string, reverse it, and compare.

```java
class Solution {
    public boolean isPalindrome(String s) {
        StringBuilder cleaned = new StringBuilder();

        for (char ch : s.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                cleaned.append(Character.toLowerCase(ch));
            }
        }

        String normal = cleaned.toString();
        String reversed = cleaned.reverse().toString();

        return normal.equals(reversed);
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Use two pointers and skip non-alphanumeric characters.

```java
class Solution {
    public boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;

        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }

            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }

            char leftChar = Character.toLowerCase(s.charAt(left));
            char rightChar = Character.toLowerCase(s.charAt(right));

            if (leftChar != rightChar) {
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

- Time: `O(n)`
- Space: `O(1)`

