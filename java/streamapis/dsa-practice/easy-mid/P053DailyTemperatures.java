import java.util.*;

/**
 * P053. Daily Temperatures. Given the input described by the method signature,
 * implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P053DailyTemperatures {

    private P053DailyTemperatures() {
    }

    public int[] dailyTemperatures(int[] temperatures) {
        int[] ans = new int[temperatures.length];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < temperatures.length; i++) {
    while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
        int prev = stack.pop();
        ans[prev] = i - prev;
    }
    stack.push(i);
        }
        return ans;
    }

}
