import java.util.*;

/**
 * P171. Expression Add Operators. This is a mid-to-hard Java DSA coding problem
 * commonly seen in
 * service based company technical rounds. Read the full input from the method
 * parameters, choose the
 * expected optimal data structure or algorithm, handle edge cases such as empty
 * inputs and duplicates,
 * and return the exact platform-style output.
 */
public final class P171ExpressionAddOperators {

    private P171ExpressionAddOperators() {
    }

    public List<String> addOperators(String num, int target) {
        List<String> ans = new ArrayList<>();
        dfs(num, target, 0, 0, 0, new StringBuilder(), ans);
        return ans;
    }

    private void dfs(String s, int target, int pos, long value, long prev, StringBuilder path, List<String> ans) {
        if (pos == s.length()) {
            if (value == target)
                ans.add(path.toString());
            return;
        }
        int len = path.length();
        for (int i = pos; i < s.length(); i++) {
            if (i > pos && s.charAt(pos) == '0')
                break;
            long cur = Long.parseLong(s.substring(pos, i + 1));
            if (pos == 0) {
                path.append(cur);
                dfs(s, target, i + 1, cur, cur, path, ans);
                path.setLength(len);
            } else {
                path.append('+').append(cur);
                dfs(s, target, i + 1, value + cur, cur, path, ans);
                path.setLength(len);
                path.append('-').append(cur);
                dfs(s, target, i + 1, value - cur, -cur, path, ans);
                path.setLength(len);
                path.append('*').append(cur);
                dfs(s, target, i + 1, value - prev + prev * cur, prev * cur, path, ans);
                path.setLength(len);
            }
        }
    }
}
