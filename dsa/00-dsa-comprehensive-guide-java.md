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

Here’s the next continuation of the **DSA in Java** deep-dive series (2025–2026 interview-oriented edition).

We’ll now cover a few more **high-value, frequently appearing patterns / techniques** that often separate good from great candidates in medium ↔ hard rounds.

### 29. 0-1 BFS (Deque instead of PriorityQueue)

**When normal Dijkstra is too slow** because all edge weights are **0 or 1**.

**Key Insight**  
Treat weight 0 edges as moving to the **front** of the queue (like level 0 increase), weight 1 edges to the **back** (level +1).

→ Time complexity drops from O((V+E) log V) → **O(V + E)**

**Very frequent in 2024–2026**  
- Shortest path in grid with obstacles/teleports  
- Minimum operations to convert number A → B (×2, +1, -1, etc.)  
- 0-1 matrix problems  
- Many AtCoder / Codeforces graph problems

**Clean Java Implementation (2025 style)**

```java
class ZeroOneBFS {
    static class Pair {
        int node;
        long dist;
        Pair(int n, long d) { node = n; dist = d; }
    }

    public long[] shortestPath(int V, List<List<int[]>> adj, int src) { // adj[u] = {v, weight} where weight ∈ {0,1}
        long[] dist = new long[V];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[src] = 0;

        Deque<Integer> deque = new ArrayDeque<>();
        deque.offer(src);

        while (!deque.isEmpty()) {
            int u = deque.pollFirst();

            for (int[] edge : adj.get(u)) {
                int v = edge[0];
                int w = edge[1]; // 0 or 1

                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;

                    if (w == 0) {
                        deque.offerFirst(v);    // important: 0-cost → front
                    } else {
                        deque.offerLast(v);     // 1-cost  → back
                    }
                }
            }
        }

        return dist;
    }
}
```

**Important interview notes**:
- Never use visited[] array — allow multiple entries (relaxation handles it)
- Use `offerFirst` for 0, `offerLast` for 1
- Use `long` for distance (prevents overflow in large graphs)

### 30. Digit DP (Digital Dynamic Programming)

**What is it?**  
Digit-by-digit DP — used when you need to count / find numbers with certain properties in a given range [L, R].

**Classic 2025–2026 problems**:
- Count numbers with digit sum = S
- Numbers with no consecutive repeating digits
- Count beautiful numbers (sum of digits ≤ threshold)
- Numbers whose digits are non-decreasing
- Count numbers ≤ N with exactly K set bits (binary version)

**State usually looks like**:
dp[pos][tight][leading_zeros][other_states…]

- pos = current position from left (MSB)
- tight = 1 if number so far matches prefix of limit, 0 otherwise
- leading_zeros = whether we have started placing non-zero digits

**Typical Java Template (count numbers ≤ N with property)**

```java
class DigitDP {

    String S;               // string representation of upper limit N
    Long[][][] memo;        // pos, tight, leadingZero (add more dimensions as needed)

    public long countNumbers(long N, /* other params like sum, etc. */) {
        if (N < 0) return 0;
        S = Long.toString(N);
        int len = S.length();

        memo = new Long[len][2][2]; // pos, tight, leadingZero

        return dp(0, 1, 1 /*, other initial states */);
    }

    private long dp(int pos, int tight, int leadingZero /*, other states */) {
        if (pos == S.length()) {
            // base case: check if valid number formed
            return 1; // or 0 depending on condition
        }

        if (memo[pos][tight][leadingZero] != null) {
            return memo[pos][tight][leadingZero];
        }

        long ans = 0;
        int up = tight == 1 ? S.charAt(pos) - '0' : 9;

        for (int d = 0; d <= up; d++) {
            int newTight = tight & (d == up ? 1 : 0);
            int newLeadingZero = leadingZero & (d == 0 ? 1 : 0);

            // add your condition logic here
            // e.g. if (!newLeadingZero) { check digit constraints }

            ans += dp(pos + 1, newTight, newLeadingZero /*, updated states */);
        }

        return memo[pos][tight][leadingZero] = ans;
    }

    // Wrapper for range [L,R]
    public long countInRange(long L, long R) {
        return countNumbers(R) - countNumbers(L - 1);
    }
}
```

**Pro tip for interviews**:
- Always handle L-1 carefully (negative → 0)
- Memoization key must include **all changing states**
- Leading zero flag is very important when digit properties matter

### 31. Tree DP (very common hard tree problems)

**Most frequent styles in 2025–2026**

1. Max path sum (any node to any node)
2. Diameter of binary tree
3. House Robber III (max sum without adjacent nodes)
4. Maximum independent set size on tree
5. Count good nodes / paths with condition

**Standard two-pass / return style Tree DP in Java**

```java
class TreeNode {
    int val;
    TreeNode left, right;
}

class TreeDP {

    private int maxPath = Integer.MIN_VALUE;

    public int maxPathSum(TreeNode root) {
        maxGain(root);
        return maxPath;
    }

    // Returns max gain if we include this node in path going up
    private int maxGain(TreeNode node) {
        if (node == null) return 0;

        int leftGain = Math.max(maxGain(node.left), 0);
        int rightGain = Math.max(maxGain(node.right), 0);

        // Max path through current node (can be used as answer)
        int priceNewPath = node.val + leftGain + rightGain;

        // Update global max
        maxPath = Math.max(maxPath, priceNewPath);

        // Return max we can get going one direction (for parent)
        return node.val + Math.max(leftGain, rightGain);
    }

    // Diameter variant
    private int diameter = 0;

    public int diameterOfBinaryTree(TreeNode root) {
        height(root);
        return diameter;
    }

    private int height(TreeNode node) {
        if (node == null) return 0;

        int left = height(node.left);
        int right = height(node.right);

        diameter = Math.max(diameter, left + right);

        return 1 + Math.max(left, right);
    }
}
```

