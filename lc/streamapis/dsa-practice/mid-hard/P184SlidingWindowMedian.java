import java.util.*;

/**
 * P184. Sliding Window Median. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Read the full input from the method
 * parameters, choose the expected
 * optimal data structure or algorithm, handle edge cases such as empty inputs
 * and duplicates, and
 * return the exact platform-style output.
 */
public final class P184SlidingWindowMedian {

    private P184SlidingWindowMedian() {
    }

    public double[] medianSlidingWindow(int[] nums, int k) {
        double[] ans = new double[nums.length - k + 1];
        for (int i = 0; i < ans.length; i++) {
            int[] win = Arrays.copyOfRange(nums, i, i + k);
            Arrays.sort(win);
            ans[i] = k % 2 == 1 ? win[k / 2] : ((double) win[k / 2 - 1] + win[k / 2]) / 2.0;
        }
        return ans;
    }
}
