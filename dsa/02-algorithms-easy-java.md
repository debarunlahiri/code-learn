# Algorithms in Java (Easy)

Goal: Learn core algorithms with simple logic, clear intuition, and easy Java code.

---

## 1. Linear Search

### What it does
Scans the array from left to right until the target is found.

### Why it matters
- Simplest search algorithm
- Works on unsorted data
- Foundation for understanding search

### Intuition
Imagine looking for your friend in a line. You start from the first person and ask each one until you find your friend.

### When to use
- Array is small
- Data is unsorted
- Simplicity matters over speed

### Time complexity
- Worst case: `O(n)`
- Best case: `O(1)` (target at first position)

### Edge cases
- Empty array
- Target not present
- Multiple occurrences (returns first)

### Java code
```java
public class LinearSearch {
    static int findIndex(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] arr = {7, 3, 9, 1};
        System.out.println(findIndex(arr, 9)); // 2
    }
}
```

---

## 2. Binary Search

### What it does
Works on sorted array. Compare middle value and discard half each step.

### Why it matters
- Extremely fast for sorted data
- Logarithmic time complexity
- Basis for many advanced algorithms

### Intuition
Think of finding a word in a dictionary. Open to the middle, see if your word comes before or after, then focus only on that half and repeat.

### When to use
- Data is sorted
- Fast search is needed
- No frequent insertions/deletions

### Time complexity
- Worst case: `O(log n)`
- Best case: `O(1)` (target at middle)

### Edge cases
- Empty array
- Target smaller/larger than all elements
- Integer overflow when calculating mid (use `mid = left + (right - left) / 2`)

### Java code
```java
public class BinarySearch {
    static int findIndex(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // avoids overflow

            if (arr[mid] == target) return mid;
            if (arr[mid] < target) left = mid + 1;
            else right = mid - 1;
        }

        return -1;
    }

    public static void main(String[] args) {
        int[] sorted = {2, 4, 6, 8, 10, 12};
        System.out.println(findIndex(sorted, 8)); // 3
    }
}
```

---

## 3. Bubble Sort

### What it does
Repeatedly compare adjacent values and swap if wrong order.

### Why it matters
- Easiest sorting algorithm to understand
- Demonstrates sorting concepts
- Good for teaching, not for production

### Intuition
Imagine bubbles in water rising to the surface. In each pass, the largest unsorted element "bubbles up" to its correct position.

### When to use
- Educational purposes
- Very small datasets
- When simplicity is more important than performance

### Time complexity
- Worst case: `O(n²)`
- Best case (already sorted): `O(n)` with optimization

### Edge cases
- Empty or single-element array
- All elements equal
- Already sorted (detect and stop early)

### Java code
```java
public class BubbleSort {
    static void sort(int[] arr) {
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // swap
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            // If no swapping, array is sorted
            if (!swapped) break;
        }
    }

    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        sort(arr);
        System.out.println(java.util.Arrays.toString(arr)); // [11, 12, 22, 25, 34, 64, 90]
    }
}
```

### Use when
Learning sorting basics. Not good for large input.

### Time complexity
- Worst/Average: `O(n^2)`

### Java code
```java
import java.util.Arrays;

public class BubbleSort {
    static void sort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }

            if (!swapped) break;
        }
    }

    public static void main(String[] args) {
        int[] arr = {5, 1, 4, 2, 8};
        sort(arr);
        System.out.println(Arrays.toString(arr)); // [1, 2, 4, 5, 8]
    }
}
```

---

## 4. Merge Sort

### Idea
Divide array into halves, sort each half, then merge.

### Use when
Need reliable `O(n log n)` sorting.

### Time complexity
- Worst/Average: `O(n log n)`
- Extra space: `O(n)`

### Java code
```java
import java.util.Arrays;

public class MergeSort {
    static void sort(int[] arr, int left, int right) {
        if (left >= right) return;

        int mid = left + (right - left) / 2;
        sort(arr, left, mid);
        sort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }

    static void merge(int[] arr, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left;
        int j = mid + 1;
        int k = 0;

        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) temp[k++] = arr[i++];
            else temp[k++] = arr[j++];
        }
        while (i <= mid) temp[k++] = arr[i++];
        while (j <= right) temp[k++] = arr[j++];

        for (int x = 0; x < temp.length; x++) {
            arr[left + x] = temp[x];
        }
    }

    public static void main(String[] args) {
        int[] arr = {9, 4, 7, 3, 10, 5};
        sort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr)); // [3, 4, 5, 7, 9, 10]
    }
}
```

---

## 5. Quick Sort

### Idea
Pick a pivot, place smaller values left and larger values right, then recurse.

### Use when
Fast average-case sorting with in-place behavior.

### Time complexity
- Average: `O(n log n)`
- Worst: `O(n^2)`

### Java code
```java
import java.util.Arrays;

public class QuickSort {
    static void sort(int[] arr, int low, int high) {
        if (low >= high) return;

        int pivotIndex = partition(arr, low, high);
        sort(arr, low, pivotIndex - 1);
        sort(arr, pivotIndex + 1, high);
    }

    static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    public static void main(String[] args) {
        int[] arr = {10, 7, 8, 9, 1, 5};
        sort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr)); // [1, 5, 7, 8, 9, 10]
    }
}
```

---

## 6. BFS (Breadth-First Search)

### Idea
Visit graph level by level using a queue.

### Use when
Need shortest path in unweighted graph, or level order traversal.

### Time complexity
- `O(V + E)` where `V` is vertices and `E` is edges.

### Java code
```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BFSGraph {
    static void bfs(int start, List<List<Integer>> graph) {
        boolean[] visited = new boolean[graph.size()];
        Queue<Integer> queue = new LinkedList<>();

        visited[start] = true;
        queue.offer(start);

        while (!queue.isEmpty()) {
            int node = queue.poll();
            System.out.print(node + " ");

            for (int next : graph.get(node)) {
                if (!visited[next]) {
                    visited[next] = true;
                    queue.offer(next);
                }
            }
        }
    }

    public static void main(String[] args) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < 5; i++) graph.add(new ArrayList<>());

        graph.get(0).addAll(Arrays.asList(1, 2));
        graph.get(1).addAll(Arrays.asList(0, 3, 4));
        graph.get(2).add(0);
        graph.get(3).add(1);
        graph.get(4).add(1);

        bfs(0, graph); // 0 1 2 3 4
    }
}
```

---

## 7. DFS (Depth-First Search)

### Idea
Go deep first, then backtrack. Usually done with recursion.

### Use when
Path checking, connected components, cycle detection (with extra logic).

### Time complexity
- `O(V + E)`

### Java code
```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DFSGraph {
    static void dfs(int node, List<List<Integer>> graph, boolean[] visited) {
        visited[node] = true;
        System.out.print(node + " ");

        for (int next : graph.get(node)) {
            if (!visited[next]) {
                dfs(next, graph, visited);
            }
        }
    }

    public static void main(String[] args) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < 5; i++) graph.add(new ArrayList<>());

        graph.get(0).addAll(Arrays.asList(1, 2));
        graph.get(1).addAll(Arrays.asList(0, 3, 4));
        graph.get(2).add(0);
        graph.get(3).add(1);
        graph.get(4).add(1);

        boolean[] visited = new boolean[5];
        dfs(0, graph, visited); // 0 1 3 4 2
    }
}
```
