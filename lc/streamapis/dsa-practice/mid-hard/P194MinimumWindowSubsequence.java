import java.util.*;

/**
 * P194. Minimum Window Subsequence. This is a mid-to-hard Java DSA coding
 * problem commonly seen in
 * service based company technical rounds. Read the full input from the method
 * parameters, choose the
 * expected optimal data structure or algorithm, handle edge cases such as empty
 * inputs and duplicates,
 * and return the exact platform-style output.
 */
public final class P194MinimumWindowSubsequence {

    private P194MinimumWindowSubsequence() {
    }

    public String minWindow(String s1, String s2) {
        String best = "";
        int n = s1.length();
        for (int i = 0; i < n; i++) {
            int j = 0, k = i;
            while (k < n && j < s2.length())
                if (s1.charAt(k++) == s2.charAt(j))
                    j++;
            if (j == s2.length()) {
                int end = k;
                j--;
                k--;
                while (j >= 0)
                    if (s1.charAt(k--) == s2.charAt(j))
                        j--;
                String cand = s1.substring(k + 1, end);
                if (best.isEmpty() || cand.length() < best.length())
                    best = cand;
            }
        }
        return best;
    }
}
