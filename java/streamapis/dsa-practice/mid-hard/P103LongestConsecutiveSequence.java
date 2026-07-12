import java.util.*;

/**
 * P103. Longest Consecutive Sequence. This is a mid-to-hard Java DSA coding
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
public final class P103LongestConsecutiveSequence {

    private P103LongestConsecutiveSequence() {
    }

    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int n : nums)
            set.add(n);
        int best = 0;
        for (int n : set)
            if (!set.contains(n - 1)) {
                int cur = n;
                while (set.contains(cur))
                    cur++;
                best = Math.max(best, cur - n);
            }
        return best;
    }
}
