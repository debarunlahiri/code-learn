# 011. Remove Duplicates from Sorted Array

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Two Pointers

## Problem Statement

Given a sorted integer array `nums`, remove duplicates in-place so each unique value appears only once. Return the number of unique values.

The first returned-length positions of `nums` should contain the unique values in sorted order.

## Constraints

- `1 <= nums.length <= 3 * 10^4`
- `-100 <= nums[i] <= 100`
- `nums` is sorted in non-decreasing order.

## Example

Input:

```text
nums = [1, 1, 2]
```

Output:

```text
2
```

The first two elements become `[1, 2]`.

## Brute Force Approach

Store unique values in another list, then copy them back into the array.

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    public int removeDuplicates(int[] nums) {
        List<Integer> unique = new ArrayList<>();

        for (int num : nums) {
            if (unique.isEmpty() || unique.get(unique.size() - 1) != num) {
                unique.add(num);
            }
        }

        for (int i = 0; i < unique.size(); i++) {
            nums[i] = unique.get(i);
        }

        return unique.size();
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Use one pointer for the position where the next unique value should be written.

```java
class Solution {
    public int removeDuplicates(int[] nums) {
        int writeIndex = 1;

        for (int readIndex = 1; readIndex < nums.length; readIndex++) {
            if (nums[readIndex] != nums[readIndex - 1]) {
                nums[writeIndex] = nums[readIndex];
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

