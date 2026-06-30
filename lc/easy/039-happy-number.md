# 039. Happy Number

Platform: LeetCode  
Difficulty: Easy  
Topic: Math, Hash Set, Two Pointers

## Problem Statement

Given a positive integer `n`, repeatedly replace the number by the sum of the squares of its digits.

Return `true` if the process eventually reaches `1`. Return `false` if it falls into a cycle.

## Constraints

- `1 <= n <= 2^31 - 1`

## Example

Input:

```text
n = 19
```

Output:

```text
true
```

Explanation: `19 -> 82 -> 68 -> 100 -> 1`.

## Brute Force Approach

Use a set to remember numbers already seen. If a number repeats, the process is stuck in a cycle.

```java
import java.util.HashSet;
import java.util.Set;

class Solution {
    public boolean isHappy(int n) {
        Set<Integer> seen = new HashSet<>();

        while (n != 1) {
            if (seen.contains(n)) {
                return false;
            }

            seen.add(n);
            n = nextNumber(n);
        }

        return true;
    }

    private int nextNumber(int n) {
        int sum = 0;

        while (n > 0) {
            int digit = n % 10;
            sum += digit * digit;
            n = n / 10;
        }

        return sum;
    }
}
```

Complexity:

- Time: `O(log n)` per transformation
- Space: `O(k)`, where `k` is the number of seen values.

## Best Approach

Use slow and fast pointers to detect a cycle without extra space.

```java
class Solution {
    public boolean isHappy(int n) {
        int slow = n;
        int fast = nextNumber(n);

        while (fast != 1 && slow != fast) {
            slow = nextNumber(slow);
            fast = nextNumber(nextNumber(fast));
        }

        return fast == 1;
    }

    private int nextNumber(int n) {
        int sum = 0;

        while (n > 0) {
            int digit = n % 10;
            sum += digit * digit;
            n = n / 10;
        }

        return sum;
    }
}
```

Complexity:

- Time: `O(log n)` per transformation
- Space: `O(1)`