**Quick checklist for Tree DP**:
- Do I need global variable? (max path sum, diameter → yes)
- Do I return value to parent? (height, max gain → yes)
- States: include/exclude current node, color, group size, etc.

### Quick 2025–2026 “Pick me” problems list (Java)

If you master these 12, you cover ~70–80% of hard algorithmic interviews:

1. 239 Sliding Window Maximum (monotonic deque)
2. 212 Word Search II (Trie + backtracking)
3. 875 Koko Eating Bananas (binary search on answer)
4. 410 Split Array Largest Sum (binary search on answer)
5. 23 Merge k Sorted Lists (heap)
6. 124 Binary Tree Maximum Path Sum (tree DP)
7. 200 Number of Islands (DFS/BFS/Union-Find)
8. 2390 Longest Subarray With Maximum Bitwise AND (clever observation / monotonic)
9. 862 Shortest Subarray with Sum at Least K (deque + prefix)
10. 76 Minimum Window Substring (sliding window)
11. 42 Trapping Rain Water (two pointers / monotonic stack)
12. 968 Binary Tree Cameras (tree greedy / DP)

Let me know what you want next:

- Deep explanation + code for any of the 12 problems above  
- 0-1 BFS full problem walkthrough (with grid example)  
- Digit DP complete example (e.g. count numbers with digit sum = K)  
- Custom Heap / PriorityQueue comparator tricks in Java  
- Most common Java interview bugs / gotchas (HashMap resize, Integer caching, etc.)  
- Or just say “continue” again for more patterns

The previous message was **not incomplete** — it was a complete, structured, practical framework that experienced developers and competitive programmers actually use (and teach) to identify the right data structure + algorithm in ~30–90 seconds during a contest or interview.

But I understand what you mean:  
You want a **more complete, more exhaustive, more step-by-step version** — something closer to a **decision tree / flowchart in text form** that you can mentally replay every single time you read a new problem.

Here is the **most complete version** I usually give in workshops / to mentees — the full mental checklist people use after ~1000+ problems.

### Ultimate Problem → DS/Algo Identification Framework (2025–2026 style)

#### Phase 0 – First 15–45 seconds (forced questions – answer them silently)

1. What is the **output type** exactly?
   - single number / yes-no / count
   - one index / position
   - all matching elements / positions
   - sequence / path / arrangement
   - grouping / partitioning
   - all possible ways / combinations

2. What are the **main constraints**? (write / remember 2–3 most important)
   - n ≤ 10, 20, 40 → exponential is ok (backtracking, bitmask DP, subsets)
   - n ≤ 100–500 → O(n²) dp / floyd usually ok
   - n ≤ 10⁴–10⁵ → O(n log n) or O(n) mandatory
   - n ≤ 10⁶+ → almost always O(n) or O(n log n)

3. Are there **negative numbers** / **weights** / **cycles** possible?

4. Input already **sorted**? Or **almost sorted**? Or needs to stay sorted?

#### Phase 1 – Pattern Keyword & Phrase Matching (60–70% of problems solved here)

Read the problem once more → look for these trigger words/phrases (very strong signals)

| You see this in problem statement                                   | Almost certainly this family / technique                           | First DS to consider                           |
|---------------------------------------------------------------------|--------------------------------------------------------------------|------------------------------------------------|
| sorted array, find if exists, first/last occurrence, boundary       | Binary Search family                                               | sorted array + binary search                   |
| minimize maximum pain / load / distance / time                      | Binary Search on Answer                                            | binary search + feasibility function           |
| maximize minimum distance / beauty / score                          | Binary Search on Answer                                            | binary search + check function                 |
| top / kth largest / smallest / most frequent                        | Heap / Selection                                                   | min-heap / max-heap / quickselect              |
| sliding window, longest / maximum / minimum / at most k distinct    | Sliding Window / Two Pointers                                      | two pointers + map/set/array                   |
| next greater / next smaller / stock span / trapping rain            | Monotonic Stack / Monotonic Queue                                  | stack (increasing / decreasing)                |
| merge intervals, non-overlapping, meeting rooms                     | Interval problems                                                  | sort by start + sweep / greedy                 |
| frequency of characters / anagrams / group similar                  | Hashing / Counting                                                 | HashMap / int[256] / Counter                   |
| all subsets / all combinations / permutations / N-queens / sudoku  | Backtracking / Recursion                                           | recursion + pruning                            |
| number of islands / connected components / union groups             | Union-Find (DSU)                                                   | Union-Find + path compression + rank           |
| course prerequisites / build order / topological order              | Topological Sort                                                   | Kahn’s algorithm or DFS topo                   |
| shortest path in grid / unweighted graph / level order              | BFS (level order)                                                  | Queue + visited                                |
| shortest path with non-negative weights                             | Dijkstra                                                           | PriorityQueue                                  |
| shortest path with only 0 and 1 weights                             | 0–1 BFS                                                            | Deque (0 → front, 1 → back)                    |
| cycle detection in directed graph / deadlock                        | Topo Sort fail OR DFS color                                        | DFS / Kahn’s                                   |
| parentheses matching / undo / expression evaluation                 | Stack                                                              | Stack                                          |
| prefix search / autocomplete / dictionary / word break              | Trie                                                               | Trie                                           |
| range sum / range min / range max query + point update              | Segment Tree / Fenwick Tree / Sparse Table                         | Segment Tree or Binary Indexed Tree            |
| numbers from 1 to N / digit constraints / count numbers ≤ N        | Digit DP                                                           | DP[pos][tight][leading_zero][states…]          |
| tree + max path / diameter / independent set / cameras              | Tree DP                                                            | recursion returning multiple values            |
| grid path counting / min cost path / unique paths                   | DP on grid                                                         | 2D DP or Dijkstra if weighted                  |

