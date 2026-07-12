import java.util.*;

/**
 * P068. Graph Valid Tree. This is a easy-to-mid Java DSA coding problem
 * commonly practiced for service
 * based company coding rounds. Given the input described by the method
 * signature, implement the
 * required operation efficiently and return the expected result. Handle normal
 * edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P068GraphValidTree {

    private P068GraphValidTree() {
    }

    public boolean validTree(int n, int[][] edges) {
        if (edges.length != n - 1)
    return false;
        int[] parent = new int[n];
        for (int i = 0; i < n; i++)
    parent[i] = i;
        for (int[] e : edges)
    if (!union(parent, e[0], e[1]))
        return false;
        return true;
    }

    private int find(int[] parent, int x) {
        return parent[x] == x ? x : (parent[x] = find(parent, parent[x]));
    }

    private boolean union(int[] parent, int a, int b) {
        int pa = find(parent, a), pb = find(parent, b);
        if (pa == pb)
    return false;
        parent[pa] = pb;
        return true;
    }

}
