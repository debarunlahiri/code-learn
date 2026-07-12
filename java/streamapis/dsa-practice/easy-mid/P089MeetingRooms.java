import java.util.*;

/**
 * P089. Meeting Rooms.  Given the input described by the method
 * signature, implement the
 * required operation efficiently and return the expected result. Handle normal
 * edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P089MeetingRooms {

    private P089MeetingRooms() {
    }

    public boolean canAttendMeetings(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        for (int i = 1; i < intervals.length; i++)
    if (intervals[i][0] < intervals[i - 1][1])
        return false;
        return true;
    }

}
