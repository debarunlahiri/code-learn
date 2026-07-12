import java.util.*;

/**
 * P098. Partition Labels. This is a easy-to-mid Java DSA coding problem
 * commonly practiced for service
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
public final class P098PartitionLabels {

    private P098PartitionLabels() {
    }

    public List<Integer> partitionLabels(String s) {
        int[] last = new int[26];
        for (int i = 0; i < s.length(); i++)
    last[s.charAt(i) - 'a'] = i;
        List<Integer> ans = new ArrayList<>();
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
    end = Math.max(end, last[s.charAt(i) - 'a']);
    if (i == end) {
        ans.add(end - start + 1);
        start = i + 1;
    }
        }
        return ans;
    }

}
