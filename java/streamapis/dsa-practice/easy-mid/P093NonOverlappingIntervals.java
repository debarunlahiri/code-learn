import java.util.*;

/**
 * P093. Non Overlapping Intervals. This is a easy-to-mid Java DSA coding
 * problem commonly practiced
 * for service based company coding rounds. Given the input described by the
 * method signature,
 * implement the required operation efficiently and return the expected result.
 * Handle normal edge
 * cases such as empty collections, duplicate values, boundary indexes, and null
 * child pointers when
 * the data structure allows them. Prefer the standard optimal approach used in
 * coding rounds, and keep
 * the implementation readable for revision.
 */
public final class P093NonOverlappingIntervals {

    private P093NonOverlappingIntervals() {
    }

    public int eraseOverlapIntervals(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[1]));
        int removed = 0, end = Integer.MIN_VALUE;
        for (int[] in : intervals) {
    if (in[0] >= end)
        end = in[1];
    else
        removed++;
        }
        return removed;
    }

}
