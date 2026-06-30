# 059. Hamming Distance

Platform: LeetCode  
Difficulty: Easy  
Topic: Bit Manipulation

## Problem Statement

Given two integers `x` and `y`, return the number of bit positions where they are different.

## Constraints

- `0 <= x, y <= 2^31 - 1`

## Example

Input:

```text
x = 1, y = 4
```

Output:

```text
2
```

## Brute Force Approach

Compare each bit position one by one.

```java
class Solution {
    public int hammingDistance(int x, int y) {
        int count = 0;

        for (int bit = 0; bit < 32; bit++) {
            int bitX = (x >> bit) & 1;
            int bitY = (y >> bit) & 1;

            if (bitX != bitY) {
                count++;
            }
        }

        return count;
    }
}
```

Complexity:

- Time: `O(1)` because there are always 32 bits.
- Space: `O(1)`

## Best Approach

XOR marks different bits as `1`. Count the set bits.

```java
class Solution {
    public int hammingDistance(int x, int y) {
        int xor = x ^ y;
        int count = 0;

        while (xor != 0) {
            xor = xor & (xor - 1);
            count++;
        }

        return count;
    }
}
```

Complexity:

- Time: `O(1)`
- Space: `O(1)`

