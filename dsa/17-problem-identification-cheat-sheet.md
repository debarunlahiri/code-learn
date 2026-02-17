# Problem Identification & DSA Cheat Sheet

**Target:** WITCH (Wipro, Infosys, TCS, Cognizant, HCL) & FAANG (Facebook, Amazon, Apple, Netflix, Google) interview prep.  
**Focus:** Quickly identify problem type, choose right data structure and algorithm.

---

## Quick Reference Table

| Problem Pattern | Key Indicators | Data Structure | Algorithm | Companies |
|----------------|---------------|----------------|-----------|----------|
| **Array/Matrix** | Find/search in array, 2D grid, subarray | Array, HashMap | Two Pointers, Sliding Window, Binary Search | All |
| **String** | Pattern matching, manipulation, validation | String, HashMap, Trie | KMP, Rabin-Karp, Two Pointers | All |
| **Linked List** | Reverse, merge, cycle detection, kth node | ListNode | Two Pointers, Fast/Slow | WITCH |
| **Tree** | BST operations, traversal, LCA | TreeNode | DFS, BFS, Recursion | All |
| **Graph** | Shortest path, connectivity, cycle | Adjacency List, Matrix | BFS, DFS, Dijkstra, Union-Find | FAANG |
| **Greedy** | Optimization, scheduling, selection | Array, Heap | Sort, Greedy Choice | WITCH |
| **DP** | Optimal substructure, overlapping subproblems | Array, DP Table | Memoization, Bottom-up | FAANG |
| **Recursion/Backtracking** | Permutations, combinations, paths | Array, Tree | Backtracking | All |
| **Heap/PQ** | Top-k, min/max, scheduling | PriorityQueue | Heap Operations | FAANG |
| **Range Query** | Range sum, min/max updates | Segment Tree, BIT | Prefix Sum, Lazy Propagation | FAANG |

---

## 1. Array Problems

### When to Use Arrays
- Input is a list of numbers/strings
- Need O(1) index access
- Problem mentions "array", "list", "sequence"

### Common Patterns & Solutions

| Pattern | Keywords | Solution |
|---------|----------|---------|
| **Two Sum** | "find two numbers", "pair sum" | HashMap (O(n)) or Two Pointers (if sorted) |
| **Three Sum** | "find three numbers", "triplet" | Sort + Two Pointers (O(n²)) |
| **Subarray Sum** | "subarray sum equals k", "continuous" | Prefix Sum + HashMap (O(n)) |
| **Maximum Subarray** | "maximum sum", "contiguous" | Kadane's Algorithm (O(n)) |
| **Product Array** | "product except self" | Prefix/Suffix Products (O(n)) |
| **Merge Intervals** | "merge intervals", "schedule" | Sort + Merge (O(n log n)) |
| **Spiral Matrix** | "spiral order", "clockwise" | Four-pointer traversal |
| **Search in Matrix** | "2D search", "sorted matrix" | Start from corner, move accordingly |

### Quick Decision Tree
```
Is array sorted? → Binary Search
Need subarray sum? → Prefix Sum + HashMap
Need to merge intervals? → Sort + Two Pointers
Need product except self? → Prefix/Suffix Products
Need max subarray? → Kadane's
Need to find pairs/triplets? → HashMap/Two Pointers
```

---

## 2. String Problems

### When to Use String Techniques
- Input is text/word/sentence
- Pattern matching required
- Character manipulation

### Common Patterns & Solutions

| Pattern | Keywords | Solution |
|---------|----------|---------|
| **Pattern Search** | "find pattern", "substring" | KMP (O(n+m)) or Rabin-Karp |
| **Anagram** | "rearrange", "scramble", "anagram" | Frequency Array (O(1)) |
| **Longest Substring** | "without repeating", "unique chars" | Sliding Window + HashMap |
| **Palindrome** | "reverse same", "palindrome" | Two Pointers |
| **String to Integer** | "atoi", "convert string" | Linear scan with edge cases |
| **Valid Parentheses** | "balanced brackets", "valid expression" | Stack |
| **Longest Common Prefix** | "common prefix", "shared start" | Horizontal scanning |
| **Group Anagrams** | "group anagrams", "same letters" | HashMap with sorted string key |

