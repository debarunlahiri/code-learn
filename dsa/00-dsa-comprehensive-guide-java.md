# Introduction to Data Structures and Algorithms (DSA) in Java

Data Structures and Algorithms (DSA) form the foundation of efficient problem-solving in computer science. Data structures are ways to organize and store data for efficient access and modification, while algorithms are step-by-step procedures to perform computations or solve problems. In Java, DSA is implemented using built-in classes (e.g., from `java.util`), arrays, and custom classes.

This guide covers major DSA topics in detail, including definitions, key operations, time and space complexities, Java implementations, and examples. Topics are grouped logically: basic structures, advanced structures, searching/sorting algorithms, and algorithmic paradigms. For each, I'll explain concepts, provide Java code snippets (which you can run in a Java environment), and discuss real-world applications.

Note: Time complexities are in Big O notation (worst-case unless specified). Space complexities include auxiliary space.

---

## 1. Arrays

**Definition**: An array is a fixed-size, contiguous collection of elements of the same type, accessed by index. In Java, arrays are objects.

**Key Operations**:
- Access: O(1)
- Insertion/Deletion: O(n) (requires shifting elements)
- Search: O(n) for linear search

**Advantages**: Fast random access, cache-friendly.
**Disadvantages**: Fixed size, inefficient for frequent insertions/deletions.
**Applications**: Storing lists of items, matrices, buffers.

**Java Implementation**:
Java arrays are declared as `type[] arr = new type[size];`. Use `Arrays` class for utilities like sorting.

**Example**:
```java
import java.util.Arrays;

public class ArrayExample {
    public static void main(String[] args) {
        int[] arr = {5, 3, 8, 1, 9}; // Declaration and initialization
        System.out.println("Element at index 2: " + arr[2]); // Access: 8
        
        // Insertion (manual shift)
        int pos = 2, value = 10;
        for (int i = arr.length - 1; i > pos; i--) {
            arr[i] = arr[i - 1];
        }
        arr[pos] = value;
        System.out.println("After insertion: " + Arrays.toString(arr));
        
        // Deletion (manual shift)
        pos = 1;
        for (int i = pos; i < arr.length - 1; i++) {
            arr[i] = arr[i + 1];
        }
        System.out.println("After deletion: " + Arrays.toString(arr)); // Note: Last element is duplicate, resize if needed
    }
}
```

**Time/Space Complexity Table**:

| Operation | Time | Space |
|-----------|------|-------|
| Access    | O(1) | O(1)  |
| Insert    | O(n) | O(1)  |
| Delete    | O(n) | O(1)  |

---

## 2. Linked Lists

**Definition**: A linked list is a dynamic collection of nodes, each containing data and a reference to the next node. Unlike arrays, size can change easily.

### 2.1 Singly Linked List
**Key Operations**:
- Access: O(n)
- Insertion/Deletion: O(1) at head/tail (if pointers available), O(n) otherwise
- Search: O(n)

**Advantages**: Dynamic size, efficient insertions/deletions.
**Disadvantages**: No random access, extra space for pointers.
**Applications**: Implementing stacks/queues, music playlists.

**Java Implementation**:
Custom `Node` class.

**Example**:
```java
class Node {
    int data;
    Node next;
    Node(int d) { data = d; next = null; }
}

public class SinglyLinkedList {
    Node head;
    
    void insertAtEnd(int data) {
        Node newNode = new Node(data);
        if (head == null) { head = newNode; return; }
        Node last = head;
        while (last.next != null) last = last.next;
        last.next = newNode;
    }
    
    void delete(int key) {
        Node temp = head, prev = null;
        if (temp != null && temp.data == key) { head = temp.next; return; }
        while (temp != null && temp.data != key) {
            prev = temp;
            temp = temp.next;
        }
        if (temp == null) return;
        prev.next = temp.next;
    }
    
    void print() {
        Node n = head;
        while (n != null) {
            System.out.print(n.data + " ");
            n = n.next;
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        SinglyLinkedList list = new SinglyLinkedList();
        list.insertAtEnd(1);
        list.insertAtEnd(2);
        list.insertAtEnd(3);
        list.print(); // 1 2 3
        list.delete(2);
        list.print(); // 1 3
    }
}
```

### 2.2 Doubly Linked List
Adds a previous pointer for bidirectional traversal.
**Key Operations**: Similar to singly, but deletion O(1) with node reference.
**Java**: Use `java.util.LinkedList` for built-in doubly linked list.

### 2.3 Circular Linked List
Last node points to first (singly or doubly).
**Applications**: Round-robin scheduling.

**Time/Space Complexity Table** (for all linked lists):

| Operation | Time (Singly/Doubly) | Space |
|-----------|----------------------|-------|
| Access    | O(n)                 | O(1)  |
| Insert    | O(1)/O(1) at ends    | O(1)  |
| Delete    | O(n)/O(1)            | O(1)  |

---

## 3. Stacks

**Definition**: LIFO (Last In, First Out) structure. Operations: push (add), pop (remove), peek (top).

**Key Operations**:
- Push/Pop/Peek: O(1)
- Search: O(n)

**Advantages**: Simple, used for undo/redo.
**Disadvantages**: Fixed size if array-based.
**Applications**: Function call stack, expression evaluation.

