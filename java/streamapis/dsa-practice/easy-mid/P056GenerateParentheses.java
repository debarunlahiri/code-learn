import java.util.*;

/**
 * P056. Generate Parentheses. Given the input described by the method
 * signature, implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P056GenerateParentheses {

    private P056GenerateParentheses() {
    }

    public List<String> generateParenthesis(int n) {
        List<String> ans = new ArrayList<>();
        backtrackParenthesis(ans, new StringBuilder(), 0, 0, n);
        return ans;
    }

    private void backtrackParenthesis(List<String> ans, StringBuilder path, int open, int close, int n) {
        if (path.length() == 2 * n) {
    ans.add(path.toString());
    return;
        }
        if (open < n) {
    path.append('(');
    backtrackParenthesis(ans, path, open + 1, close, n);
    path.deleteCharAt(path.length() - 1);
        }
        if (close < open) {
    path.append(')');
    backtrackParenthesis(ans, path, open, close + 1, n);
    path.deleteCharAt(path.length() - 1);
        }
    }

}
