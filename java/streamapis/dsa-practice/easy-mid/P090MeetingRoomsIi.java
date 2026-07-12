import java.util.*;

/**
 * P090. Meeting Rooms Ii. This is a easy-to-mid Java DSA coding problem
 * commonly practiced for service
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
public final class P090MeetingRoomsIi {

    private P090MeetingRoomsIi() {
    }

    public int minMeetingRooms(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        PriorityQueue<Integer> ends = new PriorityQueue<>();
        for (int[] in : intervals) {
    if (!ends.isEmpty() && ends.peek() <= in[0])
        ends.poll();
    ends.offer(in[1]);
        }
        return ends.size();
    }

}
