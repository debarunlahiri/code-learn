# 016. Sqrt(x)

Platform: LeetCode  
Difficulty: Easy  
Topic: Math, Binary Search

## Problem Statement

Given a non-negative integer `x`, return the integer part of its square root.

Do not use built-in exponent functions.

## Constraints

- `0 <= x <= 2^31 - 1`

## Example

Input:

```text
x = 8
```

Output:

```text
2
```

Explanation: The square root of `8` is about `2.82`, so return `2`.

## Brute Force Approach

Try every number from `1` upward until its square becomes greater than `x`.

```java
class Solution {
    public int mySqrt(int x) {
        long number = 0;

        while (number * number <= x) {
            number++;
        }

        return (int) number - 1;
    }
}
```

Complexity:

- Time: `O(sqrt(x))`
- Space: `O(1)`

## Best Approach

Use binary search over possible answers.

```java
class Solution {
    public int mySqrt(int x) {
        int left = 0;
        int right = x;
        int answer = 0;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            long square = (long) mid * mid;

            if (square <= x) {
                answer = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return answer;
    }
}
```

Complexity:

- Time: `O(log x)`
- Space: `O(1)`

