import java.util.*;

/**
 * P186. Reverse Pairs. This is a mid-to-hard Java DSA coding problem commonly
 * seen in service based
 * company technical rounds. Read the full input from the method parameters,
 * choose the expected
 * optimal data structure or algorithm, handle edge cases such as empty inputs
 * and duplicates, and
 * return the exact platform-style output.
 */
public final class P186ReversePairs {

    private P186ReversePairs() {
    }

    public int reversePairs(int[] nums) {
        return merge(nums, 0, nums.length - 1, new int[nums.length]);
    }

    private int merge(int[] a, int l, int r, int[] tmp) {
        if (l >= r)
            return 0;
        int m = (l + r) / 2, c = merge(a, l, m, tmp) + merge(a, m + 1, r, tmp);
        int j = m + 1;
        for (int i = l; i <= m; i++) {
            while (j <= r && (long) a[i] > 2L * a[j])
                j++;
            c += j - m - 1;
        }
        int i = l, k = l;
        j = m + 1;
        while (i <= m || j <= r)
            tmp[k++] = j > r || (i <= m && a[i] <= a[j]) ? a[i++] : a[j++];
        System.arraycopy(tmp, l, a, l, r - l + 1);
        return c;
    }
}
