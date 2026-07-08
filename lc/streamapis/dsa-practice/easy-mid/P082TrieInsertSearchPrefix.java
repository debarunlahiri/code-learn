import java.util.*;

/**
 * P082. Trie Insert Search Prefix. This is a easy-to-mid Java DSA coding
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
public final class P082TrieInsertSearchPrefix {

    private P082TrieInsertSearchPrefix() {
    }

    class Trie {
        private final Trie[] child = new Trie[26];
        private boolean end;

        public void insert(String word) {
    Trie cur = this;
    for (char ch : word.toCharArray()) {
        int i = ch - 'a';
        if (cur.child[i] == null)
            cur.child[i] = new Trie();
        cur = cur.child[i];
    }
    cur.end = true;
        }

        public boolean search(String word) {
    Trie node = find(word);
    return node != null && node.end;
        }

        public boolean startsWith(String prefix) {
    return find(prefix) != null;
        }

        private Trie find(String s) {
    Trie cur = this;
    for (char ch : s.toCharArray()) {
        int i = ch - 'a';
        if (cur.child[i] == null)
            return null;
        cur = cur.child[i];
    }
    return cur;
        }
    }

}
