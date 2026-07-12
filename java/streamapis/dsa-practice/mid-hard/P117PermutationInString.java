import java.util.*;

/**
 * P117. Permutation In String. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Implement the required method using an
 * efficient algorithm, not
 * brute force where a better standard approach exists. The solution should
 * handle boundary cases,
 * duplicate values, disconnected states, and large inputs according to the
 * method signature. Return
 * the final computed value or data structure exactly as the platform-style
 * method expects.
 */
public final class P117PermutationInString {

    private P117PermutationInString() {
    }

    public boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length())
            return false;
        int[] need = new int[26], win = new int[26];
        for (char c : s1.toCharArray())
            need[c - 'a']++;
        for (int i = 0; i < s2.length(); i++) {
            win[s2.charAt(i) - 'a']++;
            if (i >= s1.length())
                win[s2.charAt(i - s1.length()) - 'a']--;
            if (Arrays.equals(need, win))
                return true;
        }
        return false;
    }
}
