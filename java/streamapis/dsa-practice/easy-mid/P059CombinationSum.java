import java.util.*;

/**
 * P059. Combination Sum.  Given the input described by the method
 * signature, implement the
 * required operation efficiently and return the expected result. Handle normal
 * edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P059CombinationSum {

    private P059CombinationSum() {
    }

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> ans = new ArrayList<>();
        backtrackCombination(candidates, target, 0, new ArrayList<>(), ans);
        return ans;
    }

    private void backtrackCombination(int[] a, int remain, int start, List<Integer> path, List<List<Integer>> ans) {
        if (remain == 0) {
    ans.add(new ArrayList<>(path));
    return;
        }
        for (int i = start; i < a.length && a[i] <= remain; i++) {
    path.add(a[i]);
    backtrackCombination(a, remain - a[i], i, path, ans);
    path.remove(path.size() - 1);
        }
    }

}
