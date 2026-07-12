import java.util.*;

/**
 * P163. Top K Frequent Words. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Read the full input from the method
 * parameters, choose the expected
 * optimal data structure or algorithm, handle edge cases such as empty inputs
 * and duplicates, and
 * return the exact platform-style output.
 */
public final class P163TopKFrequentWords {

    private P163TopKFrequentWords() {
    }

    public List<String> topKFrequent(String[] words, int k) {
        Map<String, Integer> count = new HashMap<>();
        for (String w : words)
            count.merge(w, 1, Integer::sum);
        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> count.get(a).equals(count.get(b)) ? b.compareTo(a) : count.get(a) - count.get(b));
        for (String w : count.keySet()) {
            pq.offer(w);
            if (pq.size() > k)
                pq.poll();
        }
        LinkedList<String> ans = new LinkedList<>();
        while (!pq.isEmpty())
            ans.addFirst(pq.poll());
        return ans;
    }
}
