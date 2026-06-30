# 064. Next Greater Element I

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Stack, Hash Map

## Problem Statement

You are given two arrays `nums1` and `nums2`, where `nums1` is a subset of `nums2`.

For each value in `nums1`, find the first greater value to its right in `nums2`. If it does not exist, return `-1` for that value.

## Constraints

- `1 <= nums1.length <= nums2.length <= 1000`
- `0 <= nums1[i], nums2[i] <= 10^4`
- All integers in both arrays are unique.

## Example

Input:

```text
nums1 = [4, 1, 2], nums2 = [1, 3, 4, 2]
```

Output:

```text
[-1, 3, -1]
```

## Brute Force Approach

For every value in `nums1`, find it in `nums2`, then scan right until a greater value appears.

```java
class Solution {
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        int[] answer = new int[nums1.length];

        for (int i = 0; i < nums1.length; i++) {
            answer[i] = -1;
            int position = -1;

            for (int j = 0; j < nums2.length; j++) {
                if (nums2[j] == nums1[i]) {
                    position = j;
                    break;
                }
            }

            for (int j = position + 1; j < nums2.length; j++) {
                if (nums2[j] > nums1[i]) {
                    answer[i] = nums2[j];
                    break;
                }
            }
        }

        return answer;
    }
}
```

Complexity:

- Time: `O(n * m)`
- Space: `O(1)` apart from output.

## Best Approach

Use a monotonic stack to precompute the next greater value for every number in `nums2`.

```java
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

class Solution {
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Map<Integer, Integer> nextGreater = new HashMap<>();
        Stack<Integer> stack = new Stack<>();

        for (int num : nums2) {
            while (!stack.isEmpty() && stack.peek() < num) {
                nextGreater.put(stack.pop(), num);
            }

            stack.push(num);
        }

        int[] answer = new int[nums1.length];

        for (int i = 0; i < nums1.length; i++) {
            answer[i] = nextGreater.getOrDefault(nums1[i], -1);
        }

        return answer;
    }
}
```

Complexity:

- Time: `O(n + m)`
- Space: `O(m)`

