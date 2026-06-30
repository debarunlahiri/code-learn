# 012. Remove Element

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Two Pointers

## Problem Statement

Given an integer array `nums` and an integer `val`, remove all occurrences of `val` in-place. Return the number of remaining elements.

The first returned-length positions should contain the remaining values. Their order does not matter.

## Constraints

- `0 <= nums.length <= 100`
- `0 <= nums[i] <= 50`
- `0 <= val <= 100`

## Example

Input:

```text
nums = [3, 2, 2, 3], val = 3
```

Output:

```text
2
```

The first two elements can be `[2, 2]`.

## Brute Force Approach

Copy all values that are not equal to `val` into another list, then copy them back.

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    public int removeElement(int[] nums, int val) {
        List<Integer> remaining = new ArrayList<>();

        for (int num : nums) {
            if (num != val) {
                remaining.add(num);
            }
        }

        for (int i = 0; i < remaining.size(); i++) {
            nums[i] = remaining.get(i);
        }

        return remaining.size();
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Overwrite unwanted values by writing only valid values from left to right.

```java
class Solution {
    public int removeElement(int[] nums, int val) {
        int writeIndex = 0;

        for (int num : nums) {
            if (num != val) {
                nums[writeIndex] = num;
                writeIndex++;
            }
        }

        return writeIndex;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

