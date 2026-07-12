import java.util.*;

/**
 * P120. Asteroid Collision. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Implement the required method using an
 * efficient algorithm, not
 * brute force where a better standard approach exists. The solution should
 * handle boundary cases,
 * duplicate values, disconnected states, and large inputs according to the
 * method signature. Return
 * the final computed value or data structure exactly as the platform-style
 * method expects.
 */
public final class P120AsteroidCollision {

    private P120AsteroidCollision() {
    }

    public int[] asteroidCollision(int[] asteroids) {
        Deque<Integer> stack = new ArrayDeque<>();
        outer: for (int a : asteroids) {
            while (!stack.isEmpty() && a < 0 && stack.peek() > 0) {
                int diff = stack.peek() + a;
                if (diff < 0)
                    stack.pop();
                else {
                    if (diff == 0)
                        stack.pop();
                    continue outer;
                }
            }
            stack.push(a);
        }
        int[] ans = new int[stack.size()];
        for (int i = ans.length - 1; i >= 0; i--)
            ans[i] = stack.pop();
        return ans;
    }
}
