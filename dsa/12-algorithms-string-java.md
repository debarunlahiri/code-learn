# Algorithms: String (Easy to Hard)

## 1. KMP Pattern Search
```java
import java.util.ArrayList;
import java.util.List;

public class KMP {
    static List<Integer> search(String text, String pattern) {
        List<Integer> ans = new ArrayList<>();
        if (pattern.isEmpty()) return ans;

        int[] lps = buildLPS(pattern);
        int i = 0, j = 0;

        while (i < text.length()) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == pattern.length()) {
                    ans.add(i - j);
                    j = lps[j - 1];
                }
            } else {
                if (j > 0) j = lps[j - 1];
                else i++;
            }
        }
        return ans;
    }

    static int[] buildLPS(String p) {
        int[] lps = new int[p.length()];
        int len = 0, i = 1;
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

## 2. Rabin-Karp (rolling hash)
```java
import java.util.ArrayList;
import java.util.List;

public class RabinKarp {
    static List<Integer> search(String text, String pattern) {
        List<Integer> ans = new ArrayList<>();
        int n = text.length(), m = pattern.length();
        if (m > n) return ans;

        int base = 256, mod = 1_000_000_007;
        long pHash = 0, tHash = 0, pow = 1;

        for (int i = 0; i < m - 1; i++) pow = (pow * base) % mod;
        for (int i = 0; i < m; i++) {
            pHash = (pHash * base + pattern.charAt(i)) % mod;
            tHash = (tHash * base + text.charAt(i)) % mod;
        }

        for (int i = 0; i <= n - m; i++) {
            if (pHash == tHash && text.substring(i, i + m).equals(pattern)) ans.add(i);
            if (i < n - m) {
                tHash = (tHash - text.charAt(i) * pow) % mod;
                if (tHash < 0) tHash += mod;
                tHash = (tHash * base + text.charAt(i + m)) % mod;
            }
        }
        return ans;
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

