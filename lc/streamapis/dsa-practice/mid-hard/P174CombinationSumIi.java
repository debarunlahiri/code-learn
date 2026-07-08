import java.util.*;

/**
 * P174. Combination Sum II. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Read the full input from the method
 * parameters, choose the expected
 * optimal data structure or algorithm, handle edge cases such as empty inputs
 * and duplicates, and
 * return the exact platform-style output.
 */
public final class P174CombinationSumIi {

    private P174CombinationSumIi() {
    }

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> ans = new ArrayList<>();
        dfs(candidates, target, 0, new ArrayList<>(), ans);
        return ans;
    }

    private void dfs(int[] a, int remain, int start, List<Integer> path, List<List<Integer>> ans) {
        if (remain == 0) {
            ans.add(new ArrayList<>(path));
            return;
        }
        for (int i = start; i < a.length && a[i] <= remain; i++) {
            if (i > start && a[i] == a[i - 1])
                continue;
            path.add(a[i]);
            dfs(a, remain - a[i], i + 1, path, ans);
            path.remove(path.size() - 1);
        }
    }
}
