import java.util.*;

/**
 * P087. Find Median Data Stream. Given the input described by the method
 * signature, implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P087FindMedianDataStream {

    private P087FindMedianDataStream() {
    }

    class MedianFinder {
        private final PriorityQueue<Integer> small = new PriorityQueue<>(Comparator.reverseOrder());
        private final PriorityQueue<Integer> large = new PriorityQueue<>();

        public void addNum(int num) {
    small.offer(num);
    large.offer(small.poll());
    if (large.size() > small.size())
        small.offer(large.poll());
        }

        public double findMedian() {
    return small.size() > large.size() ? small.peek() : (small.peek() + large.peek()) / 2.0;
        }
    }

}