#### Phase 2 – If no strong pattern matched yet (the harder 30%)

Ask these elimination questions in this order:

1. **Is n very small?** (≤ 20–25)  
   → Try **exponential time** first: backtracking / subsets / permutations / bitmask DP

2. **Does the problem smell like optimization on subarrays / substrings / windows?**  
   → Sliding window / two pointers / prefix sums / kadane

3. **Graph hidden somewhere?** (even if not explicitly said)
   - nodes = objects / people / cities / cells
   - edges = friendship / distance / can reach / same group
   → model as graph → BFS / DFS / Union-Find / Dijkstra / Topo

4. **Need to remember previous state / choices / used items?**  
   → DP (now decide dimension & state)

   Common DP state patterns:
   - dp[i] = best for first i elements
   - dp[i][j] = between i and j / using first i items & j capacity
   - dp[i][k] = position i & k previous choices / k remaining
   - dp[mask] = subset mask used
   - dp[pos][tight] = digit DP

5. **Need sorted order all the time + insert/delete?**  
   → TreeSet / TreeMap / multiset (in Java → TreeMap / TreeSet)

6. **Need fast min/max + insert/delete arbitrary?**  
   → Two heaps (max-heap + min-heap) or multiset

#### Phase 3 – Final sanity check before coding (last 30 seconds)

- Time complexity goal realistic for constraints?
- Space complexity ok? (especially map vs array)
- Edge cases obvious? (empty, single element, all same, negative, overflow)
- Can I combine two techniques? (very common in hard problems)
  - Binary Search + Sliding Window
  - Heap + HashMap
  - Union-Find + BFS
  - Trie + DFS backtracking
  - Monotonic Queue + Sliding Window

### One-page printable mental flowchart (text version)

```
Read problem once
   ↓
What is asked? (min/max/count/path/group/arrange/yes-no)
   ↓
Look at n → very small? → backtracking / bitmask DP
   ↓
Keywords match any classic phrase? → go to that family
   ↓
No clear pattern?
   ↓
Is it optimization → binary search on answer?
Is it subarray/substring → sliding / two pointers / kadane / prefix
Is it graph-like → model + BFS/DFS/Union-Find/Dijkstra/Topo
Is it counting ways/states → DP (choose dimension)
Still nothing? → greedy? sort + two pointers? two heaps?
   ↓
Write 3–4 most important constraints again
Choose top 1–2 candidates
Think if one can be combined
Start pseudocode with chosen DS
```

Use this checklist **literally every problem** for 2–3 months → your brain will start skipping steps automatically.

Which category are you currently weakest at recognizing?  
( arrays/strings, graphs, DP, trees, greedy, binary search tricks, etc.)

Here is the **most complete, exhaustive, structured version** of how to identify which data structure and which algorithm / technique to use when you face a new problem (interview, contest, real work, etc.).

This is the version many people print / keep as a one-pager after reaching 800–1500 solved problems.  
It is designed to be followed **literally step-by-step** until it becomes automatic.

### Complete Problem → DS + Algo Identification Flowchart (text version – 2025/2026 style)

**Phase 0 – First 20–60 seconds (forced questions – answer ALL of them mentally or on paper)**

1. Output type?  
   single value · yes/no · count · index/position · all matching items · sequence/path · grouping · all possible combinations/arrangements · arrangement with constraints

2. Main constraints (write the 2–3 most important ones)  
   n ≤ 10–20–25 → exponential ok  
   n ≤ 100–500 → O(n²) usually acceptable  
   n ≤ 10⁴ → O(n²) risky, prefer O(n log n)  
   n ≤ 10⁵ → O(n log n) or O(n) strongly preferred  
   n ≤ 10⁶–10⁷ → almost always O(n) or O(n log n)  
   values: small (0–100), reasonable (0–1e9), coordinates, strings, very large numbers

3. Already sorted? Almost sorted? Needs to stay sorted dynamically?

4. Negative numbers / negative weights possible?

5. Duplicates important? Or unique elements?

6. Graph hidden? (friendship = edge, can reach = edge, same group = edge, distance = edge, prerequisite = edge…)

**Phase 1 – Strongest pattern matching (this solves ~65–75% of problems instantly)**

