import java.util.*;

/**
 * P158. Alien Dictionary. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service based
 * company technical rounds. Read the input represented by the method
 * parameters, apply the standard
 * efficient approach for this topic, and return the exact result requested.
 * Handle empty inputs,
 * duplicate values, boundary indexes, and large constraints in a clean Java
 * implementation.
 */
public final class P158AlienDictionary {

    private P158AlienDictionary() {
    }

    public String alienOrder(String[] words) {
        Map<Character, Set<Character>> graph = new HashMap<>();
        Map<Character, Integer> indegree = new HashMap<>();
        for (String w : words)
            for (char c : w.toCharArray()) {
                graph.putIfAbsent(c, new HashSet<>());
                indegree.putIfAbsent(c, 0);
            }
        for (int i = 1; i < words.length; i++) {
            String a = words[i - 1], b = words[i];
            if (a.length() > b.length() && a.startsWith(b))
                return "";
            for (int j = 0; j < Math.min(a.length(), b.length()); j++)
                if (a.charAt(j) != b.charAt(j)) {
                    if (graph.get(a.charAt(j)).add(b.charAt(j)))
                        indegree.merge(b.charAt(j), 1, Integer::sum);
                    break;
                }
        }
        Queue<Character> q = new ArrayDeque<>();
        for (var e : indegree.entrySet())
            if (e.getValue() == 0)
                q.offer(e.getKey());
        StringBuilder ans = new StringBuilder();
        while (!q.isEmpty()) {
            char c = q.poll();
            ans.append(c);
            for (char next : graph.get(c))
                if (indegree.merge(next, -1, Integer::sum) == 0)
                    q.offer(next);
        }
        return ans.length() == indegree.size() ? ans.toString() : "";
    }
}
