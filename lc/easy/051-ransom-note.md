# 051. Ransom Note

Platform: LeetCode  
Difficulty: Easy  
Topic: String, Counting

## Problem Statement

Given two strings `ransomNote` and `magazine`, return `true` if `ransomNote` can be built using letters from `magazine`.

Each letter from `magazine` can be used only once.

## Constraints

- `1 <= ransomNote.length, magazine.length <= 10^5`
- Both strings contain lowercase English letters.

## Example

Input:

```text
ransomNote = "aa", magazine = "aab"
```

Output:

```text
true
```

## Brute Force Approach

For each character in `ransomNote`, search for an unused matching character in `magazine`.

```java
class Solution {
    public boolean canConstruct(String ransomNote, String magazine) {
        boolean[] used = new boolean[magazine.length()];

        for (int i = 0; i < ransomNote.length(); i++) {
            char needed = ransomNote.charAt(i);
            boolean found = false;

            for (int j = 0; j < magazine.length(); j++) {
                if (!used[j] && magazine.charAt(j) == needed) {
                    used[j] = true;
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }
}
```

Complexity:

- Time: `O(n * m)`
- Space: `O(m)`

## Best Approach

Count letters from `magazine`, then use those counts for `ransomNote`.

```java
class Solution {
    public boolean canConstruct(String ransomNote, String magazine) {
        int[] count = new int[26];

        for (char ch : magazine.toCharArray()) {
            count[ch - 'a']++;
        }

        for (char ch : ransomNote.toCharArray()) {
            count[ch - 'a']--;

            if (count[ch - 'a'] < 0) {
                return false;
            }
        }

        return true;
    }
}
```

Complexity:

- Time: `O(n + m)`
- Space: `O(1)`

