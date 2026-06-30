# 062. License Key Formatting

Platform: LeetCode  
Difficulty: Easy  
Topic: String

## Problem Statement

Given a license key string `s` and an integer `k`, reformat the key so groups are separated by dashes.

All letters should be uppercase. The first group may be shorter, but every other group must have exactly `k` characters.

## Constraints

- `1 <= s.length <= 10^5`
- `1 <= k <= 10^4`
- `s` contains English letters, digits, and dashes.

## Example

Input:

```text
s = "5F3Z-2e-9-w", k = 4
```

Output:

```text
"5F3Z-2E9W"
```

## Brute Force Approach

Remove dashes, uppercase the string, then build groups from the front.

```java
class Solution {
    public String licenseKeyFormatting(String s, int k) {
        String cleaned = s.replace("-", "").toUpperCase();
        StringBuilder result = new StringBuilder();

        int firstGroupLength = cleaned.length() % k;
        int index = 0;

        if (firstGroupLength > 0) {
            result.append(cleaned, 0, firstGroupLength);
            index = firstGroupLength;
        }

        while (index < cleaned.length()) {
            if (result.length() > 0) {
                result.append("-");
            }

            result.append(cleaned, index, index + k);
            index += k;
        }

        return result.toString();
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Build from right to left and insert dashes after every `k` valid characters.

```java
class Solution {
    public String licenseKeyFormatting(String s, int k) {
        StringBuilder result = new StringBuilder();
        int groupCount = 0;

        for (int i = s.length() - 1; i >= 0; i--) {
            char ch = s.charAt(i);

            if (ch == '-') {
                continue;
            }

            if (groupCount == k) {
                result.append('-');
                groupCount = 0;
            }

            result.append(Character.toUpperCase(ch));
            groupCount++;
        }

        return result.reverse().toString();
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

