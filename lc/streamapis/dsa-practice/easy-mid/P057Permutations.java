import java.util.*;

/**
 * P057. Permutations.  Given the input described by the method
 * signature, implement the
 * required operation efficiently and return the expected result. Handle normal
 * edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P057Permutations {

    private P057Permutations() {
    }

    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        backtrackPermute(nums, new boolean[nums.length], new ArrayList<>(), ans);
        return ans;
    }

    private void backtrackPermute(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> ans) {
        if (path.size() == nums.length) {
    ans.add(new ArrayList<>(path));
    return;
        }
        for (int i = 0; i < nums.length; i++)
    if (!used[i]) {
        used[i] = true;
        path.add(nums[i]);
        backtrackPermute(nums, used, path, ans);
        path.remove(path.size() - 1);
        used[i] = false;
    }
    }

}