**Java Implementation**: Use `java.util.Stack` (extends Vector) or array/linked list.

**Example** (Array-based):
```java
public class StackExample {
    int[] stack;
    int top = -1;
    int capacity;
    
    StackExample(int cap) {
        capacity = cap;
        stack = new int[capacity];
    }
    
    void push(int item) {
        if (top >= capacity - 1) { System.out.println("Overflow"); return; }
        stack[++top] = item;
    }
    
    int pop() {
        if (top < 0) { System.out.println("Underflow"); return -1; }
        return stack[top--];
    }
    
    int peek() { return (top < 0) ? -1 : stack[top]; }
    
    public static void main(String[] args) {
        StackExample s = new StackExample(3);
        s.push(1); s.push(2);
        System.out.println(s.peek()); // 2
        s.pop();
        System.out.println(s.peek()); // 1
    }
}
```

**Time/Space**: All ops O(1), space O(n).

---

## 4. Queues

**Definition**: FIFO (First In, First Out) structure. Operations: enqueue (add), dequeue (remove), front.

### 4.1 Simple Queue
**Key Operations**: Enqueue/Dequeue: O(1) if circular array or linked list.

**Advantages**: Efficient for order preservation.
**Disadvantages**: Fixed size in array.
**Applications**: Task scheduling, BFS.

**Java Implementation**: Use `java.util.Queue` interface, e.g., `LinkedList`.

**Example** (Array-based Circular Queue):
```java
public class QueueExample {
    int[] queue;
    int front = 0, rear = -1, size = 0;
    int capacity;
    
    QueueExample(int cap) {
        capacity = cap;
        queue = new int[capacity];
    }
    
    void enqueue(int item) {
        if (size == capacity) { System.out.println("Full"); return; }
        rear = (rear + 1) % capacity;
        queue[rear] = item;
        size++;
    }
    
    int dequeue() {
        if (size == 0) { System.out.println("Empty"); return -1; }
        int item = queue[front];
        front = (front + 1) % capacity;
        size--;
        return item;
    }
    
    public static void main(String[] args) {
        QueueExample q = new QueueExample(3);
        q.enqueue(1); q.enqueue(2);
        System.out.println(q.dequeue()); // 1
    }
}
```

### 4.2 Priority Queue
Elements have priorities; highest priority dequeued first.
**Java**: `java.util.PriorityQueue` (min-heap by default).

### 4.3 Deque (Double-Ended Queue)
Add/remove from both ends.
**Java**: `java.util.ArrayDeque`.

**Time/Space**: O(1) for ends operations, space O(n).

---

## 5. Trees

**Definition**: Hierarchical structure with nodes: root, children, leaves. No cycles.

### 5.1 Binary Tree
Each node has at most 2 children.
**Traversals**: Inorder (LNR), Preorder (NLR), Postorder (LRN), Level Order (BFS).
**Key Operations**:
- Insertion/Deletion/Search: O(n) in worst case (skewed).
**Applications**: Expression trees, file systems.

**Java Implementation**:
```java
class TreeNode {
    int data;
    TreeNode left, right;
    TreeNode(int d) { data = d; }
}

public class BinaryTree {
    TreeNode root;
    
    void inorder(TreeNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.data + " ");
        inorder(node.right);
    }
    
    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree();
        tree.root = new TreeNode(1);
        tree.root.left = new TreeNode(2);
        tree.root.right = new TreeNode(3);
        tree.inorder(tree.root); // 2 1 3
    }
}
```

### 5.2 Binary Search Tree (BST)
Left child < node < right child.
**Key Operations**:
- Search/Insert/Delete: O(h) where h is height (O(log n) balanced, O(n) skewed).
**Applications**: Sorted data storage.

**Example Insertion**:
```java
TreeNode insert(TreeNode root, int key) {
    if (root == null) return new TreeNode(key);
    if (key < root.data) root.left = insert(root.left, key);
    else if (key > root.data) root.right = insert(root.right, key);
    return root;
}
```

### 5.3 AVL Tree (Balanced BST)
Self-balancing via rotations; height difference <=1.
**Operations**: O(log n).
**Java**: Custom implementation with balance factors.

### 5.4 Other Trees
- Red-Black Tree: Balanced, used in `TreeMap`.
- Trie: For prefix searches, strings.
- Segment Tree/Fenwick Tree: Range queries.

**Time/Space Complexity Table** (BST):

| Operation | Time (Balanced) | Space |
|-----------|-----------------|-------|
| Search    | O(log n)        | O(1)  |
| Insert    | O(log n)        | O(1)  |
| Delete    | O(log n)        | O(1)  |

---

## 6. Graphs

**Definition**: Nodes (vertices) connected by edges. Directed/undirected, weighted/unweighted.

**Representations**: Adjacency Matrix (O(v^2) space), Adjacency List (O(v+e)).

**Key Algorithms**:
- Traversals: BFS (O(v+e)), DFS (O(v+e)).
- Shortest Path: Dijkstra (O((v+e) log v) with heap), Bellman-Ford (O(ve)).
- MST: Kruskal/Prim (O(e log v)).
- Cycle Detection: DFS with colors.

**Advantages**: Model relationships (social networks).
**Disadvantages**: High space for dense graphs.
**Applications**: Maps, social graphs.

