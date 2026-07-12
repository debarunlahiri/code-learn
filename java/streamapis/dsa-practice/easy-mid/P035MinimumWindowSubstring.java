import java.util.*;

/**
 * P035. Minimum Window Substring. Given the input described by the method
 * signature, implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P035MinimumWindowSubstring {

    private P035MinimumWindowSubstring() {
    }

    public String minWindow(String s, String t) {
        int[] need = new int[128];
        for (char c : t.toCharArray())
    need[c]++;
        int missing = t.length(), left = 0, start = 0, len = Integer.MAX_VALUE;
        for (int right = 0; right < s.length(); right++) {
    if (need[s.charAt(right)]-- > 0)
        missing--;
    while (missing == 0) {
        if (right - left + 1 < len) {
            start = left;
            len = right - left + 1;
        }
        if (++need[s.charAt(left++)] > 0)
            missing++;
    }
        }
        return len == Integer.MAX_VALUE ? "" : s.substring(start, start + len);
    }

}
