# 031. Isomorphic Strings

Platform: LeetCode  
Difficulty: Easy  
Topic: String, Hash Map

## Problem Statement

Given two strings `s` and `t`, return `true` if they are isomorphic.

Two strings are isomorphic when every character in `s` can be replaced to get `t`, while preserving order. A character must always map to the same character, and two different characters cannot map to the same character.

## Constraints

- `1 <= s.length <= 5 * 10^4`
- `t.length == s.length`
- `s` and `t` contain valid ASCII characters.

## Example

Input:

```text
s = "egg", t = "add"
```

Output:

```text
true
```

## Brute Force Approach

For every pair of positions, check whether equality relationships match in both strings.

```java
class Solution {
    public boolean isIsomorphic(String s, String t) {
        for (int i = 0; i < s.length(); i++) {
            for (int j = i + 1; j < s.length(); j++) {
                boolean sameInS = s.charAt(i) == s.charAt(j);
                boolean sameInT = t.charAt(i) == t.charAt(j);

                if (sameInS != sameInT) {
                    return false;
                }
            }
        }

        return true;
    }
}
```

Complexity:

- Time: `O(n^2)`
- Space: `O(1)`

## Best Approach

Track mapping in both directions.

```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public boolean isIsomorphic(String s, String t) {
        Map<Character, Character> sToT = new HashMap<>();
        Map<Character, Character> tToS = new HashMap<>();

        for (int i = 0; i < s.length(); i++) {
            char first = s.charAt(i);
            char second = t.charAt(i);

            if (sToT.containsKey(first) && sToT.get(first) != second) {
                return false;
            }

            if (tToS.containsKey(second) && tToS.get(second) != first) {
                return false;
            }

            sToT.put(first, second);
            tToS.put(second, first);
        }

        return true;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)` because ASCII character count is bounded.

