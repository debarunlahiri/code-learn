# 002. Valid Parentheses

Platform: LeetCode  
Difficulty: Easy  
Topic: String, Stack

## Problem Statement

Given a string `s` containing only the characters `(`, `)`, `{`, `}`, `[` and `]`, determine whether the brackets are valid.

A string is valid when:

- Every opening bracket has a matching closing bracket.
- Brackets close in the correct order.
- Each closing bracket matches the most recent unmatched opening bracket.

## Constraints

- `1 <= s.length <= 10^4`
- `s` contains only bracket characters.

## Example

Input:

```text
s = "()[]{}"
```

Output:

```text
true
```

## Brute Force Approach

Repeatedly remove valid adjacent pairs: `()`, `{}`, and `[]`. If the string becomes empty, it is valid.

```java
class Solution {
    public boolean isValid(String s) {
        boolean changed = true;

        while (changed) {
            int oldLength = s.length();

            s = s.replace("()", "");
            s = s.replace("{}", "");
            s = s.replace("[]", "");

            changed = s.length() != oldLength;
        }

        return s.length() == 0;
    }
}
```

Complexity:

- Time: `O(n^2)`
- Space: `O(n)`

## Best Approach

Use a stack. Push opening brackets. When a closing bracket appears, the top of the stack must be the matching opening bracket.

```java
import java.util.Stack;

class Solution {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();

        for (char ch : s.toCharArray()) {
            if (ch == '(' || ch == '{' || ch == '[') {
                stack.push(ch);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }

                char top = stack.pop();

                if (ch == ')' && top != '(') {
                    return false;
                }
                if (ch == '}' && top != '{') {
                    return false;
                }
                if (ch == ']' && top != '[') {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

