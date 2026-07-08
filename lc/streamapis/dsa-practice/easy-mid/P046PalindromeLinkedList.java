import java.util.*;

/**
 * P046. Palindrome Linked List.  Given the input described by the method signature, implement
 * the required operation efficiently and return the expected result. Handle normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding rounds, and keep the
 * implementation readable for revision.
 */
public final class P046PalindromeLinkedList {

    private P046PalindromeLinkedList() {
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

    public boolean isPalindrome(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode second = reverse(slow);
        while (second != null) {
            if (head.val != second.val)
                return false;
            head = head.next;
            second = second.next;
        }
        return true;
    }

    private ListNode reverse(ListNode head) {
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
