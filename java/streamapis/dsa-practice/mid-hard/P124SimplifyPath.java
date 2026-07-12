import java.util.*;

/**
 * P124. Simplify Unix Path. This is a mid-to-hard Java DSA coding problem
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
public final class P124SimplifyPath {

    private P124SimplifyPath() {
    }

    public String simplifyPath(String path) {
        Deque<String> stack = new ArrayDeque<>();
        for (String part : path.split("/")) {
            if (part.isEmpty() || part.equals("."))
                continue;
            if (part.equals("..")) {
                if (!stack.isEmpty())
                    stack.pop();
            } else
                stack.push(part);
        }
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty())
            sb.append('/').append(stack.removeLast());
        return sb.length() == 0 ? "/" : sb.toString();
    }
}
