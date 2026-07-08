import java.util.*;

/**
 * P157. Word Ladder. This is a mid-to-hard Java DSA coding problem commonly
 * seen in service based
 * company technical rounds. Read the input represented by the method
 * parameters, apply the standard
 * efficient approach for this topic, and return the exact result requested.
 * Handle empty inputs,
 * duplicate values, boundary indexes, and large constraints in a clean Java
 * implementation.
 */
public final class P157WordLadder {

    private P157WordLadder() {
    }

    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> dict = new HashSet<>(wordList);
        if (!dict.contains(endWord))
            return 0;
        Queue<String> q = new ArrayDeque<>();
        q.offer(beginWord);
        int steps = 1;
        while (!q.isEmpty()) {
            for (int size = q.size(); size > 0; size--) {
                String w = q.poll();
                if (w.equals(endWord))
                    return steps;
                char[] a = w.toCharArray();
                for (int i = 0; i < a.length; i++) {
                    char old = a[i];
                    for (char c = 'a'; c <= 'z'; c++) {
                        a[i] = c;
                        String next = new String(a);
                        if (dict.remove(next))
                            q.offer(next);
                    }
                    a[i] = old;
                }
            }
            steps++;
        }
        return 0;
    }
}
