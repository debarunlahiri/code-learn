import java.util.*;

/**
 * P118. Find All Anagrams In String. This is a mid-to-hard Java DSA coding
 * problem commonly seen in
 * service based company technical rounds. Implement the required method using
 * an efficient algorithm,
 * not brute force where a better standard approach exists. The solution should
 * handle boundary cases,
 * duplicate values, disconnected states, and large inputs according to the
 * method signature. Return
 * the final computed value or data structure exactly as the platform-style
 * method expects.
 */
public final class P118FindAllAnagramsInString {

    private P118FindAllAnagramsInString() {
    }

    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> ans = new ArrayList<>();
        if (p.length() > s.length())
            return ans;
        int[] need = new int[26], win = new int[26];
        for (char c : p.toCharArray())
            need[c - 'a']++;
        for (int i = 0; i < s.length(); i++) {
            win[s.charAt(i) - 'a']++;
            if (i >= p.length())
                win[s.charAt(i - p.length()) - 'a']--;
            if (Arrays.equals(need, win))
                ans.add(i - p.length() + 1);
        }
        return ans;
    }
}