| You read / hear these words or very similar phrasing                                 | Extremely strong signal → this family / technique first                             | Primary data structures to consider first                          |
|--------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------|--------------------------------------------------------------------|
| sorted array, find if exists, first/last position, smallest index ≥ x               | Classic Binary Search                                                                | sorted array + binary search                                       |
| minimize the maximum …, maximize the minimum …, within X days/hours/operations       | Binary Search on Answer (optimization + feasibility)                                 | binary search + check function                                     |
| top k / kth largest / kth smallest / most frequent elements                          | Heap / QuickSelect                                                                   | PriorityQueue (min/max heap), quickselect                          |
| sliding window, longest / shortest / maximum sum / at most k distinct chars          | Sliding Window + Two Pointers                                                        | two pointers + HashMap / freq array / set                          |
| next greater element, next smaller, stock span, largest rectangle in histogram      | Monotonic Stack / Monotonic Queue                                                    | stack (strictly increasing / decreasing)                           |
| merge intervals, non-overlapping intervals, meeting rooms, insert interval          | Interval / Sweep line                                                                | sort by start time + greedy sweep                                  |
| anagrams, group by frequency, two sum / three sum / subarray sum = k                | Hashing / Frequency counting                                                         | HashMap, int[256] or long[ ], Counter                              |
| all subsets, all combinations, permutations, N-queens, sudoku solver, word break II | Backtracking / Recursion with pruning                                                | recursion + used[] / bitmask                                       |
| number of islands, connected components, union of groups, cycle in undirected graph | Union-Find (Disjoint Set Union)                                                      | Union-Find + path compression + union by rank/size                 |
| course schedule, prerequisites, build order, topological order                      | Topological Sort                                                                     | Kahn’s BFS (indegree) or DFS finishing time                        |
| shortest path in grid / maze / unweighted graph / same level                        | BFS (level-order traversal)                                                          | Queue + visited array / set                                        |
| shortest path with non-negative weights                                             | Dijkstra                                                                             | PriorityQueue (min-heap)                                           |
| shortest path with only 0-1 edge weights                                            | 0-1 BFS                                                                              | Deque (0-cost → push_front, 1-cost → push_back)                    |
| cycle in directed graph, deadlock possibility                                       | Topological sort fails OR DFS with color/recursion stack                             | DFS color (white/gray/black) or Kahn’s fail                        |
| parentheses validation, expression evaluation, undo/redo, monotonic call stack      | Stack                                                                                | Stack (ArrayDeque in Java)                                         |
| autocomplete, prefix search, dictionary words, word break, replace words            | Trie (prefix tree)                                                                   | Trie node with children map/array + isEnd flag                     |
| range sum query + point update, range min/max + update                              | Segment Tree / Fenwick Tree (Binary Indexed Tree)                                    | Segment Tree array or Fenwick tree                                 |
| count numbers ≤ N with digit constraints (sum of digits, no consecutive, etc.)      | Digit DP                                                                             | DP[pos][tight][leading_zero][other states]                         |
| tree: max path sum any nodes, diameter, house robber on tree, cameras on tree       | Tree DP                                                                              | recursion returning 1–3 values (include/exclude, height, etc.)     |
| grid path count, min cost path, unique paths with obstacles                         | DP on grid / BFS if weighted                                                         | 2D DP array or Dijkstra if weighted                                |

**Phase 2 – When no strong keyword match (the remaining ~25–35%)**

Follow this exact elimination order:

1. n ≤ 20–25?  
   → Yes → backtracking / generate all subsets / permutations / bitmask DP first

2. Smells like subarray / substring / window with condition?  
   → Yes → sliding window / two pointers / prefix sum / kadane variants first

3. Smells like graph even if not said explicitly?  
   (objects = nodes, relation = edge, can reach = path, same group = connected, prerequisite = directed edge)  
   → Yes → model as graph → choose BFS / DFS / Union-Find / Dijkstra / Topo depending on weighted / directed / unweighted

4. Need to remember previous choices / used items / remaining capacity?  
   → Yes → Dynamic Programming  
   Then ask sub-questions:
   - position in array? → 1D dp[i]
   - two sequences / intervals? → 2D dp[i][j]
   - knapsack-like capacity? → dp[i][capacity]
   - subset of items? → dp[mask]
   - digits from left to right? → digit DP
   - tree nodes? → tree DP (return multiple values)

5. Need elements always sorted + fast insert/delete?  
   → TreeSet / TreeMap / multiset (in Java: TreeSet / TreeMap)

6. Need fast min & max together + insert/delete arbitrary elements?  
   → Two heaps technique (max-heap for lower half + min-heap for upper half)

7. Greedy choice property obvious? (sort + pick, largest/smallest first, interval greedy)  
   → Try greedy after sorting

**Phase 3 – Final 30-second sanity check before writing code**

- Target time complexity realistic for n?  
- Space ok? (map vs array vs 2D array size)  
- Obvious edge cases? (n=0/1, all equal, negative values, overflow long vs int)  
- Can combine two techniques? (very common in medium-hard)  
  Examples seen in 2024–2026:  
  • Binary Search + Sliding Window  
  • Heap + HashMap (top k frequent)  
  • Union-Find + BFS  
  • Trie + Backtracking  
  • Monotonic Queue + Sliding Window  
  • Prefix Sum + HashMap  
  • Binary Search + Greedy feasibility

### Ultra-condensed one-liner intuition triggers (after ~1000 problems this becomes muscle memory)

- sorted + search → binary search  
- min/max something → binary search on answer  
- top k / frequent → heap  
- window / subarray condition → sliding / two pointers  
- next/previous greater/smaller → monotonic stack  
- connectivity / groups → union-find  
- shortest same-level → BFS  
- weighted shortest non-neg → dijkstra  
- 0-1 weights → 0-1 BFS  
- dependencies → topo sort  
- prefix words → trie  
- range queries + updates → segment / fenwick  
- count numbers with property → digit DP  
- tree path / sum / selection → tree DP  
- all arrangements → backtracking

Use this full checklist **on every problem** for the next 50–100 problems you solve.  
After that it becomes mostly subconscious.

If you want, tell me:
- which category you currently misclassify most often (arrays/strings, graphs, trees, DP, greedy, binary search, etc.)
- or give me 2–3 recent problems you struggled to classify

You're asking how to identify **which data structure and algorithm** a problem from **WITCH companies** (or typical service-based companies) is likely testing / expecting.

In other words:  
When you get a coding question in a TCS / Infosys / Wipro / Cognizant / HCL interview (or similar mass-recruiter service-based company), how do you quickly figure out “ah, this is a classic array + two pointers” or “this is linked list cycle detection” etc.?

Here is a realistic, narrowed-down guide specifically tuned for **WITCH / service-based company interviews in 2025–2026**.

### Quick Reality Check – What They Actually Ask (2024–2026 pattern)

| Difficulty | Frequency | Typical topics they reach | LeetCode difficulty equivalent |
|:-----------|:----------|:--------------------------|:--------------------------------|
| Easy       | ~70–80%   | Very high                 | Easy                            |
| Easy-Medium| ~15–25%   | Common                    | Easy-Medium                     |
| Medium     | ~5–10%    | Sometimes (good candidates / elite tracks) | Medium (rarely hard)     |
| Medium-Hard / Hard | <5%     | Very rare (only in specialist / prime / digital / off-campus drives) | Almost never     |

