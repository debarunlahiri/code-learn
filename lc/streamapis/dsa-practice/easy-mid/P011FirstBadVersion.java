import java.util.*;

/**
 * P011. First Bad Version. Given the input described by the method signature,
 * implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P011FirstBadVersion {

    private P011FirstBadVersion() {
    }

    public int firstBadVersion(int n) {
        int left = 1, right = n;
        while (left < right) {
    int mid = left + (right - left) / 2;
    if (isBadVersion(mid))
        right = mid;
    else
        left = mid + 1;
        }
        return left;
    }

    private boolean isBadVersion(int version) {
        return false;
    }

}