### Quick Decision Tree
```
Need pattern matching? → KMP/Rabin-Karp
Need unique characters? → Sliding Window
Need palindrome check? → Two Pointers
Need bracket validation? → Stack
Need frequency count? → Array/HashMap
Need prefix matching? → Linear scan
```

---

## 3. Linked List Problems

### When to Use Linked Lists
- Problem mentions "linked list", "singly/doubly linked"
- Need efficient insert/delete at head
- Memory constraints (no random access needed)

### Common Patterns & Solutions

| Pattern | Keywords | Solution |
|---------|----------|---------|
| **Reverse** | "reverse linked list" | Three-pointer iteration |
| **Merge Two Lists** | "merge sorted lists" | Two-pointer comparison |
| **Detect Cycle** | "has cycle", "loop" | Fast/Slow Pointers |
| **Find Middle** | "middle node", "nth from end" | Fast/Slow Pointers |
| **Remove Nth** | "remove nth from end" | Two-pointer with distance |
| **Palindrome List** | "palindrome linked list" | Reverse second half + compare |
| **Intersection** | "common node", "intersection" | Hash nodes or length method |

### Quick Decision Tree
```
Need to reverse? → Three-pointer
Need cycle detection? → Fast/Slow pointers
Need middle/nth from end? → Fast/Slow pointers
Need to merge sorted lists? → Two-pointer
Need palindrome check? → Reverse + compare
```

---

## 4. Tree Problems

### When to Use Trees
- Hierarchical data (file system, organization)
- Binary search operations
- Path finding in tree structure

### Common Patterns & Solutions

| Pattern | Keywords | Solution |
|---------|----------|---------|
| **BST Operations** | "search", "insert", "delete" | BST properties |
| **Traversal** | "inorder", "preorder", "postorder" | DFS/Recursion |
| **Level Order** | "level order", "breadth first" | BFS with queue |
| **LCA** | "lowest common ancestor" | Parent pointers or Single traversal |
| **Validate BST** | "valid BST", "check BST" | Inorder check |
| **Serialize** | "serialize", "convert to string" | BFS/DFS with markers |
| **Path Sum** | "root to leaf sum" | DFS with target sum |
| **Balanced Tree** | "height balanced", "AVL" | Height calculation |

### Quick Decision Tree
```
Is BST? → Use BST properties
Need level order? → BFS
Need LCA? → Parent pointers or single traversal
Need path sum? → DFS with target
Need validation? → Inorder check
```

---

## 5. Graph Problems

### When to Use Graphs
- Networks, relationships, connections
- Shortest path, connectivity
- Dependencies, scheduling

### Common Patterns & Solutions

| Pattern | Keywords | Solution |
|---------|----------|---------|
| **BFS Shortest Path** | "shortest path", "minimum edges" | BFS (unweighted) |
| **Dijkstra** | "weighted shortest path" | Priority Queue |
| **DFS Connectivity** | "connected components", "reachability" | DFS/Union-Find |
| **Cycle Detection** | "has cycle", "loop in graph" | DFS (parent tracking) |
| **Topological Sort** | "course schedule", "dependency" | Kahn's BFS |
| **MST** | "minimum spanning tree" | Kruskal (Union-Find) or Prim |
| **Union-Find** | "connected components", "dynamic connectivity" | DSU operations |
| **Bipartite** | "bipartite graph", "possible coloring" | BFS coloring |

### Quick Decision Tree
```
Unweighted shortest path? → BFS
Weighted shortest path? → Dijkstra
Need components? → Union-Find or DFS
Need cycle detection? → DFS with parent
Need topological order? → Kahn's BFS
Need MST? → Kruskal/Prim
Need bipartite check? → BFS coloring
```

