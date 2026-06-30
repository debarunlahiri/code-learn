# 050. Intersection of Two Arrays

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Hash Set

## Problem Statement

Given two integer arrays `nums1` and `nums2`, return an array of their unique intersection values.

Each value in the result must be unique, and the result can be in any order.

## Constraints

- `1 <= nums1.length, nums2.length <= 1000`
- `0 <= nums1[i], nums2[i] <= 1000`

## Example

Input:

```text
nums1 = [1, 2, 2, 1], nums2 = [2, 2]
```

Output:

```text
[2]
```

## Brute Force Approach

Compare every pair and store matching values in a set.

```java
import java.util.HashSet;
import java.util.Set;

class Solution {
    public int[] intersection(int[] nums1, int[] nums2) {
        Set<Integer> resultSet = new HashSet<>();

        for (int first : nums1) {
            for (int second : nums2) {
                if (first == second) {
                    resultSet.add(first);
                }
            }
        }

        int[] result = new int[resultSet.size()];
        int index = 0;

        for (int num : resultSet) {
            result[index] = num;
            index++;
        }

        return result;
    }
}
```

Complexity:

- Time: `O(n * m)`
- Space: `O(min(n, m))`

## Best Approach

Store values from the first array in a set, then check values from the second array.

```java
import java.util.HashSet;
import java.util.Set;

class Solution {
    public int[] intersection(int[] nums1, int[] nums2) {
        Set<Integer> firstSet = new HashSet<>();
        Set<Integer> resultSet = new HashSet<>();

        for (int num : nums1) {
            firstSet.add(num);
        }

        for (int num : nums2) {
            if (firstSet.contains(num)) {
                resultSet.add(num);
            }
        }

        int[] result = new int[resultSet.size()];
        int index = 0;

        for (int num : resultSet) {
            result[index] = num;
            index++;
        }

        return result;
    }
}
```

Complexity:

- Time: `O(n + m)`
- Space: `O(n + m)`

