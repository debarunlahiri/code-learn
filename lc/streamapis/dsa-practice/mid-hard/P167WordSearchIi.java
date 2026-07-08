import java.util.*;

/**
 * P167. Word Search II. This is a mid-to-hard Java DSA coding problem commonly
 * seen in service based
 * company technical rounds. Read the full input from the method parameters,
 * choose the expected
 * optimal data structure or algorithm, handle edge cases such as empty inputs
 * and duplicates, and
 * return the exact platform-style output.
 */
public final class P167WordSearchIi {

    private P167WordSearchIi() {
    }

    static class Trie {
        Trie[] next = new Trie[26];
        String word;
    }

    public List<String> findWords(char[][] board, String[] words) {
        Trie root = new Trie();
        for (String w : words) {
            Trie cur = root;
            for (char c : w.toCharArray()) {
                int i = c - 'a';
                if (cur.next[i] == null)
                    cur.next[i] = new Trie();
                cur = cur.next[i];
            }
            cur.word = w;
        }
        List<String> ans = new ArrayList<>();
        for (int r = 0; r < board.length; r++)
            for (int c = 0; c < board[0].length; c++)
                dfs(board, r, c, root, ans);
        return ans;
    }

    private void dfs(char[][] b, int r, int c, Trie node, List<String> ans) {
        if (r < 0 || c < 0 || r == b.length || c == b[0].length || b[r][c] == '#')
            return;
        char ch = b[r][c];
        Trie next = node.next[ch - 'a'];
        if (next == null)
            return;
        if (next.word != null) {
            ans.add(next.word);
            next.word = null;
        }
        b[r][c] = '#';
        dfs(b, r + 1, c, next, ans);
        dfs(b, r - 1, c, next, ans);
        dfs(b, r, c + 1, next, ans);
        dfs(b, r, c - 1, next, ans);
        b[r][c] = ch;
    }
}
