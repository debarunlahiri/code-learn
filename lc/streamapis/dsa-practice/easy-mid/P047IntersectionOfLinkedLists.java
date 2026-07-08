import java.util.*;

/**
 * P047. Intersection Of Linked Lists. This is a easy-to-mid Java DSA coding
 * problem commonly practiced
 * for service based company coding rounds. Given the input described by the
 * method signature,
 * implement the required operation efficiently and return the expected result.
 * Handle normal edge
 * cases such as empty collections, duplicate values, boundary indexes, and null
 * child pointers when
 * the data structure allows them. Prefer the standard optimal approach used in
 * coding rounds, and keep
 * the implementation readable for revision.
 */
public final class P047IntersectionOfLinkedLists {

    private P047IntersectionOfLinkedLists() {
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

    public ListNode getIntersectionNode(ListNode a, ListNode b) {
        ListNode p = a, q = b;
        while (p != q) {
    p = p == null ? b : p.next;
    q = q == null ? a : q.next;
        }
        return p;
    }

}
