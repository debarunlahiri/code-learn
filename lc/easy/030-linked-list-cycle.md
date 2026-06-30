# 030. Linked List Cycle

Platform: LeetCode  
Difficulty: Easy  
Topic: Linked List, Two Pointers

## Problem Statement

Given the head of a linked list, return `true` if the linked list has a cycle.

A cycle exists when a node can be reached again by continuously following `next` pointers.

## Constraints

- The number of nodes is between `0` and `10^4`.
- `-10^5 <= Node.val <= 10^5`

## Example

Input:

```text
head = [3, 2, 0, -4], pos = 1
```

Output:

```text
true
```

## Brute Force Approach

Store visited nodes in a set. If a node appears again, there is a cycle.

```java
import java.util.HashSet;
import java.util.Set;

class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
        next = null;
    }
}

public class Solution {
    public boolean hasCycle(ListNode head) {
        Set<ListNode> visited = new HashSet<>();

        while (head != null) {
            if (visited.contains(head)) {
                return true;
            }

            visited.add(head);
            head = head.next;
        }

        return false;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Use slow and fast pointers. If there is a cycle, the fast pointer will eventually meet the slow pointer.

```java
class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
        next = null;
    }
}

public class Solution {
    public boolean hasCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast) {
                return true;
            }
        }

        return false;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

