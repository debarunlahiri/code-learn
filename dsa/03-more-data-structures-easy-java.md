# More Data Structures in Java (Easy)

Goal: Continue with useful DSA structures often asked in interviews.

---

## 1. HashSet

### What it is
A collection of unique values (no duplicates).

### When to use
Fast membership check: "does this value exist?"

### Time complexity
- Add: `O(1)` average
- Contains: `O(1)` average
- Remove: `O(1)` average

### Java code
```java
import java.util.HashSet;
import java.util.Set;

public class HashSetExample {
    public static void main(String[] args) {
        Set<Integer> set = new HashSet<>();
        set.add(10);
        set.add(20);
        set.add(20); // duplicate ignored

        System.out.println(set.contains(20)); // true
        System.out.println(set.size());       // 2
    }
}
```

---

## 2. PriorityQueue (Min Heap)

### What it is
Queue where smallest element comes out first.

### When to use
Top-k problems, shortest path, scheduling.

### Time complexity
- Add: `O(log n)`
- Poll smallest: `O(log n)`
- Peek smallest: `O(1)`

### Java code
```java
import java.util.PriorityQueue;

public class PriorityQueueExample {
    public static void main(String[] args) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.offer(30);
        pq.offer(10);
        pq.offer(20);

        System.out.println(pq.poll()); // 10
        System.out.println(pq.peek()); // 20
    }
}
```

---

## 3. Deque

### What it is
Double-ended queue. Insert/delete from both front and back.

### When to use
Sliding window, monotonic queue, palindrome checks.

### Time complexity
- Add/remove first/last: `O(1)`

### Java code
```java
import java.util.ArrayDeque;
import java.util.Deque;

public class DequeExample {
    public static void main(String[] args) {
        Deque<Integer> dq = new ArrayDeque<>();

        dq.addFirst(2);
        dq.addLast(3);
        dq.addFirst(1);

        System.out.println(dq.removeFirst()); // 1
        System.out.println(dq.removeLast());  // 3
    }
}
```

---

## 4. Trie (Prefix Tree)

### What it is
Tree used to store strings by characters.

### When to use
Prefix search, autocomplete, dictionary problems.

### Java code
```java
class TrieNode {
    TrieNode[] child = new TrieNode[26];
    boolean isWord;
}

public class TrieExample {
    private final TrieNode root = new TrieNode();

    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (node.child[index] == null) {
                node.child[index] = new TrieNode();
            }
            node = node.child[index];
        }
        node.isWord = true;
    }

    public boolean search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (node.child[index] == null) return false;
            node = node.child[index];
        }
        return node.isWord;
    }

    public static void main(String[] args) {
        TrieExample trie = new TrieExample();
        trie.insert("cat");
        trie.insert("car");

        System.out.println(trie.search("cat")); // true
        System.out.println(trie.search("cap")); // false
    }
}
```

---

## 5. Disjoint Set Union (Union-Find)

### What it is
Structure to track connected components efficiently.

### When to use
Graph connectivity, cycle detection, Kruskal MST.

### Java code
```java
import java.util.Arrays;

public class DSUExample {
    private final int[] parent;

    public DSUExample(int n) {
        parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public void union(int a, int b) {
        int pa = find(a);
        int pb = find(b);
        if (pa != pb) parent[pa] = pb;
    }

    public static void main(String[] args) {
        DSUExample dsu = new DSUExample(5);
        dsu.union(0, 1);
        dsu.union(1, 2);

        System.out.println(dsu.find(0) == dsu.find(2)); // true
        System.out.println(Arrays.toString(dsu.parent));
    }
}
```
