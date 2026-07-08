import java.util.*;

/**
 * P116. Longest Repeating Character Replacement. This is a mid-to-hard Java DSA
 * coding problem
 * commonly seen in service based company technical rounds. Implement the
 * required method using an
 * efficient algorithm, not brute force where a better standard approach exists.
 * The solution should
 * handle boundary cases, duplicate values, disconnected states, and large
 * inputs according to the
 * method signature. Return the final computed value or data structure exactly
 * as the platform-style
 * method expects.
 */
public final class P116LongestRepeatingCharacterReplacement {

    private P116LongestRepeatingCharacterReplacement() {
    }

    public int characterReplacement(String s, int k) {
        int[] count = new int[26];
        int left = 0, maxFreq = 0, best = 0;
        for (int right = 0; right < s.length(); right++) {
            maxFreq = Math.max(maxFreq, ++count[s.charAt(right) - 'A']);
            while (right - left + 1 - maxFreq > k)
                count[s.charAt(left++) - 'A']--;
            best = Math.max(best, right - left + 1);
        }
        return best;
    }
}
