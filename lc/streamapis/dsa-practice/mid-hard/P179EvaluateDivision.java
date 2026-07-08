import java.util.*;

/**
 * P179. Evaluate Division. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Read the full input from the method
 * parameters, choose the expected
 * optimal data structure or algorithm, handle edge cases such as empty inputs
 * and duplicates, and
 * return the exact platform-style output.
 */
public final class P179EvaluateDivision {

    private P179EvaluateDivision() {
    }

    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        Map<String, Map<String, Double>> g = new HashMap<>();
        for (int i = 0; i < equations.size(); i++) {
            String a = equations.get(i).get(0), b = equations.get(i).get(1);
            g.computeIfAbsent(a, k -> new HashMap<>()).put(b, values[i]);
            g.computeIfAbsent(b, k -> new HashMap<>()).put(a, 1.0 / values[i]);
        }
        double[] ans = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++)
            ans[i] = dfs(g, queries.get(i).get(0), queries.get(i).get(1), new HashSet<>());
        return ans;
    }

    private double dfs(Map<String, Map<String, Double>> g, String a, String b, Set<String> seen) {
        if (!g.containsKey(a))
            return -1;
        if (a.equals(b))
            return 1;
        seen.add(a);
        for (var e : g.get(a).entrySet())
            if (!seen.contains(e.getKey())) {
                double v = dfs(g, e.getKey(), b, seen);
                if (v != -1)
                    return e.getValue() * v;
            }
        return -1;
    }
}
