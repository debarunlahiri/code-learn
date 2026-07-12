import java.util.*;

/**
 * P055. Evaluate Reverse Polish Notation. This is a easy-to-mid Java DSA coding
 * problem commonly
 * practiced for service based company coding rounds. Given the input described
 * by the method
 * signature, implement the required operation efficiently and return the
 * expected result. Handle
 * normal edge cases such as empty collections, duplicate values, boundary
 * indexes, and null child
 * pointers when the data structure allows them. Prefer the standard optimal
 * approach used in coding
 * rounds, and keep the implementation readable for revision.
 */
public final class P055EvaluateReversePolishNotation {

    private P055EvaluateReversePolishNotation() {
    }

    public int evalRPN(String[] tokens) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (String t : tokens) {
    if ("+-*/".contains(t) && t.length() == 1) {
        int b = stack.pop(), a = stack.pop();
        stack.push(t.equals("+") ? a + b : t.equals("-") ? a - b : t.equals("*") ? a * b : a / b);
    } else
        stack.push(Integer.parseInt(t));
        }
        return stack.pop();
    }

}
