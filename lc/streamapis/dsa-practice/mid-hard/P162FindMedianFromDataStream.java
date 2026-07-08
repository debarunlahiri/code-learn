import java.util.*;

/**
 * P162. Find Median From Data Stream. This is a mid-to-hard Java DSA coding
 * problem commonly seen in
 * service based company technical rounds. Read the full input from the method
 * parameters, choose the
 * expected optimal data structure or algorithm, handle edge cases such as empty
 * inputs and duplicates,
 * and return the exact platform-style output.
 */
public final class P162FindMedianFromDataStream {

    private P162FindMedianFromDataStream() {
    }

    private final PriorityQueue<Integer> left = new PriorityQueue<>(Comparator.reverseOrder());
    private final PriorityQueue<Integer> right = new PriorityQueue<>();

    public void addNum(int num) {
        left.offer(num);
        right.offer(left.poll());
        if (right.size() > left.size())
            left.offer(right.poll());
    }

    public double findMedian() {
        return left.size() > right.size() ? left.peek() : (left.peek() + right.peek()) / 2.0;
    }
}
