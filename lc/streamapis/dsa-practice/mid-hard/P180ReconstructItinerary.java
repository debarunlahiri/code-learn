import java.util.*;

/**
 * P180. Reconstruct Itinerary. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Read the full input from the method
 * parameters, choose the expected
 * optimal data structure or algorithm, handle edge cases such as empty inputs
 * and duplicates, and
 * return the exact platform-style output.
 */
public final class P180ReconstructItinerary {

    private P180ReconstructItinerary() {
    }

    public List<String> findItinerary(List<List<String>> tickets) {
        Map<String, PriorityQueue<String>> g = new HashMap<>();
        for (List<String> t : tickets)
            g.computeIfAbsent(t.get(0), k -> new PriorityQueue<>()).offer(t.get(1));
        LinkedList<String> ans = new LinkedList<>();
        visit("JFK", g, ans);
        return ans;
    }

    private void visit(String u, Map<String, PriorityQueue<String>> g, LinkedList<String> ans) {
        PriorityQueue<String> pq = g.get(u);
        while (pq != null && !pq.isEmpty())
            visit(pq.poll(), g, ans);
        ans.addFirst(u);
    }
}