**Java Implementation** (Adjacency List):
```java
import java.util.*;

public class Graph {
    int V;
    LinkedList<Integer>[] adj;
    
    Graph(int v) {
        V = v;
        adj = new LinkedList[v];
        for (int i = 0; i < v; ++i) adj[i] = new LinkedList();
    }
    
    void addEdge(int u, int v) { adj[u].add(v); } // Directed
    
    void BFS(int s) {
        boolean[] visited = new boolean[V];
        Queue<Integer> queue = new LinkedList<>();
        visited[s] = true;
        queue.add(s);
        while (!queue.isEmpty()) {
            s = queue.poll();
            System.out.print(s + " ");
            for (int n : adj[s]) {
                if (!visited[n]) {
                    visited[n] = true;
                    queue.add(n);
                }
            }
        }
    }
    
    public static void main(String[] args) {
        Graph g = new Graph(4);
        g.addEdge(0, 1); g.addEdge(0, 2); g.addEdge(1, 3);
        g.BFS(0); // 0 1 2 3
    }
}
```

**Time/Space**: Traversals O(v+e), space O(v).

---

## 7. Hashing

**Definition**: Maps keys to values via hash function for O(1) average access. Handles collisions (chaining, open addressing).

**Key Operations**:
- Insert/Search/Delete: O(1) average, O(n) worst (bad hash).

**Advantages**: Fast lookups.
**Disadvantages**: Collisions, no order.
**Applications**: Dictionaries, caches.

**Java Implementation**: `java.util.HashMap` (chaining), `HashSet`.

**Example**:
```java
import java.util.HashMap;

public class HashingExample {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("apple", 1);
        map.put("banana", 2);
        System.out.println(map.get("apple")); // 1
        map.remove("banana");
        System.out.println(map.containsKey("banana")); // false
    }
}
```

Custom hash table can be array of linked lists.

**Time/Space**: O(1) average, space O(n).

---

## 8. Heaps

**Definition**: Complete binary tree satisfying heap property (min/max).

**Key Operations**:
- Insert/Extract Min/Max: O(log n)
- Build Heap: O(n)

**Advantages**: Priority queues.
**Disadvantages**: No efficient search.
**Applications**: Heap sort, scheduling.

**Java**: `PriorityQueue` (min-heap).

**Example** (Min-Heap Array Implementation):
```java
public class MinHeap {
    int[] heap;
    int size, capacity;
    
    MinHeap(int cap) {
        capacity = cap;
        heap = new int[cap];
        size = 0;
    }
    
    int parent(int i) { return (i - 1) / 2; }
    int left(int i) { return 2 * i + 1; }
    int right(int i) { return 2 * i + 2; }
    
    void insert(int key) {
        if (size == capacity) return;
        heap[size++] = key;
        int i = size - 1;
        while (i != 0 && heap[parent(i)] > heap[i]) {
            swap(i, parent(i));
            i = parent(i);
        }
    }
    
    void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    
    public static void main(String[] args) {
        MinHeap h = new MinHeap(5);
        h.insert(3); h.insert(1); h.insert(2);
        System.out.println(h.heap[0]); // 1 (min)
    }
}
```

**Time/Space**: O(log n) for ops, space O(n).

---

## 9. Sorting Algorithms

Sorting arranges elements in order.

**Comparison Table**:

| Algorithm     | Time (Best/Avg/Worst) | Space | Stable? |
|---------------|-----------------------|-------|---------|
| Bubble Sort   | O(n)/O(n^2)/O(n^2)   | O(1)  | Yes     |
| Selection Sort| O(n^2)/O(n^2)/O(n^2) | O(1)  | No      |
| Insertion Sort| O(n)/O(n^2)/O(n^2)   | O(1)  | Yes     |
| Merge Sort    | O(n log n) all        | O(n)  | Yes     |
| Quick Sort    | O(n log n)/O(n log n)/O(n^2) | O(log n) | No   |
| Heap Sort     | O(n log n) all        | O(1)  | No      |

### 9.1 Bubble Sort
Repeatedly swap adjacent if out of order.
**Java**:
```java
void bubbleSort(int[] arr) {
    int n = arr.length;
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}
```

### 9.2 Selection Sort
Select min and swap to front.
Similar loop structure.

### 9.3 Insertion Sort
Build sorted portion by inserting.
**Java**:
```java
void insertionSort(int[] arr) {
    int n = arr.length;
    for (int i = 1; i < n; ++i) {
        int key = arr[i];
        int j = i - 1;
        while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j--;
        }
        arr[j + 1] = key;
    }
}
```

### 9.4 Merge Sort
Divide and conquer: split, sort, merge.
**Java**: Recursive.

### 9.5 Quick Sort
Partition around pivot, recurse.
**Java**: Choose pivot, partition.

### 9.6 Heap Sort
Build max-heap, extract max repeatedly.

**Applications**: Databases, file sorting.

---

## 10. Searching Algorithms

**Definition**: Find elements in data.

**Comparison Table**:

| Algorithm | Time (Best/Avg/Worst) | Space | Prerequisite |
|-----------|-----------------------|-------|--------------|
| Linear    | O(1)/O(n)/O(n)       | O(1)  | None         |
| Binary    | O(1)/O(log n)/O(log n)| O(1) | Sorted       |

