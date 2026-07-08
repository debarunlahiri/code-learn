import java.util.*;

/**
 * P091. Merge Intervals. This is a easy-to-mid Java DSA coding problem commonly
 * practiced for service
 * based company coding rounds. Given the input described by the method
 * signature, implement the
 * required operation efficiently and return the expected result. Handle normal
 * edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P091MergeIntervals {

    private P091MergeIntervals() {
    }

    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        List<int[]> ans = new ArrayList<>();
        for (int[] in : intervals) {
    if (ans.isEmpty() || ans.get(ans.size() - 1)[1] < in[0])
        ans.add(in);
    else
        ans.get(ans.size() - 1)[1] = Math.max(ans.get(ans.size() - 1)[1], in[1]);
        }
        return ans.toArray(new int[ans.size()][]);
    }

}
