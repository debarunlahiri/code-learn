import java.util.*;

/**
 * P108. Koko Eating Bananas. This is a mid-to-hard Java DSA coding problem
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
public final class P108KokoEatingBananas {

    private P108KokoEatingBananas() {
    }

    public int minEatingSpeed(int[] piles, int h) {
        int l = 1, r = Arrays.stream(piles).max().orElse(1);
        while (l < r) {
            int m = l + (r - l) / 2;
            long hours = 0;
            for (int p : piles)
                hours += (p + m - 1) / m;
            if (hours <= h)
                r = m;
            else
                l = m + 1;
        }
        return l;
    }
}