### 10.1 Linear Search
Iterate through elements.

**Java**:
```java
int linearSearch(int[] arr, int key) {
    for (int i = 0; i < arr.length; i++) {
        if (arr[i] == key) return i;
    }
    return -1;
}
```

### 10.2 Binary Search
Halve search interval in sorted array.

**Java**:
```java
int binarySearch(int[] arr, int key) {
    int low = 0, high = arr.length - 1;
    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (arr[mid] == key) return mid;
        if (arr[mid] < key) low = mid + 1;
        else high = mid - 1;
    }
    return -1;
}
```

**Applications**: Dictionaries, databases.

---

## 11. Dynamic Programming (DP)

**Definition**: Break problems into subproblems, store results (memoization/tabulation) to avoid recomputation. Optimal substructure + overlapping subproblems.

**Key Concepts**:
- Top-Down (Recursion + Memo): Fibonacci.
- Bottom-Up (Iteration): Knapsack.

**Example: Fibonacci** (Memoization)
**Time**: O(n), Space O(n)
```java
int[] memo;
int fib(int n) {
    if (memo == null) memo = new int[n + 1];
    if (memo[n] != 0) return memo[n];
    if (n <= 1) return n;
    memo[n] = fib(n - 1) + fib(n - 2);
    return memo[n];
}
```

**Bottom-Up**:
```java
int fibBottomUp(int n) {
    if (n <= 1) return n;
    int[] dp = new int[n + 1];
    dp[1] = 1;
    for (int i = 2; i <= n; i++) {
        dp[i] = dp[i - 1] + dp[i - 2];
    }
    return dp[n];
}
```

**Applications**: Optimization (0/1 Knapsack, LCS), pathfinding.

---

## 12. Greedy Algorithms

**Definition**: Make locally optimal choice at each step, hoping for global optimum. No backtracking.

**Examples**:
- Coin Change: Select largest coin first.
- Huffman Coding: Build tree by merging lowest freq.
- Kruskal's MST: Sort edges, add non-cycle.

**Java Example: Fractional Knapsack**
Maximize value/weight ratio.
Sort items by value/weight descending, add until capacity.

**Time**: Depends on problem (e.g., O(n log n) for sorting).
**Advantages**: Simple, fast.
**Disadvantages**: Not always optimal (use when proven greedy).
**Applications**: Scheduling, compression.

---

## 13. Backtracking

**Definition**: Build solution incrementally, abandon partial if invalid (pruning). Depth-first search variant.

**Examples**:
- N-Queens: Place queens without attacks.
- Sudoku Solver.
- Subset Sum.

**Java Example: Permutations**
```java
import java.util.*;

public class Backtracking {
    void permute(List<Integer> nums, List<Integer> path, boolean[] used, List<List<Integer>> result) {
        if (path.size() == nums.size()) {
            result.add(new ArrayList<>(path));
            return;
        }
        for (int i = 0; i < nums.size(); i++) {
            if (used[i]) continue;
            used[i] = true;
            path.add(nums.get(i));
            permute(nums, path, used, result);
            used[i] = false;
            path.remove(path.size() - 1);
        }
    }
    
    public static void main(String[] args) {
        Backtracking bt = new Backtracking();
        List<Integer> nums = Arrays.asList(1, 2, 3);
        List<List<Integer>> result = new ArrayList<>();
        boolean[] used = new boolean[nums.size()];
        bt.permute(nums, new ArrayList<>(), used, result);
        System.out.println(result); // All permutations
    }
}
```

**Time**: Exponential (O(n!) for permutations).
**Applications**: Puzzles, combinatorial optimization.

---

## 14. Bit Manipulation

**Definition**: Operate on binary representations using &, |, ^, <<, >>.

**Key Operations**:
- Set/Check Bit: O(1)
- Common Tricks: n & (n-1) to remove last set bit (power of 2 check).

**Examples**:
- XOR for single non-duplicate: All pairs cancel.
- Right Shift for divide by 2.

**Java Example**:
```java
public class BitManipulation {
    public static void main(String[] args) {
        int a = 5; // 101
        int b = 3; // 011
        System.out.println(a & b); // 1 (001)
        System.out.println(a | b); // 7 (111)
        System.out.println(a ^ b); // 6 (110)
        System.out.println(a << 1); // 10 (1010)
        
        // Check if power of 2
        int n = 8; // 1000
        System.out.println((n & (n - 1)) == 0); // true
    }
}
```

**Applications**: Low-level optimization, cryptography, compression.

---

## 15. Other Advanced Topics

- **Divide and Conquer**: Binary search, merge sort.
- **String Algorithms**: KMP for pattern search (O(n+m)), Rabin-Karp (hashing).
- **Graph Advanced**: Topological Sort (DAG), Strongly Connected Components (Kosaraju).
- **Union-Find (Disjoint Set)**: For cycle detection, O(α(n)) ≈ O(1) with path compression.
**Java**: Custom arrays for parent/rank.

---

This covers core DSA topics. For deeper dives, practice on platforms like LeetCode. If you need code for a specific sub-topic, ask!

