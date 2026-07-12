import java.util.*;

/**
 * P041. Linked List Cycle. Given the input described by the method signature,
 * implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P041LinkedListCycle {

    private P041LinkedListCycle() {
    }

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
    this.val = val;
        }

        ListNode(int val, ListNode next) {
    this.val = val;
    this.next = next;
        }
    }

    public boolean hasCycle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;
    if (slow == fast)
        return true;
        }
        return false;
    }

}
