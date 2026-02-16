# DSA Practice Questions (Hard) with Java Answers

---

## 1. Trapping Rain Water

```java
public class TrappingRainWater {
    static int trap(int[] h) {
        int left = 0, right = h.length - 1;
        int leftMax = 0, rightMax = 0, water = 0;

        while (left < right) {
            if (h[left] < h[right]) {
                if (h[left] >= leftMax) leftMax = h[left];
                else water += leftMax - h[left];
                left++;
            } else {
                if (h[right] >= rightMax) rightMax = h[right];
                else water += rightMax - h[right];
                right--;
            }
        }
        return water;
    }
}
```

## 2. Median of Two Sorted Arrays

```java
public class MedianTwoSortedArrays {
    static double findMedianSortedArrays(int[] a, int[] b) {
        if (a.length > b.length) return findMedianSortedArrays(b, a);

        int n1 = a.length, n2 = b.length;
        int low = 0, high = n1;

        while (low <= high) {
            int cut1 = (low + high) / 2;
            int cut2 = (n1 + n2 + 1) / 2 - cut1;

            int l1 = (cut1 == 0) ? Integer.MIN_VALUE : a[cut1 - 1];
            int l2 = (cut2 == 0) ? Integer.MIN_VALUE : b[cut2 - 1];
            int r1 = (cut1 == n1) ? Integer.MAX_VALUE : a[cut1];
            int r2 = (cut2 == n2) ? Integer.MAX_VALUE : b[cut2];

            if (l1 <= r2 && l2 <= r1) {
                if ((n1 + n2) % 2 == 0) return (Math.max(l1, l2) + Math.min(r1, r2)) / 2.0;
                return Math.max(l1, l2);
            } else if (l1 > r2) {
                high = cut1 - 1;
            } else {
                low = cut1 + 1;
            }
        }

        return 0.0;
    }
}
```

## 3. Merge K Sorted Lists (Priority Queue)

```java
import java.util.PriorityQueue;

public class MergeKSortedLists {
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    static ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>((x, y) -> x.val - y.val);

        for (ListNode node : lists) {
            if (node != null) pq.offer(node);
        }

        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;

        while (!pq.isEmpty()) {
            ListNode node = pq.poll();
            tail.next = node;
            tail = tail.next;
            if (node.next != null) pq.offer(node.next);
        }

        return dummy.next;
    }
}
```

## 4. Word Ladder (Shortest Transformation)

```java
import java.util.*;

public class WordLadder {
    static int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> dict = new HashSet<>(wordList);
        if (!dict.contains(endWord)) return 0;

        Queue<String> queue = new LinkedList<>();
        queue.offer(beginWord);
        int level = 1;

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                String word = queue.poll();
                if (word.equals(endWord)) return level;

                char[] arr = word.toCharArray();
                for (int p = 0; p < arr.length; p++) {
                    char old = arr[p];
                    for (char c = 'a'; c <= 'z'; c++) {
                        arr[p] = c;
                        String next = new String(arr);
                        if (dict.remove(next)) {
                            queue.offer(next);
                        }
                    }
                    arr[p] = old;
                }
            }

            level++;
        }

        return 0;
    }
}
```
