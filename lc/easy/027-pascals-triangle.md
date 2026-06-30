# 027. Pascal's Triangle

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Dynamic Programming

## Problem Statement

Given an integer `numRows`, return the first `numRows` rows of Pascal's Triangle.

Each number is the sum of the two numbers directly above it.

## Constraints

- `1 <= numRows <= 30`

## Example

Input:

```text
numRows = 5
```

Output:

```text
[[1], [1, 1], [1, 2, 1], [1, 3, 3, 1], [1, 4, 6, 4, 1]]
```

## Brute Force Approach

Use a combination formula for each position.

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> triangle = new ArrayList<>();

        for (int row = 0; row < numRows; row++) {
            List<Integer> current = new ArrayList<>();

            for (int col = 0; col <= row; col++) {
                current.add(combination(row, col));
            }

            triangle.add(current);
        }

        return triangle;
    }

    private int combination(int n, int r) {
        int result = 1;

        for (int i = 1; i <= r; i++) {
            result = result * (n - i + 1) / i;
        }

        return result;
    }
}
```

Complexity:

- Time: `O(numRows^3)` if every combination is calculated separately
- Space: `O(1)` apart from output.

## Best Approach

Build each row from the previous row.

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> triangle = new ArrayList<>();

        for (int row = 0; row < numRows; row++) {
            List<Integer> current = new ArrayList<>();

            for (int col = 0; col <= row; col++) {
                if (col == 0 || col == row) {
                    current.add(1);
                } else {
                    List<Integer> previous = triangle.get(row - 1);
                    current.add(previous.get(col - 1) + previous.get(col));
                }
            }

            triangle.add(current);
        }

        return triangle;
    }
}
```

Complexity:

- Time: `O(numRows^2)`
- Space: `O(1)` apart from output.

