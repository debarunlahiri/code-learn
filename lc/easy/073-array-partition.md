# 073. Array Partition

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Greedy, Sorting

## Problem Statement

Given an integer array `nums` of length `2n`, group the numbers into `n` pairs.

Return the maximum possible sum of the minimum value from each pair.

## Constraints

- `1 <= n <= 10^4`
- `nums.length == 2 * n`
- `-10^4 <= nums[i] <= 10^4`

## Example

Input:

```text
nums = [1, 4, 3, 2]
```

Output:

```text
4
```

Explanation: Pair `(1, 2)` and `(3, 4)`. Sum of minimums is `1 + 3 = 4`.

## Brute Force Approach

Try to pair numbers after sorting. Pairing neighboring sorted values gives a strong result because it avoids wasting large values with very small values.

```java
import java.util.Arrays;

class Solution {
    public int arrayPairSum(int[] nums) {
        Arrays.sort(nums);
        int sum = 0;

        for (int i = 0; i < nums.length; i += 2) {
            sum += nums[i];
        }

        return sum;
    }
}
```

Complexity:

- Time: `O(n log n)`
- Space: `O(1)` apart from sorting internals.

## Best Approach

The sorted neighboring-pair greedy approach is optimal.

```java
import java.util.Arrays;

class Solution {
    public int arrayPairSum(int[] nums) {
        Arrays.sort(nums);

        int answer = 0;

        for (int i = 0; i < nums.length; i += 2) {
            answer += nums[i];
        }

        return answer;
    }
}
```

Complexity:

- Time: `O(n log n)`
- Space: `O(1)` apart from sorting internals.