---

## 6. Greedy Problems

### When to Use Greedy
- Optimization with local optimal choices
- Scheduling, selection problems
- When greedy proof exists

### Common Patterns & Solutions

| Pattern | Keywords | Solution |
|---------|----------|---------|
| **Activity Selection** | "non-overlapping intervals" | Sort by end time |
| **Jump Game** | "can reach end", "jump array" | Track farthest reachable |
| **Huffman Coding** | "minimum cost", "merge files" | Min-Heap |
| **Fractional Knapsack** | "fractional items", "maximum value" | Sort by ratio |
| **Coin Change** | "minimum coins", "make amount" | Greedy (if canonical) |
| **Meeting Rooms** | "minimum platforms", "schedule" | Sort times + two pointers |

### Quick Decision Tree
```
Interval selection? → Sort by end time
Jump/reachability? → Track farthest
Cost minimization? → Min-Heap
Fractional selection? → Sort by ratio
```

---

## 7. Dynamic Programming

### When to Use DP
- Optimal substructure
- Overlapping subproblems
- Optimization problems

### Common Patterns & Solutions

| Pattern | Keywords | Solution |
|---------|----------|---------|
| **Fibonacci** | "ways to climb", "fib numbers" | Bottom-up array |
| **Knapsack** | "maximum value", "capacity" | 2D DP table |
| **LCS** | "longest common subsequence" | 2D DP table |
| **LIS** | "longest increasing subsequence" | Patience sorting |
| **Coin Change** | "minimum coins", "make amount" | Bottom-up DP |
| **House Robber** | "non-adjacent max sum" | DP with two states |
| **Edit Distance** | "edit distance", "transform" | 2D DP table |
| **Partition Equal** | "partition equal subset sum" | Subset sum DP |

### Quick Decision Tree
```
Linear DP? → 1D array
2D DP? → 2D array
Need optimization? → Space optimization
Need path reconstruction? → Backtrack from DP table
```

---

## 8. Heap/Priority Queue Problems

### When to Use Heaps
- Top-k elements
- Streaming data
- Priority scheduling

### Common Patterns & Solutions

| Pattern | Keywords | Solution |
|---------|----------|---------|
| **Kth Largest** | "kth largest", "top k" | Min-Heap of size k |
| **Kth Smallest** | "kth smallest", "bottom k" | Max-Heap of size k |
| **Median Stream** | "running median", "median" | Two heaps |
| **Priority Queue** | "schedule tasks", "priority" | Custom comparator |
| **Merge K Lists** | "merge k sorted lists" | Min-Heap |

### Quick Decision Tree
```
Need k largest? → Min-Heap of size k
Need k smallest? → Max-Heap of size k
Need median? → Two heaps approach
Need priority scheduling? → Custom comparator
```

---

## 9. Range Query Problems

### When to Use Range Query Structures
- Multiple queries on array
- Range updates/queries
- Static vs dynamic data

### Common Patterns & Solutions

| Pattern | Keywords | Solution |
|---------|----------|---------|
| **Range Sum** | "range sum query", "subarray sum" | Prefix Sum (static) or BIT/Segment Tree |
| **Range Min/Max** | "range minimum", "range maximum" | Sparse Table (static) or Segment Tree |
| **Point Updates** | "update element", "change value" | BIT or Segment Tree |
| **Range Updates** | "range increment", "range add" | Difference Array + BIT/Segment Tree |
| **2D Queries** | "matrix sum", "submatrix sum" | 2D Prefix Sum |

### Quick Decision Tree
```
Static array, many queries? → Prefix Sum
Dynamic updates needed? → BIT or Segment Tree
Range updates needed? → Difference Array + BIT/Segment Tree
2D matrix? → 2D Prefix Sum
```

---

## Company-Specific Focus

