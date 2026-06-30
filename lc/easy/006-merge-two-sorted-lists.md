# 006. Merge Two Sorted Lists

Platform: LeetCode  
Difficulty: Easy  
Topic: Linked List

## Problem Statement

You are given the heads of two sorted linked lists, `list1` and `list2`.

Merge both lists into one sorted linked list and return the head of the merged list.

## Constraints

- The number of nodes in both lists is between `0` and `50`.
- `-100 <= Node.val <= 100`
- Both lists are sorted in non-decreasing order.

## Example

Input:

```text
list1 = [1, 2, 4]
list2 = [1, 3, 4]
```

Output:

```text
[1, 1, 2, 3, 4, 4]
```

## Brute Force Approach

Put all values into an array, sort it, and create a new linked list from the sorted values.

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}

class Solution {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        List<Integer> values = new ArrayList<>();

        while (list1 != null) {
            values.add(list1.val);
            list1 = list1.next;
        }

        while (list2 != null) {
            values.add(list2.val);
            list2 = list2.next;
        }

        Collections.sort(values);

        ListNode dummy = new ListNode(0);
        ListNode current = dummy;

        for (int value : values) {
            current.next = new ListNode(value);
            current = current.next;
        }

        return dummy.next;
    }
}
```

Complexity:

- Time: `O((n + m) log(n + m))`
- Space: `O(n + m)`

## Best Approach

Use two pointers. Since both lists are already sorted, always attach the smaller current node to the result.

```java
class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}

class Solution {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;

        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                current.next = list1;
                list1 = list1.next;
            } else {
                current.next = list2;
                list2 = list2.next;
            }

            current = current.next;
        }

        if (list1 != null) {
            current.next = list1;
        } else {
            current.next = list2;
        }

        return dummy.next;
    }
}
```

Complexity:

- Time: `O(n + m)`
- Space: `O(1)`

