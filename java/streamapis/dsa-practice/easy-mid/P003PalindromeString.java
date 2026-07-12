import java.util.*;

/**
 * P003. Palindrome String. Given the input described by the method signature,
 * implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P003PalindromeString {

    private P003PalindromeString() {
    }

    public boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(s.charAt(left)))
                left++;
            while (left < right && !Character.isLetterOrDigit(s.charAt(right)))
                right--;
            if (Character.toLowerCase(s.charAt(left++)) != Character.toLowerCase(s.charAt(right--)))
                return false;
        }
        return true;
    }

}