### WITCH Companies (Wipro, Infosys, TCS, Most Common Questions)

#### Wipro
- **Arrays:** Find missing number, rotate array, find duplicates, merge two arrays
- **Strings:** Remove duplicates, reverse words, check anagram, string manipulation
- **Linked Lists:** Find middle, reverse in groups, detect and remove cycle
- **Sorting:** Implement bubble, selection, insertion sort (understanding fundamentals)
- **Math:** Prime numbers, GCD/LCM, factorial, Armstrong number
- **Patterns:** Star patterns, number patterns, pyramid patterns
- **Basic DS:** Stack/Queue operations, HashMap usage, ArrayList basics

#### Infosys
- **Arrays:** Subarray with given sum, find pairs, rotate matrix, spiral traversal
- **Strings:** String compression, longest substring, pattern matching
- **Trees:** Mirror tree, check BST, serialize/deserialize, LCA
- **Graph:** BFS/DFS, detect cycle, shortest path in unweighted graph
- **DP:** Fibonacci, climbing stairs, house robber, coin change basics
- **Recursion:** Generate permutations/combinations, solve Sudoku, N-Queens
- **Greedy:** Activity selection, job scheduling, basic knapsack

#### TCS (Ninja/HackerRank)
- **Arrays:** Array manipulation, sorting, searching, matrix operations
- **Strings:** String operations, pattern matching, text processing
- **Trees:** Tree operations, traversals, conversions
- **Graph:** Graph traversals, shortest path, connectivity
- **DP:** Dynamic programming basics, optimization problems
- **Greedy:** Greedy algorithms, scheduling problems
- **Math:** Number theory, arithmetic operations
- **Problem Solving:** Algorithmic thinking, problem decomposition

#### Cognizant
- **Arrays:** Array operations, multi-dimensional arrays, matrix problems
- **Strings:** String algorithms, text processing, pattern matching
- **Linked Lists:** List operations, cycle detection, merging
- **Trees:** Tree algorithms, traversals, conversions
- **Graph:** Graph algorithms, network flow, connectivity
- **Sorting:** Various sorting algorithms and comparisons
- **Searching:** Search techniques and optimization

#### HCL
- **Arrays:** Array manipulation, optimization problems
- **Strings:** String algorithms and processing
- **Trees:** Tree operations and algorithms
- **Graph:** Graph theory applications
- **Math:** Mathematical problem solving
- **Algorithms:** Algorithm design and analysis

### FAANG Companies

#### Facebook (Meta)
- **Arrays:** Product of array except self, trapping rain water, sliding window maximum
- **Strings:** Longest palindromic substring, regular expression matching
- **Linked Lists:** Add two numbers, reverse nodes in groups, LRU cache
- **Trees:** Serialize/deserialize binary tree, validate BST, LCA
- **Graph:** Clone graph, course schedule, alien dictionary
- **DP:** Longest increasing subsequence, coin change, edit distance
- **Design:** Design Twitter feed, design tinyURL, design cache
- **System:** Design distributed systems, message queues, databases

#### Amazon
- **Arrays:** Container with most water, three sum, product array except self
- **Strings:** Longest substring without repeating characters, string compression
- **Trees:** Validate BST, serialize/deserialize, maximum depth
- **Graph:** Network delay time, course schedule, alien dictionary
- **Heap:** Top k frequent elements, find median from data stream
- **Greedy:** Meeting rooms, task scheduling, jump game
- **DP:** Coin change, house robber, word break problem
- **Design:** Design LRU cache, design snake game, design Twitter

#### Apple
- **Trees:** Validate BST, maximum depth, same tree, symmetric tree
- **Arrays:** Two sum, three sum, product array except self
- **Strings:** Longest palindrome substring, regular expression matching
- **Linked Lists:** Add two numbers, reverse linked list, merge two lists
- **Graph:** Clone graph, word ladder, course schedule
- **Design:** Design phone directory, design file system, design parking system
- **Recursion:** Generate parentheses, combination sum, permutation sequence
- **DP:** Unique paths, minimum path sum, longest increasing subsequence

