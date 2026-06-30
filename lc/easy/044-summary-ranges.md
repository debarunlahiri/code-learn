# 044. Summary Ranges

Platform: LeetCode  
Difficulty: Easy  
Topic: Array

## Problem Statement

Given a sorted unique integer array `nums`, summarize the continuous ranges.

For a range with one number, use `"a"`. For a range with multiple numbers, use `"a->b"`.

## Constraints

- `0 <= nums.length <= 20`
- `-2^31 <= nums[i] <= 2^31 - 1`
- Values are unique and sorted ascending.

## Example

Input:

```text
nums = [0, 1, 2, 4, 5, 7]
```

Output:

```text
["0->2", "4->5", "7"]
```

## Brute Force Approach

Start a range at every unused position and move forward while values are consecutive.

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<String> summaryRanges(int[] nums) {
        List<String> result = new ArrayList<>();
        int index = 0;

        while (index < nums.length) {
            int start = nums[index];

            while (index + 1 < nums.length && nums[index + 1] == nums[index] + 1) {
                index++;
            }

            int end = nums[index];

            if (start == end) {
                result.add(String.valueOf(start));
            } else {
                result.add(start + "->" + end);
            }

            index++;
        }

        return result;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)` apart from output.

## Best Approach

The one-pass range construction is already optimal because every value must be inspected.

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<String> summaryRanges(int[] nums) {
        List<String> ranges = new ArrayList<>();

        for (int i = 0; i < nums.length; i++) {
            int start = nums[i];

            while (i + 1 < nums.length && nums[i + 1] == nums[i] + 1) {
                i++;
            }

            if (start == nums[i]) {
                ranges.add(String.valueOf(start));
            } else {
                ranges.add(start + "->" + nums[i]);
            }
        }

        return ranges;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)` apart from output.

