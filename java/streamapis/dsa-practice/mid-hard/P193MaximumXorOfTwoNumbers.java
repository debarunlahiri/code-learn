import java.util.*;

/**
 * P193. Maximum XOR Of Two Numbers. This is a mid-to-hard Java DSA coding
 * problem commonly seen in
 * service based company technical rounds. Read the full input from the method
 * parameters, choose the
 * expected optimal data structure or algorithm, handle edge cases such as empty
 * inputs and duplicates,
 * and return the exact platform-style output.
 */
public final class P193MaximumXorOfTwoNumbers {

    private P193MaximumXorOfTwoNumbers() {
    }

    static class Trie {
        Trie[] next = new Trie[2];
    }

    public int findMaximumXOR(int[] nums) {
        Trie root = new Trie();
        for (int n : nums) {
            Trie cur = root;
            for (int i = 31; i >= 0; i--) {
                int b = (n >>> i) & 1;
                if (cur.next[b] == null)
                    cur.next[b] = new Trie();
                cur = cur.next[b];
            }
        }
        int best = 0;
        for (int n : nums) {
            Trie cur = root;
            int val = 0;
            for (int i = 31; i >= 0; i--) {
                int b = (n >>> i) & 1, want = 1 - b;
                if (cur.next[want] != null) {
                    val |= 1 << i;
                    cur = cur.next[want];
                } else
                    cur = cur.next[b];
            }
            best = Math.max(best, val);
        }
        return best;
    }
}