#### Netflix
- **Arrays:** Top k frequent elements, sliding window maximum
- **Strings:** Longest substring without repeating characters
- **Trees:** Validate BST, maximum depth, same tree
- **Graph:** Clone graph, course schedule, network delay time
- **Heap:** Find median from data stream, top k frequent elements
- **Design:** Design video rental system, design movie recommendation
- **System:** Design distributed systems, caching strategies

#### Google
- **Arrays:** Product of array except self, trapping rain water, sliding window
- **Strings:** Longest palindrome substring, regular expression matching
- **Trees:** Serialize/deserialize binary tree, validate BST, LCA
- **Graph:** Clone graph, course schedule, alien dictionary, word ladder
- **DP:** Regular expression matching, edit distance, wildcard matching
- **Design:** Design LRUCache, design Twitter, design tinyURL
- **System:** Design distributed systems, consensus algorithms, load balancers
- **Advanced:** Advanced algorithms, optimization problems, system design

---

## Quick Algorithm Selection Guide

### By Problem Type

#### Search Problems
```
Array is sorted? → Binary Search (O(log n))
Array unsorted? → Linear Search (O(n)) or HashMap (O(1))
Pattern in string? → KMP (O(n+m)) or Rabin-Karp
Search in tree? → BST search (O(h)) or DFS/BFS
Search in graph? → BFS (shortest path) or DFS
```

#### Optimization Problems
```
Local optimal works? → Greedy (prove with exchange argument)
Overlapping subproblems? → DP (memoization or bottom-up)
Need global optimum? → DP/Graph algorithms
Multiple constraints? → DP with multiple states
```

#### Path Problems
```
Unweighted graph? → BFS (O(V+E))
Weighted graph, positive weights? → Dijkstra (O(E + V log V))
Negative weights? → Bellman-Ford (O(VE))
All pairs shortest path? → Floyd-Warshall (O(V³))
Single source, DAG? → Topological order + DP
```

#### Range Problems
```
Static array, many queries? → Prefix Sum (O(1) per query)
Point updates needed? → BIT/Segment Tree (O(log n) per update)
Range updates needed? → Difference Array + BIT/Segment Tree
2D matrix queries? → 2D Prefix Sum (O(1) per query)
Range min/max queries? → Sparse Table (static) or Segment Tree (dynamic)
```

#### Subsequence/Subarray Problems
```
Longest increasing subsequence? → Patience sorting (O(n log n))
Maximum subarray sum? → Kadane's algorithm (O(n))
Subarray with given sum? → Prefix Sum + HashMap (O(n))
All subsequences? → Backtracking (O(2^n))
Longest common subsequence? → DP (O(m*n))
```

#### Permutation/Combination Problems
```
All permutations? → Backtracking (O(n!))
All combinations? → Backtracking (O(2^n))
Next permutation? → Lexicographic algorithm (O(n))
Combination sum? → Backtracking with pruning
```

#### Tree Problems
```
Tree traversal? → DFS (preorder, inorder, postorder) or BFS (level order)
BST operations? → BST properties (O(h))
Lowest common ancestor? → Parent pointers or single traversal
Tree serialization? → BFS/DFS with markers
Tree is balanced? → Height calculation (O(n))
Tree is BST? → Inorder traversal check (O(n))
```

#### Graph Problems
```
Graph traversal? → BFS/DFS (O(V+E))
Connected components? → Union-Find or DFS (O(V+E))
Cycle detection? → DFS with parent tracking (O(V+E))
Topological sort? → Kahn's algorithm (O(V+E))
Minimum spanning tree? → Kruskal (O(E log E)) or Prim (O(E log V))
Maximum flow? → Ford-Fulkerson or Edmonds-Karp (O(VE * max_flow))
```

