import java.util.*;

/**
 * P154. Redundant Connection. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Read the input represented by the method
 * parameters, apply the
 * standard efficient approach for this topic, and return the exact result
 * requested. Handle empty
 * inputs, duplicate values, boundary indexes, and large constraints in a clean
 * Java implementation.
 */
public final class P154RedundantConnection {

    private P154RedundantConnection() {
    }

    public int[] findRedundantConnection(int[][] edges) {
        int[] parent = new int[edges.length + 1];
        for (int i = 1; i < parent.length; i++)
            parent[i] = i;
        for (int[] e : edges)
            if (!union(parent, e[0], e[1]))
                return e;
        return new int[0];
    }

    private int find(int[] p, int x) {
        return p[x] == x ? x : (p[x] = find(p, p[x]));
    }

    private boolean union(int[] p, int a, int b) {
        int pa = find(p, a), pb = find(p, b);
        if (pa == pb)
            return false;
        p[pa] = pb;
        return true;
    }
}
