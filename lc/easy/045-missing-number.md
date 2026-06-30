# 045. Missing Number

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Math, Bit Manipulation

## Problem Statement

Given an array `nums` containing `n` distinct numbers from the range `[0, n]`, return the only number in the range that is missing from the array.

## Constraints

- `n == nums.length`
- `1 <= n <= 10^4`
- `0 <= nums[i] <= n`
- All numbers are unique.

## Example

Input:

```text
nums = [3, 0, 1]
```

Output:

```text
2
```

## Brute Force Approach

Use a boolean array to mark which numbers are present.

```java
class Solution {
    public int missingNumber(int[] nums) {
        boolean[] present = new boolean[nums.length + 1];

        for (int num : nums) {
            present[num] = true;
        }

        for (int i = 0; i < present.length; i++) {
            if (!present[i]) {
                return i;
            }
        }

        return -1;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Use the expected sum of numbers from `0` to `n`, then subtract all values in the array.

```java
class Solution {
    public int missingNumber(int[] nums) {
        int n = nums.length;
        int expectedSum = n * (n + 1) / 2;
        int actualSum = 0;

        for (int num : nums) {
            actualSum += num;
        }

        return expectedSum - actualSum;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

