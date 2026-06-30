# 033. Reverse Linked List

Platform: LeetCode  
Difficulty: Easy  
Topic: Linked List

## Problem Statement

Given the head of a singly linked list, reverse the list and return the new head.

## Constraints

- The number of nodes is between `0` and `5000`.
- `-5000 <= Node.val <= 5000`

## Example

Input:

```text
head = [1, 2, 3, 4, 5]
```

Output:

```text
[5, 4, 3, 2, 1]
```

## Brute Force Approach

Store values in a list, then create a new linked list in reverse order.

```java
import java.util.ArrayList;
import java.util.List;

class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }
}

class Solution {
    public ListNode reverseList(ListNode head) {
        List<Integer> values = new ArrayList<>();

        while (head != null) {
            values.add(head.val);
            head = head.next;
        }

        ListNode dummy = new ListNode(0);
        ListNode current = dummy;

        for (int i = values.size() - 1; i >= 0; i--) {
            current.next = new ListNode(values.get(i));
            current = current.next;
        }

        return dummy.next;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Reverse pointers one by one.

```java
class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }
}

class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode previous = null;
        ListNode current = head;

        while (current != null) {
            ListNode nextNode = current.next;
            current.next = previous;
            previous = current;
            current = nextNode;
        }

        return previous;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

