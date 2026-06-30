# 015. Plus One

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Math

## Problem Statement

You are given a non-empty array `digits` representing a non-negative integer. Add one to the integer and return the resulting digits.

The most significant digit is at the front. The number does not contain leading zeroes unless the number is `0`.

## Constraints

- `1 <= digits.length <= 100`
- `0 <= digits[i] <= 9`

## Example

Input:

```text
digits = [1, 2, 3]
```

Output:

```text
[1, 2, 4]
```

## Brute Force Approach

Build the number, add one, then convert it back to digits. This is simple but can overflow for large arrays.

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Solution {
    public int[] plusOne(int[] digits) {
        long number = 0;

        for (int digit : digits) {
            number = number * 10 + digit;
        }

        number++;

        List<Integer> result = new ArrayList<>();

        while (number > 0) {
            result.add((int) (number % 10));
            number = number / 10;
        }

        Collections.reverse(result);

        int[] answer = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            answer[i] = result.get(i);
        }

        return answer;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Process digits from right to left. If a digit is less than `9`, increase it and return. If all digits are `9`, create a new array with leading `1`.

```java
class Solution {
    public int[] plusOne(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--) {
            if (digits[i] < 9) {
                digits[i]++;
                return digits;
            }

            digits[i] = 0;
        }

        int[] result = new int[digits.length + 1];
        result[0] = 1;
        return result;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)` if no new array is needed, otherwise `O(n)`.