#### String Problems
```
Pattern matching? → KMP (O(n+m)) or Rabin-Karp (average O(n+m))
Anagram? → Frequency array (O(n))
Palindrome? → Two pointers (O(n))
Longest substring without repeats? → Sliding window (O(n))
String to integer? → Linear scan (O(n))
Edit distance? → DP (O(m*n))
```

#### Greedy Problems
```
Activity selection? → Sort by end time (O(n log n))
Fractional knapsack? → Sort by ratio (O(n log n))
Huffman coding? → Min-heap (O(n log n))
Job scheduling? → Sort by deadline/profit (O(n log n))
Coin change (canonical)? → Greedy (O(n))
```

---

## Advanced Problem Patterns

### 1. Two Pointers Technique
**When to use:**
- Sorted arrays
- Finding pairs/triplets
- Merging sorted arrays
- Container with most water

**Pattern:**
```java
left = 0, right = n-1
while (left < right) {
    // Process arr[left], arr[right]
    if (condition) left++;
    else right--;
}
```

### 2. Sliding Window
**When to use:**
- Subarray/substring problems
- Fixed/variable window size
- Longest/shortest with constraints

**Pattern:**
```java
left = 0
for (right = 0; right < n; right++) {
    // Add arr[right] to window
    while (window_condition_violated) {
        // Remove arr[left] from window
        left++;
    }
    // Update answer
}
```

### 3. Fast and Slow Pointers
**When to use:**
- Cycle detection
- Find middle element
- Find nth from end
- Circular linked list

**Pattern:**
```java
slow = head, fast = head
while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;
}
```

### 4. Merge Intervals
**When to use:**
- Overlapping intervals
- Meeting rooms
- Schedule conflicts

**Pattern:**
```java
sort(intervals by start)
for (interval in intervals) {
    if (interval.start <= last.end) {
        merge intervals
    } else {
        add to result
    }
}
```

### 5. In-place Reversal
**When to use:**
- Reverse array/string
- Reverse linked list
- Reverse words in string

**Pattern:**
```java
left = 0, right = n-1
while (left < right) {
    swap(arr[left], arr[right])
    left++; right--;
}
```

### 6. Monotonic Stack
**When to use:**
- Next greater element
- Largest rectangle in histogram
- Trapping rain water
- Temperature problems

**Pattern:**
```java
Stack<Integer> stack = new Stack<>();
for (int i = 0; i < n; i++) {
    while (!stack.isEmpty() && arr[i] > arr[stack.peek()]) {
        // Process stack.pop()
    }
    stack.push(i);
}
```

### 7. Boyer-Moore Voting
**When to use:**
- Majority element (> n/2)
- Find element appearing > n/3 times

**Pattern:**
```java
int candidate = 0, count = 0;
for (int num : nums) {
    if (count == 0) candidate = num;
    count += (num == candidate) ? 1 : -1;
}
```

---

## Complexity Analysis Guide

### Time Complexity Hierarchy (Best to Worst)
```
O(1) < O(log n) < O(n) < O(n log n) < O(n²) < O(n³) < O(2^n) < O(n!)
```

### When Each Complexity is Acceptable
- **O(1):** Perfect, always acceptable
- **O(log n):** Excellent, acceptable for any input size
- **O(n):** Good, acceptable for up to 10^7 elements
- **O(n log n):** Good, acceptable for up to 10^6 elements
- **O(n²):** Acceptable only for n ≤ 10^4
- **O(n³):** Acceptable only for n ≤ 500
- **O(2^n):** Acceptable only for n ≤ 20
- **O(n!):** Acceptable only for n ≤ 10

### Space Complexity Guidelines
- **O(1):** In-place algorithms
- **O(log n):** Recursive stack depth
- **O(n):** Linear space, usually acceptable
- **O(n²):** Quadratic space, avoid for large n
- **O(2^n):** Exponential space, usually not acceptable

---

