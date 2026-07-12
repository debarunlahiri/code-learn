import java.util.*;

/**
 * P083. Word Dictionary Wildcard. Given the input described by the method
 * signature, implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P083WordDictionaryWildcard {

    private P083WordDictionaryWildcard() {
    }

    class WordDictionary {
        private final WordDictionary[] child = new WordDictionary[26];
        private boolean end;

        public void addWord(String word) {
    WordDictionary cur = this;
    for (char ch : word.toCharArray()) {
        int i = ch - 'a';
        if (cur.child[i] == null)
            cur.child[i] = new WordDictionary();
        cur = cur.child[i];
    }
    cur.end = true;
        }

        public boolean search(String word) {
    return search(word, 0, this);
        }

        private boolean search(String w, int i, WordDictionary node) {
    if (node == null)
        return false;
    if (i == w.length())
        return node.end;
    char ch = w.charAt(i);
    if (ch == '.') {
        for (WordDictionary next : node.child)
            if (search(w, i + 1, next))
                return true;
        return false;
    }
    return search(w, i + 1, node.child[ch - 'a']);
        }
    }

}
