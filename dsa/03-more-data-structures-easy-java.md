# More Data Structures in Java (Easy)

Goal: Continue with useful DSA structures often asked in interviews.

---

## 1. HashSet

### What it is
A collection of unique values (no duplicates) implemented using a hash table.

### Why it matters
- Fastest way to check if an element exists
- Automatically handles duplicates
- Foundation for many algorithms (detecting cycles, finding unique elements)

### Intuition
Think of a dictionary where each word appears only once. You can instantly check if a word exists by looking it up directly.

### When to use
- Fast membership check: "does this value exist?"
- Removing duplicates from array
- Set operations (union, intersection)

### Time complexity
- Add: `O(1)` average
- Contains: `O(1)` average
- Remove: `O(1)` average

### Edge cases
- Hash collisions (rare, handled internally)
- Custom objects need proper `hashCode()` and `equals()`
- Iteration order is not guaranteed

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
Queue where smallest element comes out first (by default). Can be customized for max-heap.

### Why it matters
- Efficiently get smallest/largest element
- Foundation for Dijkstra, Prim's algorithms
- Top-K problems become trivial

### Intuition
Imagine a priority queue at a hospital. The most critical patient (smallest priority number) is always treated first, regardless of arrival order.

### When to use
- Top-k problems (k largest/smallest elements)
- Shortest path algorithms
- Task scheduling with priorities
- Finding median in stream

### Time complexity
- Add: `O(log n)`
- Poll smallest: `O(log n)`
- Peek smallest: `O(1)`

### Edge cases
- Default is min-heap (use custom comparator for max-heap)
- `null` elements not allowed (unless comparator allows)
- Not thread-safe

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

### Why it matters
- More flexible than regular queue
- Perfect for sliding window problems
- Used in palindromes, undo/redo operations

### Intuition
Think of a hallway with doors at both ends. You can enter or exit from either end, unlike a regular queue with only one entrance/exit.

### When to use
- Sliding window maximum/minimum
- Palindrome checking
- Implementing queue using stacks
- Undo/redo functionality

### Time complexity
- Add/remove from ends: `O(1)`
- Access by index: `O(n)`
- Contains: `O(n)`

### Edge cases
- `ArrayDeque` is generally faster than `LinkedList`
- Not thread-safe
- Capacity grows automatically

### Java code
```java
import java.util.ArrayDeque;
import java.util.Deque;

public class DequeExample {
    public static void main(String[] args) {
        Deque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(10);
        deque.addLast(20);
        deque.addFirst(5);

        System.out.println(deque.removeFirst()); // 5
        System.out.println(deque.removeLast());  // 20
    }
}
```

---

## 4. HashMap

### What it is
Key-value pairs with fast lookup using hash table.

### Why it matters
- Most used data structure in interviews
- Enables O(1) average time for lookups
- Foundation for frequency counting, caching

### Intuition
Like a phone book where you can instantly find someone's number using their name (key). Each name appears only once.

### When to use
- Frequency counting
- Caching/memoization
- Two-sum problems
- Mapping relationships

### Time complexity
- Get/Put: `O(1)` average
- Remove: `O(1)` average
- Iterate: `O(n)`

### Edge cases
- Custom objects need proper `hashCode()` and `equals()`
- Initial capacity affects performance
- Null allowed for key and value (once)

### Java code
```java
import java.util.HashMap;
import java.util.Map;

public class HashMapExample {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("Alice", 25);
        map.put("Bob", 30);
        map.put("Alice", 26); // Updates existing

        System.out.println(map.get("Alice")); // 26
        System.out.println(map.containsKey("Bob")); // true
    }
}
```

---

## 5. Stack

### What it is
LIFO (Last In First Out) data structure.

### Why it matters
- Function call simulation
- Expression evaluation
- Backtracking algorithms
- Undo operations

### Intuition
Like a stack of plates. You can only add or remove the top plate. Last plate placed is the first one removed.

### When to use
- Parentheses matching
- Expression evaluation (postfix/infix)
- DFS traversal
- Browser history back button

### Time complexity
- Push: `O(1)`
- Pop: `O(1)`
- Peek: `O(1)`

### Edge cases
- Empty stack throws exception on pop/peek
- `ArrayDeque` preferred over `Stack` class
- Not thread-safe

### Java code
```java
import java.util.Stack;

public class StackExample {
    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(10);
        stack.push(20);
        stack.push(30);

        System.out.println(stack.pop());  // 30
        System.out.println(stack.peek()); // 20
    }
}
```
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