They **very rarely** go into heavy DP, graphs (Dijkstra, topo sort with cycle), segment trees, tries with backtracking, bitmask DP, digit DP, monotonic queues, etc.

### Most Reliable Identification Triggers for WITCH-level questions

Use this short list — it covers **~90–95% of coding questions** you will actually face in service-based company technical rounds.

| You see these kinds of phrases / requirements in the problem | Almost certainly this category | Most common DS used | Typical operations they expect |
|--------------------------------------------------------------|----------------------------------|----------------------|---------------------------------|
| Find if element exists, count frequency, sum of pairs, group by value, anagrams | Hashing / Frequency | HashMap / int[] / HashSet | O(n) time, O(n) space |
| Sorted array, search, find first/last occurrence, floor/ceiling | Binary Search (classic) | sorted array | log n search, boundary variants |
| Reverse array / string / linked list, find middle, detect cycle | Linked List basics | Linked List (sometimes array simulation) | slow-fast pointer, reverse in-place |
| Print pattern (star, number triangle, spiral), sum of rows/columns | 2D Array / Matrix basics | 2D array | boundary traversal, spiral order |
| Remove duplicates from sorted array, move zeros to end, sort 0s 1s 2s | Two Pointers (simple) | Array + two pointers | in-place modification, O(1) extra space |
| Find pair with given sum, subarray with sum = k (positive numbers) | Two Pointers / Hashing | Two pointers or HashSet + prefix | O(n) or O(n log n) |
| Maximum subarray sum, longest substring without repeating chars | Kadane / Sliding Window (easy version) | Array + variables | O(n) single pass |
| Find duplicates in array (1 to n range), missing number 1 to n | Array math / XOR / Cycle sort idea | Array | O(1) space tricks |
| Reverse words in string, valid palindrome (ignore case/space) | String manipulation | String / StringBuilder | two pointers from ends |
| Check balanced parentheses, next greater element (simple) | Stack (easy uses) | Stack | push/pop, monotonic increasing |
| Level order traversal, sum of nodes at level k | Tree basics (very rare) | Queue (BFS) | level-wise processing |
| Sort array (they sometimes ask to implement bubble/insertion/selection) | Basic sorting | Array | Understand time complexity |
| Find min/max in array, second largest, rotate array by k | Simple array traversal | Array | O(n) or O(1) space rotate |

### Fast Mental Checklist for WITCH interviews (30–60 seconds)

1. Is the input an **array/string** and they ask something about **existence / count / pair / frequency**?  
   → HashMap or frequency array first

2. Input is **sorted** or they say “sorted array”?  
   → Binary search (or two pointers)

3. Words like **“remove duplicates”, “move zeros”, “sort colors (0,1,2)”, “partition”**?  
   → Two pointers (Dutch National Flag style or simple left-right)

4. Words like **“reverse”, “palindrome”, “middle element”, “cycle”**?  
   → Linked list (slow-fast pointer or reverse)

5. Words like **“subarray sum”, “longest substring”, “without repeating”**?  
   → Sliding window or prefix sum + map

6. Words like **“maximum sum”, “contiguous”**?  
   → Kadane’s algorithm (very common easy-medium)

7. Words like **“parentheses”, “undo”, “matching”**?  
   → Stack

8. Words like **“pattern printing”, “spiral”, “matrix rotation”**?  
   → 2D array traversal / boundary logic

9. Words like **“missing number”, “duplicate in 1 to n”, “first missing positive”**?  
   → Array index as hash / math formula / XOR

10. Very rarely: **“tree traversal”, “level sum”, “graph connectivity”**  
    → Queue (BFS) or basic recursion

### Quick “cheat codes” people use in WITCH interviews

- If n ≤ 10⁵ and they want **O(n)** or **O(n log n)** → almost always hashing or sorting + two pointers
- If they say **“without extra space”** or **“in-place”** → two pointers or swap/index tricks
- If input range is **1 to n** or **small numbers** → array index manipulation very likely
- If they give **string and ask about characters** → 256-size array or HashMap
- If they ask **“implement bubble/selection/insertion sort”** → they just want to see loop understanding

### Summary – Top 8 categories that cover ~92% of WITCH coding questions

1. HashMap / frequency counting  
2. Two pointers (remove duplicates, move zeros, sort colors, pair sum)  
3. Binary Search (sorted array variants)  
4. Sliding Window / Kadane (easy versions)  
5. Linked List basics (reverse, cycle, middle)  
6. Stack (parentheses, next greater simple)  
7. 2D array patterns / matrix traversal  
8. Simple array math tricks (missing number, duplicates)

If you prepare **only these 8 categories well** (≈ 120–180 LeetCode easy + easy-medium problems), you are already stronger than most candidates in WITCH / service-based technical rounds.

Here are three separate lists/answers to the three options you mentioned.

Pick whichever one(s) you want to focus on more deeply.

### 1. 40–50 most repeated problems in WITCH interviews  
(≈ LeetCode-style names + topic + difficulty as seen in 2023–2026 WITCH drives)

