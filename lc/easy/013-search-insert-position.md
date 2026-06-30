# 013. Search Insert Position

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Binary Search

## Problem Statement

Given a sorted array of distinct integers and a target value, return the index if the target is found.

If it is not found, return the index where it should be inserted to keep the array sorted.

## Constraints

- `1 <= nums.length <= 10^4`
- `-10^4 <= nums[i] <= 10^4`
- `nums` contains distinct values sorted in ascending order.
- `-10^4 <= target <= 10^4`

## Example

Input:

```text
nums = [1, 3, 5, 6], target = 5
```

Output:

```text
2
```

## Brute Force Approach

Scan from left to right and return the first index whose value is greater than or equal to `target`.

```java
class Solution {
    public int searchInsert(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] >= target) {
                return i;
            }
        }

        return nums.length;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

## Best Approach

Use binary search because the array is sorted.

```java
class Solution {
    public int searchInsert(int[] nums, int target) {
        int left = 0;
        int right = nums.length;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return left;
    }
}
```

Complexity:

- Time: `O(log n)`
- Space: `O(1)`

