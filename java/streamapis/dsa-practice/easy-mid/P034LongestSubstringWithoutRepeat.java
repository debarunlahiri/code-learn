import java.util.*;

/**
 * P034. Longest Substring Without Repeat. This is a easy-to-mid Java DSA coding
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
public final class P034LongestSubstringWithoutRepeat {

    private P034LongestSubstringWithoutRepeat() {
    }

    public int lengthOfLongestSubstring(String s) {
        int[] last = new int[128];
        Arrays.fill(last, -1);
        int left = 0, best = 0;
        for (int right = 0; right < s.length(); right++) {
    left = Math.max(left, last[s.charAt(right)] + 1);
    last[s.charAt(right)] = right;
    best = Math.max(best, right - left + 1);
        }
        return best;
    }

}
