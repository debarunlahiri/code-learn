# 042. Power of Two

Platform: LeetCode  
Difficulty: Easy  
Topic: Math, Bit Manipulation

## Problem Statement

Given an integer `n`, return `true` if it is a power of two.

An integer is a power of two if it can be written as `2^x` for some integer `x >= 0`.

## Constraints

- `-2^31 <= n <= 2^31 - 1`

## Example

Input:

```text
n = 16
```

Output:

```text
true
```

## Brute Force Approach

Keep dividing by `2`. If an odd number greater than `1` appears, it is not a power of two.

```java
class Solution {
    public boolean isPowerOfTwo(int n) {
        if (n <= 0) {
            return false;
        }

        while (n % 2 == 0) {
            n = n / 2;
        }

        return n == 1;
    }
}
```

Complexity:

- Time: `O(log n)`
- Space: `O(1)`

## Best Approach

A power of two has exactly one set bit. For positive `n`, `n & (n - 1)` removes the lowest set bit, so the result must be `0`.

```java
class Solution {
    public boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
}
```

Complexity:

- Time: `O(1)`
- Space: `O(1)`

