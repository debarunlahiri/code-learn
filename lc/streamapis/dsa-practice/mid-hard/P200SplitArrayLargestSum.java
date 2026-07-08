import java.util.*;

/**
 * P200. Split Array Largest Sum. This is a mid-to-hard Java DSA coding problem
 * commonly seen in
 * service based company technical rounds. Read the full input from the method
 * parameters, choose the
 * expected optimal data structure or algorithm, handle edge cases such as empty
 * inputs and duplicates,
 * and return the exact platform-style output.
 */
public final class P200SplitArrayLargestSum {

    private P200SplitArrayLargestSum() {
    }

    public int splitArray(int[] nums, int k) {
        int l = Arrays.stream(nums).max().orElse(0), r = Arrays.stream(nums).sum();
        while (l < r) {
            int m = l + (r - l) / 2;
            if (parts(nums, m) <= k)
                r = m;
            else
                l = m + 1;
        }
        return l;
    }

    private int parts(int[] nums, int max) {
        int count = 1, sum = 0;
        for (int n : nums) {
            if (sum + n > max) {
                count++;
                sum = 0;
            }
            sum += n;
        }
        return count;
    }
}