| #  | Problem (common name / description)                              | Main Topic / Pattern                          | LeetCode-like difficulty | Very common in |
|----|------------------------------------------------------------------|-----------------------------------------------|---------------------------|----------------|
| 1  | Two Sum                                                          | HashMap / pair sum                            | Easy                      | almost all     |
| 2  | Remove Duplicates from Sorted Array                              | Two Pointers                                  | Easy                      | almost all     |
| 3  | Remove Element (remove all occurrences of a value)               | Two Pointers                                  | Easy                      | very common    |
| 4  | Move Zeroes                                                      | Two Pointers                                  | Easy                      | very common    |
| 5  | Sort Colors (Dutch National Flag – 0,1,2)                        | Two / Three Pointers                          | Medium                    | common         |
| 6  | Best Time to Buy and Sell Stock                                  | One pass / min tracking                       | Easy                      | very common    |
| 7  | Maximum Subarray (Kadane's)                                      | Kadane / dynamic programming simple           | Easy–Medium               | very common    |
| 8  | Merge Sorted Array (in-place into first array)                   | Two Pointers from end                         | Easy                      | very common    |
| 9  | Majority Element (Boyer-Moore or sorting)                        | Boyer-Moore / frequency                       | Easy                      | common         |
|10  | Rotate Array by k steps                                          | Reverse trick or juggling                     | Medium                    | common         |
|11  | Find the Duplicate Number (1 to n with one duplicate)            | Floyd cycle / array as hash                   | Medium                    | common         |
|12  | Missing Number (0 to n)                                          | XOR / sum formula                             | Easy                      | very common    |
|13  | First Missing Positive                                           | Array index as hash                           | Hard (but easy version asked) | sometimes   |
|14  | Intersection of Two Arrays                                       | HashSet                                       | Easy                      | common         |
|15  | Valid Anagram                                                    | Frequency array / HashMap                     | Easy                      | very common    |
|16  | Group Anagrams                                                   | HashMap with sorted key                       | Medium                    | somewhat common|
|17  | Valid Palindrome (ignore non-alphanum, case insensitive)         | Two Pointers                                  | Easy                      | common         |
|18  | Reverse String                                                   | Two Pointers swap                             | Easy                      | very common    |
|19  | Reverse Words in a String                                        | Split + reverse + join                        | Medium                    | common         |
|20  | Longest Common Prefix                                            | Horizontal scan / vertical scan               | Easy                      | common         |
|21  | Valid Parentheses                                                | Stack                                         | Easy                      | very common    |
|22  | Min Stack (design stack with getMin in O(1))                     | Two stacks or one stack with pair             | Medium                    | somewhat common|
|23  | Next Greater Element I                                           | Monotonic stack (simple version)              | Easy–Medium               | sometimes      |
|24  | Implement strStr() / IndexOf (KMP not needed)                    | Brute force or simple loop                    | Easy                      | common         |
|25  | Longest Substring Without Repeating Characters                   | Sliding Window + HashSet                      | Medium                    | common         |
|26  | Contains Duplicate                                               | HashSet                                       | Easy                      | very common    |
|27  | Single Number (all appear twice except one)                      | XOR trick                                     | Easy                      | common         |
|28  | Plus One (digits array)                                          | Carry simulation                              | Easy                      | common         |
|29  | Add Binary                                                       | Carry from right                              | Easy                      | common         |
|30  | Sqrt(x) (integer square root)                                    | Binary Search                                 | Easy–Medium               | common         |
|31  | Search Insert Position                                           | Binary Search                                 | Easy                      | very common    |
|32  | Find Minimum in Rotated Sorted Array                             | Binary Search modified                        | Medium                    | somewhat common|
|33  | Find Peak Element                                                | Binary Search                                 | Medium                    | sometimes      |
|34  | Count and Say                                                    | String simulation                             | Medium                    | sometimes      |
|35  | Pascal's Triangle                                                | 2D array simulation                           | Easy                      | common         |
|36  | Merge Intervals (sometimes simplified)                           | Sort + merge                                  | Medium                    | somewhat common|
|37  | Spiral Matrix                                                    | Boundary traversal                            | Medium                    | somewhat common|
|38  | Set Matrix Zeroes                                                | In-place markers                              | Medium                    | sometimes      |
|39  | Rotate Image (90 degrees)                                        | Transpose + reverse rows                      | Medium                    | sometimes      |
|40  | Word Search (very basic version – no Trie needed)                | DFS backtracking                              | Medium                    | rare but asked |
|41  | Implement Queue using Stacks                                     | Two stacks                                    | Easy                      | sometimes      |
|42  | Implement Stack using Queues                                     | Two queues                                    | Easy                      | rare           |
|43  | Linked List Cycle                                                | Floyd slow-fast pointer                       | Easy                      | common         |
|44  | Middle of the Linked List                                        | Slow-fast pointer                             | Easy                      | very common    |
|45  | Reverse Linked List                                              | Iterative / recursive                         | Easy                      | very common    |
|46  | Merge Two Sorted Lists                                           | Dummy node or recursive                       | Easy                      | common         |
|47  | Intersection of Two Linked Lists                                 | Two pointers (length diff)                    | Easy                      | sometimes      |
|48  | Remove Nth Node From End of List                                 | Slow-fast pointer                             | Medium                    | common         |
|49  | Palindrome Linked List                                           | Reverse half + compare                        | Easy                      | somewhat common|
|50  | Bubble Sort / Selection Sort / Insertion Sort implementation     | Basic sorting algorithms                      | Easy                      | very common (theory + code) |

### 2. Smaller “must-do” list for TCS / Infosys / Wipro / Cognizant  
(≈ 20–25 most repeated + highest probability)

1. Two Sum  
2. Remove Duplicates from Sorted Array  
3. Move Zeroes  
4. Sort Colors / Dutch National Flag  
5. Maximum Subarray (Kadane)  
6. Best Time to Buy and Sell Stock  
7. Merge Sorted Array  
8. Valid Parentheses  
9. Valid Anagram  
10. Reverse String  
11. Valid Palindrome  
12. Contains Duplicate  
13. Single Number (XOR)  
14. Missing Number  
15. Search Insert Position  
16. Sqrt(x)  
17. Longest Substring Without Repeating Characters  
18. Reverse Linked List  
19. Middle of the Linked List  
20. Linked List Cycle  
21. Remove Nth Node From End  
22. Merge Two Sorted Lists  
23. Spiral Matrix / Pascal's Triangle  
24. Plus One  
25. Implement strStr() or basic string search

→ If you can solve these 25 cleanly in <20–25 minutes each → you are already above average for WITCH technical rounds.

### 3. How questions differ: on-campus mass hiring vs off-campus / elite track (2025–2026)

| Aspect                          | On-campus mass hiring (TCS NQT, Infosys InfyTQ, Wipro Elite, etc.) | Off-campus / normal drive | Elite / Prime / Digital / Specialist tracks |
|---------------------------------|---------------------------------------------------------------------|----------------------------|---------------------------------------------|
| Coding questions count          | 1–3 (mostly 2)                                                     | 1–3                        | 2–4 (sometimes 5)                           |
| Difficulty                      | Easy – Easy-Medium (rarely medium)                                  | Easy-Medium                | Medium (sometimes medium-hard)              |
| Topics covered                  | Arrays, strings, basic hashing, two pointers, simple linked list   | + Kadane, basic stack      | + Sliding window, monotonic stack, trees (basic), greedy |
| Time per question               | 15–30 min each                                                     | 20–40 min                  | 30–60 min                                   |
| Platform                        | TCS NQT platform / HackerRank / own platform                        | HackerRank / Codility      | HackerRank / LeetCode-style / own           |
| Other sections weight           | Aptitude + verbal + reasoning very heavy                           | Moderate                   | Less weight — coding matters more           |
| DSA depth                       | Surface level (implementation focus)                                | Slightly deeper            | Closer to product-company easy-medium       |
| Probability of linked list      | High                                                               | High                       | Medium–high                                 |
| Probability of tree/graph       | Very low (almost never)                                            | Low                        | Medium (basic BFS/DFS possible)             |
| Probability of DP               | Almost never                                                       | Very rare                  | Low–medium (kadane / simple 1D)             |
| Probability of greedy           | Very low                                                           | Low                        | Medium                                      |

Short summary:

- **Mass on-campus** → prepare top 20–25 easy problems from the small list above  
- **Off-campus normal** → same + kadane + basic stack + simple sliding window  
- **Elite / Prime / Digital** → add medium sliding window, monotonic stack, basic binary search variants, simple greedy, very basic trees

Here is a consolidated, detailed response covering **all three** requests you asked for regarding WITCH (and similar service-based) company interview preparation in the current context (early 2026 trends).

### 1. 40–50 Most Repeated Problems in WITCH Interviews (2024–2026)

This list is based on patterns reported from on-campus/off-campus drives of TCS, Infosys, Wipro, Cognizant, HCLTech, Accenture, Capgemini, Tech Mahindra, etc.

| #  | Problem Title / Description                              | Core Topic / Pattern                  | LeetCode Equivalent Difficulty | Frequency in WITCH Drives |
|----|----------------------------------------------------------|----------------------------------------|--------------------------------|----------------------------|
| 1  | Two Sum                                                  | HashMap + pair sum                     | Easy                           | Extremely High             |
| 2  | Remove Duplicates from Sorted Array                      | Two Pointers                           | Easy                           | Extremely High             |
| 3  | Move Zeroes                                              | Two Pointers                           | Easy                           | Extremely High             |
| 4  | Sort Colors (0s, 1s, 2s)                                 | Dutch National Flag / Three Pointers   | Medium                         | High                       |
| 5  | Best Time to Buy and Sell Stock                          | One pass min tracking                  | Easy                           | Very High                  |
| 6  | Maximum Subarray Sum (Kadane)                            | Kadane’s Algorithm                     | Easy–Medium                    | Very High                  |
| 7  | Merge Sorted Array (in-place)                            | Two Pointers from end                  | Easy                           | Very High                  |
| 8  | Valid Parentheses                                        | Stack                                  | Easy                           | Very High                  |
| 9  | Valid Anagram                                            | Frequency count / HashMap              | Easy                           | Very High                  |
|10  | Reverse String                                           | Two Pointers swap                      | Easy                           | Very High                  |
|11  | Valid Palindrome (ignore case, non-alphanum)             | Two Pointers                           | Easy                           | High                       |
|12  | Contains Duplicate                                       | HashSet                                | Easy                           | Very High                  |
|13  | Single Number (XOR trick)                                | Bitwise XOR                            | Easy                           | High                       |
|14  | Missing Number (0 to n)                                  | XOR / Sum formula                      | Easy                           | Very High                  |
|15  | Search Insert Position                                   | Binary Search                          | Easy                           | Very High                  |
|16  | Sqrt(x) – integer square root                            | Binary Search                          | Easy–Medium                    | High                       |
|17  | Longest Substring Without Repeating Characters           | Sliding Window + HashSet               | Medium                         | High                       |
|18  | Reverse Linked List                                      | Iterative / Recursive reversal         | Easy                           | Very High                  |
|19  | Middle of the Linked List                                | Slow-Fast Pointer                      | Easy                           | Very High                  |
|20  | Linked List Cycle (Floyd’s cycle detection)              | Slow-Fast Pointer                      | Easy                           | High                       |
|21  | Remove Nth Node From End of List                         | Slow-Fast Pointer                      | Medium                         | High                       |
|22  | Merge Two Sorted Lists                                   | Dummy node / Recursive                 | Easy                           | High                       |
|23  | Spiral Matrix (print in spiral order)                    | Boundary traversal                     | Medium                         | Medium–High                |
|24  | Pascal’s Triangle                                        | 2D array construction                  | Easy                           | Medium–High                |
|25  | Plus One (digits array)                                  | Carry simulation                       | Easy                           | High                       |
|26  | Rotate Array by k steps                                  | Reverse three parts                    | Medium                         | Medium                     |
|27  | Majority Element                                         | Boyer-Moore Voting                     | Easy                           | Medium                     |
|28  | Intersection of Two Arrays                               | HashSet                                | Easy                           | Medium                     |
|29  | Group Anagrams                                           | HashMap + sorted string key            | Medium                         | Medium                     |
|30  | Find the Duplicate Number (1 to n)                       | Floyd cycle / array as hash            | Medium                         | Medium                     |
|31  | First Missing Positive                                   | Array index as hash                    | Hard (simplified version asked)| Low–Medium                 |
|32  | Next Greater Element I (simple version)                  | Monotonic Stack                        | Easy–Medium                    | Low–Medium                 |
|33  | Implement strStr() / Index of substring                  | Simple loop / Brute force              | Easy                           | Medium                     |
|34  | Add Binary                                               | Carry from right                       | Easy                           | Medium                     |
|35  | Count and Say                                            | String simulation                      | Medium                         | Low–Medium                 |
|36  | Set Matrix Zeroes                                        | In-place with markers                  | Medium                         | Low–Medium                 |
|37  | Rotate Image (90 degrees)                                | Transpose + reverse rows               | Medium                         | Low–Medium                 |
|38  | Word Search (basic DFS, no Trie needed)                  | DFS backtracking                       | Medium                         | Low                        |
|39  | Implement Queue using Stacks                             | Two stacks                             | Easy                           | Low                        |
|40  | Palindrome Linked List                                   | Reverse half + compare                 | Easy–Medium                    | Medium                     |
|41–50 | Basic sorting implementations (Bubble, Selection, Insertion, Merge sort pseudo) | Sorting algorithms theory & code | Easy                           | Very High (especially theory) |

### 2. Smaller “Must-Do” List (Top 20–25) – Enough for 85–90% Coverage

If you have limited time (2–4 weeks prep), focus only on these:

1. Two Sum  
2. Remove Duplicates from Sorted Array  
3. Move Zeroes  
4. Sort Colors  
5. Maximum Subarray (Kadane)  
6. Best Time to Buy and Sell Stock  
7. Merge Sorted Array  
8. Valid Parentheses  
9. Valid Anagram  
10. Reverse String  
11. Valid Palindrome  
12. Contains Duplicate  
13. Single Number  
14. Missing Number  
15. Search Insert Position  
16. Sqrt(x)  
17. Longest Substring Without Repeating Characters  
18. Reverse Linked List  
19. Middle of the Linked List  
20. Linked List Cycle  
21. Remove Nth Node From End  
22. Merge Two Sorted Lists  
23. Spiral Matrix / Pascal’s Triangle  
24. Plus One  
25. Implement Bubble / Selection / Insertion Sort

→ Solve these 25 problems → write clean code → explain time & space → handle all edge cases → you’ll clear almost all WITCH coding rounds.

### 3. How Questions Differ: On-Campus Mass Hiring vs Off-Campus vs Elite/Prime/Digital Tracks

| Aspect                     | On-Campus Mass Hiring (NQT, InfyTQ, Elite National, Superset, etc.) | Normal Off-Campus / Walk-in Drives | Elite / Prime / Digital / Specialist Tracks |
|----------------------------|-----------------------------------------------------------------------|-------------------------------------|---------------------------------------------|
| Number of coding questions | 1–3 (usually 2)                                                      | 1–3                                 | 2–5                                         |
| Difficulty                 | Easy → Easy-Medium (rarely crosses medium)                            | Easy-Medium                         | Medium (occasionally easy-medium-hard)      |
| Time per question          | 15–30 minutes                                                        | 20–45 minutes                       | 30–60 minutes                               |
| Main platforms             | TCS NQT platform, HackerRank, Mettl, own portal                       | HackerRank, Codility                | HackerRank, LeetCode-style, own advanced    |
| DSA depth                  | Surface-level implementation focus                                    | Slightly deeper                     | Closer to product-company easy-medium       |
| Most common topics         | Arrays, strings, hashing, two pointers, basic linked list             | + Kadane, basic stack, sliding window | + Monotonic stack, greedy, basic trees, simple DP |
| Probability of trees/graphs| Almost never                                                         | Low (5–10%)                         | Medium (15–30%)                             |
| Probability of DP          | Almost never                                                         | Very rare                           | Low–Medium (Kadane, simple 1D, LCS variant) |
| Probability of greedy      | Very low                                                             | Low                                 | Medium                                      |
| Other sections weight      | Aptitude + logical + verbal + essay very heavy                        | Moderate                            | Lower weight — coding + projects matter more|
| Communication / HR round   | Very important (mass filtering)                                       | Important                           | Important but technical carries more weight |
| Chance of pseudo-code only | High (especially TCS NQT)                                            | Medium                              | Low                                         |

**Quick Summary Table – Preparation Strategy**

| Your Target                     | Recommended Preparation Level                  | Must Cover Problems Count | Expected Success Rate |
|---------------------------------|------------------------------------------------|-----------------------------------|-----------------------|
| Mass on-campus WITCH            | Easy + some easy-medium                        | 20–30                             | 80–95%                |
| Normal off-campus               | Easy + easy-medium + few medium                | 30–50                             | 70–90%                |
| Elite/Prime/Digital track       | Easy-medium + medium + few medium-hard         | 60–120                            | 50–80% (competitive)  |

If you want, I can now:

- Give Java code + explanation + edge cases for any 5–10 problems from the list  
- Provide a 2-week / 4-week preparation roadmap for WITCH  
- Explain how to answer “explain time & space complexity” in interviews  
- Share common follow-up questions they ask after these problems






