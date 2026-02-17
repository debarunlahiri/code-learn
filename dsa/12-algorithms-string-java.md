# Algorithms: String (Easy to Hard)

Goal: Master string processing algorithms with clear explanations and practical Java code.

---

## 1. KMP Pattern Search

### What it does
Find all occurrences of a pattern in text using Knuth-Morris-Pratt algorithm.

### Why it matters
- Linear time pattern matching (O(n+m))
- Avoids re-examining characters
- Foundation for text editors, search engines
- Better than naive O(n*m) approach

### Intuition
When mismatch occurs, instead of starting over, use previously computed information about the pattern to skip ahead. Like recognizing a word you've partially seen before.

### When to use
- Pattern searching in large texts
- Text editors' find functionality
- Bioinformatics (DNA sequence matching)
- Plagiarism detection

### Time complexity
- Preprocessing (LPS array): `O(m)` where m = pattern length
- Searching: `O(n)` where n = text length
- Space: `O(m)`

### Edge cases
- Empty pattern
- Pattern longer than text
- Multiple overlapping matches
- All characters same

### Java code
```java
import java.util.ArrayList;
import java.util.List;

public class KMP {
    static List<Integer> search(String text, String pattern) {
        List<Integer> ans = new ArrayList<>();
        if (pattern.isEmpty()) return ans;

        int[] lps = buildLPS(pattern);
        int i = 0, j = 0; // i: text index, j: pattern index

        while (i < text.length()) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == pattern.length()) {
                    ans.add(i - j); // Pattern found at i-j
                    j = lps[j - 1];  // Continue searching
                }
            } else {
                if (j > 0) j = lps[j - 1];
                else i++;
            }
        }
        return ans;
    }

    // Build LPS (Longest Prefix Suffix) array
    static int[] buildLPS(String p) {
        int[] lps = new int[p.length()];
        int len = 0, i = 1; // len: length of previous longest prefix suffix

        while (i < p.length()) {
            if (p.charAt(i) == p.charAt(len)) {
                lps[i++] = ++len;
            } else if (len > 0) {
                len = lps[len - 1];
            } else {
                lps[i++] = 0;
            }
        }
        return lps;
    }
}
```

---

## 2. Rabin-Karp (rolling hash)

### What it does
Find pattern occurrences using rolling hash technique for efficient string matching.

### Why it matters
- Average case linear time
- Good for multiple pattern searches
- Used in plagiarism detection
- Foundation for rolling hash problems

### Intuition
Think of each string as a number in base-256. Instead of comparing entire strings, compare their hash values. When sliding window, update hash efficiently (rolling hash).

### When to use
- Multiple pattern searches
- Finding similar strings
- Plagiarism detection
- When average performance is acceptable

### Time complexity
- Average: `O(n + m)`
- Worst case: `O(n * m)` (hash collisions)
- Space: `O(1)`

### Edge cases
- Hash collisions (need double hashing or verification)
- Large strings (use modulo to prevent overflow)
- Unicode characters

### Java code
```java
import java.util.ArrayList;
import java.util.List;

public class RabinKarp {
    static List<Integer> search(String text, String pattern) {
        List<Integer> ans = new ArrayList<>();
        int n = text.length(), m = pattern.length();
        if (m > n) return ans;

        int base = 256, mod = 1_000_000_007; // Large prime to avoid overflow
        long pHash = 0, tHash = 0, pow = 1;

        // Compute base^(m-1) % mod for rolling
        for (int i = 0; i < m - 1; i++) pow = (pow * base) % mod;

        // Compute initial hashes
        for (int i = 0; i < m; i++) {
            pHash = (pHash * base + pattern.charAt(i)) % mod;
            tHash = (tHash * base + text.charAt(i)) % mod;
        }

        // Slide the pattern over text
        for (int i = 0; i <= n - m; i++) {
            if (pHash == tHash && text.substring(i, i + m).equals(pattern)) {
                ans.add(i);
            }
            if (i < n - m) {
                // Remove leading char and add trailing char
                tHash = (tHash - text.charAt(i) * pow) % mod;
                if (tHash < 0) tHash += mod;
                tHash = (tHash * base + text.charAt(i + m)) % mod;
            }
        }
        return ans;
    }
}
```

---

## 3. Longest Substring Without Repeating Characters

### What it does
Find length of longest substring with all unique characters.

### Why it matters
- Classic sliding window problem
- Tests understanding of hash maps and two pointers
- Used in text processing, DNA analysis
- Common interview question

### Intuition
Maintain a window with unique characters. When duplicate found, shrink window from left until all characters are unique again.

### When to use
- Finding unique sequences
- String deduplication
- Pattern matching with uniqueness constraint

### Time complexity
- Time: `O(n)`
- Space: `O(min(n, alphabet size))`

### Edge cases
- Empty string
- All characters unique
- All characters same
- Unicode characters