Here’s the continuation of the **DSA in Java** detailed guide, picking up from where we left off. I'll now cover some of the most frequently asked / most important remaining topics that are commonly expected in interviews, competitive programming, and real-world usage.

### 16. Trie (Prefix Tree / Digital Tree)

**Definition**: A tree-like data structure used to store a dynamic set of strings where each node represents a character. Very efficient for prefix-based operations.

**Key Operations**:
- Insert: O(m) where m = length of word
- Search: O(m)
- Prefix search (startsWith): O(m)
- Delete: O(m)

**Advantages**: Fast prefix lookups, auto-complete, spell checker
**Disadvantages**: High memory usage (especially with large alphabets)

**Applications**:
- Autocomplete
- Spell checker
- IP routing (longest prefix match)
- Dictionary suggestions

**Java Implementation** (basic version with lowercase letters only):

```java
class TrieNode {
    TrieNode[] children = new TrieNode[26];
    boolean isEndOfWord;
}

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            if (node.children[idx] == null) {
                node.children[idx] = new TrieNode();
            }
            node = node.children[idx];
        }
        node.isEndOfWord = true;
    }

    public boolean search(String word) {
        TrieNode node = searchPrefix(word);
        return node != null && node.isEndOfWord;
    }

    public boolean startsWith(String prefix) {
        return searchPrefix(prefix) != null;
    }

    private TrieNode searchPrefix(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            int idx = c - 'a';
            if (node.children[idx] == null) {
                return null;
            }
            node = node.children[idx];
        }
        return node;
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("apple");
        trie.insert("app");
        trie.insert("application");

        System.out.println(trie.search("app"));       // true
        System.out.println(trie.search("apps"));      // false
        System.out.println(trie.startsWith("appl"));  // true
    }
}
```

**Space Complexity**: O(ALPHABET_SIZE × N × M) worst case  
**Tip**: For real projects → use `Map<Character, TrieNode>` instead of fixed array to save space for sparse data.

### 17. Union-Find (Disjoint Set Union - DSU)

**Definition**: Data structure to manage partition of a set into disjoint subsets. Very fast with path compression + union by rank/size.

**Key Operations**:
- find (with path compression): amortized O(α(n)) ≈ O(1)
- union: amortized O(α(n))

**Applications**:
- Kruskal’s Minimum Spanning Tree
- Cycle detection in undirected graph
- Connected components
- Dynamic connectivity queries

**Java Implementation** (with path compression + union by rank):

```java
public class UnionFind {
    private int[] parent;
    private int[] rank;

    public UnionFind(int size) {
        parent = new int[size];
        rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 1;
        }
    }

    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // path compression
        }
        return parent[x];
    }

    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) return false; // already connected

        // Union by rank
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        return true;
    }

    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }
}
```

**Usage example** (cycle detection):

```java
UnionFind uf = new UnionFind(5);
uf.union(0, 1);
uf.union(1, 2);
uf.union(2, 3);
System.out.println(uf.connected(0, 3)); // true
```

### 18. Segment Tree (Range Query + Point Update)

**Definition**: A full binary tree used for range queries (sum, min, max, gcd, etc.) and updates in logarithmic time.

**Key Operations**:
- Build: O(n)
- Update (point): O(log n)
- Range Query: O(log n)

**Applications**:
- Range sum/min/max queries
- Range update + lazy propagation (advanced)
- Competitive programming problems

**Java Implementation** (Range Sum Query):

```java
public class SegmentTree {
    private int[] tree;
    private int n;

    public SegmentTree(int[] arr) {
        n = arr.length;
        tree = new int[4 * n];
        build(arr, 0, 0, n - 1);
    }

    private void build(int[] arr, int node, int start, int end) {
        if (start == end) {
            tree[node] = arr[start];
            return;
        }
        int mid = (start + end) / 2;
        build(arr, 2 * node + 1, start, mid);
        build(arr, 2 * node + 2, mid + 1, end);
        tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
    }

    public void update(int idx, int val, int node, int start, int end) {
        if (start == end) {
            tree[node] = val;
            return;
        }
        int mid = (start + end) / 2;
        if (idx <= mid)
            update(idx, val, 2 * node + 1, start, mid);
        else
            update(idx, val, 2 * node + 2, mid + 1, end);
        tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
    }

    public int query(int left, int right, int node, int start, int end) {
        if (right < start || left > end) return 0;
        if (left <= start && end <= right) return tree[node];

        int mid = (start + end) / 2;
        int p1 = query(left, right, 2 * node + 1, start, mid);
        int p2 = query(left, right, 2 * node + 2, mid + 1, end);
        return p1 + p2;
    }

    // Public methods
    public void update(int idx, int val) {
        update(idx, val, 0, 0, n - 1);
    }

    public int query(int left, int right) {
        return query(left, right, 0, 0, n - 1);
    }
}
```

### 19. Top 10 Most Asked DSA Patterns (Java Interviews 2025–2026)

