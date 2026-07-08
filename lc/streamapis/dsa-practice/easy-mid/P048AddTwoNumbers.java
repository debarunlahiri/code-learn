import java.util.*;

/**
 * P048. Add Two Numbers. This is a easy-to-mid Java DSA coding problem commonly
 * practiced for service
 * based company coding rounds. Given the input described by the method
 * signature, implement the
 * required operation efficiently and return the expected result. Handle normal
 * edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P048AddTwoNumbers {

    private P048AddTwoNumbers() {
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

    public ListNode addTwoNumbers(ListNode a, ListNode b) {
        ListNode dummy = new ListNode(0), tail = dummy;
        int carry = 0;
        while (a != null || b != null || carry != 0) {
    int sum = carry + (a == null ? 0 : a.val) + (b == null ? 0 : b.val);
    carry = sum / 10;
    tail.next = new ListNode(sum % 10);
    tail = tail.next;
    if (a != null)
        a = a.next;
    if (b != null)
        b = b.next;
        }
        return dummy.next;
    }

}