### Java code
```java
import java.util.HashMap;
import java.util.Map;

public class LongestUniqueSubstring {
    static int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) return 0;

        Map<Character, Integer> lastSeen = new HashMap<>();
        int maxLen = 0, start = 0;

        for (int end = 0; end < s.length(); end++) {
            char c = s.charAt(end);
            if (lastSeen.containsKey(c) && lastSeen.get(c) >= start) {
                start = lastSeen.get(c) + 1;
            }
            lastSeen.put(c, end);
            maxLen = Math.max(maxLen, end - start + 1);
        }
        return maxLen;
    }
}
```

---

## 4. Valid Anagram

### What it does
Check if two strings are anagrams (contain same characters with same frequency).

### Why it matters
- String manipulation basics
- Frequency counting
- Used in word games, cryptography
- Tests array/hash map skills

### Intuition
Two strings are anagrams if they have the same character frequency count. Like rearranging letters of one word to form another.

### When to use
- Word games (Scrabble, crosswords)
- Checking string permutations
- Security (anagram-based ciphers)

### Time complexity
- Time: `O(n)` where n = length of strings
- Space: `O(1)` (fixed array for ASCII) or `O(k)` for Unicode

### Edge cases
- Different length strings
- Empty strings
- Unicode characters
- Case sensitivity

### Java code
```java
public class ValidAnagram {
    static boolean isAnagram(String s, String t) {
        if (s == null || t == null) return s == t;
        if (s.length() != t.length()) return false;

        int[] freq = new int[26]; // Assuming lowercase English letters
        for (int i = 0; i < s.length(); i++) {
            freq[s.charAt(i) - 'a']++;
            freq[t.charAt(i) - 'a']--;
        }

        for (int count : freq) {
            if (count != 0) return false;
        }
        return true;
    }

    // For Unicode/General case
    static boolean isAnagramUnicode(String s, String t) {
        if (s.length() != t.length()) return false;

        int[] freq = new int[256]; // Extended ASCII
        for (int i = 0; i < s.length(); i++) {
            freq[s.charAt(i)]++;
            freq[t.charAt(i)]--;
        }

        for (int count : freq) {
            if (count != 0) return false;
        }
        return true;
    }
}
```

---

## 5. String to Integer (atoi)

### What it does
Convert string representation of integer to actual integer, handling edge cases.

### Why it matters
- String parsing fundamentals
- Edge case handling
- Used in parsers, interpreters
- Classic interview problem

### Intuition
Carefully parse string character by character, handling optional sign, whitespace, digits, and overflow.

### When to use
- Input validation
- Converting user input to numbers
- File parsing
- Network protocol implementation

### Time complexity
- Time: `O(n)`
- Space: `O(1)`

### Edge cases
- Leading/trailing whitespace
- Optional +/- sign
- Non-digit characters
- Integer overflow
- Empty string

### Java code
```java
public class StringToInteger {
    static int myAtoi(String s) {
        if (s == null || s.length() == 0) return 0;

        int i = 0, n = s.length();
        // Skip leading whitespace
        while (i < n && s.charAt(i) == ' ') i++;

        // Handle optional sign
        int sign = 1;
        if (i < n && (s.charAt(i) == '+' || s.charAt(i) == '-')) {
            sign = s.charAt(i) == '-' ? -1 : 1;
            i++;
        }

        // Convert digits
        long result = 0;
        while (i < n && Character.isDigit(s.charAt(i))) {
            result = result * 10 + (s.charAt(i) - '0');
            i++;
            
            // Check for overflow
            if (result * sign > Integer.MAX_VALUE) return Integer.MAX_VALUE;
            if (result * sign < Integer.MIN_VALUE) return Integer.MIN_VALUE;
        }

        return (int) (result * sign);
    }
}
```

## 3. Z-Algorithm
```java
public class ZAlgorithm {
    static int[] zFunction(String s) {
        int n = s.length();
        int[] z = new int[n];
        int l = 0, r = 0;

        for (int i = 1; i < n; i++) {
            if (i <= r) z[i] = Math.min(r - i + 1, z[i - l]);
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) z[i]++;
            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }
        return z;
    }
}
```

## 4. Manacher (Longest palindromic substring in O(n))
```java
public class Manacher {
    static String longestPalindrome(String s) {
        StringBuilder t = new StringBuilder("^");
        for (char c : s.toCharArray()) t.append("#").append(c);
        t.append("#$");

        int n = t.length();
        int[] p = new int[n];
        int center = 0, right = 0, bestCenter = 0, bestLen = 0;

        for (int i = 1; i < n - 1; i++) {
            int mirror = 2 * center - i;
            if (i < right) p[i] = Math.min(right - i, p[mirror]);

            while (t.charAt(i + p[i] + 1) == t.charAt(i - p[i] - 1)) p[i]++;

            if (i + p[i] > right) {
                center = i;
                right = i + p[i];
            }
            if (p[i] > bestLen) {
                bestLen = p[i];
                bestCenter = i;
            }
        }

        int start = (bestCenter - bestLen) / 2;
        return s.substring(start, start + bestLen);
    }
}
```

