# 018. Merge Sorted Array

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Two Pointers

## Problem Statement

You are given two sorted arrays `nums1` and `nums2`.

`nums1` has length `m + n`, where the first `m` elements are valid and the last `n` positions are empty space. Merge `nums2` into `nums1` so `nums1` becomes sorted.

## Constraints

- `nums1.length == m + n`
- `nums2.length == n`
- `0 <= m, n <= 200`
- `1 <= m + n <= 200`
- `-10^9 <= nums1[i], nums2[j] <= 10^9`

## Example

Input:

```text
nums1 = [1, 2, 3, 0, 0, 0], m = 3
nums2 = [2, 5, 6], n = 3
```

Output:

```text
[1, 2, 2, 3, 5, 6]
```

## Brute Force Approach

Copy `nums2` into the empty positions of `nums1`, then sort `nums1`.

```java
import java.util.Arrays;

class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        for (int i = 0; i < n; i++) {
            nums1[m + i] = nums2[i];
        }

        Arrays.sort(nums1);
    }
}
```

Complexity:

- Time: `O((m + n) log(m + n))`
- Space: `O(1)`

## Best Approach

Fill `nums1` from the back. Compare the largest remaining values from both arrays.

```java
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int first = m - 1;
        int second = n - 1;
        int write = m + n - 1;

        while (second >= 0) {
            if (first >= 0 && nums1[first] > nums2[second]) {
                nums1[write] = nums1[first];
                first--;
            } else {
                nums1[write] = nums2[second];
                second--;
            }

            write--;
        }
    }
}
```

Complexity:

- Time: `O(m + n)`
- Space: `O(1)`

