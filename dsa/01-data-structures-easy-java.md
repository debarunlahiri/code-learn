# Data Structures in Java (Easy)

Goal: Understand what each structure does, when to use it, and the simplest Java usage.

---

## 1. Array

### What it is
An array stores values in a fixed-size box.

### Why it matters
- Fastest way to access by index (`O(1)`)
- Memory layout is contiguous (cache-friendly)
- Base for many other structures

### Intuition
Think of a row of numbered lockers. Each locker has a fixed position. You can instantly go to locker #5, but you can’t add more lockers once built.

### When to use
- Size is known beforehand
- Need fast random access
- No frequent insertions/deletions

### Time complexity
- Read by index: `O(1)`
- Search: `O(n)`
- Insert/delete (except at end): `O(n)`

### Edge cases
- Empty array (`length = 0`)
- Single element
- Out-of-bounds access throws `ArrayIndexOutOfBoundsException`

### Java code
```java
import java.util.Arrays;

public class ArrayExample {
    public static void main(String[] args) {
        int[] numbers = {10, 20, 30, 40};

        System.out.println(numbers[2]); // 30
        numbers[1] = 25;
        System.out.println(Arrays.toString(numbers)); // [10, 25, 30, 40]
    }
}
```

---

## 2. ArrayList

### What it is
A dynamic array. Size grows automatically.

### Why it matters
- You get array-like access without fixed size
- Handles resizing internally
- Most commonly used list in interviews

### Intuition
Imagine a stretchable bag. It starts small, but when you add too many items, it magically expands (by copying to a bigger bag).

### When to use
- Do not know size in advance
- Need fast index access
- Frequent additions at the end

### Time complexity
- Add at end: `O(1)` average (amortized)
- Read by index: `O(1)`
- Remove by value/index: `O(n)`
- Insert at middle: `O(n)`

### Edge cases
- Initial capacity matters if you know size (use `new ArrayList<>(n)`)
- Removing while iterating can cause `ConcurrentModificationException`

### Java code
```java
import java.util.ArrayList;
import java.util.List;

public class ArrayListExample {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        names.add("Asha");
        names.add("Ravi");
        names.add("Neha");

        names.remove("Ravi");
        System.out.println(names); // [Asha, Neha]
    }
}
```

---

## 3. LinkedList

### What it is
A chain of nodes. Each node points to the next node.

### Why it matters
- Fast insert/delete at both ends (`O(1)`)
- No wasted capacity (unlike ArrayList)
- Good for queues and deques

### Intuition
Think of a train. Each car knows only the next car. You can quickly add/remove cars at the ends, but to find the 10th car, you must walk through all previous cars.

### When to use
- Many insert/delete operations at beginning/end
- Implementing queue/stack
- No random access needed

### Time complexity
- Access by index: `O(n)`
- Insert/delete at head/tail: `O(1)`
- Insert/delete at middle: `O(n)` (need to traverse)

### Edge cases
- `get(index)` is slow; avoid in loops
- `remove(Object)` needs traversal
- Not cache-friendly (non-contiguous)

### Java code
```java
import java.util.LinkedList;
import java.util.Queue;

public class LinkedListExample {
    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();
        list.addFirst(10);
        list.addLast(20);
        list.add(15);

        System.out.println(list); // [10, 20, 15]
        System.out.println(list.removeFirst()); // 10
    }
}
```

### Java code (simple custom node)
```java
public class LinkedListExample {
    static class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }

    public static void main(String[] args) {
        Node head = new Node(5);
        head.next = new Node(10);
        head.next.next = new Node(15);

        Node current = head;
        while (current != null) {
            System.out.print(current.value + " ");
            current = current.next;
        }
        // 5 10 15
    }
}
```

---

## 4. Stack (LIFO)

### What it is
Last In, First Out. Last pushed item is removed first.

### When to use
Undo feature, balanced brackets, recursion helper.

### Time complexity
- Push: `O(1)`
- Pop: `O(1)`

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

---

## 5. Queue (FIFO)

### What it is
First In, First Out. First inserted item is removed first.

### When to use
Task scheduling, BFS traversal.

### Time complexity
- Enqueue (`offer`): `O(1)`
- Dequeue (`poll`): `O(1)`

### Java code
```java
import java.util.LinkedList;
import java.util.Queue;

public class QueueExample {
    public static void main(String[] args) {
        Queue<Integer> queue = new LinkedList<>();

        queue.offer(100);
        queue.offer(200);
        queue.offer(300);

        System.out.println(queue.poll()); // 100
        System.out.println(queue.peek()); // 200
    }
}
```

---

## 6. HashMap

### What it is
Stores data in key-value form.

### When to use
Fast lookup by key (like dictionary/phonebook).

### Time complexity
- Put: `O(1)` average
- Get: `O(1)` average
- Remove: `O(1)` average

### Java code
```java
import java.util.HashMap;
import java.util.Map;

public class HashMapExample {
    public static void main(String[] args) {
        Map<String, Integer> marks = new HashMap<>();
        marks.put("Asha", 91);
        marks.put("Ravi", 86);

        System.out.println(marks.get("Asha"));         // 91
        System.out.println(marks.containsKey("Ravi")); // true
    }
}
```

---

## 7. Binary Tree (basic traversal)

### What it is
A tree where each node can have at most two children: left and right.

### When to use
Hierarchical data, fast search variants (BST), recursion practice.

### Java code (inorder traversal)
```java
public class BinaryTreeExample {
    static class TreeNode {
        int value;
        TreeNode left;
        TreeNode right;

        TreeNode(int value) {
            this.value = value;
        }
    }

    static void inorder(TreeNode root) {
        if (root == null) return;
        inorder(root.left);
        System.out.print(root.value + " ");
        inorder(root.right);
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(2);
        root.left = new TreeNode(1);
        root.right = new TreeNode(3);

        inorder(root); // 1 2 3
    }
}
```
