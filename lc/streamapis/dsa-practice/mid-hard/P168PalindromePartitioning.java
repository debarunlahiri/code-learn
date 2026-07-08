import java.util.*;

/**
 * P168. Palindrome Partitioning. This is a mid-to-hard Java DSA coding problem
 * commonly seen in
 * service based company technical rounds. Read the full input from the method
 * parameters, choose the
 * expected optimal data structure or algorithm, handle edge cases such as empty
 * inputs and duplicates,
 * and return the exact platform-style output.
 */
public final class P168PalindromePartitioning {

    private P168PalindromePartitioning() {
    }

    public List<List<String>> partition(String s) {
        List<List<String>> ans = new ArrayList<>();
        backtrack(s, 0, new ArrayList<>(), ans);
        return ans;
    }

    private void backtrack(String s, int start, List<String> path, List<List<String>> ans) {
        if (start == s.length()) {
            ans.add(new ArrayList<>(path));
            return;
        }
        for (int end = start; end < s.length(); end++)
            if (pal(s, start, end)) {
                path.add(s.substring(start, end + 1));
                backtrack(s, end + 1, path, ans);
                path.remove(path.size() - 1);
            }
    }

    private boolean pal(String s, int l, int r) {
        while (l < r)
            if (s.charAt(l++) != s.charAt(r--))
                return false;
        return true;
    }
}
