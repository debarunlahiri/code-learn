import java.util.*;

/**
 * P002. Reverse String. Given the input described by the method signature, implement the
 * required operation efficiently and return the expected result. Handle normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding rounds, and keep the
 * implementation readable for revision.
 */
public final class P002ReverseString {

    private P002ReverseString() {
    }

    public void reverseString(char[] s) {
        int left = 0, right = s.length - 1;
        while (left < right) {
            char temp = s[left];
            s[left++] = s[right];
            s[right--] = temp;
        }
    }

}
