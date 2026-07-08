import java.util.*;

/**
 * P153. Accounts Merge. This is a mid-to-hard Java DSA coding problem commonly
 * seen in service based
 * company technical rounds. Read the input represented by the method
 * parameters, apply the standard
 * efficient approach for this topic, and return the exact result requested.
 * Handle empty inputs,
 * duplicate values, boundary indexes, and large constraints in a clean Java
 * implementation.
 */
public final class P153AccountsMerge {

    private P153AccountsMerge() {
    }

    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        Map<String, String> parent = new HashMap<>(), owner = new HashMap<>();
        for (List<String> a : accounts)
            for (int i = 1; i < a.size(); i++) {
                parent.putIfAbsent(a.get(i), a.get(i));
                owner.put(a.get(i), a.get(0));
                union(parent, a.get(1), a.get(i));
            }
        Map<String, TreeSet<String>> groups = new HashMap<>();
        for (String email : parent.keySet())
            groups.computeIfAbsent(find(parent, email), k -> new TreeSet<>()).add(email);
        List<List<String>> ans = new ArrayList<>();
        for (var e : groups.values()) {
            List<String> row = new ArrayList<>();
            row.add(owner.get(e.first()));
            row.addAll(e);
            ans.add(row);
        }
        return ans;
    }

    private String find(Map<String, String> p, String x) {
        if (!p.get(x).equals(x))
            p.put(x, find(p, p.get(x)));
        return p.get(x);
    }

    private void union(Map<String, String> p, String a, String b) {
        p.put(find(p, a), find(p, b));
    }
}
