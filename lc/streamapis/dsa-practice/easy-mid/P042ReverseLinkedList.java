import java.util.*;

/**
 * P042. Reverse Linked List. Given the input described by the method signature,
 * implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P042ReverseLinkedList {

    private P042ReverseLinkedList() {
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

    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        while (head != null) {
    ListNode next = head.next;
    head.next = prev;
    prev = head;
    head = next;
        }
        return prev;
    }

}
