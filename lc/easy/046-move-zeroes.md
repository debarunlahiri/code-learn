# 046. Move Zeroes

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Two Pointers

## Problem Statement

Given an integer array `nums`, move all zeroes to the end while keeping the relative order of non-zero elements.

Do this in-place.

## Constraints

- `1 <= nums.length <= 10^4`
- `-2^31 <= nums[i] <= 2^31 - 1`

## Example

Input:

```text
nums = [0, 1, 0, 3, 12]
```

Output:

```text
[1, 3, 12, 0, 0]
```

## Brute Force Approach

Create another array, copy non-zero values first, then copy the result back.

```java
class Solution {
    public void moveZeroes(int[] nums) {
        int[] result = new int[nums.length];
        int index = 0;

        for (int num : nums) {
            if (num != 0) {
                result[index] = num;
                index++;
            }
        }

        for (int i = 0; i < nums.length; i++) {
            nums[i] = result[i];
        }
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Write non-zero values at the front, then fill the rest with zeroes.

```java
class Solution {
    public void moveZeroes(int[] nums) {
        int writeIndex = 0;

        for (int num : nums) {
            if (num != 0) {
                nums[writeIndex] = num;
                writeIndex++;
            }
        }

        while (writeIndex < nums.length) {
            nums[writeIndex] = 0;
            writeIndex++;
        }
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

