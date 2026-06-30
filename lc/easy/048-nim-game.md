# 048. Nim Game

Platform: LeetCode  
Difficulty: Easy  
Topic: Math, Game Theory

## Problem Statement

You are playing a game with `n` stones. You and your friend take turns removing `1`, `2`, or `3` stones. You move first.

Return `true` if you can guarantee a win assuming both players play optimally.

## Constraints

- `1 <= n <= 2^31 - 1`

## Example

Input:

```text
n = 4
```

Output:

```text
false
```

## Brute Force Approach

Use dynamic programming to decide whether each stone count is winning or losing.

```java
class Solution {
    public boolean canWinNim(int n) {
        if (n <= 3) {
            return true;
        }

        boolean[] win = new boolean[n + 1];
        win[1] = true;
        win[2] = true;
        win[3] = true;

        for (int stones = 4; stones <= n; stones++) {
            win[stones] = !win[stones - 1] || !win[stones - 2] || !win[stones - 3];
        }

        return win[n];
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Every multiple of `4` is losing because any move leaves the opponent a non-multiple of `4`.

```java
class Solution {
    public boolean canWinNim(int n) {
        return n % 4 != 0;
    }
}
```

Complexity:

- Time: `O(1)`
- Space: `O(1)`