| #  | Pattern                     | Key Data Structures          | Example Problems                              |
|----|-----------------------------|------------------------------|-----------------------------------------------|
| 1  | Sliding Window              | Array / Deque                | Longest Substring Without Repeating, Max Sum Subarray of size K |
| 2  | Two Pointers / Fast & Slow  | Array / Linked List          | Remove Duplicates, Linked List Cycle, 3Sum    |
| 3  | Fast & Slow Pointers        | Linked List                  | Middle of Linked List, Happy Number           |
| 4  | Merge Intervals             | Array (sorting)              | Merge Intervals, Meeting Rooms II             |
| 5  | Cyclic Sort                 | Array                        | Find Missing Number, Find All Duplicates      |
| 6  | In-place Reversal of LL     | Linked List                  | Reverse Linked List, Reverse Nodes in k-Group |
| 7  | Tree BFS (Level Order)      | Queue                        | Level Order Traversal, Zigzag Traversal       |
| 8  | Tree DFS (Recursion)        | Recursion / Stack            | Path Sum, Diameter of Binary Tree             |
| 9  | Subsets / Backtracking      | Recursion                    | Subsets, Permutations, Combination Sum        |
|10  | Top K Elements              | Heap / QuickSelect           | Kth Largest Element, Top K Frequent Elements  |

Would you like me to:

1. Deep-dive into any one of these patterns with 3–4 solved examples each?
2. Provide more advanced topics (Lazy Propagation, Heavy-Light Decomposition, Suffix Array, etc.)?
3. Give a curated list of 50 most important LeetCode problems categorized by topic?
4. Explain any specific algorithm / problem in full detail with Java code?

Just tell me which direction you want to go next!

Continuing from the previous part...

Here are some more **advanced / frequently appearing DSA topics and patterns** that remain very relevant in 2025–2026 for Java-based interviews (FAANG / MAANG, product-based companies, high-scale backend roles, etc.).

I'll focus on concepts that often appear in medium-hard problems, system-design-adjacent questions, or optimization rounds.

### 20. Suffix Array & Suffix Tree (String Advanced)

**Why still relevant?**  
Very powerful for heavy string problems (longest repeated substring, longest common substring, pattern matching at scale, genome problems).  
Appears in hard LeetCode-style questions or when companies ask about text indexing.

**Suffix Array** — sorted array of all suffixes of a string.  
Build → O(n log n) or O(n) with DC3/DC algorithm (advanced).  
Common use: LCP (Longest Common Prefix) array → enables fast queries.

**Java note**: No built-in. Usually implement naive O(n² log n) sort for interviews, or use libraries for production.

**Quick conceptual code skeleton (naive suffix array)**:

```java
class SuffixArrayNaive {
    static class Suffix implements Comparable<Suffix> {
        String suff;
        int index;
        Suffix(String s, int i) { suff = s; index = i; }
        public int compareTo(Suffix other) {
            return this.suff.compareTo(other.suff);
        }
    }

    static int[] buildSuffixArray(String s) {
        int n = s.length();
        Suffix[] suffixes = new Suffix[n];
        for (int i = 0; i < n; i++) {
            suffixes[i] = new Suffix(s.substring(i), i);
        }
        Arrays.sort(suffixes);
        int[] sa = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = suffixes[i].index;
        }
        return sa;
    }
}
```

**Realistic interview tip**: Most companies accept explaining the concept + naive implementation + mentioning O(n) construction exists.

### 21. KMP (Knuth-Morris-Pratt) Algorithm – String Matching

**Definition**: Efficient substring search — O(n + m) time.

**Core idea**: Precompute longest proper prefix that is also suffix (LPS / π array) → avoid unnecessary backtracking.

**Java Implementation** (most asked version):

```java
public class KMP {
    public static int[] computeLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0; // length of previous longest prefix suffix
        int i = 1;
        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    public static List<Integer> search(String text, String pattern) {
        List<Integer> positions = new ArrayList<>();
        int[] lps = computeLPS(pattern);
        int i = 0, j = 0;
        while (i < text.length()) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++; j++;
            }
            if (j == pattern.length()) {
                positions.add(i - j);
                j = lps[j - 1];
            } else if (i < text.length() && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return positions;
    }
}
```

**Variants asked**: Find all occurrences, longest prefix which is suffix, etc.

### 22. Monotonic Stack / Monotonic Queue

**Definition**: Stack that maintains elements in increasing / decreasing order.

**Key Problems**:
- Next Greater Element (to right / left)
- Next Smaller Element
- Largest Rectangle in Histogram
- Trapping Rain Water (variation)
- Stock Span Problem
- Remove K Digits (make smallest / largest number)

**Java Example – Next Greater Element to the right**:

```java
public int[] nextGreaterElement(int[] arr) {
    int n = arr.length;
    int[] nge = new int[n];
    Arrays.fill(nge, -1);
    Deque<Integer> stack = new ArrayDeque<>(); // monotonic decreasing

    for (int i = n - 1; i >= 0; i--) {
        while (!stack.isEmpty() && stack.peek() <= arr[i]) {
            stack.pop();
        }
        if (!stack.isEmpty()) {
            nge[i] = stack.peek();
        }
        stack.push(arr[i]);
    }
    return nge;
}
```

**Monotonic Queue variation**: Used in Sliding Window Maximum (LeetCode 239).

### 23. Topological Sort (Kahn’s Algorithm + DFS)

**Definition**: Linear ordering of vertices such that for every directed edge uv, vertex u comes before v.

**Use cases**:
- Course Schedule (LeetCode 207, 210)
- Alien Dictionary
- Build order in dependency graph
- Detecting cycle in directed graph

