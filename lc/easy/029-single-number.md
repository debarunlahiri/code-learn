# 029. Single Number

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Bit Manipulation

## Problem Statement

Given a non-empty integer array `nums`, every element appears twice except one element. Return the element that appears only once.

## Constraints

- `1 <= nums.length <= 3 * 10^4`
- `-3 * 10^4 <= nums[i] <= 3 * 10^4`
- Every element appears twice except one.

## Example

Input:

```text
nums = [4, 1, 2, 1, 2]
```

Output:

```text
4
```

## Brute Force Approach

Count each value using a hash map, then return the value with count `1`.

```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int singleNumber(int[] nums) {
        Map<Integer, Integer> count = new HashMap<>();

        for (int num : nums) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }

        for (int num : nums) {
            if (count.get(num) == 1) {
                return num;
            }
        }

        return -1;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Use XOR. A number XOR itself becomes `0`, and a number XOR `0` stays the same.

```java
class Solution {
    public int singleNumber(int[] nums) {
        int answer = 0;

        for (int num : nums) {
            answer = answer ^ num;
        }

        return answer;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

