# 054. Third Maximum Number

Platform: LeetCode  
Difficulty: Easy  
Topic: Array

## Problem Statement

Given an integer array `nums`, return the third distinct maximum number. If the third distinct maximum does not exist, return the maximum number.

## Constraints

- `1 <= nums.length <= 10^4`
- `-2^31 <= nums[i] <= 2^31 - 1`

## Example

Input:

```text
nums = [3, 2, 1]
```

Output:

```text
1
```

## Brute Force Approach

Use a set to remove duplicates, sort the values, and pick the third largest if it exists.

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Solution {
    public int thirdMax(int[] nums) {
        Set<Integer> unique = new HashSet<>();

        for (int num : nums) {
            unique.add(num);
        }

        List<Integer> values = new ArrayList<>(unique);
        Collections.sort(values);

        if (values.size() < 3) {
            return values.get(values.size() - 1);
        }

        return values.get(values.size() - 3);
    }
}
```

Complexity:

- Time: `O(n log n)`
- Space: `O(n)`

## Best Approach

Track the top three distinct values in one pass using `Long` to handle `Integer.MIN_VALUE`.

```java
class Solution {
    public int thirdMax(int[] nums) {
        Long first = null;
        Long second = null;
        Long third = null;

        for (int num : nums) {
            long value = num;

            if ((first != null && value == first)
                    || (second != null && value == second)
                    || (third != null && value == third)) {
                continue;
            }

            if (first == null || value > first) {
                third = second;
                second = first;
                first = value;
            } else if (second == null || value > second) {
                third = second;
                second = value;
            } else if (third == null || value > third) {
                third = value;
            }
        }

        return third == null ? first.intValue() : third.intValue();
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

