import java.util.*;

/**
 * P119. Decode String. This is a mid-to-hard Java DSA coding problem commonly
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
public final class P119DecodeString {

    private P119DecodeString() {
    }

    public String decodeString(String s) {
        Deque<Integer> counts = new ArrayDeque<>();
        Deque<StringBuilder> stack = new ArrayDeque<>();
        StringBuilder cur = new StringBuilder();
        int num = 0;
        for (char ch : s.toCharArray()) {
            if (Character.isDigit(ch))
                num = num * 10 + ch - '0';
            else if (ch == '[') {
                counts.push(num);
                stack.push(cur);
                cur = new StringBuilder();
                num = 0;
            } else if (ch == ']') {
                StringBuilder prev = stack.pop();
                int repeat = counts.pop();
                while (repeat-- > 0)
                    prev.append(cur);
                cur = prev;
            } else
                cur.append(ch);
        }
        return cur.toString();
    }
}