**Kahn’s Algorithm (BFS + indegree)** – most common in interviews:

```java
public int[] topologicalSort(int V, List<List<Integer>> adj) {
    int[] indegree = new int[V];
    for (int u = 0; u < V; u++) {
        for (int v : adj.get(u)) {
            indegree[v]++;
        }
    }

    Queue<Integer> q = new LinkedList<>();
    for (int i = 0; i < V; i++) {
        if (indegree[i] == 0) q.offer(i);
    }

    int[] order = new int[V];
    int idx = 0;
    while (!q.isEmpty()) {
        int u = q.poll();
        order[idx++] = u;
        for (int v : adj.get(u)) {
            indegree[v]--;
            if (indegree[v] == 0) q.offer(v);
        }
    }

    return (idx == V) ? order : new int[0]; // empty if cycle
}
```

### 24. Bitmask DP (State Compression DP)

**When to use**: Small constraints on one dimension (n ≤ 20), need to track subset of items/states.

**Classic problems**:
- Traveling Salesman Problem (TSP) – Held–Karp
- Assign unique tasks to workers (minimum cost assignment)
- Subset sum variants
- Hamilton path / cycle

**Example skeleton** (TSP style):

```java
// dp[mask][city] = min cost to visit exactly the cities in mask, ending at city
int tsp(int mask, int city, int[][] dist, int[][] dp, int n) {
    if (mask == (1 << n) - 1) {
        return dist[city][0]; // back to start
    }
    if (dp[mask][city] != -1) return dp[mask][city];

    int ans = Integer.MAX_VALUE;
    for (int next = 0; next < n; next++) {
        if ((mask & (1 << next)) == 0) {
            int sub = tsp(mask | (1 << next), next, dist, dp, n);
            if (sub != Integer.MAX_VALUE) {
                ans = Math.min(ans, dist[city][next] + sub);
            }
        }
    }
    return dp[mask][city] = ans;
}
```

### 25. Quick Summary – Most Impactful Patterns in 2025–2026 (based on recent trends)

