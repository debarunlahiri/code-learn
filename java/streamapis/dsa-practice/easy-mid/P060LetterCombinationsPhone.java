import java.util.*;

/**
 * P060. Letter Combinations Phone. This is a easy-to-mid Java DSA coding
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
public final class P060LetterCombinationsPhone {

    private P060LetterCombinationsPhone() {
    }

    public List<String> letterCombinations(String digits) {
        if (digits.isEmpty())
    return List.of();
        String[] map = { "", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz" };
        List<String> ans = new ArrayList<>();
        backtrackPhone(digits, 0, new StringBuilder(), map, ans);
        return ans;
    }

    private void backtrackPhone(String digits, int index, StringBuilder path, String[] map, List<String> ans) {
        if (index == digits.length()) {
    ans.add(path.toString());
    return;
        }
        for (char c : map[digits.charAt(index) - '0'].toCharArray()) {
    path.append(c);
    backtrackPhone(digits, index + 1, path, map, ans);
    path.deleteCharAt(path.length() - 1);
        }
    }

}
