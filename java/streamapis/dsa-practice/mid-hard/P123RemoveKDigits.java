import java.util.*;

/**
 * P123. Remove K Digits. This is a mid-to-hard Java DSA coding problem commonly
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
public final class P123RemoveKDigits {

    private P123RemoveKDigits() {
    }

    public String removeKdigits(String num, int k) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : num.toCharArray()) {
            while (k > 0 && !stack.isEmpty() && stack.peek() > c) {
                stack.pop();
                k--;
            }
            stack.push(c);
        }
        while (k-- > 0)
            stack.pop();
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty())
            sb.append(stack.removeLast());
        while (sb.length() > 1 && sb.charAt(0) == '0')
            sb.deleteCharAt(0);
        return sb.length() == 0 ? "0" : sb.toString();
    }
}