## Common Interview Gotchas

### Array Problems
- Check for empty/null arrays
- Handle single element cases
- Consider overflow for large numbers
- Remember 0-based vs 1-based indexing

### String Problems
- Handle empty strings
- Consider Unicode characters
- Watch for null pointer exceptions
- String immutability in Java

### Linked List Problems
- Handle null head
- Remember to break cycles
- Consider using dummy node
- Be careful with next pointers

### Tree Problems
- Handle null root
- Consider tree height (stack overflow)
- Remember BST properties
- Handle duplicate values

### Graph Problems
- Handle disconnected components
- Consider self-loops
- Watch for infinite loops
- Use visited array/set

### DP Problems
- Define base cases clearly
- Consider space optimization
- Handle large numbers (use long)
- Initialize DP table properly

---

## Quick Reference for Common Operations

### Array Operations
```java
// Sort
Arrays.sort(arr);                    // O(n log n)

// Binary Search
int idx = Arrays.binarySearch(arr, target);  // O(log n)

// Copy
int[] copy = Arrays.copyOf(arr, arr.length);  // O(n)

// Fill
Arrays.fill(arr, value);               // O(n)
```

### String Operations
```java
// Length
int len = s.length();                  // O(1)

// Char at
char c = s.charAt(i);                  // O(1)

// Substring
String sub = s.substring(start, end);  // O(end-start)

// Split
String[] parts = s.split(",");         // O(n)

// Reverse
String rev = new StringBuilder(s).reverse().toString();  // O(n)
```

### HashMap Operations
```java
// Put/Get
map.put(key, value);                   // O(1) average
value = map.get(key);                  // O(1) average

// Contains
boolean exists = map.containsKey(key);  // O(1) average

// Remove
map.remove(key);                        // O(1) average

// Size
int size = map.size();                  // O(1)
```

### Stack Operations
```java
// Push
stack.push(item);                      // O(1)

// Pop
item = stack.pop();                     // O(1)

// Peek
item = stack.peek();                    // O(1)

// Empty
boolean empty = stack.isEmpty();        // O(1)
```

### Queue Operations
```java
// Offer
queue.offer(item);                     // O(1)

// Poll
item = queue.poll();                    // O(1)

// Peek
item = queue.peek();                    // O(1)

// Empty
boolean empty = queue.isEmpty();        // O(1)
```

---

## Final Interview Strategy

### 1. Problem Understanding (2-3 minutes)
- Read problem carefully
- Identify input/output format
- Note constraints
- Ask clarifying questions

### 2. Approach Selection (2-3 minutes)
- Identify problem pattern
- Choose appropriate data structure
- Select algorithm
- Estimate complexity

### 3. Implementation (10-15 minutes)
- Write clean, readable code
- Handle edge cases
- Add comments if needed
- Test with examples

### 4. Optimization (2-3 minutes)
- Review time/space complexity
- Consider edge cases
- Discuss trade-offs
- Mention alternative approaches

### 5. Communication (Throughout)
- Explain your thought process
- Justify your choices
- Discuss complexity
- Handle feedback gracefully

---

