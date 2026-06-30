# 076. Maximum Product of Three Numbers

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Sorting

## Problem Statement

Given an integer array `nums`, return the maximum product of any three numbers.

## Constraints

- `3 <= nums.length <= 10^4`
- `-1000 <= nums[i] <= 1000`

## Example

Input:

```text
nums = [1, 2, 3]
```

Output:

```text
6
```

## Brute Force Approach

Try every possible group of three numbers.

```java
class Solution {
    public int maximumProduct(int[] nums) {
        int best = Integer.MIN_VALUE;

        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                for (int k = j + 1; k < nums.length; k++) {
                    best = Math.max(best, nums[i] * nums[j] * nums[k]);
                }
            }
        }

        return best;
    }
}
```

Complexity:

- Time: `O(n^3)`
- Space: `O(1)`

## Best Approach

The maximum product is either the three largest numbers, or the two smallest numbers and the largest number.

```java
import java.util.Arrays;

class Solution {
    public int maximumProduct(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;

        int productOfLargestThree = nums[n - 1] * nums[n - 2] * nums[n - 3];
        int productWithTwoNegatives = nums[0] * nums[1] * nums[n - 1];

        return Math.max(productOfLargestThree, productWithTwoNegatives);
    }
}
```

Complexity:

- Time: `O(n log n)`
- Space: `O(1)` apart from sorting internals.

