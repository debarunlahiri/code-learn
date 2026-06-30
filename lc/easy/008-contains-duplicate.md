# 008. Contains Duplicate

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Hash Set

## Problem Statement

Given an integer array `nums`, return `true` if any value appears at least twice. Return `false` if every value is unique.

## Constraints

- `1 <= nums.length <= 10^5`
- `-10^9 <= nums[i] <= 10^9`

## Example

Input:

```text
nums = [1, 2, 3, 1]
```

Output:

```text
true
```

## Brute Force Approach

Compare every element with every other element.

```java
class Solution {
    public boolean containsDuplicate(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
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

- Time: `O(n^2)`
- Space: `O(1)`

## Best Approach

Use a `HashSet` to remember numbers we have already seen.

```java
import java.util.HashSet;
import java.util.Set;

class Solution {
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> seen = new HashSet<>();

        for (int num : nums) {
            if (seen.contains(num)) {
                return true;
            }

            seen.add(num);
        }

        return false;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

