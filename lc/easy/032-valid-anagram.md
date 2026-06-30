# 032. Valid Anagram

Platform: LeetCode  
Difficulty: Easy  
Topic: String, Hash Map

## Problem Statement

Given two strings `s` and `t`, return `true` if `t` is an anagram of `s`.

An anagram uses the same characters with the same counts, but the order may be different.

## Constraints

- `1 <= s.length, t.length <= 5 * 10^4`
- `s` and `t` contain lowercase English letters.

## Example

Input:

```text
s = "anagram", t = "nagaram"
```

Output:

```text
true
```

## Brute Force Approach

Sort both strings and compare them.

```java
import java.util.Arrays;

class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }

        char[] first = s.toCharArray();
        char[] second = t.toCharArray();

        Arrays.sort(first);
        Arrays.sort(second);

        return Arrays.equals(first, second);
    }
}
```

Complexity:

- Time: `O(n log n)`
- Space: `O(n)`

## Best Approach

Count characters. Add counts from `s` and subtract counts from `t`.

```java
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }

        int[] count = new int[26];

        for (int i = 0; i < s.length(); i++) {
            count[s.charAt(i) - 'a']++;
            count[t.charAt(i) - 'a']--;
        }

        for (int value : count) {
            if (value != 0) {
                return false;
            }
        }

        return true;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