| Rank | Pattern                        | Frequency | Difficulty | Key Problems (LeetCode #)                  |
|------|--------------------------------|-----------|------------|--------------------------------------------|
| 1    | Sliding Window                 | Very High | Easy–Hard  | 3, 76, 239, 424, 567, 1004                |
| 2    | Two Pointers / Fast-Slow       | Very High | Easy–Med   | 11, 19, 141, 142, 287, 202                |
| 3    | Monotonic Stack/Queue          | High      | Med–Hard   | 42, 84, 85, 496, 739, 901                 |
| 4    | Top K / Heap tricks            | High      | Med        | 215, 347, 373, 692, 973                   |
| 5    | Binary Search on Answer        | High      | Med–Hard   | 410, 875, 1011, 1231, 1482                |
| 6    | Bitmask DP                     | Medium    | Hard       | 464, 943, 1681 (TSP variants)             |
| 7    | Union-Find with path compression| High     | Med        | 200, 547, 684, 721, 947                   |
| 8    | Tree DP                        | Medium    | Hard       | 124, 337, 543, 687, 968                   |
| 9    | Digit DP                       | Low-Med   | Hard       | 1012, 2376, 2719                          |
|10    | Heavy-Light / Centroid Decomp  | Rare      | Expert     | Very hard graph problems (almost never)   |

If you want, pick any one of these:

- Detailed code + explanation of any pattern above (with 3–5 LeetCode problems solved in Java)
- 40–50 most repeated LeetCode problems in 2025–2026 grouped by company / pattern
- Advanced String / Graph topics (Manacher, Tarjan SCC, Dijkstra with potentials, etc.)
- Common Java-specific tricks in interviews (PriorityQueue custom comparator, HashMap pitfalls, etc.)

Just say the number or topic name — let's go deeper wherever you need!

Here’s the next part of the **DSA in Java** deep-dive series.

We’ll now move into some of the most **high-frequency, high-impact topics** that appear in medium ↔ hard interviews in 2025–2026, especially at companies that still emphasize algorithms (Google, Amazon, Atlassian, Uber, Flipkart, etc.).

### 26. Binary Search on Answer (Search Space Reduction)

**Core Idea**  
Instead of searching over the array itself, we binary search over a **range of possible answers** (usually min–max possible values).

**Classic Problems (very frequent in 2024–2026)**

- Aggressive Cows / Magnetic Force Between Balls
- Capacity To Ship Packages Within D Days
- Koko Eating Bananas
- Split Array Largest Sum
- Minimum Number of Days to Make m Bouquets
- Find the Smallest Divisor Given a Threshold
- Minimize Max Distance to Gas Station

**Template (Java)** — most clean & reusable version

```java
class BinarySearchOnAnswer {

    // Example: Koko Eating Bananas (LeetCode 875)
    public int minEatingSpeed(int[] piles, int h) {
        int left = 1;
        int right = Arrays.stream(piles).max().getAsInt(); // max pile size

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (canFinish(piles, mid, h)) {
                right = mid;           // try smaller speed
            } else {
                left = mid + 1;        // need larger speed
            }
        }
        return left;
    }

    private boolean canFinish(int[] piles, int speed, int hours) {
        long totalHours = 0;
        for (int pile : piles) {
            totalHours += (pile + speed - 1) / speed; // ceil division
            if (totalHours > hours) return false;
        }
        return true;
    }

    // General template skeleton
    public int binarySearchOnAnswer(int[] arr, int target) {
        int left = 0;           // minimal possible answer
        int right = 1_000_000_000; // or some realistic upper bound

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (isPossible(arr, mid, target)) {
                right = mid;    // try to minimize / tighten
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    private boolean isPossible(int[] arr, int midValue, int target) {
        // implement feasibility check
        return true; // placeholder
    }
}
```

**Quick checklist when you see this pattern**:
- Asked to **minimize** the **maximum** something
- Asked to **maximize** the **minimum** something
- “Within X days/hours”, “at most X operations”, “smallest possible Y such that…”

### 27. Trie + Backtracking (Word Search II, Boggle, etc.)

**Very frequent hard problem combination in 2025**

**LeetCode 212 – Word Search II** remains one of the most asked hard problems.

**Clean Java solution (Trie + DFS backtracking)**

```java
class WordSearchII {
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        String word = null; // store full word when it's end
    }

    public List<String> findWords(char[][] board, String[] words) {
        List<String> result = new ArrayList<>();

        // Build Trie
        TrieNode root = new TrieNode();
        for (String word : words) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                node = node.children.computeIfAbsent(c, k -> new TrieNode());
            }
            node.word = word; // mark end
        }

        int m = board.length, n = board[0].length;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dfs(board, i, j, root, result);
            }
        }

        return result;
    }

    private void dfs(char[][] board, int i, int j, TrieNode node, List<String> result) {
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) return;
        char c = board[i][j];
        if (c == '#' || !node.children.containsKey(c)) return;

        board[i][j] = '#'; // mark visited
        TrieNode next = node.children.get(c);

        if (next.word != null) {
            result.add(next.word);
            next.word = null; // avoid duplicate
        }

        dfs(board, i - 1, j, next, result);
        dfs(board, i + 1, j, next, result);
        dfs(board, i, j - 1, next, result);
        dfs(board, i, j + 1, next, result);

        board[i][j] = c; // backtrack
    }
}
```

**Optimization notes (2025 interviews)**:
- Use `word = null` instead of boolean flag → prevents adding same word multiple times
- Trie pruning → if no children left after backtracking, can remove node (advanced)

### 28. Dijkstra with PriorityQueue (shortest path in weighted graph)

**Still extremely common** — especially with negative weights forbidden questions.

**Standard Java implementation (2025 style – clean & fast)**

```java
class Dijkstra {
    static class Pair {
        int node;
        long dist;
        Pair(int n, long d) { node = n; dist = d; }
    }

    public long[] dijkstra(int V, List<List<int[]>> adj, int src) {
        long[] dist = new long[V];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[src] = 0;

        PriorityQueue<Pair> pq = new PriorityQueue<>((a, b) -> Long.compare(a.dist, b.dist));
        pq.offer(new Pair(src, 0));

        while (!pq.isEmpty()) {
            Pair curr = pq.poll();
            int u = curr.node;
            long d = curr.dist;

            if (d > dist[u]) continue; // outdated

            for (int[] edge : adj.get(u)) {
                int v = edge[0];
                long w = edge[1];

                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.offer(new Pair(v, dist[v]));
                }
            }
        }

        return dist;
    }
}
```

**Important notes for interviews**:
- Use `long` for distances (prevents overflow)
- Do **not** use visited array in Dijkstra with PQ (lazy deletion via distance check)
- For 0-1 BFS → use Deque instead (treat weight 0 as front, weight 1 as back)

### Quick 2025–2026 Trend Snapshot (Java interviews)

| Category               | Rising Fast (2025–2026)                  | Stable High-frequency                     | Declining a bit (but still asked) |
|-----------------------|------------------------------------------|--------------------------------------------|------------------------------------|
| Graph                 | 0-1 BFS, Dijkstra + potentials           | Dijkstra, Topo Sort, Union-Find            | Bellman-Ford (unless negatives)    |
| DP                    | Digit DP, Bitmask DP on subsets ≤ 20     | 1D/2D Knapsack, LCS variants               | Classic matrix chain (rare now)    |
| Strings               | Suffix structures (conceptual), KMP+Rabin| Trie + Backtracking, Sliding Window        | Z-algorithm (less frequent)        |
| Binary Search         | Binary Search on Answer + Feasibility    | Classic sorted array search                | —                                  |
| Heap / Top-K          | Custom comparators, Multi-pointer heaps  | Kth largest, Merge K lists                 | —                                  |

Next topics I can cover (pick any):

1. **0-1 BFS** + Deque trick  
2. **Digit DP** (count numbers with property X)  
3. **Tree DP** patterns (diameter, max path sum, house robber III style)  
4. **Sliding Window Maximum** (monotonic deque in detail)  
5. **Most asked Heap problems** with custom comparators in Java  
6. **Union-Find with rollback / undo** (rare but asked in hard problems)  
7. **50 most repeated LeetCode problems** (company-wise or pattern-wise) in Java

Just tell me the number or topic name — or say “keep going sequentially” if you want more breadth.
