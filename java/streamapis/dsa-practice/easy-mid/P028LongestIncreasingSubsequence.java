import java.util.*;

/**
 * P028. Longest Increasing Subsequence. This is a easy-to-mid Java DSA coding
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
public final class P028LongestIncreasingSubsequence {

    private P028LongestIncreasingSubsequence() {
    }

    public int lengthOfLIS(int[] nums) {
        int[] tails = new int[nums.length];
        int size = 0;
        for (int n : nums) {
    int i = Arrays.binarySearch(tails, 0, size, n);
    if (i < 0)
        i = -(i + 1);
    tails[i] = n;
    if (i == size)
        size++;
        }
        return size;
    }

}
