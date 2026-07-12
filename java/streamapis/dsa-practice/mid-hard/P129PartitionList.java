import java.util.*;

/**
 * P129. Partition List. This is a mid-to-hard Java DSA coding problem commonly
 * seen in service based
 * company technical rounds. Implement the required method using an efficient
 * algorithm, not brute
 * force where a better standard approach exists. The solution should handle
 * boundary cases, duplicate
 * values, disconnected states, and large inputs according to the method
 * signature. Return the final
 * computed value or data structure exactly as the platform-style method
 * expects.
 */
public final class P129PartitionList {

    private P129PartitionList() {
    }

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }
    }

    public ListNode partition(ListNode head, int x) {
        ListNode small = new ListNode(0), big = new ListNode(0), a = small, b = big;
        while (head != null) {
            if (head.val < x) {
                a.next = head;
                a = a.next;
            } else {
                b.next = head;
                b = b.next;
            }
            head = head.next;
        }
        b.next = null;
        a.next = big.next;
        return small.next;
    }
}
