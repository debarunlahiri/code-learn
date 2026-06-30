# 037. Majority Element

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Hash Map, Voting

## Problem Statement

Given an array `nums`, return the element that appears more than `n / 2` times.

You may assume the majority element always exists.

## Constraints

- `1 <= nums.length <= 5 * 10^4`
- `-10^9 <= nums[i] <= 10^9`

## Example

Input:

```text
nums = [3, 2, 3]
```

Output:

```text
3
```

## Brute Force Approach

Count every number using a hash map and return the one whose count becomes greater than `n / 2`.

```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int majorityElement(int[] nums) {
        Map<Integer, Integer> count = new HashMap<>();

        for (int num : nums) {
            count.put(num, count.getOrDefault(num, 0) + 1);

            if (count.get(num) > nums.length / 2) {
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

Use Boyer-Moore voting. The majority element can cancel out all other elements and still remain.

```java
class Solution {
    public int majorityElement(int[] nums) {
        int candidate = 0;
        int votes = 0;

        for (int num : nums) {
            if (votes == 0) {
                candidate = num;
            }

            if (num == candidate) {
                votes++;
            } else {
                votes--;
            }
        }

        return candidate;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

