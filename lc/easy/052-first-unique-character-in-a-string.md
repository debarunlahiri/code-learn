# 052. First Unique Character in a String

Platform: LeetCode  
Difficulty: Easy  
Topic: String, Counting

## Problem Statement

Given a string `s`, return the index of the first character that appears exactly once. If no such character exists, return `-1`.

## Constraints

- `1 <= s.length <= 10^5`
- `s` contains lowercase English letters.

## Example

Input:

```text
s = "leetcode"
```

Output:

```text
0
```

## Brute Force Approach

For each character, scan the whole string to count how many times it appears.

```java
class Solution {
    public int firstUniqChar(String s) {
        for (int i = 0; i < s.length(); i++) {
            int count = 0;

            for (int j = 0; j < s.length(); j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    count++;
                }
            }

            if (count == 1) {
                return i;
            }
        }

        return -1;
    }
}
```

Complexity:

- Time: `O(n^2)`
- Space: `O(1)`

## Best Approach

Count all characters once, then scan again to find the first character with count `1`.

```java
class Solution {
    public int firstUniqChar(String s) {
        int[] count = new int[26];

        for (char ch : s.toCharArray()) {
            count[ch - 'a']++;
        }

        for (int i = 0; i < s.length(); i++) {
            if (count[s.charAt(i) - 'a'] == 1) {
                return i;
            }
        }

        return -1;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

