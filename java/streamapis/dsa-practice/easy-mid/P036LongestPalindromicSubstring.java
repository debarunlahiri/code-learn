import java.util.*;

/**
 * P036. Longest Palindromic Substring. This is a easy-to-mid Java DSA coding
 * problem commonly
 * practiced for service based company coding rounds. Given the input described
 * by the method
 * signature, implement the required operation efficiently and return the
 * expected result. Handle
 * normal edge cases such as empty collections, duplicate values, boundary
 * indexes, and null child
 * pointers when the data structure allows them. Prefer the standard optimal
 * approach used in coding
 * rounds, and keep the implementation readable for revision.
 */
public final class P036LongestPalindromicSubstring {

    private P036LongestPalindromicSubstring() {
    }

    public String longestPalindrome(String s) {
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
    int a = expand(s, i, i), b = expand(s, i, i + 1), len = Math.max(a, b);
    if (len > end - start + 1) {
        start = i - (len - 1) / 2;
        end = i + len / 2;
    }
        }
        return s.substring(start, end + 1);
    }

    private int expand(String s, int l, int r) {
        while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
    l--;
    r++;
        }
        return r - l - 1;
    }

}
