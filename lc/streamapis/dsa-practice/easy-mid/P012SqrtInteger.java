import java.util.*;

/**
 * P012. Sqrt Integer. This is a easy-to-mid Java DSA coding problem commonly
 * practiced for service
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
public final class P012SqrtInteger {

    private P012SqrtInteger() {
    }

    public int mySqrt(int x) {
        int left = 1, right = x, ans = 0;
        while (left <= right) {
    int mid = left + (right - left) / 2;
    if (mid <= x / mid) {
        ans = mid;
        left = mid + 1;
    } else
        right = mid - 1;
        }
        return ans;
    }

}
