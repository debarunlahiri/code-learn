# 072. Reverse Words in a String III

Platform: LeetCode  
Difficulty: Easy  
Topic: String

## Problem Statement

Given a string `s`, reverse the characters of every word while keeping the word order and spaces unchanged.

## Constraints

- `1 <= s.length <= 5 * 10^4`
- `s` contains printable ASCII characters.
- Words are separated by a single space.

## Example

Input:

```text
s = "Let's take LeetCode contest"
```

Output:

```text
"s'teL ekat edoCteeL tsetnoc"
```

## Brute Force Approach

Split the sentence into words, reverse each word, and join them again.

```java
class Solution {
    public String reverseWords(String s) {
        String[] words = s.split(" ");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            result.append(new StringBuilder(words[i]).reverse());

            if (i + 1 < words.length) {
                result.append(' ');
            }
        }

        return result.toString();
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Use a character array and reverse each word range in-place.

```java
class Solution {
    public String reverseWords(String s) {
        char[] chars = s.toCharArray();
        int start = 0;

        for (int end = 0; end <= chars.length; end++) {
            if (end == chars.length || chars[end] == ' ') {
                reverse(chars, start, end - 1);
                start = end + 1;
            }
        }

        return new String(chars);
    }

    private void reverse(char[] chars, int left, int right) {
        while (left < right) {
            char temp = chars[left];
            chars[left] = chars[right];
            chars[right] = temp;
            left++;
            right--;
        }
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)` because Java strings are immutable and need a character array.