**Remember:** Practice makes perfect! The key is to recognize patterns quickly and apply the right approach systematically.
Unweighted graph? → BFS
Weighted graph? → Dijkstra
Negative weights? → Bellman-Ford
All pairs? → Floyd-Warshall
```

#### Range Problems
```
Static array? → Prefix Sum
Point updates? → BIT/Segment Tree
Range updates? → Difference Array + BIT/Segment Tree
```

---

## Problem Identification Checklist

### Step 1: Identify Input Type
- [ ] Array/List
- [ ] String
- [ ] Linked List
- [ ] Tree
- [ ] Graph
- [ ] Matrix

### Step 2: Identify Operation Type
- [ ] Search/Find
- [ ] Sort/Order
- [ ] Insert/Delete
- [ ] Update/Modify
- [ ] Calculate/Compute
- [ ] Optimize/Maximize

### Step 3: Identify Constraints
- [ ] Time complexity requirements
- [ ] Space complexity requirements
- [ ] Static vs Dynamic data
- [ ] Single vs Multiple queries

### Step 4: Choose Data Structure
- [ ] Array (O(1) access, O(n) operations)
- [ ] HashMap (O(1) average lookup)
- [ ] Stack (LIFO operations)
- [ ] Queue (FIFO operations)
- [ ] Heap (Priority operations)
- [ ] Tree (Hierarchical data)
- [ ] Graph (Network data)
- [ ] BIT/Segment Tree (Range queries)

### Step 5: Choose Algorithm
- [ ] Linear Search
- [ ] Binary Search
- [ ] Two Pointers
- [] Sliding Window
- [] BFS/DFS
- [] Greedy
- [] DP
- [] Divide and Conquer

---

## Common Interview Red Flags

### When NOT to Use:
- **Brute Force** for large inputs (O(n²) or worse)
- **Recursion** for deep recursion (stack overflow)
- **Sorting** when order doesn't matter
- **Complex DS** when simple solution exists
- **Multiple DS** when single DS suffices

### When to Optimize:
- **O(n²)** → Look for O(n log n) or O(n)
- **O(n log n)** → Look for O(n) if possible
- **O(n)** → Already optimal for most cases
- **Space O(n)** → Look for O(1) if possible

---

## Final Cheat Sheet

### Quick Decision Matrix

```
INPUT TYPE + OPERATION → DATA STRUCTURE + ALGORITHM
----------------------------------------------------
Array + Search → Binary Search (if sorted) or HashMap
Array + Subarray → Prefix Sum or Sliding Window
Array + Sort → Arrays.sort() (O(n log n))
Array + Top-k → Heap (O(n log k))
String + Pattern → KMP or Rabin-Karp
String + Unique → Sliding Window + HashMap
String + Palindrome → Two Pointers
LinkedList + Reverse → Three Pointers
LinkedList + Cycle → Fast/Slow Pointers
Tree + Traversal → DFS/Recursion
Tree + BST → BST Properties
Tree + LCA → Parent Pointers or Single Traversal
Graph + Shortest Path → BFS (unweighted) or Dijkstra (weighted)
Graph + Connectivity → Union-Find or DFS
Graph + Cycle → DFS with Parent Tracking
Graph + Topological → Kahn's Algorithm
Greedy + Intervals → Sort by End Time
DP + Optimization → Bottom-up Table with Space Optimization
Recursion + Permutations → Backtracking
Range Query → Prefix Sum (static) or BIT/Segment Tree (dynamic)
```

### Company Priority Matrix

```
WITCH: Fundamentals > Speed > Clean Code
FAANG: Optimization > Scalability > Advanced Algorithms

WITCH Focus: Arrays, Strings, Basic DS, Simple Algorithms
FAANG Focus: Advanced DS, Complex Algorithms, Optimization
```

---

## Practice Recommendations

### For WITCH Companies
1. Master array operations (search, sort, two pointers)
2. Practice string manipulation (palindrome, anagram)
3. Learn linked list basics (reverse, cycle)
4. Understand tree traversals (inorder, preorder, level order)
5. Know basic sorting algorithms
6. Practice greedy problems
7. Implement recursion basics

### For FAANG Companies
1. Master advanced array techniques (range queries, optimization)
2. Learn string algorithms (KMP, Rabin-Karp, advanced patterns)
3. Understand tree algorithms (LCA, balanced trees, serialization)
4. Master graph algorithms (shortest paths, network flow)
5. Practice DP extensively (all classic patterns)
6. Learn advanced data structures (BIT, Segment Tree, Trie)
7. Understand heap applications and streaming algorithms

---

**Remember:** The key is to quickly identify the problem pattern and choose the most appropriate data structure and algorithm. Practice makes perfect!
