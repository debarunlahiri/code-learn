import java.util.*;

/**
 * P191. Design Add And Search Words. This is a mid-to-hard Java DSA coding
 * problem commonly seen in
 * service based company technical rounds. Read the full input from the method
 * parameters, choose the
 * expected optimal data structure or algorithm, handle edge cases such as empty
 * inputs and duplicates,
 * and return the exact platform-style output.
 */
public final class P191DesignAddAndSearchWords {

    private P191DesignAddAndSearchWords() {
    }

    static class Trie {
        Trie[] child = new Trie[26];
        boolean end;
    }

    private final Trie root = new Trie();

    public void addWord(String word) {
        Trie cur = root;
        for (char c : word.toCharArray()) {
            int i = c - 'a';
            if (cur.child[i] == null)
                cur.child[i] = new Trie();
            cur = cur.child[i];
        }
        cur.end = true;
    }

    public boolean search(String word) {
        return dfs(word, 0, root);
    }

    private boolean dfs(String w, int i, Trie node) {
        if (node == null)
            return false;
        if (i == w.length())
            return node.end;
        char c = w.charAt(i);
        if (c == '.') {
            for (Trie next : node.child)
                if (dfs(w, i + 1, next))
                    return true;
            return false;
        }
        return dfs(w, i + 1, node.child[c - 'a']);
    }
}
