# 041. Contains Duplicate II

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Hash Map, Sliding Window

## Problem Statement

Given an integer array `nums` and an integer `k`, return `true` if there are two different indices `i` and `j` such that:

- `nums[i] == nums[j]`
- `abs(i - j) <= k`

## Constraints

- `1 <= nums.length <= 10^5`
- `-10^9 <= nums[i] <= 10^9`
- `0 <= k <= 10^5`

## Example

Input:

```text
nums = [1, 2, 3, 1], k = 3
```

Output:

```text
true
```

## Brute Force Approach

For each index, check the next `k` positions.

```java
class Solution {
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length && j <= i + k; j++) {
                if (nums[i] == nums[j]) {
                    return true;
                }
            }
        }

        return false;
    }
}
```

Complexity:

- Time: `O(n * k)`
- Space: `O(1)`

## Best Approach

Store the latest index of each value. If the same value appears again within distance `k`, return `true`.

```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        Map<Integer, Integer> lastIndex = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            if (lastIndex.containsKey(nums[i])) {
                int previousIndex = lastIndex.get(nums[i]);

                if (i - previousIndex <= k) {
                    return true;
                }
            }

            lastIndex.put(nums[i], i);
        }

        return false;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

