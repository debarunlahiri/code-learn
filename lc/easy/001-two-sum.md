# 001. Two Sum

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Hash Map

## Problem Statement

Given an integer array `nums` and an integer `target`, return the indices of two different elements whose sum is equal to `target`.

You may assume there is exactly one valid answer. The same array element cannot be used twice.

## Constraints

- `2 <= nums.length <= 10^4`
- `-10^9 <= nums[i] <= 10^9`
- `-10^9 <= target <= 10^9`
- Exactly one answer exists.

## Example

Input:

```text
nums = [2, 7, 11, 15], target = 9
```

Output:

```text
[0, 1]
```

Explanation: `nums[0] + nums[1] = 2 + 7 = 9`.

## Brute Force Approach

Check every pair of elements. If their sum is equal to `target`, return their indices.

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[] { i, j };
                }
            }
        }

        return new int[] { -1, -1 };
    }
}
```

Complexity:

- Time: `O(n^2)`
- Space: `O(1)`

## Best Approach

Use a hash map to store each number and its index. For every number, calculate the needed value: `target - nums[i]`.

If the needed value already exists in the map, we found the answer.

```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> valueToIndex = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int needed = target - nums[i];

            if (valueToIndex.containsKey(needed)) {
                return new int[] { valueToIndex.get(needed), i };
            }

            valueToIndex.put(nums[i], i);
        }

        return new int[] { -1, -1 };
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

