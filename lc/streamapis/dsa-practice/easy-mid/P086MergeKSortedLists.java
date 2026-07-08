import java.util.*;

/**
 * P086. Merge K Sorted Lists. Given the input described by the method
 * signature, implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P086MergeKSortedLists {

    private P086MergeKSortedLists() {
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

    public ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.val));
        for (ListNode node : lists)
    if (node != null)
        pq.offer(node);
        ListNode dummy = new ListNode(0), tail = dummy;
        while (!pq.isEmpty()) {
    ListNode node = pq.poll();
    tail.next = node;
    tail = tail.next;
    if (node.next != null)
        pq.offer(node.next);
        }
        return dummy.next;
    }

}
