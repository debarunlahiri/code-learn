# 057. Assign Cookies

Platform: LeetCode  
Difficulty: Easy  
Topic: Greedy, Sorting

## Problem Statement

Each child has a greed factor, and each cookie has a size. A child is content if they receive a cookie whose size is at least their greed factor.

Each child can receive at most one cookie, and each cookie can be used once. Return the maximum number of content children.

## Constraints

- `1 <= g.length, s.length <= 3 * 10^4`
- `1 <= g[i], s[j] <= 2^31 - 1`

## Example

Input:

```text
g = [1, 2, 3], s = [1, 1]
```

Output:

```text
1
```

## Brute Force Approach

For each child, search for the smallest unused cookie that can satisfy them.

```java
class Solution {
    public int findContentChildren(int[] g, int[] s) {
        boolean[] used = new boolean[s.length];
        int content = 0;

        for (int greed : g) {
            int bestCookie = -1;

            for (int i = 0; i < s.length; i++) {
                if (!used[i] && s[i] >= greed) {
                    if (bestCookie == -1 || s[i] < s[bestCookie]) {
                        bestCookie = i;
                    }
                }
            }

            if (bestCookie != -1) {
                used[bestCookie] = true;
                content++;
            }
        }

        return content;
    }
}
```

Complexity:

- Time: `O(n * m)`
- Space: `O(m)`

## Best Approach

Sort both arrays. Give the smallest possible cookie to the least greedy remaining child.

```java
import java.util.Arrays;

class Solution {
    public int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);

        int child = 0;
        int cookie = 0;

        while (child < g.length && cookie < s.length) {
            if (s[cookie] >= g[child]) {
                child++;
            }

            cookie++;
        }

        return child;
    }
}
```

Complexity:

- Time: `O(n log n + m log m)`
- Space: `O(1)` apart from sorting internals.

