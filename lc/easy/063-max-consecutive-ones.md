# 063. Max Consecutive Ones

Platform: LeetCode  
Difficulty: Easy  
Topic: Array

## Problem Statement

Given a binary array `nums`, return the maximum number of consecutive `1`s.

## Constraints

- `1 <= nums.length <= 10^5`
- `nums[i]` is either `0` or `1`.

## Example

Input:

```text
nums = [1, 1, 0, 1, 1, 1]
```

Output:

```text
3
```

## Brute Force Approach

For every starting index, count how many consecutive ones begin there.

```java
class Solution {
    public int findMaxConsecutiveOnes(int[] nums) {
        int best = 0;

        for (int i = 0; i < nums.length; i++) {
            int count = 0;

            for (int j = i; j < nums.length && nums[j] == 1; j++) {
                count++;
            }

            best = Math.max(best, count);
        }

        return best;
    }
}
```

Complexity:

- Time: `O(n^2)` in the worst case
- Space: `O(1)`

## Best Approach

Scan once and reset the current count whenever a zero appears.

```java
class Solution {
    public int findMaxConsecutiveOnes(int[] nums) {
        int current = 0;
        int best = 0;

        for (int num : nums) {
            if (num == 1) {
                current++;
                best = Math.max(best, current);
            } else {
                current = 0;
            }
        }

        return best;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

