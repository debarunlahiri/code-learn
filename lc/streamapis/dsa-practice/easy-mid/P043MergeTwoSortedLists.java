import java.util.*;

/**
 * P043. Merge Two Sorted Lists. Given the input described by the method
 * signature, implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P043MergeTwoSortedLists {

    private P043MergeTwoSortedLists() {
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

    public ListNode mergeTwoLists(ListNode a, ListNode b) {
        ListNode dummy = new ListNode(0), tail = dummy;
        while (a != null && b != null) {
    if (a.val <= b.val) {
        tail.next = a;
        a = a.next;
    } else {
        tail.next = b;
        b = b.next;
    }
    tail = tail.next;
        }
        tail.next = a != null ? a : b;
        return dummy.next;
    }

}
