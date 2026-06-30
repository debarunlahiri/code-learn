# 053. Fizz Buzz

Platform: LeetCode  
Difficulty: Easy  
Topic: Math, String

## Problem Statement

Given an integer `n`, return a list of strings from `1` to `n`.

For each number:

- Use `"FizzBuzz"` if it is divisible by both `3` and `5`.
- Use `"Fizz"` if it is divisible by `3`.
- Use `"Buzz"` if it is divisible by `5`.
- Otherwise, use the number itself as a string.

## Constraints

- `1 <= n <= 10^4`

## Example

Input:

```text
n = 5
```

Output:

```text
["1", "2", "Fizz", "4", "Buzz"]
```

## Brute Force Approach

Check all conditions directly for every number.

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<String> fizzBuzz(int n) {
        List<String> result = new ArrayList<>();

        for (int number = 1; number <= n; number++) {
            if (number % 3 == 0 && number % 5 == 0) {
                result.add("FizzBuzz");
            } else if (number % 3 == 0) {
                result.add("Fizz");
            } else if (number % 5 == 0) {
                result.add("Buzz");
            } else {
                result.add(String.valueOf(number));
            }
        }

        return result;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)` apart from output.

## Best Approach

The direct one-pass approach is already optimal.

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<String> fizzBuzz(int n) {
        List<String> answer = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            String value = "";

            if (i % 3 == 0) {
                value += "Fizz";
            }
            if (i % 5 == 0) {
                value += "Buzz";
            }
            if (value.isEmpty()) {
                value = String.valueOf(i);
            }

            answer.add(value);
        }

        return answer;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)` apart from output.

